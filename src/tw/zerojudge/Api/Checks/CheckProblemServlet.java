package tw.zerojudge.Api.Checks;

import java.io.IOException;
import java.util.LinkedHashSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Exceptions.CheckCause;
import tw.zerojudge.Exceptions.CheckException;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/CheckProblem.api" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class CheckProblemServlet extends HttpServlet {

	private static final long serialVersionUID = -1336123130335663471L;
	ObjectMapper mapper = new ObjectMapper(); 

	public static enum ACTION {
		Insert, Update
	};

	public void checkInsert(HttpServletRequest request) throws CheckException {
		LinkedHashSet<String> checkSet = new LinkedHashSet<String>();

		Problem problem = new Problem();
		try {
			problem.setProblemid(new Problemid(request
					.getParameter("problemid")));
		} catch (DataException e) {
			e.printStackTrace();
			checkSet.add(e.getLocalizedMessage());
		}
		try {
			problem.setTabid(request.getParameter("tabid"));
		} catch (DataException e) {
			e.printStackTrace();
			checkSet.add(e.getLocalizedMessage());
		}
		try {
			problem.setLanguage(request.getParameter("language"));
		} catch (DataException e) {
			e.printStackTrace();
			checkSet.add(e.getLocalizedMessage());
		}
		try {
			problem.setMemorylimit(request.getParameter("memorylimit"));
		} catch (DataException e) {
			e.printStackTrace();
			checkSet.add(e.getLocalizedMessage());
		}

		try {
			problem.setTitle(request.getParameter("title"));
		} catch (DataException e) {
			e.printStackTrace();
			checkSet.add(e.getLocalizedMessage());
		}
		try {
			problem.setContent(request.getParameter("content"));
		} catch (DataException e) {
			e.printStackTrace();
			checkSet.add(e.getLocalizedMessage());
		}

		if (checkSet.size() > 0) {
			throw new CheckException("部分參數不正確！", checkSet);
		}
	}

	public void checkUpdate(HttpServletRequest request) throws CheckException {
		this.checkInsert(request);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		switch (ACTION.valueOf(action)) {
		case Insert:
			try {
				checkInsert(request);
			} catch (CheckException e1) {
				e1.printStackTrace();
				CheckCause checkCause = (CheckCause) e1.getCause();
				response.getWriter().print(
						mapper.writeValueAsString(checkCause.getChecks()));
				return;
			}
			response.getWriter().print(
					mapper.writeValueAsString(new LinkedHashSet<String>()));
			return;
		case Update:
			try {
				checkUpdate(request);
			} catch (CheckException e) {
				e.printStackTrace();
				CheckCause checkCause = (CheckCause) e.getCause();
				response.getWriter().print(
						mapper.writeValueAsString(checkCause.getChecks()));
				return;
			}
			response.getWriter().print(
					mapper.writeValueAsString(new LinkedHashSet<String>()));
			return;
		default:
			break;
		}
		return;
	}
}
