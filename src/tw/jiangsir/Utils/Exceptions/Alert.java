package tw.jiangsir.Utils.Exceptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

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
			ArrayList<String> debugs) {
		this.setType(type);
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setContent(content);
		this.appendUris(uris);
		if (debugs != null && debugs.size() > 0) {
			this.getDebugs().addAll(debugs);
		}
	}

	public Alert(String title, Throwable throwable) {
		this.setType(TYPE.EXCEPTION);
		this.setTitle(title);
		if (throwable != null) {
			this.setStacktrace(throwable.getStackTrace());
		}
	}

	public Alert(Throwable throwable) {
		this(throwable == null ? "" : throwable.getLocalizedMessage(), throwable);
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

	public ArrayList<String> getList() {
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
