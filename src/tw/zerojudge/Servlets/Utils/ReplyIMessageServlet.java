package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.IMessageDAO;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.IMessage;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/ReplyIMessage" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class ReplyIMessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		Integer imessageid = Parameter.parseInteger(request.getParameter("imessageid"));
		IMessage imessage = new IMessageDAO().getIMessageById(imessageid);
		if (imessage == null || IMessage.RECEIVER_STATUS.deleted == imessage.getReceiver_status()
				|| !onlineUser.getAccount().toLowerCase().equals(imessage.getReceiver().toLowerCase())) {
			throw new DataException("即時訊息編號不正確！請檢查。\n");
		}
		request.setAttribute("to", imessage.getSender());
		request.setAttribute("subject", "Re: " + imessage.getSubject());
		String[] lines = imessage.getContent().split("\\\n");
		String content = "";
		for (String line : lines) {
			content += "> " + line + "\n";
		}
		request.setAttribute("content", content);
		request.getRequestDispatcher("SendIMessage.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
