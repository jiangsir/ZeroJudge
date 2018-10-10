package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.TreeSet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;

@WebServlet(urlPatterns = { "/ShowLogs" })
@RoleSetting
public class ShowLogsServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request)
			throws AccessException {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		AccessFilter(onlineUser, new IpAddress(request.getRemoteAddr()));
	}

	private boolean AccessFilter(OnlineUser onlineUser, IpAddress ipaddr)
			throws AccessException {
		if (onlineUser.getIsDEBUGGER() || ipaddr.getIsSubnetOf(
				ApplicationScope.getAppConfig().getManager_ip())) {
			return true;
		}
		throw new AccessException(onlineUser, "您沒有權限進行這個動作。");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		LogDAO logDao = new LogDAO();
		String tabid = request.getParameter("tabid");
		String tab = "tab00";

		int tabnum = 0;
		for (Log.TABID logtabid : new Log().getTabids()) {
			tabnum++;
			if (tabid == null || "".equals(tabid)) {
				tabid = logtabid.name();
				tab = "tab0" + tabnum;
			} else if (tabid.equals(logtabid.name())) {
				tab = "tab0" + tabnum;
			}
		}

		//

		TreeSet<String> rules = new TreeSet<String>();
		rules.add("tabid='" + tabid + "'");

		int page = Parameter.parseInteger((request.getParameter("page")));
		request.setAttribute("pagenum", page <= 0 ? 1 : page);
		request.setAttribute("tabs", new Log().getTabids());
		request.setAttribute("tab", tab);
		request.setAttribute("querystring",
				StringTool.querystingMerge(request.getQueryString()));

		request.setAttribute("logs", logDao.getLogs(rules, page));
		request.getRequestDispatcher("/ShowLogs.jsp").forward(request,
				response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LogDAO logDao = new LogDAO();
		String searchword = request.getParameter("searchword");
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("account LIKE '%" + searchword + "%' OR uri LIKE '%"
				+ searchword + "%' OR message LIKE '%" + searchword
				+ "%' OR stacktrace LIKE '%" + searchword + "%'");
		request.setAttribute("tabs", new Log().getTabids());
		request.setAttribute("querystring",
				StringTool.querystingMerge(request.getQueryString()));

		request.setAttribute("logs", logDao.getLogs(rules, 0));
		request.getRequestDispatcher("/ShowLogs.jsp").forward(request,
				response);
	}

}
