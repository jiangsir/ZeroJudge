package tw.zerojudge.Servlets;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/ShowCode" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ShowCodeServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getAnonymousLogger();


	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession sesion = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(sesion);
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		this.AccessFilter(onlineUser, solution);
	}

	public void AccessFilter(OnlineUser onlineUser, Solution solution) throws AccessException {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (onlineUser.getIsDEBUGGER() || solution.getContest().getIsOwner(onlineUser)) {
			return;
		}

		if (!onlineUser.isNullOnlineUser() && onlineUser.getJoinedcontestid().intValue() != 0
				&& onlineUser.getJoinedcontestid().intValue() != solution.getContestid().intValue()) {
			logger.info("您正在參賽中，請勿觀看競賽以外的程式碼。");
			throw new AccessException("您正在參賽中，請勿觀看競賽以外的程式碼。");
		}

		if (!onlineUser.isNullOnlineUser() && onlineUser.getId() != 0 && !onlineUser.isIsGroupGuest()
				&& (solution.getUser().getAccount().equals(onlineUser.getAccount())
						|| (onlineUser.getIsQualifiedAuthor() && solution.getProblem().getIsOwner(onlineUser))
						|| Solution.CODELOCKER_OPEN == solution.getCodelocker().intValue())) {
			return;
		} else {
			logger.info("您並非這個程式的作者，無法參觀喔!!");
			throw new AccessException("您並非這個程式的作者，無法參觀喔!!");
		}
	}

	public boolean isAccessible(OnlineUser onlineUser, Solution solution) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}


		try {
			this.AccessFilter(onlineUser, solution);
			return true;
		} catch (AccessException e) {
			return false;
		}

	}

}
