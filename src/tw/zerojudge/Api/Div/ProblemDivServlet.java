package tw.zerojudge.Api.Div;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/Problem.div" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ProblemDivServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	private static enum GETACTION {
		getSpecialJudgeFiles;
	}

	@Override
	public void AccessFilter(HttpServletRequest request)
			throws AccessException {
		if (request.getMethod().equals("GET")) {
			String action = request.getParameter("action");
			switch (GETACTION.valueOf(action)) {
			case getSpecialJudgeFiles:
				new UpdateProblemServlet().AccessFilter(request);
				break;
			default:
				break;
			}
		} else if (request.getMethod().equals("POST")) {
			String action = request.getParameter("action");
			switch (POSTACTION.valueOf(action)) {
			default:
				break;
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		Problemid problemid = new Problemid(request.getParameter("problemid"));
		Problem problem = new ProblemService().getProblemByProblemid(problemid);
		switch (GETACTION.valueOf(action)) {
		case getSpecialJudgeFiles:
			request.setAttribute("problem", problem);
			request.getRequestDispatcher("include/div/SpecialJudgeFiles.jsp")
					.forward(request, response);
			return;
		default:
			return;
		}
	}

	private static enum POSTACTION {
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		switch (POSTACTION.valueOf(action)) {

		default:
			break;
		}

	}
}
