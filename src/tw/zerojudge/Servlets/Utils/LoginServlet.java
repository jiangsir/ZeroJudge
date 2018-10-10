package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/Login" })
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper(); 

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();

		if (onlineUser != null && !onlineUser.isNullOnlineUser()) {
			response.sendRedirect("./");
			return;
		}

		request.getRequestDispatcher("Login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		try {

			String account = request.getParameter("account");
			String passwd = request.getParameter("passwd");
			User user = new UserService().getUserByAccountPasswd(account, passwd);

			new UserService().doLogin(session, user);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
		HashMap<String, String> returnPage = new HashMap<String, String>();
		returnPage.put("currentPage", new SessionScope(session).getCurrentPage());
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(mapper.writeValueAsString(returnPage));
		response.flushBuffer();
	}

}
