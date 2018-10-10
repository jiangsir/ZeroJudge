package tw.zerojudge.Model;

import java.sql.Timestamp;
import java.util.Date;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.TaskService;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Task;

public class ContestStarter implements Runnable {

	private long delay = 0;
	private long threadstarttime = 0;
	private String threadkey = ""; 
	private Contest contest;
	private Date firststart;


	public ContestStarter(Contest contest, Date firststart, long delay) {
		this.contest = contest;
		this.firststart = firststart;
		this.delay = delay;
	}

	public long getLivetime() {
		if (this.threadstarttime == 0) {
			return 0;
		}
		return System.currentTimeMillis() - this.threadstarttime;
	}

	public String getThreadKey() {
		return this.threadkey;
	}

	public void run() {
		this.threadstarttime = System.currentTimeMillis();
		long stoptime = this.threadstarttime + this.delay;

		Task newtask = new Task();
		newtask.setName(Task.NAME.ContestStarter);
		newtask.setPeriod(this.delay);
		newtask.setParameter(String.valueOf(contest.getId()));

		newtask.setFirststart(new Timestamp(this.firststart.getTime()));
		newtask.setStarttime(new Timestamp(this.threadstarttime));
		newtask.setStoptime(new Timestamp(stoptime));

		newtask = new TaskService(newtask).doStart(Thread.currentThread());

		contest.setTaskid(newtask.getId());
		new ContestService().update(contest);

		try {
			Thread.sleep(this.delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			new TaskService(newtask).doForcedStop();
			return;
		} finally {
			new TaskService(newtask).doStop();
		}


		contest.doRunning();
	}

	public String getThreadName() {
		return Thread.currentThread().getName();
	}

	public boolean isThreadActive() {
		return Thread.currentThread().isAlive();
	}

	public void interrupt() {
		Thread.currentThread().interrupt();
	}
}
