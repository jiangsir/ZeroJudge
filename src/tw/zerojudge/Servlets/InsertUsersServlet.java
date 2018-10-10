package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/InsertUsers" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class InsertUsersServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		if (!onlineUser.getIsHigherEqualThanMANAGER() && !onlineUser.isCheckedConfig(User.CONFIG.VClassManager)) {
			throw new AccessException("您不能批次新增使用者！");
		}
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("InsertUsers.jsp").forward(request, response);
	}

	private boolean checkUserScriptLine(String line) throws DataException {
		String[] UserScript = line.split(",");
		if (UserScript.length != 7) {
			throw new DataException("欄位數目有誤！(" + UserScript.length + ")");
		}
		User newuser = new User();
		newuser.setAccount(UserScript[0].trim());
		newuser.setBirthyear(UserScript[4].trim());
		newuser.setEmail(UserScript[5].trim());
		return true;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			OnlineUser onlineUser = new SessionScope(request).getOnlineUser();

			UserDAO userDao = new UserDAO();

			String scripts = request.getParameter("scripts");
			String[] lines = scripts.split("\n");
			for (int i = 0; i < lines.length; i++) {
				lines[i] = lines[i].trim();
				if (lines[i] != null && "".equals(lines[i]) && !lines[i].startsWith("#")) {
					if (new UserService().isexitAccount(lines[i].split(",")[0])) {
					} else {
						checkUserScriptLine(lines[i]);
					}
				}
			}

			for (int i = 0; i < lines.length; i++) {
				if (!lines[i].startsWith("#")) {
					String[] datas = lines[i].split(",");
					if (!new UserService().isexitAccount(datas[0].trim())) {
						User newuser = new User();
						newuser.setAccount(datas[0].trim());
						newuser.setUsername(datas[1].trim());
						newuser.setTruename(datas[2].trim());
						newuser.setPasswd(datas[3].trim(), datas[3].trim());
						newuser.setBirthyear(datas[4].trim());
						newuser.setEmail(datas[5].trim());
						newuser.setSchoolid(datas[6].trim());
						newuser.setRole(User.ROLE.USER);
						newuser.setCreateby(onlineUser.getId());
						new UserService().insert(newuser);
					} else {
						User user = new UserService().getUserByAccount(datas[0].trim());
						if (user.getCreateby().intValue() == onlineUser.getId().intValue()) {
							user.setUsername(datas[1].trim());
							user.setTruename(datas[2].trim());
							user.setPasswd(datas[3].trim(), datas[3].trim());
							user.setBirthyear(Integer.parseInt(datas[4].trim()));
							user.setEmail(datas[5].trim());
							user.setSchoolid(Integer.parseInt(datas[6].trim()));
							user.setRole(User.ROLE.USER);
							new UserService().update(user);
							userDao.updatePasswd(user);
						} else {
							throw new DataException(
									"您(" + onlineUser.getAccount() + ")不能修改 (" + user.getAccount() + ") 的資料！");
						}
					}
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(e.getLocalizedMessage());
			response.flushBuffer();
			return;
		}

	}
}
