/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * nknush-001
 */
package tw.jiangsir.Utils.Exceptions;

import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.Alert.TYPE;

/**
 * @author nknush-001
 * 
 */
public class AlertException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Alert alert = new Alert();




	public AlertException(Alert alert) {
		super(alert.getTitle(), alert);
		this.alert = alert;
	}

	public AlertException(String message) {
		super(message, new Alert(TYPE.DATAERROR, message, "", "", null, null));
		this.alert = new Alert(TYPE.DATAERROR, message, "", "", null, null);
	}

	public AlertException(String message, Alert alert) {
		super(message, alert);
		this.alert = alert;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

}
