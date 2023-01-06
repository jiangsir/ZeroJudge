package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.TokenTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.SchoolService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/InsertUser" })
public class InsertUserServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (!appConfig.isChecked_registerable()) {
			throw new AccessException("系統暫停註冊。");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		session.setAttribute("token", TokenTool.generateToken());

		request.setAttribute("schools", new SchoolService().getSchoolsByOrder("schoolname ASC"));
		request.setAttribute("user", new User());
		request.getRequestDispatcher("/EditUser.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		SessionScope sessionScope = new SessionScope(session);
		User newuser = new User();
		try {
			newuser.setAccount(request.getParameter("account"));
			newuser.setRole(User.ROLE.USER);
			newuser.setUsername(request.getParameter("username"));
			newuser.setTruename(request.getParameter("truename"));
			newuser.setBirthyear(Parameter.parseInteger(request.getParameter("birthyear")));
			newuser.setEmail(request.getParameter("email"));
			newuser.setLabel(Parameter.parseString(request.getParameter("label")));
			newuser.setSchoolid(Parameter.parseInteger(request.getParameter("schoolid")));
			newuser.setRegistertime(new Timestamp(System.currentTimeMillis()));
			newuser.setLastlogin(new Timestamp(System.currentTimeMillis()));
			newuser.setIpset(sessionScope.getSession_ipset());

			if (newuser.getSchoolid().intValue() == -1) {
				School newschool = new School();
				newschool.setSchoolname(request.getParameter("otherschoolname"));
				newschool.setUrl(request.getParameter("otherschoolurl"));

				SchoolService schoolService = new SchoolService();
				School sameurlSchool;
				School samenameSchool;
				sameurlSchool = schoolService.getSchoolByURL(newschool.getUrl());
				samenameSchool = schoolService.getSchoolByName(newschool.getSchoolname());
				if (sameurlSchool.getId() > 0) {
					if (sameurlSchool.getCheckid().intValue() == School.CHECKID_NONECHECK) {
						sameurlSchool.setSchoolname(newschool.getSchoolname());
						schoolService.update(sameurlSchool);
					}
					newuser.setSchoolid(sameurlSchool.getId());
				} else if (samenameSchool.getId() > 0) {
					if (samenameSchool.getCheckid().intValue() == School.CHECKID_NONECHECK) {
						samenameSchool.setUrl(newschool.getUrl());
						schoolService.update(samenameSchool);
					}
					newuser.setSchoolid(samenameSchool.getId());
				} else if (samenameSchool.getId() <= 0 && sameurlSchool.getId() <= 0) {
					int newSchoolid;
					newSchoolid = schoolService.insert(newschool);
					newuser.setSchoolid(newSchoolid);
				}

			}

			newuser.setPasswd(request.getParameter("passwd"), request.getParameter("passwd2"));

			AppConfig appConfig = ApplicationScope.getAppConfig();
			String secretkey = appConfig.getSecret_key();
			if (appConfig.getIsGoogleRecaptchaSetup(request.getRemoteAddr()) && !appConfig.isCaptchaValid(secretkey,
					request.getParameter("g-recaptcha-response"), new IpAddress(request))) {
				throw new JQueryException("reCaptcha 機器人驗證不通過。");
			}

			newuser.setId(new UserService().insert(newuser));

		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("新增使用者成功!! #編號 " + newuser.getId());
		response.flushBuffer();

	}

}
