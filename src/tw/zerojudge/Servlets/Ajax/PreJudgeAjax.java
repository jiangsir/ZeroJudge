package tw.zerojudge.Servlets.Ajax;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/PreJudge.ajax" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class PreJudgeAjax extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		try {
			new UpdateProblemServlet().AccessFilter(request);
		} catch (AccessException e) {
			e.printStackTrace();
			throw new RoleException("您沒有權限進行前測");
		}
	}


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		problem.setPrejudgement(ServerOutput.JUDGEMENT.Waiting);
		String code = request.getParameter("code");
		if (code != null && !"".equals(code.trim())) {
			problem.setSamplecode(code);
		}

		new ProblemService().update(problem);
		new ProblemService().doPreJudge(onlineUser, problem);

		response.getWriter().print(ServerOutput.JUDGEMENT.Waiting);
	}

}
