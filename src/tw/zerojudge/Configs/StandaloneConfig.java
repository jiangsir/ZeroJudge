package tw.zerojudge.Configs;

import java.io.File;

public class StandaloneConfig extends AppConfig {
	private String driver = "com.mysql.jdbc.Driver";
	private String jdbc = "jdbc:mysql://127.0.0.1:3306/zerojudge?useUnicode=true&characterEncoding=UTF-8";
	private String dbaccount = "root";
	private String dbpasswd = "ashsashs";
	private File RealPath = new File("");
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getJdbc() {
		return jdbc;
	}

	public void setJdbc(String jdbc) {
		this.jdbc = jdbc;
	}

	public String getDbaccount() {
		return dbaccount;
	}

	public void setDbaccount(String dbaccount) {
		this.dbaccount = dbaccount;
	}

	public String getDbpasswd() {
		return dbpasswd;
	}

	public void setDbpasswd(String dbpasswd) {
		this.dbpasswd = dbpasswd;
	}

}
