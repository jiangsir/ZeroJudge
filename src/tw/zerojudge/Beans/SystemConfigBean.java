/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.JsonObjects.Problemtab;

public class SystemConfigBean {
	private String session_account;
	private AppConfig appConfig;


	public SystemConfigBean() {
		appConfig = ApplicationScope.getAppConfig();
	}

	//



	public boolean isIS_CONTESTMODE() {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		return appConfig.getSystemMode() == AppConfig.SYSTEM_MODE.CONTEST_MODE;
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

	private String tabid = "";

	public void setTabid(String tabid) {
		this.tabid = tabid;
	}

	public String getTabname() {
		for (Problemtab problemtab : appConfig.getProblemtabs()) {
			if (this.tabid.equals(problemtab.getId())) {
				return problemtab.getName();
			}
		}
		return "";
	}

}
