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
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/DeleteVClass" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class DeleteVClassServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		int vclassid = Integer.parseInt(request.getParameter("vclassid"));
		if (!new VClassDAO().getVClassById(vclassid).getIsOwner(onlineUser)) {
			throw new AccessException("您的權限不能進行這個動作！");
		}
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int vclassid = Integer.parseInt(request.getParameter("vclassid"));

		VClassDAO vclassDao = new VClassDAO();

		VClass vclass = vclassDao.getVClassById(vclassid);
		vclass.setVisible(VClass.VISIBLE_HIDE);
		for (Contest contest : vclass.getContests()) {
			if (contest.getIsPausing() || contest.getIsRunning() || contest.getIsStarting()) {
				contest.doStop();
			}
		}
		vclassDao.update(vclass);
		for (VClassStudent student : new VClassStudentDAO().getStudentsByVclassid(vclassid)) {
			vclassDao.removeVClassStudent(vclassid, student.getUserid());
		}
	}
}
