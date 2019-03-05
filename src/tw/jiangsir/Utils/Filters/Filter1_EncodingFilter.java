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
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = { "/*" }, asyncSupported = true)
public class Filter1_EncodingFilter implements Filter {
	public enum ENCODING {
		ISO_8859_1("ISO-8859-1"), 
		UTF_8("UTF-8");
		private String value = "";

		ENCODING(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	};


	/**
	 * Default constructor.
	 */
	public Filter1_EncodingFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		req.setCharacterEncoding(ENCODING.UTF_8.getValue());
		resp.setContentType("text/html;charset=" + ENCODING.UTF_8.getValue());
		resp.setCharacterEncoding(ENCODING.UTF_8.getValue());


		chain.doFilter(req, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
