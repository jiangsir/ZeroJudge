package tw.jiangsir.Utils.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tw.zerojudge.Tables.User;

/**
 * 三種狀況：<br>
 * 1. 沒有宣告的就代表不需要登入即可瀏覽。<br>
 * 2. 宣告 @RoleSetting 的 Servlet 代表需要登入。<br>
 * 3. 沒有設定任何參數代表只有最高權限者可以瀏覽。
 * 
 * @author jiangsir
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleSetting {
	/**
	 * 指定高於(含)某角色者允許存取。
	 * 
	 * @return
	 */
	User.ROLE allowHigherThen() default User.ROLE.DEBUGGER;

	/**
	 * 指定低於(含)某個角色者禁止存取。
	 * 
	 * @return
	 */
	User.ROLE denyLowerThen() default User.ROLE.GUEST;

	/**
	 * 指定不允許存取的角色，若角色重疊，則 denyRoles 優先於 allowRoles
	 * 
	 * @return
	 */
	User.ROLE[] denys() default {};

	/**
	 * 指定允許存取的角色，若角色重疊，則 denyRoles 優先於 allowRoles
	 * 
	 * @return
	 */
	User.ROLE[] allows() default {};

}
