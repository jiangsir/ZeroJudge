package tw.jiangsir.Utils.Listeners.Tasks;

import java.net.URL;
import java.sql.Timestamp;
import java.util.TimerTask;
import java.util.logging.Logger;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Configs.ServerConfig;
import tw.zerojudge.DAOs.AppConfigService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Mailer;

public class ReloadServerConfigTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run() {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		logger.info(this.getClass().getSimpleName() + " 定時重讀 serverConfig(serverUrl="
				+ appConfig.getServerUrl() + ") Running...."
				+ new Timestamp(System.currentTimeMillis()));
		try {
			URL serverUrl = appConfig.getServerUrl();
			ServerConfig serverConfig = appConfig.readServerConfig(serverUrl);
			appConfig.setServerConfig(serverConfig);
			ApplicationScope.setAppConfig(appConfig);
		} catch (Exception e) {
			e.printStackTrace();
			URL defaultServerUrl = appConfig.getDefaultServerUrl();
			ServerConfig serverConfig = appConfig.readServerConfig(defaultServerUrl);
			appConfig.setServerUrl(defaultServerUrl);
			appConfig.setServerConfig(serverConfig);
			ApplicationScope.setAppConfig(appConfig);
			new AppConfigService().update(appConfig);

			for (User user : new UserService().getUsersByROLEDEBUGGER()) {
				String subject = "[SEVERE] 無法讀取評分機(" + e.getLocalizedMessage() + ")，改用預設本機評分機。";
				String stacktrace = "";
				for (StackTraceElement s : e.getStackTrace()) {
					stacktrace += s;
				}
				Mailer mailer = new Mailer(user, subject, stacktrace,
						ApplicationScope.getHostURI());
				try {
					mailer.GmailSender();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
	}

}
