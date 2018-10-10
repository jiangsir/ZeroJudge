package tw.zerojudge.Servlets.Utils;

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
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Utils.Utils;

@WebServlet(urlPatterns = { "/EditUserConfig" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class EditUserConfigServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userid = Integer.parseInt(request.getParameter("userid"));

		request.setAttribute("articletypes", Article.TYPE.values());
		request.setAttribute("articlehiddens", Article.HIDDEN.values());
		request.setAttribute("schools", new SchoolService().getSchoolsByOrder("schoolname ASC"));
		request.setAttribute("user", new UserService().getUserById(userid));
		request.getRequestDispatcher("EditUserConfig.jsp").forward(request, response);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		try {
			OnlineUser onlineUser = UserFactory.getOnlineUser(session);
			int userid = Integer.parseInt(request.getParameter("userid"));

			User user = new UserService().getUserById(userid);
			user.setUsername(request.getParameter("username"));
			user.setTruename(request.getParameter("truename"));
			user.setBirthyear(Integer.parseInt(request.getParameter("birthyear")));
			User.ROLE role = User.ROLE.valueOf(request.getParameter("role"));
			if (onlineUser.getRole().isHigherEqualThan(role)) {
				user.setRole(request.getParameter("role"));
			}
			user.setLabel(request.getParameter("label"));
			user.setExtraprivilege(request.getParameter("extraprivilege"));
			user.setComment(request.getParameter("comment"));
			user.setAuthhost(request.getParameter("authhost"));
			user.setSchoolid(Parameter.parseInteger(request.getParameter("schoolid")));

			if (user.getIsAuthhost_localhost()) {
				user.setEmail(request.getParameter("email"));
				String passwd = request.getParameter("passwd");
				String passwd2 = request.getParameter("passwd2");
				if ((passwd != null && !passwd.equals("") || (passwd2 != null && !passwd2.equals("")))) {
					user.setPasswd(passwd, passwd2);
					new UserDAO().updatePasswd(user);
				}
			}

			String[] configarray = request.getParameterValues("config");

			user.setConfig(0); 
//			if (onlineUser.getIsDEBUGGER()) {
//				user.setConfig(0);
//			} else {
//				
//				
//				
//			}
			for (int i = 0; configarray != null && i < configarray.length; i++) {
				user.setConfig(new Utils().enableConfig(user.getConfig(), Integer.valueOf(configarray[i])));
				if (user.isCheckedConfig(User.CONFIG.InsertProblem)) {
					user.setConfig(new Utils().enableConfig(user.getConfig(), User.CONFIG.ProblemManager.ordinal()));
				}
			}

			new UserService().update(user);

			new UserService().rebuiltUserStatisticByDataBase(user);

		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("修改使用者資訊成功!!");
		response.flushBuffer();

	}

}
