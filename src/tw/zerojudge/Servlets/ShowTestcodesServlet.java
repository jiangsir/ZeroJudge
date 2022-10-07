package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.TestcodeDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;

@WebServlet(urlPatterns = { "/ShowTestcodes" })
@RoleSetting
public class ShowTestcodesServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static boolean isAccessible(OnlineUser sessionUser) {
		if (sessionUser.getIsDEBUGGER()) {
			return true;
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser sessionUser = UserFactory.getOnlineUser(session);
		if (!ShowTestcodesServlet.isAccessible(sessionUser)) {
			return;
		}

		request.setAttribute("testcodes", new TestcodeDAO().getTestcodes());
		request.getRequestDispatcher("ShowTestcodes.jsp").forward(request,
				response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
