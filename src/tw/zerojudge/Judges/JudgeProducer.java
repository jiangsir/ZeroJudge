package tw.zerojudge.Judges;

public class JudgeProducer implements Runnable {

	public boolean isInterrupted = false;
	private JudgeServer judgeServer;
	private JudgeObject judgeObject;

	public JudgeProducer(JudgeServer judgeServer, JudgeObject judgeObject) {
		this.judgeServer = judgeServer;
		this.judgeObject = judgeObject;
	}

	public void run() {
		judgeServer.addJudgeObject(judgeObject);
	}

	public void interrupt() {
		this.isInterrupted = true;
	}
}
