package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.Ajax.ReJudgeAjaxServlet;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ReJudge" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ReJudgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer solutionid = Solution.parseSolutionid(request.getParameter("solutionid"));
		if (!ApplicationScope.getAppConfig().getRejudgeable()) {
			throw new RoleException("不允許重測");
		}
		if (solutionid != 0) {
			Solution solution = new SolutionService().getSolutionById(solutionid);
			reJudgeSolution(request, response, solution);
		}
	}

	private void reJudgeSolution(HttpServletRequest request, HttpServletResponse response, Solution solution)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		try {
			onlineUser.isSolutionRejudgable(solution);
			solution.doRejudge(onlineUser);
			response.sendRedirect(new SessionScope(session).getPreviousPage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleException(e);
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
