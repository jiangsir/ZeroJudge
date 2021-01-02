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
import tw.zerojudge.DAOs.VClassTemplateDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/ShowVClasses" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class ShowVClassesServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		VClassDAO vclassDao = new VClassDAO();
		request.setAttribute("owned_vclasses", vclassDao.getVClassesByOwnerid(onlineUser.getId()));
		request.setAttribute("joined_vclasses", vclassDao.getVClassesByUserid(onlineUser.getId()));
		if (onlineUser.getIsHigherEqualThanMANAGER()) {
			request.setAttribute("all_vclasses", vclassDao.getAllVisible_VClasses());
		}
		request.setAttribute("vclassTemplates", new VClassTemplateDAO().getTemplatesByOwnerid(onlineUser.getId()));
		request.setAttribute("publicTemplates", new VClassTemplateDAO().getTemplatesByVisible(1));

		request.getRequestDispatcher("ShowVClasses.jsp").forward(request, response);
	}

}
