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
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/VerifyingProblems" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class VerifyingProblemsServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		this.isAccessible(onlineUser, problem);
	}

	public boolean isAccessible(OnlineUser onlineUser, Problem problem) throws AccessException {
		if (onlineUser.getIsDEBUGGER() || problem.getIsOwner(onlineUser)) {
			return true;
		}
		throw new AccessException("您不能審核提交的題目！");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		problem.setDisplay(Problem.DISPLAY.open);
		new ProblemService().update(problem);

		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}

}
