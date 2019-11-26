package tw.jiangsir.Utils.Exceptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;

/**
 * @author jiangsir
 * 
 */
public class AccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4800234202163628817L;

	public AccessException(Throwable throwable) {
		super(throwable == null ? "LocalizedMessage為null!" : throwable.getLocalizedMessage(),
				new Alert(throwable));
	}

	public AccessException(String message) {
		super(message);
	}

	public AccessException(String title, String subtitle, String content,
			ArrayList<String> debugs) {
		super(title, new Alert(Alert.TYPE.EXCEPTION, title, subtitle, content, null, debugs,
				UserFactory.getNullOnlineUser()));
	}


	/**
	 * @param message
	 */
	public AccessException(OnlineUser onlineUser, String message) {
		super(message);
	}

}
