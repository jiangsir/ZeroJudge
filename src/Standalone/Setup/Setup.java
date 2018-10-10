package Standalone.Setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;
import tw.jiangsir.Utils.Scopes.ApplicationScope;

/**
 *  - Setup.java
 * 2009/3/13 下午 02:11:13
 * jiangsir
 */

/**
 * @author jiangsir
 * 
 */
public class Setup {
	private String BasePath;
	// private String webxml_path;
	private String contextxml_path;
	// private String myproperties_path;
	// Properties props = null;
	public String contextxml = "";
	// public String properties_path = "";
	private Document doc = null;
	private Document doc_contextxml = null;
	private String dbhost = "";
	private String dbname = "";
	private String dbusername = "";
	private String dbpassword = "";

	public enum CONTEXT_PARAM {
		username, //
		connectionName, //
		password, //
		connectionPassword, //
		url, //
		connectionURL;//
	}

	public Setup(String BasePath) {
		// 如果是解壓縮後直接執行 Setup 就應指定 ZeroJudge
		// 20090320 因為將 Setup.sh Setup.class 都直接放入 ZeroJudge 根目錄，因此不必再指定路徑了
		// this.BasePath = "./../ZeroJudge/";
		this.BasePath = BasePath;
		// this.webxml_path = this.BasePath + "/WEB-INF/web.xml";
		this.contextxml_path = this.BasePath + "/META-INF/context.xml";
		// this.myproperties_path = this.BasePath + "/META-INF/properties.xml";
		// this.properties_path = BasePath + "/META-INF/properties.xml";
		this.contextxml = this.readContextxml();
		// this.initProperty();
	}

