package tw.zerojudge.DAOs;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Beans.AppAttributes;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Factories.AttributeFactory;
import tw.zerojudge.Factories.ContestFactory;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.Solution.VISIBLE;
import tw.zerojudge.Utils.*;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.ServerOutput;

public class SolutionDAO extends SuperDAO<Solution> {
	ObjectMapper mapper = new ObjectMapper(); 
	int PAGESIZE = 20;

	public SolutionDAO() {
	}

	/**
	 * 將該題目的題解送出一次及以上。
	 * 
	 * @param session_account
	 * @param problemid
	 * @return
	 * @throws DataException
	 */

	public int getCountByRules(TreeSet<String> rules) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT id FROM solutions ");
		sql.append(this.makeRules(rules, null, 0));
		return executeCount(sql.toString());
	}

	/**
	 * 無法使用 pstmt 來取，預設只取出 visible = open 的
	 * 
	 * @param rules
	 *            rules 為 AND 條件
	 * @param orderby
	 *            傳入 null 代表不指定
	 * @param page
	 *            取消 page==0 全部列出。這樣會由外部參數刻意設定而導致嚴重的 lag
	 * @return
	 */
	public ArrayList<Long> getSolutionidsByRules(TreeSet<String> rules, String orderby, int page) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT id FROM solutions ");
		sql.append(this.makeRules(rules, orderby, page));
		return this.executeQueryId(sql.toString());
	}

	/**
	 * 從資料庫取出一個 solution
	 * 
	 * @param solutionid
	 * @return
	 */
	public Solution getSolutionById(int solutionid) {
		String sql = "SELECT * FROM solutions WHERE id=" + solutionid;
		for (Solution solution : executeQuery(sql, Solution.class)) {
			return solution;
		}
		return new Solution();
	}

	//

	/**
	 * 此處用到 subquery, 子查詢 因此需求 Mysql 5 以上
	 * 
	 * @param BaseCodeid
	 * @return
	 */
	public ArrayList<Solution> getCompareCodes(Integer BaseCodeid) {
		String sql = "SELECT * FROM solutions WHERE contestid=(SELECT " + "contestid FROM solutions WHERE id="
				+ BaseCodeid + " ) AND pid=" + "(SELECT pid FROM solutions WHERE id=" + BaseCodeid
				+ ") AND userid!=(SELECT userid FROM solutions " + "WHERE id=" + BaseCodeid + ") AND language="
				+ "(SELECT language FROM solutions WHERE id=" + BaseCodeid + ") AND id<" + BaseCodeid
				+ " AND judgement=" + ServerOutput.JUDGEMENT.AC.ordinal() + " ORDER BY id DESC";
		return this.executeQuery(sql, Solution.class);
	}

	/**
	 * 取得已經寫入 difflog 的 codes 結果
	 * 
	 * @param BaseCodeid
	 * @return
	 */
	public ArrayList<?> getDiffCodes(Integer BaseCodeid, Integer contestid) {
		return null;
	}

	/**
	 * 寫入 difflog 表格裡
	 * 
	 * @param BaseCode
	 * @param CompareCodes
	 */
	public synchronized void insertDifflog(Solution BaseCode, ArrayList<?> CompareCodes) {
	}

	/**
	 * 取得 difflog 裡一筆資料
	 * 
	 * @param basecodeid
	 * @param comparecodeid
	 */
	public HashMap<?, ?> getDifflog(Integer basecodeid, Integer comparecodeid) {
		return null;
	}

	protected synchronized int insert(Solution solution) throws DataException {
		User user = new UserService().getUserById(solution.getUserid());
		if (!user.getUserlanguage().getName().equals(solution.getLanguage().getName())) {
			user.setUserlanguage(solution.getLanguage());
			new UserService().update(user);
		}

		String sql = "INSERT INTO solutions (userid, vclassid, pid, " + "language, code, codelength, contestid, score, "
				+ "submittime, details, serveroutputs, ipfrom, visible) " + "VALUES(?,?,?,?,?, ?,?,?,?,?, ?,?,?)";
		int solutionid = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, solution.getUserid());
			pstmt.setInt(2, solution.getVclassid());
			pstmt.setInt(3, solution.getPid());
			pstmt.setString(4, solution.getLanguage().getName());
			pstmt.setString(5, solution.getCode());
			pstmt.setInt(6, solution.getCode().length());
			pstmt.setInt(7, solution.getContestid());
			pstmt.setInt(8, solution.getScore());
			pstmt.setTimestamp(9, new Timestamp(solution.getSubmittime().getTime()));
			pstmt.setString(10, solution.getDetails());
			try {
				pstmt.setString(11, mapper.writeValueAsString(solution.getServeroutputs()));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			pstmt.setString(12, solution.getIpfrom().toString());
			pstmt.setInt(13, solution.getVisible().getValue());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			solutionid = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			return solutionid;
		}
		solution.setId(solutionid);
		AppAttributes appAttributes = AttributeFactory.readAppAttributes();
		appAttributes.setLastSolutionid(solutionid);
		AttributeFactory.writeAppAttributes(appAttributes);

		return solutionid;
	}


	@Override
	public boolean delete(int i) {
		return false;
	}

	protected synchronized int update(Solution solution) {
		String SQL = "UPDATE solutions SET judgement=?, contestid=?, timeusage=?"
				+ ", memoryusage=?, exefilesize=?, score=?, details=?, serveroutputs=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setInt(1, solution.getJudgement().ordinal());
			pstmt.setInt(2, solution.getContestid());
			pstmt.setLong(3, solution.getTimeusage());
			pstmt.setInt(4, solution.getMemoryusage());
			pstmt.setInt(5, solution.getExefilesize());
			pstmt.setInt(6, solution.getScore());
			pstmt.setString(7, solution.getDetails());
			try {
				pstmt.setString(8, mapper.writeValueAsString(solution.getServeroutputs()));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			pstmt.setInt(9, solution.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			return result;
		}

		return result;
	}

	/**
	 * 評分完畢更改結果。
	 * 
	 * @param solutionid
	 * @param judgement
	 */
	public synchronized void updateJudgement(int solutionid, ServerOutput.JUDGEMENT judgement) {
		String SQL = "UPDATE solutions SET judgement=" + judgement.ordinal() + " WHERE id=" + solutionid;
		new SolutionDAO().execute(SQL);
		Solution solution = this.getSolutionById(solutionid);
		solution.setJudgement(judgement);
	}



	/**
	 * 獲取某個 contest 當中的所有解題紀錄。
	 * 
	 * @param contestid
	 * @return
	 */
	public TreeSet<Integer> getSolutionidsByContest(Contest contest) {
		String sql = "SELECT id FROM solutions WHERE contestid=? AND visible=? " + "ORDER BY id DESC";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, contest.getId());
			pstmt.setInt(2, Solution.VISIBLE.incontest.getValue());
			TreeSet<Integer> solutionids = new TreeSet<Integer>(new ContestFactory.DESCComparator<Integer>());
			for (long solutionid : this.executeQueryId(pstmt)) {
				solutionids.add((int) solutionid);
			}
			return solutionids;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 檢查測驗中某題目是否送出過。
	 * 
	 * @param contest
	 * @param problem
	 * @param user
	 * @return
	 */
	public TreeSet<Integer> getSolutionidsByContestProblemUser(Contest contest, Problem problem, User user) {
		String sql = "SELECT id FROM solutions WHERE contestid=? AND visible=? AND pid=? AND userid=? ORDER BY id DESC";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, contest.getId());
			pstmt.setInt(2, Solution.VISIBLE.incontest.getValue());
			pstmt.setInt(3, problem.getId());
			pstmt.setInt(4, user.getId());
			TreeSet<Integer> solutionids = new TreeSet<Integer>(new ContestFactory.DESCComparator<Integer>());
			for (long solutionid : this.executeQueryId(pstmt)) {
				solutionids.add((int) solutionid);
			}
			return solutionids;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}

	}

	/**
	 * 獲取某個 contest 當中的 testjudge 列表。
	 * 
	 * @param contestid
	 * @return
	 */
	public TreeSet<Integer> getTestjudgeidsByContestid(int contestid) {
		String sql = "SELECT id FROM solutions WHERE contestid=" + contestid + " AND visible="
				+ Solution.VISIBLE.testjudge.getValue() + " ORDER BY id DESC";
		TreeSet<Integer> solutionids = new TreeSet<Integer>(new ContestFactory.DESCComparator<Integer>());
		for (long solutionid : this.executeQueryId(sql)) {
			solutionids.add((int) solutionid);
		}
		return solutionids;
	}


	public TreeSet<Integer> getSolutionsByVclassidUserid(int vclassid, int userid) {
		String sql = "SELECT id FROM solutions WHERE vclassid=" + vclassid + " AND userid=" + userid + " AND judgement="
				+ ServerOutput.JUDGEMENT.AC.ordinal() + " GROUP BY pid";
		TreeSet<Integer> solutionids = new TreeSet<Integer>(new ContestFactory.DESCComparator<Integer>());
		for (long solutionid : this.executeQueryId(sql)) {
			solutionids.add((int) solutionid);
		}
		return solutionids;
	}

	public ArrayList<Integer> getUseridsByContestid(int contestid) {
		String sql = "SELECT userid FROM solutions WHERE contestid=" + contestid + " GROUP BY userid, contestid";
		ArrayList<Integer> userids = new ArrayList<Integer>();
		for (Solution solution : this.executeQuery(sql, Solution.class)) {
			userids.add(solution.getUserid());
		}
		return userids;
	}

	/**
	 * 判斷送出這個 solution 的 user 以前是否曾經送出過這個題目
	 * 
	 * @param user
	 * @param pid
	 * @param solutionid
	 * @return
	 */
	public boolean isTrythisProblem(User user, int pid, int solutionid) {
		String sql = "SELECT * FROM solutions WHERE pid=" + pid + " AND userid=" + user.getId() + " AND id!="
				+ solutionid;
		return executeCount(sql) == 0 ? false : true;
	}

	/**
	 * 
	 * 
	 * @param judgement
	 * @param offset
	 * 
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByJudgementWaiting() {
		String sql = "SELECT * FROM solutions WHERE judgement=" + ServerOutput.JUDGEMENT.Waiting.ordinal();
		return executeQuery(sql, Solution.class);
	}

	/**
	 * 
	 * 
	 * @param judgements
	 * @param page
	 * @return
	 */
	public ArrayList<Solution> getSolutionsByJudgement(ServerOutput.JUDGEMENT[] judgements, int page) {
		String where = "";
		for (ServerOutput.JUDGEMENT judgement : judgements) {
			if (where.equals("")) {
				where = " judgement=" + judgement.ordinal();
			} else {
				where += " OR judgement=" + judgement.ordinal();
			}
		}

		String limit = "";
		if (page != 0) {
			limit = " LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE;
		}
		if (page < 100) {
			String sql = "SELECT * FROM solutions WHERE " + where + " ORDER BY id DESC " + limit;
			return this.executeQuery(sql, Solution.class);
		} else { 
			String sql = "SELECT id FROM solutions WHERE " + where + " ORDER BY id DESC " + limit;
			ArrayList<Solution> solutions = new ArrayList<Solution>();
			for (long solutionid : this.executeQueryId(sql)) {
				solutions.add(this.getSolutionById((int) solutionid));
			}
			return solutions;
		}
	}

	/**
	 * 只計算總筆數
	 * 
	 * @param judgements
	 * @return
	 */
	public int countSolutionsByJudgement(ServerOutput.JUDGEMENT[] judgements) {
		String where = "";
		for (ServerOutput.JUDGEMENT judgement : judgements) {
			if (where.equals("")) {
				where = " judgement=" + judgement.ordinal();
			} else {
				where += " OR judgement=" + judgement.ordinal();
			}
		}
		String sql = "SELECT * FROM solutions WHERE " + where;
		return this.executeCount(sql);
	}

	public int executeCount(TreeMap<String, Object> fields) {
		return super.executeCount("solutions", fields);
	}

	/**
	 * 
	 * @param priority
	 * @param solution
	 * @param result
	 */
	public void rebuiltStatistic(JudgeObject.PRIORITY priority, Solution solution, ServerOutput.JUDGEMENT result) {
		long begintime = System.currentTimeMillis();

		if (priority == JudgeObject.PRIORITY.Submit) {
			new ProblemDAO().recountProblem_UserCount(solution.getProblem());

			new UserDAO().rebuiltUserStatistic_SPEEDUP(new UserService().getUserById(solution.getUserid()), solution,
					priority);
		} else if (priority == JudgeObject.PRIORITY.Rejudge || priority == JudgeObject.PRIORITY.MANUALJUDGE) {
			new ProblemDAO().recountProblem_UserCount(solution.getProblem());
			new UserService().rebuiltUserStatisticByDataBase(new UserService().getUserById(solution.getUserid()));
		}




		if (solution.getContestid() != 0) { 
			ContestDAO contestsDAO = new ContestDAO();
			contestsDAO.updateContestRanking(solution.getUserid(), Math.abs(solution.getContestid()));
		}
		if (solution.getVclassid() > 0 && result == ServerOutput.JUDGEMENT.AC) {
			VClassStudent vclassstudent = new VClassStudentDAO().getStudent(solution.getVclassid(),
					solution.getUserid());
			TreeSet<Problemid> aclist = vclassstudent.getVclassaclist();
			Problem problem = new ProblemService().getProblemByPid(solution.getPid());
			aclist.add(problem.getProblemid());
			vclassstudent.setVclassaclist(aclist);
			new VClassStudentDAO().update(vclassstudent);
		}

	}

	/**
	 * 依 best solution 原則排序
	 * 
	 * @param pagenum
	 * @return
	 */
	public ArrayList<Long> getBestSolutionids(int pid, Language language, int pagenum) {
		int PAGESIZE = ApplicationScope.getAppConfig().getPageSize();

		if (pagenum <= 0) {
			pagenum = 1;
		}
		String ORDERBY = " ORDER BY timeusage ASC, memoryusage ASC, codelength ASC";
		String LIMIT = " LIMIT " + (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		String sql = "SELECT id FROM solutions WHERE language=? AND pid=? " + "AND judgement=? AND visible=? " + ORDERBY
				+ LIMIT;

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, language.getName());
			pstmt.setInt(2, pid);
			pstmt.setInt(3, ServerOutput.JUDGEMENT.AC.ordinal());
			pstmt.setInt(4, Solution.VISIBLE.open.getValue());
			return executeQueryId(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}



	public ArrayList<HashMap<String, Object>> getHashMapForUserStatistic_SLOW(int userid) {
		String sql = "SELECT judgement, pid, COUNT(*) AS COUNT FROM solutions WHERE userid=" + userid + " AND visible="
				+ Solution.VISIBLE.open.getValue() + " GROUP BY pid, judgement ORDER BY pid ASC, judgement ASC";
		return this.executeQueryByMap(sql);

	}

	/**
	 * 取得某個日期之後的解題 problemid <br>
	 * 用在 每日重新計算 recount 題目的答對/解題 人數
	 * 
	 * @param timestamp
	 * @return
	 */
	protected TreeSet<Problemid> getProblemidAfter(Timestamp timestamp) {
		TreeSet<Problemid> problemidset = new TreeSet<Problemid>();
		String sql = "SELECT pid FROM solutions WHERE visible=" + VISIBLE.open.getValue() + " AND submittime>='"
				+ timestamp + "'";
		for (Solution solution : this.executeQuery(sql, Solution.class)) {
			problemidset.add(solution.getProblemid());
		}
		return problemidset;
	}

	protected Integer getMaxSolutionid() {
		String sql = "SELECT MAX(id) as max FROM solutions";
		for (HashMap<String, Object> s : this.executeQueryByMap(sql)) {
			return (Integer) s.get("max");
		}
		return 0;
	}

	/**
	 * 取得最後一個已經統計過 problemid 的 solutionid 之後的解題 problemid <br>
	 * 用在 每日重新計算 recount 題目的答對/解題 人數
	 * 
	 * @return
	 */
	protected TreeSet<Problemid> getProblemidAfter_LastSolutionid() {
		AppConfig appConfig = ApplicationScope.getAppConfig();

		TreeSet<Problemid> problemidset = new TreeSet<Problemid>();
		String sql = "SELECT pid FROM solutions WHERE visible=" + VISIBLE.open.getValue() + " AND id>="
				+ appConfig.getLast_solutionid_for_RecountProblemidset() + " GROUP BY pid";

		appConfig.setLast_solutionid_for_RecountProblemidset(this.getMaxSolutionid());

		for (Solution solution : this.executeQuery(sql, Solution.class)) {
			problemidset.add(solution.getProblemid());
		}

		new AppConfigService().update(appConfig);

		return problemidset;
	}

}
