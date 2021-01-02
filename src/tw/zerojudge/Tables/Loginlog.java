/**
 * idv.jiangsir.objects - User.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Objects.IpAddress;

/**
 * @author jiangsir
 * 
 */
public class Loginlog {
	@Persistent(name = "id")
	private Long id = 0L;
	@Persistent(name = "userid")
	private int userid = 0;
	@Persistent(name = "useraccount")
	private String useraccount = "";
	@Persistent(name = "ipfrom")
	private List<IpAddress> ipfrom = new ArrayList<IpAddress>();
	@Persistent(name = "ipinfo")
	private String ipinfo = "";
	@Persistent(name = "message")
	private String message = "";
	@Persistent(name = "logintime")
	private Timestamp logintime = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "logouttime")
	private Timestamp logouttime = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "staymin")
	private int staymin = 0;

	/** ******************************************************************** */
	public Loginlog() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public List<IpAddress> getIpfrom() {
		return ipfrom;
	}

	public void setIpfrom(List<IpAddress> ipfrom) {
		this.ipfrom = ipfrom;
	}

	public void setIpfrom(String ipfrom) {
		if (ipfrom == null || "".equals(ipfrom.trim())) {
			return;
		}
		this.setIpfrom(StringTool.String2IpAddressList(ipfrom));
	}

	public String getIpinfo() {
		return ipinfo;
	}

	public void setIpinfo(String ipinfo) {
		this.ipinfo = ipinfo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getLogintime() {
		return logintime;
	}

	public void setLogintime(Timestamp logintime) {
		this.logintime = logintime;
	}

	public Timestamp getLogouttime() {
		return logouttime;
	}

	public void setLogouttime(Timestamp logouttime) {
		this.logouttime = logouttime;
	}

	public int getStaymin() {
		return staymin;
	}

	public void setStaymin(int staymin) {
		this.staymin = staymin;
	}

	public void setStaymin(String staymin) {
		if (staymin == null || !staymin.matches("[0-9]+")) {
			return;
		}
		this.setStaymin(Integer.parseInt(staymin));
	}

}
