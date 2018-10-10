package tw.jiangsir.Utils.Tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;

public class TokenTool {
	public static String generateToken() {
		String token = System.currentTimeMillis() + "" + new Random().nextInt(99999999);

		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte md5[] = md.digest(token.getBytes());

			final Base64 base64 = new Base64();
			return base64.encodeToString(md5);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static synchronized boolean isToken(HttpServletRequest request) {
		String client_token = request.getParameter("token");
		if (client_token == null) {
			return false;
		}

		String server_token = (String) request.getSession().getAttribute("token");
		if (server_token == null) {
			return false;
		}
		if (!client_token.equals(server_token)) {
			return false;
		}

		return true;
	}

}
