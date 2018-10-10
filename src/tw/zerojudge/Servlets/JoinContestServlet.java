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
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/JoinContest" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class JoinContestServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
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
		if (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) {
			throw new AccessException("您是主辦人，不能加入競賽!!");
		}
		if (contest.getIsStopped()) {
			throw new AccessException("競賽已經結束!!");
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("JoinContest.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));


		if (contest.isCheckedConfig(Contest.CONFIG.Team)) {

			response.sendRedirect(request.getContextPath()
					+ ShowContestServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0].substring(1)
					+ "?contestid=" + contest.getId());
		} else { 
			String account = request.getParameter("account");
			String userpasswd = request.getParameter("passwd");
			User user = new UserService().getUserByAccountPasswd(account, userpasswd);
			onlineUser = new UserService().doLogin(session, user);
			onlineUser.setSession_ip(request.getRemoteAddr());

			onlineUser.doJoinContest(contest);

			response.sendRedirect(
					request.getContextPath() + ShowContestServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]
							+ "?contestid=" + contest.getId());
		}
	}

}
