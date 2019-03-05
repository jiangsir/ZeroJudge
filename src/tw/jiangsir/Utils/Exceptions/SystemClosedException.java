package tw.jiangsir.Utils.Exceptions;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Factories.UserFactory;

public class SystemClosedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemClosedException(String title, String subtitle) {
		super(title, new Alert(Alert.TYPE.INFO, title, subtitle, "", null, null,
				UserFactory.getNullOnlineUser()));
	}

	public SystemClosedException() {
		super(ApplicationScope.getAppConfig().getSystem_closed_message(),
				new Alert(Alert.TYPE.EXCEPTION,
						ApplicationScope.getAppConfig().getSystem_closed_message(), "", "", null,
						null, UserFactory.getNullOnlineUser()));
	}
}
