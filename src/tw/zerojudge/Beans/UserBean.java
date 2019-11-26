/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

import tw.zerojudge.Tables.*;

public class UserBean {

	public UserBean(String a, String b, String c) {
	}

	public static String parseSessionid(String sessionid) {
		if (sessionid != null) {
			return sessionid;
		}
		return new User().getSessionid();
	}

	//
	//
	//
	//
	//

}
