package tw.zerojudge.Objects;

import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Tables.Problem;

public class TestdataPair {
	public Testdatafile in;
	public Testdatafile out;
	private int score = 0;
	private double timelimit = 1.0;

	public TestdataPair(Testdatafile in, Testdatafile out) {
		this.in = in;
		this.out = out;
		Problem problem = new ProblemService().getProblemByProblemid(in.getProblemid());
		try {
			this.setScore(problem.getScores()[in.getIndex()]);
			this.setTimelimit(problem.getTimelimits()[in.getIndex()]);
		} catch (Exception e) {
		}

	}

	public Testdatafile getInfile() {
		return in;
	}
	public Testdatafile getOutfile() {
		return out;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public double getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(double timelimit) {
		this.timelimit = timelimit;
	}

}
