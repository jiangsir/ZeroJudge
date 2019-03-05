/**
 * idv.jiangsir.objects - User.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Utils.*;

/**
 * @author jiangsir
 * 
 */
public class Log {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "method")
	private String method = "";
	@Persistent(name = "uri")
	private String uri = "";
	@Persistent(name = "session_account")
	private String session_account = "";
	@Persistent(name = "ipaddr")
	private List<IpAddress> ipaddr = new ArrayList<IpAddress>();

	public static enum TABID {
		INFO, 
		NONE, 
		WARNING, 
		SEVERE, 
		WATCHING, 
		BANNED, 
		GC, 
		MAIL, 
		SLOW, 
		ERRORPAGE, 
		ERRORTOKEN, 
		RUNCOMMAND, 
		ACCESS_EXCEPTION, 
		DATA_EXCEPTION;
	}

	@Persistent(name = "tabid")
	private TABID tabid = Log.TABID.INFO;
	@Persistent(name = "title")
	private String title = "";
	@Persistent(name = "message")
	private String message = "";
	@Persistent(name = "stacktrace")
	private String stacktrace = "";
	@Persistent(name = "timestamp")
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());


	public Log() {

	}

	public Log(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		this.setMethod(request.getMethod());
		String uri = request.getMethod() + " " + request.getRequestURI();
		if (request.getQueryString() != null) {
			uri += "?" + request.getQueryString();
		}
		this.setUri(uri);
		SessionScope sessionScope = new SessionScope(session);
		if (sessionScope.getOnlineUser() != null) {
			String account = sessionScope.getOnlineUser().getAccount();
			this.setSession_account(account);
		}

		this.setIpaddr(request);
		this.setTabid(Log.TABID.WARNING);
	}

	public Log(HttpServletRequest request, Throwable throwable) {
		this(request);
		this.setTabid(Log.TABID.WARNING);
		if (throwable != null) {
			this.setMessage(throwable.getLocalizedMessage());
			this.setStacktrace(new Utils().printStackTrace(throwable));
		}
	}

	public Log(Exception e) {
		this.setTabid(Log.TABID.WARNING);
		this.setMessage(e.getLocalizedMessage());
		this.setStacktrace(new Utils().printStackTrace(e));
	}

	public Log(Class c, Exception e) {
		this.setUri(c.getName());
		this.setTabid(Log.TABID.WARNING);
		this.setMessage(e.getLocalizedMessage());
		this.setStacktrace(new Utils().printStackTrace(e));
	}

	public Log(String title, Class c, Exception e) {
		this.setTitle(title);
		this.setUri(c.getName());
		this.setTabid(Log.TABID.WARNING);
		this.setMessage(e.getLocalizedMessage());
		this.setStacktrace(new Utils().printStackTrace(e));
	}

	/** ******************************************************************** */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		int maxUrl = 30;
		if (uri == null) {
			return;
		}
		this.uri = (uri.length() <= maxUrl) ? uri : uri.substring(0, maxUrl - 1);
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		this.session_account = (sessionAccount == null) ? this.session_account : sessionAccount;
	}


	//
	//
	//
	//

	public List<IpAddress> getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(List<IpAddress> ipaddr) {
		this.ipaddr = ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		if (ipaddr == null || "".equals(ipaddr.trim())) {
			return;
		}
		this.setIpaddr(StringTool.String2IpAddressList(ipaddr));
	}

	public void setIpaddr(HttpServletRequest request) {
		this.setIpaddr(new Utils().getIpList(request));
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		int Maxlength = 255;
		if (message == null) {
			return;
		}
		this.message = (message.length() <= Maxlength) ? message : message.substring(0, Maxlength - 1);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null) {
			return;
		}
		this.title = title;
	}

	public TABID getTabid() {
		return tabid;
	}

	public void setTabid(TABID tabid) {
		this.tabid = tabid;
	}

	public void setTabid(String tabid) {
		if (tabid == null) {
			return;
		}
		this.setTabid(TABID.valueOf(tabid));
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		int maxlength = 65000;
		if (stacktrace == null) {
			return;
		}
		this.stacktrace = (stacktrace.length() <= maxlength) ? stacktrace : stacktrace.substring(0, maxlength - 1);
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 取得所有存在的 tabid
	 * 
	 * @return
	 */
	public TABID[] getTabids() {
		return TABID.values();
	}

}
