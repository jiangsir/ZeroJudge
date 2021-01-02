package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.Tables.Message;

@WebServlet(urlPatterns = { "/ShowContextAttribute" })
@RoleSetting
public class ShowContextAttributeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Enumeration<?> enumeration = this.getServletContext()
				.getAttributeNames();
		StringBuffer text = new StringBuffer(5000);
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement().toString();
			text.append(name + " = "
					+ this.getServletContext().getAttribute(name) + "<br>");
		}
		if (text.toString().equals("")) {
			text.append("沒有任何 ServletContext Attribute可讀取");
		}
		new MessageDAO().dispatcher(request, response, new Message(
				Message.MessageType_INFOR, "顯示所有的  ServletContext Attribute",
				text.toString()));
	}
}
