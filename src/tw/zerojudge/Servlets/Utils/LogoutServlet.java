package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Servlets.IndexServlet;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;

@WebServlet(urlPatterns = {"/Logout"})
public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		if (onlineUser == null || onlineUser.isNullOnlineUser()) {
			response.sendRedirect("./" + IndexServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		}
		String target = request.getContextPath();

		if (request.getQueryString() == null || "".equals(request.getQueryString())) {
			try {
				onlineUser.doLogout();
				session.invalidate();
			} catch (AccessException e) {
				e.printStackTrace();
				return;
			} catch (DataException e) {
				e.printStackTrace();
				return;
			}

		} else if (onlineUser.getIsDEBUGGER()) {
			String sessionid = request.getParameter("sessionid");
			if (SessionFactory.getSessionById(sessionid) != null) {
				SessionFactory.getSessionById(sessionid).invalidate();
				target = request.getContextPath()
						+ ShowOnlineUsersServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];
			}
		}
		response.sendRedirect("./");
	}
}
