package tw.zerojudge.Exceptions;

/**
 * @author jiangsir
 * 
 */
public class JudgeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JudgeException() {
		super();
	}

	public JudgeException(String message, Throwable cause) {
		super(message, cause);
	}

	public JudgeException(Throwable cause) {
		super(cause);
	}

	public JudgeException(String message) {
		super(message);
	}

	public JudgeException(String session_account, String message) {
		super(message);
	}

	public JudgeException(Throwable cause, String session_account) {
		super(cause);
	}
}
