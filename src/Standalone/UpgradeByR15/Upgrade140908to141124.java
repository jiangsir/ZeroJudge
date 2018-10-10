/**
 * idv.jiangsir.Upgrade - Upgrade.java
 * 2011/11/21 上午11:02:20
 * jiangsir
 */
package Standalone.UpgradeByR15;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.DAOs.GeneralDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.JsonObjects.Compiler;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Tables.Problem;

/**
 * @author jiangsir
 * 
 */
public class Upgrade140908to141124 implements Runnable {
	ObjectMapper mapper = new ObjectMapper(); 

	public Upgrade140908to141124() {
	}

	void upgradeFielddata() {

	}

	public void upgradeSchema() throws AccessException {
		GeneralDAO db = new GeneralDAO();
		db.execute("DROP TABLE IF EXISTS `contestusers`");
		db.execute("DROP TABLE IF EXISTS `errorlog`");
		db.execute("DROP TABLE IF EXISTS `pagelog`");
		db.execute("DROP TABLE IF EXISTS `votes`");
		try {
			db.execute("ALTER TABLE  `problems` CHANGE  `pid`  `id` INT( 11 ) " + "NOT NULL AUTO_INCREMENT;");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		db.execute("CREATE TABLE IF NOT EXISTS `configs` (`id` INT "
				+ "NOT NULL AUTO_INCREMENT PRIMARY KEY , `key` VARCHAR( 100 )"
				+ " NOT NULL ,`value` TEXT NOT NULL , UNIQUE (`key`)) " + "ENGINE = MYISAM CHARACTER SET utf8 COLLATE "
				+ "utf8_general_ci;");
		try {
			db.execute("ALTER TABLE  `problems` ADD  `testfilelength` INT " + "NOT NULL AFTER `title`");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			this.upgradeTestfilelength();
			db.execute("ALTER TABLE  `problems` DROP  `testfiles`");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.execute("ALTER TABLE  `imessages` CHANGE  `imessageid`  `id` " + "INT NOT NULL AUTO_INCREMENT;");
		db.execute("ALTER TABLE  `downloads` CHANGE  `ipfrom`  `ipfrom` "
				+ "VARCHAR( 150 ) CHARACTER SET utf8 COLLATE utf8_general_ci " + "NOT NULL DEFAULT  '0.0.0.0';");
		db.execute("ALTER TABLE  `problems` CHANGE  `code`  `samplecode` "
				+ "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;");
		db.execute("ALTER TABLE  `contests` CHANGE  `detailtime`  " + "`freezelimit` INT( 11 ) NOT NULL DEFAULT  '0';");
		db.execute("ALTER TABLE  `contests` DROP  `contestconfig`");
		db.execute("UPDATE contests SET freezelimit=0 WHERE freezelimit=-1;");


		db.execute("ALTER TABLE  `problems` DROP  `prejudgemsg`");
		db.execute("CREATE TABLE IF NOT EXISTS `images` (" + "`id` int(11) NOT NULL AUTO_INCREMENT,"
				+ " `filename` varchar(255) NOT NULL," + " `bytes` blob NOT NULL,"
				+ " `descript` varchar(255) NOT NULL," + " `visible` tinyint(1) NOT NULL, PRIMARY KEY (`id`)"
				+ " ) ENGINE=MyISAM CHARACTER SET utf8 COLLATE " + "utf8_general_ci;");
	}

	private void upgradeTestfilelength() throws SQLException {
		GeneralDAO db = new GeneralDAO();
		ArrayList<HashMap<String, Object>> maps = db.executeQuery("SELECT * FROM problems");

		Iterator<HashMap<String, Object>> it = maps.iterator();
		while (it.hasNext()) {
			HashMap<String, Object> map = it.next();
			int id = (Integer) map.get("id");
			int testfilenum;
			try {
				testfilenum = ((String) map.get("testfiles")).split(",").length;
			} catch (Exception e) {
				e.printStackTrace();
				testfilenum = 0;
			}
			db.executeUpdate("UPDATE problems SET testfilelength=" + testfilenum + " WHERE id=" + id);
		}
	}

	/**
	 * 將所有不符規定的 compiler 預設為 compilers 中的第一個。
	 * 
	 */
	private void updateDefaultCompiler() {
		ProblemService problemService = new ProblemService();
		for (Problem problem : new ProblemService().getAllProblems()) {
			if (problem.getLanguage() == new Problem().getLanguage()) {
				problem.setLanguage(new Language("C", "c"));
			}
		}
	}

	private void upgradeVersion() throws AccessException {
		GeneralDAO db = new GeneralDAO();
		db.execute("UPDATE configs SET value='R1.4 Built1121' WHERE `key`='version'");
	}

	public void upgrade() {
		try {
			this.upgradeSchema();
		} catch (AccessException e) {
			e.printStackTrace();
		}
		this.upgradeFielddata();
		this.updateDefaultCompiler();
		try {
			this.upgradeVersion();
		} catch (AccessException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		this.upgrade();
	}
}
