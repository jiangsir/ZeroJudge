package tw.jiangsir.Utils.Listeners.Tasks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Listeners.CrontabListener;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Mailer;

public class MailerTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run() {
		try {
			logger.info(this.getClass().getSimpleName() + " Running...."
					+ new Timestamp(System.currentTimeMillis()));
			LogDAO logDao = new LogDAO();
			StringBuffer content = new StringBuffer(5000);
			TreeSet<Log.TABID> tabids = new TreeSet<Log.TABID>();
			tabids.add(Log.TABID.ACCESS_EXCEPTION);
			tabids.add(Log.TABID.DATA_EXCEPTION);
			tabids.add(Log.TABID.EXCEPTIONS);
			tabids.add(Log.TABID.GC);
			tabids.add(Log.TABID.SLOW);
			tabids.add(Log.TABID.ERRORPAGE);
			tabids.add(Log.TABID.ERRORTOKEN);

			ArrayList<Log> logs = logDao.getLogByTabid(tabids, CrontabListener.mailer_interval);
			int slow = 0, exceptions = 0, banned = 0, gc = 0;
			for (Log log : logs) {
				if (log.getTabid() == Log.TABID.SLOW) {
					slow++;
				} else if (log.getTabid() == Log.TABID.ERRORPAGE) {
//					if (log.getUri().equals("/favicon.ico") || log.getUri().equals("/robots.txt")
//							|| log.getUri().endsWith("IE_pngfix.js")
//							|| log.getUri().endsWith("jquery.timeout.interval.idle.js")
//							|| log.getUri().endsWith("tiny_mce.js")
//							|| log.getUri().endsWith("tinymce.js")
//							|| log.getUri().endsWith("jquery.autocomplete.js")
//							|| log.getUri().matches(".*\\.png")) {
//						continue;
//					}
				} else if (log.getTabid() == Log.TABID.ACCESS_EXCEPTION
						|| log.getTabid() == Log.TABID.DATA_EXCEPTION
						|| log.getTabid() == Log.TABID.EXCEPTIONS) {
					exceptions++;
				} else if (log.getTabid() == Log.TABID.BANNED) {
					banned++;
				} else if (log.getTabid() == Log.TABID.GC) {
					gc++;
				}
				content.append("[" + log.getTabid().name() + "] BY " + log.getSession_account()
						+ "[" + log.getIpaddr() + "]");
				content.append("" + log.getTitle() + "<br>");
				content.append("*MESSAGE " + log.getMessage() + "<br>");
				content.append(log.getTimestamp() + "<hr>");
			}
			if (content.length() == 0) {
				return;
			}

			for (User user : new UserService().getUsersByROLEDEBUGGER()) {
				String subject = "Log分析(" + ApplicationScope.getHostURI() + "): ";

				subject += slow == 0 ? "" : "SLOW=" + slow;
				subject += exceptions == 0 ? "" : "EXCP=" + exceptions;
				subject += banned == 0 ? "" : "BANNED=" + banned;
				subject += gc == 0 ? "" : "GC=" + gc;

				Mailer mailer = new Mailer(user, subject, content.toString(),
						ApplicationScope.getHostURI());
				try {
					mailer.GmailSender();
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (DataException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
