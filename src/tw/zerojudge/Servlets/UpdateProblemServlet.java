package tw.zerojudge.Servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Api.Checks.CheckProblemServlet;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.ProblemFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.Problem.DISPLAY;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/UpdateProblem" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class UpdateProblemServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getAnonymousLogger();

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		this.AccessFilter(onlineUser, new ProblemService().getProblemByProblemid(problemid));
	}

	public void AccessFilter(OnlineUser onlineUser, Problem problem) throws AccessException {
		if (onlineUser == null || onlineUser.isNullOnlineUser()) {
			throw new AccessException("您沒有登入！");
		}
		if (!onlineUser.getIsDEBUGGER() && !onlineUser.getIsMANAGER()) {
			if (((Problem.DISPLAY.open == problem.getDisplay()) || Problem.DISPLAY.verifying == problem.getDisplay()
					|| Problem.DISPLAY.practice == problem.getDisplay() || (!"".equals(onlineUser.getAccount())))
					&& problem.getIsOwner(onlineUser)) {
				return;
			} else {
				throw new AccessException("您沒有權限管理這個題目！");
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		request.setAttribute("nextaction",
				UpdateProblemServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0].substring(1));
		request.setAttribute("tabs", ApplicationScope.getAppConfig().getProblemtabs());
		request.setAttribute("problem", problem);
		request.setAttribute("suggest_keywords",
				new ProblemService().getSuggestKeywords(session, request.getServletPath()));
		request.setAttribute("suggest_backgrounds",
				new ProblemService().getSuggestBackgrounds(session, request.getServletPath()));
		request.setAttribute("suggest_references",
				ProblemFactory.getSuggestReferences(session, request.getServletPath()));

		request.getRequestDispatcher("EditProblem.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		new CheckProblemServlet().checkUpdate(request);


		problem.setBackgrounds(request.getParameter("backgrounds"));
		problem.setContent(request.getParameter("content"));
		problem.setTheinput(request.getParameter("theinput"));
		problem.setTheoutput(request.getParameter("theoutput"));
		problem.setSampleinput(request.getParameter("sampleinput"));
		problem.setSampleoutput(request.getParameter("sampleoutput"));
		problem.setHint(request.getParameter("hint"));

		problem.setScores(request.getParameterValues("scores"));
		problem.setTimelimits(request.getParameterValues("timelimits"));

		if (problem.getDisplay() == DISPLAY.unfinished) {
			problem.setDisplay(DISPLAY.hide);
		}

		if (Problem.DISPLAY.verifying == problem.getDisplay()) {
			Log log = new Log(request);
			log.setMessage(problem.getProblemid() + " 設定為 '已提交'");
			new LogDAO().insert(log);
		}

		problem.setSamplecode(request.getParameter("samplecode"));
		problem.setComment(request.getParameter("comment"));
		problem.setReference(request.getParameter("reference"));
		problem.setUpdatetime(new Timestamp(System.currentTimeMillis()));

		problem = new ProblemService().downloadProblemAttachfiles(problem, request.getServerPort());

		problem.setTitle(request.getParameter("title"));
		problem.setMemorylimit(request.getParameter("memorylimit"));
		problem.setLanguage(request.getParameter("language"));

		problem.setAlteroutdata(request.getParameter("alteroutdata"));

		new ProblemService().update(problem);

		response.sendRedirect(
				"." + this.getClass().getAnnotation(WebServlet.class).urlPatterns()[0] + "?problemid=" + problemid);
	}
}
