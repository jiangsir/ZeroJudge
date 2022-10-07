package tw.zerojudge.Objects;

import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Judges.ServerOutput.JUDGEMENT;
import tw.zerojudge.Tables.Solution;

public class Codediff {
	private int solutionid = 0;
	private String code_0 = ""; 
	private String code_1 = ""; 
	private String similarity = ""; 
	private JUDGEMENT judgement = JUDGEMENT.Waiting;

	public Codediff() {
	}

	public int getSolutionid() {
		return solutionid;
	}

	public Solution getSolution() {
		return new SolutionService().getSolutionById(this.getSolutionid());
	}

	public void setSolutionid(int solutionid) {
		this.solutionid = solutionid;
	}

	public String getCode_0() {
		return code_0;
	}

	public void setCode_0(String code_0) {
		this.code_0 = code_0;
	}

	public String getCode_1() {
		return code_1;
	}

	public void setCode_1(String code_1) {
		this.code_1 = code_1;
	}

	public String getSimilarity() {
		return similarity;
	}

	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}

	public JUDGEMENT getJudgement() {
		return judgement;
	}

	public void setJudgement(JUDGEMENT judgement) {
		this.judgement = judgement;
	}

}
