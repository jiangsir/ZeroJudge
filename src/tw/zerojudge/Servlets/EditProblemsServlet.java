package tw.zerojudge.Servlets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = {"/EditProblems"}, description = "編輯所有符合權限的題目")
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class EditProblemsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		AppConfig appConfig = ApplicationScope.getAppConfig();

		TreeSet<String> rules = new TreeSet<String>();

		String tabid = request.getParameter("tabid");
		String orderby = "updatetime DESC";
		String tab = "tab";
		ArrayList<Problem> problems = new ArrayList<Problem>();

		if (tabid == null || "".equals(tabid)) {
			tabid = Problem.TAB_MYPROBLEM;
		}
		int i = 0;
		for (Problemtab problemtab : appConfig.getProblemtabs()) {
			if (problemtab.getId().equals(tabid)) {
				tab = "tab" + new DecimalFormat("00").format(i);
				rules.add("tabid='" + tabid + "'");
				orderby = problemtab.getOrderby();
			}
			i++;
		}

		String searchword = request.getParameter("searchword");
		if (searchword == null) {
			searchword = "";
		}
		if ("GET".equals(request.getMethod())) {
			searchword = new String(searchword.getBytes("UTF-8"), "UTF-8");
		}

		int page = Parameter.parsePage(request.getParameter("page"));
		request.setAttribute("pagenum", page);

		String author = request.getParameter("author");
		if (author != null) {
			author = onlineUser.getAccount();
		}
		if (searchword == null || "".equals(searchword)) {
		} else {
			rules.add("title LIKE '%" + searchword + "%' OR backgrounds LIKE '%" + searchword + "%' OR content LIKE '%"
					+ searchword + "%' OR theinput LIKE '%" + searchword + "%' OR theoutput LIKE '%" + searchword
					+ "%' OR hint LIKE '%" + searchword + "%' OR tab LIKE '%" + searchword + "%' OR reference LIKE '%"
					+ searchword + "%' OR keywords LIKE '%" + searchword + "%'");
		}

		request.setAttribute("querystring", StringTool.querystingMerge(request.getQueryString()));

		if (Problem.TAB_MYPROBLEM.equals(tabid)) {
			tab = "tab" + new DecimalFormat("00").format(10);
			problems = new ProblemService().getMyProblems(onlineUser, 0);
		} else if (Problem.TAB_NOTOPEN.equals(tabid)) {
			tab = "tab" + new DecimalFormat("00").format(11);
			problems = new ProblemService().getNotopenProblems(onlineUser, 0);
		} else if (Problem.TAB_VERIFYING.equals(tabid)) {
			tab = "tab" + new DecimalFormat("00").format(13);
			problems = new ProblemService().getVerifyingProblems(onlineUser, 0);
		} else if (Problem.TAB_SPECIAL.equals(tabid)) {
			tab = "tab" + new DecimalFormat("00").format(14);
			problems = new ProblemService().getSpecialProblems(onlineUser, 0);
		} else {
			problems = new ProblemService().getEditableProblems(onlineUser, rules, orderby, 0);
		}

		request.setAttribute("problems", problems);
		request.setAttribute("suggest_keywords",
				new ProblemService().getSuggestKeywords(session, request.getServletPath()));
		request.setAttribute("suggest_backgrounds",
				new ProblemService().getSuggestBackgrounds(session, request.getServletPath()));
		request.setAttribute("tab", tab);

		request.getRequestDispatcher("EditProblems.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		String tag = request.getParameter("tag");
		String[] tab_search = request.getParameterValues("tab_search");
		String[] difficulty = request.getParameterValues("difficulty");
		String keyword = request.getParameter("keyword");
		String background = request.getParameter("background");
		String searchword = request.getParameter("searchword");
		String ownerid = request.getParameter("ownerid");
		String locale = request.getParameter("locale");

		TreeSet<String> rules = new ProblemService().getSearchRules(tag, tab_search, difficulty, keyword, background,
				searchword, locale, ownerid);

		ArrayList<Problem> problems = new ProblemService().getEditableProblems(onlineUser, rules, "updatetime DESC", 0);

		String tab = "tab";
		request.setAttribute("tab", tab);
		request.setAttribute("problems", problems);
		request.setAttribute("suggest_keywords",
				new ProblemService().getSuggestKeywords(session, request.getServletPath()));
		request.setAttribute("suggest_backgrounds",
				new ProblemService().getSuggestBackgrounds(session, request.getServletPath()));
		request.setAttribute("lastpage", 1);
		request.setAttribute("pagenum", 1);
		request.getRequestDispatcher("EditProblems.jsp").forward(request, response);
	}
}
