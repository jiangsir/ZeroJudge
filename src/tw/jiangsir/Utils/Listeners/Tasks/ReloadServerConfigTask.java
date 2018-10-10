package tw.jiangsir.Utils.Listeners.Tasks;

import java.net.URL;
import java.sql.Timestamp;
import java.util.TimerTask;
import java.util.logging.Logger;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.ServerConfig;

public class ReloadServerConfigTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run() {
		logger.info(this.getClass().getSimpleName() + " 定時重讀 serverConfig Running...."
				+ new Timestamp(System.currentTimeMillis()));
		try {
			URL serverUrl = ApplicationScope.getAppConfig().getServerUrl();
			ServerConfig serverConfig = ApplicationScope.getAppConfig().readServerConfig(serverUrl);
			ApplicationScope.getAppConfig().setServerConfig(serverConfig);
			ApplicationScope.setAppConfig(ApplicationScope.getAppConfig());
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationScope.getAppConfig().setServerConfig(new ServerConfig());
			ApplicationScope.setAppConfig(ApplicationScope.getAppConfig());
		}
	}

}
