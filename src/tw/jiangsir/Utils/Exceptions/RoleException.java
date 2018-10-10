package tw.jiangsir.Utils.Exceptions;

public class RoleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleException(String title) {
		super(title, new Alert(Alert.TYPE.ROLEERROR, title,
				RoleException.class.getSimpleName(), "", null, null));
	}


	public RoleException(Throwable throwable) {
		super(throwable.getLocalizedMessage(), new Alert(throwable));
	}

	public RoleException(Alert alert) {
		super(alert.getTitle(), alert);
	}

}
