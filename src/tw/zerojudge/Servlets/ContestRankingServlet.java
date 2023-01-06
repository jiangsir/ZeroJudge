package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.TLDs.ContestTLD;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.TaskDAO;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ContestRanking" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
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


		if (onlineUser.getIsDEBUGGER() || onlineUser.getIsMANAGER() || contest.getIsOwner(onlineUser)) {
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

		if (contest.getIsSuspending()) {
			throw new AccessException("這個" + contest.getBundle_Contest(onlineUser.getSession_locale()) + "(\""
					+ contest.getTitle() + "\") 尚未開始，無法觀看結果。");
		}


		if (contest.getIsStopped() && contest.getVisible() == Contest.VISIBLE.open
				&& !contest.isCheckedConfig(Contest.CONFIG.ContestRanking)) {
			throw new AccessException(
					"這個" + contest.getBundle_Contest(onlineUser.getSession_locale()) + "(\"" + contest.getTitle()
							+ "\")這個" + contest.getBundle_Contest(onlineUser.getSession_locale()) + "並不開放“結果”");
		}

		if (ContestTLD.getIsStudent(onlineUser, contest) && contest.getIsStopped()
				&& !contest.isCheckedConfig(Contest.CONFIG.ContestRanking)) {
			throw new AccessException("這個" + contest.getBundle_Contest(onlineUser.getSession_locale()) + "(\""
					+ contest.getTitle() + "\")不開放觀看參觀");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));


		int pagenum = Parameter.parsePage(request.getParameter("page"));
		request.setAttribute("pagenum", pagenum);
		request.setAttribute("querystring", StringTool.querystingMerge(request.getQueryString()));

		ArrayList<Contestant> contestants;
		if (contest.getIsOwner(onlineUser) || onlineUser.getIsHigherEqualThanMANAGER()) {
			contestants = new ContestService().getContestantsForRanking(contest.getId());
			request.setAttribute("contestants", contestants);
		} else {
			if (ApplicationScope.getAppConfig().getIsCLASS_MODE() || contest.isCheckedConfig_ShowOnlyOneContestant()) {
				Contestant onlineContestant = contest.getContestantByUserid(onlineUser.getId());
				ArrayList<Contestant> oneContestant = new ArrayList<Contestant>();
				oneContestant.add(onlineContestant);
				request.setAttribute("contestants", oneContestant);
			} else {
				contestants = new ContestService().getContestantsForRanking(contest.getId());
				request.setAttribute("contestants", contestants);
			}
		}


		ArrayList<VClassStudent> students = new VClassStudentDAO().getStudentsByVclassid(contest.getVclassid());

		request.setAttribute("students", students);
		Task contestTask = new TaskDAO().getTaskById(contest.getTaskid());
		request.setAttribute("taskmap", contestTask);
		request.setAttribute("contest", contest);
		request.getRequestDispatcher("ContestRanking.jsp").forward(request, response);
	}

}
