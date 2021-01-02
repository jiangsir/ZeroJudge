package tw.zerojudge.DAOs;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeSet;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Factories.ContestFactory;
import tw.zerojudge.JsonObjects.ExportContest;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.VClass;
import tw.zerojudge.Tables.VClassStudent;
import tw.zerojudge.Tables.Contest.VISIBLE;

public class ContestService {
	public static Hashtable<Integer, Contest> HashContests = new Hashtable<Integer, Contest>();

	public Contest getContestById(Integer contestid) {
		if (!HashContests.containsKey(contestid)) {
			Contest contest = new ContestDAO().getContestById(contestid);
			HashContests.put(contestid, contest);
		}

		return HashContests.get(contestid);
	}

	public Contest getContestById(String contestid) {
		if (contestid == null || !contestid.trim().matches("[0-9]+")) {
			return ContestFactory.getNullcontest();
		}
		try {
			int id = Integer.parseInt(contestid.trim());
			return this.getContestById(id);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return ContestFactory.getNullcontest();
	}

	public TreeSet<Integer> getSolutionidsByContest(Contest contest) {
		return new SolutionDAO().getSolutionidsByContest(contest);
	}

	public ArrayList<Contest> getRunningContests() {
		HashSet<String> rules = new HashSet<String>();
		rules.add("vclassid=0");
		rules.add("visible='" + Contest.VISIBLE.open.toString() + "' OR visible='"
				+ Contest.VISIBLE.nondetail.toString() + "'");
		rules.add("conteststatus='" + Contest.STATUS.RUNNING + "'");
		return new ContestDAO().getContestsByRules(rules, "starttime ASC", 0);
	}

	public ArrayList<Contest> getStartingContests() {
		HashSet<String> rules = new HashSet<String>();
		rules.add("vclassid=0");
		rules.add("visible='" + Contest.VISIBLE.open + "' OR visible='" + Contest.VISIBLE.nondetail + "'");
		rules.add("conteststatus='" + Contest.STATUS.STARTING.toString() + "'");
		return new ContestDAO().getContestsByRules(rules, "starttime ASC", 0);
	}

	private TreeSet<String> getStopedRules() {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("vclassid=0");
		rules.add("visible='" + Contest.VISIBLE.open + "' OR visible='" + Contest.VISIBLE.nondetail + "'");
		rules.add("conteststatus='" + Contest.STATUS.STOPPED + "'");
		return rules;
	}

	public ArrayList<Contest> getStopedContests(int page) {
		return new ContestDAO().getContestsByRules(this.getStopedRules(), "starttime DESC", page);
	}

	public int getStopedContestCount() {
		return new ContestDAO().getCountByRules(this.getStopedRules());
	}

	public ArrayList<Contest> getPausingContests() {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("vclassid=0");
		rules.add("visible='" + Contest.VISIBLE.open.toString() + "' OR visible='"
				+ Contest.VISIBLE.nondetail.toString() + "'");
		rules.add("conteststatus='" + Contest.STATUS.PAUSING.toString() + "'");
		return new ContestDAO().getContestsByRules(rules, "starttime DESC", 0);
	}

	/**
	 * 用 owner 取得 contests
	 * 
	 * @param cownerid
	 * @return
	 */
	public ArrayList<Contest> getContestsByOwnerid(int ownerid, int page) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("vclassid=0");
		rules.add("ownerid=" + ownerid);
		rules.add("visible='" + VISIBLE.open + "'");
		ArrayList<Contest> contests = new ContestDAO().getContestsByRules(rules, "id DESC", page);
		return contests;
	}



	/**
	 * 取得本次競賽的 contestants
	 * 
	 * @param contestid
	 * @return
	 */
	public ArrayList<Contestant> getContestantsByContestid(int contestid) {
		return new ContestantDAO().getAllContestants(contestid);
	}

	/**
	 * 透過 vclassid 取得 contests
	 * 
	 * @param vclassid
	 * @param orderby
	 * @param page
	 * @return
	 */
	public ArrayList<Contest> getContestsByVclassid(int vclassid, String orderby, int page) {
		if (orderby == null || "".equals(orderby) || (!orderby.endsWith("DESC") && !orderby.endsWith("ASC"))) {
			orderby = "id DESC";
		}
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("vclassid=" + vclassid);
		rules.add("visible!='" + Contest.VISIBLE.remove + "'"); 
		ArrayList<Contest> contests = new ContestDAO().getContestsByRules(rules, orderby, page);
		return contests;

	}

