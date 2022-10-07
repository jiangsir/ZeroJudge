package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.LoginlogDAO;

@WebServlet(urlPatterns = { "/ManageLoginlog" })
@RoleSetting
public class ManageLoginlogServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		long starttime = System.currentTimeMillis();
		LoginlogDAO loginlogDao = new LoginlogDAO();
		request.setAttribute("loginlog", loginlogDao.getLoginlogs(30));
		request.setAttribute("TimeUsage", System.currentTimeMillis()
				- starttime);
		request.getRequestDispatcher("ManageLoginlog.jsp").forward(request,
				response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
