package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ContestService;

@WebServlet(urlPatterns = { "/Contests" })
public class ContestsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		if (page == null || !page.matches("[0-9]+")) {
			page = "1";
		}
		request.setAttribute("pagenum", page);

		ContestService contestService = new ContestService();
		request.setAttribute("runningContests",
				contestService.getRunningContests());
		request.setAttribute("pausingContests",
				contestService.getPausingContests());
		request.setAttribute("startingContests",
				contestService.getStartingContests());
		request.setAttribute("stopedContests",
				contestService.getStopedContests(Integer.parseInt(page)));
		request.setAttribute("querystring",
				StringTool.querystingMerge(request.getQueryString()));
		request.getRequestDispatcher("Contests.jsp").forward(request, response);
	}
}
