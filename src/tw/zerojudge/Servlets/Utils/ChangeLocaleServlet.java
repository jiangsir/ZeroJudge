package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;

@WebServlet(urlPatterns = { "/ChangeLocale.api" })
public class ChangeLocaleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		AppConfig appConfig = ApplicationScope.getAppConfig();

		HttpSession session = request.getSession(false);

		SessionScope sessionScope = new SessionScope(session);

		String localeString = request.getParameter("locale");
		Locale localee = new Locale("en", "US");
		for (Locale locale : appConfig.getLocales()) {
			if (locale.toString().equals(localeString)) {
				localee = locale;
			}
		}

		sessionScope.setSession_locale(localee);



		response.sendRedirect(sessionScope.getPreviousPage());
	}
}
