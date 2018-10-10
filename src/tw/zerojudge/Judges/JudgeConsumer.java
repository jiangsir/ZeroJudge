package tw.zerojudge.Judges;

import tw.zerojudge.Factories.JudgeFactory;

public class JudgeConsumer implements Runnable {

	public boolean isInterrupted = false;

	private JudgeServer judgeServer;

	public JudgeConsumer(JudgeServer judgeServer) {
		this.judgeServer = judgeServer;
	}

	public void run() {
		Thread.currentThread().setName(this.getClass().getName());

		while (true) {
//			
			try {
				judgeServer.doJudge();
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				return;
			} catch (Exception e) {
				continue;
			}
		}

	}

	public boolean isThreadActive() {
		return Thread.currentThread().isAlive();
	}

	public void interrupt() {
		this.isInterrupted = true;
	}
}
