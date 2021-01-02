package tw.zerojudge.Exceptions;

import java.util.LinkedHashSet;

/**
 * @author jiangsir
 * 
 */
public class CheckException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4607108503267389163L;

	public CheckException(String message, LinkedHashSet<String> checks) {
		super(message, new CheckCause(checks));
	}

}
