package tw.zerojudge.DAOs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.JsonObjects.Compiler;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Solution;

public class SolutionService {
	public static Hashtable<Integer, Solution> HashSolutions = new Hashtable<Integer, Solution>();

	public Hashtable<Integer, Solution> gethashSolutions() {
		return HashSolutions;
	}

	/**
	 * 
	 * @param solutionid
	 * @return
	 */
	public Solution getSolutionById(Integer solutionid) {
		if (!HashSolutions.containsKey(solutionid)) {
			Solution solution = new SolutionDAO().getSolutionById(solutionid);
			HashSolutions.put(solutionid, solution);
		}

		return HashSolutions.get(solutionid);
	}

	public Solution getSolutionById(String solutionid) {
		if (solutionid == null || "".equals(solutionid) || !solutionid.matches("[0-9]+")) {
			return new Solution();
		}
		try {
			return getSolutionById(Integer.parseInt(solutionid));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new Solution();
		}
	}

	/**
	 * 取得目前所有 solution 的數量。
	 * 
	 * @return
	 */
	public int getCountByAllSolution() {
		return new SolutionDAO().executeCount("solutions", new TreeMap<String, Object>());
	}

	/**
	 * 依 best solution 原則排序
	 * 
	 * @param pagenum
	 * @return
	 */
	public ArrayList<Solution> getBestSolutions(int pid, Language language, int pagenum) {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getBestSolutionids(pid, language, pagenum)) {
			solutions.add(this.getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 用 codelocker 取的 solutionBean
	 * 
	 * @param session
	 * @param codelocker
	 * @param pagenum
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByCodelocker(HttpSession session, int codelocker, int pagenum) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		rules.add("codelocker=" + codelocker);
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, "id DESC", pagenum)) {
			solutions.add(getSolutionById((int) solutionid));
		}
		return solutions;
	}

	public ArrayList<Solution> getSolutionsByLanguage(Language language, int page) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		rules.add("language='" + language.getName() + "'");
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, "id DESC", page)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 經由 status 取得 list
	 * 
	 * @param status
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByJudgement(ServerOutput.JUDGEMENT judgement, int pagenum) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		rules.add("judgement=" + judgement.ordinal());
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, "id DESC", pagenum)) {
			solutions.add(getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得某題目某語言的解題列表
	 * 
	 * @param problemid
	 * @param language
	 * @param pagenum
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByProblemidLanguage(Problemid problemid, Language language, int pagenum) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		rules.add("pid=" + new ProblemService().getProblemByProblemid(problemid).getId());
		rules.add("language='" + language.getName() + "'");
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, "id DESC", pagenum)) {
			solutions.add(getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 用 problemid 來取得 solution 分頁
	 * 
	 * @param problemid
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByProblemid(Problemid problemid, int page) {
		int pid = new ProblemService().getProblemByProblemid(problemid).getId();
		return this.getSolutionsByPid(pid, page);
	}

	/**
	 * 取得某一個 problemid 的所有 solutions
	 * 
	 * @param problemid
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByProblemid(Problemid problemid) {
		int pid = new ProblemService().getProblemByProblemid(problemid).getId();
		return this.getSolutionsByPid(pid, 0);
	}

	/**
	 * 取得某一個 pid 的所有 solutions
	 * 
	 * @param pid
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByPid(int pid, int page) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		rules.add("pid=" + pid);
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, "id DESC", page)) {
			solutions.add(getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 獲取某位使用者在某個 contest 當中的解題紀錄。
	 * 
	 * @param userid
	 * @param contestid
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByUseridContestid(int userid, int contestid, int page) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.incontest.getValue());
		rules.add("userid=" + userid);
		rules.add("contestid=" + contestid);
		String ORDERBY = "id DESC";

		ArrayList<Long> solutionids = new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, page);
		solutionids.removeAll(new ContestService().getContestById(contestid).getRemovedsolutionids());
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : solutionids) {
			solutions.add(getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得 contestid 的 Solutions page==0 代表全部取出.
	 * 
	 * @param contest
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByContest(Contest contest, int page) {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (Integer solutionid : new SolutionDAO().getSolutionidsByContest(contest)) {
			solutions.add(new SolutionService().getSolutionById(solutionid));
		}
		return solutions;
	}

	public ArrayList<Solution> getSolutionsByPidUseridSolutionid(int pid, int userid, int solutionid) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("pid=" + pid);
		rules.add("userid=" + userid);
		rules.add("id!=" + solutionid);
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long id : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, 0)) {
			solutions.add(new SolutionService().getSolutionById((int) id));
		}
		return solutions;
	}

	/**
	 * 用 problemid, 及某個 status 來取得 submissions
	 * 
	 * @param problemid
	 * @return
	 * @throws DataException
	 */
	public ArrayList<Solution> getSolutionsByProblemidJudgement(Problemid problemid, ServerOutput.JUDGEMENT judgement,
			int page) throws DataException {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		rules.add("pid=" + new ProblemService().getProblemByProblemid(problemid).getId());
		rules.add("judgement=" + judgement.ordinal());
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, page)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得 某人,某題 的所有解題狀態
	 * 
	 * @return
	 * @throws DataException
	 */
	public ArrayList<Solution> getSolutionsByPidUserid(String account, Problemid problemid, int page)
			throws DataException {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		rules.add("pid=" + new ProblemService().getProblemByProblemid(problemid).getId());
		rules.add("userid=" + new UserService().getUserByAccount(account).getId());
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, page)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 某人 某題 的 某種狀態
	 * 
	 * 
	 * @param account
	 * @param judgement
	 * @param problemid
	 * @param page
	 * @return
	 */
	public ArrayList<Solution> getSolutions(String account, ServerOutput.JUDGEMENT judgement, Problemid problemid,
			int page) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("userid=" + new UserService().getUserByAccount(account).getId());
		rules.add("judgement=" + judgement.ordinal());
		rules.add("pid=" + new ProblemService().getProblemByProblemid(problemid).getId());
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, page)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * submissions 查詢使用<br>
	 * 如: 某人 AC 的題目
	 * 
	 * @param session
	 * @param userid
	 * @param judgement
	 * @param page
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByUseridJudgement(Integer userid, ServerOutput.JUDGEMENT judgement,
			int page) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("userid=" + userid);
		rules.add("judgement=" + judgement.ordinal());
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, page)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 用 userid 取得 status
	 * 
	 * @param userid
	 * @param page
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByUserid(Integer userid, int page) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("userid=" + userid);
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, page)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得 contest 裡指定 problemid 的所有 solutions
	 * 
	 * @param contestid
	 * @param pid
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByContestidProblemid(int contestid, int pid) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("contestid=" + contestid);
		rules.add("pid=" + pid);
		rules.add("visible=" + Solution.VISIBLE.incontest.getValue());
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, 0)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得某種 status 的 solutions
	 * 
	 * @param judgement
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByJudgement_NOUSE(ServerOutput.JUDGEMENT judgement) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("judgement=" + judgement.ordinal());
		String ORDERBY = "id DESC";

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, 0)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得一個分頁的 solutions
	 * 
	 * @param page
	 * @return
	 */
	public ArrayList<Solution> getSolutions(int page) {
		TreeSet<String> rules = new TreeSet<String>();
		String ORDERBY = "id DESC";

		if (page > 500) {
			rules.add("visible=" + Solution.VISIBLE.open.getValue());
			//
		} else {
			rules.add("visible=" + Solution.VISIBLE.open.getValue());
		}
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, ORDERBY, page)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得某一定範圍的 solution 來進行 rejudge
	 * 
	 * @param solutionidfrom
	 * @param solutionidto
	 * @param statusarray
	 * @param languagearray
	 * @return
	 */
	public ArrayList<Solution> getSolutionsForRejudge(Integer solutionidfrom, Integer solutionidto,
			String[] statusarray, String[] languagearray) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("id>=" + solutionidfrom);
		rules.add("id<=" + solutionidto);
		String judgementrules = "";
		for (int i = 0; i < statusarray.length; i++) {
			judgementrules += (i == 0 ? "" : " OR ") + " judgement="
					+ ServerOutput.JUDGEMENT.valueOf(statusarray[i]).ordinal();
		}
		rules.add(judgementrules);
		String languages = "";
		for (int i = 0; i < languagearray.length; i++) {
			languages += (i == 0 ? "" : " OR ") + " language='" + languagearray[i] + "' ";
		}
		rules.add(languages);
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (long solutionid : new SolutionDAO().getSolutionidsByRules(rules, "", 0)) {
			solutions.add(new SolutionService().getSolutionById((int) solutionid));
		}
		return solutions;
	}

	/**
	 * 取得某 solutionid 所在頁數
	 * 
	 * @param solutionid
	 * @return
	 */
	public int getPageNum(Integer solutionid) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("id>=" + solutionid);
		rules.add("visible=" + Solution.VISIBLE.open.getValue());

		int PAGESIZE = ApplicationScope.getAppConfig().getPageSize();
		int count = new SolutionDAO().getCountByRules(rules);
		return ((count - 1) / PAGESIZE) + 1;
	}

	public int update(Solution solution) {
		int result = new SolutionDAO().update(solution);
		HashSolutions.put(solution.getId(), solution);
		return result;
	}

	public int insert(Solution solution) {
		int id = new SolutionDAO().insert(solution);
		solution.setId(id);
		HashSolutions.put(id, solution);
		return id;
	}

	/**
	 * 對任何空資料表來說，必須先加入預設的每一種編譯器的 solutions，在新建立的系統當中放入基本資料。
	 * 
	 * @throws AccessException
	 */
	public synchronized void insertInitSolutions() {
		if (this.getCountByAllSolution() > 0) {
			return;
		}
		for (Compiler compiler : ApplicationScope.getAppConfig().getServerConfig().getEnableCompilers()) {
			Solution solution = new Solution();
			solution.setUserid(1);
			solution.setPid(1);
			solution.setIpfrom(new ArrayList<IpAddress>() {
				private static final long serialVersionUID = 1L;
				{
					add(new IpAddress());
				}
			});
			solution.setSubmittime(new Timestamp(System.currentTimeMillis()));
			try {
				solution.setLanguage(compiler.getLanguage());
				solution.setCode(compiler.getSamplecode());
				solution.setCodelength(solution.getCode().length());
				this.insert(solution);
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
	}

}
