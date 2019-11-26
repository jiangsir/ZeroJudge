package tw.zerojudge.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.JsonObjects.Problemtab;

public class AppConfigService {

	public int insert(AppConfig appConfig) throws DataException {
		new AppConfigDAO().truncate();
		try {
			ApplicationScope.setAppConfig(appConfig);
			return new AppConfigDAO().insert(appConfig);
		} catch (SQLException e) {
			throw new DataException(e);
		}
	}

	public void update(AppConfig appConfig) throws DataException {
		try {
			appConfig.getServerConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		new AppConfigDAO().truncate();
		try {
			new AppConfigDAO().insert(appConfig);
			ApplicationScope.setAppConfig(appConfig);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	public void delete(int id) {
		new AppConfigDAO().delete(id);
	}

	public AppConfig getAppConfig() {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		for (AppConfig appConfig : new AppConfigDAO().getAppConfigByFields(fields, "id DESC", 0)) {
			return appConfig;
		}
		return new AppConfig();
	}

	public int countAll() {
		return new AppConfigDAO().countAll();
	}

	/**
	 * 刪除 problem tabid
	 * 
	 * @param from_tab
	 */
	public void deleteProblemTab(Problemtab from_tab, Problemtab to_tab) {
		if (from_tab == null || to_tab == null) {
			return;
		}
		AppConfig appConfig = this.getAppConfig();
		ArrayList<Problemtab> problemtabs = appConfig.getProblemtabs();
		//
		for (Problemtab problemtab : problemtabs) {
			if (problemtab.getId().equals(from_tab.getId())) {
				new ProblemDAO().updateProblemTab(from_tab, to_tab);
				if (problemtabs.size() > 1) {
					problemtabs.remove(problemtab); 
					this.update(appConfig);
				}
				break;
			}
		}
	}

	/**
	 * 針對空資料庫初始化
	 */
	public void init() {
		if (new AppConfigDAO().countAll() > 0) {
			return;
		}
		this.insert(this.getAppConfig());
	}

}
