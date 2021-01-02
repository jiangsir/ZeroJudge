/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Objects;

/**
 * @author jiangsir
 * 
 */
public class Parameter {
	private Integer page = 1;
	private String scripts = "";

	public Parameter() {

	}

	public static String parseFilepath(String filepath) {
		if (filepath != null && !filepath.contains("..")) {
			return filepath;
		}
		return "null";
	}

	public static String parseString(String parameter) {
		if (parameter != null) {
			return parameter;
		}
		return "";
	}

	/**
	 * 非正整數或 null 回傳 -1
	 * 
	 * @param parameter
	 * @return
	 */
	public static Integer parseInteger(String parameter) {
		if (parameter != null && parameter.trim().matches("[0-9]+")) {
			return Integer.valueOf(parameter.trim());
		}
		return -1;
	}

	public static Integer parsePage(String page) {
		if (page != null && page.trim().matches("[0-9]+")) {
			return Integer.valueOf(page.trim());
		}
		return 1;
	}

	public static String parseTabn(String tabn) {
		if (tabn != null && tabn.trim().matches("tab[0-9][0-9]")) {
			return tabn;
		}
		return "tab01";
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(String page) {
		if (page != null && page.trim().matches("[0-9]+")) {
			this.page = Integer.valueOf(page);
		}
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getScripts() {
		return scripts;
	}

	public void setScripts(String scripts) {
		if (scripts != null) {
			this.scripts = scripts;
		}
	}

	/**
	 * 所有網頁上由user 輸入的 text 欄位都應該過濾, 不可含有敏感符號
	 * 
	 * @deprecated
	 * @param text
	 * @return
	 */
	@Deprecated
	public static boolean islegalParameter_Deprecated20110224(String parameter) {
		if (parameter == null || parameter.contains("'")
				|| parameter.contains("\\") || parameter.contains(";")
				|| parameter.contains("--") || parameter.contains("#")) {
			return false;
		}
		return true;
	}

}
