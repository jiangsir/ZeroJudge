/**
 * idv.jiangsir.Objects - Message.java
 * 2008/12/19 下午 01:23:42
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author jiangsir
 * 
 */
public class Message {
	public final static int MessageType_INFOR = 0;
	public final static int MessageType_ALERT = 1;
	public final static int MessageType_ERROR = 2;
	public final static String Resource_SystemInContestMode = "Message.SystemInContestMode";
	public final static String Resource_CannotOpenOthersCode = "Message.CannotOpenOthersCode";
	public final static String Resource_YouCannotShowOthersErrmsg = "Message.YouCannotShowOthersErrmsg";
	public final static String Resource_ContestWithoutDetail = "Message.ContestWithoutDetail";
	public final static String Resource_ContestIsSuspendingCannotView = "Message.ContestIsSuspendingCannotView";
	public final static String Resource_YouArenotTheProblemAuthor = "Message.YouArenotTheProblemAuthor";
	public final static String Resource_YouArenotTheContestCreater = "Message.YouArenotTheContestCreater";
	public final static String Resource_UseridError = "Message.UseridError";
	public final static String Resource_ProblemidError = "Message.ProblemidError";
	public final static String Resource_ContestHidded = "Message.ContestHidded";
	public final static String Resource_RejudgeNotAllow = "Message.RejudgeNotAllow";
	public final static String Resource_MustInTheSameContest = "Message.MustInTheSameContest";
	public final static String Resource_IllegalAccount = "Message.IllegalAccount";
	public final static String Resource_IllegalText = "Message.IllegalText";
	public final static String Resource_NoAccount = "Message.NoAccount";
	public final static String Resource_NoSubject = "Message.NoSubject";
	public final static String Resource_NoContent = "Message.NoContent";
	public final static String Resource_AccountNotExist = "Message.AccountNotExist";
	public final static String Resource_YouAreNotAllowJoinThisContest = "Message.YouAreNotAllowJoinThisContest";
	public final static String Resource_YouAreInContest = "Message.YouAreInContest";
	public final static String Resource_CodeEmpty = "Message.CodeEmpty";
	public final static String Resource_CodeLengthTooLarge = "Message.CodeLengthTooLarge";
	public final static String Resource_YouCannotUpdateOtherUser = "Message.YouCannotUpdateOtherUser";
	public final static String Resource_YouCannotDoThis = "Message.YouCannotDoThis";
	public final static String Resource_Codelock_Second = "CodeLock.Second";
	public final static String Resource_Codelock_Minute = "CodeLock.Minute";
	public final static String Resource_Codelock_Hour = "CodeLock.Hour";
	public final static String Resource_Codelock_Day = "CodeLock.Day";
	public final static String Resource_Codelock_Week = "CodeLock.Week";
	public final static String Resource_Codelock_Month = "CodeLock.Month";
	public final static String Resource_Contest_Countdown = "Contest.Countdown";
	public final static String Resource_Contest_TimeLimit = "Contest.TimeLimit";
	public final static String Resource_Contest_StartTime = "Contest.StartTime";
	public final static String Resource_Contest_StopTime = "Contest.StopTime";
	public final static String Resource_Contest_JoinedPersons = "Contest.JoinedPersons";
	public final static String Resource_Message_Ren = "Message.Ren";
	public final static String Resource_Login_Incorrect = "Login.Incorrect";

	private String session_account = "";
	private String Type = "";
	private String PlainTitle = "";
	private String ResourceTitle = "";
	private String PlainMessage = "";
	private String ResourceMessage = "";
	private String[] ResourceParam = {};
	private String Debug = "";
	private String uri = "";
	private String ipaddress = "";

	private LinkedHashMap<String, String> Links = new LinkedHashMap<String, String>();

	public Message() {
		this.setType(Message.MessageType_INFOR);
	}

	public Message(HttpServletRequest request) {
		this.setType(Message.MessageType_INFOR);
		HttpSession session = request.getSession(false);
		String uri = request.getMethod() + " " + request.getRequestURI();
		if (request.getQueryString() != null) {
			uri += "?" + request.getQueryString();
		}
		this.setUri(uri);
		this.setSession_account((String) session
				.getAttribute("session_account"));
	}

	public Message(HttpServletRequest request, Throwable throwable) {
		this(request);
		this.setType(Message.MessageType_INFOR);
		this.setPlainTitle(throwable.getLocalizedMessage());
	}

	public Message(int type, String plaintitle) {
		this.setType(type);
		this.setPlainTitle(plaintitle);
	}

	public Message(int type, String plaintitle, String PlainMessage) {
		this.setType(type);
		this.setPlainTitle(plaintitle);
		this.setPlainMessage(PlainMessage);
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

	public LinkedHashMap<String, String> getLinks() {
		if (Links.size() == 0) {
			Links.put("javascript:history.back();", "上一頁");
		}
		return Links;
	}

	public void setLinks(String key, String value) {
		Links.put(key, value);
	}

	public String getPlainMessage() {
		return PlainMessage;
	}

	public void setPlainMessage(String plainMessage) {
		PlainMessage = plainMessage;
	}

	public String getPlainTitle() {
		return PlainTitle;
	}

	public void setPlainTitle(String plainTitle) {
		PlainTitle = plainTitle;
	}

	public String getResourceTitle() {
		return ResourceTitle;
	}

	public void setResourceTitle(String resourceTitle) {
		ResourceTitle = resourceTitle;
	}

	public String getType() {
		return Type;
	}

	public void setType(int type) {
		if (type == Message.MessageType_INFOR) {
			Type = "INFOR";
		} else if (type == Message.MessageType_ALERT) {
			Type = "ALERT";
		} else if (type == Message.MessageType_ERROR) {
			Type = "ERROR";
		}
	}

	public String getResourceMessage() {
		return ResourceMessage;
	}

	/**
	 * 可放入以 , 隔開的多個 Resource Message
	 * 
	 * @param resourceMessage
	 */
	public void setResourceMessage(String resourceMessage) {
		ResourceMessage = resourceMessage;
	}

	public String[] getResourceParam() {
		return ResourceParam;
	}

	public void setResourceParam(String[] resourceParam) {
		ResourceParam = resourceParam;
	}

	public static String getPlainText(String resourceText) {
		ResourceBundle resource = ResourceBundle.getBundle("resource");
		return resource.getString(resourceText);
	}

	public String getDebug() {
		return Debug;
	}

	public void setDebug(String debug) {
		Debug = debug;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

}
