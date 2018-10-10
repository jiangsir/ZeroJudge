/**
 * idv.jiangsir.zerojudge.model - JudgeObject.java
 * 2009/3/6 下午 04:45:33
 * jiangsir
 */
package tw.zerojudge.Judges;

import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Solution;

/**
 * @author jiangsir
 * 
 */
public class JudgeObject {

	private String code;
	private Language language;

	private String session_account;

	public static enum PRIORITY {
		Zero, 
		Submit, 
		Prejudge, 
		Testjudge, 
		TESTCODE, 
		MANUALJUDGE, 
		Rejudge;
	}

	private PRIORITY priority = JudgeObject.PRIORITY.Submit;

	private Problem problem;
	private Solution solution;

	public JudgeObject(Problemid problemid) {
		this.solution = new Solution();
		this.problem = new ProblemService().getProblemByProblemid(problemid);
		this.language = problem.getLanguage();
		this.code = problem.getSamplecode();
	}

	/**
	 * 
	 * @param session_account
	 * @param from
	 * @param solution
	 * @param problem
	 */
	public JudgeObject(PRIORITY priority, Solution solution, Problem problem) throws Exception {
		this.priority = priority;
		this.solution = solution;
		this.problem = problem;

		if (priority == JudgeObject.PRIORITY.Submit || priority == JudgeObject.PRIORITY.Rejudge
				|| priority == JudgeObject.PRIORITY.Testjudge) {
			this.code = solution.getCode();
			this.language = solution.getLanguage();
		} else if (priority == JudgeObject.PRIORITY.Prejudge) {
			this.code = problem.getSamplecode();
			this.language = problem.getLanguage();
		} else if (priority == JudgeObject.PRIORITY.TESTCODE) {
			this.code = solution.getCode();
			this.language = solution.getLanguage();
		} else {
			this.code = "";
			this.language = new Language("C", "c");
		}

	}

	/**
	 * 完整程式碼
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	public void setLanguage(String language) {
		this.language = new Language(language, language.toLowerCase());
	}

	public Problem getProblem() {
		return problem;
	}

	public Solution getSolution() {
		return solution;
	}

	public PRIORITY getPriority() {
		return priority;
	}

	public void setPriority(PRIORITY priority) {
		this.priority = priority;
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

}
