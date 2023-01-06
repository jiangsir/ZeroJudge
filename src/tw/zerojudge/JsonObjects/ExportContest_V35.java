/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.JsonObjects;

import java.util.ArrayList;
import java.util.HashMap;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.Contest;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportContest_V35 {
	private String version = "3.5";
	private int targetContestid = 0;
	private Contest contest = new Contest();
	private ArrayList<Solution> solutions = new ArrayList<Solution>();
	private ArrayList<Contestant> contestants = new ArrayList<Contestant>();
	private ArrayList<ExportProblem> exportproblems = new ArrayList<ExportProblem>();
	private HashMap<String, Integer> problemid_pids = new HashMap<>(); 
	private HashMap<String, String> reservedmap = new HashMap<>(); 

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

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

	public ArrayList<ExportProblem> getExportProblems() {
		return exportproblems;
	}

	public void setExportProblems(ArrayList<ExportProblem> exportProblems) {
		this.exportproblems = exportProblems;
	}

	public HashMap<String, Integer> getProblemid_pids() {
		return problemid_pids;
	}

	public void setProblemid_pid(HashMap<String, Integer> problemid_pids) {
		this.problemid_pids = problemid_pids;
	}

	public HashMap<String, String> getReservedmap() {
		return reservedmap;
	}

	public void setReservedmap(HashMap<String, String> reservedmap) {
		this.reservedmap = reservedmap;
	}

}
