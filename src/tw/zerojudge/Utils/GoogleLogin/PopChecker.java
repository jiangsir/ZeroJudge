package tw.zerojudge.Utils.GoogleLogin;

import java.security.Security;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import tw.jiangsir.Utils.Exceptions.DataException;

public class PopChecker {
	/**
	 * 檢查 gmail(app) 密碼是否正確
	 * 
	 * @return
	 */
	public boolean isGmailAccount(String email, String passwd) {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		Properties props = System.getProperties();
		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", "995");
		props.setProperty("mail.pop3.socketFactory.port", "995");

		Session session = Session.getDefaultInstance(props, null);

		URLName urln = new URLName("pop3", "pop.gmail.com", 995, null, email,
				passwd);
		try {
			Store store = session.getStore(urln);
			store.connect();
			store.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			if (e instanceof AuthenticationFailedException) {
				throw new DataException("驗證有誤，帳號密碼可能有誤（如果您的學生信箱尚未開通，請登入後即可開通）！"
						+ e.getLocalizedMessage());
			}
			if (e.getLocalizedMessage().contains("timed out")) {
				throw new DataException("連線逾時！");
			}
			if (e.getLocalizedMessage().contains("Connect failed")) {
				throw new DataException("無法與驗證主機連線！");
			}

			throw new DataException(e);
		}
		return true;
	}

}
