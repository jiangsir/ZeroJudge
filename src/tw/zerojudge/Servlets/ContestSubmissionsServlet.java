package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.TLDs.ContestTLD;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.TaskDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.ContestFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ContestSubmissions" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ContestSubmissionsServlet extends HttpServlet implements IAccessFilter {

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

	/**
	 * 存取被拒的話，可依 Exception 取得原因。
	 * 
	 * @param onlineUser
	 * @param contest
	 * @throws AccessException
	 */
	public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (contest == null) {
			contest = ContestFactory.getNullcontest();
		}

		if (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) {
			return;
		}
		if (onlineUser.getJoinedcontestid().intValue() == contest.getId().intValue() && contest.getIsRunning()) {
			return;
		}
		new ShowContestServlet().AccessFilter(onlineUser, contest);

		if (contest.getVclassid().intValue() > 0) {
			if (ContestTLD.getIsStudent(onlineUser, contest) && contest.getIsStopped()
					&& contest.isCheckedConfig(Contest.CONFIG.ContestRanking)) {
				return;
			}
		} else {
			if (contest.getIsStopped() && contest.getVisible() == Contest.VISIBLE.open
					&& contest.isCheckedConfig(Contest.CONFIG.ContestSubmissions)) {
				return;
			}
		}

		if (contest.getIsRunning() && onlineUser.getJoinedcontestid().intValue() != contest.getId().intValue()) {
			throw new AccessException(
					"這個競賽(\"" + contest.getTitle() + "\")進行中，但您(" + onlineUser.getAccount() + ") 並未參加這個競賽");
		}
		if (contest.getIsPausing()) {
			throw new AccessException("這個競賽(\"" + contest.getTitle() + "\")暫停中，暫不顯示");
		}


		if (contest.getIsStopped() && !contest.isCheckedConfig(Contest.CONFIG.ContestSubmissions)) {
			throw new AccessException("Redirect");
		} else {
		}

		if (contest.getIsStopped() && contest.getVisible() == Contest.VISIBLE.open
				&& !contest.isCheckedConfig(Contest.CONFIG.ContestSubmissions)) {
			throw new AccessException("這個競賽(\"" + contest.getTitle() + "\")這個競賽並不開放參觀");
		}

		if (ContestTLD.getIsStudent(onlineUser, contest) && contest.getIsStopped()
				&& !contest.isCheckedConfig(Contest.CONFIG.ContestSubmissions)) {
			throw new AccessException("這個競賽(\"" + contest.getTitle() + "\")不開放觀看參觀");
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		String problemid = request.getParameter("problemid");

		String userid = request.getParameter("userid");
		int PageNum = Parameter.parsePage(request.getParameter("page"));
		ContestDAO contestdao = new ContestDAO();
		if (contest.getId() > 0 && userid != null && userid.matches("[0-9]+")) {
			request.setAttribute("solutions",
					new SolutionService().getSolutionsByUseridContestid(Integer.parseInt(userid), contest.getId(), 0));
		} else if (contest.getId() > 0 && problemid != null) {
			request.setAttribute("solutions", new SolutionService().getSolutionsByContestidProblemid(contest.getId(),
					new ProblemService().getProblemByProblemid(new Problemid(problemid)).getId()));
		} else if (contest.getId() > 0) {
			request.setAttribute("solutions", contest.getSolutions());
		}

		request.setAttribute("removedSolutions", contest.getRemovedSolutions());
		request.setAttribute("testJudgeids", contest.getTestJudgeids());
		request.setAttribute("querystring", StringTool.querystingMerge(request.getQueryString()));
		Task contestTask = new TaskDAO().getTaskById(contest.getTaskid());
		request.setAttribute("taskmap", contestTask);
		request.setAttribute("contestcreater", contest.getOwner().getAccount());
		request.setAttribute("contest", contest);
		request.setAttribute("lastpage", contestdao.getLastpage());
		request.setAttribute("pagenum", PageNum);
		request.getRequestDispatcher("ContestSubmissions.jsp").forward(request, response);
	}

}
