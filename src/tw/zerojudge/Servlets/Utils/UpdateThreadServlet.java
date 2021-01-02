package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.TokenTool;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Utils.*;

@WebServlet(urlPatterns = {"/UpdateThread"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class UpdateThreadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		session.setAttribute("token", TokenTool.generateToken());

		int postid = Integer.parseInt(request.getParameter("postid"));
		request.setAttribute("article", new ForumDAO().getArticleById(postid));
		request.getRequestDispatcher("NewThread.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ForumDAO forumDao = new ForumDAO();
		HttpSession session = request.getSession(false);
		if (!TokenTool.isToken(request)) {
			throw new DataException("無效的 submit，請重新整理再試一次。");
		}
		session.removeAttribute("token");

		int postid = Integer.parseInt(request.getParameter("postid"));

		Article article = new ForumDAO().getArticleById(postid);
		article.setSubject(request.getParameter("subject"));
		article.setArticletype(request.getParameter("articletype"));
		article.setContent(request.getParameter("content"));

		article.setIpfrom(new Utils().getIpList(request).toString());

		forumDao.update(article);
		response.sendRedirect("." + ForumServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
	}
}
