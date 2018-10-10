package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Api.Checks.CheckProblemServlet;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.IMessageDAO;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.IMessage;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.User.ROLE;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/InsertProblem" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class InsertProblemServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.getIsHigherEqualThanMANAGER() && !onlineUser.isInsertProblem()) {
			throw new AccessException("您沒有權限新增題目！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		AppConfig appConfig = ApplicationScope.getAppConfig();
		request.setAttribute("tabs", appConfig.getProblemtabs());

		Problem newproblem = new ProblemService().getUnfinishedProblem(onlineUser);

		if (newproblem.isNullProblem()) {
			newproblem.setProblemid(new ProblemService().createNextProblemid());
			newproblem.setTestfilelength(1);
			newproblem.setTimelimits(new double[] { 1 });
			newproblem.setScores(new int[] { 100 });
			newproblem.setOwnerid(onlineUser.getId());
			newproblem.setLocale(new SessionScope(session).getSession_locale());
			newproblem.setDisplay(Problem.DISPLAY.unfinished);
			int pid = new ProblemService().insert(newproblem);
			newproblem.setId(pid);
			try {
				new ProblemService().doCreateTestfile(newproblem, 0, "Upload Infile data!!\n",
						"Upload Outfile data!!\n");
			} catch (DataException e) {
				new ProblemService().delete(pid);
				throw new DataException(e);
			}
		}

		response.sendRedirect(
				request.getContextPath() + UpdateProblemServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]
						+ "?problemid=" + newproblem.getProblemid());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		SessionScope sessionScope = new SessionScope(session);
		OnlineUser onlineUser = sessionScope.getOnlineUser();

		ProblemDAO problemDao = new ProblemDAO();
		new CheckProblemServlet().checkInsert(request);

		Problem newproblem = new ProblemService().getUnfinishedProblem(onlineUser);
		if (newproblem.isNullProblem()) {
			throw new DataException(onlineUser + "這個題目尚未新增。");
		}
		newproblem.setBackgrounds(request.getParameter("backgrounds"));
		newproblem.setLocale(request.getParameter("locale"));
		newproblem.setContent(request.getParameter("content"));
		newproblem.setTheoutput(request.getParameter("theoutput"));
		newproblem.setTestfilelength(1);
		newproblem.setSampleinput(request.getParameter("sampleinput"));
		newproblem.setSampleoutput(request.getParameter("sampleoutput"));
		newproblem.setAlteroutdata(request.getParameter("alteroutdata"));
		newproblem.setJudgemode(request.getParameter("judgemode"));

		newproblem.setHint(request.getParameter("hint"));
		newproblem.setSamplecode(request.getParameter("samplecode"));
		newproblem.setComment(request.getParameter("comment"));

		newproblem.setReference(request.getParameter("reference"));
		newproblem.setSortable(request.getParameter("sortable"));
		newproblem.setKeywords(request.getParameter("keywords"));
		newproblem.setDifficulty(Parameter.parseInteger(request.getParameter("difficulty")));
		newproblem.setWa_visible(request.getParameter("wa_visible"));
		String display = request.getParameter("display");

		if (Problem.DISPLAY.verifying.name().equals(display)
				&& onlineUser.isCheckedConfig(User.CONFIG.QualifiedAuthor)) {
			newproblem.setDisplay(Problem.DISPLAY.open);
		} else {
			newproblem.setDisplay(display);
		}
		if (Problem.DISPLAY.verifying == newproblem.getDisplay()) {
			Log log = new Log(request);
			log.setTabid(Log.TABID.INFO);
			log.setMessage(newproblem.getProblemid() + " 設定為 '已提交'");
			new LogDAO().insert(log);
//			for (User user : new UserService().getGeneralManagers()) {
//				IMessage imessage = new IMessage();
//				imessage.setSender("admin");
//				imessage.setReceiver(user.getAccount());
//				imessage.setSubject(newproblem.getProblemid() + " 設定為 '已提交' 請進入處理");
//				imessage.setContent("題目 " + newproblem.getProblemid() + "已設定為'提交', 請進入\"管理題目\",選擇"
//						+ " \"已提交\"分頁， 點擊該題目的 '已提交'字樣，即可改為 '已公開'");
//				new IMessageDAO().sendIMessage(imessage, sessionScope.getHostURI());
//			}
		}
		newproblem.setTestfilelength(1);
		newproblem.setTheinput(request.getParameter("theinput"));
		newproblem.setTitle(request.getParameter("title"));
		newproblem.setMemorylimit(request.getParameter("memorylimit"));
		newproblem.setLanguage(request.getParameter("language"));
		newproblem.setTabid(request.getParameter("tabid"));
		newproblem.setTimelimits(new double[] { 1.0 });
		newproblem.setScores(new int[] { 100 }); 
		newproblem = new ProblemService().downloadProblemAttachfiles(newproblem, request.getServerPort());

		int id = problemDao.insert(newproblem);
		newproblem.setId(id);

		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}

}
