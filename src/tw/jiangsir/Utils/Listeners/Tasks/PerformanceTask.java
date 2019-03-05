package tw.jiangsir.Utils.Listeners.Tasks;

import java.sql.Timestamp;
import java.util.TimerTask;
import java.util.logging.Logger;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.Log;

public class PerformanceTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run() {
		logger.info(this.getClass().getSimpleName() + " Running...." + new Timestamp(System.currentTimeMillis()));

		int freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);
		int totalMemory = (int) (Runtime.getRuntime().totalMemory() / 1024 / 1024);
		double percent = ((totalMemory - freeMemory) / (double) totalMemory);

		StringBuffer memory = new StringBuffer(5000);

		memory.append("記憶體用量：" + ApplicationScope.getMemoryInfo() + ", Solution Cache size="
				+ SolutionService.HashSolutions.size() + ", " + "User Cache size=" + UserService.HashUsers.size() + ", "
				+ "Problem Cache size=" + ProblemService.HashProblems.size() + ", " + "Contest Cache size="
				+ ContestService.HashContests.size() + "\n");

		logger.info(memory.toString());

		if (percent > 0.6 && percent < 0.8) {
			String content = "";

			String beforeInfo = ApplicationScope.getMemoryInfo();
			content = "啟動 gc 前 usage=" + beforeInfo + "\n";
			int beforeUsage = this.getMemoryUsage();
			System.gc();
			int afterUsage = this.getMemoryUsage();
			content += "完成 gc usage=" + ApplicationScope.getMemoryInfo();
			Log log = new Log();
			log.setTabid(Log.TABID.GC);
			log.setTitle("記憶體用量偏高(" + beforeInfo + ")，啟動 GC 減少 " + (beforeUsage - afterUsage) + "MB 記憶體用量");
			log.setMessage(content);
			new LogDAO().insert(log);
			return;
		}
		if (percent >= 0.8) {
			String beforeInfo = ApplicationScope.getMemoryInfo();
			String content = "啟動 gc 前 MemoryInfo=" + beforeInfo + "\n";
			int beforeUsage = this.getMemoryUsage();
			System.gc();
			int afterUsage = this.getMemoryUsage();
			content += "完成 gc 後 MemoryInfo=" + ApplicationScope.getMemoryInfo();
			content += "\n啟動 GC ,共減少 " + (afterUsage - beforeUsage) + "MB 記憶體";
			Log log = new Log();
			log.setTabid(Log.TABID.GC);
			log.setTitle("記憶體即將用罄(" + beforeInfo + ")，tomcat restart。");
			log.setMessage(content);
			new LogDAO().insert(log);


			new RunCommand().execRestartTomcat();
			return;
		}

		double loading = new RunCommand().getLoadAverage();
		if (loading >= 3 && loading < 5) {
			Log log = new Log();
			log.setTabid(Log.TABID.GC);
			log.setTitle("系統 Loading 過高！ loading=" + loading + ", tomcat 重啓！");
			log.setMessage("uptime=" + new RunCommand().execUptime() + "\n tomcat restart");
			new LogDAO().insert(log);
			new RunCommand().execRestartTomcat();
			return;
		}
		if (loading >= 5) {
			Log log = new Log();
			log.setTabid(Log.TABID.GC);
			log.setTitle("系統 Loading 過高！ loading=" + loading + ", 系統 reboot");
			log.setMessage("uptime=" + new RunCommand().execUptime() + "\n reboot");
			new LogDAO().insert(log);
			new RunCommand().execReboot();
			return;
		}

	}
	/**
	 * 單位 MB
	 * 
	 * @return
	 */
	private int getMemoryUsage() {
		int freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);
		int totalMemory = (int) (Runtime.getRuntime().totalMemory() / 1024 / 1024);
		return (totalMemory - freeMemory);
	}

}
