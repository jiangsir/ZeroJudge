package tw.jiangsir.Utils.Exceptions;

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
		super(throwable.getLocalizedMessage(), new Alert(throwable));
	}

	public AccessException(String message) {
		super(message);
	}


	/**
	 * @param message
	 */
	public AccessException(OnlineUser onlineUser, String message) {
		super(message);
	}

}
