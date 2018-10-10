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
import tw.zerojudge.Tables.Log;

/**
 * Servlet implementation class ErrorHandlerServlet
 */
@WebServlet(urlPatterns = { "/ErrorPageHandler" })
public class ErrorPageHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
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
		HashMap<String, URI> uris = new HashMap<String, URI>();
		try {
			uris.put("回前頁", new URI(new SessionScope(session).getCurrentPage()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		String message = (String) request.getAttribute("javax.servlet.error.message");
		Log log = new Log(request, throwable);
		log.setTabid(Log.TABID.ERRORPAGE);
		log.setTitle("網頁錯誤編號：" + statusCode + "(" + requestUri + ")");
		log.setMessage(message);
		log.setUri(requestUri);
		if (!log.getUri().equals("/favicon.ico") && !log.getUri().equals("/robots.txt")) {
			new LogDAO().insert(log);
		}

		Alert alert = new Alert(Alert.TYPE.ERROR, message, log.getTitle(), "BY: " + this.getClass().getSimpleName(),
				uris, null);
		request.setAttribute("alert", alert);
		request.getRequestDispatcher("/Alert.jsp").forward(request, response);
	}
}
