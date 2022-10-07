package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Tables.Solution;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = { "/UserGuide.html" })
public class UserGuideServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<Solution> latestsolutions = new SolutionService().getLatestSolutions(10);
		request.setAttribute("latestsolutions", latestsolutions);
		int countsolutions = new SolutionDAO().getCountByRules(new TreeSet<String>());
		request.setAttribute("countsolutions", countsolutions);
		request.setAttribute("misssolutions", latestsolutions.get(0).getId() - countsolutions);
		request.getRequestDispatcher("UserGuide.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
