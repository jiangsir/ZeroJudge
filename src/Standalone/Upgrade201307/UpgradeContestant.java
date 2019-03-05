package Standalone.Upgrade201307;

import java.util.ArrayList;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Contestant;

public class UpgradeContestant {
	private ContestDAO contestDao = new ContestDAO();
	private ContestantDAO contestantDao = new ContestantDAO();

	static enum ACTION {
		upgradeAllContestants("依據 Contestant 的資料結構將資料庫資料全部讀出，再寫入。"), //
		exit("退出。");
		private String value;

		private ACTION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

	public UpgradeContestant(ContestDAO contestDao, ContestantDAO contestantDao) {
		this.contestDao = contestDao;
		this.contestantDao = contestantDao;
	}

	public void updateAllContestants() {
		for (Contest contest : contestDao.getAllContests()) {
			for (Contestant contestant : contestantDao
					.getAllContestants(contest.getId())) {
				contestantDao.update(contestant);
			}
		}
	}

	private void test1() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("aaa");
		list.add("bbb");
		list.add("aaa");
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		list2.add(1);
		list2.add(2);
		list2.add(1);
		ArrayList<Double> list3 = new ArrayList<Double>();
		list3.add(1.0);
		list3.add(2.0);
		list3.add(1.0);
	}

}
