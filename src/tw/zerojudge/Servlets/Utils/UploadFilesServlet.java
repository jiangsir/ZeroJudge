package tw.zerojudge.Servlets.Utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.FileTools;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Pair;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Objects.TestdataPair;
import tw.zerojudge.Objects.Testdatafile.SUFFIX;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User.ROLE;

@MultipartConfig(maxFileSize = 100 * 1024 * 1024, maxRequestSize = 500 * 1024 * 1024)
@WebServlet(urlPatterns = { "/UploadFiles.api" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class UploadFilesServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void AccessFilter(HttpServletRequest request) throws AccessException {
		new UpdateProblemServlet().AccessFilter(request);
	}

	public String getFilename(Part part) {
		String header = part.getHeader("Content-Disposition");
		String filename = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
		return filename;
	}

	/**
	 * 將 filename regex 換成所需要的 index 比如: a001_([0-9]+).in --> a001_01.in
	 * 
	 * @param regex
	 * @param filename
	 * @param replacement
	 * @return
	 */
	public String replaceFilename(String regex, String filename, String replacement) {
		StringBuffer sb = new StringBuffer(filename);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(filename);
		if (m.find()) {
			sb.replace(m.start(1), m.end(1), replacement);
			return sb.toString();
		}
		return filename;
	}

	private int countCharInString(char c, String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 檢查 infile outfile 的 pattern 是否有效。<br>
	 * 接受 1 個 或 2 個 ## 來代表數字位數，3個以上不接受。 正確範例: (a001_##.in, a001_##.out) (a001_#.in,
	 * a001_#.out) 錯誤範例: (a001_###.in, a001_###.out) (a00#_##.in, a00#_##.out)
	 * 
	 * @param infilepattern
	 * @param outfilepattern
	 * @return
	 */
	public boolean checkFilepattern(String infilepattern, String outfilepattern) {
		if (infilepattern == null || outfilepattern == null) {
			throw new DataException("pattern==null 無法取得 pattern");
		}
		if (infilepattern.equals(outfilepattern)) {
			throw new DataException("輸入測資與輸出測資的  pattern 不可以相同。");
		}
		if (infilepattern.matches(".*#.+#.*") || outfilepattern.matches(".*#.+#.*")) {
			throw new DataException("# 代位字元位置有誤!");
		}
		if (this.countCharInString('#', infilepattern) != this.countCharInString('#', outfilepattern)) {
			throw new DataException("# 代位字元數量不一致!");
		}
		String rule1 = ".*[#]{1}.*\\..*";
		String rule2 = ".*[#]{2}.*\\..*";
		if ((infilepattern.matches(rule1) && outfilepattern.matches(rule1))
				|| (infilepattern.matches(rule2) && outfilepattern.matches(rule2))) {
			return true;
		}
		throw new DataException("pattern 不符合規則，範例如下: a001_##.in, a001_##.out");
	}

	/**
	 * 由使用者上傳的檔案中, 分析出所有合乎規則的檔名。 檔名符合 pattern, 編號由 0 開始, 連續編號。 純粹分析檔名，並不實際進行存檔
	 * 只要有不符規則的測資檔，彈出顯示訊息，並全部不進行上傳。
	 * 
	 * @return
	 */
	public Pair<ArrayList<Pair<String, String>>, Set<String>> matchedLocalfilePairs(String infilepattern,
			String outfilepattern, Set<String> localfileSet) {
		int size = localfileSet.size();
		ArrayList<Pair<String, String>> matchedLocalfilePairs = new ArrayList<>();
		for (int index = 0; index < size / 2; index++) {
			String infilename = infilepattern.replaceAll("##", new DecimalFormat("00").format(index));
			String outfilename = outfilepattern.replaceAll("##", new DecimalFormat("00").format(index));
			infilename = infilename.replaceAll("#", new DecimalFormat("0").format(index));
			outfilename = outfilename.replaceAll("#", new DecimalFormat("0").format(index));
			if (localfileSet.contains(infilename) && localfileSet.contains(outfilename)) {
				matchedLocalfilePairs.add(Pair.create(infilename, outfilename));
				localfileSet.remove(infilename);
				localfileSet.remove(outfilename);
			} else {
				break;
			}
		}
		return Pair.create(matchedLocalfilePairs, localfileSet);
	}

	/**
	 * 從使用者上傳的測資檔中取出格式正確的 TestdataPairs
	 * 
	 * @param infilepattern
	 * @param outfilepattern
	 * @param localfilenames
	 * @return "testdtaPairs": List &lt;TestdataPair&gt;, "notmatchLocalfileSet":
	 *         TreeSet&lt;String&gt;
	 * @throws IOException
	 * @Deprecated
	 */

	private HashMap<String, Object> readTestdataPairs(String infilepattern, String outfilepattern,
			HashMap<String, Part> testdataParts, Problem problem) throws IOException {
		HashMap<String, Object> returnMap = new HashMap<>();
		List<TestdataPair> testdataPairs = new ArrayList<TestdataPair>();
		Set<String> localfileSet = testdataParts.keySet();
		File testdataPath = ApplicationScope.getAppConfig().getTestdataPath(problem.getProblemid());
		String rsyncaccount = ApplicationScope.getAppConfig().getRsyncAccount();
		int size = localfileSet.size();
		for (int index = 0; index < size / 2; index++) {
			String infilename = infilepattern.replaceAll("##", new DecimalFormat("00").format(index));
			String outfilename = outfilepattern.replaceAll("##", new DecimalFormat("00").format(index));
			infilename = infilename.replaceAll("#", new DecimalFormat("0").format(index));
			outfilename = outfilename.replaceAll("#", new DecimalFormat("0").format(index));
			if (localfileSet.contains(infilename) && localfileSet.contains(outfilename)) {

				if (!testdataPath.exists()) {
					FileTools.forceMkdir(testdataPath, rsyncaccount);
				}

				File infile = new File(testdataPath, problem.getTESTDATA_FILENAME(index) + "." + SUFFIX.in);

				FileTools.writePartToFile(infile, testdataParts.get(infilename), rsyncaccount);


				File outfile = new File(testdataPath, problem.getTESTDATA_FILENAME(index) + "." + SUFFIX.out);
				FileTools.writePartToFile(outfile, testdataParts.get(outfilename), rsyncaccount);


				problem.setTestfilelength(index + 1);

				localfileSet.remove(infilename);
				localfileSet.remove(outfilename);
			} else {
				break;
			}
		}
		returnMap.put("testdataPairs", testdataPairs);
		returnMap.put("notmatchLocalfileSet", localfileSet);
		return returnMap;
	}

	private double defaultTimelimit(int size) {
		if (size <= 10) {
			return 1.0;
		} else {
			return 10.0 / size;
		}
	}

	/**
	 * 上傳測資檔
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	private void doPost_uploadTestdataPairs(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, ServletException, JQueryException {
		new UpdateProblemServlet().AccessFilter(request);

		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		HashMap<String, Part> testdataParts = new HashMap<>();
		Part testdataPart = request.getPart("testdatas");
		if (this.getFilename(testdataPart) == null || "".equals(this.getFilename(testdataPart))) {
			throw new JQueryException("您未選取任何檔案，沒有檔案可以上傳！");
		}
		for (Part part : request.getParts()) {
			if ("testdatas".equals(part.getName())) {
				String localfile = this.getFilename(part);
				testdataParts.put(localfile, part);
			}
		}

		String infilepattern = request.getParameter("infilepattern");
		String outfilepattern = request.getParameter("outfilepattern");
		try {
			checkFilepattern(infilepattern, outfilepattern);
		} catch (DataException e) {
			throw new JQueryException(e.getLocalizedMessage());
		}

		Set<String> localfileSet = new TreeSet<>(testdataParts.keySet());
		Pair<ArrayList<Pair<String, String>>, Set<String>> returnPair = this.matchedLocalfilePairs(infilepattern,
				outfilepattern, localfileSet);
		ArrayList<Pair<String, String>> matchedLocalfilePairs = returnPair.getLeft();
		Set<String> notmatchedLocalfileSet = returnPair.getRight();
		if (notmatchedLocalfileSet.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("測資上傳失敗!!! \n\n");
			sb.append("上傳測資檔需符合以下規則: \n");
			sb.append("1. 檔名必須符合定義的 pattern, 您的 pattern 為(" + infilepattern + ", " + outfilepattern + ")。\n");
			sb.append("2. 編號由 0 開始且編號必須連續。 \n");
			sb.append("3. 輸入輸出測資需成對出現。 \n");
			if (matchedLocalfilePairs.size() > 0) {
				sb.append("已符合規則並成對測資如下: \n");
			}
			for (Pair<String, String> testfilePair : matchedLocalfilePairs) {
				sb.append("* " + testfilePair + "\n");
			}
			sb.append("下列您所選擇的測資檔不符合以上規則: \n");
			for (String notmatchedlocalfile : notmatchedLocalfileSet) {
				sb.append(notmatchedlocalfile + "\n");
			}
			throw new JQueryException(sb.toString());
		}

		problem.setTestfilelength(matchedLocalfilePairs.size());

		File testdataPath = ApplicationScope.getAppConfig().getTestdataPath(problem.getProblemid());
		String rsyncaccount = ApplicationScope.getAppConfig().getRsyncAccount();

		for (int index = 0; index < matchedLocalfilePairs.size(); index++) {
			if (!testdataPath.exists()) {
				FileTools.forceMkdir(testdataPath, rsyncaccount);
			}
			Pair<String, String> pair = matchedLocalfilePairs.get(index);
			File infile = new File(testdataPath, problem.getTESTDATA_FILENAME(index) + "." + SUFFIX.in);
			FileTools.writePartToFile(infile, testdataParts.get(pair.getLeft()), rsyncaccount);
			File outfile = new File(testdataPath, problem.getTESTDATA_FILENAME(index) + "." + SUFFIX.out);
			FileTools.writePartToFile(outfile, testdataParts.get(pair.getRight()), rsyncaccount);
		}

		ArrayList<TestdataPair> pairs = problem.getTestdataPairs();
		int scores[] = new int[pairs.size()];

		//
		if (pairs.size() != problem.getScores().length) {
			scores = problem.autoScoring(pairs.size());
		}

		problem.setScores(scores);

		double[] timelimits = new double[pairs.size()];
		if (pairs.size() > problem.getTimelimits().length) {
			for (int i = 0; i < pairs.size(); i++) {
				timelimits[i] = i < problem.getTimelimits().length ? problem.getTimelimits()[i]
						: defaultTimelimit(pairs.size());
			}
		} else {
			for (int i = 0; i < pairs.size(); i++) {
				timelimits[i] = problem.getTimelimits()[i];
			}
		}
		problem.setTimelimits(timelimits);

		new ProblemService().update(problem);
		new RunCommand().exec("sudo " + RunCommand.Command.dos2unix + " " + appConfig.getTestdataPath(problemid)
				+ File.separator + "*");
		new ProblemService().rsyncTestdataByProblem(problem, UserFactory.getOnlineUser(request));

	}

	/**
	 * 上傳 Special Judge 檔案
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	private void doPost_uploadSpecialJudgeFiles(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, ServletException {
		try {
			Problemid problemid = new Problemid(request.getParameter("problemid"));
			if (!problemid.getIsLegal()) {
				throw new JQueryException("problemid(" + problemid + ") 有錯誤！無法繼續。");
			}
			Problem problem = new ProblemService().getProblemByProblemid(problemid);
			problem.setSpecialjudge_code(request.getParameter("specialjudge_code"));
			problem.setSpecialjudge_language(request.getParameter("specialjudge_language"));
			AppConfig appConfig = ApplicationScope.getAppConfig();

			for (Part part : request.getParts()) {
				if ("specialjudges".equals(part.getName())) {
					String filename = this.getFilename(part);
					if (filename == null || "".equals(filename)) {
						break;
					}
					File specialpath = appConfig.getSpecialPath(problemid);
					if (!specialpath.exists()) {
						FileTools.forceMkdir(specialpath, appConfig.getRsyncAccount());
					}
					FileTools.writePartToFile(new File(specialpath, filename), part, appConfig.getRsyncAccount());
				}
			}
			if (!"".equals(problem.getSpecialjudge_code().trim())) {
				File SpecialJudgeFile = problem.getSpecialJudgeFile();
				FileTools.writeStringToFile(SpecialJudgeFile, problem.getSpecialjudge_code(),
						appConfig.getRsyncAccount());
			}

			new ProblemService().update(problem);

			new ProblemService().rsyncSpecialJudge(problem, UserFactory.getOnlineUser(request));
		} catch (DataException e) {
			e.printStackTrace();
			Alert alert = (Alert) e.getCause();
			throw new JQueryException(e.getLocalizedMessage(), alert);
		} catch (Exception e) {
			e.printStackTrace();
			new RunCommand().exec(RunCommand.Command.whoami.toString());
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

	public static enum POSTACTION {
		uploadSpecialJudgeFiles, 
		uploadTestdatas, //
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		switch (POSTACTION.valueOf(action)) {
		case uploadSpecialJudgeFiles:
			new UpdateProblemServlet().AccessFilter(request);
			doPost_uploadSpecialJudgeFiles(request, response);
			return;
		case uploadTestdatas:
			new UpdateProblemServlet().AccessFilter(request);
			try {
				doPost_uploadTestdataPairs(request, response);
			} catch (Exception e) {
				throw new JQueryException(e.getLocalizedMessage());
			}
			return;
		default:
			break;
		}
	}

}
