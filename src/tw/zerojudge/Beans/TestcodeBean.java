/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

public class TestcodeBean {
	private String expected_status;
	private String actual_status;

	public TestcodeBean() {
	}

	public String getExpected_status() {
		return expected_status;
	}

	public void setExpected_status(String expectedStatus) {
		expected_status = expectedStatus;
	}

	public String getActual_status() {
		return actual_status;
	}

	public void setActual_status(String actualStatus) {
		actual_status = actualStatus;
	}

	public boolean isAssertEqual() {
		if (this.expected_status.startsWith("!")) {
			return !this.expected_status.substring(1)
					.equals(this.actual_status);
		} else {
			return this.expected_status.equals(this.actual_status);
		}
	}
}
