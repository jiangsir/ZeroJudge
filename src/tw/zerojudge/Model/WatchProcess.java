package tw.zerojudge.Model;

public class WatchProcess implements Runnable {
	Process process;
	double max_timelimit = 40; 
	double timelimit = 0; 
	boolean over = false;

	public WatchProcess(Process process) {
		this.process = process;
	}

	public void setTimelimit(double timelimit) {
		this.timelimit = timelimit;
	}

	public void run() {
		try {
			if (this.timelimit != 0) {
				this.max_timelimit = this.timelimit + 1;
			}
			Thread.sleep((long) (this.max_timelimit * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		if (!this.over) {

			this.process.destroy();
		}
	}

	public void setOver(boolean over) {
		this.over = over;
		Thread.interrupted();
	}

}
