package tw.zerojudge.Exceptions;

/**
 * @author jiangsir
 * 
 */
public class NullUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NullUserException() {
		super();
	}

	public NullUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NullUserException(Throwable cause) {
		super(cause);
	}

	public NullUserException(String message) {
		super(message);
	}

	public NullUserException(String session_account, String message) {
		super(message);
	}

	public NullUserException(Throwable cause, String session_account) {
		super(cause);
	}
}
