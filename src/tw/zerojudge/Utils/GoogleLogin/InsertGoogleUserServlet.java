package tw.zerojudge.Utils.GoogleLogin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;

@WebServlet(urlPatterns = {"/InsertGoogleUser"})
public class InsertGoogleUserServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (!appConfig.getIsGoogleLoginSetup()) {
			throw new DataException("GoogleLogin 並未設定完成！無法使用。請通知管理員。");
		}
		String oauth_uri = "https://accounts.google.com/o/oauth2/auth?response_type=code";
		String scope = "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";
		String redirect_uri = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort())
				+ request.getContextPath()
				+ CallbackInsertGoogleUserServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];


		response.sendRedirect(oauth_uri + "&client_id=" + appConfig.getClient_id() + "&redirect_uri=" + redirect_uri
				+ "&scope=" + scope);
		return;
	}

}
