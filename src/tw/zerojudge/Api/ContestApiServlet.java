package tw.zerojudge.Api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.TLDs.ContestTLD;
import tw.zerojudge.Api.ProblemApiServlet.ContentType;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.ContestsServlet;
import tw.zerojudge.Servlets.ShowContestServlet;
import tw.zerojudge.Servlets.ShowVClassServlet;
import tw.zerojudge.Servlets.UpdateContestServlet;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.Contestant.STATUS;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/Contest.api" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ContestApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7225313752592602748L;
	ObjectMapper mapper = new ObjectMapper(); 

	/**
	 * 交卷相關控管
	 * 
	 * @author jiangsir
	 *
	 */
	public class FinishContest {
		public void AccessFilter(HttpServletRequest request) throws AccessException {
			HttpSession session = request.getSession(false);
			OnlineUser onlineUser = UserFactory.getOnlineUser(session);
			String userid = request.getParameter("userid");
			if (userid == null || !userid.matches("[0-9]+")) {
				throw new AccessException("要踢掉的使用者編號有誤！(userid=" + userid + ")");
			}
			Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
			this.AccessFilter(onlineUser, contest, Integer.parseInt(userid));
		}

		/**
		 * 
		 * @param onlineUser
		 * @param contest
		 * @param kickuserid
		 * @throws AccessException
		 */
		public void AccessFilter(OnlineUser onlineUser, Contest contest, int userid) throws AccessException {
			if (onlineUser == null) {
				onlineUser = UserFactory.getNullOnlineUser();
			}
			if (!contest.getIsOwner(onlineUser)
					&& !(onlineUser.getJoinedcontestid().intValue() == contest.getId().intValue())) {
				throw new AccessException("您(" + onlineUser + ")沒有權限進行這個動作(交卷 Finish Contest)！");
			}
			if (!contest.isNullContest() && (contest.getIsRunning() || contest.getIsStarting())) {
			} else {
				throw new AccessException("目前 " + contest.getBundle_Contest(onlineUser.getSession_locale()) + " 的狀態("
						+ contest.getConteststatus() + ") 不能進行這個動作(交卷 Finish Contest)！");
			}
			Contestant contestant = contest.getContestantByUserid(userid);
			if (contestant.getStatus() == STATUS.kicked) {
				throw new AccessException("目前參與者的狀態(" + contestant.getStatus().getValue() + ") 請 "
						+ contest.getBundle_Contest(onlineUser.getSession_locale()) + "擁有者("
						+ contest.getOwner().getAccount() + ")將之回復，再進行(交卷 Finish Contest)！");
			}
		}

	}

	public class KickedUser {
		public void AccessFilter(HttpServletRequest request) throws AccessException {
			HttpSession session = request.getSession(false);
			OnlineUser onlineUser = UserFactory.getOnlineUser(session);
			String userid = request.getParameter("userid");
			if (userid == null || !userid.matches("[0-9]+")) {
				throw new AccessException("要踢掉的使用者編號有誤！(userid=" + userid + ")");
			}
			Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
			this.AccessFilter(onlineUser, contest, Integer.parseInt(userid));
		}

		/**
		 * 
		 * @param onlineUser
		 * @param contest
		 * @param kickuserid
		 * @throws AccessException
		 */
		public void AccessFilter(OnlineUser onlineUser, Contest contest, int kickuserid) throws AccessException {

			if (!onlineUser.getIsHigherEqualThanMANAGER() && !contest.getIsOwner(onlineUser)) {
				throw new AccessException("您(" + onlineUser + ")沒有權限進行這個動作(KickUser)！");
			}

		}
	}

	public class JoinContest {
		public void AccessFilter(HttpServletRequest request) throws AccessException {
			HttpSession session = request.getSession(false);
			OnlineUser onlineUser = UserFactory.getOnlineUser(session);
			Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
			this.AccessFilter(onlineUser, contest);
		}

		/**
		 * 
		 * @param onlineUser
		 * @param contest
		 * @throws AccessException
		 */
		public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
			new ShowContestServlet().AccessFilter(onlineUser, contest);

			if (onlineUser == null || onlineUser.isNullOnlineUser()) {
				throw new AccessException(
						"請先登入！才能參加 " + contest.getBundle_Contest(onlineUser.getSession_locale()) + "。");
			}
			if (onlineUser.getIsDEBUGGER()) {
				throw new AccessException("無法加入 " + contest.getBundle_Contest(onlineUser.getSession_locale()) + " !!");
			}
			/**
			 * @QX 20200512 暫時開放 Owner 開放主辦人加入隨堂測驗。
			 */
			if (contest.getIsStopped()) {
				throw new AccessException("" + contest.getBundle_Contest(onlineUser.getSession_locale()) + " 已經結束!!");
			}

		}

	}

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
	}

	public static enum doGet_ACTION {
		doExportContest, 
	}

	public static enum doPost_ACTION {
		doJoinContest, 
		doJoinContestByOnlineUser, 
		doPause, //
		doResume, //
		doStart, //
		doStop, //
		forcedstart, //
		forcedstop, //
		kick, 
		finish, 
		leave, //
		registed, //
		getCountdown, //
		checkContestForm, 
		jsonContestRanking, 
		countRejudgeContest, 
		doRejudgeContest, 
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		switch (doGet_ACTION.valueOf(action)) {
		case doExportContest:
			Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
			if (contest.visible_ExportContest(request.getSession(false))) {
				this.doGet_doExportContest(request, response);
			}
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		String action = request.getParameter("action");
		switch (doPost_ACTION.valueOf(action)) {
		case doJoinContest:
			this.doPost_doJoinContest(request);
			return;
		case doJoinContestByOnlineUser:
			this.doPost_doJoinContestByOnlineUser(request, response);
			return;
		case doPause:
			try {
				contest.doPause();
			} catch (DataException e) {
				e.printStackTrace();
				response.getWriter().print(e.getLocalizedMessage());
			}
			break;
		case doResume:
			try {
				contest.doResume();
			} catch (DataException e) {
				e.printStackTrace();
				response.getWriter().print(e.getLocalizedMessage());
			}
			break;
		case doStart:
			break;
		case doStop:
			break;
		case finish:
			this.doPost_finish(request, response);
			break;
		case forcedstart:
			this.doPost_forcedstart(request);
			break;
		case forcedstop:
			if (ContestTLD.isVisible_stopContest(onlineUser, contest)) {
				contest.doStop();
			}
			break;
		case getCountdown:
			response.getWriter().print(contest.getCountdown());
			return;
		case kick:
			this.doPost_kick(request);
			break;
		case leave:
			this.doPost_leave(request, response);
			break;
		case registed:
			this.doPost_registed(request);
			break;
		case checkContestForm:
			this.doPost_checkContestForm(request, response);
			break;
		case jsonContestRanking:
			this.doPost_jsonContestRanking(request, response);
			break;
		case countRejudgeContest:
			this.doPost_countRejudgeContest(request, response);
			break;
		case doRejudgeContest:
			this.doPost_doRejudgeContest(request, response);
			break;
		default:
			break;

		}
		response.getWriter().print("");
		return;
	}

	public void doPost_finish(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			new FinishContest().AccessFilter(request);
		} catch (AccessException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		new ContestService().doFinishByContestUserid(contest, Integer.parseInt(request.getParameter("userid")));

		//
		new UserService().doReload(request.getSession(false));

		if (contest.isVContest()) {
			response.sendRedirect(
					request.getContextPath() + ShowVClassServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]
							+ "?vclassid=" + contest.getVclassid());
			return;
		} else {
			response.sendRedirect(
					request.getContextPath() + ContestsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		}
	}

	/**
	 * 將參賽者踢出測驗, 使用者無法自行再次加入，排行榜上不會出現。
	 * 
	 * @param request
	 */
	public void doPost_kick(HttpServletRequest request) {
		try {
			this.new KickedUser().AccessFilter(request);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}

		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		Contestant contestant = contest.getContestantByUserid(request.getParameter("userid"));

		contestant.setStatus(Contestant.STATUS.kicked);
		contestant.setFinishtime(new Timestamp(System.currentTimeMillis()));
		new ContestantDAO().update(contestant);
		User user = contestant.getUser();
		if (user.getJoinedcontestid().intValue() == contest.getId()) {
			user.setJoinedcontestidToNONE();
		}
		new UserService().update(user);

	}

	public void doPost_leave(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		if (ContestTLD.isVisible_LeaveContest(onlineUser, contest)) {
			String userid = request.getParameter("userid");
			Contestant contestant = contest.getContestantByUserid(userid);
			contestant.setStatus(Contestant.STATUS.leave);
			new ContestantDAO().update(contestant);

			User user = contestant.getUser();
			if (user.getJoinedcontestid().intValue() == contest.getId()) {
				user.setJoinedcontestidToNONE();
			}
			new UserService().update(user);
		}
		if (contest.isVContest()) {
			response.sendRedirect(
					request.getContextPath() + ShowVClassServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]
							+ "?vclassid=" + contest.getVclassid());
			return;
		} else {
			response.sendRedirect(
					request.getContextPath() + ContestsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		}

	}

	public void doPost_forcedstart(HttpServletRequest request) {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		if (ContestTLD.isVisible_startContest(onlineUser, contest)) {
			contest.doRunning();
		}
	}

	public void doPost_registed(HttpServletRequest request) {
		String userid = request.getParameter("userid");
		if (userid == null || !userid.matches("[0-9]+")) {
			return;
		}
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		Contestant contestant = contest.getContestantByUserid(Integer.parseInt(userid));
		contestant.setStatus(Contestant.STATUS.registed);
		new ContestantDAO().update(contestant);
	}

	/**
	 * 匯出「競賽」
	 * 
	 * @param request
	 * @throws IOException
	 * @throws DataException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public void doGet_doExportContest(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, DataException, IOException {
		int contestid = Integer.parseInt(request.getParameter("contestid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		File file = new ContestDAO().ExportContestToJson(contestid, onlineUser);
		this.doGet_downloadFile(response, file, ContentType.octet);
	}

	public void doPost_doJoinContest(HttpServletRequest request) {
		new JoinContest().AccessFilter(request);

		String account = request.getParameter("account");
		String userpasswd = request.getParameter("passwd");
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		try {
			User user = new UserService().getUserByAccountPasswd(account, userpasswd);
			onlineUser = new UserService().doLogin(session, user);
			onlineUser.setSession_ip(request.getRemoteAddr());

			onlineUser.doJoinContest(contest);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

	public void doPost_doJoinContestByOnlineUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			new JoinContest().AccessFilter(request);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}

		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		try {
			onlineUser.setSession_ip(request.getRemoteAddr());

			onlineUser.doJoinContest(contest);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

	private void doPost_jsonContestRanking(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		response.getWriter().print(mapper.writeValueAsString(contest));
	}

	private void doPost_checkContestForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Contest checkContest = new Contest();

		try {
			checkContest.setConteststatus(request.getParameter("conteststatus"));
			checkContest.setTimelimit(request.getParameter("hours"), request.getParameter("mins"));
			checkContest.setTitle(request.getParameter("title"));
			checkContest.setProblemids(request.getParameter("problemids"));
			checkContest.setScores(request.getParameter("scores"));
			checkContest.setUserrules(request.getParameter("users"));
			new ContestService().checkInsert(checkContest);
		} catch (DataException e) {
			e.printStackTrace();
			response.getWriter().print(e.getLocalizedMessage());
			return;
		}
	}

	private void doPost_countRejudgeContest(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		response.getWriter().print(new SolutionDAO().countRejudgeContestByContestid(contest.getId()));
	}

	private void doPost_doRejudgeContest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			new UpdateContestServlet().AccessFilter(request);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}

		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		for (Solution solution : new SolutionService().getSolutionsByContest(contest, 0)) {
			solution.doRejudge(onlineUser);
		}
	}

	private void doGet_downloadFile(HttpServletResponse response, File file, ContentType type)
			throws UnsupportedEncodingException {
		String filename = file.getName();



		filename = java.net.URLEncoder.encode(filename, "UTF-8");

		response.reset(); 
		response.setContentType(type.getValue() + ";charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.write(FileUtils.readFileToString(file));
			writer.flush();
			writer.close();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 透過 POST 下載 JSON 檔案。
	 * 
	 * @param response
	 * @param file
	 * @param type
	 * @throws IOException
	 */
	private void doPost_downloadFile(HttpServletRequest request, HttpServletResponse response, File file,
			ContentType type) throws IOException {
		String filename = file.getName();


		response.reset(); 
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=\"test.json\"");
		try {
			InputStream in = new FileInputStream(file.getPath());

			OutputStream out = response.getOutputStream();

			ServletContext context = getServletConfig().getServletContext();
			String mimetype = context.getMimeType(file.getName());
			response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");

			response.setHeader("Content-Disposition", "attachment;filename=" + "test.json");
			int b = 0;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
