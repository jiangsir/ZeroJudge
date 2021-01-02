package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;

@WebServlet(urlPatterns = { "/ReloadUserinfo" })
@RoleSetting
public class ReloadUserinfoServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String sessionid = request.getParameter("sessionid");
		if (!"".equals(sessionid)) {
		} else {
		}
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}
}
