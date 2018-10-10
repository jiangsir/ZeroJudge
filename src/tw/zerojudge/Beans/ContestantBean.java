/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.OnlineUser;

public class ContestantBean {
	private OnlineUser sessionUser = UserFactory.getNullOnlineUser();


	public ContestantBean() {
	}

	public ContestantBean(OnlineUser sessionUser, Contestant contestant) {
		this.setSessionUser(sessionUser);

		Method[] methods = Contestant.class.getDeclaredMethods();
		for (Method method : methods) {
			Object value;
			if (method.getName().startsWith("set")) {
				try {
					Method tablegetter = Contestant.class.getMethod(method.getName().replaceFirst("set", "get"));
					value = tablegetter.invoke(contestant);
					Method beansetter = this.getClass().getMethod(method.getName(), new Class[]{value.getClass()});
					beansetter.invoke(this, new Object[]{value});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public OnlineUser getSessionUser() {
		return sessionUser;
	}

	public void setSessionUser(OnlineUser sessionUser) {
		this.sessionUser = sessionUser;
	}

}
