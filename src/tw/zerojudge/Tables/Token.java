/**
 * idv.jiangsir.objects - Contest.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.Serializable;
import java.sql.Timestamp;
import tw.jiangsir.Utils.Annotations.Persistent;

/**
 * @author jiangsir
 * 
 */
public class Token implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "base64")
	private String base64 = "";
	@Persistent(name = "userid")
	private Integer userid = 0;
	@Persistent(name = "descript")
	private String descript = "";
	@Persistent(name = "alive")
	private Boolean isdone = false;
	@Persistent(name = "timestamp")
	private Timestamp timestamp = Timestamp.valueOf("2006-06-08 00:00:00");

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Boolean getIsdone() {
		return isdone;
	}

	public void setIsdone(Boolean isdone) {
		this.isdone = isdone;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return this.getBase64() + ": isdone=" + this.getIsdone() + ", descript="
				+ this.getDescript();
	}

}
