package tw.zerojudge.Api.WebAPI.User.V1;

import java.util.TreeSet;

import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.User;

public class UserAcceptedJson {
	private String version = "V1.0";
	private int userid = 0;
	private String account = "";
	private String email = "";
	private String[] accepted;

	public UserAcceptedJson(User user) {
		this.userid = user.getId();
		this.account = user.getAccount();
		this.email = user.getEmail();
		accepted = new String[user.getAclist().size()];
		TreeSet<Problemid> aclist = user.getAclist();
		int i = 0;
		for (Problemid problemid : aclist) {
			accepted[i] = problemid.getProblemid();
			i++;
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String[] getAccepted() {
		return accepted;
	}

	public void setAccepted(String[] accepted) {
		this.accepted = accepted;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
