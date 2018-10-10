package tw.jiangsir.Utils.Scopes;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.AppConfigService;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Task;
import tw.zerojudge.Tables.User;

public class ApplicationScope {
	public static ServletContext servletContext = null;
	private static String built = null;
	private static File appRoot = null;
	private static String version = null;
	private static boolean isReleased = false;
	private static ConcurrentHashMap<String, HttpSession> onlineSessions = new ConcurrentHashMap<String, HttpSession>();
	private static TreeMap<String, OnlineUser> onlineUsers = new TreeMap<String, OnlineUser>();
	private static HashMap<String, HttpServlet> urlpatterns = new HashMap<String, HttpServlet>();
	private static HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>> roleMap = new HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>>();
	private static TreeMap<Long, Thread> threadPool = new TreeMap<Long, Thread>();
	private static Hashtable<Integer, Task> runningTasks = new Hashtable<Integer, Task>();
	private static AppConfig appConfig = null;
	private static TreeSet<Problemid> OpenedProblemidSet = new TreeSet<Problemid>();
	private static String initException = "";
	private static String memoryInfo = "";

	public static void setAllAttributes(ServletContext servletContext) {
		ApplicationScope.servletContext = servletContext;

		ApplicationScope.setBuilt();
		ApplicationScope.setMemoryInfo();
		ApplicationScope.setAppRoot();
		ApplicationScope.setVersion();
		ApplicationScope.setIsReleased();
		ApplicationScope.setOnlineSessionScopes(onlineSessions);
		ApplicationScope.setOnlineUsers(onlineUsers);
		ApplicationScope.setUrlpatterns(urlpatterns);
		ApplicationScope.setRoleMap(roleMap);
		ApplicationScope.setThreadPool(threadPool);
		ApplicationScope.setRunningTasks(runningTasks);
		ApplicationScope.setAppConfig(new AppConfigService().getAppConfig());
		ApplicationScope.setOpenedProblemidSet(new ProblemService().getOpenedProblemidSet());
		ApplicationScope.setHashUsers(new UserService().getHashUsers());
		ApplicationScope.setHashProblems(new ProblemService().getHashProblems());
	}

	//

	public static ConcurrentHashMap<String, HttpSession> getOnlineSessions() {
		return onlineSessions;
	}

	public static void setOnlineSessionScopes(ConcurrentHashMap<String, HttpSession> onlineSessions) {
		ApplicationScope.onlineSessions = onlineSessions;
		ApplicationScope.servletContext.setAttribute("onlineSessions", onlineSessions);
	}


	public static TreeMap<String, OnlineUser> getOnlineUsers() {
		return onlineUsers;
	}

	public static void setOnlineUsers(TreeMap<String, OnlineUser> onlineUsers) {
		ApplicationScope.onlineUsers = onlineUsers;
		ApplicationScope.servletContext.setAttribute("onlineUsers", onlineUsers);
	}

	public static HashMap<String, HttpServlet> getUrlpatterns() {
		return urlpatterns;
	}

	public static void setUrlpatterns(HashMap<String, HttpServlet> urlpatterns) {
		ApplicationScope.urlpatterns = urlpatterns;
		ApplicationScope.servletContext.setAttribute("urlpatterns", urlpatterns);
	}

	public static HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>> getRoleMap() {
		return roleMap;
	}

	public static void setRoleMap(HashMap<User.ROLE, HashSet<Class<? extends HttpServlet>>> roleMap) {
		ApplicationScope.roleMap = roleMap;
		ApplicationScope.servletContext.setAttribute("roleMap", roleMap);
	}

	public static TreeMap<Long, Thread> getThreadPool() {
		return threadPool;
	}

	public static void setThreadPool(TreeMap<Long, Thread> threadPool) {
		ApplicationScope.threadPool = threadPool;
		ApplicationScope.servletContext.setAttribute("threadPool", threadPool);
	}

	public static Hashtable<Integer, Task> getRunningTasks() {
		return runningTasks;
	}

	public static void setRunningTasks(Hashtable<Integer, Task> runningTasks) {
		ApplicationScope.runningTasks = runningTasks;
		ApplicationScope.servletContext.setAttribute("runningTasks", runningTasks);
	}

	public static String getBuilt() {
		if (ApplicationScope.built == null) {
			setBuilt();
		}
		return ApplicationScope.built;
	}

	public static void setBuilt() {
		ApplicationScope.built = new SimpleDateFormat("yyMMdd")
				.format(new Date(ApplicationScope.getAppRoot().lastModified()));
		servletContext.setAttribute("built", ApplicationScope.built);
	}

