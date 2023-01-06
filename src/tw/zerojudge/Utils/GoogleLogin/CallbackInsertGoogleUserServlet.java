package tw.zerojudge.Utils.GoogleLogin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;

/**
 * Servlet implementation class OAuth2CallbackServlet
 */
@WebServlet(urlPatterns = {"/callbackInsertGoogleUser"})
public class CallbackInsertGoogleUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper(); 

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String code = request.getParameter("code");
		Verifier verifier = new Verifier(code);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		String apiKey = appConfig.getClient_id(); 
		String apiSecret = appConfig.getClient_secret();
		String redirect_uri = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort())
				+ request.getContextPath()
				+ CallbackInsertGoogleUserServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];
		OAuthService service = new ServiceBuilder().provider(Google2Api.class).apiKey(apiKey).apiSecret(apiSecret)
				.callback(redirect_uri)
				.scope("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email")
				.build();
		Token accessToken = service.getAccessToken(null, verifier);

		OAuthRequest req = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
		service.signRequest(accessToken, req);
		Response resp = req.send();
		String _sBody = resp.getBody();

		GoogleUser googleUser = mapper.readValue(_sBody, GoogleUser.class);
		String domain = googleUser.getEmail().split("@")[1];

		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		if (onlineUser == null || onlineUser.isNullOnlineUser()) { 
			User user = new UserService().getUserByGoogle(googleUser);
			if (user == null || user.isNullUser()) {
				this.insertGoogleUser(session, googleUser);
			} else {
				onlineUser = new OnlineUser(session, user);
				new CallbackGoogleLoginServlet().doGoogleLogin(session, onlineUser, googleUser);
			}
		}
		new SessionScope(session).setGoogleUser(googleUser);

		response.sendRedirect("./");
	}

	/**
	 * 對於沒有登入，且透過 google 登入的人，直接新增一個帳號給他。
	 * 
	 * @param session
	 * @param googleUser
	 */
	protected void insertGoogleUser(HttpSession session, GoogleUser googleUser) {
		User user = new UserService().insertGoogleUser(googleUser);
		OnlineUser onlineUser = new OnlineUser(session, user);
		new SessionScope(session).setOnlineUser(onlineUser);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
