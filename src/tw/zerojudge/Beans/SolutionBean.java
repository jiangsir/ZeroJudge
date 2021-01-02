/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.zerojudge.Tables.Solution;

public class SolutionBean {

    ObjectMapper mapper = new ObjectMapper(); 


    public SolutionBean() {
    }

    public SolutionBean(HttpSession session, Solution solution) {
	if (session != null) {
	}

	for (Field field : Solution.class.getDeclaredFields()) {
	    Persistent persistent = field.getAnnotation(Persistent.class);
	    if (persistent == null)
		continue;
	    String name = persistent.name();
	    name = name.replaceFirst(name.substring(0, 1), name.toUpperCase()
		    .substring(0, 1));
	    Object value;
	    try {
		Method getter = Solution.class.getMethod("get" + name);
		value = getter.invoke(solution);

		Method setter = this.getClass().getMethod("set" + name,
			new Class[] { value.getClass() });
		setter.invoke(this, new Object[] { value });

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


    //
    //
    //

}
