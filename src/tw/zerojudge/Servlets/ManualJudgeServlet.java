package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = { "/ManualJudge" })
@RoleSetting
public class ManualJudgeServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		this.AccessFilter(onlineUser, solution);
	}

	/**
	 * 
	 * @param onlineUser
	 * @param solution
	 * @throws AccessException
	 */
	public void AccessFilter(OnlineUser onlineUser, Solution solution) throws AccessException {
		Contest contest = new ContestService().getContestById(solution.getContestid());
		if ((onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser))
				&& contest.isCheckedConfig(Contest.CONFIG.ManualJudge)) {
			return;
		}
		throw new AccessException("您沒有權限！");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		throw new InfoException("開始進行 manualjudge");
	}

}
