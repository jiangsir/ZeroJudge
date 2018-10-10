package tw.jiangsir.Utils.Tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Model.WatchProcess;
import tw.zerojudge.Tables.Log;

public class RunCommand implements Runnable {
	private String[] command;
	private long delay = 0;
	public long executetime;
	private ArrayList<String> errStream = new ArrayList<String>();
	private ArrayList<String> outStream = new ArrayList<String>();
	public int exitCode = -1;
	private double timelimit = 40; 
	public boolean isInterrupted = false;
	public LineNumberReader in = null;

	public static enum Command {
		sudo, //
		echo, //
		java, //
		mysql, //
		rsync, //
		whoami, //
		ssh, //
		uptime, //
		touch, //
		find, //
		egrep, //
		awk, //
		ls, //
		dos2unix, //
		reboot,//
	}

	Logger logger = Logger.getLogger(this.getClass().getName());

	public enum CMD {
		rsync("C", "c"), //
		whoami("C", "c"), //
		tomcatrestart("C", "c");//

		private String param; //
		private String descript;//

		private CMD(String param, String descript) {
			this.param = param;
			this.descript = descript;
		}

		public String getParam() {
			return param;
		}

		public String getDescript() {
			return descript;
		}

	}

	public RunCommand() {
	}

	public RunCommand(String[] command, long delay) {
		this.command = command;
		this.delay = delay;
	}


	public LineNumberReader getIn() {
		return in;
	}

	public void setIn(LineNumberReader in) {
		this.in = in;
	}

	public void setTimelimit(double timelimit) {
		this.timelimit = timelimit;
	}

	private void exec(String[] cmd) {
		Thread.currentThread().setName(this.getClass().getName());
		logger.info("RunCommand.exec()=" + cmd[2]);
		long starttime = System.currentTimeMillis();
		try {
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec(cmd);
			try {
				WatchProcess watch = new WatchProcess(process);
				if (this.timelimit != 0) {
					watch.setTimelimit(this.timelimit);
				}
				new Thread(watch).start();
				this.exitCode = process.waitFor();
				watch.setOver(true);

				logger.info("執行系統程式。command=" + cmd[2]);

			} catch (InterruptedException e) {
				logger.info("執行系統程式。command=" + cmd[2] + "失敗！:" + e.getLocalizedMessage());
				Log log = new Log();
				log.setTabid(Log.TABID.WATCHING);
				log.setTitle("執行系統程式失敗！(" + cmd[2] + ")");
				log.setMessage("command=" + cmd[2] + "失敗！:" + e.getLocalizedMessage());
				new LogDAO().insert(log);
				e.printStackTrace();
				return;
			}

			this.executetime = (System.currentTimeMillis() - starttime);
			InputStream stdin = process.getInputStream(); 
			InputStream stderr = process.getErrorStream(); 

			String errString = null;
			LineNumberReader err = new LineNumberReader(new InputStreamReader(stderr));
			while ((errString = err.readLine()) != null && this.errStream.size() < 500) {
				this.errStream.add(errString);
				logger.info("errString=" + errString);
			}
			LineNumberReader in = new LineNumberReader(new InputStreamReader(stdin));
			String outString = null;
			while ((outString = in.readLine()) != null && this.outStream.size() < 500) {
				this.outStream.add(outString);
				logger.info("outString=" + outString);
			}
			this.in = in;

			Log log = new Log();
			log.setTabid(Log.TABID.RUNCOMMAND);
			log.setTitle("RunCommand=" + cmd[2]);
			log.setMessage("RunCommand=" + cmd[2] + "(" + this.executetime + "ms)" + "\noutString=" + outString
					+ "\nerrString=" + errString);
			new LogDAO().insert(log);

		} catch (IOException e) {
			logger.severe(
					"IOException: RunCommand=" + cmd[2] + ", time=" + (System.currentTimeMillis() - starttime) + " ms");
			e.printStackTrace();
			return;
		}
	}

	public void exec(String cmd) {
		String[] command = {"/bin/sh", "-c", cmd};
		this.exec(command);
	}

	public void run() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		if (this.isInterrupted) {
			logger.info("command 被中斷: " + command[2]);
			Log log = new Log();
			log.setTabid(Log.TABID.WATCHING);
			log.setMessage("command 被中斷: " + command[2]);
			new LogDAO().insert(log);
			return;
		}

