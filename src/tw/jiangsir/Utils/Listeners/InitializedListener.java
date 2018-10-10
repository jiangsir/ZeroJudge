package tw.jiangsir.Utils.Listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Filters.RoleFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Beans.AppAttributes;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.AttributeFactory;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Factories.SolutionFactory;
import tw.zerojudge.Judges.JudgeConsumer;
import tw.zerojudge.Model.*;
import tw.zerojudge.Servlets.Ajax.ReJudgeAjaxServlet;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.Task;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.*;

@WebListener
public class InitializedListener implements ServletContextListener {
	Logger logger = Logger.getLogger(this.getClass().getName());
	ObjectMapper mapper = new ObjectMapper(); 

	private void contestCloserInitialized(ServletContext context) {
		TaskDAO taskDao = new TaskDAO();
		for (Task task : taskDao.getTasksByContestCloser()) {
			String parameter = task.getParameter();
			Date stop = task.getStoptime();
			long delay = stop.getTime() - System.currentTimeMillis();
			if (delay < 0) {
				delay = 0;
			}

			task.setStatus(Task.STATUS.ContextRestart);
			taskDao.update(task);
			Contest contest = new ContestDAO().getContestById(Integer.valueOf(parameter));
			Thread contestCloser = new Thread(new ContestCloser(contest, task.getFirststart(), delay));
			contestCloser.start();
		}
	}

	private void contestStarterInitialized(ServletContext context) {
		TaskDAO taskDao = new TaskDAO();
		for (Task task : taskDao.getTasksByContestStarter()) {
			String parameter = task.getParameter();
			long delay = task.getStoptime().getTime() - System.currentTimeMillis();
			if (delay < 0) {
				delay = 0;
			}
			task.setStatus(Task.STATUS.ContextRestart);
			taskDao.update(task);
			Contest contest = new ContestDAO().getContestById(Integer.valueOf(parameter));
			Thread contestStarter = new Thread(new ContestStarter(contest, task.getFirststart(), delay));
			contestStarter.start();
		}
	}

	/**
	 *
	 */
	public void contextInitialized(ServletContextEvent event) {
		try {

			ServletContext servletContext = event.getServletContext();
			AttributeFactory.setServletContext(servletContext);
			ApplicationScope.setAllAttributes(servletContext);
			AppAttributes appAttributes = AttributeFactory.readAppAttributes();

			for (User.ROLE role : User.ROLE.values()) {
				ApplicationScope.getRoleMap().put(role, new HashSet<Class<? extends HttpServlet>>());
			}

			Map<String, ? extends ServletRegistration> registrations = servletContext.getServletRegistrations();
			for (String key : registrations.keySet()) {
				String servletClassName = registrations.get(key).getClassName();
				HttpServlet httpServlet;
				try {
					httpServlet = (HttpServlet) Class.forName(servletClassName).newInstance();
					if (httpServlet instanceof HttpServlet) {
						if (httpServlet.getClass() == ReJudgeAjaxServlet.class) {
						}
						for (User.ROLE role : new RoleFilter().getRoleSet(httpServlet)) {
							ApplicationScope.getRoleMap().get(role).add(httpServlet.getClass());
						}

						WebServlet webServlet = httpServlet.getClass().getAnnotation(WebServlet.class);
						if (webServlet != null) {
							for (String urlpattern : webServlet.urlPatterns()) {
								ApplicationScope.getUrlpatterns().put(urlpattern, httpServlet);
							}
						}
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			AppConfig appConfig = ApplicationScope.getAppConfig();
			if (appConfig.getSystemMode() == AppConfig.SYSTEM_MODE.CLOSE_MODE) {
				throw new InfoException(appConfig.getSystem_closed_message());
			}

			ENV.setSYSTEM_MONITOR_ACCOUNT(appConfig.getSystemMonitorAccount());
			ENV.setSYSTEM_MONITOR_IP(appConfig.getSystem_monitor_ip());
			String osname = System.getProperty("os.name").toLowerCase();
			if (osname.startsWith("windows")) {
				ENV.setCOMMAND(System.getenv("ComSpec"));
			} else if (osname.startsWith("linux") || osname.startsWith("freebsd") || osname.startsWith("mac")) {
			}

			this.contestStarterInitialized(servletContext);
			this.contestCloserInitialized(servletContext);
			JudgeConsumer judgeConsumer = new JudgeConsumer(JudgeFactory.getJudgeServer());

			Thread judgeConsumerThread = new Thread(judgeConsumer);
			judgeConsumerThread.start();

			appAttributes.setHashSessions(SessionFactory.getHashSessions());

			new AppConfigService().init();
			new UserService().insertInitUsers();
			new ProblemService().insertInitProblems();
			new ContestService().doInitialized();

			appAttributes.setLastSolutionid(SolutionFactory.getLastSolutionid());
			AttributeFactory.writeAppAttributes(appAttributes);

			logger.info(ApplicationScope.getAppRoot() + " 初始化完成！");
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationScope.setInitException(e.getLocalizedMessage());
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		for (Contest contest : new ContestService().getRunningContests()) {
			contest.setRestarting();
		}

		Connection conn = (Connection) context.getAttribute("conn");
		context.removeAttribute("conn");
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
			logger.info("資料庫連結關閉");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
