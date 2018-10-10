package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Utils.Utils;

@WebServlet(urlPatterns = { "/UpdateUser" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class UpdateUserServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		String account = request.getParameter("account");
		//
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();

		if (onlineUser.getIsDEBUGGER()) {
			request.setAttribute("articletypes", Article.TYPE.values());
			request.setAttribute("articlehiddens", Article.HIDDEN.values());
		}

		request.setAttribute("schools", new SchoolService().getSchoolsByOrder("schoolname ASC"));
		request.setAttribute("user", (User) onlineUser);

		request.getRequestDispatcher("EditUser.jsp").forward(request, response);
		return;

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

			User user = new UserService().getUserById(onlineUser.getId());
			user.setUsername(request.getParameter("username"));
			user.setTruename(request.getParameter("truename"));
			user.setBirthyear(request.getParameter("birthyear"));

			user.setSchoolid(request.getParameter("schoolid"));
			if (user.getSchoolid().intValue() == -1) {
				School newschool = new School();
				newschool.setSchoolname(request.getParameter("otherschoolname"));
				newschool.setUrl(request.getParameter("otherschoolurl"));

				SchoolService schoolService = new SchoolService();
				School sameurlSchool;
				School samenameSchool;
				try {
					sameurlSchool = schoolService.getSchoolByURL(newschool.getUrl());
					samenameSchool = schoolService.getSchoolByName(newschool.getSchoolname());
				} catch (DataException e1) {
					e1.printStackTrace();
					sameurlSchool = new School();
					samenameSchool = new School();
				}
				if (sameurlSchool.getId() > 0) {
					if (sameurlSchool.getCheckid().intValue() == School.CHECKID_NONECHECK) {
						sameurlSchool.setSchoolname(newschool.getSchoolname());
						schoolService.update(sameurlSchool);
					}
					user.setSchoolid(sameurlSchool.getId());
				} else if (samenameSchool.getId() > 0) {
					if (samenameSchool.getCheckid().intValue() == School.CHECKID_NONECHECK) {
						samenameSchool.setUrl(newschool.getUrl());
						schoolService.update(samenameSchool);
					}
					user.setSchoolid(samenameSchool.getId());
				} else if (samenameSchool.getId() <= 0 && sameurlSchool.getId() <= 0) {
					int newSchoolid = schoolService.insert(newschool);
					user.setSchoolid(newSchoolid);
				}

			}
			user.setIpset(new Utils().getIpList(request));

			UserDAO userDao = new UserDAO();
			user.setPasswd(request.getParameter("passwd"), request.getParameter("passwd2"));
			userDao.updatePasswd(user);

			if (user.getIsAuthhost_localhost()) {
				user.setEmail(request.getParameter("email"));
			}
			new UserService().update(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("修改使用者資訊成功!!");
		response.flushBuffer();

	}
}
