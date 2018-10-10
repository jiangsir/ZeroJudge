/**
 * idv.jiangsir.Objects - Classes.java
 * 2009/2/18 下午 08:03:21
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.util.TreeSet;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Objects.Problemid;

/**
 * @author jiangsir
 * 
 */
public class VClassStudent {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "vclassid")
	private Integer vclassid = 0;
	@Persistent(name = "userid")
	private Integer userid = 0;
	@Persistent(name = "account")
	private String account = "";
	@Persistent(name = "vclassaclist")
	private TreeSet<Problemid> vclassaclist = new TreeSet<Problemid>();
	@Persistent(name = "ac")
	private Integer ac = 0;


	public VClassStudent() {
	}

	public Integer getAc() {
		return ac;
	}

	public void setAc(Integer ac) {
		this.ac = ac;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public TreeSet<Problemid> getVclassaclist() {
		return vclassaclist;
	}

	public void setVclassaclist(TreeSet<Problemid> vclassaclist) {
		this.vclassaclist = vclassaclist;
	}

	public void setVclassaclist(String vclassaclist) {
		if (vclassaclist == null) {
			return;
		}
		this.setVclassaclist(StringTool.String2Problemidset(vclassaclist));
	}

	public Integer getVclassid() {
		return vclassid;
	}

	public void setVclassid(Integer vclassid) {
		this.vclassid = vclassid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return new UserService().getUserById(userid).getUsername();
	}

	public String getTruename() {
		return new UserService().getUserById(userid).getTruename();
	}

	public int getJoinedcontestid() {
		return new UserService().getUserById(userid).getJoinedcontestid();
	}

	public boolean getIsOnline() {
		for (OnlineUser onlineUser : ApplicationScope.getOnlineUsers().values()) {
			if (this.getAccount().equals(onlineUser.getAccount())) {
				return true;
			}
		}
		return false;
	}

	public User getUser() {
		return new UserService().getUserById(userid);
	}

}
