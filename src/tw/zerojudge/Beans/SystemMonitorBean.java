/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

import java.util.ArrayList;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.AttributeFactory;
import tw.zerojudge.Tables.User;

public class SystemMonitorBean {

	public SystemMonitorBean() {
	}

	public String getLivetime() {
		AppAttributes appAttributes = AttributeFactory.readAppAttributes();
		long livetime = System.currentTimeMillis() - appAttributes.getLastContextRestart().getTime();
		long min = (livetime - (livetime % 60000)) / 60000;
		long hour = (min - (min % 60)) / 60;
		long day = (hour - (hour % 24)) / 24;
		return day + " 天 " + (hour % 24) + " 小時 " + (min % 60) + " 分";
	}

	public ArrayList<User> getProblemManagers() {
		return new UserService().getProblemManagers();
	}

	public ArrayList<User> getVClassManagers() {
		return new UserService().getVClassManagers();
	}

//	public ArrayList<User> getGeneralManagers() {
//		return new UserService().getGeneralManagers();
//	}


}
