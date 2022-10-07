package tw.zerojudge.DAOs;

import java.sql.Timestamp;
import java.util.Date;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Tables.Task;
import tw.zerojudge.Utils.ENV;

public class TaskService {
	private Task task;

	public TaskService(Task task) {
		this.task = task;
	}

	/**
	 * 啓動一個新的 task
	 */
	public Task doStart(Thread currentThread) {
		task.setThreadid(currentThread.getId());
		int taskid = new TaskDAO().insert(task);
		task.setId(taskid);

		ApplicationScope.getThreadPool().put(currentThread.getId(),
				currentThread);
		ApplicationScope.getRunningTasks().put(task.getId(), task);

		return task;
	}

	/**
	 * 重新啓動一個 task, 用在當系統重啓之後
	 * 
	 * @return
	 */
	public void doRestart() {

	}

	public void doStop() {
		this.task.setStatus(Task.STATUS.Stop);
		new TaskDAO().update(this.task);
		ApplicationScope.getRunningTasks().remove(task);

		Thread thread = null;
		if (ApplicationScope.getThreadPool().containsKey(
				this.task.getThreadid())) {
			thread = ApplicationScope.getThreadPool().remove(
					this.task.getThreadid());

		}
		if (thread != null) {
			thread.interrupt();
		}

	}

	public void doForcedStop() {


		if (ApplicationScope.getThreadPool().containsKey(
				this.task.getThreadid())) {
			Thread thread = ApplicationScope.getThreadPool().remove(
					this.task.getThreadid());
			thread.interrupt();

		}
		this.task.setStoptime(new Timestamp(new Date().getTime()));

		this.task.setStatus(Task.STATUS.ForcedStop);

		new TaskDAO().update(this.task);
		ApplicationScope.getRunningTasks().remove(task);

	}

	public void doForcedStopCodeLocker() {
		if (ApplicationScope.getThreadPool().containsKey(task.getThreadid())) {
			Thread thread = ApplicationScope.getThreadPool().remove(
					task.getThreadid());
			thread.interrupt();
		}
		this.task.setStoptime(new Timestamp(new Date().getTime()));

		task.setStatus(Task.STATUS.ForcedStop);
		ApplicationScope.getRunningTasks().remove(task);

		new TaskDAO().update(this.task);
	}

}
