/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.JsonObjects;

import java.util.ArrayList;
import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.Contest;

public class ExportContest {
	private int targetContestid = 0;
	private Contest contest = new Contest();
	private ArrayList<Solution> solutions = new ArrayList<Solution>();
	private ArrayList<Contestant> contestants = new ArrayList<Contestant>();

	public int getTargetContestid() {
		return targetContestid;
	}

	public void setTargetContestid(int targetContestid) {
		this.targetContestid = targetContestid;
	}

	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public ArrayList<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(ArrayList<Solution> solutions) {
		this.solutions = solutions;
	}

	public ArrayList<Contestant> getContestants() {
		return contestants;
	}

	public void setContestants(ArrayList<Contestant> contestants) {
		this.contestants = contestants;
	}

}
