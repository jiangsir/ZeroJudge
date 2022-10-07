package tw.zerojudge.Tables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassTemplateDAO;
import tw.zerojudge.Objects.Problemid;

/**
 * @author jiangsir
 * 
 */
public class VClassTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7085237790965759477L;
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "name")
	private String name = "";
	@Persistent(name = "title")
	private String title = "";
	@Persistent(name = "ownerid")
	private Integer ownerid = 0;
	@Persistent(name = "classno")
	private Integer classno = 0;
	@Persistent(name = "descript")
	private String descript = "";
	@Persistent(name = "problemids")
	private Set<Problemid> problemids = new LinkedHashSet<Problemid>();
	@Persistent(name = "visible")
	private Integer visible = 1;


	public VClassTemplate() {

	}

	public static Integer parseVClassid(String vclassid) {
		if (vclassid == null || !vclassid.matches("[0-9]+")) {
			return 0;
		}
		return Integer.parseInt(vclassid);
	}

	/**
	 * 判定是否為空物件。
	 * 
	 * @return
	 */
	public boolean getIsNull() {
		if (this.getId().intValue() == 0 && "".equals(this.getProblemids().toString().trim())) {
			return true;
		}
		return false;
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

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			return;
		}
		if ("".equals(name.trim())) {
			throw new DataException("「模板名稱」不可為空字串。");
		}

		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null) {
			return;
		}
		if ("".equals(title.trim())) {
			throw new DataException("「模板標題」不可為空字串。");
		}

		this.title = title;
	}

	public Set<Problemid> getProblemids() {
		return problemids;
	}

	public void setProblemids(Set<Problemid> problemids) {
		for (Problemid problemid : problemids) {
			Problem problem = new ProblemService().getProblemByProblemid(problemid);
			if (problem.getDisplay().ordinal() > Problem.DISPLAY.practice.ordinal()) {
				throw new DataException(
						problemid + " 設定為 " + problem.getDisplay().name() + " 的題目無法作為課程模板題目使用，請設定為「公開題」或「練習題」");
			}
		}
		this.problemids = problemids;
	}

	public void setProblemids(String problemids) {
		if (problemids == null) {
			return;
		}
		this.setProblemids(StringTool.String2LinkedHashSetProblemid(problemids));
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public void setVisible(String visible) {
		if (visible == null || "".equals(visible.trim()) || visible.matches("[0-1]")) {
			return;
		}
		this.visible = Integer.parseInt(visible);
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

	/**
	 * 用 name 取得這個課程模板裡的所有課堂描述、課堂題目
	 * 
	 * @return
	 */
	public ArrayList<VClassTemplate> getTemplatesByName() {
		return new VClassTemplateDAO().getTemplatesByName(this.getName());
	}

}
