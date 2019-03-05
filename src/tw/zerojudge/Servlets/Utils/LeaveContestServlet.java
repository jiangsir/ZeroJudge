package tw.zerojudge.Servlets.Utils;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.ContestsServlet;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/LeaveContest" })
@RoleSetting
public class LeaveContestServlet extends HttpServlet implements IAccessFilter {

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
		if ((contest.getIsRunning() || contest.getIsStarting() || contest.getIsPausing()) && (onlineUser.getIsDEBUGGER()
				|| contest.getIsOwner(onlineUser) || onlineUser.getJoinedcontestid().intValue() == contest.getId())) {
			return;
		} else {
			throw new AccessException("您沒有權限離開這個競賽。");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		if (onlineUser != null) {
			onlineUser.doLeaveContest();
		}

		response.sendRedirect(
				request.getContextPath() + ContestsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
	}

}
