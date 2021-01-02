package tw.zerojudge.Servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.JudgeProducer;
import tw.zerojudge.Judges.JudgeServer;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
@WebServlet(urlPatterns = { "/Testjudge" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class TestjudgeServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		this.AccessFilter(onlineUser, contest);
	}

	public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (onlineUser == null || onlineUser.isNullOnlineUser()) {
			throw new AccessException("必須登入才能進行“測試執行”");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		ServerOutput[] serverOutputs;

		int contestid = 0;
		try {
			contestid = Integer.parseInt(request.getParameter("contestid"));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}

		if (isLatter(session)) {
			serverOutputs = new ServerOutput[1];
			serverOutputs[0] = new ServerOutput();
			serverOutputs[0].setJudgement(ServerOutput.JUDGEMENT.SE);
			serverOutputs[0].setHint("請稍候再送出!");

			int queuesize = ApplicationScope.getAppConfig().getJudgeQueueSize();
			if (queuesize > 50) {
				throw new JQueryException("目前待評分程式眾多，請稍候再送出!");
			} else {
				throw new JQueryException("請稍候再送出!");
			}
		}

		Problemid problemid = new Problemid(request.getParameter("problemid"));

		String testcode = request.getParameter("testcode");
		String[] testjudge_indatas = request.getParameterValues("testjudge_indata");
		String[] testjudge_outdatas = request.getParameterValues("testjudge_outdata");
		String language = request.getParameter("language");

		onlineUser.setUserlanguage(language);
		List<IpAddress> iplist = new Utils().getIpList(request);
		try {
			serverOutputs = this.doTestjudge(onlineUser, contestid, problemid, testcode, testjudge_indatas,
					testjudge_outdatas, language, iplist);
			SessionScope sessionScope = new SessionScope(session);
			sessionScope.setLastsubmission(new Date());

			new UserService().update(onlineUser);
			new UserService().doReload(request.getSession(false));
		} catch (DataException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
		try {
			response.getWriter().print(mapper.writeValueAsString(serverOutputs));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		}
	}

	private ServerOutput[] doTestjudge(OnlineUser onlineUser, int contestid, Problemid problemid, String code,
			String[] testjudge_indatas, String[] testjudge_outdatas, String language, List<IpAddress> iplist) {


		int max_testjudge_datalength = 2000;
		if (Arrays.toString(testjudge_indatas).getBytes().length > max_testjudge_datalength
				|| Arrays.toString(testjudge_outdatas).getBytes().length > max_testjudge_datalength) {
			throw new DataException("輸出入測資超過上限 " + max_testjudge_datalength + " bytes. ，請重新決定測資。");
		}

		Solution testsolution = null;
		Problem testproblem = null;
		testsolution = this.makeTestSolution(onlineUser, code, language, iplist, contestid, problemid);
		testproblem = this.makeTestProblem(testsolution.getId(), language, testjudge_indatas, testjudge_outdatas);
		ServerOutput[] serverOutputs;
		ServerOutput serverOutput = new ServerOutput();
		JudgeObject judgeObject;
		try {
			judgeObject = new JudgeObject(JudgeObject.PRIORITY.Testjudge, testsolution, testproblem);
		} catch (Exception e) {
			serverOutputs = new ServerOutput[1];
			serverOutput.setSession_account(onlineUser.getAccount());
			serverOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			serverOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			serverOutput.setHint("資料有誤，請檢查。" + e.getLocalizedMessage());
			serverOutputs[0] = serverOutput;
			new JudgeServer().summaryServerOutput(JudgeObject.PRIORITY.Testjudge, testproblem, testsolution,
					serverOutputs);
			e.printStackTrace();
			return serverOutputs;
		}
		JudgeProducer judgeProducer = new JudgeProducer(JudgeFactory.getJudgeServer(), judgeObject);
		Thread judgeProducerThread = new Thread(judgeProducer);
		judgeProducerThread.start();

		serverOutputs = new ServerOutput[1];
		serverOutput.setSolutionid(testsolution.getId());
		serverOutput.setJudgement(ServerOutput.JUDGEMENT.Waiting);
		serverOutputs[0] = serverOutput;
		return serverOutputs;
	}

	private Solution makeTestSolution(OnlineUser onlineUser, String code, String language, List<IpAddress> iplist,
			int contestid, Problemid problemid) {
		Solution newsolution = new Solution();
		newsolution.setUserid(onlineUser.getId());
		newsolution.setCode(code);
		newsolution.setLanguage(language);
		newsolution.setContestid(contestid);
		newsolution.setPid(new ProblemService().getProblemByProblemid(problemid).getId());
		newsolution.setIpfrom(iplist);
		newsolution.setSubmittime(new Timestamp(new Date().getTime()));
		newsolution.setVisible(Solution.VISIBLE.testjudge);
		int solutionid = new SolutionService().insert(newsolution);
		newsolution.setId(solutionid);
		return newsolution;
	}

	/**
	 * 是否請使用者「稍候再送出」for testjudge
	 * 
	 * @return
	 */
	private boolean isLatter(HttpSession session) {
		Date lastsubmission = new SessionScope(session).getLastsubmission();
		int queuesize = ApplicationScope.getAppConfig().getJudgeQueueSize();
		int maxwait = 60 * 1000; 
		int wait = (10 + queuesize / 10 * 5) * 1000;
		if (lastsubmission != null
				&& Math.abs(new Date().getTime() - lastsubmission.getTime()) < Math.min(wait, maxwait))
			return true;
		else
			return false;
	}

	/**
	 * testjudge 時，把 testjudge_indata, testjudge_outdata 裝載在 problem 當中。
	 * 
	 * @param solutionid
	 * @param language
	 * @param testjudge_indata
	 * @param testjudge_outdata
	 * @return
	 */
	private Problem makeTestProblem(int solutionid, String language, String[] testjudge_indatas,
			String[] testjudge_outdatas) {
		Problem testproblem = new Problem();
		int testfilelength = Math.min(testjudge_indatas.length, testjudge_outdatas.length);
		testproblem.setTestfilelength(testfilelength);
		try {
			testproblem.setSampleinput(mapper.writeValueAsString(testjudge_indatas));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			testproblem.setSampleinput("testjudge_indata mapper ERROR!!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			testproblem.setSampleinput("testjudge_indata mapper ERROR!!");
		} catch (IOException e) {
			e.printStackTrace();
			testproblem.setSampleinput("testjudge_indata mapper ERROR!!");
		}
		try {
			testproblem.setSampleoutput(mapper.writeValueAsString(testjudge_outdatas));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			testproblem.setSampleoutput("testjudge_outdata mapper ERROR!!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			testproblem.setSampleoutput("testjudge_outdata mapper ERROR!!");
		} catch (IOException e) {
			e.printStackTrace();
			testproblem.setSampleoutput("testjudge_outdata mapper ERROR!!");
		}
		testproblem.setLanguage(language);
		testproblem.setMemorylimit(64);

		double[] timelimits = new double[testfilelength];
		Arrays.fill(timelimits, 1.0);
		testproblem.setTimelimits(timelimits);

		int[] scores = new int[testfilelength];
		int total = 100;
		for (int i = 0; i < scores.length; i++) {
			scores[i] = total / scores.length + (total % scores.length > i ? 1 : 0);
		}
		testproblem.setScores(scores);

		return testproblem;
	}

}
