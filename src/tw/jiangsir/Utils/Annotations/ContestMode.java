package tw.jiangsir.Utils.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.http.HttpServlet;

/**
 * 負責設定當系統進入 Contest Mode 時的權限設定：<br>
 * 
 * @author jiangsir
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContestMode {

	/**
	 * 
	 * 
	 * @return
	 */
	Class<? extends HttpServlet>[] denys() default {};

	/**
	 * 
	 * 
	 * @return
	 */
	Class<? extends HttpServlet>[] allows() default {};

}
