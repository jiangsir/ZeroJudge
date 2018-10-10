package Standalone.Setup;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import Standalone.Upgrade201307.UpgradeContest;
import Standalone.Upgrade201307.UpgradeContestant;
import Standalone.Upgrade201307.UpgradeProblems;
import Standalone.Upgrade201307.UpgradeSolutions;
import Standalone.Upgrade201307.UpgradeUsers;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;

public class Rebuilt {

	SolutionDAO solutionDao;
	ProblemDAO problemDao;
	Logger logger = Logger.getLogger(Rebuilt.class.getName());
	ObjectMapper mapper = new ObjectMapper(); // can reuse, share
	int PAGESIZE = 20;

	static enum ACTION {
		rebuiltAllUsers("針對全體 user 進行 rebuilt 重新計算答對題數。"), //
		currectAllUsers("將全部的 user 讀出來，若資料有誤，就改成預設值。"), //
		exit("退出。");
		private String value;

		private ACTION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String[] webappspath = new String[] {
				"/var/lib/tomcat7/webapps/ZeroJudge7/",
				"/var/lib/tomcat7/webapps/ROOT/",
				"/Users/jiangsir/DynamicWebProjects/ZeroJudge7/WebContent/" };
		for (int i = 0; i < webappspath.length; i++) {
			System.out.println((i + 1) + ". " + webappspath[i]);
		}
		System.out.print("請選擇 webapps 絕對路徑：");

		File appRoot = new File(webappspath[scanner.nextInt() - 1]);
		System.out.println(appRoot);
		ApplicationScope.setAppRoot(appRoot);
		String driver = "com.mysql.jdbc.Driver";
		String jdbc = "jdbc:mysql://127.0.0.1:3306/zerojudge?useUnicode=true&characterEncoding=UTF-8";
		System.out.print("請輸入資料庫帳號：");
		String dbaccount = scanner.next();
		System.out.print("請輸入資料庫密碼：");
		String dbpasswd = scanner.next();
		while (true) {
			System.out.println("所有動作：");
			int count = 0;
			for (ACTION action : ACTION.values()) {
				System.out.println(++count + ". " + action.name() + ": ("
						+ action.getValue() + ")");
			}
			System.out.print("情選擇要執行的動作：");
			ACTION action = ACTION.values()[scanner.nextInt() - 1];
			SolutionDAO solutionDao = new SolutionDAO();
			solutionDao.setConnection(driver, jdbc, dbaccount, dbpasswd);
			ProblemDAO problemDao = new ProblemDAO();
			problemDao.setConnection(driver, jdbc, dbaccount, dbpasswd);
			ContestDAO contestDao = new ContestDAO();
			contestDao.setConnection(driver, jdbc, dbaccount, dbpasswd);
			ContestantDAO contestantDao = new ContestantDAO();
			contestDao.setConnection(driver, jdbc, dbaccount, dbpasswd);
			UpgradeProblems updateProblems = new UpgradeProblems(problemDao);
			UpgradeContestant updateContestant = new UpgradeContestant(
					contestDao, contestantDao);
			UpgradeContest updateContest = new UpgradeContest(contestDao);
			UpgradeSolutions updateSolutions = new UpgradeSolutions(
					solutionDao, problemDao);
			UserDAO userDao = new UserDAO();
			userDao.setConnection(driver, jdbc, dbaccount, dbpasswd);
			UpgradeUsers updateUsers = new UpgradeUsers(userDao);

			switch (action) {
			case rebuiltAllUsers:
				new UserService().rebuiltAllUserStatistic();
				return;
			case currectAllUsers:
				new UserService().currectAllUserStatistic();
				return;
			case exit:
				return;
			}
		}
	}

}
