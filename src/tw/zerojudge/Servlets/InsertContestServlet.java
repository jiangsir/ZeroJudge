package tw.zerojudge.Servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.Contest.CONFIG;

@WebServlet(urlPatterns = { "/InsertContest" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class InsertContestServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.getIsHigherEqualThanMANAGER() && !onlineUser.isContestManager()) {
			throw new AccessException("您沒有權限存取這個功能！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		Contest newcontest = new Contest();
		newcontest.setOwnerid(onlineUser.getId());

		LinkedHashSet<Problemid> problemids = new LinkedHashSet<Problemid>();
		problemids.add(new ProblemService().getProblemByPid(1).getProblemid());
		newcontest.setProblemids(problemids);
		newcontest.setScores(new int[] { 100 });
		newcontest.setTimelimit(100 * 60 * 1000L);
		newcontest.setStarttime(new Timestamp(System.currentTimeMillis()));
		request.setAttribute("contest", newcontest);
		request.getRequestDispatcher("EditContest.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		try {
			OnlineUser onlineUser = UserFactory.getOnlineUser(session);

			String startradio = request.getParameter("startradio");
			Contest newcontest = new Contest();

			HashMap<?, ?> map = (HashMap<?, ?>) request.getParameterMap();
			Iterator<?> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key.startsWith("config_")) {
					String index = key.split("_")[1];
					newcontest.doAssignConfig(Contest.CONFIG.values()[Integer.parseInt(index)],
							Integer.valueOf(((String[]) map.get(key))[0]) > 0);
				}
			}
			newcontest.setTitle(request.getParameter("title"));
			newcontest.setSubtitle(request.getParameter("subtitle"));
			newcontest.setAddonprivilege(request.getParameter("addonprivilege"));
			newcontest.setRankingmode(Parameter.parseString(request.getParameter("rankingmode")));


			newcontest.setOwnerid(onlineUser.getId());

			newcontest.setProblemids(request.getParameter("problemids"));

			newcontest.setScores(request.getParameter("scores"));
			newcontest.setUserrules(request.getParameter("users"));
			newcontest.setTimelimit(request.getParameter("hours"), request.getParameter("mins"));

			newcontest.setVisible(request.getParameter("contestvisible"));
			newcontest.setFreezelimit(request.getParameter("freezelimit"));

			switch (Contest.STATUS.parse(startradio)) {
			case RUNNING:
				newcontest.setConteststatus(Contest.STATUS.RUNNING);
				newcontest.setStarttime(new Timestamp(new Date().getTime()));
				break;
			case STARTING:
				newcontest.setConteststatus(Contest.STATUS.STARTING);
				newcontest.setStarttime(request.getParameter("starttime"));
				break;
			case SUSPENDING:
				newcontest.setConteststatus(Contest.STATUS.SUSPENDING);
				newcontest.doDisableConfig(CONFIG.AutoRunning);
				newcontest.setStarttime(request.getParameter("starttime"));
				break;
			case STOPPED:
				newcontest.setConteststatus(Contest.STATUS.STOPPED);
				newcontest.setStarttime(request.getParameter("starttime"));
				break;
			default:
				break;
			}


			int contestid = new ContestService().insert(newcontest);
			Contest contest = new ContestService().getContestById(contestid);

			switch (contest.getConteststatus()) {
			case RESTARTING:
				break;
			case RUNNING:
				contest.doRunning();
				break;
			case STARTING:
				contest.doStarting();
				break;
			case PAUSING:
				break;
			case STOPPED:
				break;
			case SUSPENDING:
				break;
			default:
				break;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (DataException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

}
