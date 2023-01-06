package tw.zerojudge.Servlets.Ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.TestcodeDAO;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Judges.JudgeProducer;
import tw.zerojudge.Judges.JudgeServer;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Servlets.ShowTestcodesServlet;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.Testcode;
import tw.zerojudge.Utils.ENV;

@WebServlet(urlPatterns = {"/Testcode.ajax"})
@RoleSetting
public class TestcodeAjax extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		HttpSession session = request.getSession(false);
		OnlineUser sessionUser = UserFactory.getOnlineUser(session);
		String action = request.getParameter("action");
		TestcodeDAO testcodeDao = new TestcodeDAO();
		if ("delete".equals(action) && ShowTestcodesServlet.isAccessible(sessionUser)) {
			int id = Integer.parseInt(request.getParameter("id"));
			testcodeDao.delete(id);
		} else if ("run".equals(action) && ShowTestcodesServlet.isAccessible(sessionUser)) {
			int id = Integer.parseInt(request.getParameter("id"));
			Testcode testcode = testcodeDao.getTestcodeById(id);
			String actual_status = this.doJudge(sessionUser.getAccount(), testcode);
			response.getWriter().print(actual_status);
		} else if ("readindata".equals(action) && ShowTestcodesServlet.isAccessible(sessionUser)) {
			int id = Integer.parseInt(request.getParameter("id"));
			Testcode testcode = testcodeDao.getTestcodeById(id);
			response.getWriter().print(testcode.getIndata());
		} else if ("readoutdata".equals(action) && ShowTestcodesServlet.isAccessible(sessionUser)) {
			int id = Integer.parseInt(request.getParameter("id"));
			Testcode testcode = testcodeDao.getTestcodeById(id);
			response.getWriter().print(testcode.getOutdata());
		} else if ("savecode".equals(action) && ShowTestcodesServlet.isAccessible(sessionUser)) {
			Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
			Testcode newtestcode = new Testcode();
			newtestcode.setCode(solution.getCode());
			newtestcode.setLanguage(solution.getLanguage().getName());
			newtestcode.setExpected_status(request.getParameter("expected_status"));
			String descript = request.getParameter("descript");
			descript = java.net.URLDecoder.decode(descript, "UTF-8");
			newtestcode.setDescript(descript);
			testcodeDao.insert(newtestcode);
		}
		return;
	}

	private String doJudge(String session_account, Testcode testcode) throws IOException {
		testcode.setActual_status(new Testcode().getActual_status());
		testcode.setActual_detail(new Testcode().getActual_detail());
		TestcodeDAO testcodeDao = new TestcodeDAO();
		testcodeDao.update(testcode);

		Solution newsolution = new Solution();

		newsolution.setId(testcode.getId());
		newsolution.setCode(testcode.getCode().trim() + "\n");
		newsolution.setLanguage(testcode.getLanguage());

		Problem problem = new Problem();
		problem.setTestfilelength(1);
		problem.setTimelimits(new double[]{1.0});
		problem.setMemorylimit(64);
		problem.setScores(new int[]{0});

		testcode.doCreateTestfile(newsolution.getId());

		ServerOutput[] serverOutputs;
		JudgeObject judgeObject;
		try {
			judgeObject = new JudgeObject(JudgeObject.PRIORITY.TESTCODE, newsolution, problem);
		} catch (Exception e) {
			serverOutputs = new ServerOutput[1];
			ServerOutput serverOutput = new ServerOutput();
			serverOutput.setSession_account(session_account);
			serverOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			serverOutput.setReason(ServerOutput.REASON.SYSTEMERROR);
			serverOutput.setHint("資料有誤，請檢查。" + e.getLocalizedMessage());
			serverOutputs[0] = serverOutput;
			new JudgeServer().summaryServerOutput(JudgeObject.PRIORITY.TESTCODE, problem, newsolution, serverOutputs);
			e.printStackTrace();
			return "";
		}
		JudgeProducer judgeProducer = new JudgeProducer(JudgeFactory.getJudgeServer(), judgeObject);
		Thread judgeProducerThread = new Thread(judgeProducer);
		judgeProducerThread.start();

		while (true) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Testcode testcode2 = testcodeDao.getTestcodeById(testcode.getId());
			if (!testcode2.getActual_status().equals(new Testcode().getActual_status())) {
				return testcode2.getActual_status();
			}
		}
	}
}
