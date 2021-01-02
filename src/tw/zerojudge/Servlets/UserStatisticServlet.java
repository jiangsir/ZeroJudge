package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/UserStatistic", "/Users/*" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class UserStatisticServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public enum TRIED {
		NOT_TRIED, 
		AC, 
		TRIED; 
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		User user;
		if (request.getServletPath().startsWith("/Users")) {
			String[] infos = request.getPathInfo().split("/");
			user = new UserService().getUserByAccount(infos[infos.length - 1]);
		} else if (request.getParameter("account") != null) {
			String account = request.getParameter("account");
			user = new UserService().getUserByAccount(account);
		} else if (request.getParameter("id") != null) {
			int userid = Integer.parseInt(request.getParameter("id"));
			user = new UserService().getUserById(userid);
		} else {
			user = onlineUser;
		}

		if (user != null && !user.isNullUser() && user.getRole().isLowerEqualThan(User.ROLE.MANAGER)
				&& user.isCheckedConfig(User.CONFIG.ENABLE)) {
			ProblemDAO problemDao = new ProblemDAO();
			TreeSet<Problemid> aclist = user.getAclist();
			request.setAttribute("user", user);
			request.setAttribute("problemnum", problemDao.getCountOfOpenedProblem());
			AppConfig appConfig = ApplicationScope.getAppConfig();
			ArrayList<Problemtab> problemtabs = appConfig.getProblemtabs();
			LinkedHashMap<String, TreeMap<Problemid, Integer>> tabs = new LinkedHashMap<String, TreeMap<Problemid, Integer>>();

			for (Problemtab problemtab : problemtabs) {
				TreeMap<Problemid, Integer> tab_problemids = new TreeMap<Problemid, Integer>();
				TreeSet<Problemid> problemidset = new ProblemService().getProblemtabProblemidset()
						.get(problemtab.getId());
				if (problemidset != null) {
					for (Problemid problemid : problemidset) {
						tab_problemids.put(problemid, TRIED.NOT_TRIED.ordinal());
					}
				}
				for (Problemid triedproblemid : user.getTriedset()) {
					if (tab_problemids.containsKey(triedproblemid)) {
						tab_problemids.put(triedproblemid, TRIED.TRIED.ordinal());
					}
				}
				for (Problemid acproblemid : aclist) {
					if (tab_problemids.containsKey(acproblemid)) {
						tab_problemids.put(acproblemid, TRIED.AC.ordinal());
					}
				}
				tabs.put(problemtab.getName(), tab_problemids);
			}

			request.setAttribute("difficultyCounts", this.getDifficultyCounts(user));
			request.setAttribute("allDifficultyCounts", this.getAllDifficultyCounts());
			request.setAttribute("tabs", tabs);
			request.getRequestDispatcher("/UserStatistic.jsp").forward(request, response);
			return;
		} else {
			throw new DataException("沒有資料可以顯示。");
		}
	}

	/**
	 * 取得題目的困難度統計。
	 * 
	 * @return
	 */
	private HashMap<Integer, Integer> getDifficultyCounts(User user) {
		HashMap<Integer, Integer> ans = new HashMap<Integer, Integer>();
		ans.put(0, 0);
		ans.put(1, 0);
		ans.put(2, 0);
		ans.put(3, 0);
		ans.put(4, 0);
		ans.put(5, 0);
		for (Problemid problemid : user.getAclist()) {
			Problem problem = new ProblemService().getProblemByProblemid(problemid);
			if (problem.getDisplay() == Problem.DISPLAY.open) {
				int value = ans.get(problem.getDifficulty());
				ans.put(problem.getDifficulty().intValue(), value + 1);
			}
		}
		return ans;
	}

	/**
	 * 取得所有題目的 DifficultyCounts
	 * 
	 * @return
	 */
	private HashMap<Integer, Integer> getAllDifficultyCounts() {
		Map<Integer, Integer> countsmap = new ProblemDAO().getAllDifficultyCounts();
		HashMap<Integer, Integer> ans = new HashMap<Integer, Integer>();
		ans.put(0, 0);
		ans.put(1, 0);
		ans.put(2, 0);
		ans.put(3, 0);
		ans.put(4, 0);
		ans.put(5, 0);
		for (Integer key : countsmap.keySet()) {
			ans.put(key, countsmap.get(key));
		}
		return ans;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
