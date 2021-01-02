package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Tables.VClass;

@WebServlet(urlPatterns = { "/RemoveVClassStudent" })
@RoleSetting
public class RemoveVClassStudentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Integer vclassid = VClass.parseVClassid(request
				.getParameter("vclassid"));
		String account = request.getParameter("account");

		new VClassDAO().removeVClassStudent(vclassid, new UserService()
				.getUserByAccount(account).getId());
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}
}
