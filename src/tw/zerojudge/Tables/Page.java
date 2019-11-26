/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import tw.zerojudge.DAOs.GeneralDAO;

/**
 * @author jiangsir
 * 
 */
public class Page {
	private Long id = 0L;
	private Integer userid = 0;
	private String account = "";
	public final int account_SIZE = 50;
	private String method = "";
	public final int mathod_SIZE = 50;
	private String currentpage = "";
	public final int currentpage_SIZE = 255;
	private String querystring = "";
	public final int querystring_SIZE = 65000;
	private String ip = "0.0.0.0";
	public final int ip_SIZE = 20;
	private Date timestamp = new Date();

	public Page() {
	}

	public Page(int id) {
		String sql = "SELECT * FROM pages WHERE id=" + id;
		ArrayList<?> list = new GeneralDAO().executeQuery(sql);
		if (list.size() == 0) {
			return;
		}
		this.init((HashMap<?, ?>) list.get(0));
	}

	public Page(HashMap<?, ?> map) {
		if (map == null) {
			return;
		}
		this.init(map);
	}

	private void init(HashMap<?, ?> map) {
		this.id = (Long) map.get("id");
		this.userid = (Integer) map.get("userid");
		this.account = (String) map.get("account");
		this.method = (String) map.get("method");
		this.currentpage = (String) map.get("currentpage");
		this.querystring = (String) map.get("querystring");
		this.ip = (String) map.get("ip");
		this.timestamp = (Date) map.get("timestamp");
	}

	/** ****************************************************************** */

	public String getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(String currentpage) {
		this.currentpage = currentpage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getQuerystring() {
		return querystring;
	}

	public void setQuerystring(String querystring) {
		this.querystring = querystring == null ? this.querystring : querystring;
		if (this.querystring.length() >= this.querystring_SIZE) {
			this.querystring.substring(0, this.querystring_SIZE);
		}
	}

}
