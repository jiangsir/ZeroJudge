/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * jiangsir
 */
package tw.jiangsir.Utils.Exceptions;

import tw.jiangsir.Utils.Exceptions.Alert.TYPE;
import tw.zerojudge.Factories.UserFactory;

/**
 * @author jiangsir <br/>
 *         專門轉給 jQuery 前端處理的 ajax.success
 * 
 */
public class JQuerySuccess extends DataException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Alert alert = new Alert();

	public JQuerySuccess(Alert alert) {
		super(alert.getTitle(), alert);
		this.alert = alert;
	}

	public JQuerySuccess(String message) {
		super(message, new Alert(TYPE.EXCEPTION, message, "", "", null, null, UserFactory.getNullOnlineUser()));
		this.alert = new Alert(TYPE.EXCEPTION, message, "", "", null, null, UserFactory.getNullOnlineUser());
	}

	public JQuerySuccess(String message, Alert alert) {
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
