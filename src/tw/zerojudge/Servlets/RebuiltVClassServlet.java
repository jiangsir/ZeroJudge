package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/RebuiltVClass" })
@RoleSetting
public class RebuiltVClassServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		Integer vclassid = Parameter.parseInteger(request.getParameter("vclassid"));
		VClass vclass = new VClassDAO().getVClassById(vclassid);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!vclass.getIsOwner(onlineUser)) {
			throw new AccessException("您並非課程教師，不能進行此動作。");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer vclassid = Parameter.parseInteger(request.getParameter("vclassid"));

		VClassDAO vclassDao = new VClassDAO();
		VClass vclass = vclassDao.getVClassById(vclassid);
		vclassDao.rebuiltVClass(vclass);
		throw new InfoException("重新計算 vclass 完成");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
