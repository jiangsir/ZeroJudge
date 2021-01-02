package tw.zerojudge.Model;

import java.sql.Timestamp;
import java.util.Date;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.TaskService;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Task;

public class ContestCloser implements Runnable {

	private long delay = 0;
	private long threadstarttime = 0;
	private String threadkey = ""; 
	private String parameter = "";
	private Date firststart;
	private boolean isInterrupted = false;
	//private int taskid = 0;
	private Contest contest;

	public ContestCloser(Contest contest, Date firststart, long delay) {
		this.contest = contest;
		this.firststart = firststart;
		this.delay = delay;
	}

	/**
	 * @param context2
	 * @param id
	 * @param creater
	 * @param long1
	 */
	public ContestCloser(Contest contest, Date firststart, String timelimit) {
		this.contest = contest;
		this.firststart = firststart;
		this.delay = Long.valueOf(timelimit);
	}

	public long getLivetime() {
		if (this.threadstarttime == 0) {
			return 0;
		}
		return new Date().getTime() - this.threadstarttime;
	}

	public long getDelay() {
		return this.delay;
	}

	public String getThreadKey() {
		return this.threadkey;
	}

	public void run() {
		this.threadstarttime = new Date().getTime();


		long stoptime = this.threadstarttime + this.delay;
		this.parameter = String.valueOf(contest.getId());
		Task newtask = new Task();
		newtask.setName(Task.NAME.ContestCloser);
		//newtask.setUseraccount(this.taskcreater);
		newtask.setFirststart(new Timestamp(this.firststart.getTime()));
		newtask.setPeriod(this.delay);
		newtask.setThreadid(Thread.currentThread().getId());
		newtask.setParameter(this.parameter);
		newtask.setStarttime(new Timestamp(threadstarttime));
		newtask.setStoptime(new Timestamp(stoptime));

		newtask = new TaskService(newtask).doStart(Thread.currentThread());

		contest.setTaskid(newtask.getId());

		new ContestService().update(contest);


		try {
			Thread.sleep(this.delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		if (this.isInterrupted) {
			return;
		}
		try {
			new TaskService(newtask).doStop();
			contest.doStop();
		} catch (DataException e) {
			e.printStackTrace();
			new LogDAO().insert(new Log(e));
		}
	}

	public String getThreadName() {
		return Thread.currentThread().getName();
	}

	public boolean isThreadActive() {
		return Thread.currentThread().isAlive();
	}

	public void interrupt() throws AccessException, DataException {
		contest.doStop();
		Thread.currentThread().interrupt();
	}
}
