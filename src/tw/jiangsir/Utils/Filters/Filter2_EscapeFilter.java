package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import tw.jiangsir.Utils.Wrappers.EscapeWrapper;

/**
 * Servlet Filter implementation class EscapeFilter
 */
@WebFilter(filterName = "EscapeFilter", urlPatterns = { "/*" }, asyncSupported = true)
public class Filter2_EscapeFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public Filter2_EscapeFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest requestWrapper = new EscapeWrapper(
				(HttpServletRequest) req);
		chain.doFilter(requestWrapper, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
