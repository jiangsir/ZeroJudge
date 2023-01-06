package tw.jiangsir.Utils.Exceptions;

import java.util.ArrayList;
import java.util.Date;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Utils.Utils;

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
		super(throwable == null ? "throwable is null!" : throwable.getLocalizedMessage(), new Alert(throwable));
	}

	public AccessException(String title) {
		super(title);
	}

	public AccessException(String title, Throwable throwable) {
		super(new Alert(title, throwable));
	}

	public AccessException(String title, String subtitle, String content, ArrayList<String> debugs) {
		super(title, new Alert(Alert.TYPE.EXCEPTION, title, subtitle, content, null, debugs,
				UserFactory.getNullOnlineUser()));
	}


	/**
	 * @param message
	 */
	public AccessException(OnlineUser onlineUser, String message) {
		super("[" + new Utils().parseDatetime(new Date()) + "] (onlineUser=" + onlineUser + ")" + message);
	}

}
