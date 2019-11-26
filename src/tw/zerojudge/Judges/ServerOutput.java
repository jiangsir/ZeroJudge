package tw.zerojudge.Judges;

import java.util.ResourceBundle;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonValue;

import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;

/**
 * @author jiangsir
 * 
 */
public class ServerOutput {
	public static enum REASON {
		DES_ERROR, 
		JsonGenerationException, 
		CANT_SEND_DATA_TO_JUDGESERVER, 
		JUDGESERVER_NOT_FOUND, 
		TESTDATA_NOT_FOUND, 
		SPECIAL_JUDGE_NOT_FOUND, 
		SYSTEMERROR, 
		WRONG_JAVA_CLASS, 
		FORCED_STOP, 
		COMPILE_TOO_LONG, 
		COMPILE_ERROR, 
		SYSTEMERROR_WHEN_COMPILE, 
		LESS_THAN_STANDARD_OUTPUT, 
		MORE_THAN_STANDARD_OUTPUT, 
		ANSWER_NOT_MATCHED, 
		OS_NOT_SUPPORTTED, 
		JSON_PARSE_ERROR, 
		MANUAL_JUDGE, 
		SYSTEMERROR_WHEN_COMPARE, 
		CANT_SYNC_TESTDATA, 
		CONTEST_PREJUDGE_DATA_EXCEED, 
		YouCannotShowOthersErrmsg, 
		OLD_STYLE_SOLUTION_DETAILS, 
		SPECIALJUDGE_COMPILE_ERROR, 
		SPECIALJUDGE_COMPILE_FORCEDSTOP, 
		SPECIALJUDGE_COMPILE_TLE, 
		SPECIALJUDGE_SYSTEMERROR_WHEN_COMPILE, 
		SPECIALJUDGE_EXECUTE_TLE, 
		SPECIALJUDGE_EXECUTE_MLE, 
		AC, 
		TLE, 
		MLE, 
		RE, 
		RF, 
		OLE; 

		//
		//

		@JsonCreator
		public static REASON fromValue(String reasonkey) {
			if (reasonkey.startsWith("Server.REASON")) {
				reasonkey = reasonkey.replace("Server.REASON", "");
				reasonkey = reasonkey.substring(1);
			}
			return REASON.valueOf(reasonkey);
		}

		@Override
		@JsonValue
		public String toString() {
			return "Server.REASON." + this.name();
		}
	}

	public static enum JUDGEMENT {
		Waiting, AC, NA, WA, TLE, MLE, OLE, RE, RF, CE, SE, DN;

		public static JUDGEMENT valueOf(Integer ordinal) {
			return JUDGEMENT.values()[ordinal];
		}
	};

	/**
	 * ServerOutput 不能 extends Throwable 因為無法轉換為 JSON 格式。
	 */
	private String servername = "";
	private String session_account = "";
	private String account = "";
	private Integer solutionid = 0;
	private String problemid = "";
	private JUDGEMENT judgement = JUDGEMENT.Waiting; 
	private String info = "";
	private REASON reason = REASON.SYSTEMERROR;
	private String hint = ""; 
	private String debug = ""; 
	private Long timeusage = 0L; 
	private Integer memoryusage = 0; 
	private Integer exefilesize = 0; 
	private Integer score = 0; 
	private String summary = null; 

	@JsonIgnore
	private OnlineUser onlineUser = UserFactory.getNullOnlineUser();

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		if (sessionAccount == null) {
			return;
		}
		session_account = sessionAccount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		if (account == null) {
			return;
		}
		this.account = account;
	}

	public Integer getSolutionid() {
		return solutionid;
	}

	public void setSolutionid(Integer solutionid) {
		this.solutionid = solutionid;
	}

	public Integer getExefilesize() {
		return exefilesize;
	}

	public JUDGEMENT getJudgement() {
		return judgement;
	}

	@JsonIgnore
	public void setJudgement(JUDGEMENT judgement) {
		this.judgement = judgement;
	}

	public void setJudgement(String judgement) {
		if (judgement == null) {
			return;
		}
		this.setJudgement(JUDGEMENT.valueOf(judgement));
	}

	public String getProblemid() {
		return problemid;
	}


	public void setProblemid(String problemid) {
		if (problemid == null) {
			return;
		}
		this.problemid = problemid;
	}

	/**
	 * judgement 後的簡短說明。只有如下幾個會有資料: WA (line:20), OLE, RE 其餘都直接從 summary 計算
	 * 
	 * @return
	 */
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public REASON getReason() {
		return reason;
	}

	public void setReason(REASON reason) {
		this.reason = reason;
	}

	@JsonIgnore
	public void setReason(String reasonkey) {
		try {
			this.setReason(REASON.fromValue(reasonkey));
		} catch (Exception e) {
			this.setReason(REASON.SYSTEMERROR);
		}
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		int max_length = 3000;
		if (hint.length() <= max_length) {
			this.hint = hint;
		} else {
			hint = hint.substring(0, max_length - 1);
			this.hint = hint + "...訊息太長省略。";
		}
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public void setExefilesize(Integer exefilesize) {
		this.exefilesize = exefilesize;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getTimeusage() {
		return timeusage;
	}

	public void setTimeusage(Long timeusage) {
		this.timeusage = timeusage;
	}

	public Integer getMemoryusage() {
		return memoryusage;
	}

	public void setMemoryusage(Integer memoryusage) {
		this.memoryusage = memoryusage;
	}

	@JsonIgnore
	public OnlineUser getOnlineUser() {
		return onlineUser;
	}

	@JsonIgnore
	public void setOnlineUser(OnlineUser onlineUser) {
		this.onlineUser = onlineUser;
	}

	/**
	 * 存粹提供 JSON 解析用。
	 */
	public void setSummary(String summary) {

	}

	/**
	 * 從 ServerOutput[] 裡面去分析出要呈現出來的統計資訊。如<br>
	 * AC -> 1ms, 1MB<br>
	 * TLE -> 1.5s<br>
	 * MLE -> 128MB<br>
	 * WA -> line:6<br>
	 * RE -> SIGKILL<br>
	 * 顯示單一一個 serveroutput 的訊息。
	 * 
	 * @return
	 */
	public String getSummary() {
		if (summary != null) {
			return summary;
		}
		switch (this.getJudgement()) {
		case AC:
			summary = Solution.parseTimeusage(getTimeusage()) + ", " + Solution.parseMemoryusage(getMemoryusage());
			break;
		case TLE:
			summary = Solution.parseTimeusage(this.getTimeusage());
			break;
		case MLE:
			summary = Solution.parseMemoryusage(this.getMemoryusage());
			break;
		case RE:
			summary = getInfo();
			break;
		case WA:
			summary = getInfo();
			break;
		case Waiting:
			return "";
		default:
			return "";
		}
		return summary;
	}

	@JsonIgnore
	public JUDGEMENT getAC() {
		return JUDGEMENT.AC;
	}

	public boolean getIsWaiting() {
		return this.getJudgement() == JUDGEMENT.Waiting;
	}

	public void setIsWaiting(String isWaiting) {

	}

	public boolean getIsAccept() {
		return this.getJudgement() == JUDGEMENT.AC;
	}

	public void setIsAccept(String isAccept) {

	}

}
