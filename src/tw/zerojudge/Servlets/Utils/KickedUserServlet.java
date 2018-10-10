package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.LinkedHashSet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Utils.*;

@WebServlet(urlPatterns = { "/KickedUser" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class KickedUserServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		String userid = request.getParameter("userid");
		if (userid == null || !userid.contains("[0-9]+")) {
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
		if (contest.getIsStopped()) {
			throw new AccessException("競賽已結束，不能進行這個動作！");
		}
		if ((contest.getIsRunning() || contest.getIsStarting() || contest.getIsSuspending() || contest.getIsPausing())
				&& (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser) || onlineUser.getId() == kickuserid
						|| onlineUser.getJoinedcontestid().intValue() == contest.getId())) {
			return;
		} else {
			throw new AccessException("您沒有權限進行這個動作！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		int contestid = Contest.parseContestid(request.getParameter("contestid"));
		String account = request.getParameter("account");

		ContestantDAO contestantDao = new ContestantDAO();
		contestantDao.removeContestant(contestid, account);
		Contest contest = new ContestService().getContestById(contestid);
		if (!onlineUser.getAccount().equals(account)
				&& contest.getOwnerid().intValue() == onlineUser.getId().intValue()) {
			LinkedHashSet<String> userrules = contest.getUserrules();
			userrules.add("!" + account);
			contest.setUserrules(userrules);
			new ContestService().update(contest);
		}

		User user = new UserService().getUserByAccount(account);
		user.setJoinedcontestidToNONE();
		new UserService().update(user);
		response.sendRedirect("." + new SessionScope(session).getPreviousPage());
	}
}
