package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.TokenTool;
import tw.zerojudge.DAOs.ForumDAO;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Utils.*;

@WebServlet(urlPatterns = { "/NewThread" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class NewThreadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();

		Article newArticle = new Article();
		newArticle.setProblemid(request.getParameter("problemid"));
		newArticle.setArticletype(onlineUser, request.getParameter("articletype"));

		session.setAttribute("token", TokenTool.generateToken());
		request.setAttribute("nextaction", NewThreadServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
		request.setAttribute("article", newArticle);
		request.setAttribute("problemid", new Problemid(request.getParameter("problemid")));

		request.getRequestDispatcher("NewThread.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ForumDAO forumDao = new ForumDAO();
		HttpSession session = request.getSession(false);

		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		Article newarticle = new Article();
		try {
			newarticle.setUserid(onlineUser.getId());
			newarticle.setAccount(onlineUser.getAccount());
			newarticle.setReply(Article.parseReply(request.getParameter("reply")));
			newarticle.setSubject(request.getParameter("subject"));

			Problemid problemid = new Problemid(request.getParameter("problemid"));
			newarticle.setProblemid(problemid);
			newarticle.setArticletype(onlineUser, request.getParameter("articletype"));

			newarticle.setContent(request.getParameter("content"));
			newarticle.setIpfrom(new Utils().getIpList(request).toString());

			forumDao.insert(newarticle);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
		response.sendRedirect("." + ForumServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
	}
}
