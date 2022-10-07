package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.Tables.Message;

@WebServlet(urlPatterns = { "/RemoveSessions" })
@RoleSetting
public class RemoveSessionsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		StringBuffer text = new StringBuffer(5000);
		if (session == null) {
			text.append("Session 不存在 ");
		} else {
			Enumeration<?> enumeration = session.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement().toString();
				text.append(name + " = " + session.getAttribute(name)
						+ " --- 已被刪除<br>");
				session.removeAttribute(name);
			}
		}
		if (text.toString().equals("")) {
			text.append("Session 內沒有任何資料，因此沒有資料被刪除");
		}
		new MessageDAO().dispatcher(request, response, new Message(
				Message.MessageType_INFOR, "刪除所有的 sessions", text.toString()));
	}

}
