package Standalone;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.AppConfigDAO;
import tw.zerojudge.DAOs.AppConfigService;

/**
 * 
 * @author jiangsir
 *
 */
public class EditAppConfig {

	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			throw new Exception("參數數量錯誤! EditAppConfig <path> <dbname> <dbaccount> <dbpass>");
		}
		Scanner scanner = new Scanner(System.in);
		Logger logger = Logger.getLogger(EditAppConfig.class.getName());

		String apppath = args[0];
		String dbname = args[1];
		String dbaccount = args[2];
		String dbpass = args[3];

		File appRoot = new File(apppath);
		ApplicationScope.setAppRoot(appRoot);

		String driver = "com.mysql.jdbc.Driver";
		String jdbc = "jdbc:mysql://127.0.0.1:3306/" + dbname
				+ "?useUnicode=true&characterEncoding=UTF-8";
		AppConfigDAO appConfigDao = new AppConfigDAO();
		appConfigDao.setConnection(driver, jdbc, dbaccount, dbpass);

		AppConfig appConfig = new AppConfigService().getAppConfig();

		logger.info("<manager_ip>: 請輸入新的 manager_ip 按 ENTER 表示不更改。(" + appConfig.getManager_ip()
				+ "): ");
		String manager_ip = scanner.nextLine();
		if (!(manager_ip == null || "".equals(manager_ip.trim()))) {
			appConfig.setManager_ip(manager_ip.trim());
		}

		logger.info(
				"<systemmode>: 請輸入新的 systemmode[" + Arrays.toString(AppConfig.SYSTEM_MODE.values())
						+ "] 按 ENTER 表示不更改。(" + appConfig.getSystemMode() + "): ");
		String systemmode = scanner.nextLine();
		if (!(systemmode == null || "".equals(systemmode.trim()))) {
			appConfig.setSystemMode(systemmode.trim());
		}

		new AppConfigService().update(appConfig);
		scanner.close();
	}

}
