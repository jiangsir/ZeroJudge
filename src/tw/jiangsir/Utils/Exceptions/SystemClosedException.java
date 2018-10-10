package tw.jiangsir.Utils.Exceptions;

import tw.jiangsir.Utils.Scopes.ApplicationScope;

public class SystemClosedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemClosedException(String title) {
		super(title, new Alert(Alert.TYPE.INFO, title, "", "", null, null));
	}

	public SystemClosedException() {
		super(ApplicationScope.getAppConfig().getSystem_closed_message(),
				new Alert(Alert.TYPE.EXCEPTION, ApplicationScope.getAppConfig()
						.getSystem_closed_message(), "", "", null, null));
	}
}
