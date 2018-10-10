package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Exceptions.IpException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Servlets.Utils.LogoutServlet;
import tw.zerojudge.Tables.OnlineUser;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "BannedFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class Filter3_BannedFilter implements Filter {
	Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Default constructor.
	 */
	public Filter3_BannedFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String servletPath = req.getServletPath();
		HttpServlet httpServlet = ApplicationScope.getUrlpatterns().get(servletPath);
		IpAddress ip = new IpAddress(request.getRemoteAddr());

		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (appConfig.getBannedIPSet().contains(ip)) {
			resp.getWriter().print("TOO MANY CONNECTIONS, BANNED!");
			return;
		}

		if (httpServlet != null && !ip.getIsSubnetOf(appConfig.getAllowedIP())) {
			logger.info("ip=" + ip + " is not subnet of allowedIP=" + appConfig.getAllowedIP());
			throw new IpException("您所在的位置(" + ip + ")並未允許存取本網站。", ip);
		}

		OnlineUser onlineUser = new SessionScope(req).getOnlineUser();
		if (onlineUser != null && onlineUser.getIsDEBUGGER() && !ip.isLoopbackAddress()
				&& httpServlet.getClass() != LogoutServlet.class) {
			throw new IpException("本帳號為除錯使用，不開放外部登入。", ip);
		}

		if (onlineUser != null && onlineUser.getIsHigherEqualThanMANAGER()
				&& !ip.getIsSubnetOf(appConfig.getManager_ip()) && httpServlet.getClass() != LogoutServlet.class) {
			logger.info("ip=" + ip + " is not subnet of manager_ip=" + appConfig.getManager_ip());
			throw new IpException("為安全起見，在預設環境下，只有本機桌面瀏覽器可用管理員身分登入。<br/>您目前的網路位置(" + ip + ")並未開放管理員登入，"
					+ "請管理員登入本機 「站務管理」->「管理系統參數」->「允許管理員登入的子網段」開放網段。", ip);
		}
		chain.doFilter(req, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}


}
