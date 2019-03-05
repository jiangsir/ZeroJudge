package tw.zerojudge.Servlets.Utils;

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
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Utils.*;

@WebServlet(urlPatterns = { "/Reply" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class ReplyServlet extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ForumDAO forumDao = new ForumDAO();
		Integer origid = Article.parseArticleId(request.getParameter("origid"));
		Integer previd = Article.parseArticleId(request.getParameter("previd"));
		Article origContent = forumDao.getArticleById(origid);
		Article prevContent = forumDao.getArticleById(previd);

		request.setAttribute("origContent", origContent);
		request.setAttribute("prevContent", prevContent);
		request.setAttribute("nextaction", "Reply");
		request.getRequestDispatcher("NewReply.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ForumDAO forumDao = new ForumDAO();
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		Article newarticle = new Article();
		newarticle.setUserid(onlineUser.getId());
		newarticle.setAccount(onlineUser.getAccount());
		newarticle.setReply(Article.parseReply(request.getParameter("reply")));
		newarticle
				.setProblemid(new Problemid(request.getParameter("problemid")));
		newarticle.setSubject(request.getParameter("subject"));
		newarticle.setArticletype(request.getParameter("articletype"));
		newarticle.setContent(request.getParameter("content"));
		newarticle.setIpfrom(new Utils().getIpList(request));
		forumDao.insertReply_PSTMT(newarticle);
		response.sendRedirect("."
				+ ForumServlet.class.getAnnotation(WebServlet.class)
						.urlPatterns()[0]);
	}
}
