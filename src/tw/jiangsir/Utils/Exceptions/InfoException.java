package tw.jiangsir.Utils.Exceptions;

import tw.zerojudge.Factories.UserFactory;

public class InfoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InfoException(String title, String subtitle, String content) {
		super(title, new Alert(Alert.TYPE.INFO, title, subtitle, content, null, null, UserFactory.getNullOnlineUser()));
	}

	public InfoException(String title) {
		super(title, new Alert(Alert.TYPE.INFO, title, "", "", null, null, UserFactory.getNullOnlineUser()));
	}

}
