package tw.jiangsir.Utils.Servlets;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;

/**
 * Servlet implementation class ErrorHandlerServlet
 */
@WebServlet(urlPatterns = { "/ErrorPageHandler" })
public class ErrorPageHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
		if (servletName == null) {
			servletName = "Unknown";
		}
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		HashMap<String, URI> uris = new HashMap<String, URI>();
		try {
			uris.put("回前頁", new URI(new SessionScope(session).getCurrentPage()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		String message = (String) request.getAttribute("javax.servlet.error.message");

		Alert alert = new Alert(Alert.TYPE.ERROR, message,
				"BY <error-page> 網頁錯誤編號：" + statusCode + "(" + requestUri + ")",
				"BY: " + this.getClass().getSimpleName(), uris, null, onlineUser);
		request.setAttribute("alert", alert);
		request.getRequestDispatcher("/Alert.jsp").forward(request, response);
	}
}
