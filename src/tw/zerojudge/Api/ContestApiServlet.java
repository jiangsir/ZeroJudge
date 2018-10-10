package tw.zerojudge.Api;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.TLDs.ContestTLD;
import tw.jiangsir.Utils.TLDs.ContestantTLD;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.ContestsServlet;
import tw.zerojudge.Servlets.JoinContestServlet;
import tw.zerojudge.Servlets.ShowContestServlet;
import tw.zerojudge.Servlets.ShowVClassServlet;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = {"/Contest.api"})
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ContestApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7225313752592602748L;
	ObjectMapper mapper = new ObjectMapper(); 

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		String action = request.getParameter("action");
		switch (ACTION.valueOf(action)) {
			case doJoinContest :
				break;
			case doJoinContestByOnlineUser :
				break;
			case doPause :
				break;
			case doResume :
				break;
			case doStart :
				break;
			case doStop :
				break;
			case finish :
				break;
			case forcedstart :
				break;
			case forcedstop :
				break;
			case getCountdown :
				break;
			case kick :
				break;
			case leave :
				break;
			case registed :
				break;
			default :
				break;
		}
	}

	public static enum ACTION {
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
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		String action = request.getParameter("action");
		switch (ACTION.valueOf(action)) {
			case doJoinContest :
				new JoinContestServlet().AccessFilter(request);
				this.doPost_doJoinContest(request);
				return;
			case doJoinContestByOnlineUser :
				new JoinContestServlet().AccessFilter(request);
				this.doPost_doJoinContestByOnlineUser(request, response);
				return;
			case doPause :
				try {
					contest.doPause();
				} catch (DataException e) {
					e.printStackTrace();
					response.getWriter().print(e.getLocalizedMessage());
				}
				break;
			case doResume :
				try {
					contest.doResume();
				} catch (DataException e) {
					e.printStackTrace();
					response.getWriter().print(e.getLocalizedMessage());
				}
				break;
			case doStart :
				break;
			case doStop :
				break;
			case finish :
				this.doPost_finish(request, response);
				break;
			case forcedstart :
				this.doPost_forcedstart(request);
				break;
			case forcedstop :
				if (ContestTLD.isVisible_stopContest(onlineUser, contest)) {
					contest.doStop();
				}
				break;
			case getCountdown :
				response.getWriter().print(contest.getCountdown());
				return;
			case kick :
				this.doPost_kick(request);
				break;
			case leave :
				this.doPost_leave(request, response);
				break;
			case registed :
				this.doPost_registed(request);
				break;
			case checkContestForm :
				this.doPost_checkContestForm(request, response);
				break;
			case jsonContestRanking :
				this.doPost_jsonContestRanking(request, response);
				break;
			default :
				break;

		}
		response.getWriter().print("");
		return;
	}

	public void doPost_finish(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		if (ContestTLD.isVisible_FinishContest(onlineUser, contest)) {

			new ContestService().doFinishByContestUserid(contest, Integer.parseInt(request.getParameter("userid")));

			//
			new UserService().doReload(request.getSession(false));
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

	public void doPost_kick(HttpServletRequest request) {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		Contestant contestant = contest.getContestantByUserid(request.getParameter("userid"));

		if (ContestantTLD.isVisible_KickUser(onlineUser, contestant)) {
			contestant.setStatus(Contestant.STATUS.kicked);
			contestant.setFinishtime(new Timestamp(System.currentTimeMillis()));
			new ContestantDAO().update(contestant);
			User user = contestant.getUser();
			if (user.getJoinedcontestid().intValue() == contest.getId()) {
				user.setJoinedcontestidToNONE();
			}
			new UserService().update(user);
		}

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

	public void doPost_doJoinContest(HttpServletRequest request) {
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
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		if (onlineUser == null || onlineUser.isNullOnlineUser()) {
			throw new DataException("請先登入！才能參加競賽。");
		}
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
}
