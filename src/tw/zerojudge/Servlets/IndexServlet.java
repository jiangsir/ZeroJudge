package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SchoolDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = { "/Index" })
public class IndexServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//TODO 20170502 手動評分的時候，應檢查 solutionid 是否正確的競賽主辦人





		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		AppConfig appConfig = ApplicationScope.getAppConfig();

		if (!onlineUser.getIsDEBUGGER()
				&& appConfig.getSystemMode().equals(AppConfig.SYSTEM_MODE.CONTEST_MODE.name())) {
			int contestid = appConfig.getSystemModeContestid();
			if (contestid == 0) {
				response.sendRedirect(request.getContextPath()
						+ ContestsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			} else {
				response.sendRedirect(request.getContextPath()
						+ ShowContestServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0] + "?contestid="
						+ contestid);
			}
			return;
		}

		ForumDAO forumDao = new ForumDAO();

		request.setAttribute("Announcements", forumDao.getAnnouncements());
		request.setAttribute("VBANNER", ApplicationScope.getAppConfig().getBannerContent());
		request.setAttribute("newproblems", new ProblemService().getLastestProblems(24));

		request.setAttribute("top10", new UserService().getTopUsers(10));
		request.setAttribute("topauthors", new ProblemService().getTopAuthors(10));
		request.setAttribute("topschools", new SchoolDAO().getTopSchools(10));
		request.setAttribute("runningContests", new ContestService().getRunningContests());
		request.setAttribute("startingContests", new ContestService().getStartingContests());
		request.setAttribute("MarkedThreads", forumDao.getArticlesByMarked());

		appConfig.getServerConfig();
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
