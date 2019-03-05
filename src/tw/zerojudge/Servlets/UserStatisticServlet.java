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
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/UserStatistic" })
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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		User user;
		if (request.getParameter("account") != null) {
			String account = request.getParameter("account");
			user = new UserService().getUserByAccount(account);
		} else if (request.getParameter("id") != null) {
			int userid = Integer.parseInt(request.getParameter("id"));
			user = new UserService().getUserById(userid);
		} else {
			user = onlineUser;
		}

		if (user.getRole().isLowerEqualThan(User.ROLE.MANAGER)
				&& user.isCheckedConfig(User.CONFIG.ENABLE)) {
			ProblemDAO problemDao = new ProblemDAO();
			TreeSet<Problemid> aclist = user.getAclist();
			request.setAttribute("user", user);
			request.setAttribute("problemnum",
					problemDao.getCountOfOpenedProblem());
			AppConfig appConfig = ApplicationScope.getAppConfig();
			ArrayList<Problemtab> problemtabs = appConfig.getProblemtabs();
			LinkedHashMap<String, TreeMap<Problemid, Integer>> tabs = new LinkedHashMap<String, TreeMap<Problemid, Integer>>();

			for (Problemtab problemtab : problemtabs) {
				TreeMap<Problemid, Integer> tab_problemids = new TreeMap<Problemid, Integer>();

				TreeSet<Problemid> problemidset = new ProblemService()
						.getProblemtabProblemidset().get(problemtab.getId());
				if (problemidset != null) {
					for (Problemid problemid : problemidset) {
						tab_problemids.put(problemid,
								TRIED.NOT_TRIED.ordinal());
					}
				}
				for (Problemid triedproblemid : user.getTriedset()) {
					if (tab_problemids.containsKey(triedproblemid)) {
						tab_problemids.put(triedproblemid,
								TRIED.TRIED.ordinal());
					}
				}
				for (Problemid acproblemid : aclist) {
					if (tab_problemids.containsKey(acproblemid)) {
						tab_problemids.put(acproblemid, TRIED.AC.ordinal());
					}
				}
				tabs.put(problemtab.getName(), tab_problemids);
			}

			request.setAttribute("tabs", tabs);
			request.getRequestDispatcher("UserStatistic.jsp").forward(request,
					response);
			return;
		} else {
			throw new DataException("沒有資料可以顯示。");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
