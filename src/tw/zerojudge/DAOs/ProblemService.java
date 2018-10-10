package tw.zerojudge.DAOs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.Factories.ProblemFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.JsonObjects.ExportProblem;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.JudgeProducer;
import tw.zerojudge.Judges.JudgeServer;
import tw.zerojudge.Judges.ServerInput;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.ShowImageServlet;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Problemimage;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;

public class ProblemService {
	ObjectMapper mapper = new ObjectMapper(); 
	public static Hashtable<Integer, Problem> HashProblems = new Hashtable<Integer, Problem>();
	public static Hashtable<Problemid, Integer> HashProblemids = new Hashtable<Problemid, Integer>();
	public static Hashtable<String, TreeSet<Problemid>> HashProblemtabProblemidset = new Hashtable<String, TreeSet<Problemid>>();
	private TreeSet<Problemid> allProblemids = new TreeSet<Problemid>();

	public int insert(Problem problem) {
		int pid = new ProblemDAO().insert(problem);
		if (pid == 0) {
			throw new DataException("題目新增失敗(" + problem.getTitle() + ")！");
		}
		return pid;
	}

	public int update(Problem problem) {
		Problem oldProblem = new ProblemDAO().getProblemByPid(problem.getId());
		int result = new ProblemDAO().update(problem);
		HashProblems.put(problem.getId(), problem);
		HashProblemids.put(problem.getProblemid(), problem.getId());
		HashProblemtabProblemidset.clear();
		this.getProblemtabProblemidset();
		if ((oldProblem.getDisplay() == Problem.DISPLAY.open && problem.getDisplay() != Problem.DISPLAY.open)
				|| (oldProblem.getDisplay() != Problem.DISPLAY.open && problem.getDisplay() == Problem.DISPLAY.open)) {
			new UserService().rebuiltUserStatisticByProblem(problem.getProblemid());
		}
		return result;
	}

	public int updateProblemSettings(Problem problem) {
		int result = new ProblemDAO().updateProblemSettings_PSTMT(problem);
		if (problem.getDisplay() == Problem.DISPLAY.open) {
			ApplicationScope.getOpenedProblemidSet().add(problem.getProblemid());
		} else {
			ApplicationScope.getOpenedProblemidSet().remove(problem.getProblemid());
		}
		HashProblemtabProblemidset.clear();
		this.getProblemtabProblemidset();
		return result;
	}


	public boolean delete(int id) {
		return new ProblemDAO().delete(id);
	}

	/**
	 * 取消刪除！刪除題目會導致其它地方要讀取的時候找不到這個題目，而出現很多奇怪的問題。 <br/>
	 * 因此必須確保題目不被刪除。
	 * 
	 * @deprecated
	 * @param problemid
	 * @return
	 */
	public boolean delete(String problemid) {
		return false;
	}

	public Problem getProblemByPid(int pid) {
		if (!HashProblems.containsKey(pid)) {
			Problem problem = new ProblemDAO().getProblemByPid(pid);
			HashProblems.put(pid, problem);
		}

		return HashProblems.get(pid);
	}

	public Problem getProblemByProblemid(Problemid problemid) {
		int pid = 0;
		if (HashProblemids.containsKey(problemid)) {
			pid = HashProblemids.get(problemid);
		} else {
			pid = (int) new ProblemDAO().getIdByProblemid(problemid);
			HashProblemids.put(problemid, (int) pid);
		}
		return this.getProblemByPid(pid);
	}

	public int getOpenedProblemCount() {
		return new ProblemDAO().getCountOfOpenedProblem();
	}

	public TreeSet<Problemid> getOpenedProblemidSet() {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("display", Problem.DISPLAY.open.name());
		return new ProblemDAO().getProblemidSetByFields(fields, null, 0);
	}


	/**
	 * 取得某個 last_solutionid 之後的解題 problemid <br>
	 * 用在 每日重新計算 recount 題目的答對/解題 人數
	 * 
	 * @param solutionid
	 */
	public void recountProblemidAfter_LastSolutionid() {
		for (Problemid problemid : new SolutionDAO().getProblemidAfter_LastSolutionid()) {
			this.recountProblem_UserCount(problemid);
		}

	}

	public Hashtable<Integer, Problem> getHashProblems() {
		return HashProblems;
	}

	public int getCountByAllProblems() {
		return new ProblemDAO().getCountByFields(null);
	}


	public int getLastPageByFields(TreeMap<String, Object> fields) {
		int count = new ProblemDAO().getCountByFields(fields);
		int pagesize = ApplicationScope.getAppConfig().getPageSize();
		return count % pagesize == 0 ? count / pagesize : (count / pagesize) + 1;
	}

	public ArrayList<Problem> getAllProblems() {
		return new ProblemDAO().getAllProblems();
	}

