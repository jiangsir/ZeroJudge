package tw.zerojudge.Api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Article;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/Article.api" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class ArticleApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum ACTION {
		setType, 
		setHidden;
	};

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		String action = request.getParameter("action");
		OnlineUser onlineUser = UserFactory.getOnlineUser(request.getSession(false));
		switch (ACTION.valueOf(action)) {
		case setHidden:
			if (!onlineUser.getIsHigherEqualThanMANAGER()) {
				throw new AccessException("您沒有權限！");
			}
			break;
		case setType:
			if (!onlineUser.getIsHigherEqualThanMANAGER()) {
				throw new AccessException("您沒有權限！");
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		String action = request.getParameter("action");
		Integer postid = Article.parseArticleId(request.getParameter("postid"));
		Article article = new ForumDAO().getArticleById(postid);
		switch (ACTION.valueOf(action)) {
		case setType:
			Article.TYPE articletype = Article.TYPE.valueOf(request.getParameter("articletype"));
			article.setArticletype(articletype);
			new ForumDAO().update(article);
			response.sendRedirect(new SessionScope(session).getPreviousPage());
			return;
		case setHidden:
			if (!onlineUser.getIsHigherEqualThanMANAGER()) {
				throw new AccessException("您沒有權限！");
			}

			Article.HIDDEN hidden = Article.HIDDEN.valueOf(request.getParameter("articlehidden"));
			article.setHidden(hidden);
			new ForumDAO().update(article);
			response.sendRedirect(new SessionScope(session).getPreviousPage());
			return;
		default:
			break;

		}
		response.getWriter().print("");
		return;
	}
}
