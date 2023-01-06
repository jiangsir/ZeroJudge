package tw.zerojudge.Beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

public class TaskBean {
	private OnlineUser sessionUser = UserFactory.getNullOnlineUser();


	public TaskBean() {
		super();
	}

	public TaskBean(OnlineUser sessionUser, Task task) {
		this.setSessionUser(sessionUser);

		Method[] methods = TaskBean.class.getDeclaredMethods();
		for (Method method : methods) {
			Object value;
			if (method.getName().startsWith("set")) {
				try {
					Method tablegetter = TaskBean.class.getMethod(method
							.getName().replaceFirst("set", "get"));
					value = tablegetter.invoke(task);
					Method beansetter = this.getClass().getMethod(
							method.getName(), new Class[] { value.getClass() });
					beansetter.invoke(this, new Object[] { value });
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
