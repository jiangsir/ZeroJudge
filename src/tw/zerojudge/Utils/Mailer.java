package tw.zerojudge.Utils;

import java.io.UnsupportedEncodingException;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.User;
import java.util.*;

public class Mailer implements Runnable {
	private User mailto;
	private String subject = "";
	private String content = "";
	private String host_uri = "";

	public Mailer() {
	}

	public Mailer(User mailto, String subject, String content, String host_uri) {
		this.mailto = mailto;
		this.subject = subject;
		this.content = content;
		this.host_uri = host_uri;
	}

	public void run() {
		try {
			this.GmailSender();
		} catch (AddressException e) {
			Log log = new Log(e);
			log.setUri(this.getClass().getName());
			log.setStacktrace("信件寄往 mailto=" + mailto + " 發生錯誤\n" + log.getStacktrace());
			new LogDAO().insert(log);
			e.printStackTrace();
		} catch (MessagingException e) {
			Log log = new Log(e);
			log.setUri(this.getClass().getName());
			log.setStacktrace("信件寄往 mailto=" + mailto + " 發生錯誤\n" + log.getStacktrace());
			new LogDAO().insert(log);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SMTPSender() throws AddressException, MessagingException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (!appConfig.getEnableMailer()) {
			throw new DataException("郵件功能未開啟！");
		}
		boolean sessionDebug = false;
		Properties properties = System.getProperties();
		properties.put("mail.host", appConfig.getSystemMail());
		properties.put("mail.transport.protocol", "smtp");
		Session mailsession = Session.getDefaultInstance(properties, null);
		mailsession.setDebug(sessionDebug);
		Message message = new MimeMessage(mailsession);
		InternetAddress[] address = null;

		message.setFrom(new InternetAddress(appConfig.getSystemMail()));
		address = InternetAddress.parse(this.mailto.getEmail(), false);
		message.setRecipients(Message.RecipientType.TO, address);
		message.setSubject("[ZeroJudge系統通知信] " + this.subject);
		message.setSentDate(new Date());
		message.setText(this.content);
		Transport.send(message);
	}

	public void GmailSender() throws Exception {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (!appConfig.getEnableMailer()) {
			throw new DataException("郵件功能未開啟！");
		}
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");

		final String serveremail = appConfig.getSystemMail();
		final String password = appConfig.getSystemMailPassword();
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(serveremail, password);
			}
		});

		Message message = new MimeMessage(session);
		InternetAddress[] address = null;
		InternetAddress iaFrom = new InternetAddress(appConfig.getSystemMail());
		try {
			iaFrom.setPersonal(appConfig.getTitle() + "(" + host_uri + ")");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		message.setFrom(iaFrom);

		address = InternetAddress.parse(this.mailto.getEmail(), false);
		message.setRecipients(Message.RecipientType.TO, address);
		message.setSubject("[系統通知信] " + this.subject);
		message.setSentDate(new Date());
		message.setText(this.content);
		Transport.send(message);

	}

	/**
	 * 檢查 gmail(app) 密碼是否正確
	 * 
	 * @return
	 */
	public boolean isGmailAccount(String email, String passwd) {

		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		Properties props = System.getProperties();
		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", "995");
		props.setProperty("mail.pop3.socketFactory.port", "995");

		Session session = Session.getDefaultInstance(props, null);

		URLName urln = new URLName("pop3", "pop.gmail.com", 995, null, email, passwd);
		try {
			Store store = session.getStore(urln);
			store.connect();
			Folder folder = store.getDefaultFolder();
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
