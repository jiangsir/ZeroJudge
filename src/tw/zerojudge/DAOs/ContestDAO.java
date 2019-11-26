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
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Factories.ContestFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Utils.*;

public class ContestDAO extends SuperDAO<Contest> {
	private int lastpage = 0;
	ObjectMapper mapper = new ObjectMapper(); 

	public ContestDAO() {
	}

	/**
	 * 取得 lastpage
	 * 
	 * @return
	 */
	public int getLastpage() {
		return this.lastpage;
	}

	protected synchronized int insert(Contest contest) throws DataException {
		if (contest.getScores().length != contest.getProblemids().size()) {
			throw new DataException(
					"題目數量(" + contest.getProblemids().size() + ")與分數數量(" + contest.getScores().length + ")不相同！請修正。");
		}

		String sql = "INSERT INTO contests(problemids, scores, removedsolutionids, ownerid, "
				+ "vclassid, userrules, starttime, timelimit, pausepoints, title, subtitle, "
				+ "visible, conteststatus, addonprivilege, rankingmode, "
				+ "freezelimit, config) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, contest.getProblemids().toString());
			pstmt.setString(2, Arrays.toString(contest.getScores()));
			pstmt.setString(3, contest.getRemovedsolutionids().toString());
			pstmt.setInt(4, contest.getOwnerid());
			pstmt.setInt(5, contest.getVclassid());
			pstmt.setString(6, contest.getUserrules().toString());
			pstmt.setTimestamp(7, new Timestamp(contest.getStarttime().getTime()));
			pstmt.setLong(8, contest.getTimelimit());
			try {
				String pausepoints = mapper.writeValueAsString(contest.getPausepoints());
				pstmt.setString(9, pausepoints);
			} catch (JsonGenerationException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			pstmt.setString(10, contest.getTitle());
			pstmt.setString(11, contest.getSubtitle());
			pstmt.setString(12, contest.getVisible().toString());
			pstmt.setString(13, contest.getConteststatus().toString());
			pstmt.setString(14, contest.getAddonprivilege());
			pstmt.setString(15, contest.getRankingmode().toString());
			pstmt.setInt(16, contest.getFreezelimit());
			pstmt.setInt(17, contest.getConfig());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		contest.setId(id);

		return id;
	}


	protected synchronized int update(Contest contest) throws DataException {
		if (contest.getScores().length != contest.getProblemids().size()) {
			throw new DataException(
					"題目數量(" + contest.getProblemids().size() + ")與分數數量(" + contest.getScores().length + ")不相同！請修正。");
		}

		String SQL = "UPDATE contests SET problemids=?, scores=?, ownerid=?, "
				+ "vclassid=?, userrules=?, starttime=?, timelimit=?, pausepoints=?, title=?, "
				+ "subtitle=?, taskid=?, visible=?, conteststatus=?, "
				+ "addonprivilege=?, rankingmode=?, freezelimit=?, " + "config=?, removedsolutionids=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setString(1, contest.getProblemids().toString());
			pstmt.setString(2, Arrays.toString(contest.getScores()));
			pstmt.setInt(3, contest.getOwnerid());
			pstmt.setInt(4, contest.getVclassid());
			pstmt.setString(5, contest.getUserrules().toString());
			pstmt.setTimestamp(6, contest.getStarttime());
			pstmt.setLong(7, contest.getTimelimit());

			try {
				String pausepoints = mapper.writeValueAsString(contest.getPausepoints());
				pstmt.setString(8, pausepoints);
			} catch (JsonGenerationException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			pstmt.setString(9, contest.getTitle());
			pstmt.setString(10, contest.getSubtitle());
			pstmt.setInt(11, contest.getTaskid());
			pstmt.setString(12, contest.getVisible().toString());
			pstmt.setString(13, contest.getConteststatus().toString());
			pstmt.setString(14, contest.getAddonprivilege());
			pstmt.setString(15, contest.getRankingmode().toString());
			pstmt.setInt(16, contest.getFreezelimit());
			pstmt.setInt(17, contest.getConfig());
			pstmt.setString(18, contest.getRemovedsolutionids().toString());
			pstmt.setInt(19, contest.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			return 0;
		}
		return result;
	}


	public ArrayList<Contest> getContestsByRules(Set<String> rules, String orderby, int page) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT * FROM contests WHERE 1=1");
		Iterator<String> ruleit = rules.iterator();
		while (ruleit.hasNext()) {
			sql.append(" AND (" + ruleit.next() + ")");
		}
		if (orderby != null && !"".equals(orderby)) {
			sql.append(" ORDER BY " + orderby);
		}

		if (page != 0) {
			sql.append(" LIMIT " + ((page > 1 ? page : 1) - 1) * ApplicationScope.getAppConfig().getPageSize() + ","
					+ ApplicationScope.getAppConfig().getPageSize());
		}
		return executeQuery(sql.toString(), Contest.class);
	}




	/**
	 * 取得課程當中的所有隨堂測驗。
	 * 
	 * @return
	 */
	public ArrayList<Contest> getContestsByVclassid(int vclassid) {
		String sql = "SELECT * FROM contests WHERE vclassid=" + vclassid + " ORDER BY id DESC";
		return executeQuery(sql, Contest.class);
	}

	public ArrayList<Contest> getAllContests() {
		String sql = "SELECT * FROM contests";
		return executeQuery(sql, Contest.class);
	}

	public synchronized void updateContestRanking(int userid, Integer contestid) {
		Contest contest = this.getContestById(contestid);
		switch (contest.getRankingmode()) {
			case CSAPC :
				this.updateContestRanking_CSAPC(userid, contest);
				break;
			case HSIC :
				this.updateContestRanking_HSIC(userid, contest);
				break;
			case NPSC :
				this.updateContestRanking_NPSC(userid, contest);
				break;
			default :
				break;
		}
	}

	public void rebuiltContestRanking(Contest contest) {
		for (Integer userid : new SolutionDAO().getUseridsByContestid(contest.getId())) {

			switch (contest.getRankingmode()) {
				case CSAPC :
					break;
				case HSIC :
					this.updateContestRanking_HSIC(userid, contest);
					break;
				case NPSC :
					break;
				default :
					break;
			}
		}
	}

	/**
	 * 使用在 "一般針對國內的競賽" 當中，使用 NPSC 的 penalty 方式會比較熟悉 <br>
	 * 計分方式：NPSC 的 penalty 方式 加上 分數的排名
	 * 
	 * @param userid
	 * @param contest
	 */
	private void updateContestRanking_NPSC(int userid, Contest contest) {

		ArrayList<Solution> solutions = new SolutionService().getSolutionsByUseridContestid(userid, contest.getId(), 0);

		int totalscore = 0, penalty = 0;
		String[] problemids = (String[]) contest.getProblemids().toArray();
		int[] scorearray = contest.getScores();
		int[] lastscores = new int[problemids.length];
		int[] acpenalty = new int[problemids.length];
		int[] nonacpenalty = new int[problemids.length];
		Arrays.fill(lastscores, -1);
		Arrays.fill(acpenalty, -1);
		Arrays.fill(nonacpenalty, -1);

		for (Solution solution : solutions) {
			int index = new Utils().indexOf(problemids, solution.getProblem().getProblemid().toString());
			if (index < 0) {
				continue;
			}
			if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
				acpenalty[index] = (int) Math.round(solution.getSpending() / 60000.0);
			} else {
				nonacpenalty[index] += 20;
			}
			lastscores[index] = solution.getScore();
		}
		for (int i = 0; i < lastscores.length; i++) {
			if (lastscores[i] > 0) {
				totalscore += lastscores[i] * scorearray[i] / 100;
			}
		}
		String[] aclist = new String[problemids.length];
		int ac = 0;
		for (int i = 0; i < lastscores.length; i++) {
			if (acpenalty[i] >= 0) {
				aclist[i] = ServerOutput.JUDGEMENT.AC.name();
				ac++;
				penalty += acpenalty[i];
				penalty += nonacpenalty[i];
			} else {
				aclist[i] = String.valueOf(lastscores[i]);
			}
		}
		User user = new UserService().getUserById(userid);
		Contestant contestant = contest.getContestantByUserid(user.getId());
		contestant.setAc(ac);
		contestant.setAclist(aclist);
		contestant.setSubmits(solutions.size());
		contestant.setPenalty(penalty);
		contestant.setScore(totalscore);
		new ContestantDAO().update(contestant);
	}

