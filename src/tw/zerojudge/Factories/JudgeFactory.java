package tw.zerojudge.Factories;

import java.util.Comparator;
import java.util.PriorityQueue;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.JudgeServer;
import tw.zerojudge.Objects.Problemid;

public class JudgeFactory extends SuperFactory<Object> {
	private static PriorityQueue<JudgeObject> JudgeQueue = new PriorityQueue<JudgeObject>(1000,
			new Comparator<JudgeObject>() {
				public int compare(JudgeObject x, JudgeObject y) {
					if (x.getPriority().ordinal() > y.getPriority().ordinal()) {
						return 1;
					} else if (x.getPriority().ordinal() < y.getPriority().ordinal()) {
						return -1;
					} else {
						if (x.getSolution().getId().intValue() > y.getSolution().getId().intValue()) {
							return 1;
						} else {
							return -1;
						}
					}
				}
			});

	private static JudgeServer judgeServer = new JudgeServer();


	public static PriorityQueue<JudgeObject> getJudgeQueue() {
		return JudgeQueue;
	}

	public static String getJudgeQueueList() {
		StringBuffer sb = new StringBuffer(5000);
		synchronized (JudgeQueue) {
			for (JudgeObject judgeObject : JudgeQueue) {
				if (sb.length() != 0) {
					sb.append(", ");
				}
				try {
					sb.append(judgeObject.getProblem().getProblemid() + ":" + judgeObject.getSolution().getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static void addToQueue(JudgeObject judgeObject) {
		JudgeQueue.add(judgeObject);
	}

	public static JudgeObject createJudgeObject() {
		return new JudgeObject(new Problemid(""));
	}

	public static JudgeObject removeFromQueue() {
		return JudgeQueue.remove();
	}

	public static JudgeServer getJudgeServer() {
		return judgeServer;
	}

	public static JudgeServer newJudgeServer() {
		return new JudgeServer();
	}

	//
}
