package tw.zerojudge.JsonObjects;

import org.codehaus.jackson.annotate.JsonIgnore;

import tw.zerojudge.Judges.ServerOutput;

/**
 * @author jiangsir
 * 
 */
public class Summary {
	private ServerOutput.JUDGEMENT judgement;
	private String summary;
	private int score;
	private boolean accessible;

	public ServerOutput.JUDGEMENT getJudgement() {
		return judgement;
	}

	@JsonIgnore
	public void setJudgement(ServerOutput.JUDGEMENT judgement) {
		this.judgement = judgement;
	}

	public void setJudgement(String judgement) {
		if (judgement == null || "".equals(judgement)) {
			this.setJudgement(ServerOutput.JUDGEMENT.SE);
		}
		this.setJudgement(ServerOutput.JUDGEMENT.valueOf(judgement));
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isAccessible() {
		return accessible;
	}

	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}

}
