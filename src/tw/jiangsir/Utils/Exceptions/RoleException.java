package tw.jiangsir.Utils.Exceptions;

import tw.zerojudge.Factories.UserFactory;

public class RoleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleException(String title) {
		super(title, new Alert(Alert.TYPE.ROLEERROR, title, RoleException.class.getSimpleName(), "", null, null,
				UserFactory.getNullOnlineUser()));
	}


	public RoleException(Throwable throwable) {
		super(throwable.getLocalizedMessage(), new Alert(throwable));
	}

	public RoleException(Alert alert) {
		super(alert.getTitle(), alert);
	}

}
