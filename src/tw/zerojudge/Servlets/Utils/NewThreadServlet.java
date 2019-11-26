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
import tw.zerojudge.DAOs.ProblemService;
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
		Article.TYPE articleType;
		Article newArticle = new Article();
		try {
			articleType = Article.TYPE.valueOf(request.getParameter("articletype"));
			Problemid problemid = new Problemid(request.getParameter("problemid"));
			if (articleType == Article.TYPE.problemreport
					&& onlineUser.getIsAccepted(new ProblemService().getProblemByProblemid(problemid))) {
				newArticle.setProblemid(problemid);
				newArticle.setArticletype(articleType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if (!TokenTool.isToken(request)) {
			throw new DataException("無效的 submit，請重新整理再試一次。");
		}
		session.removeAttribute("token");

		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		Article newarticle = new Article();
		newarticle.setUserid(onlineUser.getId());
		newarticle.setAccount(onlineUser.getAccount());
		newarticle.setReply(Article.parseReply(request.getParameter("reply")));
		newarticle.setProblemid(new Problemid(request.getParameter("problemid")));
		newarticle.setSubject(request.getParameter("subject"));
		newarticle.setArticletype(request.getParameter("articletype"));
		newarticle.setContent(request.getParameter("content"));

		newarticle.setIpfrom(new Utils().getIpList(request).toString());

		forumDao.insert(newarticle);
		response.sendRedirect("." + ForumServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
	}
}
