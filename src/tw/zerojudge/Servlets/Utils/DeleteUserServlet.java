package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.UserService;

@WebServlet(urlPatterns = { "/DeleteUser" })
@RoleSetting
public class DeleteUserServlet extends HttpServlet implements IAccessFilter {
	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new UserService().delete(request.getParameter("account"));
		HttpSession session = request.getSession(false);
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}

}