	/**
	 * 取得 OnlineUser 未完成 Insert 的題目。多半是按了 InsertProblem, 但沒有送出。
	 * 
	 * @return
	 */
	public Problem getUnfinishedProblem(OnlineUser onlineUser) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("ownerid", onlineUser.getId());
		fields.put("display", Problem.DISPLAY.unfinished.name());
		for (Problem problem : new ProblemDAO().getProblemByFields(fields, null, 0)) {
			return problem;
		}
		return ProblemFactory.getNullproblem();
	}


	public Hashtable<String, TreeSet<Problemid>> getProblemtabProblemidset() {
		if (ProblemService.HashProblemtabProblemidset.size() == 0) {
			AppConfig appConfig = ApplicationScope.getAppConfig();
			for (Problemtab problemtab : appConfig.getProblemtabs()) {
				ProblemService.HashProblemtabProblemidset.put(problemtab.getId(),
						this.getProblemidSetByTabidDisplay(problemtab.getId(), Problem.DISPLAY.open));
			}
		}
		return HashProblemtabProblemidset;
	}

	private TreeSet<Problemid> getProblemidSetByTabidDisplay(String tabid, Problem.DISPLAY display) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("tabid", tabid);
		fields.put("display", display.name());
		return new ProblemDAO().getProblemidSetByFields(fields, null, 0);
	}

	public boolean isexitProblemid(Problemid problemid) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("problemid", problemid.toString());
		return new ProblemDAO().getCountByFields(fields) > 0;
	}

	public ArrayList<Problem> getLastestProblems(int number) {
		ArrayList<Problem> problems = new ProblemDAO().getLastestProblems(number);
		return problems;
	}

	/**
	 * 獲得下一個可用的 problemid
	 * 
	 * @param prefix
	 * @return
	 */
	//
	//
	//

	/**
	 * 獲取一個可用的 problemid
	 * 
	 * @return
	 */
	public Problemid createNextProblemid() {
		if (allProblemids.size() == 0) {
			allProblemids = new ProblemDAO().getAllProblemids();
		}

		for (char prefix = ApplicationScope.getAppConfig().getProblemid_prefix().charAt(0); prefix <= 'z'; prefix++) {
			for (int serial = 1; serial < 1000; serial++) {
				Problemid next = new Problemid(prefix + new DecimalFormat("000").format(serial));
				if (!allProblemids.contains(next)) {
					return next;
				}
			}
		}
		return null;
	}

	public Problem ImportProblemJson(OnlineUser onlineUser, File file)
			throws JsonParseException, JsonMappingException, IOException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		Problem problem = new Problem();
		ExportProblem exportProblem = mapper.readValue(file, ExportProblem.class);
		Problemid nextproblemid = this.createNextProblemid();

		problem.setProblemid(nextproblemid);
		problem.setTitle(exportProblem.getTitle());
		problem.setTimelimits(exportProblem.getTimelimits());
		ArrayList<Problemtab> tabs = appConfig.getProblemtabs();
		problem.setTabid(tabs.get(0).getId());
		problem.setMemorylimit(exportProblem.getMemorylimit());
		problem.setBackgrounds(exportProblem.getBackgrounds());
		problem.setLocale(exportProblem.getLocale());
		problem.setContent(exportProblem.getContent());
		problem.setTheinput(exportProblem.getTheinput());
		problem.setTheoutput(exportProblem.getTheoutput());
		problem.setSampleinput(exportProblem.getSampleinput());
		problem.setSampleoutput(exportProblem.getSampleoutput());
		problem.setHint(exportProblem.getHint());
		problem.setLanguage(exportProblem.getLanguage());
		problem.setScores(exportProblem.getScores());
		problem.setSamplecode(exportProblem.getSamplecode());
		problem.setComment(exportProblem.getComment());
		problem.setTestfilelength(exportProblem.getTestfilelength());
		problem.setJudgemode(exportProblem.getJudgemode());
		problem.setDifficulty(exportProblem.getDifficulty());
		problem.setOwnerid(onlineUser.getId());
		problem.setReference(exportProblem.getReference());
		problem.setSortable(exportProblem.getSortable());
		problem.setKeywords(exportProblem.getKeywords());
		problem.setInserttime(exportProblem.getInserttime());
		problem.setUpdatetime(exportProblem.getUpdatetime());
		problem.setWa_visible(exportProblem.getErrmsg_visible());
		problem.setSpecialjudge_language(exportProblem.getSpecialjudge_language());
		problem.setSpecialjudge_code(exportProblem.getSpecialjudge_code());
		problem.setDisplay(Problem.DISPLAY.hide);

		ProblemimageDAO imageDao = new ProblemimageDAO();
		Base64 base64 = new Base64();

		List<Problemimage> images = exportProblem.getProblemimages();
		for (Problemimage image : images) {
			int oldid = image.getId();
			image.setProblemid(problem.getProblemid().toString());
			image.setFile(new ByteArrayInputStream(base64.decode(image.getFilebytes())));
			int imageid = imageDao.insert(image);
			problem.setContent(this.replaceImageForImport(oldid, imageid, problem.getContent()));
			problem.setTheinput(this.replaceImageForImport(oldid, imageid, problem.getTheinput()));
			problem.setTheoutput(this.replaceImageForImport(oldid, imageid, problem.getTheoutput()));
			problem.setHint(this.replaceImageForImport(oldid, imageid, problem.getHint()));
		}

		int testfilelength = problem.getTestfilelength();
		for (int i = 0; i < testfilelength; i++) {
			this.doCreateTestfile(problem, i, exportProblem.getTestinfiles().get(i),
					exportProblem.getTestoutfiles().get(i));
		}

		int newpid = insert(problem);

		Problem newproblem = getProblemByPid(newpid);

		return newproblem;
	}

	/**
	 * 把題目當中的 img 替換成 #id (id>0)。 以便匯入時替換為實際的 imageid
	 * 
	 * @param html
	 * @return
	 */

	/**
	 * 將匯入的題目當中 imgsrc #id 替換成 ShowImage?id=xx
	 * 
	 * @param id
	 * @param html
	 * @return
	 */
	private String replaceImageForImport(int oldid, int imageid, String html) {
		Source source = new Source(html);
		OutputDocument document = new OutputDocument(source);
		List<Element> imglist = source.getAllElements(HTMLElementName.IMG);
		String urlpattern = ShowImageServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];
		urlpattern = urlpattern.substring(1);
		for (Element img : imglist) {
			String src = img.getAttributeValue("src");
			if (src.equals(urlpattern + "?id=" + oldid)) {
				document.replace(img.getAttributes().get("src").getValueSegment(), urlpattern + "?id=" + imageid);
			}
		}
		return document.toString();
	}

	/**
	 * 安裝過程加入預設題目
	 * 
	 * @throws AccessException
	 */
	public void insertInitProblems() {
		if (this.getCountByAllProblems() > 0) {
			return;
		}
		AppConfig appConfig = ApplicationScope.getAppConfig();
		ArrayList<Problemtab> problemtabs = appConfig.getProblemtabs();

		Problem problem = new Problem();
		problem.setProblemid(new Problemid("a001"));
		problem.setTitle("哈囉");
		problem.setTimelimits(new double[] { 1, 1 });
		problem.setLanguage(new Language("C", "c"));
		problem.setScores(new int[] { 20, 80 });
		problem.setTabid(problemtabs.get(0).getId());
		problem.setMemorylimit(64);
		problem.setTestfilelength(2);
		problem.setBackgrounds(StringTool.String2TreeSet("[基本顯示]"));
		problem.setLocale(Problem.locale_zh_TW);
		problem.setContent("<p>學習所有程式語言的第一個練習題 <br />" + "請寫一個程式，可以讀入指定的字串，並且輸出指定的字串。</p>");
		problem.setTheinput("指定的文字");
		problem.setTheoutput("輸出指定的文字");
		problem.setSampleinput("world\nC++\nmary");
		problem.setSampleoutput("hello, world\nhello, C++\nhello, mary");
		problem.setHint("<p>程式說明：因為系統會賦予數量不一的測試資料來測驗" + "您的程式是否正確，因此必須先以一個 while 迴圈來讀取所有的測試資料。</p>"
				+ "<p>如有疑問，請參考 <a href=\"UserGuide.jsp#Samplecode\">" + "範例程式碼</a></p>");
		problem.setSamplecode("");
		problem.setJudgemode(ServerInput.MODE.Tolerant);
		problem.setDifficulty(1);
		problem.setOwnerid(1);
		problem.setReference("Brian Kernighan");
		problem.setInserttime(new Timestamp(System.currentTimeMillis()));
		problem.setUpdatetime(new Timestamp(System.currentTimeMillis()));
		problem.setDisplay(Problem.DISPLAY.open);
		this.insert(problem);
	}

	/**
	 * 將一個 problem 內所有圖片都下載為 db 圖片
	 * 
	 * @param problem
	 * @return
	 */
	public Problem downloadProblemAttachfiles(Problem problem, int serverport) {
		problem.setContent(this.downloadImage(problem.getProblemid(), problem.getContent(), serverport));
		problem.setContent(this.downloadAudio(problem.getProblemid(), problem.getContent(), serverport));
		problem.setTheinput(this.downloadImage(problem.getProblemid(), problem.getTheinput(), serverport));
		problem.setTheinput(this.downloadAudio(problem.getProblemid(), problem.getTheinput(), serverport));
		problem.setTheoutput(this.downloadImage(problem.getProblemid(), problem.getTheoutput(), serverport));
		problem.setTheoutput(this.downloadAudio(problem.getProblemid(), problem.getTheoutput(), serverport));
		problem.setHint(this.downloadImage(problem.getProblemid(), problem.getHint(), serverport));
		problem.setHint(this.downloadAudio(problem.getProblemid(), problem.getHint(), serverport));
		return problem;
	}

	/**
	 * 將所傳入的 html 碼當中的 img 抽出，並存入資料庫。
	 * 
	 * @param html
	 * @return
	 */
	private String downloadImage(Problemid problemid, String html, int serverport) {
		Source source = new Source(html);
		OutputDocument document = new OutputDocument(source);
		List<Element> imglist = source.getAllElements(HTMLElementName.IMG);
		String src = null;
		for (Element img : imglist) {
			src = img.getAttributeValue("src");
			String urlpattern = ShowImageServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];
			urlpattern = urlpattern.substring(1);
			if (src.startsWith(urlpattern)) {
				continue;
			}

			File file = new File(ApplicationScope.getSystemTmp(), System.currentTimeMillis() + "");
			URL url;
			try {
				if (!src.startsWith("http")) {
					url = new URL("http://127.0.0.1:" + serverport + "/"
							+ (ApplicationScope.getAppRoot().getName().equals("ROOT") ? ""
									: ApplicationScope.getAppRoot().getName() + "/")
							+ src);
				} else {
					url = new URL(src);
				}
				FileUtils.copyURLToFile(url, file);

				int max_MB = 6;
				if (file.length() > max_MB * 1024 * 1024) { 
					throw new DataException("請使用較小的圖檔。上限(" + max_MB + " MB)");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				new LogDAO().insert(new Log(problemid + ": " + src + " 無法讀取！", this.getClass(), e));
				continue;
			}

			try {
				Problemimage problemimage = new Problemimage();
				problemimage.setProblemid(problemid.toString());
				problemimage.setFilename(url.getFile());
				problemimage.setFiletype(url.openConnection().getContentType());
				problemimage.setFilesize(0); 
				problemimage.setFile(new FileInputStream(file));
				problemimage.setDescript(url.toString());
				int problemimageid = new ProblemimageDAO().insert(problemimage);
				document.replace(img.getAttributes().get("src").getValueSegment(),
						urlpattern + "?id=" + problemimageid);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		return document.toString();
	}

	/**
	 * 取得 autio 的 src 路徑。<br/>
	 * 處理 src 的兩種寫法。
	 * 
	 * @return
	 */
	private String getAudioSrc(Element audio) {
		String src = audio.getAttributeValue("src");
		if (src != null) {
			return src;
		}

		Element audio_source = audio.getFirstElement(HTMLElementName.SOURCE);
		if (audio_source != null) {
			return audio_source.getAttributeValue("src");
		}
		return "";
	}

	/**
	 * 下載後，用新的 url 取代。
	 * 
	 * @return
	 */
	private void replaceAudioSrc(OutputDocument document, Element audio, String urlpattern, int id) {
		String src = audio.getAttributeValue("src");
		if (src != null) {
			document.replace(audio.getAttributes().get("src").getValueSegment(), urlpattern + "?id=" + id);
			return;
		}
		Element audio_source = audio.getFirstElement(HTMLElementName.SOURCE);
		if (audio_source != null) {
			document.replace(audio_source.getAttributes().get("src").getValueSegment(), urlpattern + "?id=" + id);
			return;
		}
	}

	/**
	 * 將所傳入的 html 碼當中的 audio 抽出，並存入資料庫。
	 * 
	 * @param html
	 * @return
	 */
	private String downloadAudio(Problemid problemid, String html, int serverport) {
		Source source = new Source(html);
		OutputDocument document = new OutputDocument(source);
		List<Element> audios = source.getAllElements(HTMLElementName.AUDIO);
		String src = null;
		for (Element audio : audios) {
			src = this.getAudioSrc(audio);
			String urlpattern = ShowImageServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];
			urlpattern = urlpattern.substring(1);
			if (src == null || src.startsWith(urlpattern)) {
				continue;
			}

			File file = new File(ApplicationScope.getSystemTmp(), System.currentTimeMillis() + "");
			URL url;
			try {
				if (!src.startsWith("http")) {
					url = new URL("http://127.0.0.1:" + serverport + "/"
							+ (ApplicationScope.getAppRoot().getName().equals("ROOT") ? ""
									: ApplicationScope.getAppRoot().getName() + "/")
							+ src);
				} else {
					url = new URL(src);
				}
				FileUtils.copyURLToFile(url, file);

				int max_MB = 6;
				if (file.length() > max_MB * 1024 * 1024) { 
					throw new DataException("請使用較小的檔案。上限(" + max_MB + " MB)");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				new LogDAO().insert(new Log(problemid + ": " + src + " 無法讀取！", this.getClass(), e));
				continue;
			}

			try {
				Problemimage problemimage = new Problemimage();
				problemimage.setProblemid(problemid.toString());
				problemimage.setFilename(url.getFile());
				problemimage.setFiletype(url.openConnection().getContentType());
				problemimage.setFilesize(0); 
				problemimage.setFile(new FileInputStream(file));
				problemimage.setDescript(url.toString());
				int problemimageid = new ProblemimageDAO().insert(problemimage);
				this.replaceAudioSrc(document, audio, urlpattern, problemimageid);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		return document.toString();
	}

	/**
	 * 取得不開放題目列表。包含隱藏，練習，已提交...等
	 * 
	 * @param onlineUser
	 * @param page
	 * @return
	 */
	public ArrayList<Problem> getMyProblems(OnlineUser onlineUser, int page) {
		TreeSet<String> rules = new TreeSet<String>();
		if (onlineUser.isNullUser()) {
			return new ArrayList<Problem>();
		} else {
			rules.add("display!='" + Problem.DISPLAY.deprecated.name() + "'");
			rules.add("display!='" + Problem.DISPLAY.unfinished.name() + "'");
			rules.add("ownerid=" + onlineUser.getId());
		}
		return new ProblemDAO().getProblemsByRules(rules, "updatetime DESC", page);
	}

	/**
	 * 取得 “有權編輯”的題目列表。只能用在 EditProblems 裡 <br>
	 * 確保 problem 不是 deprecated 以避免誤取<br>
	 * 
	 * @param rules   rules 為 AND 條件
	 * @param orderby 傳入 null 代表不指定
	 * @param page    page==0 代表全部列出
	 * @return
	 */
	public ArrayList<Problem> getEditableProblems(OnlineUser onlineUser, TreeSet<String> rules, String orderby,
			int page) {
		if (onlineUser.isNullUser()) {
			return new ArrayList<Problem>();
		} else if (onlineUser.getIsDEBUGGER()) {
		} else if (onlineUser.getIsMANAGER()) {
			rules.add("display!='" + Problem.DISPLAY.deprecated.name() + "'");
		} else {
			rules.add("display!='" + Problem.DISPLAY.deprecated.name() + "'");
			rules.add("ownerid=" + onlineUser.getId());
		}
		rules.add("display!='" + Problem.DISPLAY.unfinished.name() + "'");
		return new ProblemDAO().getProblemsByRules(rules, orderby, page);
	}

	/**
	 * 
	 * @param onlineUser
	 * @param page
	 * @return
	 */
	public ArrayList<Problem> getSpecialProblems(OnlineUser onlineUser, int page) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		if (onlineUser.isNullUser()) {
			return new ArrayList<Problem>();
		} else if (onlineUser.getIsDEBUGGER()) {
//		} else if (onlineUser.isGeneralManager()) {
//			fields.put("display!", Problem.DISPLAY.deprecated.name());
//			fields.put("display!", Problem.DISPLAY.unfinished.name());
		} else {
			fields.put("display!", Problem.DISPLAY.deprecated.name());
			fields.put("display!", Problem.DISPLAY.unfinished.name());
			fields.put("ownerid", onlineUser.getId());

		}
		fields.put("judgemode", "Special");
		return new ProblemDAO().getProblemByFields(fields, "updatetime DESC", page);
	}

	/**
	 * 
	 * @param onlineUser
	 * @param page
	 * @return
	 */
	public ArrayList<Problem> getVerifyingProblems(OnlineUser onlineUser, int page) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		if (onlineUser.isNullUser()) {
			return new ArrayList<Problem>();
		} else if (onlineUser.getIsDEBUGGER()) {
//		} else if (onlineUser.isGeneralManager()) {
//			
//			
//			fields.put("display!", Problem.DISPLAY.deprecated.name());
//			fields.put("display!", Problem.DISPLAY.unfinished.name());
		} else {
			fields.put("display!", Problem.DISPLAY.deprecated.name());
			fields.put("display!", Problem.DISPLAY.unfinished.name());
			fields.put("ownerid", onlineUser.getId());

		}
		fields.put("display", Problem.DISPLAY.verifying.name());
		return new ProblemDAO().getProblemByFields(fields, "updatetime DESC", page);
	}

	/**
	 * 取得不開放題目列表。包含隱藏，練習，已提交...等
	 * 
	 * @param onlineUser
	 * @param rules
	 * @param orderby
	 * @param page
	 * @return
	 */
	public ArrayList<Problem> getNotopenProblems(OnlineUser onlineUser, int page) {

		TreeSet<String> rules = new TreeSet<String>();
		if (onlineUser.isNullUser()) {
			return new ArrayList<Problem>();
		} else if (onlineUser.getIsDEBUGGER()) {
		} else if (onlineUser.getIsMANAGER()) {
			rules.add("display!='" + Problem.DISPLAY.deprecated.name() + "'");
			rules.add("display!='" + Problem.DISPLAY.open.name() + "'");
			rules.add("display!='" + Problem.DISPLAY.unfinished.name() + "'");
		} else {
			rules.add("display!='" + Problem.DISPLAY.deprecated.name() + "'");
			rules.add("display!='" + Problem.DISPLAY.open.name() + "'");
			rules.add("display!='" + Problem.DISPLAY.unfinished.name() + "'");
			rules.add("ownerid=" + onlineUser.getId());
		}
		return new ProblemDAO().getProblemsByRules(rules, "updatetime DESC", page);

	}

	/**
	 * 讀取所有題目。
	 * 
	 * @param session
	 * @param orderby
	 * @return
	 */
	public ArrayList<Problem> getAllProlems(OnlineUser onlineUser, String orderby) {
		if (onlineUser.getIsDEBUGGER()) {
			ArrayList<Problem> problems = new ProblemDAO().getProblemByFields(new TreeMap<String, Object>(), orderby,
					0);
			return problems;
		} else {
			return new ArrayList<Problem>();
		}
	}

	/**
	 * 不論前面的 Rule 為何，確保取得的 problemBean 只會是 Open 的。
	 * 
	 * @param rules
	 * @param orderby
	 * @param page
	 * @return
	 */
	public ArrayList<Problem> getOpenProblemsByRules(TreeSet<String> rules, String orderby, int page) {
		rules.add("display='" + Problem.DISPLAY.open.name() + "'");
		return new ProblemDAO().getProblemsByRules(rules, orderby, page);
	}

	public ArrayList<Problem> getOpenProblemsByFields(TreeMap<String, Object> fields, String orderby, int page) {
		fields.put("display", Problem.DISPLAY.open.name());
		return new ProblemDAO().getProblemByFields(fields, orderby, page);
	}


	/**
	 * 計算某個題目總共被送出多少次答案。
	 * 
	 * @return
	 */
	public int getCountByProblemSubmissions(Problem problem) {
		TreeMap<String, Object> rules = new TreeMap<String, Object>();
		rules.put("pid", problem.getId());
		rules.put("visible", Solution.VISIBLE.open.getValue());
		return new SolutionDAO().executeCount(rules);
	}

	public LinkedHashMap<User, Integer> getTopAuthors(int topn) {
		return new ProblemDAO().getTopAuthors(topn);
	}


	/**
	 * 題目管理者進行的前測
	 * 
	 * @param session_account
	 * @param problemid
	 * @throws DataException
	 */
	public void doPreJudge(OnlineUser onlineUser, Problem problem) {
		ServerOutput[] serverOutputs;
		JudgeObject judgeObject;
		try {
			judgeObject = new JudgeObject(JudgeObject.PRIORITY.Prejudge, new Solution(), problem);
		} catch (Exception e) {
			serverOutputs = new ServerOutput[1];
			ServerOutput serverOutput = new ServerOutput();
			serverOutput.setSession_account(onlineUser.getAccount());
			serverOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			serverOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			serverOutput.setHint("資料有誤，請檢查。" + e.getLocalizedMessage());
			serverOutputs[0] = serverOutput;
			new JudgeServer().summaryServerOutput(JudgeObject.PRIORITY.Prejudge, problem, null, serverOutputs);
			e.printStackTrace();
			return;
		}
		JudgeProducer judgeProducer = new JudgeProducer(JudgeFactory.getJudgeServer(), judgeObject);
		Thread judgeProducerThread = new Thread(judgeProducer);
		judgeProducerThread.start();
	}

	/**
	 * 實際建立題目的測資檔,包含輸出及輸入測資。
	 * 
	 * @param index       第幾個測資檔，由 0 開始
	 * @param infiledata  輸入檔資料
	 * @param outfiledata 輸出檔資料
	 * @throws IOException
	 */
	public void doCreateTestfile(Problem problem, int index, String infiledata, String outfiledata) throws IOException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		FileUtils.writeStringToFile(new File(appConfig.getTestdataPath(problem.getProblemid()),
				problem.getTESTDATA_FILENAME(index) + ".in"), infiledata);
		FileUtils.writeStringToFile(new File(appConfig.getTestdataPath(problem.getProblemid()),
				problem.getTESTDATA_FILENAME(index) + ".out"), outfiledata);
	}

	/**
	 * 刪除編號 index 的測資
	 * 
	 * @param index
	 * @throws DataException
	 * @throws AccessException
	 */
	public void doDeleteTestdataPair(Problem problem, int index) {

		AppConfig appConfig = ApplicationScope.getAppConfig();
		File filein = new File(appConfig.getTestdataPath(problem.getProblemid()),
				problem.getTESTDATA_FILENAME(index) + ".in");
		File fileout = new File(appConfig.getTestdataPath(problem.getProblemid()),
				problem.getTESTDATA_FILENAME(index) + ".out");
		int testfilelength = problem.getTestdataPairs().size();
		int[] scores = problem.getScores();
		double[] timelimits = problem.getTimelimits();
		if (filein.exists() && fileout.exists()) {
			problem.getTestdataPairs().remove(index);

			filein.delete();
			fileout.delete();

			for (int i = index; i < testfilelength; i++) {
				new File(appConfig.getTestdataPath(problem.getProblemid()), problem.getTESTDATA_FILENAME(i + 1) + ".in")
						.renameTo(new File(appConfig.getTestdataPath(problem.getProblemid()),
								problem.getTESTDATA_FILENAME(i) + ".in"));
				new File(appConfig.getTestdataPath(problem.getProblemid()),
						problem.getTESTDATA_FILENAME(i + 1) + ".out")
								.renameTo(new File(appConfig.getTestdataPath(problem.getProblemid()),
										problem.getTESTDATA_FILENAME(i) + ".out"));
			}
			double[] newtimelimits = new double[testfilelength - 1];
			int[] newscores = new int[testfilelength - 1];

			for (int i = 0; i < testfilelength - 1; i++) {
				if (i != index) {
					newscores[i] = scores[i];
					newtimelimits[i] = timelimits[i];
				} else {
					newscores[i] = scores[i + 1];
					newtimelimits[i] = timelimits[i + 1];
				}
			}
			problem.setTestfilelength(testfilelength - 1);
			problem.setTimelimits(newtimelimits);
			problem.setScores(newscores);
		}
		this.update(problem);
		this.rsyncTestdataByProblem(problem);
	}

	/**
	 * 同步全系統的題目到裁判機上。
	 */
	public void rsyncAllTestdatas() {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		File testdataPath = new File(appConfig.getTestdataPath() + File.separator);
		if (!testdataPath.exists()) {
			ArrayList<String> debugs = new ArrayList<String>();
			debugs.add(testdataPath.toString() + "不存在");
			throw new DataException("本地端的測資(" + testdataPath.getName() + ")不存在，因此無法同步到裁判機上！請管理員檢查。", debugs);
		}


		File serverTestdataPath = appConfig.getServerConfig().getTestdataPath();

		new RunCommand().exec(RunCommand.Command.whoami.toString());


		String rsync = "sudo -u " + appConfig.getRsyncAccount() + " ";
		rsync += "rsync -av --delete -e \"ssh -p " + appConfig.getServerConfig().getSshport() + "\" "
				+ appConfig.getTestdataPath() + File.separator + " " + appConfig.getServerConfig().getRsyncAccount()
				+ "@" + appConfig.getServerUrl().getHost() + ":" + serverTestdataPath;

		RunCommand runCommand = new RunCommand();
		runCommand.exec(rsync);

		if (runCommand.getErrStream().size() != 0) {
			throw new DataException("無法與裁判機同步測資！請管理員檢查。\n", runCommand.getErrStream());
		}

	}

	/**
	 * 由 Webapp 端將測資檔 push 到 裁判機。 用 rsync 傳遞。
	 * 
	 * @param problem
	 */
	public void rsyncTestdataByProblem(Problem problem) {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		File testfile = appConfig.getTestdataPath(problem.getProblemid());
		if (!testfile.exists()) {
			ArrayList<String> debugs = new ArrayList<String>();
			debugs.add(testfile.getPath() + "不存在");

			throw new DataException("本地端的測資檔不存在，因此無法同步到裁判機上！請管理員檢查。(" + testfile.getName() + ")", debugs);
		}


		File serverTestdataPath = appConfig.getServerConfig().getTestdataPath();

		String rsync = "";
		rsync += RunCommand.Command.sudo + " -u " + appConfig.getRsyncAccount() + " ";
		rsync += RunCommand.Command.rsync + " -av --delete -e \"" + RunCommand.Command.ssh + " -p "
				+ appConfig.getServerConfig().getSshport() + "\" " + appConfig.getTestdataPath(problem.getProblemid())
				+ " ";
		InetAddress address;
		try {
			address = InetAddress.getByName(appConfig.getServerUrl().getHost());
			if (!address.isLoopbackAddress()) {
				rsync += appConfig.getServerConfig().getRsyncAccount() + "@" + appConfig.getServerUrl().getHost() + ":";
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		rsync += serverTestdataPath;

		RunCommand runCommand = new RunCommand();
		runCommand.exec(rsync);

		if (runCommand.getErrStream().size() != 0) {
			RunCommand whoami = new RunCommand();
			whoami.exec(RunCommand.Command.whoami.name());

			ArrayList<String> error = new ArrayList<String>();
			error.addAll(whoami.getOutStream());
			error.add(rsync);
			error.addAll(runCommand.getErrStream());
			throw new DataException("無法與裁判機同步測資！請管理員檢查。", error);
		}
	}

	/**
	 * 將 SpecialJudge 程式執行檔同步到 JudgeServer
	 */
	public void rsyncSpecialJudge(Problem problem) {
		AppConfig appConfig = ApplicationScope.getAppConfig();

		File specialjudge = appConfig.getSpecialPath(problem.getProblemid());
		if (!specialjudge.exists()) {
			if (!specialjudge.mkdirs()) {
				throw new DataException("無法建立目錄，請檢查權限。" + specialjudge);
			}
		}

		if (!specialjudge.exists()) {
			ArrayList<String> debugs = new ArrayList<String>();
			debugs.add(specialjudge.getPath() + "不存在");
			throw new DataException("本地端的「Special Judge 資料夾」建立失敗，因此無法同步到裁判機上！請管理員檢查。(" + specialjudge.getName() + ")",
					debugs);
		}

		String rsync = RunCommand.Command.sudo + " -u " + appConfig.getRsyncAccount() + " ";
		rsync += RunCommand.Command.rsync + " --chmod=+x -av --delete -e \"" + RunCommand.Command.ssh + " -p "
				+ appConfig.getServerConfig().getSshport() + "\" " + appConfig.getSpecialPath(problem.getProblemid())
				+ " ";
		InetAddress address;
		try {
			address = InetAddress.getByName(appConfig.getServerUrl().getHost());
			if (!address.isLoopbackAddress()) {
				rsync += appConfig.getServerConfig().getRsyncAccount() + "@" + appConfig.getServerUrl().getHost() + ":";
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		rsync += appConfig.getServerConfig().getSpecialPath();

		RunCommand runCommand = new RunCommand();
		runCommand.exec(rsync);

		if (runCommand.getErrStream().size() != 0) {
			RunCommand whoami = new RunCommand();
			whoami.execWhoami();

			ArrayList<String> error = new ArrayList<String>();
			error.addAll(whoami.getOutStream());
			error.add(rsync);
			error.addAll(runCommand.getErrStream());
			throw new DataException("無法與裁判機同步「Special Judge程式」！請管理員檢查。", error);
		}

	}

	/**
	 * 以 TreeSet 放置所有 problems 所有存在的 keyrowd
	 * 
	 * @return
	 */
	public TreeSet<String> getSuggestKeywords(HttpSession session, String servletPath) {
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		TreeSet<String> keywords = new TreeSet<String>();
		TreeSet<String> rules = new TreeSet<String>();
		if (onlineUser.isNullUser()) {
			return new TreeSet<String>();
		} else {
			rules.add("display!='" + Problem.DISPLAY.deprecated.name() + "'");
			rules.add("ownerid=" + onlineUser.getId());
		}
		rules.add("display!='" + Problem.DISPLAY.unfinished.name() + "'");
		rules.add("keywords!='" + new TreeSet<String>().toString() + "' AND keywords!=''");

		for (Problem problem : new ProblemDAO().getProblemsByRules(rules, null, 0)) {
			keywords.addAll(problem.getKeywords());
		}
		return keywords;
	}

	/**
	 * 以 TreeSet 放置所有 problems 所有存在的 background
	 * 
	 * @return
	 */
	public TreeSet<String> getSuggestBackgrounds(HttpSession session, String servletPath) {
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		TreeSet<String> rules = new TreeSet<String>();
		if (onlineUser.isNullUser()) {
			return new TreeSet<String>();
		} else {
			rules.add("display!='" + Problem.DISPLAY.deprecated.name() + "'");
			rules.add("ownerid=" + onlineUser.getId());
		}
		rules.add("display!='" + Problem.DISPLAY.unfinished.name() + "'");
		rules.add("backgrounds!='" + new TreeSet<String>().toString() + "' AND backgrounds!=''");

		TreeSet<String> backgrounds = new TreeSet<String>();
		for (Problem problem : new ProblemDAO().getProblemsByRules(rules, null, 0)) {
			backgrounds.addAll(problem.getBackgrounds());
		}
		return backgrounds;
	}

	/**
	 * 全文檢索，以及進階搜尋。
	 * 
	 * @param tab_search
	 * @param difficulty
	 * @param keyword
	 * @param background
	 * @param searchword
	 * @param author
	 * @param locale
	 * @return
	 */
	public TreeSet<String> getSearchRules(String tag, String[] tab_search, String[] difficulty, String keyword,
			String background, String searchword, String locale, String ownerid) {
		tag = StringEscapeUtils.escapeSql(tag);
		keyword = StringEscapeUtils.escapeSql(keyword);
		background = StringEscapeUtils.escapeSql(background);
		searchword = StringEscapeUtils.escapeSql(searchword);
		locale = StringEscapeUtils.escapeSql(locale);

		TreeSet<String> rules = new TreeSet<String>();
		if (tag != null && !"".equals(tag.trim())) {
			if (tag.length() > 50) {
				throw new DataException("tag 太長！");
			}
			rules.add("keywords LIKE '%" + tag + "%' OR backgrounds LIKE '%" + tag + "%' OR reference LIKE '%" + tag
					+ "%'");
			return rules;
		}
		if (ownerid != null && ownerid.trim().matches("[0-9]+")) {
			rules.add("ownerid=" + Integer.parseInt(ownerid.trim()));
		}

		if (searchword != null && !"".equals(searchword)) {
			if (searchword.length() > 30) {
				throw new DataException("searchword 太長！(" + searchword + ")");
			}
			rules.add("problemid='" + searchword + "' OR title LIKE '%" + searchword + "%' OR backgrounds LIKE '%"
					+ searchword + "%' OR content LIKE '%" + searchword + "%' OR theinput LIKE '%" + searchword
					+ "%' OR theoutput LIKE '%" + searchword + "%' OR hint LIKE '%" + searchword
					+ "%' OR reference LIKE '%" + searchword + "%' OR keywords LIKE '%" + searchword + "%'");
		}

		if (tab_search != null && tab_search.length > 0) {
			String tab_rule = "tabid='" + tab_search[0] + "'";
			for (int i = 1; i < tab_search.length; i++) {
				tab_rule += " OR tabid='" + tab_search[i] + "'";
			}
			rules.add(tab_rule);
		}
		if (difficulty != null && difficulty.length > 0) {
			String difficulty_rule = "difficulty=" + difficulty[0];
			for (int i = 1; i < difficulty.length; i++) {
				difficulty_rule += " OR difficulty=" + difficulty[i];
			}
			rules.add(difficulty_rule);
		}
		if (keyword != null && !"".equals(keyword)) {
			if (keyword.length() > 30) {
				throw new DataException("keyword 太長！");
			}
			rules.add("keywords LIKE '%" + keyword + "%' OR backgrounds LIKE '%" + keyword + "%' OR reference LIKE '%"
					+ keyword + "%'");
		}
		if (background != null && !"".equals(background)) {
			if (background.length() > 30) {
				throw new DataException("background 太長！");
			}

			rules.add("backgrounds LIKE '%" + background + "%'");
		}

		if (locale != null && !"".equals(locale)) {
			rules.add("locale ='" + locale + "'");
		}


		if (rules.size() == 0) {
			throw new DataException("很抱歉，您並未選擇任何條件。");
		}

		return rules;
	}

	/**
	 * 重新「所有」計算題目的 通過人數/送出人數
	 * 
	 */
	public void recountAllProblem_UserCount() {
		ProblemDAO problemDao = new ProblemDAO();
		for (Problem problem : this.getAllProblems()) {
			problemDao.recountProblem_UserCount(problem);
		}
	}

	/**
	 * 重新計算題目的 通過人數/送出人數(這個執行非常慢)
	 * 
	 * @param problem
	 */
	public void recountProblem_UserCount(Problemid problemid) {
		new ProblemDAO().recountProblem_UserCount(this.getProblemByProblemid(problemid));
	}

	/**
	 * 重新計算題目的 通過人數/送出人數(這個執行非常慢)
	 * 
	 * @param problem
	 */
	public void recountProblem_UserCount(Problem problem) {
		new ProblemDAO().recountProblem_UserCount(problem);
	}

}
