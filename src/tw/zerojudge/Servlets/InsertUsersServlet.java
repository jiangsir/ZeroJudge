package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Exceptions.JQuerySuccess;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.VClass;

@WebServlet(urlPatterns = { "/InsertUsers" })
@RoleSetting(allowHigherThen = User.ROLE.MANAGER)
public class InsertUsersServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper(); 

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		if (onlineUser.getIsHigherEqualThanMANAGER()) {
			return;
		}
		throw new AccessException("您不能批次新增使用者！");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("InsertUsers.jsp").forward(request, response);
	}

	class CheckResult {
		private String rowdata = "";
		private String errormsg = "";
		private Boolean ispass = false;

		public CheckResult() {
		}

		public CheckResult(String rowdata, String errormsg, Boolean ispass) {
			super();
			this.rowdata = rowdata;
			this.errormsg = errormsg;
			this.ispass = ispass;
		}

		public String getRowdata() {
			return rowdata;
		}

		public void setRowdata(String rowdata) {
			this.rowdata = rowdata;
		}

		public String getErrormsg() {
			return errormsg;
		}

		public void setErrormsg(String errormsg) {
			this.errormsg = errormsg;
		}

		public Boolean getIspass() {
			return ispass;
		}

		public void setIspass(Boolean ispass) {
			this.ispass = ispass;
		}

	}

	/**
	 * 檢查 InsertUsers 的資料，並回覆合適說明。
	 * 
	 * @return
	 */
	private ArrayList<CheckResult> checkInsertUsersScript(String lines, OnlineUser onlineUser) {
		ArrayList<CheckResult> checkResults = new ArrayList<CheckResult>();
		if (lines == null) {
			checkResults.add(new CheckResult("", "無法讀取 user 資料 userlines == null! ", false));
			return checkResults;
		}
		String[] userlines = lines.split("\n");
		int MAX = 1000;
		Boolean isCheckPassed = false;
		UserService userService = new UserService();
		if (userlines.length >= MAX) {
			checkResults.add(new CheckResult("", "批次新增使用者數量(" + userlines.length + "筆)上限每次以 " + MAX + " 筆為限！️", false));
			return checkResults;
		}
		for (String userline : userlines) {
			CheckResult checkresult = new CheckResult();
			String lineerror = "";
			if (userline.trim().equals("") || userline.trim().startsWith("#"))
				continue;
			String[] userfields = userline.trim().split(",");
			if (userfields.length != 7) {
				checkresult.setRowdata(userline);
				checkresult.setErrormsg("欄位數量有錯, 應有 7 個欄位，但問題列有 " + userfields.length + " 個欄位。\n");
				checkresult.setIspass(false);
				checkResults.add(checkresult);
				continue;
			}
			User newuser = new User();
			try {
				newuser.setAccount(userfields[0].trim());
			} catch (DataException e) {
				lineerror += "帳號：" + e.getLocalizedMessage();
				isCheckPassed = false;
			}
			User user = userService.getUserByAccount(newuser.getAccount());
			if (!user.isNullUser()) {
				if (user.getCreateby().intValue() == onlineUser.getId().intValue()) {
				} else {
					lineerror += "帳號(" + newuser.getAccount() + ")已存在，但您(" + onlineUser.getAccount()
							+ ")並非該帳號的創建者，沒有權限更新該帳號。\n";
					isCheckPassed = false;
				}
			}
			try {
				newuser.setUsername(userfields[2].trim());
			} catch (DataException e) {
				lineerror += " 姓名：" + e.getLocalizedMessage();
				isCheckPassed = false;
			}
			try {
				newuser.setTruename(userfields[3].trim());
			} catch (DataException e) {
				lineerror += " 真實姓名：" + e.getLocalizedMessage();
				isCheckPassed = false;
			}
			try {
				newuser.setPasswd(userfields[4].trim(), userfields[4].trim());
			} catch (DataException e) {
				lineerror += "密碼：" + e.getLocalizedMessage();
				isCheckPassed = false;
			}
			try {
				newuser.setBirthyear(userfields[5].trim());
			} catch (DataException e) {
				lineerror += "生日：" + e.getLocalizedMessage();
				isCheckPassed = false;
			}
			newuser.setEmail(userfields[6].trim());

			if (!lineerror.equals("")) {
				checkresult.setRowdata(userline);
				checkresult.setErrormsg(lineerror);
				checkresult.setIspass(isCheckPassed);
				checkResults.add(checkresult);
			}
		}

		return checkResults;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		String scripts = request.getParameter("userscripts");
		ArrayList<CheckResult> checkResults = this.checkInsertUsersScript(scripts, onlineUser);
		if (checkResults.size() == 0) {
			ArrayList<String> successAlerts = this.doPost_InsertUsers(request, response);
			throw new JQuerySuccess(mapper.writeValueAsString(successAlerts));
		} else {
			throw new JQueryException(mapper.writeValueAsString(checkResults));
		}

	}

	/**
	 * 實際進行新增使用者
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected ArrayList<String> doPost_InsertUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		ArrayList<String> successAlerts = new ArrayList<String>();
		UserService userService = new UserService();
		String scripts = request.getParameter("userscripts");
		String vclassid = request.getParameter("vclassid");

		String[] userlines = scripts.split("\n");
		for (int i = 0; i < userlines.length; i++) {
			try {
				if (!userlines[i].startsWith("#")) {
					String[] userfields = userlines[i].split(",");
					/**
					 * 在 CLASS_MODE ，批次新增使用者可視為新增整批學生。因此直接組合 schoolid_account
					 */
					String account = userfields[0].trim();
					int schoolid = Integer.parseInt(userfields[1].trim());
					String username = userfields[2].trim();
					String truename = userfields[3].trim();
					String passwd = userfields[4].trim();
					int birthyear = Integer.parseInt(userfields[5].trim());
					String email = userfields[6].trim();



					if (!new UserService().isexitAccount(account)) {
						User newuser = new User();
						newuser.setAccount(account);
						newuser.setSchoolid(schoolid);
						newuser.setUsername(username);
						newuser.setComment(account);
						newuser.setTruename(truename);
						newuser.setPasswd(passwd, passwd);
						newuser.setBirthyear(birthyear);
						newuser.setEmail(email);
						newuser.setRole(User.ROLE.USER);
						newuser.setCreateby(onlineUser.getId());
						newuser.enableConfig(User.CONFIG.BatchedUser);
						new UserService().insert(newuser);

						if (vclassid != null && vclassid.matches("[0-9]+")) {
							VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(vclassid));
							new VClassDAO().insertStudent(vclass, newuser);
						}

						successAlerts.add("新增 " + newuser.getAccount());
					} else {
						User user = new UserService().getUserByAccount(account);
						if (!user.isCheckedConfig(User.CONFIG.BatchedUser)
								&& user.getCreateby().intValue() == onlineUser.getId().intValue()) {
							user.setSchoolid(schoolid);
							user.setUsername(username);
							user.setTruename(truename);
							user.setPasswd(passwd, passwd);
							user.setBirthyear(birthyear);
							user.setEmail(email);
							user.setRole(User.ROLE.USER);
							userService.update(user);
							new UserDAO().updatePasswd(user);
							successAlerts.add("更新 " + user.getAccount());
							if (vclassid != null && vclassid.matches("[0-9]+")) {
								VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(vclassid));
								new VClassDAO().insertStudent(vclass, user);
							}
						} else {
							successAlerts
									.add("您(" + onlineUser.getAccount() + ")不能修改 (" + user.getAccount() + ") 的資料！");
							continue;
						}
					}
				}
			} catch (Exception e) {
				successAlerts.add("失敗 " + e.getLocalizedMessage());
				continue;
			}
		}
		return successAlerts;
	}
}