	private String readContextxml() {
		BufferedReader breader = null;
		StringBuffer text = new StringBuffer(50000);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.contextxml_path);
			breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while ((line = breader.readLine()) != null) {
				text.append(line + " ");
			}
			breader.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text.toString();
	}

	void writeContextxml() {
		XMLOutputter outp = new XMLOutputter();
		FileOutputStream fo = null;

		try {
			fo = new FileOutputStream(this.contextxml_path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			outp.output(this.doc_contextxml, fo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 設定 context.xml
	 * 
	 * @param key
	 * @param value
	 */
	public void setContextParam_OLD(String key, String value) {
		if ("".equals(value.trim())) {
			return;
		}
		if (this.contextxml.matches(".*" + key + "=\".*\".*")) {
			this.contextxml = this.contextxml.replaceAll(key + "=\".[^\"]*\"", key + "=\"" + value + "\"");
		}
	}

	public void setContextDBSetting(String dbhost, String dbname, String dbusername, String dbpassword) {
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_contextxml == null) {
				this.doc_contextxml = builder.build(new File(this.contextxml_path));
			}
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Get the root element
		Element root = this.doc_contextxml.getRootElement();
		// Print servlet information
		// List<?> children = root.getChildren();
		// Iterator<?> i = children.iterator();
		// while (i.hasNext()) {
		// Element child = (Element) i.next();
		Element resource = root.getChild("Resource");
		System.out.println(resource);
		resource.setAttribute(CONTEXT_PARAM.url.name(), "jdbc:mysql://" + this.getDbhost() + ":3306/" + this.getDbname()
				+ "?useUnicode=true&characterEncoding=UTF-8");
		resource.setAttribute(CONTEXT_PARAM.username.name(), dbusername);
		resource.setAttribute(CONTEXT_PARAM.password.name(), dbpassword);
		Element manager = root.getChild("Manager");
		if (manager != null) {
			Element store = manager.getChild("Store");
			store.setAttribute(CONTEXT_PARAM.connectionName.name(), dbusername);
			store.setAttribute(CONTEXT_PARAM.connectionPassword.name(), dbpassword);
			store.setAttribute(CONTEXT_PARAM.connectionURL.name(), "jdbc:mysql://" + this.getDbhost() + ":3306/"
					+ this.getDbname() + "?useUnicode=true&characterEncoding=UTF-8");
		}
	}

	/**
	 * 取得 context.xml
	 * 
	 * @param key
	 * @return
	 */
	public String getContextParam_OLD(String key) {
		String tmp = this.contextxml;
		String value = "";
		System.out.println("this.contextxml=" + this.contextxml);
		System.out.println("key=" + key);
		if (tmp.matches(".*" + key + "=\".*\".*")) {
			tmp = tmp.substring(tmp.indexOf(key + "=\""), tmp.length());
			System.out.println("tmp=" + tmp);
			value = tmp.substring(key.length() + 1, tmp.indexOf("\""));
		}
		return value;
	}

	public String getContextParam(CONTEXT_PARAM key) {
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_contextxml == null) {
				this.doc_contextxml = builder.build(new File(this.contextxml_path));
			}
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Get the root element
		Element root = this.doc_contextxml.getRootElement();
		// Print servlet information
		List<?> children = root.getChildren();
		Iterator<?> i = children.iterator();

		while (i.hasNext()) {
			Element child = (Element) i.next();
			if ("Resource".equals(child.getName())) {
				return child.getAttributeValue(key.name());
			}
		}
		return null;
	}

	public String getDbhost() {
		return dbhost;
	}

	public void setDbhost(String dbhost) {
		if ("".equals(dbhost)) {
			return;
		}
		this.dbhost = dbhost;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		if ("".equals(dbname)) {
			return;
		}
		this.dbname = dbname;
	}

	public String getDefault_DBHost() {
		String url = getContextParam(CONTEXT_PARAM.url);
		// jdbc: mysql: //
		// 127.0.0.1:3306/zerojudge?useUnicode=true&characterEncoding=UTF-8
		return url.substring(url.indexOf("//") + 2, url.indexOf(":3306"));
	}

	//
	// public void setDBHost(String host) {
	// if ("".equals(host)) {
	// return;
	// }
	// String url = "jdbc:mysql://" + host
	// + ":3306/zerojudge?useUnicode=true&amp;characterEncoding=UTF-8";
	// // String url = this.getContextParam(CONTEXT_PARAM.url);
	// url = url.replaceFirst(this.getDBHost(), host);
	// this.setContextParam(CONTEXT_PARAM.url, url);
	// this.setContextParam(CONTEXT_PARAM.connectionURL, url);
	// }

	public String getDbusername() {
		return dbusername;
	}

	public void setDbusername(String dbusername) {
		if ("".equals(dbusername)) {
			return;
		}
		this.dbusername = dbusername;
	}

	public String getDbpassword() {
		return dbpassword;
	}

	public void setDbpassword(String dbpassword) {
		if ("".equals(dbpassword)) {
			return;
		}
		this.dbpassword = dbpassword;
	}

	public String getDefault_DBName() {
		String url = getContextParam(CONTEXT_PARAM.url);
		// jdbc: mysql: //
		// 127.0.0.1:3306/zerojudge_dev?useUnicode=true&characterEncoding=UTF-8
		return url.substring(url.indexOf("3306/") + 5, url.indexOf("?"));
	}

	//
	// public void setDBName(String name) {
	// if ("".equals(name)) {
	// return;
	// }
	// String url = this.getContextParam(CONTEXT_PARAM.url);
	// url = url.replaceFirst(this.getDBName(), name);
	// this.setContextParam(CONTEXT_PARAM.url, url);
	// }

	// public void setDburl() {
	// String url = "jdbc:mysql://" + this.getDbhost() + ":3306/"
	// + this.getDbname()
	// + "?useUnicode=true&amp;characterEncoding=UTF-8";
	// this.setContextDBSetting(CONTEXT_PARAM.url, url);
	// this.setContextDBSetting(CONTEXT_PARAM.connectionURL, url);
	// }
	//
	// public void setDbusername(String dbusername) {
	// if (dbusername.equals("")) {
	// return;
	// }
	// this.setContextDBSetting(CONTEXT_PARAM.username, dbusername);
	// this.setContextDBSetting(CONTEXT_PARAM.connectionName, dbusername);
	// }
	//
	// public void setDbpassword(String dbpassword) {
	// if ("".equals(dbpassword)) {
	// return;
	// }
	// this.setContextDBSetting(CONTEXT_PARAM.password, dbpassword);
	// this.setContextDBSetting(CONTEXT_PARAM.connectionPassword, dbpassword);
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("/****************************************************************/");
		System.out.println("// ZeroJudge 設定程式                     ");

		System.out.println("// 說明：");
		System.out.println("// 每個選項後的括號代表預設值，直接按 ENTER 將直接套用預設值");
		System.out.println("");
		System.out.println("/***************************************************************/");
		System.out.print("您的 webapps 路徑是否為 \"" + args[0] + "\"? (Y/N)");
		if ("Y".equals(scanner.nextLine())) {
			ApplicationScope.setAppRoot(new File(args[0]));
		} else {
			// String[] webappspath = new String[] {
			// "/var/lib/tomcat7/webapps/ZeroJudge7/",
			// "/var/lib/tomcat7/webapps/ROOT/",
			// "/var/lib/tomcat6/webapps/ZeroJudge/",
			// "/var/lib/tomcat6/webapps/ROOT/",
			// "/Users/jiangsir/DynamicWebProjects/ZeroJudge7/WebContent/",
			// "/Users/jiangsir/DynamicWebProjects/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/ZeroJudge7"
			// };
			// for (int i = 0; i < webappspath.length; i++) {
			// System.out.println((i + 1) + ". " + webappspath[i]);
			// }
			System.out.print("請自行輸入 webapps 絕對路徑：");
			File appRoot = new File(scanner.nextLine());
			ApplicationScope.setAppRoot(appRoot);
		}
		// System.out.println("appRoot=" + ApplicationScope.getAppRoot());
		// ApppConfig appConfig = ApplicationScope.getApppConfig();
		// System.out.println(appConfig.getHeader());
		// String BasePath = "./";
		// if (args.length > 0) {
		// BasePath = args[0];
		// }
		Setup setup = new Setup(ApplicationScope.getAppRoot().getPath());
		setup.setDbhost(setup.getDefault_DBHost());
		System.out.print("請指定資料庫主機位置： (" + setup.getDbhost() + ")：");
		setup.setDbhost(scanner.nextLine());

		setup.setDbname(setup.getDefault_DBName());
		System.out.print("請指定資料庫名稱： (" + setup.getDbname() + ")：");
		setup.setDbname(scanner.nextLine());

		setup.setDbusername(setup.getContextParam(CONTEXT_PARAM.username));
		System.out.print("請指定資料庫使用者帳號： (" + setup.getDbusername() + ")：");
		setup.setDbusername(scanner.nextLine());

		setup.setDbpassword(setup.getContextParam(CONTEXT_PARAM.password));
		System.out.print("請指定資料庫使用者密碼：(" + setup.getDbpassword() + ")：");
		setup.setDbpassword(scanner.nextLine());

		setup.setContextDBSetting(setup.getDbhost(), setup.getDbname(), setup.getDbusername(), setup.getDbpassword());

		System.out.println("\n注意!! 若系統正在執行當中，修改這些設定 apps 將會自動重啟。");
		System.out.print("確定(yes/no)?");
		if ("yes".equals(scanner.nextLine())) {
			setup.writeContextxml();
			// setup.writetoWebxml();
			// ConfigFactory.writeAppConfig(appConfig);
			// System.out.println("設定已寫入!!");
		} else {
			System.out.println("設定未生效!!");
		}

	}
}
