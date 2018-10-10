/**
 * jiangsir
 */
package tw.zerojudge.JsonObjects;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author jiangsir
 * 
 */
public class Banner {

	private int percent = 100;
	private String content = "";
	/** ******************************************************************** */
	@JsonIgnore
	ObjectMapper mapper = new ObjectMapper(); 

	public Banner() {
	}

	public Banner(String content, int percent) {
		this.setContent(content);
		this.setPercent(percent);
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content == null) {
			return;
		}
		this.content = content;
	}

}
