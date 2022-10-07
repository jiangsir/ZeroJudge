package tw.zerojudge.JsonObjects;

import java.util.LinkedHashMap;

/**
 * @author jiangsir
 * 
 */
public class Schema {
	private String version = "Unknown";
	LinkedHashMap<String, LinkedHashMap<String, String>> tables = new LinkedHashMap<String, LinkedHashMap<String, String>>();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getTables() {
		return tables;
	}

	public void setTables(
			LinkedHashMap<String, LinkedHashMap<String, String>> tables) {
		this.tables = tables;
	}

}
