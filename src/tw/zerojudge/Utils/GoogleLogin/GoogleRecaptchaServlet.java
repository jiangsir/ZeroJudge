package tw.zerojudge.Utils.GoogleLogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

/**
 * Servlet implementation class GoogleRecaptchaServlet
 */
@WebServlet("/GoogleRecaptchaServlet")
public class GoogleRecaptchaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static boolean isCaptchaValid(String secretKey, String response) {
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

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoogleRecaptchaServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String secretkey = "6LfdNIYUAAAAAHyhG4eMLNG_Zh7Wk9SefXNMjqng";
		String sitekey = "6LfdNIYUAAAAAAMFbjvp8dHA4huNFcsycZMoaPRs";
		if (isCaptchaValid(secretkey, request.getParameter("g-recaptcha-response"))) {
		}
	}

}
