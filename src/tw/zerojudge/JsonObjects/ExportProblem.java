/**
 * idv.jiangsir.Beans - ArticleTypeBean.java
 * 2009/12/7 下午 08:35:28
 * jiangsir
 */
package tw.zerojudge.JsonObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import tw.zerojudge.Judges.ServerInput;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Problemimage;

public class ExportProblem {
	private String problemid = "";
	private String title = "";
	private double[] timelimits = new double[]{};
	private int memorylimit = 64;
	private String backgrounds = "";
	private Locale locale = Problem.locale_zh_TW;
	private String content = "";
	private String theinput = "";
	private String theoutput = "";
	private String sampleinput = "";
	private String sampleoutput = "";
	private String hint = "";
	private String language = "";
	private String samplecode = "";
	private String comment = "";
	private int testfilelength = 0;
	private int[] scores = new int[]{};
	private ServerInput.MODE judgemode = ServerInput.MODE.Tolerant;
	private Language specialjudge_language = new Language("PYTHON", "python");
	private String specialjudge_code = "";
	private int difficulty = 1;
	private String author = "";
	private String reference = "";
	private String sortable = "";
	private String keywords = "";
	private String inserttime = "";
	private String updatetime = "";
	private int errmsg_visible = 1;
	private String display = "";
	private List<Problemimage> problemimages;
	private ArrayList<String> testinfiles = new ArrayList<String>();
	private ArrayList<String> testoutfiles = new ArrayList<String>();

	public ExportProblem() {
	}

	public String getProblemid() {
		return problemid;
	}

	public void setProblemid(String problemid) {
		this.problemid = problemid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double[] getTimelimits() {
		return timelimits;
	}

	public void setTimelimits(double[] timelimits) {
		this.timelimits = timelimits;
	}

	public int getMemorylimit() {
		return memorylimit;
	}

	public void setMemorylimit(int memorylimit) {
		this.memorylimit = memorylimit;
	}

	public String getBackgrounds() {
		return backgrounds;
	}

	public void setBackgrounds(String backgrounds) {
		this.backgrounds = backgrounds;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTheinput() {
		return theinput;
	}

	public void setTheinput(String theinput) {
		this.theinput = theinput;
	}

	public String getTheoutput() {
		return theoutput;
	}

	public void setTheoutput(String theoutput) {
		this.theoutput = theoutput;
	}

	public String getSampleinput() {
		return sampleinput;
	}

	public void setSampleinput(String sampleinput) {
		this.sampleinput = sampleinput;
	}

	public String getSampleoutput() {
		return sampleoutput;
	}

	public void setSampleoutput(String sampleoutput) {
		this.sampleoutput = sampleoutput;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		if (language == null) {
			return;
		}
		this.language = language;
	}

	public String getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(String samplecode) {
		this.samplecode = samplecode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getTestfilelength() {
		return testfilelength;
	}

	public void setTestfilelength(int testfilelength) {
		this.testfilelength = testfilelength;
	}

	public int[] getScores() {
		return scores;
	}

	public void setScores(int[] scores) {
		this.scores = scores;
	}

	public ServerInput.MODE getJudgemode() {
		return judgemode;
	}

	public void setJudgemode(ServerInput.MODE judgemode) {
		this.judgemode = judgemode;
	}

	public Language getSpecialjudge_language() {
		return specialjudge_language;
	}

	public void setSpecialjudge_language(Language specialjudge_language) {
		this.specialjudge_language = specialjudge_language;
	}

	public String getSpecialjudge_code() {
		return specialjudge_code;
	}

	public void setSpecialjudge_code(String specialjudge_code) {
		this.specialjudge_code = specialjudge_code;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	//
	//
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getSortable() {
		return sortable;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public int getErrmsg_visible() {
		return errmsg_visible;
	}

	public void setErrmsg_visible(int errmsgVisible) {
		errmsg_visible = errmsgVisible;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	//

	public ArrayList<String> getTestinfiles() {
		return testinfiles;
	}

	public void setTestinfiles(ArrayList<String> testinfiles) {
		this.testinfiles = testinfiles;
	}

	public ArrayList<String> getTestoutfiles() {
		return testoutfiles;
	}

	public void setTestoutfiles(ArrayList<String> testoutfiles) {
		this.testoutfiles = testoutfiles;
	}

	public List<Problemimage> getProblemimages() {
		return problemimages;
	}

	public void setProblemimages(List<Problemimage> problemimages) {
		this.problemimages = problemimages;
	}

}
