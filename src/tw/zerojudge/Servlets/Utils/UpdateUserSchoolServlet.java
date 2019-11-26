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
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/UpdateUserSchool" })
@RoleSetting
public class UpdateUserSchoolServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Integer schoolid = Parameter.parseInteger(request
				.getParameter("schoolid"));
		User user = new UserService().getUserByAccount(request
				.getParameter("account"));
		user.setSchoolid(schoolid);
		new UserService().update(user);
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}
}
