package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataConnectionException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Exceptions.SystemClosedException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Exceptions.CheckCause;
import tw.zerojudge.Exceptions.CheckException;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "ExceptionHandlerFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class Filter0_ExceptionHandlerFilter implements Filter {
	Logger logger = Logger.getAnonymousLogger();
	/**
	 * Default constructor.
	 */
	public Filter0_ExceptionHandlerFilter() {
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
		try {
			chain.doFilter(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.severe(sw.toString());

			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) resp;
			HttpSession session = request.getSession(false);
			SessionScope sessionScope = new SessionScope(session);

			Throwable rootCause = e;


			Alert alert;
			if (e.getCause() instanceof Alert) {
				alert = (Alert) e.getCause();
			} else {
				alert = new Alert(e);
			}
			alert.getDebugs().add("由 " + this.getClass().getSimpleName() + " 所捕捉");
			ArrayList<String> list = alert.getList();
			while (rootCause.getCause() != null) {
				list.add(rootCause.getClass().getSimpleName() + ": " + rootCause.getLocalizedMessage());
				rootCause = rootCause.getCause();
			}
			try {
				alert.getUris().put("回前頁", new URI(sessionScope.getPreviousPage()));
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}

			if (e instanceof JQueryException) {
				alert = ((JQueryException) e).getAlert();
				ObjectMapper mapper = new ObjectMapper();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write(mapper.writeValueAsString(alert));
				response.flushBuffer();
				return;
			} else {

				Log log = new Log(e);
				OnlineUser onlineUser = sessionScope.getOnlineUser();
				log.setSession_account(onlineUser == null ? "null" : onlineUser.toString());
				log.setTitle(e.getLocalizedMessage());
				log.setIpaddr(sessionScope.getSession_ipset());
				log.setUri(request.getMethod() + ": " + request.getRequestURI()
						+ (request.getQueryString() == null ? "" : "?" + request.getQueryString())
						+ request.getParameterMap());
				log.setMethod(request.getMethod());
				log.setTabid(Log.TABID.WARNING);
				if (e instanceof DataException) {
					log.setTabid(Log.TABID.DATA_EXCEPTION);
				} else if (e instanceof AccessException) {
					log.setTabid(Log.TABID.ACCESS_EXCEPTION);
				} else if (e instanceof CheckException) {
					CheckCause checkCause = (CheckCause) e.getCause();
					alert.getList().addAll(checkCause.getChecks());
				} else if (e instanceof InfoException) {
					alert = (Alert) e.getCause();
				}
				if (!(e instanceof DataConnectionException) && !(e instanceof SystemClosedException)) {
					String content = "OnlineUser = " + (onlineUser == null ? "null" : onlineUser.toString()) + "\n";
					if (request != null) {
						content += request.getMethod() + ": " + request.getServletPath()
								+ (request.getQueryString() == null ? "" : "?" + request.getQueryString()) + " FROM "
								+ request.getRemoteAddr();
					}
					log.setMessage(content);
					new LogDAO().insert(log);
				}
				request.setAttribute("alert", alert);
				request.getRequestDispatcher("/Alert.jsp").forward(request, response);
			}
			return;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
