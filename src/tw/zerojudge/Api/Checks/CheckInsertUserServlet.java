package tw.zerojudge.Api.Checks;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/CheckUser.api" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class CheckInsertUserServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1336123130335663471L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		try {
			user.setAccount(request.getParameter("account"));
			user.setUsername(request.getParameter("username"));
			user.setTruename(request.getParameter("truename"));
			user.setBirthyear(request.getParameter("birthyear"));
			user.setEmail(request.getParameter("email"));

			new UserService().checkInsert(user);
		} catch (DataException e) {
			e.printStackTrace();
			response.getWriter().print(e.getLocalizedMessage());
			return;
		}
		response.getWriter().print("");
		return;
	}
}
