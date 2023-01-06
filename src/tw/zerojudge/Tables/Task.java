/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.sql.Timestamp;
import java.util.Date;

import tw.jiangsir.Utils.Annotations.Persistent;

/**
 * @author jiangsir
 * 
 */
public class Task {
	@Persistent(name = "id")
	private Integer id = 0;

	public static enum NAME {
		CodeLocker, 
		ContestStarter, 
		ContestCloser, 
		Unknown 
	}

	@Persistent(name = "name")
	private NAME name = NAME.Unknown;
	@Persistent(name = "useraccount")
	private String useraccount = "";
	@Persistent(name = "firststart")
	private Timestamp firststart = new Timestamp(new Date().getTime());
	@Persistent(name = "period")
	private Long period = 0L;

	@Persistent(name = "threadid")
	private Long threadid = 0L;

	@Persistent(name = "parameter")
	private String parameter = "";
	@Persistent(name = "starttime")
	private Timestamp starttime = new Timestamp(new Date().getTime());
	@Persistent(name = "stoptime")
	private Timestamp stoptime = new Timestamp(new Date().getTime());

	public static enum STATUS {
		Running, 
		Stop, 
		ForcedStop, 
		ContextRestart; 
	};

	@Persistent(name = "status")
	private STATUS status = STATUS.Running;

	private long left = 0; 


	public Task() {
	}

	public Integer getId() {
		return id;
	}

	public String getParameter() {
		return parameter;
	}

	public Long getPeriod() {
		return period;
	}

	public Date getStarttime() {
		return starttime;
	}

	public Date getStoptime() {
		return stoptime;
	}

	public String getUseraccount() {
		return useraccount;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NAME getName() {
		return name;
	}

	public void setName(NAME name) {
		this.name = name;
	}

	public void setName(String name) {
		this.setName(NAME.valueOf(name));
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public long getLeft() {
		return left;
	}

	public void setLeft(long left) {
		this.left = left;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public Timestamp getFirststart() {
		return firststart;
	}

	public void setFirststart(Timestamp firststart) {
		this.firststart = firststart;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public void setStoptime(Timestamp stoptime) {
		this.stoptime = stoptime;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public void setStatus(String status) {
		this.setStatus(STATUS.valueOf(status));
	}

	public Long getThreadid() {
		return threadid;
	}

	public void setThreadid(Long threadid) {
		this.threadid = threadid;
	}

	@Override
	public String toString() {
		return this.getStatus() + " [taskid=" + this.getId() + "("
				+ this.getThreadid() + "), name=" + this.getName() + "("
				+ this.getParameter() + "), 預計運行時間=" + this.getPeriod_H() + "]";
	}

	private String getPeriod_H() {
		int time = (int) (this.getPeriod() / 1000);
		String H = time % 60 + "秒";
		if (time / 60 > 0) {
			time = time / 60;
			H = time % 60 + "分" + H;
		}
		if (time / 60 > 0) {
			time = time / 60;
			H = time % 60 + "時" + H;
		}
		return H;
	}

	//

}
