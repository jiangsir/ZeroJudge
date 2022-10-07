package tw.zerojudge.Api;

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
import tw.zerojudge.DAOs.IMessageDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.IMessage;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/IMessage.api" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class IMessageApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7225313752592602748L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		String action = request.getParameter("action");
		switch (ACTION.valueOf(action)) {
		case SendIMessage:
			break;
		case doDelete:
			break;
		case doHide:
			break;
		case doRead:
			break;
		case doUnread:
			break;
		default:
			break;
		}
	}

	public static enum ACTION {
		SendIMessage, 
		doRead, 
		doUnread, 
		doHide, 
		doDelete;
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		String action = request.getParameter("action");
		String imessageid = request.getParameter("imessageid");
		IMessage imessage = new IMessageDAO().getIMessageById(Integer.parseInt(imessageid));
		switch (ACTION.valueOf(action)) {
		case SendIMessage:
			break;
		case doDelete:
			if (onlineUser.getAccount().equals(imessage.getReceiver())
					&& imessage.getReceiver_status() == IMessage.RECEIVER_STATUS.unread) {
				imessage.setReceiver_status(IMessage.RECEIVER_STATUS.deleted);
			} else {
				if (onlineUser.getAccount().equals(imessage.getSender())) {
					imessage.setSender_status(IMessage.SENDER_STATUS.deleted);
				}
				if (onlineUser.getAccount().equals(imessage.getReceiver())) {
					imessage.setReceiver_status(IMessage.RECEIVER_STATUS.deleted);
				}
			}
			new IMessageDAO().update(imessage);
			break;
		case doHide:
			if (onlineUser.getAccount().equals(imessage.getReceiver())) {
				imessage.setReceiver_status(IMessage.RECEIVER_STATUS.deleted);
			} else if (onlineUser.getAccount().equals(imessage.getSender())) {
				imessage.setSender_status(IMessage.SENDER_STATUS.deleted);
			}
			new IMessageDAO().update(imessage);
			break;
		case doRead:
			if (onlineUser.getAccount().equals(imessage.getReceiver())) {
				imessage.setReceiver_status(IMessage.RECEIVER_STATUS.readed);
			}
			new IMessageDAO().update(imessage);
			break;
		case doUnread:
			if (onlineUser.getAccount().equals(imessage.getReceiver())) {
				imessage.setReceiver_status(IMessage.RECEIVER_STATUS.unread);
			}
			new IMessageDAO().update(imessage);

			break;

		default:
			break;

		}
		response.getWriter().print("");
		return;
	}
}
