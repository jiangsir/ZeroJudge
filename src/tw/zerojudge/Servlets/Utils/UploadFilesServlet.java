package tw.zerojudge.Servlets.Utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Judges.ServerInput;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Objects.TestdataPair;
import tw.zerojudge.Objects.Testdatafile.SUFFIX;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User.ROLE;

@MultipartConfig(maxFileSize = 100 * 1024 * 1024, maxRequestSize = 500 * 1024 * 1024)
@WebServlet(urlPatterns = {"/UploadFiles.api"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class UploadFilesServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		new UpdateProblemServlet().AccessFilter(request);
	}

	public String getFilename(Part part) {
		String header = part.getHeader("Content-Disposition");
		String filename = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
		return filename;
	}

	/**
	 * 上傳測資檔
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	private void doPost_uploadTestdataPairs(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, ServletException {
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		String infilepattern = request.getParameter("infilepattern");
		String outfilepattern = request.getParameter("outfilepattern");
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		HashMap<String, Part> testdataParts = new HashMap<String, Part>();
		ArrayList<String> notmatch = new ArrayList<String>();
		Part testdataPart = request.getPart("testdatas");
		if (this.getFilename(testdataPart) == null || "".equals(this.getFilename(testdataPart))) {
			throw new JQueryException("您未選取任何檔案，沒有檔案可以上傳！");
		}
		for (Part part : request.getParts()) {
			if ("testdatas".equals(part.getName())) {
				String filename = this.getFilename(part);
				if (filename != null && !"".equals(filename.trim())
						&& (filename.matches(infilepattern.replaceAll("#", "[0-9]"))
								|| filename.matches(outfilepattern.replaceAll("#", "[0-9]")))) {
					testdataParts.put(filename, part);
				} else {
					notmatch.add(filename);
				}
			}
		}
		if (notmatch.size() > 0) {
			StringBuffer sb = new StringBuffer(5000);
			for (String filename : notmatch) {
				sb.append("不符合的檔名：" + filename + "\n");
			}
			for (String key : testdataParts.keySet()) {
				sb.append("符合的檔名：" + key + "\n");
			}
			throw new JQueryException("有部分檔名與不符合 pattern，測資檔上傳失敗。\n" + "pattern = (" + infilepattern + ", "
					+ outfilepattern + ")\n" + sb.toString());
		}

		DecimalFormat formatter = new DecimalFormat("00");
		for (int index = 0; index < problem.getTestdataPairs().size() + testdataParts.size() / 2; index++) {
			String infilename = infilepattern.replaceAll("##", formatter.format(index));
			String outfilename = outfilepattern.replaceAll("##", formatter.format(index));
			if (testdataParts.containsKey(infilename) && testdataParts.containsKey(outfilename)) {
				File testdataPath = appConfig.getTestdataPath(problemid);
				if (!testdataPath.exists()) {
					testdataPath.mkdirs();
				}
				testdataParts.get(infilename)
						.write(new File(testdataPath, problem.getTESTDATA_FILENAME(index) + "." + SUFFIX.in).getPath());
				testdataParts.get(outfilename).write(
						new File(testdataPath, problem.getTESTDATA_FILENAME(index) + "." + SUFFIX.out).getPath());
				problem.setTestfilelength(index + 1);
			} else {
				continue;
			}
		}
		ArrayList<TestdataPair> pairs = problem.getTestdataPairs();
		int scores[] = new int[pairs.size()];


		if (pairs.size() != problem.getScores().length) {
			scores = problem.autoScoring(pairs.size());
		}

		problem.setScores(scores);

		double[] timelimits = new double[pairs.size()];
		if (pairs.size() > problem.getTimelimits().length) {
			for (int i = 0; i < pairs.size(); i++) {
				timelimits[i] = i < problem.getTimelimits().length ? problem.getTimelimits()[i] : 1.0;
			}
		} else if (pairs.size() < problem.getTimelimits().length) {
			for (int i = 0; i < pairs.size(); i++) {
				timelimits[i] = problem.getTimelimits()[i];
			}
		}
		problem.setTimelimits(timelimits);

		new ProblemService().update(problem);
		new RunCommand()
				.exec(RunCommand.Command.dos2unix + " " + appConfig.getTestdataPath(problemid) + File.separator + "*");
		new ProblemService().rsyncTestdataByProblem(problem);

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
						if (!specialpath.mkdirs()) {
							throw new DataException("無法建立目錄，請檢查權限。" + specialpath);
						}
					}
					part.write(specialpath + File.separator + filename);
				}
			}
			if (!"".equals(problem.getSpecialjudge_code().trim())) {
				File SpecialJudgeFile = problem.getSpecialJudgeFile();
				FileUtils.writeStringToFile(SpecialJudgeFile, problem.getSpecialjudge_code());
				FileUtils.touch(SpecialJudgeFile);
			}

			new ProblemService().update(problem);

			new ProblemService().rsyncSpecialJudge(problem);
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
			case uploadSpecialJudgeFiles :
				new UpdateProblemServlet().AccessFilter(request);
				doPost_uploadSpecialJudgeFiles(request, response);
				return;
			case uploadTestdatas :
				new UpdateProblemServlet().AccessFilter(request);
				doPost_uploadTestdataPairs(request, response);
				return;
			default :
				break;
		}
	}

}
