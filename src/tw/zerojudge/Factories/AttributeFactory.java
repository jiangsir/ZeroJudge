package tw.zerojudge.Factories;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.Attribute;
import tw.zerojudge.Beans.AppAttributes;
import tw.zerojudge.Beans.SessionAttributes;

public class AttributeFactory {
	private static ServletContext servletContext = null;


	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		AttributeFactory.servletContext = servletContext;
	}



	/**
	 * 從給定的 session 當中將 attribute 取出並放入 sessionAttributes 內。
	 * 
	 * @param session
	 * @return
	 */
	public static SessionAttributes readSessionAttributes(HttpSession session) {
		SessionAttributes sessionAttributes = new SessionAttributes(session);
		for (Field field : SessionAttributes.class.getDeclaredFields()) {
			Attribute attribute = field.getAnnotation(Attribute.class);
			if (attribute == null) {
				continue;
			}

			Object value = session.getAttribute(attribute.name());
			if (value == null) {
				continue;
			}
			Method setter;
			try {
				setter = SessionAttributes.class.getMethod("set"
						+ field.getName().toUpperCase().substring(0, 1)
						+ field.getName().substring(1),
						new Class[] { attribute.type() });
				setter.invoke(sessionAttributes, new Object[] { value });
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return sessionAttributes;
	}

	/**
	 * 將 sessionBean 包裝好的 attributes 寫入 指定的 session 內。
	 * 
	 * @param session
	 * @param sessionAttributes
	 */
	public static void writeSessionAttributes(HttpSession session,
			SessionAttributes sessionAttributes) {
		Object value;
		synchronized (session) {
			for (Field field : SessionAttributes.class.getDeclaredFields()) {
				Attribute attribute = field.getAnnotation(Attribute.class);
				if (attribute == null) {
					continue;
				}
				Method getter;
				try {
					getter = SessionAttributes.class.getMethod("get"
							+ field.getName().toUpperCase().substring(0, 1)
							+ field.getName().substring(1));
					value = getter.invoke(sessionAttributes);
					if (value == null) {
						continue;
					}
					session.setAttribute(attribute.name(), value);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 從給定的 session 當中將 attribute 取出並放入 sessionAttributes 內。
	 * 
	 * @param session
	 * @return
	 */
	public static AppAttributes readAppAttributes() {
		AppAttributes appAttributes = new AppAttributes();
		if (servletContext == null) {
			return appAttributes;
		}
		for (Field field : AppAttributes.class.getDeclaredFields()) {
			Attribute attribute = field.getAnnotation(Attribute.class);
			if (attribute == null) {
				continue;
			}

			Object value = servletContext.getAttribute(attribute.name());
			if (value == null) {
				continue;
			}
			Method setter;
			try {
				setter = AppAttributes.class.getMethod("set"
						+ field.getName().toUpperCase().substring(0, 1)
						+ field.getName().substring(1),
						new Class[] { value.getClass() });
				setter.invoke(appAttributes, new Object[] { value });
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return appAttributes;
	}

	/**
	 * 將包裝好的 attributes 寫入 servletContext 內。
	 * 
	 * @param appAttributes
	 */
	public static void writeAppAttributes(AppAttributes appAttributes) {
		Object value;
		if (servletContext == null) {
			return;
		}
		synchronized (servletContext) {
			for (Field field : AppAttributes.class.getDeclaredFields()) {
				Attribute attribute = field.getAnnotation(Attribute.class);
				if (attribute == null) {
					continue;
				}
				Method getter;
				try {
					getter = AppAttributes.class.getMethod("get"
							+ field.getName().toUpperCase().substring(0, 1)
							+ field.getName().substring(1));
					value = getter.invoke(appAttributes);
					if (value == null) {
						continue;
					}
					servletContext.setAttribute(attribute.name(), value);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
