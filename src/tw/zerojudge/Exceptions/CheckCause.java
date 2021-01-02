package tw.zerojudge.Exceptions;

import java.util.LinkedHashSet;

/**
 * @author jiangsir
 * 
 */
public class CheckCause extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String debug_message = "";
	private LinkedHashSet<String> checks = new LinkedHashSet<String>();

	public CheckCause(String debug_message, LinkedHashSet<String> checks) {
		this.setDebug_message(debug_message);
		this.setChecks(checks);
	}

	public CheckCause(LinkedHashSet<String> checks) {
		this.setChecks(checks);
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	public String getDebug_message() {
		return debug_message;
	}

	public void setDebug_message(String debugMessage) {
		debug_message = debugMessage;
	}

	public LinkedHashSet<String> getChecks() {
		return checks;
	}

	public void setChecks(LinkedHashSet<String> checks) {
		this.checks = checks;
	}

}
