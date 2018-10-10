package tw.zerojudge.Servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/InsertVClass" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class InsertVClassServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.getIsDEBUGGER() && !onlineUser.isVClassManager()) {
			throw new AccessException("您不能新增課程！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("InsertVClass.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		VClassDAO vclassDao = new VClassDAO();
		try {
			VClass newvclass = new VClass();
			newvclass.setVclassname(request.getParameter("vclassname"));

			newvclass.setOwnerid(onlineUser.getId());
			newvclass.setFirstclasstime(new Timestamp(System.currentTimeMillis()));
			newvclass.setDescript(request.getParameter("descript"));
			int vclassid = vclassDao.insert(newvclass);
			VClass vclass = vclassDao.getVClassById(vclassid);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vclass.getFirstclasstime());

			Contest newcontest = new Contest();
			newcontest.setOwnerid(vclass.getOwnerid());
			newcontest.setTitle(vclass.getVclassname() + " - 隨堂測驗");
			newcontest.setVisible(Contest.VISIBLE.hide);
			newcontest.setTimelimit(1000 * 60 * 60 * 2L);
			newcontest.setVclassid(vclass.getId());
			newcontest.doEnableConfig(Contest.CONFIG.ShowDetail);
			newcontest.setStarttime(new Timestamp(calendar.getTime().getTime()));
			calendar.add(Calendar.DATE, 7);

			new ContestService().insert(newcontest);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

}
