package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = {"/Submissions"})
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class SubmissionsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum PARAM {
		account, //
		status, //
		problemid, //
		language, //
		solutionid, //
		page;
	}

	private enum ATTR {
		pagenum, //
		solutions, //
		querystring;//
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String account = request.getParameter(PARAM.account.name());
		String status = request.getParameter(PARAM.status.name());
		ServerOutput.JUDGEMENT judgement = null;
		if (status != null) {
			judgement = ServerOutput.JUDGEMENT.valueOf(status);
		}
		Problemid problemid = new Problemid(request.getParameter(PARAM.problemid.name()));
		String language = request.getParameter(PARAM.language.name());

		Integer solutionid = Solution.parseSolutionid(request.getParameter(PARAM.solutionid.name()));

		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter(PARAM.page.name()));
		} catch (NumberFormatException e) {
		}
		request.setAttribute(ATTR.pagenum.name(), page);

		if (account != null && (problemid != null && problemid.getIsLegal()) && judgement != null) {
			request.setAttribute(ATTR.solutions.name(),
					new SolutionService().getSolutions(account, judgement, problemid, page));
		} else if (account != null && (problemid != null && problemid.getIsLegal())) {
			request.setAttribute(ATTR.solutions.name(),
					new SolutionService().getSolutionsByPidUserid(account, problemid, page));
		} else if ((problemid != null && problemid.getIsLegal()) && judgement != null) {
			request.setAttribute(ATTR.solutions.name(),
					new SolutionService().getSolutionsByProblemidJudgement(problemid, judgement, page));
		} else if ((problemid != null && problemid.getIsLegal()) && language != null) {
			request.setAttribute(ATTR.solutions.name(), new SolutionService().getSolutionsByProblemidLanguage(problemid,
					new Language(language, language.toLowerCase()), page));
		} else if (account != null && judgement != null) {
			if (judgement == ServerOutput.JUDGEMENT.AC) {
				request.setAttribute(ATTR.solutions.name(), new SolutionService().getSolutionsByUseridJudgement(
						new UserService().getUserByAccount(account).getId(), judgement, page));
			} else {
				request.setAttribute(ATTR.solutions.name(), new SolutionService().getSolutionsByUseridJudgement(
						new UserService().getUserByAccount(account).getId(), judgement, page));
			}
		} else if (account != null) { 
			request.setAttribute(ATTR.solutions.name(), new SolutionService()
					.getSolutionsByUserid(new UserService().getUserByAccount(account).getId(), page));
		} else if (problemid != null && problemid.getIsLegal()) {
			request.setAttribute(ATTR.solutions.name(), new SolutionService().getSolutionsByProblemid(problemid, page));
		} else if (judgement != null) { 
			request.setAttribute(ATTR.solutions.name(), new SolutionService().getSolutionsByJudgement(judgement, page));
		} else if (language != null && !"".equals(language)) {
			request.setAttribute(ATTR.solutions.name(),
					new SolutionService().getSolutionsByLanguage(new Language(language, language.toLowerCase()), page));
		} else if (solutionid != 0) { 
			int pagenum = new SolutionService().getPageNum(solutionid);
			request.setAttribute(ATTR.solutions.name(), new SolutionService().getSolutions(pagenum));
		} else { 

			request.setAttribute(ATTR.solutions.name(), new SolutionService().getSolutions(page));
		}
		request.setAttribute(ATTR.querystring.name(), StringTool.querystingMergeFirst(request.getQueryString()));
		request.getRequestDispatcher("Submissions.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
