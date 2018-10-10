/**
 * idv.jiangsir.objects - Contest.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.JsonObjects.ServerOutputBean;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.ServerInput;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Judges.ServerOutput.JUDGEMENT;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Objects.TestdataPair;
import tw.zerojudge.Objects.Testdatafile;
import tw.zerojudge.Objects.Testdatafile.SUFFIX;
import tw.zerojudge.Servlets.ShowImageServlet;
import tw.zerojudge.Utils.Uploader;
import tw.zerojudge.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class Problem {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "problemid")
	private Problemid problemid = new Problemid("");
	@Persistent(name = "title")
	private String title = "Unfinished!";
	@Persistent(name = "testfilelength")
	private Integer testfilelength = 1;
	@Persistent(name = "timelimits")
	private double[] timelimits = new double[]{1.0}; 
	@Persistent(name = "memorylimit")
	private Integer memorylimit = 64; 
	@Persistent(name = "backgrounds")
	private TreeSet<String> backgrounds = new TreeSet<String>();
	public static final Locale locale_zh_TW = Locale.TAIWAN;
	public static final Locale locale_zh_CN = Locale.CHINA;
	public static final Locale locale_en_US = Locale.ENGLISH;
	@Persistent(name = "locale")
	private Locale locale = locale_zh_TW;
	@Persistent(name = "content")
	private String content = "";
	@Persistent(name = "theinput")
	private String theinput = "";
	@Persistent(name = "theoutput")
	private String theoutput = "";
	@Persistent(name = "sampleinput")
	private String sampleinput = "";
	@Persistent(name = "sampleoutput")
	private String sampleoutput = "";
	@Persistent(name = "hint")
	private String hint = "";
	@Persistent(name = "language")
	private Language language = null;
	@Persistent(name = "samplecode")
	private String samplecode = "";
	@Persistent(name = "prejudgement")
	private ServerOutput.JUDGEMENT prejudgement = ServerOutput.JUDGEMENT.Waiting;
	@Persistent(name = "serveroutputs")
	private ServerOutput[] serveroutputs = new ServerOutput[]{};
	@Persistent(name = "comment")
	private String comment = "";
	@Persistent(name = "scores")
	private int[] scores = new int[]{};
	@Persistent(name = "alteroutdata")
	private String alteroutdata = "";
	@Persistent(name = "judgemode")
	private ServerInput.MODE judgemode = ServerInput.MODE.Tolerant;

	@Persistent(name = "difficulty")
	private Integer difficulty = 3;
	@Persistent(name = "acnum")
	private Integer acnum = 0;
	@Persistent(name = "submitnum")
	private Long submitnum = 0L;
	@Persistent(name = "clicknum")
	private Long clicknum = 0L;
	@Persistent(name = "acusers")
	private Integer acusers = 0;
	@Persistent(name = "submitusers")
	private Integer submitusers = 0;
	@Persistent(name = "lastsolutionid")
	private Integer lastsolutionid = 0;
	@Persistent(name = "ownerid")
	private Integer ownerid = 0;
	@Persistent(name = "reference")
	private LinkedHashSet<String> reference = new LinkedHashSet<String>();
	@Persistent(name = "sortable")
	private String sortable = "";
	@Persistent(name = "keywords")
	private TreeSet<String> keywords = new TreeSet<String>();
	@Persistent(name = "inserttime")
	private Timestamp inserttime = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "updatetime")
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "tabid")
	private String tabid = ApplicationScope.getAppConfig().getProblemtabs().get(0).getId();
	public static final String TAB_MYPROBLEM = "MYPROBLEM";
	public static final String TAB_NOTOPEN = "NOTOPEN";
	public static final String TAB_VERIFYING = "VERIFYING";
	public static final String TAB_SPECIAL = "SPECIAL";

	public static enum EDIT_TABS {
		MYPROBLEM, //
		NOTOPEN, //
		VERIFYING, //
		SPECIAL;//
	}

	public static final int WA_visible_OPEN = 1;
	public static final int WA_visible_HIDE = 0;
	@Persistent(name = "wa_visible")
	private Integer wa_visible = WA_visible_OPEN;


	public static enum DISPLAY {
		open, 
		verifying, 
		contest, 
		practice, 
		hide, 
		deprecated, 
		unfinished; 

	}

	@Persistent(name = "display")
	private DISPLAY display = DISPLAY.unfinished;

	@Persistent(name = "reserved_text1")
	private String specialjudge_code = "";
	@Persistent(name = "reserved_text2")
	private Language specialjudge_language = new Language("PYTHON", "python");
	@Persistent(name = "reserved_text3")
	private String reserved_text3 = "reserved";
	@Persistent(name = "reserved_text4")
	private String reserved_text4 = "reserved";
	@Persistent(name = "reserved_text5")
	private String reserved_text5 = "reserved";
	@Persistent(name = "reserved_text6")
	private String reserved_text6 = "reserved";

	public static DecimalFormat DecimalFormat_ProblemTimeLimit = new DecimalFormat("####.#");
	public static final double MAX_DATAPAIRS = 30; 
	public static final double MIN_TIMELIMIT = 0.1; 
	public static final double MAX_TIMELIMIT = 10; 
	public static final double MAX_SINGLE_TIMELIMIT = 30; 
	public static final double MAX_TOTAL_TIMELIMIT = 100; 
	public static final int MAX_SCORE = 100; 
	public static final int MIN_SCORE = 1; 
	public static final int MIN_MEMORYLIMIT = 4; 
	public static final int MAX_MEMORYLIMIT = 512; 
	public static final int MAX_FILESIZE = Uploader.MAX_FILESIZE;

	Logger logger = Logger.getAnonymousLogger();

	public enum TESTFILE_POSTFIX {
		in, 
		out;
	}

	ObjectMapper mapper = new ObjectMapper(); 

	public Problem() {
	}




	/**
	 * 統一辨識 Problemid 格式
	 * 
	 * @return
	 * @throws DataException
	 */


	/*
	 * 20090505 以 hash problem 取代 public static Integer hashPid(String
	 * problemid) { if (ProblemsDAO.ProblemPids.get(problemid) == null) {
	 * ProblemsDAO.ProblemPids.put(problemid, new Problem(problemid) .getPid());
	 * } return ProblemsDAO.ProblemPids.get(problemid); }
	 * 
	 * public static String hashTitle(String problemid) { if
	 * (ProblemsDAO.ProblemTitles.get(problemid) == null) {
	 * ProblemsDAO.ProblemTitles.put(problemid, new Problem(problemid)
	 * .getTitle()); } return ProblemsDAO.ProblemTitles.get(problemid); }
	 * 
	 * public static String hashAuthor(String problemid) { if
	 * (ProblemsDAO.ProblemAuthors.get(problemid) == null) {
	 * ProblemsDAO.ProblemAuthors.put(problemid, new Problem(problemid)
	 * .getAuthor()); } return ProblemsDAO.ProblemAuthors.get(problemid); }
	 */

	public Integer getAcnum() {
		return acnum;
	}

	public Integer getAcusers() {
		return acusers;
	}

	public String getAlteroutdata() {
		return alteroutdata;
	}

	//

	public Integer getOwnerid() {
		return ownerid;
	}

	public User getOwner() {
		return new UserService().getUserById(this.getOwnerid());
	}

	public void setOwnerid(Integer ownerid) {
		this.ownerid = ownerid;
	}

	public TreeSet<String> getBackgrounds() {
		return backgrounds;
	}

	public Long getClicknum() {
		return clicknum;
	}

	public String getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(String samplecode) {
		if (samplecode == null) {
			return;
		}
		this.samplecode = samplecode;
	}

	public String getContent() {
		return content;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public DISPLAY getDisplay() {
		return display;
	}

	public void setDisplay(DISPLAY display) {
		this.display = display;
	}

	public void setDisplay(String display) {
		if (display == null) {
			this.setDisplay(DISPLAY.hide);
			return;
		}
		this.setDisplay(DISPLAY.valueOf(display));
	}

	public String getHint() {
		return hint;
	}

	//

	public Timestamp getInserttime() {
		return inserttime;
	}

	public Integer getMemorylimit() {
		return memorylimit;
	}


	public Integer getId() {
		return id;
	}

	public Problemid getProblemid() {
		return problemid;
	}

	public LinkedHashSet<String> getReference() {
		return reference;
	}

	public String getSampleinput() {
		return sampleinput;
	}

	public String getSampleoutput() {
		return sampleoutput;
	}

	public Long getSubmitnum() {
		return submitnum;
	}

	public Integer getSubmitusers() {
		return submitusers;
	}

	public Integer getLastsolutionid() {
		return lastsolutionid;
	}

	public void setLastsolutionid(Integer lastsolutionid) {
		this.lastsolutionid = lastsolutionid;
	}

	public String getTheinput() {
		return theinput;
	}

	public String getTheoutput() {
		return theoutput;
	}

	public String getTitle() {
		return title;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public String getComment() {
		return comment;
	}

	public int[] getScores() {
		return scores;
	}

	/**
	 * 配分有 0 分或總分 != MAX_SCORE 的話，進行 autoScoring
	 * 
	 * @param scores
	 */
	public void setScores(int[] scores) {
		int total = 0;
		for (int score : scores) {
			if (score == 0) {
				this.scores = this.autoScoring(scores.length);
				return;
			}
			total += score;
		}
		if (total != Problem.MAX_SCORE) {
			this.scores = this.autoScoring(scores.length);
		} else {
			this.scores = scores;
		}
	}

	public void checkScores(String[] scores) throws DataException {
		int totalscore = 0;
		for (String score : scores) {
			if (!score.trim().matches("[0-9]+")) {
				throw new DataException("(" + problemid + ") 各測資配分必須為整數");
			}
			if (Integer.parseInt(score.trim()) < Problem.MIN_SCORE) {
				throw new DataException("(" + problemid + ") 各測資配分必須 >= " + Problem.MIN_SCORE + " 分");
			}
			totalscore += Integer.parseInt(score.trim());
		}

		if (totalscore != Problem.MAX_SCORE) {
			throw new DataException("(" + problemid + ") 分數總和必須為 " + Problem.MAX_SCORE + " 分");
		}
	}

	public void setScores(String scores) throws DataException {
		this.setScores(new Utils().String2Array(scores));
	}

	/**
	 * 進行自動配分
	 * 
	 * @return
	 */
	public int[] autoScoring(int length) {
		int[] scores = new int[length];
		for (int i = 0; i < length; i++) {
			if (i < length - Problem.MAX_SCORE % length) {
				scores[i] = Problem.MAX_SCORE / length;
			} else {
				scores[i] = Problem.MAX_SCORE / length + 1;
			}
		}
		return scores;
	}

	public void setScores(String[] scores) {
		if (scores == null) {
			return;
		}
		try {
			this.checkScores(scores);

			int[] intscores = new int[scores.length];
			for (int i = 0; i < intscores.length; i++) {
				intscores[i] = Integer.parseInt(scores[i]);
			}
			this.setScores(intscores);
		} catch (Exception e) {
			e.printStackTrace();
			this.setScores(this.autoScoring(scores.length));
		}
	}

	public void setScores(ArrayList<String> scores) throws DataException {
		if (scores == null) {
			return;
		}
		this.setScores(scores.toArray(new String[0]));
	}

	public TreeSet<String> getKeywords() {
		return keywords;
	}

	public Integer getTestfilelength() {
		return testfilelength;
	}

	public void setTestfilelength(Integer testfilelength) {
		this.testfilelength = testfilelength;
	}

	public void setKeywords(String keywords) {
		if (keywords == null) {
			return;
		}
		keywords = StringTool.escapeHtmlTag(keywords);
		this.setKeywords(StringTool.String2TreeSet(keywords));
	}

	public void setKeywords(TreeSet<String> keywords) {
		this.keywords = keywords;
	}

	public void setReference(LinkedHashSet<String> reference) {
		if (reference == null) {
			return;
		}
		this.reference = reference;
	}

	public void setReference(String reference) {
		if (reference == null) {
			return;
		}
		reference = StringTool.escapeHtmlTag(reference);
		this.setReference(StringTool.String2LinkedHashSet(reference));
	}

	public String getSortable() {
		return sortable;
	}

	public void setSortable(String sortable) {
		if (sortable != null) {
			this.sortable = sortable;
		}
	}

	public void setAcnum(Integer acnum) {
		this.acnum = acnum;
	}

	public void setAcusers(Integer acusers) {
		this.acusers = acusers;
	}

	public void setAlteroutdata(String alteroutdata) {
		this.alteroutdata = alteroutdata == null ? this.alteroutdata : alteroutdata;
	}


	public void setBackgrounds(String backgrounds) {
		if (backgrounds == null) {
			return;
		}
		backgrounds = StringTool.escapeHtmlTag(backgrounds);
		this.setBackgrounds(StringTool.String2TreeSet(backgrounds));
	}

	public void setBackgrounds(TreeSet<String> backgrounds) {
		this.backgrounds = backgrounds;
	}

	public void setClicknum(Long clicknum) {
		this.clicknum = clicknum;
	}

	public void setComment(String comment) {
		if (comment == null) {
			return;
		}
		this.comment = comment;
	}

	public ServerInput.MODE getJudgemode() {
		return judgemode;
	}

	public void setJudgemode(ServerInput.MODE judgemode) {
		if (judgemode == null) {
			return;
		}
		this.judgemode = judgemode;
	}

	public void setJudgemode(String judgemode) {
		if (judgemode == null) {
			this.setJudgemode(ServerInput.MODE.Tolerant);
			return;
		}
		this.setJudgemode(ServerInput.MODE.valueOf(judgemode));
	}

	//
	//
	//
	public String getSpecialjudge_code() {
		return specialjudge_code;
	}

	public void setSpecialjudge_code(String specialjudge_code) throws IOException {
		if (specialjudge_code == null) {
			return;
		}
		this.specialjudge_code = specialjudge_code;
	}

	//

	public void setContent(String content) {
		if (content == null) {
			return;
		}
		this.content = StringTool.escapeScriptStyle(content.trim());
	}

	public void setDifficulty(Integer difficulty) {
		if (difficulty >= 1 && difficulty <= 5) {
			this.difficulty = difficulty;
		}
	}

	public void setDifficulty(String difficulty) {
		if (difficulty == null || !difficulty.matches("[0-9]+")) {
			return;
		}
		this.setDifficulty(Integer.parseInt(difficulty));
	}

	public void setHint(String hint) {
		if (hint == null) {
			return;
		}
		this.hint = StringTool.escapeScriptStyle(hint);
	}


	public void setInserttime(Timestamp inserttime) {
		this.inserttime = inserttime;
	}

	public void setInserttime(String inserttime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.inserttime = new Timestamp(format.parse(inserttime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Language getLanguage() {
		if (this.language == null) {
			return new Language("C", "c");
		}
		return language;
	}

	public void setLanguage(Language language) {
		if (language == null) {
			return;
		}
		this.language = language;
	}

	public void setLanguage(String language) {
		if (language == null) {
			return;
		}
		this.setLanguage(new Language(language, language.toLowerCase()));
	}

	public Language getSpecialjudge_language() {
		return specialjudge_language;
	}

	public void setSpecialjudge_language(Language specialjudge_language) {
		if (specialjudge_language == null) {
			return;
		}
		this.specialjudge_language = specialjudge_language;
	}

	public void setSpecialjudge_language(String specialjudge_language) {
		logger.info("specialjudge_language=" + specialjudge_language);
		if (specialjudge_language == null) {
			return;
		}
		if ("".equals(specialjudge_language)) {
			this.setSpecialjudge_language(new Language("PYTHON", "python"));
		} else {
			this.setSpecialjudge_language(new Language(specialjudge_language, specialjudge_language.toLowerCase()));
		}
	}

	public void setMemorylimit(String memorylimit) throws DataException {
		if (memorylimit != null && memorylimit.matches("[0-9]+")) {
			this.setMemorylimit(Integer.valueOf(memorylimit));
		}
	}

	public void setMemorylimit(Integer memorylimit) throws DataException {
		if (memorylimit > Problem.MAX_MEMORYLIMIT || memorylimit < Problem.MIN_MEMORYLIMIT) {
			throw new DataException("記憶體(" + memorylimit + ")應指定在 " + Problem.MIN_MEMORYLIMIT + "MB 到 "
					+ Problem.MAX_MEMORYLIMIT + "MB 之間。");
		}
		this.memorylimit = memorylimit;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProblemid(Problemid problemid) {
		this.problemid = problemid;
	}

	public void setProblemid(String problemid) throws DataException {
		Problemid p = new Problemid(problemid);
		if (p.getIsNull() || !p.getIsLegal()) {
			throw new DataException("problemid=" + p + " 不合法！");
		}
		this.setProblemid(p);
	}

	public void setSampleinput(String sampleinput) {
		if (sampleinput == null) {
			return;
		}
		this.sampleinput = StringTool.escapeScriptStyle(sampleinput);
	}

	public void setSampleoutput(String sampleoutput) {
		if (sampleoutput == null) {
			return;
		}
		this.sampleoutput = StringTool.escapeScriptStyle(sampleoutput);
	}

	public void setSubmitnum(Long submitnum) {
		this.submitnum = submitnum;
	}

	public void setSubmitusers(Integer submitusers) {
		this.submitusers = submitusers;
	}


	public void setPrejudgement(String prejudgement) {
		try {
			this.prejudgement = ServerOutput.JUDGEMENT.valueOf(prejudgement);
		} catch (IllegalArgumentException e) {
		}
	}

	public ServerOutput.JUDGEMENT getPrejudgement() {
		return prejudgement;
	}

	public void setPrejudgement(ServerOutput.JUDGEMENT prejudgement) {
		this.prejudgement = prejudgement;
	}

	//

	public String getSummary() {
		String summary = null;
		switch (this.getPrejudgement()) {
			case AC :
				long timeusage = 0;
				int memoryusage = 0;
				for (ServerOutput serverOutput : this.getServeroutputs()) {
					timeusage = Math.max(timeusage, serverOutput.getTimeusage());
					memoryusage = Math.max(memoryusage, serverOutput.getMemoryusage());
				}
				summary = Solution.parseTimeusage(timeusage) + ", " + Solution.parseMemoryusage(memoryusage);
				break;
			case TLE :
				timeusage = 0;
				for (ServerOutput serverOutput : this.getServeroutputs()) {
					timeusage = Math.max(timeusage, serverOutput.getTimeusage());
				}
				summary = Solution.parseTimeusage(timeusage);
				break;
			case MLE :
				memoryusage = 0;
				for (ServerOutput serverOutput : this.getServeroutputs()) {
					memoryusage = Math.max(memoryusage, serverOutput.getMemoryusage());
				}
				summary = Solution.parseMemoryusage(memoryusage);
				break;
			case NA :
				int score = 0;
				for (ServerOutput serverOutput : this.getServeroutputs()) {
					if (serverOutput.getJudgement() == ServerOutput.JUDGEMENT.AC) {
						score += serverOutput.getScore();
					}
				}
				summary = "score:" + score + "%";
				break;
			case RE :
				for (ServerOutput serverOutput : this.getServeroutputs()) {
					summary = serverOutput.getInfo();
					break; 
				}
				break; 
			case WA :
				for (ServerOutput serverOutput : this.getServeroutputs()) {
					summary = serverOutput.getInfo();
					break; 
				}
				break; 
			case Waiting :
				return "";
			default :
				return "";
		}
		return "(" + summary + ")";
	}

	//

	public ServerOutput[] getServeroutputs() {
		return serveroutputs;
	}

	public void setServeroutputs(ServerOutput[] serveroutputs) {
		this.serveroutputs = serveroutputs;
	}

	public void setServeroutputs(String serveroutputs) {
		try {
			this.setServeroutputs(mapper.readValue(serveroutputs, ServerOutput[].class));
		} catch (JsonParseException e) {
			logger.info("JsonParseException: " + e.getLocalizedMessage() + ": serverouts=" + serveroutputs);
			this.setServeroutputs(new ServerOutput[]{});
		} catch (JsonMappingException e) {
			logger.info("JsonMappingException: " + e.getLocalizedMessage() + ": serverouts=" + serveroutputs);
			this.setServeroutputs(new ServerOutput[]{});
		} catch (IOException e) { 
			logger.info("IOException: " + e.getLocalizedMessage() + ": serverouts=" + serveroutputs);
			this.setServeroutputs(new ServerOutput[]{});
		}
	}


	public void setTabid(String tabid) {
		if (tabid == null || "".equals(tabid)) {
			for (Problemtab tab : ApplicationScope.getAppConfig().getProblemtabs()) {
				this.tabid = tab.getId();
				return;
			}
		}
		this.tabid = tabid;
	}

	public String getTabid() {
		return tabid;
	}

	public Problemtab getTab() {
		for (Problemtab tab : ApplicationScope.getAppConfig().getProblemtabs()) {
			if (tab.getId().equals(this.getTabid())) {
				return tab;
			}
		}
		return new Problemtab();
	}

	public void setTheinput(String theinput) {
		if (theinput == null) {
			return;
		}
		this.theinput = StringTool.escapeScriptStyle(theinput);
	}

	public void setTheoutput(String theoutput) {
		if (theoutput == null) {
			return;
		}
		this.theoutput = StringTool.escapeScriptStyle(theoutput);
	}

	public double[] getTimelimits() {
		return timelimits;
	}

	public void setTimelimits(double[] timelimits) {
		this.timelimits = timelimits;
	}

	public void checkTimelimits(String[] timelimits) throws DataException {
		for (String tl : timelimits) {
			try {
				Double.parseDouble(tl);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new DataException("測資時間設定必須為浮點數");
			}
		}
		if (timelimits.length == 1) {
			double timelimit = Double.parseDouble(timelimits[0]);
			if (timelimit <= Problem.MIN_TIMELIMIT) {
				throw new DataException(
						"最低時間限制為 " + Problem.DecimalFormat_ProblemTimeLimit.format(Problem.MIN_TIMELIMIT) + "秒!");
			}
			if (timelimit > Problem.MAX_SINGLE_TIMELIMIT) {
				throw new DataException("單一測資點時間上限為 "
						+ Problem.DecimalFormat_ProblemTimeLimit.format(Problem.MAX_SINGLE_TIMELIMIT) + " 秒!");
			}
		} else {
			int totaltimelimit = 0;
			for (String timelimit : timelimits) {
				double tl = Double.parseDouble(timelimit);
				if (tl <= Problem.MIN_TIMELIMIT) {
					throw new DataException(
							"最低時間限制為 " + Problem.DecimalFormat_ProblemTimeLimit.format(Problem.MIN_TIMELIMIT) + "秒!");
				}
				if (tl > Problem.MAX_TIMELIMIT) {
					throw new DataException("單一測資點時間上限為 "
							+ Problem.DecimalFormat_ProblemTimeLimit.format(Problem.MAX_SINGLE_TIMELIMIT) + " 秒!");
				}
				totaltimelimit += tl;
			}
			if (totaltimelimit > Problem.MAX_TOTAL_TIMELIMIT) {
				throw new DataException("時限總和不能超過 "
						+ Problem.DecimalFormat_ProblemTimeLimit.format(Problem.MAX_TOTAL_TIMELIMIT) + " 秒。");
			}
		}
	}

	public void setTimelimits(String timelimits) throws DataException {
		if (timelimits == null) {
			return;
		}
		this.setTimelimits(new Utils().String2DoubleArray(timelimits));
	}

	public void setTimelimits(ArrayList<String> timelimits) {
		double[] doublearray = new double[timelimits.size()];
		int i = 0;
		for (String timelimit : timelimits) {
			doublearray[i++] = Double.parseDouble(timelimit);
		}
		this.setTimelimits(doublearray);
	}

	public void setTimelimits(String[] timelimits) {
		if (timelimits == null) {
			return;
		}
		double[] doublearray = new double[timelimits.length];
		int i = 0;
		for (String timelimit : timelimits) {
			try {
				doublearray[i++] = Double.parseDouble(timelimit);
			} catch (Exception e) {
				doublearray[i++] = 1.0;
			}
		}
		this.setTimelimits(doublearray);
	}


	public void setTitle(String title) {
		if (title == null || "".equals(title.trim()) || new Problem().getTitle().equals(title.trim())) {
			return;
		}
		this.title = StringTool.escapeScriptStyle(title.trim());
	}

	public void checkTitle(String title) {
		if (title == null || "".equals(title.trim()) || new Problem().getTitle().equals(title.trim())) {
			throw new DataException("請輸入題目標題。(" + problemid + ")");
		}
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public void setUpdatetime(String updatetime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.updatetime = new Timestamp(format.parse(updatetime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Integer getWa_visible() {
		return wa_visible;
	}

	public void setWa_visible(String wa_visible) {
		if (wa_visible == null || !wa_visible.matches("[0-9]")) {
			this.wa_visible = WA_visible_OPEN;
			return;
		}
		this.setWa_visible(Integer.parseInt(wa_visible));
	}

	public void setWa_visible(Integer wa_visible) {
		this.wa_visible = wa_visible;
	}

	public String getReserved_text3() {
		return reserved_text3;
	}

	public void setReserved_text3(String reserved_text3) {
		this.reserved_text3 = reserved_text3;
	}

	public String getReserved_text4() {
		return reserved_text4;
	}

	public void setReserved_text4(String reserved_text4) {
		this.reserved_text4 = reserved_text4;
	}

	public String getReserved_text5() {
		return reserved_text5;
	}

	public void setReserved_text5(String reserved_text5) {
		this.reserved_text5 = reserved_text5;
	}

	public String getReserved_text6() {
		return reserved_text6;
	}

	public void setReserved_text6(String reserved_text6) {
		this.reserved_text6 = reserved_text6;
	}

	/** ****************************************************************** */

	public String getDISPLAY_OPEN() {
		return DISPLAY.open.name();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		if (locale == null) {
			return;
		} else if (!locale.contains("_")) {
			return;
		} else {
			this.locale = new Locale(locale.split("_")[0], locale.split("_")[1]);
		}
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getDISPLAY_VERIFYING() {
		return DISPLAY.verifying.name();
	}

	public String getDISPLAY_CONTEST() {
		return DISPLAY.contest.name();
	}

	public String getDISPLAY_PRACTICE() {
		return DISPLAY.practice.name();
	}

	public String getDISPLAY_HIDE() {
		return DISPLAY.hide.name();
	}

	public String getDISPLAY_DEPRECATED() {
		return DISPLAY.deprecated.name();
	}

	public double getMIN_TIMELIMIT() {
		return MIN_TIMELIMIT;
	}

	public double getMAX_TIMELIMIT() {
		return MAX_TIMELIMIT;
	}

	public double getMAX_SINGLE_TIMELIMIT() {
		return MAX_SINGLE_TIMELIMIT;
	}

	public double getMAX_TOTAL_TIMELIMIT() {
		return MAX_TOTAL_TIMELIMIT;
	}

	public int getMIN_MEMORYLIMIT() {
		return MIN_MEMORYLIMIT;
	}

	public int getMAX_MEMORYLIMIT() {
		return MAX_MEMORYLIMIT;
	}

	public int getMAX_FILESIZE() {
		return MAX_FILESIZE;
	}

	/**
	 * 決定實際上存在測資目錄里的檔案名稱,不含副檔名。<br>
	 * Prefix 有兩種：<br>
	 * 1. problemid, 如 a001, a001_1, a001_2 <br>
	 * 2. Testjudge_solutionid 進行測試執行。
	 * 
	 * @param problemid
	 * @param index
	 * @return
	 */
	public String getTESTDATA_FILENAME(int index) {
		return problemid + (index == 0 ? "" : "_" + index);
	}

	public String getTESTJUDGE_FILENAME(int solutionid, int index) {
		return "Testjudge_" + solutionid + File.separator + "Testjudge_" + solutionid + (index == 0 ? "" : "_" + index);
	}

	/**
	 * 判斷題目的圖片是否使用圖片檔。使用圖片檔應該都改成 ShowImage 讀取。<br>
	 * infos[0] 代表 使用資料庫圖片的數量。<br>
	 * infos[1] 代表 使用zerojudge 站內圖片的數量<br>
	 * infos[2] 代表 使用站外圖片的數量<br>
	 * 
	 * @return
	 */
	public int[] getImageInfos() {
		int[] infos = {0, 0, 0};

		Source source = new Source(getContent() + getTheinput() + getTheoutput() + getHint());
		List<Element> imglist = source.getAllElements(HTMLElementName.IMG);
		String src = null;
		if (imglist.size() == 0) {
			return infos;
		}
		for (Element img : imglist) {
			src = img.getAttributeValue("src");
			String showImage = new ShowImageServlet().getClass().getSimpleName().replaceAll("Servlet", "");
			if (src.startsWith(showImage)) {
				infos[0]++;
			} else if (src.toLowerCase().startsWith("images/problems/")) {
				infos[1]++;
			} else {
				infos[2]++;
			}
		}
		return infos;
	}

	public int getAcscore() {
		int acscore = 0;
		for (int score : scores) {
			acscore += score;
		}
		return acscore;
	}

	//

	/**
	 * status 後所接的 小掛號里的資訊
	 * 
	 * @return
	 */
	private String parsePrejudgeInfo() {
		if (getPrejudgement() == ServerOutput.JUDGEMENT.Waiting) {
			return "";
		} else {
			String result = " <span style=\"font-size: 11px\">" + getSummary() + "</span>";
			return result;
		}
	}

	public String getHtml_PrejudgeResult() {
		String html = "";
		switch (getPrejudgement()) {
			case AC :
				html = "<a href=# id=\"acstyle\">" + getPrejudgement() + "</a>";
				break;
			case Waiting :
				return getPrejudgement().name();
			default :
				html = "<a href=#>" + getPrejudgement() + "</a>";
				break;
		}
		return html + this.parsePrejudgeInfo();
	}



	//


	//

	/**
	 * problem rejudge 時，還剩下多少 solution 還沒有 judge 到
	 * 
	 * @return
	 */
	public int getWaitingRejudgeSize() {
		int size = 0;
		for (JudgeObject judgeObject : JudgeFactory.getJudgeQueue()) {
			if (judgeObject.getProblem().getProblemid().equals(this.getProblemid())
					&& judgeObject.getSolution().getId() != 0) {
				size++;
			}
		}
		return size;

	}

	public boolean isNullProblem() {
		return this.getProblemid().equals(new Problem().getProblemid());
	}


	//

	/**
	 */

	public ArrayList<ServerOutputBean> getServerOutputBeans() {
		ServerOutput[] serverOutputs = this.getServeroutputs();
		ArrayList<ServerOutputBean> serverOutputBeans = new ArrayList<ServerOutputBean>();
		for (int i = 0; i < serverOutputs.length && serverOutputs[i] != null; i++) {
			serverOutputBeans.add(new ServerOutputBean(serverOutputs[i]));
		}
		return serverOutputBeans;
	}

	/**
	 * 取得該題目的 Testdata 的所有檔案列表。
	 * 
	 * @return
	 */

	/**
	 * 取得測資組合列表。從測資檔來決定有幾個測資，而不是由 problem 欄位紀錄。
	 * 
	 * @return
	 */
	public ArrayList<TestdataPair> getTestdataPairs() {
		ArrayList<TestdataPair> testdataPairs = new ArrayList<TestdataPair>();
		File testdataPath = ApplicationScope.getAppConfig().getTestdataPath(problemid);
		for (int index = 0; testdataPath.exists() && index < testdataPath.listFiles().length / 2; index++) {
			File in = new File(testdataPath, this.getTESTDATA_FILENAME(index) + "." + SUFFIX.in);
			File out = new File(testdataPath, this.getTESTDATA_FILENAME(index) + "." + SUFFIX.out);
			if (in.exists() && out.exists()) {
				testdataPairs.add(new TestdataPair(new Testdatafile(in), new Testdatafile(out)));
			} else {
				break;
			}
		}
		return testdataPairs;
	}

	/**
	 * 取得 某個序號的測資組合。 infile & outfile
	 *
	 * @param index
	 * @return
	 */
	public TestdataPair getTestdatapair(int index) {
		return this.getTestdataPairs().get(index);
	}

	public class SpecialJudgeFile extends File {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SpecialJudgeFile(File parent) {
			super(parent, "");
		}

		public Date getLastModified() {
			return new Date(this.lastModified());
		}

		public long getLength() {
			return this.length();
		}
	}

	/**
	 * 取得該題目的 SpecialJudge 的所有檔案列表。
	 * 
	 * @return
	 */
	public ArrayList<SpecialJudgeFile> getSpecialJudgeFiles() {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		ArrayList<SpecialJudgeFile> specialjudgeFiles = new ArrayList<SpecialJudgeFile>();
		File special = appConfig.getSpecialPath(problemid);
		if (special != null && special.exists()) {
			for (File file : special.listFiles()) {
				specialjudgeFiles.add(new SpecialJudgeFile(file));
			}
		}
		return specialjudgeFiles;
	}

	/**
	 * 取得 enable 的 SpecialJudgeFile, 由所選擇的 language 決定。
	 * 
	 * @return
	 */
	public SpecialJudgeFile getSpecialJudgeFile() {
		return this.getSpecialJudgeFile(this.getSpecialjudge_language());
	}

	/**
	 * 取得 enable 的 SpecialJudgeFile, 指定 language
	 * 
	 * @return
	 */
	public SpecialJudgeFile getSpecialJudgeFile(Language language) {
		File file = new File(ApplicationScope.getAppConfig().getSpecialPath(this.getProblemid()),
				"Special_" + this.getProblemid() + "." + language.getSuffix());
		return new SpecialJudgeFile(file);
	}

	/**
	 * 題目的 tag 包含 keywords, backgrounds, reference 等 3 個欄位資料。
	 * 
	 * @return
	 */
	public LinkedHashSet<String> getTags() {
		LinkedHashSet<String> tags = new LinkedHashSet<String>();
		tags.addAll(this.getBackgrounds());
		tags.addAll(this.getKeywords());
		return tags;
	}

	public boolean getIsPrejudgement_AC() {
		return this.getPrejudgement() == JUDGEMENT.AC;
	}

	public boolean getIsPrejudgement_Waiting() {
		return this.getPrejudgement() == JUDGEMENT.Waiting;
	}

	public String getScoresByJSON() {
		try {
			return mapper.writeValueAsString(this.getScores());
		} catch (IOException e) {
			e.printStackTrace();
			return "[]";
		}
	}

	/**
	 * 判斷是否是該題目管理員。
	 * 
	 * @return
	 */
	public boolean getIsOwner(OnlineUser onlineUser) {
		if (!onlineUser.isNullOnlineUser() && !this.isNullProblem()
				&& this.getOwnerid().intValue() == onlineUser.getId().intValue()) {
			return true;
		}
		return false;
	}

}
