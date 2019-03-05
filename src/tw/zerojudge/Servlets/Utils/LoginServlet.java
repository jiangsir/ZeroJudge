package tw.zerojudge.Servlets.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/Login" })
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper(); 

	public static boolean isCaptchaValid(String secretKey, String response, IpAddress remoteAddr) {
		if (remoteAddr.isLoopbackAddress()) {
			return true;
		}
		try {
			String url = "https://www.google.com/recaptcha/api/siteverify?" + "secret=" + secretKey
					+ "&response=" + response;
			InputStream res = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(res, Charset.forName("UTF-8")));

			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
			String jsonText = sb.toString();
			res.close();

			JSONObject json = new JSONObject(jsonText);
			return json.getBoolean("success");
		} catch (Exception e) {
			return false;
		}
	}

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
		AppConfig appConfig = ApplicationScope.getAppConfig();
		try {
			String account = request.getParameter("account");
			String passwd = request.getParameter("passwd");
			User user = new UserService().getUserByAccountPasswd(account, passwd);
			String secretkey = appConfig.getSecret_key();
			if (appConfig.getIsGoogleRecaptchaSetup(request.getRemoteAddr())
					&& !isCaptchaValid(secretkey, request.getParameter("g-recaptcha-response"),
							new IpAddress(request))) {
				throw new JQueryException("reCaptcha 機器人驗證不通過。");
			}
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
