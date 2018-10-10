package tw.jiangsir.Utils.Exceptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jiangsir
 * 
 */
public class DataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataException(String title) {
		super(title, new Alert(Alert.TYPE.DATAERROR, title, DataException.class.getSimpleName(), "", null, null));
	}

	public DataException(String title, ArrayList<String> debugs) {
		super(title, new Alert(Alert.TYPE.DATAERROR, title, DataException.class.getSimpleName(), "", null, debugs));
	}

	public DataException(String title, String subtitle, String content, HashMap<String, URI> uris,
			ArrayList<String> debugs) {
		super(title, new Alert(Alert.TYPE.DATAERROR, title, subtitle, content, uris, debugs));
	}

	public DataException(Throwable throwable) {
		super(throwable.getLocalizedMessage(), new Alert(throwable));
	}

	public DataException(Alert alert) {
		super(alert.getTitle(), alert);
	}

	public DataException(String title, Throwable throwable) {
		super(title, new Alert(title, throwable));
	}

}
