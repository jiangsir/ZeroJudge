package tw.jiangsir.Utils.Scopes;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Servlets.ShowSessionsServlet;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Servlets.ShowImageServlet;
import tw.zerojudge.Servlets.Utils.InsertUserServlet;
import tw.zerojudge.Servlets.Utils.LoginServlet;
import tw.zerojudge.Servlets.Utils.LogoutServlet;
import tw.zerojudge.Tables.IMessage;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Utils.Utils;
import tw.zerojudge.Utils.GoogleLogin.GoogleLoginServlet;
import tw.zerojudge.Utils.GoogleLogin.GoogleUser;
import tw.zerojudge.Utils.GoogleLogin.CallbackGoogleLoginServlet;
import tw.zerojudge.Utils.GoogleLogin.UnbindGoogleServlet;

public class SessionScope implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -780144410265117353L;
	private HttpSession session = null;
	private String sessionid = "";
	private ArrayList<IpAddress> session_ipset = new ArrayList<IpAddress>();
	private LinkedHashSet<String> session_privilege = new LinkedHashSet<String>();
	private Locale session_locale = Problem.locale_en_US;
	private String session_useragent = "";
	private HashMap<String, String> session_requestheaders = new HashMap<String, String>();
	private OnlineUser onlineUser = null;
	private GoogleUser googleUser = null;
	private Date lastsubmission = null;
	private long idle = 0;
	private boolean isOnlineUser = false;
	private ArrayList<IMessage> unreadIMessages = new ArrayList<IMessage>();
	private ArrayList<String> returnPages = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7989893466104807011L;

		{
			add("/");
			add("/");
		}
	};
	private String hostURI = "";

	public SessionScope(HttpServletRequest request) {
		this(request.getSession(false));
	}

	@SuppressWarnings("unchecked")
	public SessionScope(HttpSession session) {
		if (session == null) {
			return;
		}

		try {
			session.getCreationTime();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return;
		}

		this.session = session;
		this.setSessionid(session.getId());
		this.setSession_ipset((ArrayList<IpAddress>) session.getAttribute("session_ipset"));
		this.setSession_privilege((LinkedHashSet<String>) session.getAttribute("session_privilege"));
		this.setSession_locale((Locale) session.getAttribute("session_locale"));
		this.setSession_useragent((String) session.getAttribute("session_useragent"));
		this.setSession_requestheaders((HashMap<String, String>) session.getAttribute("session_requestheaders"));
		this.setOnlineUser(session.getAttribute("onlineUser"));
		this.setGoogleUser((GoogleUser) session.getAttribute("googleUser"));
		this.setLastsubmission((Date) session.getAttribute("lastsubmission"));
		this.setUnreadIMessages((ArrayList<IMessage>) session.getAttribute("unreadIMessages"));
		this.setReturnPages(session.getAttribute("returnPages"));
		this.setIdle(System.currentTimeMillis() - session.getLastAccessedTime());
		this.setIsOnlineUser();
		this.setHostURI((String) session.getAttribute("hostURI"));
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		session.setAttribute("sessionid", sessionid);
		this.sessionid = sessionid;
	}

	//

	public void setSession_ipset(String session_ipset) {
		if (session_ipset == null) {
			return;
		}
		this.setSession_ipset(StringTool.String2IpAddressList(session_ipset));
	}

	public ArrayList<IpAddress> getSession_ipset() {
		return session_ipset;
	}

	public void setSession_ipset(ArrayList<IpAddress> session_ipset) {
		if (session_ipset == null) {
			return;
		}
		session.setAttribute("session_ipset", session_ipset);
		this.session_ipset = session_ipset;
	}

	public void setSession_ipset(HttpServletRequest request) {
		if (request == null) {
			return;
		}
		this.setSession_ipset(new Utils().getIpList(request));
	}

	/**
	 * 取得主機的網址。
	 * 
	 * @return
	 */
	public String getHostURI() {
		return this.hostURI;
	}

	public void setHostURI(String hostURI) {
		if (hostURI == null) {
			return;
		}
		session.setAttribute("hostURI", hostURI);
		this.hostURI = hostURI;
	}

	/**
	 * 取得主機的網址。
	 * 
	 */
	public void setHostURI(HttpServletRequest request) {
		String hostURI = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort())
				+ request.getContextPath();
		this.setHostURI(hostURI);
	}

	public LinkedHashSet<String> getSession_privilege() {
		return session_privilege;
	}

	public void setSession_privilege(LinkedHashSet<String> session_privilege) {
		if (session_privilege == null) {
			return;
		}
		session.setAttribute("session_privilege", session_privilege);
		this.session_privilege = session_privilege;
	}

	public Locale getSession_locale() {
		return session_locale;
	}

	public void setSession_locale(Locale session_locale) {
		if (session_locale == null) {
			return;
		}
		session.setAttribute("session_locale", session_locale);
		this.session_locale = session_locale;
	}

	public String getSession_useragent() {
		return session_useragent;
	}

	public void setSession_useragent(String session_useragent) {
		if (session_useragent == null) {
			return;
		}
		session.setAttribute("session_useragent", session_useragent);
		this.session_useragent = session_useragent;
	}

	public HashMap<String, String> getSession_requestheaders() {
		return session_requestheaders;
	}

	public void setSession_requestheaders(HashMap<String, String> session_requestheaders) {
		if (session_requestheaders == null) {
			return;
		}
		session.setAttribute("session_requestheaders", session_requestheaders);
		this.session_requestheaders = session_requestheaders;
	}

	/**
	 * 若不曾登入，回傳 null
	 * 
	 * @return
	 */
	public OnlineUser getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(Object onlineUser) {
		if (onlineUser == null || !(onlineUser instanceof OnlineUser)) {
			return;
		}
		this.removeOnlineUser(); 
		session.setAttribute("onlineUser", onlineUser);
		this.onlineUser = (OnlineUser) onlineUser;
	}

	public GoogleUser getGoogleUser() {
		return googleUser;
	}

	public void setGoogleUser(GoogleUser googleUser) {
		session.setAttribute("googleUser", googleUser);
		this.googleUser = googleUser;
	}

	public void removeOnlineUser() {
		session.removeAttribute("onlineUser");
		this.onlineUser = null;
	}

	public void removeGoogleUser() {
		session.removeAttribute("googleUser");
		this.googleUser = null;
	}

	public Date getLastsubmission() {
		return lastsubmission;
	}

	public void setLastsubmission(Date lastsubmission) {
		if (lastsubmission == null) {
			return;
		}
		session.setAttribute("lastsubmission", lastsubmission);
		this.lastsubmission = lastsubmission;
	}

	public ArrayList<IMessage> getUnreadIMessages() {
		return unreadIMessages;
	}

	public void setUnreadIMessages(ArrayList<IMessage> unreadIMessages) {
		if (unreadIMessages == null) {
			return;
		}
		session.setAttribute("unreadIMessages", unreadIMessages);
		this.unreadIMessages = unreadIMessages;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getReturnPages() {
		if (session.getAttribute("returnPages") != null) {
			this.returnPages = (ArrayList<String>) session.getAttribute("returnPages");
		}
		return this.returnPages;
	}

	@SuppressWarnings("unchecked")
	public void setReturnPages(Object returnPages) {
		if (returnPages == null || !(returnPages instanceof ArrayList)) {
			this.returnPages = new ArrayList<String>();
			this.returnPages.add("/");
			this.returnPages.add("/");
			session.setAttribute("returnPages", this.returnPages);
			return;
		}
		this.returnPages = (ArrayList<String>) returnPages;
		session.setAttribute("returnPages", this.returnPages);
	}

	public void setReturnPage(String servletPath, String querystring) {
		if (servletPath.startsWith(LoginServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith(LogoutServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith(GoogleLoginServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith(UnbindGoogleServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath
						.startsWith(CallbackGoogleLoginServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith(InsertUserServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith(ShowSessionsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith(ShowImageServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])
				|| servletPath.startsWith("/Update") || servletPath.startsWith("/Insert")
				|| servletPath.startsWith("/api/") || servletPath.endsWith(".ajax") || servletPath.endsWith(".api")
				|| servletPath.endsWith(".json")) {
			return;
		}
		ArrayList<String> returnPages = this.getReturnPages();
		String returnPage = servletPath + (querystring == null ? "" : "?" + querystring);
		if (!returnPages.get(0).equals(returnPage)) {
			returnPages.remove(returnPages.size() - 1);
			returnPages.add(0, servletPath + (querystring == null ? "" : "?" + querystring));
			this.setReturnPages(returnPages);
		}
	}

	/**
	 * 回到前一頁
	 * 
	 * @return
	 */
	public String getPreviousPage() {
		return "." + this.getReturnPages().get(1);
	}

	/**
	 * 回到同一頁。
	 * 
	 * @return
	 */
	public String getCurrentPage() {
		return "." + this.getReturnPages().get(0);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(5000);
		//
		buffer.append("sessionid=" + this.getSessionid() + "\n");
		buffer.append("session_ipset=" + this.getSession_ipset() + "\n");
		buffer.append("onlineUser=" + this.getOnlineUser() + "\n");

		return buffer.toString();
	}

	public long getIdle() {
		return idle;
	}

	public int getIdle_min() {
		return (int) idle / 60 / 1000;
	}

	public void setIdle(long idle) {
		this.idle = idle;
	}

	/**
	 * 判斷是否已登入。
	 * 
	 * @return
	 */
	public boolean getIsOnlineUser() {
		return this.isOnlineUser;
	}

	public void setIsOnlineUser() {
		this.isOnlineUser = this.getOnlineUser() != null && !this.getOnlineUser().isNullOnlineUser();
		session.setAttribute("isOnlineUser", this.isOnlineUser);
	}

}
