package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Tools.FileTools;

@WebServlet(urlPatterns = { "/ShowCatalina" })
@RoleSetting
public class ShowCatalinaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String logpath = System.getProperty("catalina.home")
				+ System.getProperty("file.separator") + "logs"
				+ System.getProperty("file.separator");
		Iterator<String> it2 = new FileTools().getFilenames(logpath, ".*")
				.keySet().iterator();
		while (it2.hasNext()) {
		}
		request.setAttribute("logpath", logpath);
		request.setAttribute("logfiles",
				new FileTools().getFilenames(logpath, ".*"));
		request.getRequestDispatcher("ShowCatalina.jsp").forward(request,
				response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
