package Standalone.Upgrade201307;

import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Tables.Problem;

public class UpgradeProblems {

	ProblemDAO problemDao;
	Logger logger = Logger.getLogger(UpgradeProblems.class.getName());
	ObjectMapper mapper = new ObjectMapper(); 

	static enum ACTION {
		preloadAllProblems("preload 所有題目，不做任何資料更動。僅用於測試執行速度。"), //
		upgradeAllProblems("依據 Problem 的資料結構將資料庫資料全部讀出，再寫入。"), //
		exit("退出。");
		private String value;

		private ACTION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

	public UpgradeProblems(ProblemDAO problemDao) {
		this.problemDao = problemDao;
	}

	public void upgradeAllProblems() {
		ProblemService problemService = new ProblemService();
		for (Problem problem : this.problemDao.getAllProblems()) {
			try {
				problemService.update(problem);
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
	}

	public void preloadAllProblems() {
		this.problemDao.getAllProblems();
	}

}
