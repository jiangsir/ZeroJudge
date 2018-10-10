package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/UpdateForum" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class UpdateForumServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Integer postid = Article.parseArticleId(request.getParameter("postid"));
		ForumDAO forumDao = new ForumDAO();
		Article article = forumDao.getArticleById(postid);
		String column = request.getParameter("column");
		if ("articletype".equals(column)) {
			Article.TYPE articletype = Article.TYPE.valueOf(request.getParameter("articletype"));
			if (onlineUser.getIsDEBUGGER()
					|| (article.getProblem().getIsOwner(onlineUser) && articletype.ordinal() < 2)) {
				article.setArticletype(articletype);
				forumDao.update(article);
			}
		} else if ("hidden".equals(column)) {
			article.setHidden(request.getParameter("hidden"));
			forumDao.update(article);
		}
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}
}
