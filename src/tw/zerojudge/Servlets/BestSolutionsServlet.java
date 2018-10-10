package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.JsonObjects.Compiler;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = {"/BestSolutions"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class BestSolutionsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Problemid problemid = new Problemid(request.getParameter("problemid"));

		if (ApplicationScope.getAppConfig().getServerConfig().getEnableCompilers().size() != 0) {
			String languagestring = request.getParameter("language");

			Language language = null;
			if (languagestring == null) {
				for (Compiler compiler : ApplicationScope.getAppConfig().getServerConfig().getEnableCompilers()) {
					language = new Language(compiler.getLanguage(), compiler.getLanguage().toLowerCase());
					break;
				}
			} else {
				language = new Language(languagestring, languagestring.toLowerCase());
			}
			request.setAttribute("solutions", new SolutionService()
					.getBestSolutions(new ProblemService().getProblemByProblemid(problemid).getId(), language, 1));
		}
		request.getRequestDispatcher("BestSolutions.jsp").forward(request, response);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
