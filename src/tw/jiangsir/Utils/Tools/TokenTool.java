package tw.jiangsir.Utils.Tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiangsir
 *
 */
public class TokenTool {

	public static String generateTokenUrlBase64() {
		final String text = System.currentTimeMillis() + "" + new Random().nextInt(99999999);
		byte[] textByte = text.getBytes();

		final String encodedText = Base64.getUrlEncoder().encodeToString(textByte);

		return encodedText;
	}

	public static String generateToken() {
		String token = System.currentTimeMillis() + "" + new Random().nextInt(99999999);

		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte md5[] = md.digest(token.getBytes());

			final String encodedText = Base64.getUrlEncoder().encodeToString(md5);

			return encodedText;

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String generateTokenMD5() {
		String token = System.currentTimeMillis() + "" + new Random().nextInt(99999999);

		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte md5[] = md.digest(token.getBytes());

			return new String(md5);
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
