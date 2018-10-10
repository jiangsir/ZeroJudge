package tw.zerojudge.Utils;

public class Daemon implements Runnable {

	public void run() {
		while (true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void interrupt() {
		Thread.currentThread().interrupt();
	}
}
