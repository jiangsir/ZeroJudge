package tw.zerojudge.Servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.AppConfigService;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.TaskDAO;
import tw.zerojudge.DAOs.TaskService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Model.*;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/UpdateContest" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class UpdateContestServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);

		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		this.AccessFilter(onlineUser, contest);
	}

	public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (onlineUser.getIsDEBUGGER()) {
			return;
		}
		if (onlineUser.isContestManager() && contest.getIsOwner(onlineUser)) {
			return;
		}
		throw new AccessException("您沒有權限管理這個競賽！");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		request.setAttribute("contest", contest);
		request.getRequestDispatcher("/EditContest.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		try {
			String title = request.getParameter("title");
			String subtitle = request.getParameter("subtitle");
			String problemids = request.getParameter("problemids");
			String scores = request.getParameter("scores");
			String startradio = request.getParameter("startradio");
			String visible = request.getParameter("contestvisible");
			String conteststatus = request.getParameter("conteststatus");
			String addonprivilege = request.getParameter("addonprivilege");
			String rankingmode = request.getParameter("rankingmode");

			Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
			HashMap<?, ?> map = (HashMap<?, ?>) request.getParameterMap();
			Iterator<?> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key.startsWith("config_")) {
					String index = key.split("_")[1];
					contest.doAssignConfig(Contest.CONFIG.values()[Integer.valueOf(index)],
							Integer.valueOf(((String[]) map.get(key))[0]) > 0);
				}
			}

			contest.setSubtitle(subtitle);

			if (onlineUser.getIsDEBUGGER()) {
				AppConfig appConfig = ApplicationScope.getAppConfig();
				appConfig.setRankingmode_HSIC(request.getParameter(AppConfig.FIELD.rankingmode_HSIC.name()));
				appConfig.setRankingmode_NPSC(request.getParameter(AppConfig.FIELD.rankingmode_NPSC.name()));
				appConfig.setRankingmode_CSAPC(request.getParameter(AppConfig.FIELD.rankingmode_CSAPC.name()));
				new AppConfigService().update(appConfig);

			}
			contest.setTitle(title);
			contest.setUserrules(request.getParameter("userrules"));
			contest.setProblemids(problemids);
			contest.setScores(scores);
			contest.setVisible(visible);
			contest.setConteststatus(conteststatus);
			contest.setAddonprivilege(addonprivilege);
			contest.setRankingmode(rankingmode);
			contest.setFreezelimit(request.getParameter("freezelimit"));
			if (startradio != null) { 
				switch (Contest.STATUS.parse(startradio)) {
				case RUNNING:
					contest.setStarttime(new Timestamp(new Date().getTime()));
					contest.setConteststatus(Contest.STATUS.RUNNING);
					contest.setTimelimit(request.getParameter("hours"), request.getParameter("mins"));

					new ContestService().update(contest);
					Task task = new TaskDAO().getTaskById(contest.getTaskid());
					new TaskService(task).doStop();

					ContestStarter contestStarter = new ContestStarter(contest, contest.getStarttime(), 0);
					Thread thread = new Thread(contestStarter);
					thread.start();
					break;
				case STARTING:
					contest.setConteststatus(Contest.STATUS.STARTING);
					contest.setStarttime(request.getParameter("starttime"));
					contest.setTimelimit(request.getParameter("hours"), request.getParameter("mins"));

					long delay = contest.getStarttime().getTime() - new Date().getTime();
					if (delay < 0) {
						throw new DataException("因為您指定了過去的時間，考試將不會啟動!!\n" + "若您是想馬上開始，請選擇\"馬上開始\"");
					}
					new ContestService().update(contest);
					task = new TaskDAO().getTaskById(contest.getTaskid());
					if (task.getId().intValue() != 0) {
						new TaskService(task).doStop();
					}
					contestStarter = new ContestStarter(contest, new Date(), delay);
					thread = new Thread(contestStarter);
					thread.start();
					break;
				case PAUSING:
					break;
				case STOPPED:
					break;
				case SUSPENDING:
					contest.setConteststatus(Contest.STATUS.SUSPENDING);
					contest.setStarttime(request.getParameter("starttime"));
					new ContestService().update(contest);
					break;
				default:
					new ContestService().update(contest);
					break;
				}
			} else {
				new ContestService().update(contest);
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
