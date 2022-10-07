package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

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

@WebServlet(urlPatterns = { "/InsertBatchedUsers" })
@RoleSetting(allowHigherThen = User.ROLE.MANAGER)
public class InsertBatchedUsersServlet extends HttpServlet implements IAccessFilter {
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
	 * 檢查 InsertBatchedUsers 的資料，並回覆合適說明。
	 * 
	 * @param onlineUser
	 * @param lines
	 * @return
	 */
	private ArrayList<CheckResult> checkInsertBatchedUsers_TsvScript(OnlineUser onlineUser, String lines) {
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
			String[] userfields = userline.trim().split("\t"); 
			if (userfields.length != 5) {
				checkresult.setRowdata(userline);
				checkresult.setErrormsg("欄位數量有錯, 應有 5 個欄位，但問題列有 " + userfields.length + " 個欄位。\n");
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
				if (onlineUser.getIsHigherEqualThanMANAGER()
						|| user.getCreateby().intValue() == onlineUser.getId().intValue()) {
					Logger.getAnonymousLogger().info("帳號(" + newuser.getAccount() + ")已存在，可由" + onlineUser.getAccount()
							+ "更新該帳號的資訊。\n");
				} else {
					lineerror += "帳號(" + newuser.getAccount() + ")已存在，但您(" + onlineUser.getAccount()
							+ ")並非該帳號的擁有者或批次創建者，沒有權限更新該帳號。\n";
					isCheckPassed = false;
				}
			}
			try {
				newuser.setUsername(userfields[2].trim());
			} catch (DataException e) {
				lineerror += " 暱稱：" + e.getLocalizedMessage();
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
		String users_tsvlines = request.getParameter("userscripts");
		ArrayList<CheckResult> checkResults = this.checkInsertBatchedUsers_TsvScript(onlineUser, users_tsvlines);
		if (checkResults.size() == 0) {
			ArrayList<String> successAlerts = this.doPost_InsertBatchedUsers_byTSV(onlineUser, users_tsvlines,
					request.getParameter("vclassid"));
			throw new JQuerySuccess(mapper.writeValueAsString(successAlerts));
		} else {
			throw new JQueryException(mapper.writeValueAsString(checkResults));
		}

	}

	/**
	 * 批次新增使用者，使用 TSV 格式。
	 * 批次修改有限制。只能批次修改批次新增出來的帳號。
	 * 只有 MANAGER 與 createby 可以批次修改。
	 * 
	 * @param onlineUser
	 * @param users_tsvlines
	 * @param vclassid
	 * @return
	 */
	protected ArrayList<String> doPost_InsertBatchedUsers_byTSV(OnlineUser onlineUser, String users_tsvlines,
			String vclassid) {
		ArrayList<String> successAlerts = new ArrayList<String>();
		UserService userService = new UserService();

		String[] userlines = users_tsvlines.split("\n");
		for (int i = 0; i < userlines.length; i++) {
			try {
				if (!userlines[i].startsWith("#")) {
					String[] userfields = userlines[i].split("\t"); 
					/**
					 * 在 CLASS_MODE ，批次新增使用者可視為新增整批學生。因此直接組合 schoolid_account
					 */
					String account = userfields[0].trim();
					int schoolid = Integer.parseInt(userfields[1].trim());
					String username = userfields[2].trim();
					String truename = userfields[3].trim();
					String passwd = userfields[4].trim();

					if (!new UserService().isexitAccount(account)) {
						User newuser = new User();
						newuser.setAccount(account);
						newuser.setSchoolid(schoolid);
						newuser.setUsername(username);
						newuser.setComment(account);
						newuser.setTruename(truename);
						newuser.setPasswd(passwd, passwd);
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
						if (!user.getIsBatchedUser()) {
							successAlerts
									.add("(" + user.getAccount() + ") 並非批次使用者，無法批次修改資料！");
							continue;
						} else if (!onlineUser.getIsHigherEqualThanMANAGER()
								&& user.getCreateby().intValue() != onlineUser.getId().intValue()) {
							successAlerts
									.add("您(" + onlineUser.getAccount() + ")並非此(" + user.getAccount()
											+ ")批次帳號的建立者，無法批次修改！");
							continue;
						} else {
							user.setSchoolid(schoolid);
							user.setUsername(username);
							user.setTruename(truename);
							user.setPasswd(passwd, passwd);
							user.setRole(User.ROLE.USER);
							userService.update(user);
							new UserDAO().updatePasswd(user);
							successAlerts.add("更新 " + user.getAccount());
							if (vclassid != null && vclassid.matches("[0-9]+")) {
								VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(vclassid));
								new VClassDAO().insertStudent(vclass, user);
							}
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