	/**
	 * 計算每一個嘗試過的題目，不論對錯，的最後一次 submit 的時間和 <br>
	 * 用在 千人等級競賽
	 * 
	 * @param contestid
	 */
	private void rebuiltContestRanking_ALL_CSAPC(Contest contest) {
		String[] problemids = (String[]) contest.getProblemids().toArray();
		int[] scores = contest.getScores();
		String[] acarray = new String[problemids.length];
		String sql = "SELECT solutionid FROM (SELECT solutionid FROM solutions WHERE contestid=" + contest.getId()
				+ " ORDER BY submittime DESC) as temp GROUP BY " + "userid, pid ORDER BY userid";
		ArrayList<Solution> solutions = new SolutionDAO().executeQuery(sql, Solution.class);

		String account = "";
		int[] lastscores = new int[contest.getProblemids().size()];
		String[] aclist = new String[contest.getProblemids().size()];
		int contestac = 0;
		int penalty = 0;
		int submitsize = 0;
		int totalscore = 0;
		for (Solution solution : solutions) {
			submitsize++;
			User solutionuser = new UserService().getUserById(solution.getUserid());
			if (!account.equals(solutionuser.getAccount())) { 
				for (int i = 0; i < acarray.length; i++) {
					aclist[i] = acarray[i];
				}
				String sql2 = "UPDATE contestants SET ac='" + contestac + "', aclist='" + Arrays.toString(aclist)
						+ "', submits='" + submitsize + "', penalty='" + penalty + "', score=" + totalscore
						+ " WHERE teamaccount='" + account + "' AND contestid=" + contest.getId();
				this.execute(sql2);

				account = solutionuser.getAccount();
				for (int i = 0; i < lastscores.length; i++) {
					lastscores[i] = -1;
					acarray[i] = "-";
				}
				submitsize = 0;
				totalscore = 0;
				penalty = 0;
				contestac = 0;
				Arrays.fill(aclist, "-");
			}
			int index = new Utils().indexOf(problemids, solution.getProblem().getProblemid().toString());
			if (index < 0) {
				continue;
			}
			totalscore += solution.getScore() * scores[index] / 100;
			penalty += (int) Math.round(solution.getSpending() / 60000.0);
			if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
				contestac++;
				acarray[index] = ServerOutput.JUDGEMENT.AC.name();
			} else {
				acarray[index] = String.valueOf(solution.getScore());
			}
		}
		for (int i = 0; i < acarray.length; i++) {
			aclist[i] = acarray[i];
		}

