/**
 * idv.jiangsir.upgrade - SqlUpgrade.java
 * 2011/8/14 下午2:06:50
 * jiangsir
 */
package Standalone.UpgradeByR15;

import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Tables.*;

/**
 * @author jiangsir
 * 
 */
public class UpgradeJsondetails {
	ObjectMapper mapper = new ObjectMapper(); 
	tw.zerojudge.DAOs.GeneralDAO db;

	public UpgradeJsondetails() {
		String driver = "com.mysql.jdbc.Driver";
		String jdbc = "jdbc:mysql://127.0.0.1:3306/zerojudge_dev?useUnicode=true&characterEncoding=UTF-8";
		String dbaccount = "root";
		String dbpasswd = "ashsashs";
		int pagesize = 20;
		db = new tw.zerojudge.DAOs.GeneralDAO(driver, jdbc, dbaccount,
				dbpasswd, pagesize);

	}


	private Solution upgrade_info(Solution solution) {
		String details = solution.getDetails();
		String info = "";
		String[] detaillines = details.split("\n");
		if (detaillines[0].indexOf("(") > 0 && detaillines[0].indexOf(")") > 0) {
			info = detaillines[0].substring(detaillines[0].indexOf("(") + 1,
					detaillines[0].indexOf(")"));
		}
		return solution;
	}

	public static void main(String[] argv) {
	}

}
