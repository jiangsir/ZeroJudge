package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.DAOs.TestcodeDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = {"/InsertTestcode"})
@RoleSetting
public class InsertTestcodeServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		OnlineUser onlineUser = UserFactory.getOnlineUser(request.getSession(false));
		if (!onlineUser.getIsDEBUGGER()) {
			throw new AccessException("您不能新增 testcode");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Testcode testcode = new Testcode();
		if (request.getParameter("solutionid") != null) {
			Solution solution = new SolutionService().getSolutionById(request.getParameter("solutionid"));
			testcode.setCode(solution.getCode());
			testcode.setDescript("#" + solution.getId() + "\n");
			testcode.setLanguage(solution.getLanguage().getName());
		}

		request.setAttribute("testcode", testcode);
		request.getRequestDispatcher("InsertTestcode.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Testcode newtestcode = new Testcode();
		newtestcode.setCode(request.getParameter("code"));
		newtestcode.setLanguage(request.getParameter("language"));
		newtestcode.setIndata(request.getParameter("indata"));
		newtestcode.setOutdata(request.getParameter("outdata"));
		newtestcode.setExpected_status(request.getParameter("expected_status"));
		newtestcode.setDescript(request.getParameter("descript"));
		new TestcodeDAO().insert(newtestcode);
		response.sendRedirect("Submissions");
	}

}
