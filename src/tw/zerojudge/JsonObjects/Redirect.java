package tw.zerojudge.JsonObjects;

/**
 * @author jiangsir
 * 
 */
public class Redirect {
	private String uri;

	public Redirect(String uri) {
		this.setUri(uri);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
