package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.IMessageDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/ShowIMessages" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class ShowIMessagesServlet extends HttpServlet implements IAccessFilter {
	private static final long serialVersionUID = 1L;

	public static enum ACTION {
		inboxIM, //
		unreadIM, //
		sentIM;
	}

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {

	}

	public static boolean isAccessible(String session_account) throws AccessException {
		User user = new UserService().getUserByAccount(session_account);
		if (user.getIsDEBUGGER()) {
			return true;
		}
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		IMessageDAO im = new IMessageDAO();
		synchronized (session) {
			new SessionScope(session).setUnreadIMessages(im.getUnreadIMessage(onlineUser, page));
			request.setAttribute("inboxIM", im.getInboxIMessages(onlineUser, page));
			request.setAttribute("sentIM", im.getSentIMessages(onlineUser, page));
		}
		String action = request.getParameter("action");
		if (action == null || "".equals(action)) {
			request.setAttribute("targetIM", im.getInboxIMessages(onlineUser, page));
		} else {
			switch (ACTION.valueOf(action)) {
			case inboxIM:
				request.setAttribute("targetIM", im.getInboxIMessages(onlineUser, page));
				break;
			case sentIM:
				request.setAttribute("targetIM", im.getSentIMessages(onlineUser, page));
				break;
			case unreadIM:
				request.setAttribute("targetIM", im.getUnreadIMessage(onlineUser, page));
				break;
			default:
				break;

			}
		}
		request.setAttribute("pagenum", page);
		request.setAttribute("querystring", StringTool.querystingMerge(request.getQueryString()));
		request.getRequestDispatcher("ShowIMessages.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
