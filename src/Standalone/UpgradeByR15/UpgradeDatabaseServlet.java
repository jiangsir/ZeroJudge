package Standalone.UpgradeByR15;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Message;
import tw.zerojudge.Tables.OnlineUser;

@SuppressWarnings("serial")
public class UpgradeDatabaseServlet extends HttpServlet {
	String session_account;
	static HttpSession session = null;

	public static boolean isAccessible(OnlineUser onlineUser)
			throws AccessException {
		if (!onlineUser.getIsDEBUGGER()) {
			throw new AccessException(onlineUser, "您非管理者，並無存取權限！");
		}
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		try {
			UpgradeDatabaseServlet.isAccessible(UserFactory
					.getOnlineUser(session));
		} catch (AccessException e) {
			e.printStackTrace();
			Log log = new Log(request, e);
			log.setStacktrace("企圖進行資料庫升級, Method=" + request.getMethod()
					+ ", MessageType=ERROR\n" + log.getStacktrace());
			new LogDAO().insert(log);
			new MessageDAO().dispatcher(request, response, log);
			return;
		}
		request.getRequestDispatcher("UpgradeDatabase.jsp").forward(request,
				response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String from_version = request.getParameter("from_version");
		String to_version = request.getParameter("to_version");
		try {
			new UpgradeDatabase(from_version, to_version).upgrade();
		} catch (AccessException e) {
			e.printStackTrace();
			new MessageDAO().dispatcher(request, response, new Message(request,
					e));
			return;
		}

		response.sendRedirect("UpgradeDatabase");

	}
}
