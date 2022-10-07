package tw.zerojudge.Servlets;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.*;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Servlets.*;

@WebServlet(urlPatterns = { "/Application" })
public class ApplicationServlet extends ZJServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void AccessFilter(HttpServletRequest request) {

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		request.setAttribute("Threshold", ApplicationScope.getAppConfig().getThreshold());
//		request.getRequestDispatcher("Application.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if ("applyTeacher".equals(action)) {
			if (!ApplicationScope.getAppConfig().isChecked_applyTeacher()) {
				throw new JQueryException("系統未開放本權限！");
			}
			doPost_applyTeacher(request, response);
		} else if ("applyProblemManager".equals(action)) {
			if (!ApplicationScope.getAppConfig().isChecked_applyProblemManager()) {
				throw new JQueryException("系統未開放本權限！");
			}
			doPost_applyProblemManager(request, response);
		} else {
			throw new AccessException("未知行為！");
		}
	}

	private void doPost_applyProblemManager(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		int openedProblemCount = new ProblemDAO().getCountOfOpenedProblem();
		AppConfig appConfig = ApplicationScope.getAppConfig();

		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		int index = new Utils().indexOf(appConfig.getCandidateManager().split(","), onlineUser.getAccount());
		double d = (double) onlineUser.getAc() / (double) openedProblemCount;
		BigDecimal b = new BigDecimal(d);
		double rate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (index < 0 && rate < appConfig.getThreshold()) {
			throw new JQueryException("很抱歉，由於您的解題數尚未達到出題者的門檻 " + (int) (appConfig.getThreshold() * 100)
					+ "%。<br>若您是教師並急用來考試，亦可直接 " + "Email 或即時訊息與「站務管理員」聯繫，將由手動處理");
		}

		onlineUser.enableConfig(User.CONFIG.ContestManager);
		onlineUser.enableConfig(User.CONFIG.InsertProblem);
		onlineUser.enableConfig(User.CONFIG.ProblemManager);
		onlineUser.enableConfig(User.CONFIG.QualifiedAuthor);

		new UserService().update(onlineUser);

		throw new JQueryException("歡迎您成為出題者！<br>在您的右上角個人選單下會增加相關權限。<br>");

	}

	private void doPost_applyTeacher(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
//		ContestManager, 
//		InsertProblem, 
//		ProblemManager, 
//		VClassManager, 
		onlineUser.enableConfig(User.CONFIG.ContestManager);
		onlineUser.enableConfig(User.CONFIG.InsertProblem);
		onlineUser.enableConfig(User.CONFIG.ProblemManager);
		onlineUser.enableConfig(User.CONFIG.VClassManager);
		new UserService().update(onlineUser);

		throw new JQueryException("歡迎您成為開課教師！<br>在您的右上角個人選單下會增加相關權限。<br>");

	}

}
