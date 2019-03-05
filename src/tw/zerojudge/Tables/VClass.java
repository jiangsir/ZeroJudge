/**
 * idv.jiangsir.Objects - Classes.java
 * 2009/2/18 下午 08:03:21
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeSet;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ContestDAO;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Objects.Problemid;

//import tw.zerojudge.Factories.UserFactory;

/**
 * @author jiangsir
 * 
 */
public class VClass implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7085237790965759477L;
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "vclassname")
	private String vclassname = "";
	@Persistent(name = "vclasscode")
	private String vclasscode = "";
	@Persistent(name = "ownerid")
	private Integer ownerid = 0;
	@Persistent(name = "firstclasstime")
	private Timestamp firstclasstime = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "problemids")
	private TreeSet<Problemid> problemids = new TreeSet<Problemid>();
	@Persistent(name = "descript")
	private String descript = "";
	@Persistent(name = "visible")
	private Integer visible = 1;
	public static final int VISIBLE_HIDE = 0;
	public static final int VISIBLE_OPEN = 1;


	public VClass() {

	}

	public static Integer parseVClassid(String vclassid) {
		if (vclassid == null || !vclassid.matches("[0-9]+")) {
			return 0;
		}
		return Integer.parseInt(vclassid);
	}

	public String getVclassname() {
		return vclassname;
	}

	public void setVclassname(String vclassname) {
		if (vclassname == null || "".equals(vclassname.trim())) {
			throw new DataException("課程名稱不可為空！");
		}
		this.vclassname = vclassname.trim();
	}

	/**
	 * 判定是否為空物件。
	 * 
	 * @return
	 */
	public boolean getIsNull() {
		if (this.getId().intValue() == 0 && "".equals(this.getVclasscode().trim())) {
			return true;
		}
		return false;
	}

	public String getVclasscode() {
		return vclasscode;
	}

	public void setVclasscode(String vclasscode) {
		if (vclasscode == null || "".equals(vclasscode.trim())) {
			throw new DataException("vclasscode 不可為空！");
		}
		this.vclasscode = vclasscode.trim();
	}

	public Integer getId() {
		return id;
	}

	public Integer getOwnerid() {
		return ownerid;
	}

	public User getOwner() {
		return new UserService().getUserById(this.getOwnerid());
	}

	public void setOwnerid(Integer ownerid) {
		this.ownerid = ownerid;
	}

	public Timestamp getFirstclasstime() {
		return firstclasstime;
	}

	public void setFirstclasstime(Timestamp firstclasstime) {
		this.firstclasstime = firstclasstime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TreeSet<Problemid> getProblemids() {
		return problemids;
	}

	public void setProblemids(TreeSet<Problemid> problemids) {
		this.problemids = problemids;
	}

	public void setProblemids(String problemids) {
		if (problemids == null) {
			return;
		}
		this.setProblemids(StringTool.String2Problemidset(problemids));
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		if (descript == null || "".equals(descript.trim())) {
			return;
		}
		this.descript = StringTool.escapeScriptStyle(descript);
	}

	public VClassStudent getStudent(int userid) {
		return new VClassStudentDAO().getStudent(this.getId(), userid);
	}

	public ArrayList<VClassStudent> getStudents() {
		return new VClassStudentDAO().getStudentsByVclassid(id);
	}

	public ArrayList<Contest> getContests() {
		return new ContestDAO().getVContests(id);
	}

	public boolean isStudent(int userid) {
		if (new VClassStudentDAO().getStudent(this.getId(), userid) == null) {
			return false;
		}
		return true;
	}

	public boolean getIsOwner(OnlineUser onlineUser) {
		if (onlineUser == null || onlineUser.isNullOnlineUser()) {
			return false;
		}
		if (onlineUser.getIsDEBUGGER()) {
			return true;
		}
		if (onlineUser.getId().intValue() == this.getOwnerid().intValue()) {
			return true;
		}
		return false;
	}

	public ArrayList<Problem> getProblems() {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		for (Problemid problemid : this.getProblemids()) {
			problems.add(new ProblemService().getProblemByProblemid(problemid));
		}
		return problems;
	}

}
