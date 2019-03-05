package tw.jiangsir.Utils.Exceptions;

import java.net.URI;
import java.util.HashMap;

import tw.zerojudge.Factories.UserFactory;

/**
 * @author jiangsir
 * 
 */
public class DataConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataConnectionException(String title) {
		super(title, new Alert(Alert.TYPE.ERROR, title, DataConnectionException.class.getSimpleName(), "", null, null,
				UserFactory.getNullOnlineUser()));
	}

	public DataConnectionException(String title, String subtitle, String content, HashMap<String, URI> uris) {
		super(title,
				new Alert(Alert.TYPE.ERROR, title, subtitle, content, uris, null, UserFactory.getNullOnlineUser()));
	}

	public DataConnectionException(Throwable throwable) {
		super(throwable.getLocalizedMessage(), new Alert(throwable));
	}

	public DataConnectionException(String title, Throwable throwable) {
		super(title, new Alert(title, throwable));
	}

}
