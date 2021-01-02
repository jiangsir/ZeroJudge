package tw.jiangsir.Utils.Listeners;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Timer;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import tw.jiangsir.Utils.Listeners.Tasks.BannedTask;
import tw.jiangsir.Utils.Listeners.Tasks.InitSolutionsTask;
import tw.jiangsir.Utils.Listeners.Tasks.MailerTask;
import tw.jiangsir.Utils.Listeners.Tasks.PerformanceTask;
import tw.jiangsir.Utils.Listeners.Tasks.RecountTask;
import tw.jiangsir.Utils.Listeners.Tasks.ReloadServerConfigTask;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;

/**
 * Application Lifecycle Listener implementation class MyRequestListener
 * 
 */
/**
 * @author jiangsir
 *
 */
@WebListener
public class CrontabListener implements ServletContextListener {
	Logger logger = Logger.getLogger(this.getClass().getName());
	Timer timer = new Timer();
	public static long performance_interval = 10 * 60 * 1000; 
	public static long mailer_interval = 24 * 60 * 60 * 1000; 
	public static long banned_interval = 10 * 60 * 1000; 
	public static long recount_interval = 24 * 60 * 60 * 1000; 
	public static long reloadServerConfig_interval = 1 * 60 * 1000; 

	/**
	 * Default constructor.
	 */
	public CrontabListener() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		timer = new java.util.Timer(true); 

		timer.schedule(new PerformanceTask(), 5 * 60 * 1000, performance_interval);
		timer.schedule(new BannedTask(), 5 * 60 * 1000, banned_interval);
		timer.schedule(new ReloadServerConfigTask(), 30 * 1000, reloadServerConfig_interval);

		timer.schedule(new InitSolutionsTask(), 15 * 1000); 
		Calendar recount = Calendar.getInstance();
		recount.set(Calendar.HOUR_OF_DAY, 6);
		recount.set(Calendar.MINUTE, 0);
		recount.set(Calendar.SECOND, 0);

		while (recount.before(Calendar.getInstance())) {
			recount.add(Calendar.MILLISECOND, (int) CrontabListener.recount_interval);
		}
		logger.fine("Recount 任務排定時間:" + new Timestamp(recount.getTime().getTime()));
		new Timer(true).schedule(new RecountTask(), recount.getTime(), recount_interval);
	}
}
