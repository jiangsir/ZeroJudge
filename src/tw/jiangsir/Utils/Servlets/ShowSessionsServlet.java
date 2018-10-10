package tw.jiangsir.Utils.Servlets;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Tables.OnlineUser;

@WebServlet(urlPatterns = { "/ShowSessions" })
public class ShowSessionsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	private boolean isAccessible(HttpSession session) {
		SessionScope sessionScope = new SessionScope(session);
		for (IpAddress session_ip : sessionScope.getSession_ipset()) {
			if ((sessionScope != null && sessionScope.getOnlineUser() != null
					&& sessionScope.getOnlineUser()
							.getIsHigherEqualThanMANAGER())
					|| session_ip.getIsSubnetOf(
							ApplicationScope.getAppConfig().getManager_ip())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (!this.isAccessible(session)) {
			return;
		}
		String sessionid = request.getParameter("sessionid");

		if (sessionid != null) {
			session = SessionFactory.getSessionById(sessionid);
		}
		StringBuffer content = new StringBuffer(5000);
		if (session == null) {
			content.append("目前 session = null");
		} else {
			Enumeration<String> enumeration = session.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				if (session.getAttribute(name) instanceof OnlineUser) {
					OnlineUser onlineUser = (OnlineUser) session
							.getAttribute(name);
					content.append("<strong>" + name + "</strong> = "
							+ onlineUser + "(" + onlineUser.getAccount() + ")"
							+ "<br>");
				} else {
					content.append("<strong>" + name + "</strong> = "
							+ session.getAttribute(name) + "<br>");
				}
			}
		}
		if (content.toString().equals("")) {
			content.append("Session 內沒有任何資料");
		}
		throw new InfoException("Session 內資料列表", "", content.toString());
	}
}
