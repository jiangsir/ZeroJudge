package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/InsertVContest" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class InsertVContestServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		VClassDAO vclassDao = new VClassDAO();
		VClass vclass = vclassDao.getVClassById(Parameter.parseInteger(request
				.getParameter("vclassid")));
		vclassDao.insertVContest(vclass);
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}

}
