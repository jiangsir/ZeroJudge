package tw.jiangsir.Utils.Listeners;

import java.util.logging.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Utils.Utils;

/**
 * Application Lifecycle Listener implementation class MyRequestListener
 * 
 */
@WebListener
public class MyRequestListener implements ServletRequestListener {
	Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Default constructor.
	 */
	public MyRequestListener() {
	}

	/**
	 * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
	 */
	public void requestDestroyed(ServletRequestEvent event) {
	}

	/**
	 * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
	 */
	public void requestInitialized(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
		HttpSession session = request.getSession(true); 

		SessionScope sessionScope = new SessionScope(session);
		sessionScope.setSession_ipset(request);
		sessionScope.setSession_useragent(request.getHeader("user-agent"));
		sessionScope.setSession_requestheaders(new Utils().getRequestHeaders(request));
		sessionScope.setHostURI(request);
	}

}
