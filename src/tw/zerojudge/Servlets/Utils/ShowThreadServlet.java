package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ShowThread" })
public class ShowThreadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Article> articles = new ArrayList<Article>();
		if (request.getParameter("postid") != null) {
			Integer postid = Article.parseArticleId(request
					.getParameter("postid"));
			Integer reply = Article.parseReply(request.getParameter("reply"));
			articles = new ForumDAO().getArticles(postid, reply);
		}
		request.setAttribute("articles", articles);
		request.getRequestDispatcher("ShowThread.jsp").forward(request,
				response);
	}
}
