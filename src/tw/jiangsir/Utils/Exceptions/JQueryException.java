/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * nknush-001
 */
package tw.jiangsir.Utils.Exceptions;

import tw.jiangsir.Utils.Exceptions.Alert.TYPE;

/**
 * @author nknush-001 <br/>
 *         專門轉給前端處理的 Exception
 * 
 */
public class JQueryException extends DataException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Alert alert = new Alert();




	public JQueryException(Alert alert) {
		super(alert.getTitle(), alert);
		this.alert = alert;
	}

	public JQueryException(String message) {
		super(message, new Alert(TYPE.EXCEPTION, message, "", "", null, null));
		this.alert = new Alert(TYPE.EXCEPTION, message, "", "", null, null);
	}
	public JQueryException(String message, Alert alert) {
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
