/**
 * idv.jiangsir.objects - Solution.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.JsonObjects.ServerOutputBean;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.JudgeProducer;
import tw.zerojudge.Judges.JudgeServer;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Judges.ServerOutput.JUDGEMENT;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.ManualJudgeServlet;
import tw.zerojudge.Servlets.ShowDetailsServlet;
import tw.zerojudge.Tables.Contest.Pausepoint;
import tw.zerojudge.Utils.DES_2;
import tw.zerojudge.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class Solution {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "userid")
	private Integer userid = 0;
	@Persistent(name = "vclassid")
	private Integer vclassid = 0;
	@Persistent(name = "pid")
	private Integer pid = 0;
	@Persistent(name = "language")
	private Language language;
	@Persistent(name = "timeusage")
	private Long timeusage = -1L;
	@Persistent(name = "memoryusage")
	private Integer memoryusage = -1;
	@Persistent(name = "exefilesize")
	private Integer exefilesize = -1;
	@Persistent(name = "code")
	private String code = "";
	@Persistent(name = "codelength")
	private Integer codelength = 0;
	@Persistent(name = "judgement")
	private ServerOutput.JUDGEMENT judgement = ServerOutput.JUDGEMENT.Waiting;
	public static int CODELOCKER_CLOSE = 1;
	public static int CODELOCKER_OPEN = 0;
	@Persistent(name = "codelocker")
	private Integer codelocker = Solution.CODELOCKER_CLOSE;
	@Persistent(name = "contestid")
	private Integer contestid = 0;
	@Persistent(name = "score")
	private Integer score = 0;
	@Persistent(name = "details")
	private String details = "";
	@Persistent(name = "serveroutputs")
	private ServerOutput[] serveroutputs = new ServerOutput[] {};
	@Persistent(name = "ipfrom")
	private List<IpAddress> ipfrom = new ArrayList<IpAddress>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7261428254835822340L;
		{
			new IpAddress("127.0.0.1");
		}
	};

	//
	@Persistent(name = "submittime")
	private Timestamp submittime = new Timestamp(new java.util.Date().getTime());

	public static enum VISIBLE {
		open(1), //
		incontest(0), 
		testjudge(-1); 
		private int value;

		private VISIBLE(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public static VISIBLE valueOf(int value) {
			return VISIBLE.values()[value + 1];
		}
	}

	@Persistent(name = "visible")
	private VISIBLE visible = Solution.VISIBLE.open;

	/* 以下為非資料庫欄位************ */
	ObjectMapper mapper = new ObjectMapper(); 
	Logger logger = Logger.getAnonymousLogger();

	public Solution() {
	}


	/**
	 * **********************************************************************
	 */

	public ServerOutput.JUDGEMENT getJudgement() {
		return judgement;
	}

	public void setJudgement(ServerOutput.JUDGEMENT judgement) {
		this.judgement = judgement;
	}

	public void setJudgement(Integer judgement) {
		this.setJudgement(ServerOutput.JUDGEMENT.valueOf(judgement));
	}

	//

	public Contest getContest() {
		if (this.getContestid().intValue() <= 0) {
			return new Contest();
		} else {
			return new ContestService().getContestById(contestid);
		}
	}

	public ServerOutput[] getServeroutputs() {

		if (this.getContest() == null) {
			return serveroutputs;
		} else {
			if (!this.getContest().isVContest() && this.getVisible() == VISIBLE.incontest) {
				for (ServerOutput serverOutput : serveroutputs) {
					if (serverOutput.getJudgement() == JUDGEMENT.WA) {
						serverOutput.setHint("");
					}
				}
			}
			return serveroutputs;
		}
	}

	public void setServeroutputs(ServerOutput[] serveroutputs) {
		int length = 0;
		for (ServerOutput serverOutput : serveroutputs) {
			if (serverOutput != null) {
				length++;
			}
		}
		ServerOutput[] outputs = new ServerOutput[length];
		for (int i = 0; i < length; i++) {
			outputs[i] = serveroutputs[i];
		}
		this.serveroutputs = outputs;
	}

	public void setServeroutputs(String serveroutputs) {
		try {
			this.setServeroutputs(mapper.readValue(serveroutputs, ServerOutput[].class));
		} catch (JsonParseException e) {
			logger.info("JsonParseException: " + e.getLocalizedMessage() + ": serverouts=" + serveroutputs);
			this.setServeroutputs(new ServerOutput[] {});
		} catch (JsonMappingException e) {
			logger.info("JsonMappingException: " + e.getLocalizedMessage() + ": serverouts=" + serveroutputs);
			this.setServeroutputs(new ServerOutput[] {});
		} catch (IOException e) {
			logger.info("IOException: " + e.getLocalizedMessage() + ": serverouts=" + serveroutputs);
			this.setServeroutputs(new ServerOutput[] {});
		}
	}

	public Integer getVclassid() {
		return vclassid;
	}

	public void setVclassid(Integer vclassid) {
		this.vclassid = vclassid;
	}

	public String getCode() {
		return code;
	}

	public Integer getContestid() {
		return contestid;
	}

	public Long getTimeusage() {
		return timeusage;
	}

	public Integer getMemoryusage() {
		return memoryusage;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getSubmittime() {
		return submittime;
	}

	public Integer getUserid() {
		return userid;
	}

	public Integer getExefilesize() {
		return exefilesize;
	}

	public void setExefilesize(Integer exefilesize) {
		this.exefilesize = exefilesize;
	}

	public void setCode(String code) throws DataException {
		if (code == null || "".equals(code)) {
			throw new DataException("程式碼不可為空。solutionid=" + this.getId());
		} else if (code.getBytes().length > ApplicationScope.getAppConfig().getMaxCodeLength()) {
			throw new DataException("程式碼太長，超過限制。");
		}
		this.code = StringTool.escapeScriptStyle(code.trim()) + "\n";
	}

	public void setContestid(String contestid) {
		this.contestid = Contest.parseContestid(contestid);
	}

	public void setContestid(Integer contestid) {
		this.contestid = contestid;
	}

	public List<IpAddress> getIpfrom() {
		return ipfrom;
	}

	public void setIpfrom(List<IpAddress> ipfrom) {
		this.ipfrom = ipfrom;
	}

	public void setIpfrom(String ipfrom) {
		if (ipfrom == null || "".equals(ipfrom.trim())) {
			return;
		}
		this.setIpfrom(StringTool.String2IpAddressList(ipfrom));
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setLanguage(String language) throws DataException {
		if (language == null || "".equals(language)) {
			throw new DataException("解題語言選擇不正確!!(language=" + language + ")");
		}
		this.language = new Language(language, language.toLowerCase());
	}

	public void setMemoryusage(Integer memoryusage) {
		this.memoryusage = memoryusage;
	}

	public void setSubmittime(Timestamp submittime) {
		this.submittime = submittime;
	}

	public void setTimeusage(Long timeusage) {
		this.timeusage = timeusage;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score < 0 ? 0 : score;
	}

	public VISIBLE getVisible() {
		return visible;
	}

	public void setVisible(VISIBLE visible) {
		this.visible = visible;
	}

	public void setVisible(Integer visible) {
		this.setVisible(VISIBLE.valueOf(visible));
	}

	public Integer getCodelocker() {
		return codelocker;
	}

	public void setCodelocker(Integer codelocker) {
		this.codelocker = codelocker;
	}

	public Integer getCodelength() {
		return codelength;
	}

	public void setCodelength(Integer codelength) throws DataException {
		this.codelength = codelength;
	}

	public Integer getCODELOCKER_CLOSE() {
		return Solution.CODELOCKER_CLOSE;
	}

	public Integer getCODELOCKER_OPEN() {
		return Solution.CODELOCKER_OPEN;
	}

	/** **************************************************************** */
	//

	/** **************************************************************** */


	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details == null ? this.details : details;
	}

	/**
	 * 代表這個 solution 是花了多少時間才送出的。
	 * 
	 * @return
	 */
	public long getSpending() {
		Contest contest = new ContestService().getContestById(getContestid());
		long submittime = this.getSubmittime().getTime();
		long spending = submittime - contest.getStarttime().getTime();
		for (Pausepoint pausepoint : contest.getPausepoints()) {
			if (pausepoint.isFinished() && pausepoint.getEndtime().getTime() < submittime) {
				spending -= pausepoint.getPausetime();
			}
		}
		return spending;
	}

	public Problem getProblem() {
		return new ProblemService().getProblemByPid(pid);
	}

	public User getUser() {
		return new UserService().getUserById(userid);
	}

	public String getAccount() {
		return new UserService().getUserById(userid).getAccount();
	}

	public Problemid getProblemid() {
		return new ProblemService().getProblemByPid(pid).getProblemid();
	}

	//

	//

	public String getOwnerHtmlStatus() {
		return this.parseOwnerHtmlStatus();
	}

	public String getGuestHtmlStatus() {
		return this.parseGuestHtmlStatus();
	}

	public String parseOwnerHtmlStatus() {
		String html_judgement = this.getJudgement().name();
		switch (this.getJudgement()) {
		case AC:
			html_judgement = "<a href=# id=\"acstyle\">" + this.getJudgement().name() + "</a>";
			break;
		case Waiting:
			html_judgement = this.getJudgement().name();
			break;
		default:
			html_judgement = "<a href=#>" + this.getJudgement().name() + "</a>";
			break;

		}
		return html_judgement;
	}

	public String parseGuestHtmlStatus() {
		String htmlstatus = "";
		switch (this.getJudgement()) {
		case AC:
			htmlstatus = "<span id=\"acstyle\">" + this.getJudgement().name() + "</span>";
			break;
		default:
			htmlstatus = this.getJudgement().name();
			break;

		}
		return htmlstatus;
	}

	/**
	 * 將 timeusage 轉為適當顯示方式
	 * 
	 * @param timeusage
	 * @return
	 */
	public static String parseTimeusage(Long timeusage) {
		return timeusage < 100 ? timeusage + "ms"
				: Problem.DecimalFormat_ProblemTimeLimit.format(timeusage / 1000.0) + "s";
	}

	/**
	 * 將 memoryusage 專為適當顯示方式
	 * 
	 * @param memoryusage
	 * @return
	 */
	public static String parseMemoryusage(Integer memoryusage) {
		return memoryusage < 1024 ? memoryusage + "KB"
				: new DecimalFormat("######.#").format((int) memoryusage / 1024.0) + "MB";
	}



	/**
	 * 從 ServerOutput[] 裡面去分析出要呈現出來的統計資訊。如<br>
	 * AC -> 1ms, 1MB<br>
	 * TLE -> 1.5s<br>
	 * MLE -> 128MB<br>
	 * WA -> line:6<br>
	 * RE -> SIGKILL<br>
	 * NA -> score:20<br>
	 * 要總計所有的 serveroutputs 顯示平均耗時、記憶體用量。
	 * 
	 * @return
	 */
	public String getSummary() {
		String summary = null;
		switch (this.getJudgement()) {
		case AC:
			long timeusage = this.getTimeusage();
			int memoryusage = this.getMemoryusage();
			for (ServerOutput serverOutput : this.getServeroutputs()) {
				timeusage = Math.max(timeusage, serverOutput.getTimeusage());
				memoryusage = Math.max(memoryusage, serverOutput.getMemoryusage());
			}
			summary = parseTimeusage(timeusage) + ", " + parseMemoryusage(memoryusage);
			break;
		case TLE:
			timeusage = this.getTimeusage();
			for (ServerOutput serverOutput : this.getServeroutputs()) {
				timeusage = Math.max(timeusage, serverOutput.getTimeusage());
			}
			summary = parseTimeusage(timeusage);
			break;
		case MLE:
			memoryusage = this.getMemoryusage();

			for (ServerOutput serverOutput : this.getServeroutputs()) {
				memoryusage = Math.max(memoryusage, serverOutput.getMemoryusage());
			}
			summary = parseMemoryusage(memoryusage);
			break;
		case NA:
			int score = 0;

			for (ServerOutput serverOutput : this.getServeroutputs()) {
				score += serverOutput.getScore();
			}
			summary = "score:" + score + "%";
			break;
		case RE:
			for (ServerOutput serverOutput : this.getServeroutputs()) {
				summary = serverOutput.getInfo();
				break; 
			}
			break; 
		case WA:
			for (ServerOutput serverOutput : this.getServeroutputs()) {
				summary = serverOutput.getInfo();
				break; 
			}
			break; 
		case Waiting:
			return "";
		default:
			return "";
		}
		return "(" + summary + ")";
	}

	//


	//

	public boolean isCodelockAccessible_NOTUSE() {
		return false;
	}



	public String getImgCodelocker() {
		String img = "";
		return img;
	}


	public String getQs() {
		String qs = "action=getCode&id=" + this.getId();
		DES_2 des = new DES_2();
		return des.createEncryptor(qs).toString();
	}

	public String getCode_prefix() {
		String servername = "zeroserver";
		for (ServerOutput serverOutput : getServeroutputs()) {
			servername = serverOutput.getServername();
		}
		String BEGIN, END;
		if (getLanguage().equals("PASCAL")) {
			BEGIN = "(*";
			END = "*)";
		} else if (getLanguage().equals("BASIC")) {
			BEGIN = "/'";
			END = "'/";
		} else {
			BEGIN = "/*";
			END = "*/";
		}
		StringBuffer prefix = new StringBuffer(5000);
		int length = 80;
		Problem problem = new ProblemService().getProblemByPid(this.getPid());
		prefix.append(BEGIN + chars(length, '*') + END + "\n");
		String line1 = "  Problem: " + problem.getProblemid() + " \"" + problem.getTitle() + "\"";
		line1 += " from " + problem.getReference();
		prefix.append(BEGIN + line1 + chars(length - line1.getBytes().length, ' ') + END + "\n");
		String line4 = "  Language: " + getLanguage() + " (" + getCode().getBytes().length + " Bytes)";
		prefix.append(BEGIN + line4 + chars(length - line4.getBytes().length, ' ') + END + "\n");
		if (getContestid().intValue() <= 0
				|| new ContestService().getContestById(getContestid()).isCheckedConfig(Contest.CONFIG.ShowResult)) {
			String line3 = "  Result: " + this.getJudgement().name();
			if (!getSummary().equals("")) {
				line3 += getSummary();
			}
			line3 += " judge by " + servername + "@ZeroJudge";
			prefix.append(BEGIN + line3 + chars(length - line3.getBytes().length, ' ') + END + "\n");
		}
		User user = new UserService().getUserById(this.getUserid());
		String line2 = "  Author: " + user.getAccount() + " at " + new Utils().parseDatetime(getSubmittime());
		prefix.append(BEGIN + line2 + chars(length - line2.getBytes().length, ' ') + END + "\n");
		prefix.append(BEGIN + chars(length, '*') + END + "\n\n");
		return prefix.toString();
	}

	private String chars(int n, char c) {
		StringBuffer space = new StringBuffer(5000);
		for (int i = 0; i < n; i++) {
			space.append(c);
		}
		return space.toString();
	}

	public static Integer parseSolutionid(String solutionid) {
		if (solutionid != null && solutionid.matches("[0-9]+")) {
			return Integer.valueOf(solutionid);
		}
		return new Solution().getId();
	}


	/**
	 * 重測
	 * 
	 * @param onlineUser 進行 rejudge 的人
	 */
	public void doRejudge(OnlineUser onlineUser) {

		
		//		if (!onlineUser.isSolutionRejudgable(this)) {
		//			return;
		//		}

		ServerOutput[] serverOutputs;
		JudgeObject judgeObject;
		try {
			ApplicationScope.getAppConfig().getServerConfig();
		} catch (Exception e) {
			serverOutputs = new ServerOutput[1];
			ServerOutput serverOutput = new ServerOutput();
			serverOutput.setSession_account(onlineUser.getAccount());
			serverOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			serverOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			serverOutput.setHint(e.getLocalizedMessage() + "，可能裁判機未正確啟動。");
			serverOutputs[0] = serverOutput;
			new JudgeServer().summaryServerOutput(JudgeObject.PRIORITY.Rejudge, null, this, serverOutputs);
			e.printStackTrace();
			return;
		}

		try {
			judgeObject = new JudgeObject(JudgeObject.PRIORITY.Rejudge, this,
					new ProblemService().getProblemByPid(this.getPid()));
		} catch (Exception e) {
			serverOutputs = new ServerOutput[1];
			ServerOutput serverOutput = new ServerOutput();
			serverOutput.setSession_account(onlineUser.getAccount());
			serverOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			serverOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			serverOutput.setHint("資料有誤，請檢查。" + e.getLocalizedMessage());
			serverOutputs[0] = serverOutput;
			new JudgeServer().summaryServerOutput(JudgeObject.PRIORITY.Rejudge, null, this, serverOutputs);
			e.printStackTrace();
			return;
		}
		JudgeProducer judgeProducer = new JudgeProducer(JudgeFactory.getJudgeServer(), judgeObject);
		Thread judgeProducerThread = new Thread(judgeProducer);
		judgeProducerThread.start();

		this.setJudgement(JUDGEMENT.Waiting);
		new SolutionService().update(this);
	}

	/**
	 * Initialized Listener 專用，因 Initialized Listener 內沒有 Session 訊息。
	 */
	public void doRejudgeByListener() {
		OnlineUser onlineUser = new OnlineUser();
		onlineUser.setAccount("InitializedListener");
		this.doRejudge(onlineUser);
	}

	public ArrayList<ServerOutputBean> getServerOutputBeans() {
		ServerOutput[] serverOutputs = this.getServeroutputs();
		ArrayList<ServerOutputBean> serverOutputBeans = new ArrayList<ServerOutputBean>();
		for (int i = 0; i < serverOutputs.length && serverOutputs[i] != null; i++) {
			serverOutputBeans.add(new ServerOutputBean(serverOutputs[i]));
		}
		return serverOutputBeans;
	}

	public boolean getIsJudgement_AC() {
		return this.getJudgement() == JUDGEMENT.AC;
	}

	public boolean getIsJudgement_Waiting() {
		return this.getJudgement() == JUDGEMENT.Waiting;
	}

	public boolean getIsVisible_Testjudge() {
		return this.getVisible() == VISIBLE.testjudge;
	}

	public boolean getIsVisible_incontest() {
		return this.getVisible() == VISIBLE.incontest;
	}

	public boolean getIsVisible_ManualJudge(OnlineUser onlineUser) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		try {
			new ManualJudgeServlet().AccessFilter(onlineUser, this);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}
}
