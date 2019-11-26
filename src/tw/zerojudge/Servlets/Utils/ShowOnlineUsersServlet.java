package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Listeners.Tasks.BannedTask;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/ShowOnlineUsers" })
@RoleSetting(allowHigherThen = User.ROLE.MANAGER)
public class ShowOnlineUsersServlet extends HttpServlet {

	private static final long serialVersionUID = -1265918704923874372L;


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList<SessionScope> onlineSessionScopes = new ArrayList<SessionScope>();
		ArrayList<SessionScope> onlineUserSessionScopes = new ArrayList<SessionScope>();

		for (HttpSession session : ApplicationScope.getOnlineSessions()
				.values()) {
			if (session == null) {
				continue;
			}
			try {
				session.getCreationTime();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			SessionScope sessionScope = new SessionScope(session);
			onlineSessionScopes.add(sessionScope);
			if (sessionScope.getOnlineUser() != null
					&& !sessionScope.getOnlineUser().isNullOnlineUser()) {
				onlineUserSessionScopes.add(sessionScope);
			}
		}

		Collections.sort(onlineSessionScopes, new Comparator<SessionScope>() {
			public int compare(SessionScope o1, SessionScope o2) {
				return o1.getSession_ipset().toString()
						.compareTo(o2.getSession_ipset().toString());
			}
		});
		BannedTask bannedTask = new BannedTask();

		request.setAttribute("sortediplist",
				bannedTask.getSortedIPlist(bannedTask.builtIPTreeMap()));
		request.setAttribute("onlineSessionScopes", onlineSessionScopes);
		request.setAttribute("onlineUserSessionScopes", onlineUserSessionScopes);

		request.getRequestDispatcher("ShowOnlineUsers.jsp").forward(request,
				response);
	}
}
