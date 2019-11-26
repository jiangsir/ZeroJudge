package tw.zerojudge.Exceptions;

import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Tables.Log;

/**
 * @author jiangsir
 * 
 */
public class LogsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogsException() {
		super();
	}

	public LogsException(String message, Throwable cause) {
		super(message, cause);
		new LogDAO().insert(new Log(this));
	}

	public LogsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param string
	 */
	public LogsException(String session_account, String string) {
		super(string);
	}

}
