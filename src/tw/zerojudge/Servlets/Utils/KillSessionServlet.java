package tw.zerojudge.Servlets.Utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Tables.Message;

@WebServlet(urlPatterns = { "/KillSession" })
@RoleSetting
public class KillSessionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sessionid = request.getParameter("sessionid");
		HttpSession session = SessionFactory.getSessionById(sessionid);
		if (session != null) {
			session.invalidate();
			request.setAttribute("Message", "sessionid: " + sessionid + " 已刪除");
		} else {
			request.setAttribute("Message", "sessionid: " + sessionid + " 不存在");
		}
		Message message = new Message(Message.MessageType_INFOR, "刪除 Session");
		new MessageDAO().dispatcher(request, response, message);
	}
}
