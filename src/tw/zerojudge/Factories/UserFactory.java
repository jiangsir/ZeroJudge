package tw.zerojudge.Factories;

import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;

public final class UserFactory extends SuperFactory<User> {


	public static User getNullUser() {
		return new User();
	}

	public static OnlineUser getNullOnlineUser() {
		return new OnlineUser();
	}

	/**
	 * 取出當前登入的使用者 sessionUser
	 * 
	 * @param session
	 * @return
	 */
	public static OnlineUser getOnlineUser(HttpSession session) {
		if (session == null) {
			return UserFactory.getNullOnlineUser();
		} else {
			OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
			if (onlineUser != null) {
				return onlineUser;
			} else {
				return UserFactory.getNullOnlineUser();
			}
		}
	}

	public static OnlineUser getOnlineUserBySessionid(String sessionid) {
		HttpSession session = SessionFactory.getSessionById(sessionid);
		return UserFactory.getOnlineUser(session);
	}

	/**
	 * 經由 UserFactory 取得存放在 cache 當中的 user session
	 * 
	 * @param sessionid
	 * @return
	 */
	public static OnlineUser getOnlineUser(String sessionid) {
		if (sessionid == null || "".equals(sessionid)) {
			return UserFactory.getNullOnlineUser();
		}
		return getOnlineUser(SessionFactory.getSessionById(sessionid));
	}

}
