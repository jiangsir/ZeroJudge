package tw.zerojudge.DAOs;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Factories.ProblemFactory;
import tw.zerojudge.JsonObjects.ExportProblem;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Judges.ServerOutput.JUDGEMENT;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.ShowImageServlet;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.Problem.DISPLAY;
import tw.zerojudge.Tables.Solution.VISIBLE;

public class ProblemDAO extends SuperDAO<Problem> {

	ObjectMapper mapper = new ObjectMapper(); 

	public ProblemDAO() {
	}

	/**
	 * 用 rules 取出題目。不預設任何限制。只應由 getOpenProblems getEditableProblems 來存取。
	 * 
	 * @param rules   rules 為 AND 條件
	 * @param orderby 傳入 null 代表不指定
	 * @param page    page==0 代表全部列出
	 * @return
	 * @throws DataException
	 */
	public ArrayList<Problem> getProblemsByRules(TreeSet<String> rules, String orderby, int page) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT * FROM problems " + this.makeRules(rules, orderby, page));
		return executeQuery(sql.toString(), Problem.class);
	}

	/**
	 * 用 rules 取出題目。不預設任何限制。只應由 getOpenProblems getEditableProblems 來存取。
	 * 
	 * @param rules   rules 為 AND 條件
	 * @param orderby 傳入 null 代表不指定
	 * @param page    page==0 代表全部列出
	 * @return
	 */
	public TreeSet<Problemid> getProblemidSetByRules(TreeSet<String> rules, String orderby, int page) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT problemid FROM problems " + this.makeRules(rules, orderby, page));
		TreeSet<Problemid> problemidSet = new TreeSet<Problemid>();
		for (Problem problem : executeQuery(sql.toString(), Problem.class)) {
			problemidSet.add(problem.getProblemid());
		}
		return problemidSet;
	}


	public int getCountByFields(TreeMap<String, Object> fields) {
		return this.executeCount("problems", fields);
	}

	public ArrayList<Problem> getAllProblems() {
		String sql = "SELECT * FROM problems";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			return executeQuery(pstmt, Problem.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	protected TreeSet<Problemid> getProblemidsByDisplay(Problem.DISPLAY display) {
		TreeSet<Problemid> tset = new TreeSet<Problemid>();
		String sql = "SELECT problemid FROM problems WHERE display='" + display.name() + "'";
		for (Problem problem : executeQuery(sql, Problem.class)) {
			tset.add(problem.getProblemid());
		}
		return tset;
	}

	protected TreeSet<Problemid> getAllProblemids() {
		TreeSet<Problemid> tset = new TreeSet<Problemid>();
		String sql = "SELECT problemid FROM problems";
		for (Problem problem : executeQuery(sql, Problem.class)) {
			tset.add(problem.getProblemid());
		}
		return tset;
	}

	/**
	 * 取得最新發布的幾個題目
	 * 
	 * @param number
	 * @return
	 */
	public ArrayList<Problem> getLastestProblems(int number) {
		String sql = "SELECT * FROM problems WHERE display='" + Problem.DISPLAY.open.name() + "' "
				+ "ORDER BY updatetime DESC LIMIT 0," + number;
		return executeQuery(sql, Problem.class);
	}

	/**
	 * 取得目前公開的所有題目數目
	 * 
	 * @return
	 */
	public int getCountOfOpenedProblem() {
		String sql = "SELECT * FROM problems WHERE display='" + Problem.DISPLAY.open.name() + "'";
		return executeCount(sql);
	}




	/**
	 * 這個 update 只包含 setting 的部分，不更動 content。<br>
	 * 
	 * @param problem
	 * @return
	 * @throws AccessException
	 * @throws DataException
	 */
	protected int updateProblemSettings_PSTMT(Problem problem) throws DataException {
		String SQL = "UPDATE problems SET timelimits=?, memorylimit=?, "
				+ "backgrounds=?, locale=?, sampleinput=?, sampleoutput=?, "
				+ "language=?, samplecode=?, prejudgement=?, serveroutputs=?, "
				+ "comment=?, testfilelength=?, scores=?, alteroutdata=?, "
				+ "judgemode=?, difficulty=?, acnum=?, submitnum=?, "
				+ "clicknum=?, acusers=?, submitusers=?, lastsolutionid=?, ownerid=?, "
				+ "reference=?, sortable=?, keywords=?, inserttime=?, updatetime=?, "
				+ "tabid=?, wa_visible=?, display=?, reserved_text1=?, reserved_text2=?, "
				+ "reserved_text3=?, reserved_text4=?, reserved_text5=?, reserved_text6=? " + "WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setString(1, mapper.writeValueAsString(problem.getTimelimits()));
			pstmt.setInt(2, problem.getMemorylimit());
			pstmt.setString(3, problem.getBackgrounds().toString());
			pstmt.setString(4, problem.getLocale().toString());
			pstmt.setString(5, problem.getSampleinput());
			pstmt.setString(6, problem.getSampleoutput());
			pstmt.setString(7, problem.getLanguage().getName());
			pstmt.setString(8, problem.getSamplecode());
			pstmt.setString(9, problem.getPrejudgement().name());
			pstmt.setString(10, mapper.writeValueAsString(problem.getServeroutputs()));
			pstmt.setString(11, problem.getComment());
			pstmt.setInt(12, problem.getTestfilelength());
			pstmt.setString(13, mapper.writeValueAsString(problem.getScores()));
			pstmt.setString(14, problem.getAlteroutdata());
			pstmt.setString(15, problem.getJudgemode().name());
			pstmt.setInt(16, problem.getDifficulty());
			pstmt.setInt(17, problem.getAcnum());
			pstmt.setLong(18, problem.getSubmitnum());
			pstmt.setLong(19, problem.getClicknum());
			pstmt.setInt(20, problem.getAcusers());
			pstmt.setInt(21, problem.getSubmitusers());
			pstmt.setInt(22, problem.getLastsolutionid());
			pstmt.setInt(23, problem.getOwnerid());
			pstmt.setString(24, problem.getReference().toString());
			pstmt.setString(25, problem.getSortable());
			pstmt.setString(26, problem.getKeywords().toString());
			pstmt.setTimestamp(27, problem.getInserttime());
			pstmt.setTimestamp(28, problem.getUpdatetime());
			pstmt.setString(29, problem.getTabid());
			pstmt.setInt(30, problem.getWa_visible());
			pstmt.setString(31, problem.getDisplay().name());
			pstmt.setString(32, problem.getSpecialjudge_code());
			pstmt.setString(33, problem.getSpecialjudge_language().getName());
			pstmt.setString(34, problem.getReserved_text3());
			pstmt.setString(35, problem.getReserved_text4());
			pstmt.setString(36, problem.getReserved_text5());
			pstmt.setString(37, problem.getReserved_text6());
			pstmt.setInt(38, problem.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			throw new DataException(e);
		} catch (JsonGenerationException e) {
			throw new DataException(e);
		} catch (JsonMappingException e) {
			throw new DataException(e);
		} catch (IOException e) {
			throw new DataException(e);
		}
		return result;
	}



	/**
	 * 重新計算 problem 的通過、送出人次
	 * 
	 * @param solution
	 * @param priority
	 * @deprecated
	 */
	private void rebuiltProblem_SPEEDUP(Solution solution, JudgeObject.PRIORITY priority) {
		long starttime = System.currentTimeMillis();
		Problem problem = solution.getProblem();
		User user = new UserService().getUserById(solution.getUserid());
		if (priority == JudgeObject.PRIORITY.Rejudge) {
			problem.setSubmitnum(problem.getSubmitnum() - 1);
			if (!new SolutionDAO().isTrythisProblem(user, problem.getId(), solution.getId())) {
				problem.setSubmitusers(problem.getSubmitusers() - 1);
			}
			if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
				problem.setAcnum(problem.getAcnum() - 1);
				if (!user.getAclist().contains(problem.getProblemid())) {
					problem.setAcusers(problem.getAcusers() - 1); 
				}
			}
		}
		problem = this.rebuiltProblem_accumulate(problem, solution);
		problem.setLastsolutionid(Math.max(solution.getId(), problem.getLastsolutionid()));
		this.update(problem);
	}

	/**
	 * 
	 * @param problem
	 * @param solution
	 * @return
	 * @throws DataException
	 * @deprecated
	 */
	private Problem rebuiltProblem_accumulate(Problem problem, Solution solution) throws DataException {
		User user = new UserService().getUserById(solution.getUserid());
		problem.setSubmitnum(problem.getSubmitnum() + 1);
		if (!new SolutionDAO().isTrythisProblem(user, problem.getId(), solution.getId())) {
			problem.setSubmitusers(problem.getSubmitusers() + 1);
		}
		if (solution.getJudgement() == ServerOutput.JUDGEMENT.AC) {
			problem.setAcnum(problem.getAcnum() + 1);
			if (!user.getAclist().contains(problem.getProblemid())) {
				problem.setAcusers(problem.getAcusers() + 1);
			}
		}
		return problem;
	}

	public LinkedHashMap<User, Integer> getTopAuthors(int topn) {
		LinkedHashMap<User, Integer> topAuthors = new LinkedHashMap<User, Integer>();
		ArrayList<HashMap<String, Object>> authors = this
				.executeQueryByMap("SELECT ownerid, COUNT(*) AS COUNT FROM `problems` WHERE ownerid!=0 AND display='"
						+ DISPLAY.open + "' GROUP BY ownerid ORDER BY COUNT DESC LIMIT 0, " + topn);

		for (HashMap<String, Object> author : authors) {
			User user = new UserService().getUserById((Integer) author.get("ownerid"));
			topAuthors.put(user, ((Long) author.get("COUNT")).intValue());
		}
		return topAuthors;
	}

	/**
	 * 重新計算題目的 通過人數/送出人數(這個執行非常慢)
	 * 
	 * @param problem
	 */
	protected void recountProblem_UserCount(Problem problem) {
		long starttime = System.currentTimeMillis();
		String count = "count";
		String sql = "SELECT COUNT(*) as " + count + " FROM( SELECT COUNT(*) FROM solutions WHERE pid="
				+ problem.getId() + " AND visible=" + VISIBLE.open.getValue() + " GROUP BY userid) as submituser";
		long submitusers = (Long) this.executeQueryByMap(sql).get(0).get(count);
		String sql2 = "SELECT COUNT(*) as " + count + " FROM( SELECT COUNT(*) FROM solutions WHERE pid="
				+ problem.getId() + " AND judgement=" + JUDGEMENT.AC.ordinal() + " AND visible="
				+ VISIBLE.open.getValue() + " GROUP BY userid) as acuser";
		long acusers = (Long) this.executeQueryByMap(sql2).get(0).get(count);
		problem.setAcusers((int) acusers);
		problem.setSubmitusers((int) submitusers);
		this.update(problem);
	}

	/**
	 * 重新計算某個 problemid 的 acnum, submitnum
	 * 
	 * @param problemid
	 * @throws AccessException
	 * @throws DataException
	 * @deprecated
	 */
	private void rebuiltProblem_SLOW(int pid) {
		ArrayList<?> list = this.executeQueryByMap("SELECT *,COUNT(*) AS COUNT FROM " + "solutions WHERE pid=" + pid
				+ " GROUP BY pid,userid,judgement ORDER BY userid ASC");
		if (list == null || list.size() == 0) {
			return;
		}
		int acusers = 0, submitusers = 0;
		int acnum = 0, submitnum = 0;
		int max_solutionid = 0;
		String currentuser = "";
		for (int i = 0; i < list.size(); i++) {
			HashMap<?, ?> map = (HashMap<?, ?>) list.get(i);
			max_solutionid = Math.max((Integer) map.get("solutionid"), max_solutionid);
			if (ServerOutput.JUDGEMENT.valueOf((Integer) map.get("judgement")) == ServerOutput.JUDGEMENT.AC) {
				acusers++;
				acnum += Integer.parseInt(map.get("COUNT").toString());
			}
			if (!currentuser.equals(map.get("userid").toString())) {
				currentuser = map.get("userid").toString();
				submitusers++;
			}
			submitnum += Integer.parseInt(map.get("COUNT").toString());
		}


		ProblemService problemService = new ProblemService();
		Problem problem = problemService.getProblemByPid(pid);
		problem.setAcnum(acnum);
		problem.setSubmitnum((long) submitnum);
		problem.setAcusers(acusers);
		problem.setSubmitusers(submitusers);
		problem.setLastsolutionid(max_solutionid);
		problemService.update(problem);
	}



	/**
	 * 以 pid 直接由資料庫取得。
	 * 
	 * @param pid
	 * @return
	 */
	public Problem getProblemByPid(long pid) {
		String sql = "SELECT * FROM problems WHERE id=" + pid;
		for (Problem problem : executeQuery(sql, Problem.class)) {
			return problem;
		}
		return ProblemFactory.getNullproblem();
	}


	/**
	 * 將 problemid 轉換成 id
	 * 
	 * @param problemid
	 * @return
	 */
	public long getIdByProblemid(Problemid problemid) {
		if (problemid.getIsNull() || problemid.getIsEmpty()) {
			return ProblemFactory.getNullproblem().getId();
		}
		String sql = "SELECT id FROM problems WHERE problemid=?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, problemid.toString());
			for (long id : this.executeQueryId(pstmt)) {
				return id;
			}
			return ProblemFactory.getNullproblem().getId();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}







	public int updateProblemTab(Problemtab from_tab, Problemtab to_tab) throws AccessException {
		String SQL = "UPDATE problems SET tabid=? WHERE tabid=?";
		int result = -1;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setString(1, to_tab.getId());
			pstmt.setString(2, from_tab.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			throw new DataException(e);
		}
		return result;

	}


	/**
	 * 把所有題目的總分糾正為 100
	 * 
	 * @throws DataException
	 */
	public String currect_ProblemScores() {
		String result = "";
		for (Problem problem : this.getProblemsByRules(new TreeSet<String>(), null, 0)) {
			int totalscore = 0;
			for (int score : problem.getScores()) {
				if (score <= 0) {
					totalscore = score;
					break;
				}
				totalscore += score;
			}
			if (totalscore <= 0 || totalscore != 100) {
				try {
					result += "problemid=" + problem.getProblemid() + ", scores="
							+ mapper.writeValueAsString(problem.getScores()) + ", totalscore=" + totalscore + "<br>\n";
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public File ExportProblemJson(Problemid problemid, OnlineUser onlineUser)
			throws DataException, JsonGenerationException, JsonMappingException, IOException {
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		if (!onlineUser.isCanUpdateProblem(problem)) {
			return null;
		}


		ExportProblem exportProblem = new ExportProblem();
		exportProblem.setProblemid(problem.getProblemid().toString());
		exportProblem.setTitle(problem.getTitle());
		exportProblem.setTimelimits(problem.getTimelimits());
		exportProblem.setMemorylimit(problem.getMemorylimit());
		exportProblem.setBackgrounds(problem.getBackgrounds().toString());
		exportProblem.setLocale(problem.getLocale());
		exportProblem.setContent(problem.getContent());
		exportProblem.setTheinput(problem.getTheinput());
		exportProblem.setTheoutput(problem.getTheoutput());
		exportProblem.setSampleinput(problem.getSampleinput());
		exportProblem.setSampleoutput(problem.getSampleoutput());
		exportProblem.setHint(problem.getHint());
		exportProblem.setLanguage(problem.getLanguage().getName());
		exportProblem.setSamplecode(problem.getSamplecode());
		exportProblem.setComment(problem.getComment());
		exportProblem.setTestfilelength(problem.getTestfilelength());
		exportProblem.setScores(problem.getScores());
		exportProblem.setJudgemode(problem.getJudgemode());
		exportProblem.setSpecialjudge_language(problem.getSpecialjudge_language());
		exportProblem.setSpecialjudge_code(problem.getSpecialjudge_code());
		exportProblem.setDifficulty(problem.getDifficulty());
		exportProblem.setAuthor(problem.getOwner().getAccount());
		exportProblem.setReference(problem.getReference().toString());
		exportProblem.setSortable(problem.getSortable());
		exportProblem.setKeywords(problem.getKeywords().toString());
		exportProblem.setInserttime(problem.getInserttime().toString());
		exportProblem.setUpdatetime(problem.getUpdatetime().toString());
		exportProblem.setErrmsg_visible(problem.getWa_visible());
		exportProblem.setDisplay(problem.getDisplay().name());

		//

		List<Problemimage> problemimages = this.getImages(problem);

		Base64 base64 = new Base64();
		HashMap<Integer, String> imageStrings = new HashMap<Integer, String>();
		for (Problemimage problemimage : problemimages) {
			problemimage.setFilebytes(base64.encode(new ProblemimageDAO().getFile(problemimage.getId())));
			imageStrings.put(problemimage.getId(),
					new String(base64.encode(new ProblemimageDAO().getFile(problemimage.getId()))));
		}
		exportProblem.setProblemimages(problemimages);

		ArrayList<String> testinfiles = new ArrayList<String>();
		ArrayList<String> testoutfiles = new ArrayList<String>();

		int testfilelength = Math.min(problem.getTestfilelength(), problem.getTestdataPairs().size());
		for (int i = 0; i < testfilelength; i++) {
			testinfiles.add(problem.getTestdatapair(i).getInfile().getFullData());
			testoutfiles.add(problem.getTestdatapair(i).getOutfile().getFullData());
		}
		exportProblem.setTestinfiles(testinfiles);
		exportProblem.setTestoutfiles(testoutfiles);

		File file = new File(ApplicationScope.getSystemTmp(), problemid + "_" + problem.getTitle() + ".zjson");
		if (!file.exists()) {
			FileUtils.writeStringToFile(new File(file.getParent(), file.getName()),
					mapper.writeValueAsString(exportProblem));
		}
		return file;
	}

	/**
	 * 將匯入的題目內所有 imgsrc 的 #id 替換為正確的 ShowImage?id=xxx
	 * 
	 * @param problem
	 * @return
	 */
	public Problem replaceImagesForImport(Problem problem) {

		return problem;
	}

	public List<Problemimage> getImages(Problem problem) {
		ProblemimageDAO problemimageDao = new ProblemimageDAO();
		List<Problemimage> ids = new ArrayList<Problemimage>();
		String urlpattern = ShowImageServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];
		urlpattern = urlpattern.substring(1);

		Source source = new Source(
				problem.getContent() + problem.getTheinput() + problem.getTheoutput() + problem.getHint());
		Iterator<Element> imgs = source.getAllElements(HTMLElementName.IMG).iterator();
		while (imgs.hasNext()) {
			Element element = imgs.next();
			String src = element.getAttributeValue("src");
			if (src.startsWith(urlpattern)) {
				ids.add(problemimageDao.getImage(Integer.parseInt(src.substring(src.lastIndexOf("=") + 1))));
			}
		}
		return ids;
	}

	//

	@Override
	public synchronized int insert(Problem problem) throws DataException {
		String sql = "INSERT INTO problems(problemid, title, timelimits, "
				+ "memorylimit, backgrounds, locale, content, theinput, "
				+ "theoutput, sampleinput, sampleoutput, hint, language, "
				+ "samplecode, prejudgement, serveroutputs, comment, "
				+ "testfilelength, scores, alteroutdata, judgemode, difficulty, "
				+ "acnum, submitnum, clicknum, acusers, submitusers, "
				+ "lastsolutionid, ownerid, reference, sortable, keywords, "
				+ "inserttime, updatetime, tabid, wa_visible, display, "
				+ "reserved_text1, reserved_text2, reserved_text3, reserved_text4, "
				+ "reserved_text5, reserved_text6) " + "VALUES (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, "
				+ "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, problem.getProblemid().toString());
			pstmt.setString(2, problem.getTitle());
			pstmt.setString(3, mapper.writeValueAsString(problem.getTimelimits()));
			pstmt.setInt(4, problem.getMemorylimit());
			pstmt.setString(5, problem.getBackgrounds().toString());
			pstmt.setString(6, problem.getLocale().toString());
			pstmt.setString(7, problem.getContent());
			pstmt.setString(8, problem.getTheinput());
			pstmt.setString(9, problem.getTheoutput());
			pstmt.setString(10, problem.getSampleinput());
			pstmt.setString(11, problem.getSampleoutput());
			pstmt.setString(12, problem.getHint());
			pstmt.setString(13, problem.getLanguage().getName());
			pstmt.setString(14, problem.getSamplecode());
			pstmt.setString(15, problem.getPrejudgement().name());
			pstmt.setString(16, mapper.writeValueAsString(problem.getServeroutputs()));
			pstmt.setString(17, problem.getComment());
			pstmt.setInt(18, problem.getTestfilelength());
			pstmt.setString(19, Arrays.toString(problem.getScores()));
			pstmt.setString(20, problem.getAlteroutdata());
			pstmt.setString(21, problem.getJudgemode().name());
			pstmt.setInt(22, problem.getDifficulty());
			pstmt.setInt(23, problem.getAcnum());
			pstmt.setLong(24, problem.getSubmitnum());
			pstmt.setLong(25, problem.getClicknum());
			pstmt.setInt(26, problem.getAcusers());
			pstmt.setInt(27, problem.getSubmitusers());
			pstmt.setInt(28, problem.getLastsolutionid());
			pstmt.setInt(29, problem.getOwnerid());
			pstmt.setString(30, problem.getReference().toString());
			pstmt.setString(31, problem.getSortable());
			pstmt.setString(32, problem.getKeywords().toString());
			pstmt.setTimestamp(33, new Timestamp(System.currentTimeMillis()));
			pstmt.setTimestamp(34, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(35, problem.getTabid());
			pstmt.setInt(36, problem.getWa_visible());
			pstmt.setString(37, problem.getDisplay().name());
			pstmt.setString(38, problem.getSpecialjudge_code());
			pstmt.setString(39, problem.getSpecialjudge_language().getName());
			pstmt.setString(40, problem.getReserved_text3());
			pstmt.setString(41, problem.getReserved_text4());
			pstmt.setString(42, problem.getReserved_text5());
			pstmt.setString(43, problem.getReserved_text6());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new DataException(e);
		} catch (JsonGenerationException e) {
			throw new DataException(e);
		} catch (JsonMappingException e) {
			throw new DataException(e);
		} catch (IOException e) {
			throw new DataException(e);
		}
		problem.setId(id);

		if (problem.getDisplay() == Problem.DISPLAY.open) {
			ApplicationScope.getOpenedProblemidSet().add(problem.getProblemid());
		} else {
			ApplicationScope.getOpenedProblemidSet().remove(problem.getProblemid());
		}

		return id;
	}

	@Override
	protected synchronized int update(Problem problem) throws DataException {
		String SQL = "UPDATE problems SET title=?, timelimits=?, "
				+ "memorylimit=?, backgrounds=?, locale=?, content=?, theinput=?, "
				+ "theoutput=?, sampleinput=?, sampleoutput=?, hint=?, "
				+ "language=?, samplecode=?, prejudgement=?, serveroutputs=?, "
				+ "comment=?, testfilelength=?, scores=?, alteroutdata=?, judgemode=?, "
				+ "difficulty=?, acnum=?, submitnum=?, clicknum=?, acusers=?, "
				+ "submitusers=?, lastsolutionid=?, ownerid=?, reference=?, "
				+ "sortable=?, keywords=?, inserttime=?, updatetime=?, "
				+ "tabid=?, wa_visible=?, display=?, reserved_text1=?, "
				+ "reserved_text2=?, reserved_text3=?, reserved_text4=?, reserved_text5=?, "
				+ "reserved_text6=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setString(1, problem.getTitle());
			pstmt.setString(2, Arrays.toString(problem.getTimelimits()));
			pstmt.setInt(3, problem.getMemorylimit());
			pstmt.setString(4, problem.getBackgrounds().toString());
			pstmt.setString(5, problem.getLocale().toString());
			pstmt.setString(6, problem.getContent());
			pstmt.setString(7, problem.getTheinput());
			pstmt.setString(8, problem.getTheoutput());
			pstmt.setString(9, problem.getSampleinput());
			pstmt.setString(10, problem.getSampleoutput());
			pstmt.setString(11, problem.getHint());
			pstmt.setString(12, problem.getLanguage().getName());
			pstmt.setString(13, problem.getSamplecode());
			pstmt.setString(14, problem.getPrejudgement().name());
			pstmt.setString(15, mapper.writeValueAsString(problem.getServeroutputs()));
			pstmt.setString(16, problem.getComment());
			pstmt.setInt(17, problem.getTestfilelength());
			pstmt.setString(18, Arrays.toString(problem.getScores()));
			pstmt.setString(19, problem.getAlteroutdata());
			pstmt.setString(20, problem.getJudgemode().name());
			pstmt.setInt(21, problem.getDifficulty());
			pstmt.setInt(22, problem.getAcnum());
			pstmt.setLong(23, problem.getSubmitnum());
			pstmt.setLong(24, problem.getClicknum());
			pstmt.setInt(25, problem.getAcusers());
			pstmt.setInt(26, problem.getSubmitusers());
			pstmt.setInt(27, problem.getLastsolutionid());
			pstmt.setInt(28, problem.getOwnerid());
			pstmt.setString(29, problem.getReference().toString());
			pstmt.setString(30, problem.getSortable());
			pstmt.setString(31, problem.getKeywords().toString());
			pstmt.setTimestamp(32, problem.getInserttime());
			pstmt.setTimestamp(33, problem.getUpdatetime());
			pstmt.setString(34, problem.getTabid());
			pstmt.setInt(35, problem.getWa_visible());
			pstmt.setString(36, problem.getDisplay().name());
			pstmt.setString(37, problem.getSpecialjudge_code());
			pstmt.setString(38, problem.getSpecialjudge_language().getName());
			pstmt.setString(39, problem.getReserved_text3());
			pstmt.setString(40, problem.getReserved_text4());
			pstmt.setString(41, problem.getReserved_text5());
			pstmt.setString(42, problem.getReserved_text6());
			pstmt.setInt(43, problem.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			throw new DataException(e);
		} catch (JsonGenerationException e) {
			throw new DataException(e);
		} catch (JsonMappingException e) {
			throw new DataException(e);
		} catch (IOException e) {
			throw new DataException(e);
		}


		if (problem.getDisplay() == Problem.DISPLAY.open) {
			ApplicationScope.getOpenedProblemidSet().add(problem.getProblemid());
		} else {
			ApplicationScope.getOpenedProblemidSet().remove(problem.getProblemid());
		}


		return result;
	}

	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM problems WHERE id=?";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, id);
			return this.execute(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	protected ArrayList<Problem> getProblemByFields(TreeMap<String, Object> fields, String orderby, int page) {
		String sql = "SELECT * FROM problems " + this.makeFields(fields, orderby, page);

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			int i = 1;
			for (String field : fields.keySet()) {
				pstmt.setObject(i++, fields.get(field));
			}
			return executeQuery(pstmt, Problem.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}

	}

	protected TreeSet<Problemid> getProblemidSetByFields(TreeMap<String, Object> fields, String orderby, int page) {
		String sql = "SELECT problemid FROM problems " + this.makeFields(fields, orderby, page);

		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			int i = 1;
			for (String field : fields.keySet()) {
				pstmt.setObject(i++, fields.get(field));
			}
			TreeSet<Problemid> problemidSet = new TreeSet<Problemid>();
			for (Problem problem : executeQuery(pstmt, Problem.class)) {
				problemidSet.add(problem.getProblemid());
			}
			return problemidSet;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}

	}

}
