package tw.zerojudge.Tools;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Tables.Problem;

@SuppressWarnings("serial")
public class AdminToolsServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String method = request.getParameter("method");
		if ("ReplaceImage".equals(method)) {
			ProblemDAO problemDao = new ProblemDAO();
			for (Problem problem : problemDao.getAllProblems()) {
				int[] infos = problem.getImageInfos();
				if (infos[1] == 0 && infos[2] == 0) {
					continue;
				}
				problem = new ProblemService().downloadProblemAttachfiles(problem,
						request.getServerPort());
				new ProblemService().update(problem);
			}
		}

		request.getRequestDispatcher(
				new SessionScope(session).getPreviousPage()).forward(request,
				response);
	}

}
