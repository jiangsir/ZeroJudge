/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.VClass;

public class VClassBean {



	public VClassBean() {
	}

	public VClassBean(OnlineUser sessionUser, VClass vclass) {

		Method[] methods = VClass.class.getDeclaredMethods();
		for (Method method : methods) {
			Object value;
			if (method.getName().startsWith("set")) {
				try {
					Method tablegetter = VClass.class.getMethod(method
							.getName().replaceFirst("set", "get"));
					value = tablegetter.invoke(vclass);
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
}