	/**
	 * 取得 AppRoot 檔案位置。
	 * 
	 * @return
	 */
	public static File getAppRoot() {
		if (ApplicationScope.appRoot == null) {
			setAppRoot();
		}
		return ApplicationScope.appRoot;
	}

	public static void setAppRoot() {
		ApplicationScope.appRoot = new File(servletContext.getRealPath("/"));
		ApplicationScope.servletContext.setAttribute("appRoot", appRoot);
	}

	/**
	 * 直接指定 AppRoot，在單機直接執行的時候使用。因此不具備 serveltContext
	 * 
	 * @param appRoot
	 */
	public static void setAppRoot(File appRoot) {
		ApplicationScope.appRoot = appRoot;
	}

	/**
	 * 取得目前系統的版本。
	 */
	public static String getVersion() {
		if (ApplicationScope.version == null) {
			setVersion();
		}
		return ApplicationScope.version;
	}

	/**
	 * 取得目前系統的版本。
	 */
	public static void setVersion() {
		try {
			ApplicationScope.version = FileUtils
					.readFileToString(new File(ApplicationScope.appRoot + File.separator + "META-INF", "Version.txt"))
					.trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		servletContext.setAttribute("version", ApplicationScope.version);
	}

	public static boolean getIsReleased() {
		return isReleased;
	}

	public static void setIsReleased() {
		ApplicationScope.isReleased = getVersion().matches("V[0-9]+\\.[0-9]+\\.[0-9]+");
		ApplicationScope.servletContext.setAttribute("isReleased", isReleased);
	}

	//

	public static AppConfig getAppConfig() {
		if (appConfig == null) {
			ApplicationScope.appConfig = new AppConfigService().getAppConfig();
		}
		return appConfig;
	}

	public static void setAppConfig(AppConfig appConfig) {
		ApplicationScope.appConfig = appConfig;
		ApplicationScope.servletContext.setAttribute("appConfig", appConfig);
	}

	//

	public static TreeSet<Problemid> getOpenedProblemidSet() {
		return OpenedProblemidSet;
	}

	public static void setOpenedProblemidSet(TreeSet<Problemid> openedProblemidSet) {
		OpenedProblemidSet = openedProblemidSet;
		ApplicationScope.servletContext.setAttribute("openedProblemidSet", openedProblemidSet);
	}

	public static Hashtable<Integer, Problem> getHashProblems() {
		return new ProblemService().getHashProblems();
	}

	public static void setHashProblems(Hashtable<Integer, Problem> hashProblems) {
		ApplicationScope.servletContext.setAttribute("CacheProblems", hashProblems);
	}

	public static Hashtable<Integer, User> getHashUsers() {
		return new UserService().getHashUsers();
	}

	public static void setHashUsers(Hashtable<Integer, User> hashUsers) {
		ApplicationScope.servletContext.setAttribute("CacheUsers", hashUsers);
	}

	public static String getInitException() {
		return initException;
	}

	public static void setInitException(String initException) {
		ApplicationScope.initException = initException;
		ApplicationScope.servletContext.setAttribute("initException", initException);
	}

//	public static ArrayList<User> getGeneralManagers() {
//		return generalManagers;
//	}
//
//	public static void setGeneralManagers(ArrayList<User> generalManagers) {
//		ApplicationScope.generalManagers = generalManagers;
//		ApplicationScope.servletContext.setAttribute("generalManagers", generalManagers);
//	}

	public static String getMemoryInfo() {
		ApplicationScope.setMemoryInfo();
		return memoryInfo;
	}

	public static void setMemoryInfo() {
		int freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);
		int totalMemory = (int) (Runtime.getRuntime().totalMemory() / 1024 / 1024);
		double percent = ((totalMemory - freeMemory) / (double) totalMemory) * 100;
		ApplicationScope.memoryInfo = (totalMemory - freeMemory) + "/" + totalMemory + " MB ("
				+ String.format("%.1f", percent) + "%)";
		servletContext.setAttribute("memoryInfo", ApplicationScope.memoryInfo);
	}

	public static File getSystemTmp() {
		String osname = System.getProperty("os.name").toLowerCase();
		if (osname.startsWith("windows")) {
			return new File(System.getenv("TMP"));
		} else if (osname.startsWith("linux")) {
			return new File(System.getProperty("java.io.tmpdir"));
		} else if (osname.startsWith("mac")) {
			return new File(System.getProperty("java.io.tmpdir"));
		} else {
			return new File("/tmp");
		}
	}
}
