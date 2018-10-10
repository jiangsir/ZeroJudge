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
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/EditContestants" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class EditContestantsServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		if (!contest.getIsOwner(onlineUser) && !onlineUser.getIsDEBUGGER()) {
			throw new AccessException("您不是本競賽的發起人！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		request.setAttribute("contestants", new ContestService().getContestantsByContestid(contest.getId()));
		request.setAttribute("contest", contest);
		request.getRequestDispatcher("EditContestants.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
