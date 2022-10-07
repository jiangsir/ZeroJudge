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
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.TestcodeDAO;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/UpdateTestcode" })
@RoleSetting
public class UpdateTestcodeServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		if (!onlineUser.getIsDEBUGGER()) {
			throw new AccessException(onlineUser, "您不能編輯 testcode");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("testcodeid"));
		request.setAttribute("testcode", new TestcodeDAO().getTestcodeById(id));
		request.getRequestDispatcher("InsertTestcode.jsp").forward(request,
				response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("testcodeid"));
		Testcode testcode = new TestcodeDAO().getTestcodeById(id);
		testcode.setCode(request.getParameter("code"));
		testcode.setLanguage(request.getParameter("language"));
		testcode.setIndata(request.getParameter("indata"));
		testcode.setOutdata(request.getParameter("outdata"));
		testcode.setExpected_status(request.getParameter("expected_status"));
		testcode.setDescript(request.getParameter("descript"));
		new TestcodeDAO().update(testcode);
		response.sendRedirect(request.getContextPath()
				+ ShowTestcodesServlet.class.getAnnotation(WebServlet.class)
						.urlPatterns()[0]);
	}

}
