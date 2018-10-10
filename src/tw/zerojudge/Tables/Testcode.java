/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;

/**
 * @author jiangsir
 * 
 */
public class Testcode {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "code")
	private String code = "";
	@Persistent(name = "language")
	private String language = "";
	@Persistent(name = "indata")
	private String indata = "";
	@Persistent(name = "outdata")
	private String outdata = "";
	@Persistent(name = "expected_status")
	private String expected_status = "";
	@Persistent(name = "actual_status")
	private String actual_status = "";
	@Persistent(name = "actual_detail")
	private String actual_detail = "";
	@Persistent(name = "descript")
	private String descript = "";

	public Testcode() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getActual_detail() {
		return actual_detail;
	}

	public void setActual_detail(String actualDetail) {
		actual_detail = actualDetail;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getIndata() {
		return indata;
	}

	public void setIndata(String indata) {
		this.indata = indata;
	}

	public String getOutdata() {
		return outdata;
	}

	public void setOutdata(String outdata) {
		this.outdata = outdata;
	}

	private String getTESTDATA_FILENAME(int solutionid) {
		return "Testcode" + File.separator + "testcode_" + solutionid;
	}

	/**
	 * 實際建立"Testcode"的測資檔,包含輸出及輸入測資。
	 * 
	 * @param index
	 *            第幾個測資檔，由 0 開始
	 * @param infiledata
	 *            輸入檔資料
	 * @param outfiledata
	 *            輸出檔資料
	 * @throws IOException
	 */
	public void doCreateTestfile(int solutionid) throws IOException {
		AppConfig appConfig = ApplicationScope.getAppConfig();

		FileUtils.writeStringToFile(new File(appConfig.getTestdataPath(), getTESTDATA_FILENAME(solutionid) + ".in"),
				getIndata());
		FileUtils.writeStringToFile(new File(appConfig.getTestdataPath(), getTESTDATA_FILENAME(solutionid) + ".out"),
				getOutdata());
	}

}
