package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.IMessageDAO;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = {"/SendIMessage"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class SendIMessageServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String to = request.getParameter("account");
		request.setAttribute("to", to);
		request.getRequestDispatcher("SendIMessage.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		SessionScope sessionScope = new SessionScope(session);
		OnlineUser onlineUser = sessionScope.getOnlineUser();
		String to = request.getParameter("to");
		String[] accounts = new String[]{};
		if (to != null && !"".equals(to)) {
			accounts = to.split(",");
		}
		for (int i = 0; i < accounts.length; i++) {
			IMessage imessage = new IMessage();
			imessage.setSender(onlineUser.getAccount());
			imessage.setReceiver(accounts[i].trim());
			imessage.setSubject(request.getParameter("subject"));
			imessage.setContent(request.getParameter("content"));
			new IMessageDAO().sendIMessage(imessage, sessionScope.getHostURI());
		}
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}
}
