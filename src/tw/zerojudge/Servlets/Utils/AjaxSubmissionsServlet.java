package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Tables.Solution;

@WebServlet(urlPatterns = { "/AjaxSubmissions" })
@RoleSetting
public class AjaxSubmissionsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Solution solution = new SolutionService().getSolutionById(request
				.getParameter("solutionid"));
		response.getWriter().print(solution.getJudgement().name());
	}
}
