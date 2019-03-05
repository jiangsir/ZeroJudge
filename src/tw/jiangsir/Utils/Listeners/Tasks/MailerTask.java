package tw.jiangsir.Utils.Listeners.Tasks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Listeners.CrontabListener;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Mailer;
import tw.zerojudge.Utils.Utils;

public class MailerTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run() {
		try {
			logger.info(this.getClass().getSimpleName() + " Running...." + new Timestamp(System.currentTimeMillis()));
			LogDAO logDao = new LogDAO();
			TreeSet<String> andrules = new TreeSet<String>();
			andrules.add("timestamp>='" + new Utils()
					.parseDatetime(new Timestamp(System.currentTimeMillis() - CrontabListener.mailer_interval)) + "'");
			StringBuffer content = new StringBuffer(5000);
			ArrayList<Log> logs = logDao.getLogs(andrules, "timestamp DESC", 0);
			int slow = 0, errorpage = 0, banned = 0, gc = 0;
			for (Log log : logs) {
				if (log.getTabid() == Log.TABID.SLOW) {
					slow++;
				} else if (log.getTabid() == Log.TABID.ERRORPAGE) {
					if (log.getUri().equals("/favicon.ico") || log.getUri().equals("/robots.txt")
							|| log.getUri().endsWith("IE_pngfix.js")
							|| log.getUri().endsWith("jquery.timeout.interval.idle.js")
							|| log.getUri().endsWith("tiny_mce.js") || log.getUri().endsWith("tinymce.js")
							|| log.getUri().endsWith("jquery.autocomplete.js")) {
						continue;
					}
					errorpage++;
				} else if (log.getTabid() == Log.TABID.BANNED) {
					banned++;
				} else if (log.getTabid() == Log.TABID.GC) {
					gc++;
				}
				content.append("\n[" + log.getTimestamp() + "]" + log.getTitle());
				content.append("\n*MESSAGE " + log.getMessage());
				content.append("\n*FROM " + log.getIpaddr());
				content.append("\n*BY " + log.getSession_account());
				content.append("\n=========================================\n");
			}
			if (content.length() == 0) {
				return;
			}

			for (User user : new UserService().getUsersByROLEDEBUGGER()) {
				Mailer mailer = new Mailer(user,
						"Log分析：slow=" + slow + ", errorpage=" + errorpage + ", banned=" + banned + ", gc=" + gc,
						content.toString(), "");
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
