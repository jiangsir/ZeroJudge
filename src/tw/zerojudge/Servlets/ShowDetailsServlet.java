package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Judges.ServerOutput.JUDGEMENT;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;

/**
 * 
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/ShowDetails" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ShowDetailsServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		Integer solutionid = Solution.parseSolutionid(request.getParameter("solutionid"));
		String problemid = request.getParameter("problemid");
		if (solutionid != 0 && problemid == null) {
			new ShowDetailsServlet().AccessFilter(request);
		} else if (solutionid == 0 && problemid != null && !"".equals(problemid)) {
			new UpdateProblemServlet().AccessFilter(request);
		}

		this.AccessFilter(onlineUser, solution);
	}

	public void AccessFilter(OnlineUser onlineUser, Solution solution) throws AccessException {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		Contest contest = new ContestService().getContestById(solution.getContestid());
		if (onlineUser.getIsHigherEqualThanMANAGER() || contest.getIsOwner(onlineUser)) {
			return;
		}
		if (solution.getContestid().intValue() > 0 && solution.getIsVisible_incontest() && contest.getIsRunning()
				&& !contest.isCheckedConfig(Contest.CONFIG.ShowDetail)) {
			throw new AccessException("本競賽主辦人設定「不顯示」解題詳情！");
		}
		if (solution.getUser().getAccount().equals(onlineUser.getAccount())) {
			return;
		}
		throw new AccessException("解題詳情不允許存取！");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Integer solutionid = Solution.parseSolutionid(request.getParameter("solutionid"));
		String problemid = request.getParameter("problemid");
		Problem problem = null;
		String errmsg = "";
		String author = "";

		if (solutionid != 0 && problemid == null) { 
			Solution solution = new SolutionService().getSolutionById(solutionid);
			problem = solution.getProblem();

			if (solution.getJudgement() == ServerOutput.JUDGEMENT.NA
					&& problem.getWa_visible() == Problem.WA_visible_HIDE) {
				String[] lines = solution.getDetails().split("\n");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].startsWith("***")) {
						errmsg += lines[i] + "\n";
					}
				}
			} else {
				errmsg = solution.getDetails();
			}
		} else if (solutionid == 0 && problemid != null && !"".equals(problemid)) { 
			problem = new ProblemService().getProblemByProblemid(new Problemid(problemid));

			errmsg = mapper.writeValueAsString(problem.getServeroutputs());
		} else {
		}

		request.setAttribute("details", errmsg);
		request.setAttribute("author", author);
		request.setAttribute("problem", problem);
		request.getRequestDispatcher("ShowDetails.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
