package tw.zerojudge.Model;

import java.sql.Timestamp;
import java.util.Date;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.TaskDAO;
import tw.zerojudge.DAOs.TaskService;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.Task;

public class CodeLocker implements Runnable {

	private long delay = 0;
	private Integer solutionid = 0;
	private String useraccount = "";
	private Date firststarttime;
	private boolean isInterrupted = false;

	public CodeLocker(Integer solutionid, String useraccount,
			Date firststarttime, long delay) {
		this.solutionid = solutionid;
		this.useraccount = useraccount;
		this.firststarttime = firststarttime;
		this.delay = delay;
	}

	private void closeCode(Integer solutionid) {
		Solution solution = new SolutionDAO().getSolutionById(solutionid);
		solution.setCodelocker(Solution.CODELOCKER_CLOSE);
		new SolutionService().update(solution);
	}

	public void run() {
		long threadstarttime = new Date().getTime();
		Thread currentThread = Thread.currentThread();
		ApplicationScope.getThreadPool().put(currentThread.getId(),
				currentThread);

		TaskDAO taskDAO = new TaskDAO();
		long stoptime = threadstarttime + this.delay;

		Task newtask = new Task();
		newtask.setName(Task.NAME.CodeLocker);
		newtask.setUseraccount(this.useraccount);
		newtask.setFirststart(new Timestamp(this.firststarttime.getTime()));
		newtask.setPeriod(this.delay);
		newtask.setThreadid(currentThread.getId());
		newtask.setParameter(String.valueOf(solutionid));
		newtask.setStarttime(new Timestamp(threadstarttime));
		newtask.setStoptime(new Timestamp(stoptime));
		int taskid = taskDAO.insert(newtask);
		newtask.setId(taskid);
		try {
			Thread.sleep(this.delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		if (this.isInterrupted) {
			return;
		}
		this.closeCode(solutionid);
		new TaskService(newtask).doStop();
	}

	public String getThreadName() {
		return Thread.currentThread().getName();
	}

	public boolean isThreadActive() {
		return Thread.currentThread().isAlive();
	}

	public void interrupt() {
		this.closeCode(solutionid);
		this.isInterrupted = true;
		Thread.currentThread().interrupt();
	}
}
