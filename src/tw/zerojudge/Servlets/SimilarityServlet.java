package tw.zerojudge.Servlets;

import java.io.IOException;
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
import tw.zerojudge.Tables.Message;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = { "/Similarity" })
@RoleSetting
public class SimilarityServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String querystring = request.getQueryString();
		if (querystring == null || "".equals(querystring)) {
			return;
		}
		Integer basecodeid = Solution.parseSolutionid(request.getParameter("basecodeid"));
		Integer comparecodeid = Solution.parseSolutionid(request.getParameter("comparecodeid"));
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Solution basecode = new SolutionService().getSolutionById(basecodeid);
		Solution comparecode = new SolutionService().getSolutionById(comparecodeid);
		if (!basecode.getContestid().equals(comparecode.getContestid())) {
			Message message = new Message();
			message.setResourceMessage(Message.Resource_MustInTheSameContest);
			new MessageDAO().dispatcher(request, response, message);
			return;
		}

		if (!basecode.getContest().getIsOwner(onlineUser) && !onlineUser.getIsDEBUGGER()) {
			Message message = new Message();
			message.setResourceMessage(Message.Resource_YouArenotTheContestCreater);
			new MessageDAO().dispatcher(request, response, message);
			return;
		}
		SolutionDAO solutionDAO = new SolutionDAO();

		request.setAttribute("diff", solutionDAO.getDifflog(basecodeid, comparecodeid));
		request.setAttribute("basecode", basecode);
		request.setAttribute("comparecode", comparecode);
		request.getRequestDispatcher("/Similarity.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
