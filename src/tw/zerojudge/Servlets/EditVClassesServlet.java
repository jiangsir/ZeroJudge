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
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/EditVClasses" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class EditVClassesServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.getIsDEBUGGER() && !onlineUser.isVClassManager()) {
			throw new AccessException("您(" + onlineUser.getAccount() + ")沒有『管理課程』的權限！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		VClassDAO vclassDao = new VClassDAO();
		if (onlineUser.getIsDEBUGGER()) {
			request.setAttribute("vclasses", vclassDao.getVClasses());
		} else {
			request.setAttribute("vclasses", vclassDao.getVClassesByOwnerid(onlineUser.getId()));
		}
		request.getRequestDispatcher("EditVClasses.jsp").forward(request, response);
	}

}
