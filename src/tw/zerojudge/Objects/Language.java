/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Objects;

import java.io.Serializable;

/**
 * @author jiangsir
 * 
 */
public class Language implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3141220776766054673L;
	private String name = "C++";
	private String suffix = "cpp";

	public Language() {
	}

	public Language(String name, String suffix) {
		this.name = name;
		this.suffix = suffix;
	}

	public Language(String name) {
		this.name = name;
		this.suffix = name.toLowerCase();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
