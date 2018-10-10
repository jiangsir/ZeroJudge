package tw.zerojudge.Servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.UpfileDAO;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.JudgeProducer;
import tw.zerojudge.Judges.JudgeServer;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Utils.*;

@MultipartConfig(maxFileSize = 15 * 1024 * 1024)
@WebServlet(urlPatterns = { "/SubmitCode" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class SubmitCodeServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		this.AccessFilter(onlineUser, problem);
	}

	public void AccessFilter(OnlineUser onlineUser, Problem problem) throws AccessException {
		if (onlineUser == null || onlineUser.isNullOnlineUser()) {
			throw new AccessException("必須登入才能進行解題！");
		}
		if (problem == null) {
			throw new AccessException("參數不全！");
		}

		new ShowProblemServlet().AccessFilter(onlineUser, problem);
		Contest contest = new ContestService().getContestById(onlineUser.getJoinedcontestid());

		if (onlineUser.isIsInContest()) {
			if (!contest.getProblemids().contains(problem.getProblemid())) {
				throw new AccessException("您只能解競賽中的題目。");
			}
			if (!contest.isCheckedConfig_MultiSubmit()
					&& new SolutionDAO().getSolutionidsByContestProblemUser(contest, problem, onlineUser).size() > 0) {
				throw new AccessException(
						"本測驗設定「不允許」重複送出。(" + problem.getProblemid() + ", " + problem.getTitle() + ") 您已經送出過，無法再送出了。");
			}
		} else {
			if (Problem.DISPLAY.open != problem.getDisplay()) {
				throw new AccessException("題目未公開，無法解題(" + problem.getDisplay() + ")。");
			}
		}

	}

	public boolean isAccessible(OnlineUser onlineUser, Problem problem) {
		try {
			this.AccessFilter(onlineUser, problem);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		AppConfig appConfig = ApplicationScope.getAppConfig();
		request.setAttribute("compilers", appConfig.getServerConfig().getEnableCompilers());
		request.setAttribute("problem", new ProblemService().getProblemByProblemid(problemid));
		long max = this.getClass().getAnnotation(MultipartConfig.class).maxFileSize();
		String maxFileSize = max / 1024 / 1024 + "MB";
		request.setAttribute("maxFileSize", maxFileSize);
		request.getRequestDispatcher("SubmitCode.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Date lastsubmission = new SessionScope(session).getLastsubmission();

		if (lastsubmission != null && Math.abs(new Date().getTime() - lastsubmission.getTime()) < 10 * 1000) {
			throw new InfoException("請稍候再送出！");
		}
		synchronized (session) {
			new SessionScope(session).setLastsubmission(new Date());
		}

		try {
			request.getPart("Executable");
		} catch (IllegalStateException ise) {
			throw new DataException(ise);
		}

		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		Solution newsolution = new Solution();
		String code = request.getParameter("code");
		String language = request.getParameter("language");

		Contest contest = new ContestService().getContestById(onlineUser.getJoinedcontestid());
		if (onlineUser.isIsInContest() && !contest.getIsRunning()) {
			throw new DataException("測驗/競賽已結束，無法再送出囉!");
		}

		newsolution.setPid(problem.getId());
		newsolution.setUserid(onlineUser.getId());

		if (onlineUser.isIsInVClass()) {
			VClass vclass = new VClassDAO().getVClassById(onlineUser.getVclassid());
			if (vclass.getProblemids().contains(problemid.toString())) {
				newsolution.setVclassid(onlineUser.getVclassid());
			}
		}

		newsolution.setLanguage(language);

		newsolution.setContestid(onlineUser.getJoinedcontestid());
		newsolution.setIpfrom(new Utils().getIpList(request));

		if (newsolution.getContestid() > 0) { 
			newsolution.setVisible(Solution.VISIBLE.incontest);

			newsolution.setCode(code);
			newsolution.setId(new SolutionService().insert(newsolution));
			if (contest.isCheckedConfig(Contest.CONFIG.UploadExefile)) {
				Part part = request.getPart("Executable");
				String header = part.getHeader("Content-Disposition");
				String filename = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
				InputStream in = part.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int len;
				byte[] buffer = new byte[1024];
				while ((len = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, len);
				}
				in.close();
				out.flush();
				out.close();

				Upfile newupfile = new Upfile();
				newupfile.setFilename(filename);
				newupfile.setFiletype(part.getContentType());
				newupfile.setBytes(out.toByteArray());
				newupfile.setIpfrom(request.getRemoteAddr());
				newupfile.setSolutionid(newsolution.getId());
				new UpfileDAO().insert(newupfile);
			}

			this.submitToJudge(onlineUser, newsolution, session);
			response.sendRedirect(request.getContextPath()
					+ ContestSubmissionsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0] + "?contestid="
					+ newsolution.getContestid());
			return;
		} else { 
			newsolution.setCode(code);

			newsolution.setId(new SolutionService().insert(newsolution));

			this.submitToJudge(onlineUser, newsolution, session);
			response.sendRedirect(request.getContextPath()
					+ SubmissionsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		}
	}

	public void submitToJudge(OnlineUser onlineUser, Solution solution, HttpSession session) throws DataException {

		ServerOutput[] serverOutputs;
		JudgeObject judgeObject;
		try {
			judgeObject = new JudgeObject(JudgeObject.PRIORITY.Submit, solution, solution.getProblem());
		} catch (Exception e) {
			serverOutputs = new ServerOutput[1];
			ServerOutput serverOutput = new ServerOutput();
			serverOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			serverOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			serverOutput.setHint("資料有誤，請檢查。" + e.getLocalizedMessage());
			serverOutputs[0] = serverOutput;
			new JudgeServer().summaryServerOutput(JudgeObject.PRIORITY.Submit, solution.getProblem(), solution,
					serverOutputs);
			e.printStackTrace();
			return;
		}
		JudgeProducer judgeProducer = new JudgeProducer(JudgeFactory.getJudgeServer(), judgeObject);
		Thread judgeProducerThread = new Thread(judgeProducer);
		judgeProducerThread.start();
	}

}
