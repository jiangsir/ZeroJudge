package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;

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
import tw.zerojudge.Factories.ContestFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/EditContests" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class EditContestsServlet extends HttpServlet implements IAccessFilter {
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

	public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (contest == null) {
			contest = ContestFactory.getNullcontest();
		}

		if (!contest.isNullContest()) {
			if (!onlineUser.isProblemManager()) {
				if (!contest.getIsOwner(onlineUser) && !onlineUser.getIsHigherEqualThanMANAGER()) {
					throw new AccessException("您不是該競賽之管理員，不能進行管理。");
				}

			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		ArrayList<Contest> contests = new ArrayList<Contest>();
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
		}
		String contestid = request.getParameter("contestid");
		if (contestid == null) {
			if (onlineUser.getIsHigherEqualThanMANAGER()) {
				contests = new ContestService().getContestsByVclassid(0, page);
			} else {
				contests = new ContestService().getContestsByOwnerid(onlineUser.getId(), page);
			}
		} else {
			contests.add(new ContestService().getContestById(contestid));
		}
		request.setAttribute("contests", contests);
		request.setAttribute("pagenum", page);
		request.getRequestDispatcher("/EditContests.jsp").forward(request, response);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
