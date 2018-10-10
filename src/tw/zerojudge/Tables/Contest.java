/**
 * idv.jiangsir.objects - Contest.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.ContestFactory;
import tw.zerojudge.Model.ContestCloser;
import tw.zerojudge.Model.ContestStarter;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Contest.Pausepoint;
import tw.zerojudge.Utils.*;

/**
 * @author jiangsir
 * 
 */
public class Contest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1423474422801738775L;

	public static class Pausepoint {
		private Date begintime = new Date(0);
		private Date endtime = new Date(0);

		public Date getBegintime() {
			return begintime;
		}

		public void setBegintime(Date begintime) {
			this.begintime = begintime;
		}

		public Date getEndtime() {
			return endtime;
		}

		public void setEndtime(Date endtime) {
			this.endtime = endtime;
		}

		@JsonIgnore
		public boolean isFinished() {
			if (begintime.getTime() != 0L && endtime.getTime() != 0L && endtime.getTime() >= begintime.getTime()) {
				return true;
			}
			return false;
		}

		@JsonIgnore
		public long getPausetime() {
			if (this.isFinished()) {
				return this.endtime.getTime() - this.begintime.getTime();
			}
			return -1; 
		}
	}

	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "problemids")
	private LinkedHashSet<Problemid> problemids = new LinkedHashSet<Problemid>();
	@Persistent(name = "scores")
	private int[] scores = new int[] {};
	@Persistent(name = "removedsolutionids")
	private TreeSet<Integer> removedsolutionids = new TreeSet<Integer>();
	@Persistent(name = "ownerid")
	private Integer ownerid = 0;
	@Persistent(name = "vclassid")
	private Integer vclassid = 0;
	@Persistent(name = "userrules")
	private LinkedHashSet<String> userrules = new LinkedHashSet<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(".*");
			add("!guest");
		}
	};
	@Persistent(name = "starttime")
	private Timestamp starttime = Timestamp.valueOf("2006-06-08 00:00:00");
	@Persistent(name = "timelimit")
	private Long timelimit = 1L;

	@Persistent(name = "pausepoints")
	public ArrayList<Pausepoint> pausepoints = new ArrayList<Pausepoint>(); 
	@Persistent(name = "title")
	private String title = "";
	@Persistent(name = "subtitle")
	private String subtitle = "";
	@Persistent(name = "taskid")
	private Integer taskid = 0;

	public static enum STATUS {
		SUSPENDING("Contest.Suspending"), 
		STARTING("Contest.Starting"), 
		RUNNING("Contest.Running"), 
		PAUSING("Contest.Pausing"), 
		RESTARTING("Contest.Restarting"), 
		STOPPED("Contest.Stopped"), 
		UNKNOWN("Contest.Unknown"); 
		private String value;

		private STATUS(String value) {
			this.value = value;
		}

		public static STATUS parse(String s) {
			if (s != null) {
				for (STATUS status : STATUS.values()) {
					if (status.toString().equals(s)) {
						return status;
					}
				}
			}
			return UNKNOWN;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getValue() {
			return value;
		}

		public String getName() {
			return name();
		}
	}

	@Persistent(name = "conteststatus")
	private STATUS conteststatus = STATUS.SUSPENDING;
	@Persistent(name = "addonprivilege")
	private String addonprivilege = "";

	public static enum VISIBLE {
		open, 
		hide, 
		nondetail//
	}

	@Persistent(name = "visible")
	private VISIBLE visible = VISIBLE.open;

	public static enum RANKINGMODE {
		CSAPC, //
		NPSC, //
		HSIC; //
	}

	@Persistent(name = "rankingmode")
	private RANKINGMODE rankingmode = RANKINGMODE.HSIC;

	ObjectMapper mapper = new ObjectMapper(); 
	public static final int MAX_CONTEST_DAYS = 31;

	public static enum FreezeLimit {
		zero(0);
		private int value;

		private FreezeLimit(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	@Persistent(name = "freezelimit")
	private Integer freezelimit = FreezeLimit.zero.getValue(); 

	/**
	 * 說明： <br>
	 * 1. 競賽是否自動進行 yes=(1), no=0 <br>
	 * 2. Judge 後 rebuilt 統計數據 yes=(1), no=0<br>
	 * 3. Context 重啟時 rejudge 在 waiting 的解法 yes=(1), no=0<br>
	 * 4. 在 submissions 中顯示 Solution yes=1, no=(0)<br>
	 * 5. 顯示競賽狀態 yes=(1), no=0<br>
	 * 6. 顯示競賽結果 yes=(1), no=0<br>
	 * 7. 使用系統帳號參加。若否則就必須要事先進行報名，管理者必須匯入報名資料 yes=(1), no=0 <br>
	 * 8. 是否讓參賽者看『錯誤訊息』 yes=1, no=(0)<br>
	 * <br>
	 * () 代表預設值<br>
	 */
	public static enum CONFIG {
		AutoRunning, 
		ShowResult, 
		Visible, 
		MultiSubmit, 
		ContestSubmissions, 
		ContestRanking, 
		Team, 
		ShowDetail, 
		ManualJudge, 
		UploadExefile, 
		ShowWA, 
	};

	@Persistent(name = "config")
	private Integer config = Integer.valueOf("00111111110", 2); 

	public Contest() {
	}

	public static Integer parseContestid(String contestid) {
		if (contestid != null && contestid.matches("[0-9]+")) {
			return Integer.valueOf(contestid);
		}
		return new Contest().getId();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null) {
			return;
		} else if ("".equals(title.trim())) {
			throw new DataException("標題不可以為空字串。");
		}
		this.title = title.trim();
	}

	public boolean checkTitle() throws DataException {
		title = title.trim();
		if ("".equals(title)) {
			throw new DataException("標題不可以為空字串。");
		}
		return true;
	}

	public VISIBLE getVisible() {
		return visible;
	}

	public boolean getIsContestVisibleOpen() {
		return this.getVisible() == VISIBLE.open;
	}

	public void setVisible(String visible) {
		if (visible == null || visible.trim().equals("")) {
			return;
		}
		this.setVisible(VISIBLE.valueOf(visible));
	}

	public void setVisible(VISIBLE visible) {
		this.visible = visible;
	}

	public int getFreezelimit() {
		return freezelimit;
	}

	public void setFreezelimit(Integer freezelimit) {
		this.freezelimit = freezelimit;
	}


	public void setFreezelimit(String freezelimit) throws DataException {
		if (freezelimit == null) {
			this.setFreezelimit(30);
			return;
		}
		try {
			this.setFreezelimit(Integer.parseInt(freezelimit));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new DataException("凍結時限(" + freezelimit + ")必須是一個整數。");
		}
	}

	public RANKINGMODE getRankingmode() {
		return rankingmode;
	}

	public void setRankingmode(String rankingmode) {
		if (rankingmode != null && !"".equals(rankingmode)) {
			this.setRankingmode(RANKINGMODE.valueOf(rankingmode));
		}
	}

	public void setRankingmode(RANKINGMODE rankingmode) {
		this.rankingmode = rankingmode;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public STATUS getConteststatus() {
		return conteststatus;
	}

	public STATUS getRUNNING() {
		return STATUS.RUNNING;
	}

	public STATUS getSTARTING() {
		return STATUS.STARTING;
	}

	public STATUS getSTOPPED() {
		return STATUS.STOPPED;
	}

	public STATUS getSUSPENDING() {
		return STATUS.SUSPENDING;
	}

	public STATUS getPAUSING() {
		return STATUS.PAUSING;
	}


	public Integer getId() {
		return id;
	}


	public Integer getOwnerid() {
		return ownerid;
	}

	public User getOwner() {
		return new UserService().getUserById(this.getOwnerid());
	}

	public void setOwnerid(Integer ownerid) {
		this.ownerid = ownerid;
	}

	public Integer getVclassid() {
		return vclassid;
	}

	public void setVclassid(Integer vclassid) {
		this.vclassid = vclassid;
	}

	public LinkedHashSet<Problemid> getProblemids() {
		return problemids;
	}

	public void setProblemids(LinkedHashSet<Problemid> problemids) {
		this.problemids = problemids;
	}


	public void setProblemids(String problemids) {
		if (problemids == null || "".equals(problemids)) {
			throw new DataException("題目列表不可以為空。（problemids=" + problemids + "）");
		}
		LinkedHashSet<Problemid> problemidset = new LinkedHashSet<Problemid>();
		for (Problemid problemid : StringTool.String2LinkedHashSetProblemid(problemids)) {
			problemidset.add(problemid);
		}
		this.setProblemids(problemidset);
	}

	public void setScores(int[] scores) {
		this.scores = scores;
	}

	public boolean checkProblemids() throws DataException {
		if (this.getVclassid() == 0) { 
			if (problemids.size() == 0) {
				throw new DataException("題目列表不可以為空。contestid=" + getId() + ", title=" + getTitle());
			}
		}
		for (Problemid problemid : problemids) {
			if (!new ProblemService().isexitProblemid(problemid)) {
				throw new DataException("題目編號(" + problemid + ")有誤！該題目可能不存在！");
			}
			Problem problem = new ProblemService().getProblemByProblemid(problemid);
			if (this.getOwnerid().intValue() != problem.getOwnerid().intValue()
					&& Problem.DISPLAY.hide == problem.getDisplay()) {
				throw new DataException("他人的題目(" + problemid + ")已隱藏，不能作為競賽題目。");
			}

			if (Problem.DISPLAY.deprecated == problem.getDisplay()) {
				throw new DataException("本題目(" + problem.getProblemid() + ") 已廢棄");
			}
		}
		return true;
	}


	public void setTimelimit(Long timelimit) throws DataException {
		if (timelimit <= 0) {
			throw new DataException("競賽進行時間設定有誤！設定值：" + timelimit);
		} else if (timelimit > 60000L * 60 * 24 * Contest.MAX_CONTEST_DAYS) {
			throw new DataException(
					"目前競賽進行時間上限定義為 " + Contest.MAX_CONTEST_DAYS + " 天(" + Contest.MAX_CONTEST_DAYS * 24 + "小時)，請重新填寫。");
		}
		this.timelimit = timelimit;
	}

	public void setTimelimit(String hours, String mins) {
		if (hours == null || mins == null || !hours.matches("[0-9]+") || !mins.matches("[0-9]+")) {
			return;
		}
		this.setTimelimit(Integer.parseInt(hours.trim()) * 3600000 + Integer.parseInt(mins.trim()) * 60000L);
	}

	public LinkedHashSet<String> getUserrules() {
		return userrules;
	}

	public void setUserrules(LinkedHashSet<String> userrules) {
		this.userrules = userrules;
	}

	public void setUserrules(String userrules) {
		if (userrules == null) {
			return;
		}
		this.setUserrules(StringTool.String2LinkedHashSet(userrules));
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	/**
	 * 依據 starttime 加上 timelimit 加上 pausepoints 的時間計算得出結束時間。
	 * 
	 * @return
	 */
	public Timestamp getStoptime() {
		long time = starttime.getTime() + this.getTimelimit();
		for (Pausepoint pausepoint : this.getPausepoints()) {
			if (pausepoint.isFinished()) {
				time += pausepoint.getEndtime().getTime() - pausepoint.getBegintime().getTime();
			}
		}
		return new Timestamp(time);
	}

	public Integer getTaskid() {
		return taskid;
	}

	public Long getTimelimit() {
		return timelimit;
	}

	public String getPausepoints_HTML() {
		StringBuffer html = new StringBuffer(5000);
		int index = 1;
		for (Pausepoint pausepoint : this.getPausepoints()) {
			int secs = (int) (pausepoint.getPausetime() / 1000);
			int mins = secs / 60;
			int hours = mins / 60;
			html.append("暫停" + index++ + ": " + (hours == 0 ? "" : hours + "h") + (mins == 0 ? "" : mins % 60 + "m")
					+ secs % 60 + "s" + "@" + new Utils().parseDatetime(pausepoint.getBegintime()) + "<br/>");
		}
		return html.toString();
	}

	public ArrayList<Pausepoint> getPausepoints() {
		return pausepoints;
	}

	public void setPausepoints(ArrayList<Pausepoint> pausepoints) {
		this.pausepoints = pausepoints;
	}

	@SuppressWarnings("unchecked")
	public void setPausepoints(String pausepoints) {
		if (pausepoints == null || "".equals(pausepoints.trim())) {
			return;
		}

		try {
			this.setPausepoints(
					(ArrayList<Pausepoint>) mapper.readValue(pausepoints, new TypeReference<ArrayList<Pausepoint>>() {
					}));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTaskid(Integer taskid) {
		if (taskid != null) {
			this.taskid = taskid;
		}
	}

	public void setStarttime(String starttime) {
		if (starttime == null) {
			return;
		}
		try {
			this.setStarttime(Timestamp.valueOf(starttime.trim()));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public void setConteststatus(String conteststatus) {
		if (conteststatus == null) {
			return;
		}
		this.setConteststatus(STATUS.parse(conteststatus));
	}

	public void setConteststatus(STATUS status) {
		this.conteststatus = status;
	}

	/**
	 * 計算一個測驗已經進行的時間。將 pausepoint 的時間扣除。
	 * 
	 * @return
	 */
	public long getRunningtime() {
		long now = System.currentTimeMillis();
		long runningtime = now - this.getStarttime().getTime();
		for (Pausepoint pausepoint : this.getPausepoints()) {
			if (pausepoint.isFinished()) {
				runningtime -= (pausepoint.endtime.getTime() - pausepoint.begintime.getTime());
			} else {
				runningtime -= (now - pausepoint.begintime.getTime());
			}
		}
		return runningtime;
	}

	/**
	 * 計算一個測驗已經進行的時間。不扣除 暫停。
	 * 
	 * @return
	 */
	public long getRunningtimeTotal() {
		return System.currentTimeMillis() - this.getStarttime().getTime();
	}

	/**
	 * 競賽結束, 此時一併解除 user 的 contest 狀態, 回覆正常運作<br>
	 * 因 users 進入 contest 狀態時, 某些能力會被限制
	 * 
	 * @param contestid
	 * @throws AccessException
	 * @throws DataException
	 */
	public void doStop() throws DataException {
		Task task = new TaskDAO().getTaskById(this.getTaskid());
		new TaskService(task).doForcedStop();

		this.setConteststatus(Contest.STATUS.STOPPED);
		new ContestService().update(this);
		if (getVclassid() > 0) {
			VClass vclass = new VClassDAO().getVClassById(this.getVclassid());
			TreeSet<Problemid> problemids = vclass.getProblemids();
			for (Problemid problemid : this.getProblemids()) {
				problemids.add(problemid);
			}
			vclass.setProblemids(problemids);
			new VClassDAO().update(vclass);
		}

		new ContestService().doFinishContestants(this.getId());
	}

	//

	public void setId(Integer id) {
		this.id = id;
	}

	public int[] getScores() {
		return scores;
	}

	public int getTotalScore() {
		int total = 0;
		for (int score : this.getScores()) {
			total += score;
		}
		return total;
	}

	public String getScoresString() {
		String scores = "";
		for (int score : getScores()) {
			scores += scores.equals("") ? score : ", " + score;
		}
		return scores;
	}

	public void setScores(String scores) throws DataException {
		if (scores == null || "".equals(scores)) {
			this.setScores(new int[] {});
		}
		try {
			this.setScores(new Utils().String2IntArray(scores));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new DataException("分數必須為整數！");
		}
	}

	public boolean checkScores() throws DataException {
		if (this.getVclassid() == 0) {
			for (int score : scores) {
				if (score <= 0) {
					throw new DataException("分數必須為大於 0 的正整數。");
				}
			}
		}
		return true;
	}

	public TreeSet<Integer> getRemovedsolutionids() {
		return removedsolutionids;
	}

	public void setRemovedsolutionids(TreeSet<Integer> removedsolutionids) {
		this.removedsolutionids = removedsolutionids;
	}

	public void setRemovedsolutionids(String removedsolutionids) {
		TreeSet<Integer> descSet = new TreeSet<Integer>(new ContestFactory.DESCComparator<Integer>());
		descSet.addAll(StringTool.String2IntegerSet(removedsolutionids));
		this.setRemovedsolutionids(descSet);
	}

	public TreeSet<Integer> getTestJudgeids() {
		return new SolutionDAO().getTestjudgeidsByContestid(this.getId());
	}


	public String getAddonprivilege() {
		return addonprivilege;
	}

	public void setAddonprivilege(String addonprivilege) {
		this.addonprivilege = addonprivilege;
	}

	/**
	 * 加入 index 位置的 config, 不管原本改設定是否存在，都改為 1
	 * 
	 * @param index
	 * @return
	 */
	public void doEnableConfig(CONFIG config) {
		this.setConfig(getConfig().intValue() | (1 << config.ordinal()));
	}

	/**
	 * 移除 index 位置的 config, 不管原本該設定是否存在，都改為 0
	 * 
	 * @param index
	 * @return
	 */
	public void doDisableConfig(CONFIG config) {
		this.setConfig(getConfig().intValue() & (Integer.MAX_VALUE - (1 << config.ordinal())));
	}

	/**
	 * 變更 config[index] 的值， 0 -> 1 or 1 -> 0
	 * 
	 * @param index
	 * @return
	 */
	public void doExchangeConfig(CONFIG config) {
		this.setConfig(getConfig().intValue() ^ (1 << config.ordinal()));
	}

	/**
	 * 指定 config[index] 的值， true OR false
	 * 
	 * @param index
	 * @param value
	 * @return
	 */
	public void doAssignConfig(CONFIG config, boolean value) {
		if (value == true) {
			this.doEnableConfig(config);
		} else {
			this.doDisableConfig(config);
		}
	}

	public Integer getConfig() {
		return config;
	}

	public void setConfig(Integer config) {
		this.config = config;
	}


	/**
	 * 取得 config 的某個 index 的設定
	 * 
	 * @param index
	 * @return
	 */

	/**
	 * 判斷某個 CONFIG 的設定是 true or false
	 * 
	 * @param config
	 * @return
	 */
	public boolean isCheckedConfig(Contest.CONFIG config) {
		int x = getConfig().intValue() & (1 << config.ordinal());
		return x > 0 ? true : false;
	}

	public boolean isCheckedConfig_AutoRunning() {
		return isCheckedConfig(CONFIG.AutoRunning);
	}

	public boolean isCheckedConfig_Exefile() {
		return isCheckedConfig(CONFIG.UploadExefile);
	}

	public boolean isCheckedConfig_Team() {
		return isCheckedConfig(CONFIG.Team);
	}

	public boolean isCheckedConfig_ShowResult() {
		return isCheckedConfig(CONFIG.ShowResult);
	}

	public boolean isCheckedConfig_ShowDetail() {
		return isCheckedConfig(CONFIG.ShowDetail);
	}

	public boolean isCheckedConfig_ShowWA() {
		return isCheckedConfig(CONFIG.ShowWA);
	}

	public boolean isChecked_Config_Visible() {
		return isCheckedConfig(CONFIG.Visible);
	}

	public boolean isCheckedConfig_ContestSubmissions() {
		return this.isCheckedConfig(CONFIG.ContestSubmissions);
	}

	public boolean isCheckedConfig_ContestRanking() {
		return this.isCheckedConfig(CONFIG.ContestRanking);
	}

	public boolean isCheckedConfig_MultiSubmit() {
		return this.isCheckedConfig(CONFIG.MultiSubmit);
	}

	public boolean getIsRunning() {
		return getConteststatus() == Contest.STATUS.RUNNING;
	}

	public boolean getIsStarting() {
		return getConteststatus() == Contest.STATUS.STARTING;
	}

	public boolean getIsSuspending() {
		return getConteststatus() == Contest.STATUS.SUSPENDING;
	}

	public boolean getIsPausing() {
		return getConteststatus() == Contest.STATUS.PAUSING;
	}

	public boolean getIsStopped() {
		return getConteststatus() == Contest.STATUS.STOPPED;
	}



	public int getConfig_AutoRunning() {
		return CONFIG.AutoRunning.ordinal();
	}

	public int getConfig_ShowResult() {
		return CONFIG.ShowResult.ordinal();
	}

	public int getConfig_Visible() {
		return CONFIG.Visible.ordinal();
	}


	public int getConfig_MultiSubmit() {
		return CONFIG.MultiSubmit.ordinal();
	}

	public int getConfig_ContestSubmissions() {
		return CONFIG.ContestSubmissions.ordinal();
	}

	public int getConfig_ContestRanking() {
		return CONFIG.ContestRanking.ordinal();
	}

	public int getConfig_Team() {
		return CONFIG.Team.ordinal();
	}

	public int getConfig_ShowDetail() {
		return CONFIG.ShowDetail.ordinal();
	}

	public int getConfig_ShowWA() {
		return CONFIG.ShowWA.ordinal();
	}

	public int getConfig_ManualJudge() {
		return CONFIG.ManualJudge.ordinal();
	}

	public int getConfig_UploadExefile() {
		return CONFIG.UploadExefile.ordinal();
	}


	/**
	 * 取得這個 Contest 的 Solutions
	 * 
	 * @return
	 */
	public ArrayList<Solution> getSolutions() {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		TreeSet<Integer> solutionids = new ContestService().getSolutionidsByContest(this);
		synchronized (solutionids) {
			solutionids.removeAll(this.getRemovedsolutionids());
		}
		for (Integer solutionid : solutionids) {
			solutions.add(new SolutionService().getSolutionById(solutionid));
		}
		return solutions;
	}


	public Contestant getContestantByUserid(String userid) {
		if (userid == null || !userid.matches("[0-9]+")) {
			throw new DataException("userid 錯誤(" + userid + ")，無法取得參賽者資訊。");
		}
		return getContestantByUserid(Integer.parseInt(userid));
	}

	/**
	 * 取得參賽者
	 * 
	 * @param userid
	 * @return
	 */
	public Contestant getContestantByUserid(int userid) {
		Contestant contestant = new ContestantDAO().getContestantByUserid(userid, this.getId());
		return contestant;
	}

	/**
	 * @deprecated contest 不使用 teamaccount
	 * @param teamaccount
	 * @param passwd
	 * @return
	 */
	private Contestant getContestantByAccountPasswd(String teamaccount, String passwd) {
		return new ContestantDAO().getContestantByAccountPasswd(this, teamaccount, passwd);
	}

	//

	//
	//
	//

	//

	//

	//

	//


	//
	//




	/**
	 * 判斷是否為「隨堂測驗」
	 * 
	 * @return
	 */
	public boolean isVContest() {
		return getVclassid().intValue() > 0;
	}



	/**
	 * @return
	 */
	public int getContestantsCount() {
		return new ContestantDAO().getContestantCountByContestid(this.getId());
	}

	/**
	 * @return
	 */
	public int getJoinedContestantsCount() {
		return new ContestantDAO().getJoinedContestantCountByContestid(this.getId());
	}

	/**
	 */
	public int getOnlineContestantsCount() {
		return new ContestantDAO().getOnlineContestantsCount(getId());
	}

	public int getOfflineContestantsCount() {
		return new ContestantDAO().getOfflineContestantsCount(getId());
	}

	public String getRankingmodeInfo() {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		switch (this.getRankingmode()) {
		case CSAPC:
			return appConfig.getRankingmode_CSAPC();
		case HSIC:
			return appConfig.getRankingmode_HSIC();
		case NPSC:
			return appConfig.getRankingmode_NPSC();
		default:
			return "";
		}
	}

	public boolean isContestProblem(Problemid problemid) {
		return this.getProblemids().contains(problemid);
	}

	public long getCountdown() {
		long countdown = 0;
		if (this.getConteststatus() == STATUS.STARTING) {
			countdown = this.getStarttime().getTime() - new Date().getTime();
		} else if (this.getConteststatus() == STATUS.RUNNING) {
			countdown = this.getTimelimit() - this.getRunningtime();
		}
		return countdown < 0 ? 0 : countdown;
	}



	/**
	 * 取得這個 Contest 的 Solutions
	 * 
	 * @return
	 */
	public ArrayList<Solution> getRemovedSolutions() {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (Integer solutionid : this.getRemovedsolutionids()) {
			solutions.add(new SolutionService().getSolutionById(solutionid));
		}
		return solutions;
	}

	/**
	 * 取得該測驗裡的題目
	 * 
	 * @return
	 */
	public ArrayList<Problem> getProblems() {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		for (Problemid problemid : getProblemids()) {
			problems.add(new ProblemService().getProblemByProblemid(problemid));
		}
		return problems;
	}


	/**
	 * 競賽準備開始，但尚未開始。
	 * 
	 * @throws DataException
	 */
	public void doStarting() throws DataException {
		if (this.getTaskid() > 0) {
			Task task = new TaskDAO().getTaskById(this.getTaskid());
			new TaskService(task).doStop();
		}

		long delay = this.getStarttime().getTime() - new Date().getTime();
		if (delay < 0) {
			throw new DataException("因為您指定了過去的時間，考試將不會啟動!!\n" + "若您是想馬上開始，請選擇\"馬上開始\"");
		}

		ContestStarter contestStarter = new ContestStarter(this, new Date(), delay);
		Thread thread = new Thread(contestStarter);
		thread.start();
	}

	/**
	 * 自動進行的競賽，也就是時間到會自動停止的競賽。<br>
	 * 競賽從頭開始進行，一並把暫停點全部清除。
	 * 
	 */
	public void doRunning() {
		this.setPausepoints(new ArrayList<Pausepoint>());
		this.setConteststatus(Contest.STATUS.RUNNING);
		this.setStarttime(new Timestamp(new Date().getTime()));
		new ContestService().update(this);

		if (getVclassid() > 0) {
		}

		/**
		 * 檢查 contest config 是 autoRunning 才啓動 Closer
		 */
		if (this.isCheckedConfig(Contest.CONFIG.AutoRunning)) {
			ContestCloser contestCloser = new ContestCloser(this, this.getStarttime(), getTimelimit());
			Thread thread = new Thread(contestCloser);
			thread.start();
		}
	}

	/**
	 * 競賽暫停。
	 * 
	 * @throws DataException
	 */
	public void doPause() throws DataException {
		if (this.isCheckedConfig(Contest.CONFIG.AutoRunning)) {
			Task task = new TaskDAO().getTaskById(this.getTaskid());
			new TaskService(task).doStop();
		}

		this.setConteststatus(Contest.STATUS.PAUSING);

		Pausepoint newpausepoint = new Pausepoint();
		newpausepoint.setBegintime(new Date());
		this.getPausepoints().add(newpausepoint);
		new ContestService().update(this);
	}

	/**
	 * 競賽暫停後繼續。
	 * 
	 * @throws DataException
	 */
	public void doResume() throws DataException {
		this.setConteststatus(Contest.STATUS.RUNNING);
		Pausepoint lastPausepoint = this.getPausepoints().get(this.getPausepoints().size() - 1);
		lastPausepoint.setEndtime(new Date());

		new ContestService().update(this);

		if (getVclassid() > 0) {
			VClass vclass = new VClassDAO().getVClassById(this.getVclassid());
			for (Problemid problemid : this.getProblemids()) {
				vclass.getProblemids().add(problemid);
			}
			new VClassDAO().update(vclass);
		}

	}

	/**
	 * 競賽暫停後繼續。
	 * 
	 * @throws DataException
	 */
	public void doResume_OLD() throws DataException {

		long delay = getTimelimit() - this.getRunningtime();
		if (delay < 0) {
			this.setConteststatus(Contest.STATUS.STOPPED);
			new ContestService().update(this);
			throw new DataException("競賽時間已過，將競賽設定為停止。");
		} else {
			this.setConteststatus(Contest.STATUS.RUNNING);
			Pausepoint lastPausepoint = this.getPausepoints().get(this.getPausepoints().size() - 1);
			lastPausepoint.setEndtime(new Date());

			new ContestService().update(this);
		}
		if (getVclassid() > 0) {
			VClass vclass = new VClassDAO().getVClassById(this.getVclassid());
			for (Problemid problemid : this.getProblemids()) {
				vclass.getProblemids().add(problemid);
			}
			new VClassDAO().update(vclass);
		}

		ContestCloser contestCloser = new ContestCloser(this, this.getStarttime(), delay);
		Thread thread = new Thread(contestCloser);
		thread.start();
	}

	/**
	 * 專門處理當系統重新啓動時，仍在 Running 的 Contest 所需要進行的動作<br>
	 * 將 running contest 轉成 restarting
	 * 
	 * @throws DataException
	 */
	public void setRestarting() throws DataException {

		this.setConteststatus(Contest.STATUS.RESTARTING);
		Task task = new TaskDAO().getTaskById(this.getTaskid());
		new TaskService(task).doStop();

		Pausepoint newpausepoint = new Pausepoint();
		newpausepoint.setBegintime(new Date());
		this.getPausepoints().add(newpausepoint);
		new ContestService().update(this);
	}

	/**
	 * 只用於 InitializeListener , 專門處理 restarting contest. <br>
	 * 讓因 系統重啓而進入 restarting 的 contest 再度啓動。
	 * 
	 * @throws DataException
	 */
	public void doRestart() throws DataException {
		if (!this.isCheckedConfig_AutoRunning()) {
			this.setConteststatus(Contest.STATUS.RUNNING);

			if (this.getPausepoints().size() > 0) {
				Pausepoint lastPausepoint = this.getPausepoints().get(this.getPausepoints().size() - 1);
				lastPausepoint.setEndtime(new Date());
			}
			new ContestService().update(this);
			return;
		}

		long delay = getTimelimit() - this.getRunningtime();
		if (delay < 0) {
			this.setConteststatus(Contest.STATUS.STOPPED);
			new ContestService().update(this);
			throw new DataException("競賽時間已過，將競賽設定為停止。");
		} else {
			this.setConteststatus(Contest.STATUS.RUNNING);

			if (this.getPausepoints().size() > 0) {
				Pausepoint lastPausepoint = this.getPausepoints().get(this.getPausepoints().size() - 1);
				lastPausepoint.setEndtime(new Date());
			}
			new ContestService().update(this);
		}

		ContestCloser contestCloser = new ContestCloser(this, this.getStarttime(), delay);
		if (getVclassid() > 0) {
			VClass vclass = new VClassDAO().getVClassById(this.getVclassid());
			for (Problemid problemid : this.getProblemids()) {
				vclass.getProblemids().add(problemid);
			}
			new VClassDAO().update(vclass);
		}
		Thread thread = new Thread(contestCloser);
		thread.start();

	}

	/**
	 * 判斷是否為 某個 contest 的 creater
	 * 
	 * @param contest
	 * @return
	 */
	public boolean getIsOwner(OnlineUser onlineUser) {
		if (this != null && onlineUser != null && !this.isNullContest()
				&& this.getOwnerid().intValue() == onlineUser.getId().intValue()) {
			return true;
		}
		return false;
	}

	public boolean isNullContest() {
		return this.getId() == new Contest().getId();
	}

	@Override
	public String toString() {
		return "[contestid=" + this.getId() + ", title=" + this.getTitle() + "]";
	}

}
