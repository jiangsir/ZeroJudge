package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Tables.OnlineUser;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "AccessFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class Filter5_AccessFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public Filter5_AccessFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		HttpServlet httpServlet = ApplicationScope.getUrlpatterns().get(request.getServletPath());
		if (httpServlet == null) { 
			chain.doFilter(request, response);
			return;
		}

		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		if (onlineUser != null && !onlineUser.isNullOnlineUser()
				&& !this.isAccessible(onlineUser, httpServlet.getClass(), request)) {
			ArrayList<String> debugs = new ArrayList<String>();
			debugs.add("本頁面 " + httpServlet.getClass().getAnnotation(WebServlet.class).urlPatterns()[0] + " 瀏覽角色：");
			debugs.add("使用者：" + onlineUser.getAccount() + " 角色：" + onlineUser.getRole());
			debugs.add("可用servlet 共" + ApplicationScope.getRoleMap().get(onlineUser.getRole()).size() + "個:"
					+ ApplicationScope.getRoleMap().get(onlineUser.getRole()));

			Alert alert = new Alert(Alert.TYPE.ROLEERROR, "您(" + onlineUser.getAccount() + ")沒有權限瀏覽這個頁面。",
					httpServlet.getClass().getAnnotation(WebServlet.class).urlPatterns()[0], "", null, debugs);
			throw new AccessException(alert);
		}

		/**
		 * 最後一關，確定該使用者已經具備存取這個頁面之後，才進行判斷。<br>
		 * 由 servlet 獲得的參數來決定是否可以存取。固定寫在 servlet.AccessFilter(request);
		 */
		for (Class<?> iface : httpServlet.getClass().getInterfaces()) {
			if (iface == IAccessFilter.class) {
				for (Method method : IAccessFilter.class.getMethods()) {
					try {
						Method servletMethod = httpServlet.getClass().getDeclaredMethod(method.getName(),
								HttpServletRequest.class);
						servletMethod.invoke(httpServlet.getClass().newInstance(), new Object[]{request});
					} catch (SecurityException e) {
						e.printStackTrace();
						throw new AccessException(e);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						throw new AccessException(e);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new AccessException(e);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new AccessException(e);
					} catch (InvocationTargetException e) {
						throw new AccessException(e.getTargetException());
					} catch (InstantiationException e) {
						e.printStackTrace();
						throw new AccessException(e);
					}
				}
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * 綜合判斷 user 是否能存取所指定的 url，<br>
	 * 包含檢查<br>
	 * 1. 檢查整站模式：進入競賽模式，系統關閉<br>
	 * 2. 使用者角色 ROLE <br>
	 * 3. 使用者狀態 如：使用者進入競賽中，或成為題目管理員、站務管理員。<br>
	 * 4. 檢查某些參數不允許使用，如 UpdateUser?account=xxx 除了 ADMIN, 其它帳號都不能修改別人的資料。
	 * 
	 * @param onlineUser
	 * @param httpServletClazz
	 * @return
	 * @throws Throwable
	 */
	private boolean isAccessible(OnlineUser onlineUser, Class<? extends HttpServlet> httpServletClazz,
			HttpServletRequest request) {

		if (onlineUser != null && onlineUser.getIsHigherEqualThanMANAGER()) {
			return true;
		}

		//
		/**
		 * 處理各個使用者的特權：題目管理員、站務管理員<br>
		 * 或者因加入競賽而需要取消的權限。
		 */
		if (onlineUser.getDenyServlet().contains(httpServletClazz)) {
			throw new RoleException(
					"您的權限(" + httpServletClazz.getAnnotation(WebServlet.class).urlPatterns()[0] + ") 已經被暫時取消。");
		}
		if (!onlineUser.getAllowServlet().contains(httpServletClazz)) {
			throw new RoleException(
					"您沒有這個權限(" + httpServletClazz.getAnnotation(WebServlet.class).urlPatterns()[0] + ")");
		}
		return true;

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
