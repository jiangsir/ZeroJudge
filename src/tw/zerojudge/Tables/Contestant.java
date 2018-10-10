/**
 * idv.jiangsir.objects - Contest.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.sql.Timestamp;
import java.util.*;

import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.JsonObjects.Compiler;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class Contestant {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "contestid")
	private Integer contestid = 0;
	@Persistent(name = "userid")
	private Integer userid = 0;
	@Persistent(name = "teamname")
	private String teamname = "";
	@Persistent(name = "password")
	private String password = "";
	@Persistent(name = "email")
	private String email = "";
	@Persistent(name = "school")
	private String school = "";
	@Persistent(name = "ipset")
	private List<IpAddress> ipset = new ArrayList<IpAddress>();

	@Persistent(name = "language")
	private String language = "";
	@Persistent(name = "ac")
	private Integer ac = 0;
	@Persistent(name = "aclist")
	private String[] aclist = new String[] {};
	@Persistent(name = "submits")
	private Integer submits = 0;
	@Persistent(name = "penalty")
	private Integer penalty = 0;
	@Persistent(name = "score")
	private Integer score = 0;

	public static enum STATUS {
		online("參賽中"), 
		offline("登出中"), 
		registed("可參加"), 
		finish("已交卷"), 
		leave("已離開"), 
		kicked("已踢出"); 
		private String value = "";

		STATUS(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	@Persistent(name = "status")
	private STATUS status = STATUS.registed;
	@Persistent(name = "finishtime")
	private Timestamp finishtime = new Timestamp(new Date().getTime());


	private int curr_rank;
	ObjectMapper mapper = new ObjectMapper(); 


	public Contestant() {
	}

	//

//	public static final String parseTeamaccount(String teamaccount) {
//		if (teamaccount != null) {
//			return teamaccount;
//		}
//		return new Contestant().getTeamaccount();
//	}

	public static final String parsePassword(String password) {
		if (password != null) {
			return password;
		}
		return new Contestant().getPassword();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	//

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Integer getAc() {
		return ac;
	}

	public void setAc(Integer ac) {
		this.ac = ac;
	}

	public Integer getPenalty() {
		return penalty;
	}

	public void setPenalty(Integer penalty) {
		this.penalty = penalty;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getSubmits() {
		return submits;
	}

	public void setSubmits(Integer submits) {
		this.submits = submits;
	}

	//

	//
	//

	public Integer getContestid() {
		return contestid;
	}

	public List<IpAddress> getIpset() {
		return ipset;
	}

	public void setIpset(List<IpAddress> ipset) {
		this.ipset = ipset;
	}

	public void setIpset(String ipset) {
		if (ipset == null || "".equals(ipset.trim())) {
			return;
		}
		this.setIpset(StringTool.String2IpAddressList(ipset));
	}

	public void setContestid(Integer contestid) {
		this.contestid = contestid;
	}

	public int getCurr_rank() {
		return curr_rank;
	}

	public void setCurr_rank(int currRank) {
		curr_rank = currRank;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		if (language == null) {
			throw new DataException("language 有誤！(null)");
		}
		this.language = language;
	}

	public String[] getAclist() {
		return aclist;
	}

	public void setAclist(String[] aclist) {
		this.aclist = aclist;
	}

	public void setAclist(String aclist) {
		if (aclist == null || aclist.equals("")) {
			Contest contest = new ContestDAO().getContestById(contestid);
			String[] array = new String[contest.getProblemids().size()];
			Arrays.fill(array, "-");
			this.setAclist(array);
			return;
		}
		String[] array = new Utils().String2Array(aclist);
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals("-1") || array[i].equals("") || array[i].equals("null")) {
				array[i] = "-";
			}
		}
		this.setAclist(array);
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public void setStatus(String status) {
		if (status == null) {
			return;
		}
		this.setStatus(STATUS.valueOf(status));
	}

	/**
	 * 參賽者如果 交卷，或被踢出，的狀態，可以由主辦人設定讓他得以重新參賽。
	 * 
	 * @return
	 */
	public boolean getIsRejoin() {
		if (getStatus() == STATUS.finish || getStatus() == STATUS.kicked) {
			return true;
		}
		return false;

	}

	public Timestamp getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Timestamp finishtime) {
		this.finishtime = finishtime;
	}

	public boolean getIsOnline() {
		for (OnlineUser onlineUser : ApplicationScope.getOnlineUsers().values()) {
			if (this.getUserid().intValue() == onlineUser.getId().intValue()) {
				return true;
			}
		}
		return false;
	}

	public User getUser() {
		return new UserService().getUserById(this.getUserid());
	}



}
