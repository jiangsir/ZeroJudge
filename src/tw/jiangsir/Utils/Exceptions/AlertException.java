/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * jiangsir
 */
package tw.jiangsir.Utils.Exceptions;

import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.Alert.TYPE;
import tw.zerojudge.Factories.UserFactory;

/**
 * @author jiangsir
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
		super(message, new Alert(TYPE.DATAERROR, message, "", "", null, null, UserFactory.getNullOnlineUser()));
		this.alert = new Alert(TYPE.DATAERROR, message, "", "", null, null, UserFactory.getNullOnlineUser());
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
