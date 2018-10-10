package tw.zerojudge.Servlets.Ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;

@WebServlet(urlPatterns = { "/HtmlAccessiblity" })
@RoleSetting
public class HtmlAccessiblityServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		if ("htmlstatus".equals(action)) {

		} else if ("language".equals(action)) {
		} else if ("codelocker".equals(action)) {
		} else if ("".equals(action)) {
		} else if ("".equals(action)) {
		} else if ("".equals(action)) {

		}

	}
}
