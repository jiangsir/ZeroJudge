package tw.zerojudge.Tables;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnore;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.zerojudge.DAOs.UserService;

/**
 * @author jiangsir
 * 
 */

public class Problemlevel {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "pid")
	private Integer pid = 0;
	@Persistent(name = "levelname")
	private String levelname = "APCS"; 
	@Persistent(name = "level")
	private Integer level = 0; 
	@Persistent(name = "userid")
	private Integer userid = 0; 
	@Persistent(name = "updatetime")
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());

	public Problemlevel() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public void setPid(String pid) {
		if (pid == null || !pid.matches("[0-9]+")) {
			return;
		}
		this.setPid(Integer.parseInt(pid));
	}

	public String getLevelname() {
		return levelname;
	}

	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setLevel(String problemlevel) {
		if (problemlevel == null || !problemlevel.matches("[0-9]+")) {
			return;
		}
		this.setLevel(Integer.parseInt(problemlevel));
	}

	public Integer getUserid() {
		return userid;
	}

	@JsonIgnore
	public User getUser() {
		return new UserService().getUserById(this.userid);
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

}
