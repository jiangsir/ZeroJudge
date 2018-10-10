package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/EditUsers" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class EditUsersServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int PageNum = Parameter.parsePage(request.getParameter("page"));
		request.setAttribute("pagenum", PageNum);
		request.setAttribute("querystring",
				StringTool.querystingMerge(request.getQueryString()));
		request.setAttribute("list",
				new UserService().getUsersByROLEUSER(PageNum));
		request.getRequestDispatcher("EditUsers.jsp")
				.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String label = Parameter.parseString(request.getParameter("label"));
		request.setAttribute("list", new UserService().getUsersByLabel(label));
		request.getRequestDispatcher("EditUsers.jsp")
				.forward(request, response);
	}
}
