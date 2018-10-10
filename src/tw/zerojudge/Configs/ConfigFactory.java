package tw.zerojudge.Configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TreeMap;
import net.htmlparser.jericho.Source;
import tw.jiangsir.Utils.Scopes.ApplicationScope;

public class ConfigFactory {





	//

	/**
	 * 獲取應用程式參數。
	 * 
	 * @return
	 */

	/**
	 * 獲取應用程式參數。
	 * 
	 * @return
	 */



	/**
	 * 取得某個 class 內某個 Persistent name 的 Field
	 * 
	 * @param theclass
	 * @return
	 */


	/**
	 * 從設定檔讀取出來，放入 AppConfig 當中。
	 */
	//

	/**
	 * 將 AppConfig object 當中的設定寫入 AppConfig.xml 檔案當中。
	 * 
	 * @param appConfig
	 */
	//

	/**
	 * 獲取 ENV 環境參數
	 * 
	 * @return
	 */
	public static EnvConfig EnvConfig() {
		return new EnvConfig();
	}

	/**
	 * 取得系統提供的 properties
	 * 
	 * @return
	 */
	public static TreeMap<String, String> getSystemProperties() {
		TreeMap<String, String> list = new TreeMap<String, String>();
		Enumeration<?> enumeration = System.getProperties().keys();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement().toString();
			list.put(name, System.getProperty(name));
		}
		return list;
	}

	/**
	 * 取得 System ENV 列表
	 * 
	 * @return
	 */
	public static TreeMap<String, String> getSystemEnv() {
		TreeMap<String, String> list = new TreeMap<String, String>();
		for (String key : System.getenv().keySet()) {
			list.put(key, System.getenv().get(key));
		}
		return list;
	}

	public static File getAppRoot() {
		return ApplicationScope.getAppRoot();
	}

	public static File getMETA_INF() {
		return new File(
				ApplicationScope.getAppRoot() + File.separator + "META-INF");
	}

	public static File getWebxml() {
		return new File(
				ApplicationScope.getAppRoot() + File.separator + "WEB-INF",
				"web.xml");
	}

	public static Source getWebXmlSource() {
		InputStream is;
		try {
			is = new FileInputStream(getWebxml());
			return new Source(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
