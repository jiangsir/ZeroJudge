package tw.zerojudge.Servlets.Ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/Contest.ajax" })
@RoleSetting
public class ContestAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public static enum ACTION {
		forcedstart, forcedstop, kick, finish, leave, registed, getCountdown;
	};

	private void doAction(HttpSession session, String action, String account,
			int contestid) throws IOException, ServletException,
			AccessException, DataException {
		switch (ACTION.valueOf(action)) {
		case forcedstart:
			break;
		case forcedstop:
			break;
		case kick:
			break;
		case finish:
			break;
		case leave:
		case registed:
		case getCountdown:
		default:
			break;
		}
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		response.setContentType("text/html; charset=utf-8");
		HttpSession session = request.getSession(false);
		String action = request.getParameter("action");
		String account = request.getParameter("account");
		int contestid = Integer.parseInt(request.getParameter("contestid"));
		try {
			this.doAction(session, action, account, contestid);
		} catch (AccessException e) {
			e.printStackTrace();
			Log log = new Log(request, e);
			new LogDAO().insert(log);
			new MessageDAO().dispatcher(request, response, log);
			return;
		}
	}
}
