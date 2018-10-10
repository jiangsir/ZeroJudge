package tw.jiangsir.Utils.Listeners;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.AppConfigService;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Utils.ENV;
import tw.zerojudge.Utils.Utils;

@WebListener
public class MyHttpSessionListener implements HttpSessionListener, ServletRequestListener {

	private HttpServletRequest request;

	public void requestDestroyed(ServletRequestEvent event) {

	}

	public void requestInitialized(ServletRequestEvent event) {
		request = (HttpServletRequest) event.getServletRequest();


	}

	public void sessionCreated(HttpSessionEvent event) {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		HttpSession session = event.getSession();
		SessionFactory.putSession(session);

		SessionScope sessionScope = new SessionScope(session);
		sessionScope.setSession_locale(request.getLocale());
		sessionScope.setSession_ipset(new Utils().getIpList(request));
		sessionScope.setSession_useragent(request.getHeader("user-agent") + ", " + request.getHeader("cookie"));
		sessionScope.setSession_requestheaders(new Utils().getRequestHeaders(request));
		sessionScope.setHostURI(request);

		ApplicationScope.getOnlineSessions().put(session.getId(), session);


		int MAX_IP_CONNECTION = appConfig.getMaxConnectionByIP();
		IpAddress remoteAddr = new IpAddress(request.getRemoteAddr());
		if (ENV.IP_CONNECTION.containsKey(remoteAddr)) {
			if (ENV.IP_CONNECTION.get(remoteAddr) < MAX_IP_CONNECTION) {
				ENV.IP_CONNECTION.put(remoteAddr, ENV.IP_CONNECTION.get(remoteAddr) + 1);
			} else {
				ENV.IP_CONNECTION.put(remoteAddr, 0);

				if (!remoteAddr.getIsSubnetOf(appConfig.getSearchEngines())
						&& !appConfig.getBannedIPSet().contains(remoteAddr)) {
					Log log = new Log(request);
					log.setTabid(Log.TABID.BANNED);
					log.setMessage("IP:" + remoteAddr + " 同時連線數超過 " + MAX_IP_CONNECTION + "，擋掉。");
					new LogDAO().insert(log);
					appConfig.getBannedIPSet().add(remoteAddr);
					new AppConfigService().update(appConfig);
				}

			}
		} else {
			ENV.IP_CONNECTION.put(remoteAddr, 1);
		}
		HashMap<String, String> loggingmap = new HashMap<String, String>();
		Enumeration<?> enu = request.getHeaderNames();
		while (enu.hasMoreElements()) {
			String HeaderName = enu.nextElement().toString();
			loggingmap.put(HeaderName, request.getHeader(HeaderName));
		}
	}

	public void sessionDestroyed(final HttpSessionEvent event) {
		final HttpSession session = event.getSession();
		SessionScope sessionScope = new SessionScope(session);
		for (IpAddress session_ip : sessionScope.getSession_ipset()) {
			if (ENV.IP_CONNECTION.containsKey(session_ip)
					&& ENV.IP_CONNECTION.get(session_ip) > 1) {
				ENV.IP_CONNECTION.put(session_ip, ENV.IP_CONNECTION.get(session_ip) - 1);
			} else {
				ENV.IP_CONNECTION.remove(session_ip);
			}
		}
		String sessionid = session.getId();
		SessionFactory.removeSession(sessionid);

		synchronized (ApplicationScope.getOnlineUsers()) {
			ApplicationScope.getOnlineUsers().remove(sessionid);
		}
		synchronized (ApplicationScope.getOnlineSessions()) {
			ApplicationScope.getOnlineSessions().remove(sessionid);
		}

	}

}