	/**
	 * 判斷 contestants 內是否有某個 userid
	 * 
	 * @param contestants
	 * @param userid
	 * @return
	 */
	private boolean contains(ArrayList<Contestant> contestants, Integer userid) {
		for (Contestant contestant : contestants) {
			if (contestant.getUserid().intValue() == userid.intValue()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ranking 所需要用的參加者清單。 不分頁
	 * 
	 * @param contestid
	 * @return
	 */
	public ArrayList<Contestant> getContestantsForRanking(Integer contestid) {
		ArrayList<Contestant> contestants = new ContestantDAO().getContestantsForRanking(contestid);
		contestants.addAll(new ContestantDAO().getContestantWithoutSubmit(contestid));

		int prev_rank = 0;
		int curr_rank = 0;
		double prev_score = -1, prev_contestac = -1, prev_penalty = -1, prev_submits = -1;
		int sn = 0;
		for (Contestant contestant : contestants) {
			sn++;
			if (contestant.getScore() == prev_score && contestant.getAc() == prev_contestac
					&& contestant.getPenalty() == prev_penalty && contestant.getSubmits() == prev_submits) {
				curr_rank = prev_rank;
			} else {
				curr_rank = sn;
			}
			prev_score = contestant.getScore();
			prev_contestac = contestant.getAc();
			prev_penalty = contestant.getPenalty();
			prev_submits = contestant.getSubmits();
			prev_rank = curr_rank;
			contestant.setCurr_rank(curr_rank);
		}

		Contest contest = this.getContestById(contestid);
		if (contest.isVContest()) {
			for (VClassStudent student : new VClassStudentDAO().getStudentsByVclassid(contest.getVclassid())) {
				if (!contains(contestants, student.getUserid())) {
					Contestant contestant = new Contestant();
					contestant.setUserid(student.getUserid());
					contestant.setTeamname(student.getUsername());
					contestant.setContestid(contest.getId());
					contestant.setCurr_rank(-1);
					contestants.add(contestant);
				}
			}
		}
		return contestants;
	}

	/**
	 * 在 Init Listener 裡將一些 contest restart
	 */
	public void doInitialized() {
		ContestDAO contestDao = new ContestDAO();
		for (Contest contest : this.getRunningContests()) {
			contest.doRestart();
		}

		for (Contest contest : contestDao.getRestartingContests()) {
			contest.doRestart();
		}
		for (Contest contest : this.getStartingContests()) {
			contest.doStarting();
		}
	}

	public int update(Contest contest) throws DataException {
		this.checkUpdate(contest);

		int result = new ContestDAO().update(contest);
		HashContests.remove(contest.getId());
		HashContests.put(contest.getId(), contest);
		return result;
	}

	public int insert(Contest contest) throws DataException {
		this.checkInsert(contest);

		int id = new ContestDAO().insert(contest);
		contest.setId(id);
		HashContests.put(contest.getId(), contest);
		return id;
	}

	public void checkUpdate(Contest contest) throws DataException {
		this.checkInsert(contest);
	}

	public void checkInsert(Contest contest) throws DataException {
		contest.checkProblemids();
		contest.checkTitle();
		contest.checkScores();
		contest.checkProblemidsScores();
	}

	public Hashtable<Integer, Contest> getHashContests() {
		return HashContests;
	}

	public int getCountByAllContests() {
		return new ContestDAO().getCountByAllContests();
	}

	/**
	 * contest 結束時，把所有 contestant 設定為 finish，並將 user.joinedcontestid 設定為 0
	 * 
	 * @throws AccessException
	 * @throws DataException
	 * 
	 */
	public void doFinishContestants(int contestid) throws DataException {
		Contest contest = this.getContestById(contestid);
		ContestantDAO contestantDAO = new ContestantDAO();
		for (Contestant contestant : new ContestService().getContestantsForRanking(contestid)) {

			if (Contestant.STATUS.online == contestant.getStatus()) {
				contestant.setStatus(Contestant.STATUS.finish);
				contestant.setFinishtime(new Timestamp(new Date().getTime()));
				contestantDAO.update(contestant);
			}

			if (contest.isCheckedConfig(Contest.CONFIG.Team)) { 

			} else {
				User user = new UserService().getUserById(contestant.getUserid());
				user.setJoinedcontestidToNONE();


			}
		}
		new UserService().leaveJoinedContestid(contestid); 
	}

	/**
	 * 讓某 userid 從 某 contest 中交卷 finish
	 * 
	 * @param contest
	 * @param userid
	 */
	public void doFinishByContestUserid(Contest contest, int userid) {
		Contestant contestant = contest.getContestantByUserid(userid);
		contestant.setStatus(Contestant.STATUS.finish);
		contestant.setFinishtime(new Timestamp(System.currentTimeMillis()));
		new ContestantDAO().update(contestant);

		User user = contestant.getUser();
		if (user.getJoinedcontestid().intValue() == contest.getId()) {
			user.setJoinedcontestidToNONE();
		}
		new UserService().update(user);
	}

	/**
	 * 從 Json 匯入 contest
	 * 
	 * 
	 * @param onlineUser
	 * @param file
	 * @param targetContestid
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void ImportContestByJson(OnlineUser onlineUser, File file, int targetVclassid)
			throws JsonParseException, JsonMappingException, IOException, DataException {
		ObjectMapper mapper = new ObjectMapper(); 

		SolutionService solutionService = new SolutionService();

		ContestantDAO contestantDao = new ContestantDAO();
		VClass vclass = new VClassDAO().getVClassById(targetVclassid);

		ExportContest exportContest = mapper.readValue(file, ExportContest.class);

		Contest contest = exportContest.getContest();

		contest.setVclassid(targetVclassid);
		contest.setVisible(VISIBLE.hide);
		contest.setOwnerid(vclass.getOwnerid()); 

		int newcontestid = new ContestService().insert(contest);

		for (Contestant contestant : exportContest.getContestants()) {
			contestant.setContestid(newcontestid);
			try {
				contestantDao.insert(contestant);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int size = exportContest.getSolutions().size();
		for (int i = 0; i < size; i++) {
			Solution solution = exportContest.getSolutions().get(size - 1 - i);
			solution.setContestid(newcontestid);
			solutionService.insert(solution);
		}

		ContestantDAO contestantsDao = new ContestantDAO();
		contestantsDao.rebuiltContestants(newcontestid);

	}

}
