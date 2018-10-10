package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/VClassContest" })
public class VClassContestServlet extends HttpServlet implements IAccessFilter {
	public enum ACTION {
		forcedstart, //
		forcedstop, //
		pause, //
		resume, //
		modify;//
	}

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
	 * 
	 * @param onlineUser
	 * @param contest
	 * @throws AccessException
	 */
	public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (!contest.getIsOwner(onlineUser) && !onlineUser.getIsDEBUGGER()) {
			throw new AccessException("您沒有權限進行這個動作！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		String action = request.getParameter("action");
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
		}

		if (request.getQueryString() == null) {
			if (onlineUser.getIsDEBUGGER()) {
				request.setAttribute("contests", new ContestService().getContestsByVclassid(0, page));
			} else {
				request.setAttribute("contests",
						new ContestService().getContestsByOwnerid(onlineUser.getId(), page));
			}
			request.getRequestDispatcher("/EditContests.jsp").forward(request, response);
			return;
		}
		if (action == null || contest.isNullContest()) {
			throw new RoleException("參數有誤!");
		}
		switch (ACTION.valueOf(action)) {
		case forcedstart:

			contest.doRunning();
			response.sendRedirect(request.getContextPath()
					+ EditContestsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		case forcedstop:
			contest.doStop();
			response.sendRedirect(request.getContextPath()
					+ EditContestsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		case modify:
			request.setAttribute("contest", contest);
			request.setAttribute("nextaction",
					UpdateContestServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0].substring(1));
			request.getRequestDispatcher("/UpdateVContest.jsp").forward(request, response);
			return;
		case pause:
			throw new DataException("不明動作！");
		case resume:
			throw new DataException("不明動作！");
		default:
			throw new DataException("不明動作！");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
