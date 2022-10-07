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
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/UpdateVClass" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class UpdateVClassServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		int vclassid = Integer.parseInt(request.getParameter("vclassid"));
		VClass vclass = new VClassDAO().getVClassById(vclassid);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!vclass.getIsOwner(onlineUser)) {
			throw new AccessException("您不能修改課程資料！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int vclassid = Integer.parseInt(request.getParameter("vclassid"));
		VClassDAO vclassDao = new VClassDAO();
		VClass vclass = vclassDao.getVClassById(vclassid);
		request.setAttribute("vclass", vclass);
		request.setAttribute("vcontests", vclassDao.getVContests(vclass.getId()));
		request.setAttribute("students", new VClassStudentDAO().getStudentsByVclassid(vclass.getId()));
		request.getRequestDispatcher("UpdateVClass.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		VClassDAO vclassDao = new VClassDAO();
		try {
			VClass vclass = vclassDao.getVClassById(Integer.parseInt(request.getParameter("vclassid")));
			vclass.setVclassname(request.getParameter("vclassname").trim());
			vclass.setDescript(request.getParameter("descript").trim());
			vclassDao.update(vclass);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
