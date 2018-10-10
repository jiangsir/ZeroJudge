package Standalone.Upgrade201307;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.UserDAO;

public class Main {

	SolutionDAO solutionDao;
	ProblemDAO problemDao;
	Logger logger = Logger.getLogger(Main.class.getName());
	ObjectMapper mapper = new ObjectMapper(); // can reuse, share
	int PAGESIZE = 20;

	static enum ACTION {
		updateServerOutputs(
				"更新 serverOutputs 的資料結構。\n"
						+ "說明：\n"
						+ "更新 ServerOutputs 的 json 資料。以往 serverOutputs 對於多測資題目，"
						+ "如果結果為 SE, CE 時，只會有第一個 serverOutput, 後面的都會是 null. 長度仍為測資個數。<br>"
						+ "本次更新，是要讓只有一個 serverOutput 的多測資結果，縮短為一個。也就是把 null 全部消除。"), //
		updateTLE(
				"新版的 serveroutput 使得 TLE 會讀出 0ms，因為以往的 TLE 時間會紀錄在 info "
						+ "裡而沒有寫入 timeusage, 這個更新就是要將 info 裡面的時間訊息讀出來並重新寫入 timeusage 欄位裡"), //
		upgradeAllContests("依據 Contest 的資料結構將資料庫資料全部讀出，再寫入。"), // test
		upgradeAllContestants("依據 Contestant 的資料結構將資料庫資料全部讀出，再寫入。"), //
		preloadAllProblems("preload 所有題目，不做任何資料更動。僅用於測試執行速度。"), //
		upgradeAllProblems("依據 Problem 的資料結構將資料庫資料全部讀出，再寫入。"), //
		upgradeAllUsers("升級 Users 的資料結構 aclist"), //
		preloadAllUsers("preload 所有User，不做任何資料更動。僅用於測試執行速度。"), //
		MoveTestdata("移動所有測資檔案重 a001.in 移動到 a001/a001.in 加一層目錄，以方便評分機進行同步。"), //
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
		System.out.println("本次更新僅限於 201307 的大改版，改版內容包含\n" + "使用 Factory, \n"
				+ "使用 SuperDAO, \n" + "評分機維護獨立的測資檔，更接近能將評分機獨立出去，\n"
				+ "測資檔目錄改為 a001/a001.in：\n\n");
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
			case updateServerOutputs:
				System.out.print("要進行幾頁？(page==0代表全部)");
				updateSolutions.updateServerOutputs(scanner.nextInt());
				break;
			case updateTLE:
				System.out
						.print("這個 updateTLE 將從最新 solution 往前進行，要更新幾頁？(page==0 代表全部)");
				updateSolutions.updateTLE(scanner.nextInt());
				break;
			case upgradeAllContests:
				updateContest.updateAllContest();
				break;
			case upgradeAllContestants:
				updateContestant.updateAllContestants();
				break;
			case preloadAllProblems:
				updateProblems.preloadAllProblems();
				break;
			case upgradeAllProblems:
				updateProblems.upgradeAllProblems();
				break;
			case preloadAllUsers:
				updateUsers.preloadAllUsers();
				break;
			case upgradeAllUsers:
				updateUsers.upgradeAllUsers();
				break;
			case MoveTestdata:
				new MoveTestdata().run();
				break;
			case exit:
				return;
			}
		}
	}

}
