package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/Ranking" })
public class RankingServlet extends HttpServlet {

	private static final long serialVersionUID = 8693390120404810280L;


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		UserDAO userDao = new UserDAO();
		int pagenum = Parameter.parsePage(request.getParameter("page"));
		String tabn = request.getParameter("tab");
		request.setAttribute("pagenum", pagenum);
		ArrayList<User> users = new ArrayList<User>();
		if (tabn == null || "".equals(tabn) || "tab02".equals(tabn)) { 
			users = userDao.getUsersByROLEUSER(pagenum);
		} else if ("tab03".equals(tabn)) { 
			HttpSession session = request.getSession(false);
			int schoolid = 0;
			try {
				schoolid = Integer.parseInt(request.getParameter("schoolid"));
			} catch (Exception e) {
				OnlineUser onlineUser = new SessionScope(session)
						.getOnlineUser();
				if (onlineUser == null || onlineUser.isNullOnlineUser()) {
					throw new DataException("請登入後再進行此一動作。");
				}
				schoolid = new UserService()
						.getUserById(onlineUser.getId()).getSchoolid();
			}
			users = userDao.getusersBySchoolid(schoolid, pagenum);
		} else {
			users = userDao.getUsersByTabLIKE(tabn, pagenum);
		}

		String querystring = StringTool.querystingMerge(request
				.getQueryString());
		request.setAttribute("querystring", querystring);
		request.setAttribute("users", users);
		request.setAttribute("problemnum",
				new ProblemDAO().getCountOfOpenedProblem());
		request.getRequestDispatcher("Ranking.jsp").forward(request, response);
	}
}
