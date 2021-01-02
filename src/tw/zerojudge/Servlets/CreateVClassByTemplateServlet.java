package tw.zerojudge.Servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
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
import tw.zerojudge.DAOs.VClassTemplateDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/CreateVClassByTemplate" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class CreateVClassByTemplateServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		this.AccessFilter(onlineUser);
	}

	public void AccessFilter(OnlineUser onlineUser) throws AccessException {

		new EditVClassesServlet().AccessFilter(onlineUser);

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
			String vclassTemplateid = request.getParameter("vclassTemplateid");
			if (vclassTemplateid == null || "".equals(vclassTemplateid) || "0".equals(vclassTemplateid)) {
				Contest newcontest = new Contest();
				newcontest.setOwnerid(vclass.getOwnerid());
				newcontest.setTitle(vclass.getVclassname() + " - 隨堂練習");
				newcontest.setVisible(Contest.VISIBLE.hide);
				newcontest.setTimelimit(1000 * 60 * 60 * 2L);
				newcontest.setVclassid(vclass.getId());
				newcontest.doEnableConfig(Contest.CONFIG.ShowDetail);
				newcontest.setStarttime(new Timestamp(calendar.getTime().getTime()));
				calendar.add(Calendar.DATE, 7);
				new ContestService().insert(newcontest);
			} else {
				for (VClassTemplate template : new VClassTemplateDAO()
						.getTemplatesById(Integer.parseInt(vclassTemplateid))) {
					Contest newcontest = new Contest();
					newcontest.setOwnerid(vclass.getOwnerid());
					newcontest.setTitle(template.getTitle());
					newcontest.setSubtitle(template.getDescript());
					newcontest.setVisible(Contest.VISIBLE.hide);
					newcontest.setTimelimit(1000 * 60 * 60 * 2L);
					newcontest.setProblemids(template.getProblemids().toString());
					newcontest.setVclassid(vclass.getId());
					newcontest.setVclasstemplateid(template.getId());

					newcontest.doEnableConfig(Contest.CONFIG.ShowDetail);
					newcontest.setStarttime(new Timestamp(calendar.getTime().getTime()));
					calendar.add(Calendar.DATE, 7);
					new ContestService().insert(newcontest);
				}
			}
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
