package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.SchoolService;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = {"/EditSchools"})
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class EditSchoolsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int page = 1;
		if (request.getParameter("page") != null && request.getParameter("page").matches("[0-9]+")) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		request.setAttribute("schools", new SchoolService().getSchoolsByPage(page));
		request.getRequestDispatcher("EditSchools.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyword = request.getParameter("searchschool");
		request.setAttribute("schools", new SchoolService().getSchoolsByKeyword(keyword));
		request.getRequestDispatcher("EditSchools.jsp").forward(request, response);

	}
}
