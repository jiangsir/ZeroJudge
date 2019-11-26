package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/RebuiltManager" })
@RoleSetting
public class RebuiltManagerServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("RebuiltManager.jsp").forward(request,
				response);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String rejudgeby = request.getParameter("rejudgeby");
		String[] statusarray = request.getParameterValues("status");
		String[] languagearray = request.getParameterValues("language");
		ArrayList<Solution> solutions = new ArrayList<Solution>();

		if ("bysolutionid".equals(rejudgeby)) {
			int solutionidfrom = Solution.parseSolutionid(request
					.getParameter("solutionidfrom"));
			int solutionidto = Solution.parseSolutionid(request
					.getParameter("solutionidto"));
			if (solutionidfrom > solutionidto) {
				int tmp = solutionidfrom;
				solutionidfrom = solutionidto;
				solutionidto = tmp;
			}
			solutions = new SolutionService().getSolutionsForRejudge(
					solutionidfrom, solutionidto, statusarray, languagearray);

		} else if ("byproblemid".equals(rejudgeby)) {
			String problemlist = request.getParameter("problemidlist");
			problemlist = problemlist.trim().replaceAll(" ", "");
			String[] plist = problemlist.split(",");
			for (String problemid : plist) {
				solutions.addAll(new SolutionService()
						.getSolutionsByProblemid(new Problemid(problemid)));
			}
		} else if ("bycontestid".equals(rejudgeby)) {
			Contest contest = new ContestService().getContestById(request
					.getParameter("contestid"));
			solutions = new SolutionService().getSolutionsByContest(contest, 0);
		}

		for (Solution solution : solutions) {
			solution.doRejudge(UserFactory.getOnlineUser(session));
		}
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}
}
