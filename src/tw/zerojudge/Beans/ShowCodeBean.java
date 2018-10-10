/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.Beans;

public class ShowCodeBean {

	private Integer solutionid;
	private String session_account;
	private String session_usergroup;
	private String Html_Codelocker;

	public ShowCodeBean() {
	}

	public Integer getSolutionid() {
		return solutionid;
	}

	public void setSolutionid(Integer solutionid) {
		this.solutionid = solutionid;
	}

	public String getHtml_Codelocker() {
		return Html_Codelocker;
	}

	public void setHtml_Codelocker(String htmlCodelocker) {
		Html_Codelocker = htmlCodelocker;
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String session_account) {
		this.session_account = session_account;
	}

	public String getSession_usergroup() {
		return session_usergroup;
	}

	public void setSession_usergroup(String session_usergroup) {
		this.session_usergroup = session_usergroup;
	}

}
