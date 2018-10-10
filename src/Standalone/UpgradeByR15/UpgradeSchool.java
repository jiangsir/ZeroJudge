/**
 * idv.jiangsir.utils.testing - RunSQL.java
 * 2009/2/23 下午 12:42:54
 * jiangsir
 */
package Standalone.UpgradeByR15;

import tw.zerojudge.DAOs.SchoolDAO;

/**
 * @author jiangsir
 * 
 */
public class UpgradeSchool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SchoolDAO schoolsDao = new SchoolDAO();
		String driver = "com.mysql.jdbc.Driver";
		String jdbc = "jdbc:mysql://163.32.92.11:3306/zerojudge_dev?useUnicode=true&characterEncoding=UTF-8";
		String dbaccount = "root";
		String dbpasswd = "";
	}

}
