package tw.zerojudge.Model;

import java.util.*;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.GeneralDAO;

public class Ranking {
	GeneralDAO db;

	private int lastpage = 1;

	public Ranking() {
		this.db = new GeneralDAO();
	}

	/**
	 * @param bywhat
	 * @return
	 */
	public ArrayList<?> doSort(String bywhat) {
		return null;
	}

	/**
	 * 取得 lastpage
	 * 
	 * @return
	 */
	public int getLastpage() {
		return this.lastpage;
	}

	/**
	 * 根據原始 SQL 來取得 lastpage
	 * 
	 * @param SQL
	 */
	private void setLastPage(String SQL) {
		int count = db.executeCount(SQL);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (count % appConfig.getPageSize() == 0) {
			this.lastpage = (count - 1) / appConfig.getPageSize();
		} else {
			this.lastpage = (count - 1) / appConfig.getPageSize() + 1;
		}
	}

	private void setLastPage(int count) {
		this.lastpage = ((count - 1) / ApplicationScope.getAppConfig()
				.getPageSize()) + 1;
	}

	//






}
