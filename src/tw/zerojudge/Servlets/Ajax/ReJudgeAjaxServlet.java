package tw.zerojudge.Servlets.Ajax;

import java.io.IOException;
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
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/ReJudge.ajax" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ReJudgeAjaxServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		Integer solutionid = Solution.parseSolutionid(request.getParameter("solutionid"));
		HttpSession session = request.getSession(false);
		Solution solution = new SolutionService().getSolutionById(solutionid);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!ApplicationScope.getAppConfig().getRejudgeable()) {
			throw new AccessException("目前系統重測設定關閉中，無法重測(" + solution.getId() + ") BY (" + request.getRequestURI() + "?"
					+ request.getQueryString() + ")");
		}
		this.AccessFilter(onlineUser, solution);
	}

	public void AccessFilter(OnlineUser onlineUser, Solution solution) throws AccessException {
		if (!ApplicationScope.getAppConfig().getRejudgeable()) {
			throw new AccessException("目前系統重測設定關閉中，無法重測(" + solution.getId() + ") BY (isRejudgeable)。");
		}

		if (onlineUser == null || onlineUser.isNullOnlineUser()) {
			throw new AccessException("您並未登入，無法進行重測。");
		}

		if (onlineUser.getIsDEBUGGER()) {
			return;
		}
		if ((solution.getUserid().equals(onlineUser.getId())
				&& (solution.getJudgement() == ServerOutput.JUDGEMENT.Waiting
						|| solution.getJudgement() == ServerOutput.JUDGEMENT.RE
						|| solution.getJudgement() == ServerOutput.JUDGEMENT.SE)
				&& solution.getContestid().intValue() == 0)) {
			return;
		}
		if (solution.getProblem().getIsOwner(onlineUser)) {
			return;
		}
		if (onlineUser.getIsDEBUGGER() || solution.getContest().getIsOwner(onlineUser)) {
			return;
		}
		throw new AccessException("您目前的權限無法進行重測");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		if (request.getParameter("solutionid") != null) {
			Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
			if (onlineUser.isSolutionRejudgable(solution)) {
				solution.doRejudge(onlineUser);
			}
		}
	}

	private int rejudgeProblem(Problem problem, OnlineUser onlineUser) throws IOException {
		try {
			onlineUser.isProblemRejudgable(problem);
		} catch (AccessException e) {
			return 0;
		}

		int count = 0;
		for (Solution solution : new SolutionService().getSolutionsByPid(problem.getId(), 0)) {
			solution.doRejudge(onlineUser);
			count++;
		}
		return count;
	}

}
