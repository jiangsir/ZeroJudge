package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/RebuiltContest" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class RebuiltContestServlet extends HttpServlet implements IAccessFilter {
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
		if (ApplicationScope.getAppConfig().getIsCLASS_MODE()) {
			throw new AccessException(
					"系統模式(" + ApplicationScope.getAppConfig().getSystemMode() + ")，無法進行這項動作(rebuildContest)。");
		}
		if (!contest.getIsOwner(onlineUser) && !onlineUser.getIsHigherEqualThanMANAGER()) {
			throw new AccessException("您(" + onlineUser + ")並不是本"
					+ contest.getBundle_Contest(onlineUser.getSession_locale()) + "的擁有者，無法進行這項動作(rebuildContest)。");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long before = System.currentTimeMillis();
		Integer contestid = Contest.parseContestid(request.getParameter("contestid"));

		ContestantDAO contestantsDao = new ContestantDAO();
		contestantsDao.rebuiltContestants(contestid);

		throw new InfoException("重新計算 contestants 完成!!(" + (System.currentTimeMillis() - before) + " ms)");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