		String sql2 = "UPDATE contestants SET ac='" + contestac + "', aclist='" + Arrays.toString(aclist)
				+ "', submits='" + submitsize + "', penalty='" + penalty + "', score=" + totalscore
				+ " WHERE teamaccount='" + account + "' AND contestid=" + contest.getId();
		this.execute(sql2);
	}

	/**
	 * 20100906<br>
	 * 左中比賽方式。比較接近真實的比賽環境。也就是以第一個 AC 為準來計算分數及耗時。 <br>
	 * 20100923 改為後決!
	 * 
	 * @param registid
	 * @param contest
	 * @throws AccessException
	 * @throws DataException
	 */
	private void updateContestRanking_HSIC_ASC_TODELETE(int userid, Contest contest) {
		HashMap<Problemid, ArrayList<Solution>> problemMap = new HashMap<Problemid, ArrayList<Solution>>();

		ArrayList<Solution> solutions = new SolutionService().getSolutionsByUseridContestid(userid, contest.getId(), 0);

		int submits = 0;
		for (Solution solution : solutions) {
			submits++;
			Problemid problemid = solution.getProblemid();
			if (problemMap.containsKey(problemid)) {
				problemMap.get(problemid).add(solution);
			} else {
				ArrayList<Solution> solutionList = new ArrayList<Solution>();
				solutionList.add(solution);
				problemMap.put(problemid, solutionList);
			}
		}
		String[] problemids = (String[]) contest.getProblemids().toArray();
		int[] scores = contest.getScores();
		String[] aclistarray = new String[problemids.length];
		double totalscore = 0;
		int penalty = 0, contestac = 0;
		for (int i = 0; i < problemids.length; i++) {
			int wrong = 0, maxscore = 0;
			ArrayList<Solution> solutionList = problemMap.get(problemids[i].trim());
			if (solutionList == null) {
				continue;
			}
			Iterator<?> solution_it = solutionList.iterator();
			while (solution_it.hasNext()) {
				Solution solution = (Solution) solution_it.next();
				if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
					contestac++;
					aclistarray[i] = ServerOutput.JUDGEMENT.AC.name();
					penalty += (int) Math.round(solution.getSpending() / 60000.0) + (wrong * 20);
					maxscore = solution.getScore();
					break;
				} else {
					wrong++;
					aclistarray[i] = String.valueOf(Math.max(solution.getScore(), maxscore));
				}
				maxscore = Math.max(solution.getScore(), maxscore);
			}
			totalscore += maxscore * (scores[i] / 100.0);
		}
		String aclist = "";
		for (int i = 0; i < aclistarray.length; i++) {
			if (i == 0) {
				aclist = aclistarray[i];
			} else {
				aclist += "," + aclistarray[i];
			}
		}
		Contestant contestant = contest.getContestantByUserid(userid);
		contestant.setAc(contestac);
		contestant.setAclist(aclist);
		contestant.setSubmits(submits);
		contestant.setPenalty(penalty);
		contestant.setScore((int) Math.round(totalscore));
		new ContestantDAO().update(contestant);
	}

	/**
	 * 20100906<br>
	 * 左中比賽方式。比較接近真實的比賽環境。也就是以第一個 AC 為準來計算分數及耗時。 <br>
	 * 20100923 改為後決，且只處理最後一筆。
	 * 
	 * @param registid
	 * @param contest
	 * @throws AccessException
	 * @throws DataException
	 */
	private void updateContestRanking_HSIC_LastMajor_TODELETE(int userid, Contest contest) {
		/**
		 * 先決理由：如果某題目或測資發生錯誤，全部重測之後發現某些選手前面送出的程式碼就已經答對了。<br>
		 * 就應該直接計算前面的 AC 時間。 在第一個 AC 之前的錯誤次數 * 20 加入時間當中，看似比較合理。 <br>
		 * <br>
		 * 後決理由：只取最後一筆處理，對效能來說比較簡潔。但，現在整個 contest 的 solution 全部都放入 Hash
		 * 了，應該差異不大了。
		 */

		HashMap<Problemid, ArrayList<Solution>> problemMap = new HashMap<Problemid, ArrayList<Solution>>();

		ArrayList<Solution> solutions = new SolutionService().getSolutionsByUseridContestid(userid, contest.getId(), 0);

		int submits = 0;
		for (Solution solution : solutions) {
			submits++;
			Problemid problemid = solution.getProblemid();
			if (problemMap.containsKey(problemid)) {
				problemMap.get(problemid).add(solution);
			} else {
				ArrayList<Solution> solutionList = new ArrayList<Solution>();
				solutionList.add(solution);
				problemMap.put(problemid, solutionList);
			}
		}
		String[] problemids = (String[]) contest.getProblemids().toArray();
		int[] scores = contest.getScores();
		String[] aclistarray = new String[problemids.length];
		double totalscore = 0;
		int penalty = 0, contestac = 0;
		for (int i = 0; i < problemids.length; i++) {
			int wrong = 0, maxscore = 0;
			ArrayList<Solution> solutionList = problemMap.get(problemids[i].trim());
			if (solutionList == null) {
				continue;
			}
			Solution solution = solutionList.get(0);
			if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
				contestac++;
				aclistarray[i] = solution.getJudgement().name();
				penalty += (int) Math.round(solution.getSpending() / 60000.0);
				Iterator<Solution> sit = solutionList.iterator();
				while (sit.hasNext()) {
					Solution thesolution = sit.next();
					if (thesolution.getJudgement() != ServerOutput.JUDGEMENT.AC) {
						penalty += 20;
					}
				}
			} else {
				aclistarray[i] = String.valueOf(solution.getScore());
			}
			totalscore += solution.getScore() * (scores[i] / 100.0);
		}
		String aclist = "";
		for (int i = 0; i < aclistarray.length; i++) {
			if (i == 0) {
				aclist = aclistarray[i];
			} else {
				aclist += "," + aclistarray[i];
			}
		}
		Contestant contestant = contest.getContestantByUserid(userid);
		contestant.setAc(contestac);
		contestant.setAclist(aclist);
		contestant.setSubmits(submits);
		contestant.setPenalty(penalty);
		contestant.setScore((int) Math.round(totalscore));
		new ContestantDAO().update(contestant);
	}

	/**
	 * 實作為先決。
	 * 
	 * @param registid
	 * @param contest
	 * @throws AccessException
	 * @throws DataException
	 */
	private synchronized void updateContestRanking_HSIC(int userid, Contest contest) {
		/**
		 * 20111014 <br>
		 * 先決理由：如果某題目或測資發生錯誤，全部重測之後發現某些選手前面送出的程式碼就已經答對了。<br>
		 * 就應該直接計算前面的 AC 時間。 在第一個 AC 之前的錯誤次數 * 20 加入時間當中，看似比較合理。 <br>
		 * 如果題目配分一樣，且不分段給分。總體效果就等同 NPSC。 <br>
		 * 後決理由：只取最後一筆處理，對效能來說比較簡潔。但，現在整個 contest 的 solution 全部都放入 Hash
		 * 了，應該差異不大了。
		 */

		int[] scores = contest.getScores(); 
		String[] scoreboard = new String[contest.getProblemids().size()]; 
		Arrays.fill(scoreboard, "-");
		int[] wrongcount = new int[contest.getProblemids().size()]; 

		ArrayList<Solution> solutions = contest.getSolutions();
		Collections.reverse(solutions);

		int penalty = 0;
		double totalscore = 0;
		int ac = 0;
		int submits = 0;
		for (Solution solution : solutions) {
			if (userid != solution.getUserid().intValue()) {
				continue;
			}
			Problem problem = solution.getProblem();
			submits++;
			if (!contest.getProblemids().contains(problem.getProblemid())) {
				continue;
			}
			int problemindex = this.getContestProblemIndex(contest, problem);
			if (scoreboard[problemindex].equals(ServerOutput.JUDGEMENT.AC.name())) { 
				continue;
			}
			if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
				scoreboard[problemindex] = solution.getJudgement().name();
				penalty += (int) Math.round(solution.getSpending() / 60000.0) + (wrongcount[problemindex] * 20);
				ac++;
			} else {
				int score = scoreboard[problemindex].equals("-") ? 0 : Integer.parseInt(scoreboard[problemindex]);
				scoreboard[problemindex] = String.valueOf(Math.max(score, solution.getScore()));
				wrongcount[problemindex]++;
			}
		}

		Collections.reverse(solutions);

		int problemindex = 0;
		for (Problemid problemid : contest.getProblemids()) {
			if (scoreboard[problemindex].equals(ServerOutput.JUDGEMENT.AC.name())) {
				totalscore += new ProblemService().getProblemByProblemid(problemid).getAcscore() * scores[problemindex]
						/ 100.0;
			} else if (scoreboard[problemindex].matches("[0-9]+")) {
				totalscore += Integer.parseInt(scoreboard[problemindex]) * (scores[problemindex] / 100.0);
			}

			problemindex++;
		}
		User user = new UserService().getUserById(userid);
		Contestant contestant = contest.getContestantByUserid(user.getId());
		contestant.setAc(ac);
		contestant.setAclist(scoreboard);
		contestant.setSubmits(submits);
		contestant.setPenalty(penalty);
		contestant.setScore((int) Math.round(totalscore));
		new ContestantDAO().update(contestant);
	}

	/**
	 * 一個 solution 在競賽當中送出的話，必須進行 update result 的工作，<br>
	 * 確保 contest 狀態正確 效率考量，改為以 account 為單位進行計算<br>
	 * 計分方式：CSAPC IOI + Time penalty 欄位放置 每個題目不論對錯最後送出的時間和<br>
	 * 
	 * 20100906 這個只取出每人每題的最後一次 solution. 無法處理第一次 AC 的 solution. 因此要修改
	 * 
	 * @param account
	 * @param contestid
	 * @throws AccessException
	 * @throws DataException
	 */
	private void updateContestRanking_CSAPC(int userid, Contest contest) {
		String sql = "SELECT solutionid FROM (SELECT solutionid FROM solutions WHERE userid=" + userid
				+ " AND contestid=" + contest.getId() + " ORDER BY solutionid DESC) as temp GROUP BY "
				+ "userid, problemid";
		int totalscore = 0, time = 0;
		String[] problemids = (String[]) contest.getProblemids().toArray();
		int[] scorearray = contest.getScores();
		String[] aclist = new String[problemids.length];
		int contestac = 0, submits = 0;
		for (Solution solution : new SolutionDAO().executeQuery(sql, Solution.class)) {
			submits++;
			Problem problem = solution.getProblem();
			int index = new Utils().indexOf(problemids, problem.getProblemid().toString());
			if (index < 0) {
				continue;
			}
			if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
				contestac++;
				aclist[index] = solution.getJudgement().name();
				time += (int) Math.round(solution.getSpending() / 60000.0);
			} else {
				aclist[index] = String.valueOf(solution.getScore());
			}
			totalscore += solution.getScore() * scorearray[index] / 100;
		}
		Contestant contestant = contest.getContestantByUserid(userid);
		contestant.setAc(contestac);
		contestant.setAclist(aclist);
		contestant.setSubmits(submits);
		contestant.setPenalty(time);
		contestant.setScore(totalscore);
		new ContestantDAO().update(contestant);
	}

	/**
	 * 取得某 contest 所有的 list, 不分頁
	 * 
	 * @deprecated 改成放在 Contest 內的 getSolutionids()
	 * @return
	 */
	public ArrayList<Solution> getSolutions(Integer contestid) {
		return null;
	}

	public String addUsersrule(String userrule, String newrule) {
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		String[] rules = userrule.split(",");
		for (int i = 0; i < rules.length; i++) {
			set.add(rules[i].trim());
		}
		set.add(newrule.trim());
		String s = set.toString();
		return s.substring(1, s.length() - 1);
	}



	public Contest getContestById(int contestid) {
		String sql = "SELECT * FROM contests WHERE id=" + contestid;
		for (Contest contest : executeQuery(sql, Contest.class)) {
			return contest;
		}
		return ContestFactory.getNullcontest();
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

	public Contest getLastVContest(int vclassid) {
		String sql = "SELECT * FROM contests WHERE vclassid=" + vclassid + " ORDER BY starttime DESC LIMIT 0,1";
		for (Contest contest : executeQuery(sql, Contest.class)) {
			return contest;
		}
		return ContestFactory.getNullcontest();
	}

	public ArrayList<Contest> getVContests(Integer vclassid) {
		String sql = "SELECT * FROM contests WHERE vclassid=" + vclassid + " ORDER BY id DESC";
		return executeQuery(sql, Contest.class);
	}

	public ArrayList<Contest> getRestartingContests() {
		String sql = "SELECT * FROM contests WHERE conteststatus='" + Contest.STATUS.RESTARTING.getValue() + "'";
		return executeQuery(sql, Contest.class);
	}

	public ArrayList<Contest> getSuspendingContests() {
		String sql = "SELECT * FROM contests WHERE conteststatus='" + Contest.STATUS.SUSPENDING.getValue() + "'";
		return executeQuery(sql, Contest.class);
	}

	private int getContestProblemIndex(Contest contest, Problem problem) {
		int problemindex = -1;
		for (Problemid problemid : contest.getProblemids()) {
			problemindex++;
			if (problemid.equals(problem.getProblemid())) {
				return problemindex;
			}
		}
		return problemindex;
	}

	protected int getCountByAllContests() {
		return this.executeCount("contests", new TreeMap<String, Object>());
	}

	public int getCountByRules(TreeSet<String> rules) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT id FROM contests ");
		sql.append(this.makeRules(rules, null, 0));
		return executeCount(sql.toString());
	}

}
