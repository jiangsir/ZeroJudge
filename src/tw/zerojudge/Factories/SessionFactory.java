package tw.zerojudge.Factories;

import java.util.Hashtable;
import javax.servlet.http.HttpSession;

public class SessionFactory extends SuperFactory<HttpSession> {
	private final static Hashtable<String, HttpSession> HashSessions = new Hashtable<String, HttpSession>();

	public static void putSession(HttpSession session) {
		if (session == null || session.getId() == null
				|| session.getId().equals("")) {
			return;
		}
		HashSessions.put(session.getId(), session);
	}

	public static HttpSession getSessionById(String sessionid) {
		if (sessionid == null) {
			return null;
		} else if (!HashSessions.containsKey(sessionid)) {
			return null;
		}
		return HashSessions.get(sessionid);
	}

	public static Hashtable<String, HttpSession> getHashSessions() {
		return HashSessions;
	}

	public static void removeSession(String sessionid) {
		synchronized (HashSessions) {
			HashSessions.remove(sessionid);
		}
	}

	public static void removeSession(HttpSession session) {
		SessionFactory.removeSession(session.getId());
	}

}
