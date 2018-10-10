package tw.zerojudge.Api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.JsonObjects.Redirect;
import tw.zerojudge.JsonObjects.ServerOutputBean;
import tw.zerojudge.JsonObjects.Summary;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Judges.ServerOutput.REASON;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.ContestSubmissionsServlet;
import tw.zerojudge.Servlets.ManualJudgeServlet;
import tw.zerojudge.Servlets.ShowCodeServlet;
import tw.zerojudge.Servlets.ShowDetailsServlet;
import tw.zerojudge.Servlets.SubmissionsServlet;
import tw.zerojudge.Servlets.SubmitCodeServlet;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Utils.Utils;

@WebServlet(urlPatterns = { "/Solution.api" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class SolutionApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	public static enum POST_ACTION {
		SolutionStatusInfo, 
		DetailDialog, 
		SubmitCode, 
		getImgCodelocker, //
		getCode, //
		getDetails, //
		getServerOutputBeans, //
		setServerOutput, 
		getSummary, //
		removeSolution, //
		recoverSolution, //
		canSubmitCode, 
		getStatusinfo; 
	}

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		String action = request.getParameter("action");
		switch (POST_ACTION.valueOf(action)) {
		case SolutionStatusInfo:
			break;
		case DetailDialog:
			break;
		case SubmitCode:
			new SubmitCodeServlet().AccessFilter(request);
			break;
		case getCode:
			new ShowCodeServlet().AccessFilter(request);
			break;
		case getDetails:
			break;
		case getImgCodelocker:
			break;
		case getServerOutputBeans:
			new ShowDetailsServlet().AccessFilter(UserFactory.getOnlineUser(session),
					new SolutionService().getSolutionById(request.getParameter("solutionid")));
			break;
		case getSummary:
			break;
		case recoverSolution:
			break;
		case removeSolution:
			break;
		case setServerOutput:
			break;
		case canSubmitCode:
			break;
		default:
			break;
		}
	}

	private ServerOutput[] getResource(Locale locale, ServerOutput[] serverOutputs, int wa_visible) {
		for (ServerOutput serverOutput : serverOutputs) {
			if (wa_visible == Problem.WA_visible_HIDE && serverOutput.getJudgement() == ServerOutput.JUDGEMENT.WA) {
				serverOutput.setHint("本題目測資設定為「不公開」");
			}
		}
		return serverOutputs;
	}

	private String getDetail_solution(int solutionid) {
		String detail = "";
		Solution solution = new SolutionService().getSolutionById(solutionid);
		Problem problem = solution.getProblem();
		if (solution.getJudgement() == ServerOutput.JUDGEMENT.NA
				&& problem.getWa_visible().intValue() == Problem.WA_visible_HIDE) {
			String[] lines = solution.getDetails().split("\n");
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].startsWith("***")) {
					detail += lines[i] + "\n";
				}
			}
		} else {
			detail = solution.getDetails();
		}
		return detail;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String action = request.getParameter("action");
		try {
			Method method = this.getClass().getMethod("doPost_" + POST_ACTION.valueOf(action),
					new Class[] { HttpServletRequest.class, HttpServletResponse.class });
			method.invoke(this, new Object[] { request, response });
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new JQueryException(new Alert(e.getTargetException()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}


	public void doPost_SolutionStatusInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		request.setAttribute("solution", solution);
		request.getRequestDispatcher("include/div/DivSolutionStatusInfo.jsp").forward(request, response);
		return;
	}

	public void doPost_DetailDialog(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		request.setAttribute("serveroutputs", solution.getServeroutputs());
		request.getRequestDispatcher("include/dialog/ShowDetail.jsp").forward(request, response);
		return;
	}

	public void doPost_SubmitCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
			Date lastsubmission = new SessionScope(request).getLastsubmission();
			if (lastsubmission != null && Math.abs(System.currentTimeMillis() - lastsubmission.getTime()) < 10 * 1000) {
				throw new JQueryException("請稍候再送出！");
			}
			HttpSession session = request.getSession(false);
			synchronized (session) {
				new SessionScope(session).setLastsubmission(new Date());
			}
			Problemid problemid = new Problemid(request.getParameter("problemid"));
			Problem problem = new ProblemService().getProblemByProblemid(problemid);

			Solution newsolution = new Solution();
			String code = request.getParameter("code");
			String language = request.getParameter("language");

			Contest contest = new ContestService().getContestById(onlineUser.getJoinedcontestid());
			if (onlineUser.isIsInContest() && !contest.getIsRunning()) {
				throw new JQueryException("測驗/競賽已結束，無法再送出囉!");
			}

			newsolution.setPid(problem.getId());
			newsolution.setUserid(onlineUser.getId());
			if (onlineUser.isIsInVClass()) {
				VClass vclass = new VClassDAO().getVClassById(onlineUser.getVclassid());
				if (vclass.getProblemids().contains(problemid)) {
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

				new SubmitCodeServlet().submitToJudge(onlineUser, newsolution, session);
				response.getWriter()
						.print(mapper
								.writeValueAsString(
										new Redirect(request.getContextPath()
												+ ContestSubmissionsServlet.class.getAnnotation(WebServlet.class)
														.urlPatterns()[0]
												+ "?contestid=" + newsolution.getContestid())));
				return;
			} else { 
				newsolution.setCode(code);
				newsolution.setId(new SolutionService().insert(newsolution));
				new SubmitCodeServlet().submitToJudge(onlineUser, newsolution, session);
				response.getWriter().print(mapper.writeValueAsString(new Redirect(request.getContextPath()
						+ SubmissionsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0])));
				return;
			}
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	public void doPost_getImgCodelocker(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		response.getWriter().print(solution.getImgCodelocker());
	}

	public void doPost_getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		if (!onlineUser.getAccount().equals(solution.getUser().getAccount())
				&& solution.getCodelocker() == Solution.CODELOCKER_CLOSE) {
			Log log = new Log(request);
			log.setTabid(Log.TABID.WATCHING);
			log.setMessage("成功取得他人關閉的程式碼 onlineUser=" + onlineUser.getAccount() + ", owner="
					+ solution.getUser().getAccount() + ", solutionid=" + solution.getId());
			new LogDAO().insert(log);
		}
		response.getWriter().print(solution.getCode_prefix() + "\n" + solution.getCode());
	}

	public void doPost_getDetails(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		HttpSession session = request.getSession(false);
		ResourceBundle resource = ResourceBundle.getBundle("resource", new SessionScope(session).getSession_locale());
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		String details = "";
		if (solution.getId() != 0 && problemid.getIsEmpty()) { 
			new ShowDetailsServlet().AccessFilter(UserFactory.getOnlineUser(session), solution);

			details = this.getDetail_solution(solution.getId());
		} else if (solution.getId() == 0 && !problemid.getIsEmpty()) { 
			problem = new ProblemService().getProblemByProblemid(problemid);

			new UpdateProblemServlet().AccessFilter(UserFactory.getOnlineUser(session), problem);

			details = mapper.writeValueAsString(problem.getServeroutputs());
		} else {
		}
		response.getWriter().print(details);
	}

	public void doPost_getServerOutputBeans(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));

		if (solution.getServeroutputs().length == 0) {
			ServerOutput serverOutput = new ServerOutput();
			serverOutput.setAccount(solution.getAccount());
			serverOutput.setJudgement(solution.getJudgement());
			serverOutput.setReason(REASON.OLD_STYLE_SOLUTION_DETAILS);
			serverOutput.setScore(solution.getScore());
			serverOutput.setHint(solution.getDetails());
			ServerOutputBean serverOutputBean = new ServerOutputBean(serverOutput);
			ArrayList<ServerOutputBean> serverOutputBeans = new ArrayList<ServerOutputBean>();
			serverOutputBeans.add(serverOutputBean);
			response.getWriter().print(mapper.writeValueAsString(serverOutputBeans));
			return;
		}
		try {
			response.getWriter().print(mapper.writeValueAsString(solution.getServerOutputBeans()));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new DataException(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new DataException(e.getLocalizedMessage());
		}
	}

	public void doPost_setServerOutput(HttpServletRequest request, HttpServletResponse response) {
		new ManualJudgeServlet().AccessFilter(request);

		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);

		ServerOutput.JUDGEMENT verdict = ServerOutput.JUDGEMENT.valueOf(request.getParameter("manualjudge_verdict"));
		String hint = request.getParameter("manualjudge_hint");
		int score = 0;
		if (verdict == ServerOutput.JUDGEMENT.AC) {
			problem = solution.getProblem();
			for (int pscore : problem.getScores()) {
				score += pscore;
			}
		} else if (verdict == ServerOutput.JUDGEMENT.NA) {
			score = Parameter.parseInteger(request.getParameter("manualjudge_score").trim());
		} else if (verdict == null) {
			return;
		}
		ServerOutput[] serverOutputs = new ServerOutput[1];
		serverOutputs[0] = new ServerOutput();
		serverOutputs[0].setJudgement(verdict);
		serverOutputs[0].setInfo("score:" + score);
		serverOutputs[0].setScore(score);
		serverOutputs[0].setReason(ServerOutput.REASON.MANUAL_JUDGE);
		serverOutputs[0].setHint(hint);
		serverOutputs[0].setServername("Manual");
		solution.setServeroutputs(serverOutputs);
		solution.setJudgement(serverOutputs[0].getJudgement());
		solution.setScore(score);
		SolutionDAO solutionDao = new SolutionDAO();
		new SolutionService().update(solution);
		solutionDao.rebuiltStatistic(JudgeObject.PRIORITY.MANUALJUDGE, solution, verdict);
	}

	public void doPost_getSummary(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		Summary summary = new Summary();
		summary.setJudgement(solution.getJudgement());
		summary.setSummary(solution.getSummary());
		summary.setScore(solution.getScore());
		summary.setAccessible(onlineUser.canShowDetails(solution));
		try {
			response.getWriter().print(mapper.writeValueAsString(summary));
		} catch (JsonGenerationException e) {
			response.getWriter().print("");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			response.getWriter().print("");
			e.printStackTrace();
		}
	}

	public void doPost_removeSolution(HttpServletRequest request, HttpServletResponse response) {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		if (!onlineUser.isCanRemoveSolution()) {
			return;
		}

		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		Contest contest = new ContestService().getContestById(solution.getContestid());
		TreeSet<Integer> removedSolutionids = contest.getRemovedsolutionids();
		removedSolutionids = contest.getRemovedsolutionids();
		removedSolutionids.add(solution.getId());
		contest.setRemovedsolutionids(removedSolutionids);
		new ContestService().update(contest);

		TreeSet<Integer> solutionids = new ContestService().getSolutionidsByContest(contest);
		solutionids = new ContestService().getSolutionidsByContest(contest);
		solutionids.remove(solution.getId());


		new SolutionDAO().rebuiltStatistic(JudgeObject.PRIORITY.MANUALJUDGE, solution, solution.getJudgement());
	}

	public void doPost_recoverSolution(HttpServletRequest request, HttpServletResponse response) {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

		if (!onlineUser.isCanRemoveSolution()) {
			return;
		}

		Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
		Contest contest = new ContestService().getContestById(solution.getContestid());
		TreeSet<Integer> removedSolutionids = contest.getRemovedsolutionids();
		removedSolutionids.remove(solution.getId());
		contest.setRemovedsolutionids(removedSolutionids);
		new ContestService().update(contest);

		TreeSet<Integer> solutionids = new ContestService().getSolutionidsByContest(contest);
		solutionids.add(solution.getId());

		new SolutionDAO().rebuiltStatistic(JudgeObject.PRIORITY.MANUALJUDGE, solution, solution.getJudgement());
	}

	public void doPost_canSubmitCode(HttpServletRequest request, HttpServletResponse response) {
		new SubmitCodeServlet().AccessFilter(request);
		return;
	}

	public void doPost_getStatusinfo(HttpServletRequest request, HttpServletResponse response) {

	}

}
