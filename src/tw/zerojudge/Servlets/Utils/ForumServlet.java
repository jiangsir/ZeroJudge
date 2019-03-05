package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/Forum" })
public class ForumServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ForumDAO forumDao = new ForumDAO();
		int PageNum = Parameter.parsePage(request.getParameter("page"));
		request.setAttribute("pagenum", PageNum);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		request.setAttribute("markedArticles", forumDao.getArticlesByMarked());
		if (problemid != null && problemid.getIsLegal()) {
			request.setAttribute("articles", forumDao.getArticlesByProblemid(onlineUser, problemid, PageNum));
			request.setAttribute("problemid", problemid);
		} else {
			request.setAttribute("articles", forumDao.getArticles1(onlineUser, PageNum));
		}
		request.setAttribute("querystring", StringTool.querystingMerge(request.getQueryString()));
		request.setAttribute("lastpage", forumDao.getLastPage());
		request.getRequestDispatcher("Forum.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
