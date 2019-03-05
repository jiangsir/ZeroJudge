package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/ShowProblem" })
public class ShowProblemServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int MESSAGE_TYPE = 0;
	public static String MESSAGE_TITLE = "";

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		this.AccessFilter(onlineUser, problem);
	}

	public void AccessFilter(OnlineUser onlineUser, Problem problem) throws AccessException {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (onlineUser.getIsDEBUGGER() || problem.getIsOwner(onlineUser)) {
			return;
		}
		if (onlineUser.isIsInContest()) {
			Contest contest = new ContestService().getContestById(onlineUser.getJoinedcontestid());
			if (!contest.isContestProblem(problem.getProblemid())) {
				throw new AccessException("您已參與比賽當中，交卷後才可以解其它題目。");
			}
			if (contest.getIsStarting()) {
				throw new AccessException("比賽尚未開始！");
			}
		} else {
			if (problem.getId() == new Problem().getId()) {
				throw new AccessException("本題目(" + problem.getProblemid() + ") 不存在！");
			} else if (Problem.DISPLAY.hide == problem.getDisplay() || Problem.DISPLAY.practice == problem.getDisplay()
					|| Problem.DISPLAY.verifying == problem.getDisplay()) {
				throw new AccessException("本題目(" + problem.getProblemid() + ") 未公開！");
			} else if (Problem.DISPLAY.contest == problem.getDisplay() && !onlineUser.isIsInContest()) {
				throw new AccessException("不符合存取權限，題目(" + problem.getProblemid() + ") 不顯示(contest problem)");
			} else if (Problem.DISPLAY.deprecated == problem.getDisplay()) {
				throw new AccessException("本題目(" + problem.getProblemid() + ") 已廢棄");
			} else {
				for (Contest contest : new ContestService().getRunningContests()) {
					if (contest.getProblemids().contains(problem.getProblemid()) && !contest.getIsOwner(onlineUser)) {
						throw new AccessException("本題目(" + problem.getProblemid() + ") 正作為競賽用，暫停作答。");
					}
				}
			}
		}
		return;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long starttime = System.currentTimeMillis();
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		ForumDAO forumDao = new ForumDAO();
		if (!problemid.getIsNull() && problemid.getIsLegal()) {
			request.setAttribute("reportArticles", forumDao.getArticlesByProblemReport(problemid, 5));
			request.setAttribute("problemid", problemid);
		} else {
			request.setAttribute("articles", forumDao.getArticles1(onlineUser, 1));
		}

		AppConfig appConfig = ApplicationScope.getAppConfig();
		request.setAttribute("tabs", appConfig.getProblemtabs());

		request.setAttribute("problem", problem);
		request.setAttribute("suggest_keywords",
				new ProblemService().getSuggestKeywords(session, request.getServletPath()));

		request.setAttribute("suggest_backgrounds",
				new ProblemService().getSuggestBackgrounds(session, request.getServletPath()));
		request.setAttribute("TimeUsage", System.currentTimeMillis() - starttime);
		request.getRequestDispatcher("ShowProblem.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
