package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.TLDs.ContestTLD;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.TaskDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ContestRanking" })
public class ContestRankingServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		this.AccessFilter(onlineUser, contest);
	}

	public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) {
			return;
		}
		if (onlineUser.getJoinedcontestid().intValue() == contest.getId() && contest.getIsRunning()
				&& contest.isCheckedConfig(Contest.CONFIG.ShowResult)) {
			return;
		}
		new ShowContestServlet().AccessFilter(onlineUser, contest);

		if (contest.getIsStopped() && !contest.isCheckedConfig(Contest.CONFIG.ContestRanking)) {
			throw new AccessException("Redirect");
		}


		if (contest.getIsStopped() && contest.getVisible() == Contest.VISIBLE.open
				&& !contest.isCheckedConfig(Contest.CONFIG.ContestRanking)) {
			throw new AccessException("這個競賽(\"" + contest.getTitle() + "\")這個競賽並不開放“競賽結果”");
		}

		if (ContestTLD.getIsStudent(onlineUser, contest) && contest.getIsStopped()
				&& !contest.isCheckedConfig(Contest.CONFIG.ContestRanking)) {
			throw new AccessException("這個競賽(\"" + contest.getTitle() + "\")不開放觀看參觀");
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		int pagenum = Parameter.parsePage(request.getParameter("page"));
		request.setAttribute("pagenum", pagenum);
		request.setAttribute("querystring", StringTool.querystingMerge(request.getQueryString()));
		ArrayList<Contestant> contestants = new ContestService().getContestantsForRanking(contest.getId());
		request.setAttribute("contestants", contestants);
		Task contestTask = new TaskDAO().getTaskById(contest.getTaskid());
		request.setAttribute("taskmap", contestTask);
		request.setAttribute("contest", contest);
		request.getRequestDispatcher("ContestRanking.jsp").forward(request, response);
	}

}