		this.exec(command);
	}

	public ArrayList<String> getErrStream() {
		return errStream;
	}

	public ArrayList<String> getOutStream() {
		return outStream;
	}

	public String getThreadName() {
		return Thread.currentThread().getName();
	}

	public boolean isThreadActive() {
		return Thread.currentThread().isAlive();
	}

	public void interrupt() {
		this.isInterrupted = true;
		Thread.currentThread().interrupt();
	}

	public String getMysqlVersion() {
		this.exec(Command.sudo + " " + Command.mysql + " --version");

		ArrayList<String> out = this.getOutStream();
		if (out == null || out.size() == 0 || "".equals(out.get(0).toString().trim())) {
			return "mysql 有誤!!";
		} else {
			String s = (String) out.get(0);
			String ver = s.substring(s.indexOf("Distrib") + 7, s.indexOf(","));
			return ver;
		}
	}

	public String getJavaVersion() {
		this.exec(Command.java + " -version");

		ArrayList<String> out = this.getErrStream();
		if (out == null || out.size() == 0 || ((String) out.get(0)).contains("java: command not found")) {
			return "Java not installed!!";
		} else {
			return out.get(0);
		}
	}

	public String getJAVA_HOME() {
		this.exec(Command.echo + " $JAVA_HOME");
		ArrayList<String> out = this.getOutStream();
		if (out == null || out.size() == 0 || "".equals(out.get(0).toString().trim())) {
			return "$JAVA_HOME is empty!!";
		} else {
			return (String) out.get(0);
		}
	}

	/**
	 * 回覆 tomcat6 or tomcat7
	 * 
	 * @return
	 */
	public String getTomcatVersion() {
		this.exec(Command.sudo + " " + Command.find + " /etc/init.d/ -name \"tomcat*\"");
		ArrayList<String> out = this.getOutStream();
		if (out == null || out.size() == 0 || "".equals(out.get(0).toString().trim())) {
			return "";
		} else {
			return (String) out.get(0);
		}
	}

	public void execRestartWebapp() {

		this.exec(Command.touch + " " + ApplicationScope.getAppRoot() + "/WEB-INF/web.xml");
		this.exec(Command.touch + " " + ApplicationScope.getAppRoot() + "/META-INF/context.xml");

	}

	public String execWhoami() {
		this.exec(Command.whoami.toString());
		ArrayList<String> out = this.getOutStream();
		if (out == null || out.size() == 0 || "".equals(out.get(0).toString().trim())) {
			return "";
		} else {
			return (String) out.get(0);
		}
	}

	public String execUptime() {
		this.exec(Command.uptime.toString());
		ArrayList<String> out = this.getOutStream();

		if (out == null || out.size() == 0 || "".equals(out.get(0).toString().trim())) {
			return "";
		} else {
			return (String) out.get(0);
		}
	}

	public double getLoadAverage() {
		this.exec(Command.uptime + " | " + Command.egrep + " -o 'load average[s]*: [0-9,\\. ]+' | " + Command.awk
				+ " -F',' '{print $1$2$3}' | " + Command.awk + " -F' ' '{print $3,$4,$5}'");
		ArrayList<String> out = this.getOutStream();
		if (out == null || out.size() == 0 || "".equals(out.get(0).toString().trim())) {
			return 0;
		} else {
			try {
				String[] ds = out.get(0).trim().split(" "); 
				double min_1 = Double.parseDouble(ds[0]);
				double min_5 = Double.parseDouble(ds[1]);
				double min_15 = Double.parseDouble(ds[2]);
				return (min_1 < min_5) ? min_1 : min_5;
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}

	/**
	 * Servlet 直接呼叫重新啟動 mysql
	 */
	public void execRestartMysql() {
		this.exec(Command.sudo + " /etc/init.d/mysql restart");
	}

	/**
	 * 重新啟動 tomcat
	 */
	public void execRestartTomcat() {

		this.exec(
				Command.sudo + " " + Command.find + " /etc/init.d/ -name \"tomcat*\" -exec bash -c \"{} restart\" \\;");
	}
	/**
	 * 重新開機
	 */
	public void execReboot() {
		this.exec(Command.sudo + " " + Command.reboot);
	}
}
