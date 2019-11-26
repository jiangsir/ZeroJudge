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
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Model.RunDiff;
import tw.zerojudge.Tables.Message;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = { "/Diff" }, description = "檢查兩個程式碼的不同，已不用。")
@RoleSetting
public class DiffServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static boolean isAccessible(OnlineUser onlineUser, int solutionid) {
		Solution solution = new SolutionService().getSolutionById(solutionid);
		if ((onlineUser.getIsDEBUGGER() || solution.getContest().getIsOwner(onlineUser))
				&& solution.getIsJudgement_AC()) {
			return true;
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		String querystring = request.getQueryString();
		if (querystring == null || "".equals(querystring)) {
			return;
		}
		Integer basecodeid = Solution.parseSolutionid(request.getParameter("solutionid"));
		SolutionDAO solutionDAO = new SolutionDAO();
		Solution basecode = new SolutionService().getSolutionById(basecodeid);
		if (!DiffServlet.isAccessible(onlineUser, basecodeid)) {
			Message message = new Message();
			message.setResourceMessage(Message.Resource_YouArenotTheContestCreater);
			new MessageDAO().dispatcher(request, response, message);
			return;
		}

		ArrayList<?> comparecodes = solutionDAO.getDiffCodes(basecodeid, basecode.getContestid());
		if (comparecodes == null || comparecodes.size() == 0) {
			comparecodes = solutionDAO.getCompareCodes(basecodeid);
			solutionDAO.insertDifflog(basecode, comparecodes);
		}
		request.setAttribute("basecode", basecode);
		request.setAttribute("comparecodes", comparecodes);
		Thread rundiff = new Thread(new RunDiff(basecode, comparecodes));
		rundiff.start();
		request.getRequestDispatcher("/Diff.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
