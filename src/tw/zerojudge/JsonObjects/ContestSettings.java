/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.JsonObjects;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import tw.zerojudge.Objects.Problemid;

public class ContestSettings {
	private LinkedHashSet<Problemid> problemids = new LinkedHashSet<Problemid>();
	private int[] scores = new int[] {};
	private LinkedHashSet<String> userrules = new LinkedHashSet<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			add(".*");
			add("!guest");
		}
	};
	private Timestamp starttime = Timestamp.valueOf("2006-06-08 00:00:00");
	private Long timelimit = 1L;
	private String title = "";
	private String subtitle = "";
	private Integer config = Integer.valueOf("00111111110", 2); 
	private Integer sortable = 0;
	private Integer vclasstemplateid = 0;
	private String reserved1 = "";
	private String reserved2 = "";
	private String reserved3 = "";
	private String reserved4 = "";
	private String reserved5 = "";
	private String reserved6 = "";

	public ContestSettings() {
	}

	public LinkedHashSet<Problemid> getProblemids() {
		return problemids;
	}

	public void setProblemids(LinkedHashSet<Problemid> problemids) {
		this.problemids = problemids;
	}

	public int[] getScores() {
		return scores;
	}

	public void setScores(int[] scores) {
		this.scores = scores;
	}

	public LinkedHashSet<String> getUserrules() {
		return userrules;
	}

	public void setUserrules(LinkedHashSet<String> userrules) {
		this.userrules = userrules;
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public Long getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(Long timelimit) {
		this.timelimit = timelimit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Integer getConfig() {
		return config;
	}

	public void setConfig(Integer config) {
		this.config = config;
	}

	public Integer getSortable() {
		return sortable;
	}

	public void setSortable(Integer sortable) {
		this.sortable = sortable;
	}

	public Integer getVclasstemplateid() {
		return vclasstemplateid;
	}

	public void setVclasstemplateid(Integer vclasstemplateid) {
		this.vclasstemplateid = vclasstemplateid;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	public String getReserved5() {
		return reserved5;
	}

	public void setReserved5(String reserved5) {
		this.reserved5 = reserved5;
	}

	public String getReserved6() {
		return reserved6;
	}

	public void setReserved6(String reserved6) {
		this.reserved6 = reserved6;
	}

}
