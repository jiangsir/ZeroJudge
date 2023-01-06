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
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/DeprecateProblem" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class DeprecateProblemServlet extends HttpServlet implements
		IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		try {
			new UpdateProblemServlet().AccessFilter(request);
		} catch (AccessException e) {
			String problemid = request.getParameter("problemid");
			throw new AccessException("您非題目管理者，無法廢棄題目。(" + problemid + ")。");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		problem.setDisplay(Problem.DISPLAY.deprecated);
		new ProblemService().update(problem);
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}

}
