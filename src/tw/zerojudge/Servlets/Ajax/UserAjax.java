package tw.zerojudge.Servlets.Ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.UserService;

@WebServlet(urlPatterns = { "/User.ajax" })
@RoleSetting
public class UserAjax extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if ("isAvailableAccount".equals(action)) {
			String account = request.getParameter("account");
			response.getWriter().print(
					new UserService().isAvailableAccount(account));
			return;
		} else if ("otherActions".equals(action)) {

		}
		//
	}
}
