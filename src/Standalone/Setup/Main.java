package Standalone.Setup;

import java.util.Scanner;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.SolutionDAO;

public class Main {

	SolutionDAO solutionDao;
	ProblemDAO problemDao;
	Logger logger = Logger.getLogger(Main.class.getName());
	ObjectMapper mapper = new ObjectMapper(); // can reuse, share
	int PAGESIZE = 20;

	static enum ACTION {
		Setup("設定系統設定值。"), //
		Rebuilt("進行資料庫重整。"), //
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
		// while (true) {
		if (args.length >= 1) {
			System.out.println("當前目錄：" + args[0]);
		}
		// String dir = System.getProperty("user.dir");
		// System.out.println("user.dir=" + dir);
		// File directory = new File(".");
		// try {
		// System.out.println(directory.getCanonicalPath());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }// 取得當前路徑

		// for (File root : File.listRoots()) {
		// System.out.println("root=" + root.getAbsolutePath());
		// for (File file : FileTools.findFiles(root, "Debug.jsp")) {
		// System.out.println(file.getPath());
		// }
		// }

		System.out.println("所有動作：");
		int count = 0;
		for (ACTION action : ACTION.values()) {
			System.out.println(++count + ". " + action.name() + ": ("
					+ action.getValue() + ")");
		}
		System.out.print("情選擇要執行的動作：");
		int index = Integer.parseInt(scanner.nextLine());

		ACTION action = ACTION.values()[index - 1];

		switch (action) {
		case Rebuilt:
			Rebuilt.main(args);
			return;
		case Setup:
			Setup.main(args);
			return;
		case exit:
			return;
		}
	}
	// }
}
