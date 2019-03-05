package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.zerojudge.Beans.AppAttributes;
import tw.zerojudge.Configs.ConfigDAO;
import tw.zerojudge.Configs.ConfigFactory;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.AttributeFactory;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.JsonObjects.Schema;
import tw.zerojudge.Utils.*;

@WebServlet(urlPatterns = {"/Debug"})
@RoleSetting
public class DebugServlet extends HttpServlet implements IAccessFilter {
	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Runtime runtime = Runtime.getRuntime();
		AppAttributes appAttributes = AttributeFactory.readAppAttributes();
		request.setAttribute("freeMemory", runtime.freeMemory());
		request.setAttribute("maxMemory", runtime.maxMemory());
		request.setAttribute("totalMemory", runtime.totalMemory());
		request.setAttribute("availableProcessors", runtime.availableProcessors());
		request.setAttribute("uptime", new RunCommand().execUptime());
		request.setAttribute("loadAverage", new RunCommand().getLoadAverage());
		request.setAttribute("whoami", new RunCommand().execWhoami());
		request.setAttribute("javaVersion", new RunCommand().getJavaVersion());
		request.setAttribute("tomcatVersion", new RunCommand().getTomcatVersion());
		request.setAttribute("mysqlVersion", new RunCommand().getMysqlVersion());
		request.setAttribute("JAVA_HOME", new RunCommand().getJAVA_HOME());

		long tomcatlivetime = System.currentTimeMillis() - appAttributes.getLastContextRestart().getTime();
		long min = (tomcatlivetime - (tomcatlivetime % 60000)) / 60000;
		long hour = (min - (min % 60)) / 60;
		long day = (hour - (hour % 24)) / 24;
		request.setAttribute("tomcatliveTime", day + " 天 " + (hour % 24) + " 小時 " + (min % 60) + " 分");
		request.setAttribute("ENV_IP_CONNECTION", ENV.IP_CONNECTION);
		request.setAttribute("problem_managers", new UserService().getProblemManagers());
		request.setAttribute("vclass_managers", new UserService().getVClassManagers());
		request.setAttribute("hashSessions", SessionFactory.getHashSessions());

		request.setAttribute("countByAllProblems", new ProblemService().getCountByAllProblems());
		request.setAttribute("hashProblemsSize", new ProblemService().getHashProblems().size());

		request.setAttribute("countByAllSolutions", new SolutionService().getCountByAllSolution());
		request.setAttribute("hashSolutionsSize", new SolutionService().gethashSolutions().size());

		request.setAttribute("countByAllUsers", new UserService().getCountByAllUsers());
		request.setAttribute("hashUsersSize", new UserService().getHashUsers().size());

		request.setAttribute("countByAllContests", new ContestService().getCountByAllContests());
		request.setAttribute("hashContestsSize", new ContestService().getHashContests().size());
		request.setAttribute("system_properties", ConfigFactory.getSystemProperties());
		request.setAttribute("system_env", ConfigFactory.getSystemEnv());

		int i = 0;
		for (String key : ApplicationScope.getOnlineUsers().keySet()) {
		}


		Schema[] schemas = ApplicationScope.getAppConfig().getSchemas();
		request.setAttribute("schemas", schemas);
		request.setAttribute("schema", new ConfigDAO().getCurrentDBSchema());
		request.getRequestDispatcher("Debug.jsp").forward(request, response);
	}

	public enum POSTACTION {
		restartMysql, //
		restartTomcat, //
		reboot, //
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		switch (POSTACTION.valueOf(action)) {
			case restartMysql :
				new RunCommand().execRestartMysql();
				break;
			case restartTomcat :
				new RunCommand().execRestartTomcat();
				break;
			case reboot :
				new RunCommand().execReboot();
				break;
			default :
				break;

		}
	}

}
