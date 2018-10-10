package Standalone.Upgrade201307;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.Tables.Contest;

public class UpgradeContest {
	private ContestDAO contestDao = new ContestDAO();

	static enum ACTION {
		upgradeAllContests("依據 Contest 的資料結構將資料庫資料全部讀出，再寫入。"), 
		exit("退出。");
		private String value;

		private ACTION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

	public UpgradeContest(ContestDAO contestDao) {
		this.contestDao = contestDao;
	}

	public void updateAllContest() {
		for (Contest contest : contestDao.getAllContests()) {
			try {
				new ContestService().update(contest);
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
	}
}
