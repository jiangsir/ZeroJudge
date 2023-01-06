package tw.jiangsir.Utils.Exceptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;

public class Alert extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum TYPE {
		INFO, 
		DATAERROR, 
		ROLEERROR, 
		IPERROR, 
		WARNING, 
		EXCEPTION, 
		ERROR 
	};

	@Persistent(name = "type")
	private TYPE type = TYPE.INFO;
	@Persistent(name = "title")
	private String title = "";
	@Persistent(name = "subtitle")
	private String subtitle = "";
	@Persistent(name = "content")
	private String content = "";
	@Persistent(name = "list")
	private ArrayList<String> list = new ArrayList<String>();
	@Persistent(name = "map")
	private HashMap<String, String> map = new HashMap<String, String>();
	@Persistent(name = "stacktrace")
	private StackTraceElement[] stacktrace = new StackTraceElement[] {};
	@Persistent(name = "urls")
	private HashMap<String, URI> uris = new HashMap<String, URI>();
	@Persistent(name = "debugs")
	private HashSet<String> debugs = new HashSet<String>();
	@Persistent(name = "onlineuser")
	private OnlineUser onlineUser = UserFactory.getNullOnlineUser();
	@Persistent(name = "throwable")
	private Throwable throwable = this.getCause();

	public Alert() {
		this.setType(TYPE.EXCEPTION);
		this.setTitle("title");
		this.setSubtitle("subtitle");
		this.setContent("content");
		try {
			this.getUris().put("home", new URI("./"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public Alert(TYPE type, String title, String subtitle, String content, HashMap<String, URI> uris,
			ArrayList<String> debugs, OnlineUser onlineUser) {
		this.setType(type);
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setContent(content);
		this.appendUris(uris);
		if (onlineUser == null)
			onlineUser = UserFactory.getNullOnlineUser();
		if (debugs != null && debugs.size() > 0 && (onlineUser.getIsDEBUGGER() || onlineUser.getIsLoopback())) {
			this.getDebugs().addAll(debugs);
		}
	}

	public Alert(TYPE type, String title, String subtitle, String content, ArrayList<String> list,
			HashMap<String, URI> uris,
			ArrayList<String> debugs, OnlineUser onlineUser) {
		this.setType(type);
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setContent(content + "<br>\n" + this.getOnlineUserInfo());
		this.setList(list);
		this.appendUris(uris);
		if (onlineUser == null)
			onlineUser = UserFactory.getNullOnlineUser();
		if (debugs != null && debugs.size() > 0 && (onlineUser.getIsDEBUGGER() || onlineUser.getIsLoopback())) {
			this.getDebugs().addAll(debugs);
		}
	}

	public Alert(String title, Throwable throwable, OnlineUser onlineUser) {
		this.setType(TYPE.EXCEPTION);
		this.setTitle(title);
		this.setSubtitle(throwable.getClass().getCanonicalName() + ": " + throwable.getLocalizedMessage());
		this.setContent("By [" + this.getOnlineUserInfo() + "]");
		if (throwable != null) {
			this.setThrowable(throwable);
			this.setStacktrace(throwable.getStackTrace());
		}
		this.setOnlineUser(onlineUser);
	}

	public Alert(String title, Throwable throwable) {
		this(title, throwable, UserFactory.getNullOnlineUser());
	}

	public Alert(Throwable throwable, OnlineUser onlineUser) {
		this(throwable == null ? "throwable is null"
				: (throwable.getLocalizedMessage() == null ? throwable.getClass().getName()
						: throwable.getLocalizedMessage()),
				throwable, onlineUser);
	}

	public Alert(Throwable throwable) {
		this(throwable, UserFactory.getNullOnlineUser());
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String getOnlineUserInfo() {
		String userinfo = "None User";
		if (!onlineUser.isNullOnlineUser()) {
			userinfo = onlineUser.getAccount() + "#" + onlineUser.getId() + "(" + onlineUser.getUsername() + ")";
		}
		return userinfo;
	}

	public ArrayList<String> geList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public ArrayList<String> getStackTracelist() {
		ArrayList<String> list = new ArrayList<>();
		if (this.getThrowable() != null) {
			list.add(this.getThrowable().getClass().getCanonicalName() + ": "
					+ this.getThrowable().getLocalizedMessage());
		}
		for (StackTraceElement element : this.getStacktrace()) {
			list.add(element.toString());
		}
		return list;
	}

	public StackTraceElement[] getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(StackTraceElement[] stacktrace) {
		this.stacktrace = stacktrace;
	}

	public HashMap<String, URI> getUris() {
		return uris;
	}

	public void setUris(HashMap<String, URI> uris) {
		this.uris = uris;
	}

	public void appendUris(HashMap<String, URI> uris) {
		if (uris != null && uris.size() > 0) {
			this.getUris().putAll(uris);
		}
	}

	public HashSet<String> getDebugs() {
		return debugs;
	}

	public void setDebugs(HashSet<String> debugs) {
		this.debugs = debugs;
	}

	public OnlineUser getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(OnlineUser onlineUser) {
		this.onlineUser = onlineUser;
	}

}