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
import tw.zerojudge.Factories.UserFactory;
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
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.isContestManager()) {
			throw new AccessException("您沒有「競賽管理者」權限！");
		}
		int vclassid = Parameter.parseInteger(request.getParameter("vclassid"));
		if (vclassid < 0) {
			throw new AccessException("參數錯誤！必須指定 「課程編號」vclassid");
		}
		VClassDAO vclassDao = new VClassDAO();
		VClass vclass = vclassDao.getVClassById(vclassid);
		if (!vclass.getIsOwner(onlineUser)) {
			throw new AccessException("您並非「課程擁有者」無法進行(InsertVContest)！");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		HttpSession session = request.getSession(false);
//		VClassDAO vclassDao = new VClassDAO();
//		VClass vclass = vclassDao.getVClassById(Parameter.parseInteger(request.getParameter("vclassid")));
//		String cloneContestid = request.getParameter("cloneContestid");
//		if (cloneContestid == null || !cloneContestid.matches("[0-9]+")) {
//			vclassDao.insertVContest(vclass);
//		} else {
//			this.doPost_cloneVContestById(Parameter.parseInteger(cloneContestid));
//		}
//		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}

//	private void doPost_cloneVContestById(int cloneContestid) {
//		new VClassDAO().cloneVContestById(cloneContestid);
//	}

}
