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
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Tables.Message;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = { "/Deny" })
@RoleSetting
public class DenyServlet extends HttpServlet implements IAccessFilter {
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
		if ((onlineUser.getIsDEBUGGER() || solution.getContest().getIsOwner(onlineUser))
				&& solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
			return;
		}
		throw new AccessException("您沒有權限進行這一個動作！");

	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer solutionid = Solution.parseSolutionid(request.getParameter("solutionid"));
		HttpSession session = request.getSession(false);
		Message message = new Message();
		message.setPlainTitle("開始進行 deny");
		new MessageDAO().dispatcher(request, response, message);
		return;
	}

}
