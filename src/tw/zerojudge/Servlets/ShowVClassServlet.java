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
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ShowVClass" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ShowVClassServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		int vclassid = Integer.parseInt(request.getParameter("vclassid"));
		VClass vclass = new VClassDAO().getVClassById(vclassid);
		if (vclass.getVisible() == VClass.VISIBLE_HIDE) {
			throw new AccessException("本課程已經關閉");
		}

		if (!vclass.getIsOwner(onlineUser) && !vclass.isStudent(onlineUser.getId())) {
			throw new AccessException("您無法進入本課程！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		int vclassid = Integer.parseInt(request.getParameter("vclassid"));
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
		}
		VClass vclass = new VClassDAO().getVClassById(vclassid);

		request.setAttribute("vclass", vclass);
		request.setAttribute("vcontests", new ContestService().getContestsByVclassid(vclass.getId(), 0));
		request.setAttribute("students", new VClassStudentDAO().getStudentsByVclassid(vclass.getId()));
		onlineUser.setVclassid(vclass.getId());
		new UserService().update(onlineUser);

		request.getRequestDispatcher("ShowVClass.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
