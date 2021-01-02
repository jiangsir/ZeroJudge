package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.Objects.Parameter;

@WebServlet(urlPatterns = { "/SearchSuggest" })
public class SearchSuggestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		UserDAO usersDAO = new UserDAO();
		String account = request.getParameter("q");
		account = StringEscapeUtils.escapeSql(account);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		int limit = Parameter.parsePage(request.getParameter("limit"));
		if (limit > appConfig.getPageSize()) {
			limit = appConfig.getPageSize();
		}
		response.getWriter().print(usersDAO.getAccountSuggest(account, limit));
	}
}
