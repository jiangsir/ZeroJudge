package tw.zerojudge.Servlets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Problem.DISPLAY;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/Problems" })
public class ProblemsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum SERVICE {
		ownerid, 
		author, 
		tabid, 
		display, 
		keyword, //
		background, //
		searchword,//
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String tabid = request.getParameter("tabid");
		String page = request.getParameter("page");
		tabid = StringEscapeUtils.escapeSql(tabid);
		page = StringEscapeUtils.escapeSql(page);
		String method = request.getMethod();
		if (!"POST".equals(method) && (request.getParameterMap().size() == 0 || tabid != null || page != null)) {
			this.searchTabid(request, response);
			return;
		}

		HttpSession session = request.getSession(false);
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
		ArrayList<Problem> problems = new ProblemService().getOpenProblemsByRules(rules, "sortable DESC, problemid ASC",
				0);


		AppConfig appConfig = ApplicationScope.getAppConfig();
		request.setAttribute("tabs", appConfig.getProblemtabs());
		String tab = "tab";
		request.setAttribute("tab", tab);
		request.setAttribute("problems", problems);
		request.setAttribute("suggest_keywords",
				new ProblemService().getSuggestKeywords(session, request.getServletPath()));
		request.setAttribute("suggest_backgrounds",
				new ProblemService().getSuggestBackgrounds(session, request.getServletPath()));
		request.setAttribute("lastpage", 1);
		request.setAttribute("pagenum", 1);
		request.setAttribute("lastpage", 1);

		request.getRequestDispatcher("Problems.jsp").forward(request, response);

	}

	/**
	 * 列出 tabid 題庫。進行分頁。
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	private void searchTabid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String author = request.getParameter("author");
		int page = Parameter.parsePage(request.getParameter("page"));
		request.setAttribute("pagenum", page);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		ArrayList<Problemtab> problemtabs = appConfig.getProblemtabs();
		if (problemtabs.size() == 0) {
			throw new DataException("至少必須有一個題目分類！系統設定檔有誤！請管理員修改。");
		}
		String tabid = request.getParameter("tabid");
		if (tabid == null || "".equals(tabid)) {
			tabid = problemtabs.get(0).getId();
		}
		String orderby = "updatetime DESC";
		String tab = "tab";
		int i = 0;
		for (Problemtab t : problemtabs) {
			if (t.getId().equals(tabid)) {
				tab += new DecimalFormat("00").format(i);
				orderby = t.getOrderby();
				break;
			}
			i++;
		}


		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("display", DISPLAY.open.name());
		if (author != null && !"".equals(author)) {
			fields.put("author", author);
		}
		fields.put("tabid", tabid);

		request.setAttribute("problems", new ProblemService().getOpenProblemsByFields(fields, orderby, page));

		request.setAttribute("suggest_keywords",
				new ProblemService().getSuggestKeywords(session, request.getServletPath()));
		request.setAttribute("suggest_backgrounds",
				new ProblemService().getSuggestBackgrounds(session, request.getServletPath()));
		request.setAttribute("querystring", StringTool.querystingMerge(request.getQueryString()));
		request.setAttribute("lastpage", new ProblemService().getLastPageByFields(fields));

		request.setAttribute("tab", tab);
		request.getRequestDispatcher("Problems.jsp").forward(request, response);
		return;
	}

	//
	//
	//
	//
	//
	//
	//
	//
	//
}
