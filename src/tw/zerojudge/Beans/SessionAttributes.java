package tw.zerojudge.Beans;

import javax.servlet.http.HttpSession;

public class SessionAttributes {
	public enum NAMES {
		sessionid, 
		session_account, 
		session_ip, 
		session_currentpage, 
		session_previouspage, 
		session_locale, 
		session_useragent, //
		session_requestheaders, //
		onlineUser, //
		returnPage, //
		lastsubmission;//
	}

	//
	private HttpSession session = null;


	public SessionAttributes(HttpSession session) {
		this.session = session;
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
	//
	//
	//
	//
	//
	//
	//

}
