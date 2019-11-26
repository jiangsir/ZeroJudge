package tw.zerojudge.Tools;

import java.io.File;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Utils.XMLParser;

/**
 * @author jiangsir
 * 
 */
public class Installer {
	private File appRoot;
	private File consoledir;
	private String dbuser;
	private String dbpassword;
	ObjectMapper mapper = new ObjectMapper(); 

	public Installer(File appRoot, File consoledir, String dbuser, String dbpassword) {
		if (!appRoot.isDirectory() || !consoledir.isDirectory()) {
			return;
		}
		this.appRoot = appRoot;
		this.consoledir = consoledir;
		this.dbuser = dbuser;
		this.dbpassword = dbpassword;
	}

	public void run() {
		this.initContextxml();
		this.initSystemConfig();
	}

	/**
	 * 初始化 SystemConfig
	 */
	private void initSystemConfig() {
		ApplicationScope.setAppRoot(appRoot);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		try {
			appConfig.setTitle(new AppConfig().getTitle());
		} catch (DataException e1) {
			e1.printStackTrace();
		}
		try {
			appConfig.setTitleImage(new AppConfig().getTitleImage());
		} catch (DataException e) {
			e.printStackTrace();
		}
		try {
			appConfig.setLogo(new AppConfig().getLogo());
		} catch (DataException e) {
			e.printStackTrace();
		}
		try {
			appConfig.setHeader(new AppConfig().getHeader());
		} catch (DataException e) {
			e.printStackTrace();
		}
		try {
			appConfig.setConsolePath(new AppConfig().getConsolePath());
		} catch (DataException e) {
			e.printStackTrace();
		}
		try {
			appConfig.setSystemMail(new AppConfig().getSystemMail());
		} catch (DataException e) {
			e.printStackTrace();
		}
		try {
			appConfig.setSystemMailPassword(new AppConfig().getSystemMailPassword());
		} catch (DataException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化 context.xml, 定義資料庫名稱及密碼
	 * 
	 */
	private void initContextxml() {
		XMLParser parser = new XMLParser(appRoot);
		parser.setContextParam_docBase("zerojudge");
		parser.setContextParam("Resource", "username", dbuser);
		parser.setContextParam("Resource", "password", dbpassword);
		parser.setDBHost("127.0.0.1");
		parser.setDBName("zerojudge");
		try {
			parser.writeContextxml();
		} catch (AccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提供 ant 呼叫用
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 4 && new File(args[0]).exists()) {
			new Installer(new File(args[0]), new File(args[1]), args[2], args[3]).run();
		} else {
		}
	}

}
