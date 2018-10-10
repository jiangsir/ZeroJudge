package tw.zerojudge.DAOs;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Loginlog;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.School;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.User.AUTHHOST;
import tw.zerojudge.Tables.User.CONFIG;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Utils.Utils;
import tw.zerojudge.Utils.GoogleLogin.GoogleUser;

public class UserService {
	public static Hashtable<Integer, User> HashUsers = new Hashtable<Integer, User>();
	public static Hashtable<String, Integer> HashAccountUserid = new Hashtable<String, Integer>();
	public final int maxMultiLogin = 3; 

	public boolean isExitedUser(String account, String passwd) {
		if (new UserDAO().getUserByAccountPasswd(account, passwd) == null) {
			return false;
		}
		return true;
	}

	public boolean isExitedAccount(String account) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("account", account);
		return new UserDAO().executeCount(fields) > 0;
	}

	/**
	 * 只判斷這個 session 是否有登入。
	 * 
	 * @param session
	 * @return
	 */
	public boolean isUserOnline(HttpSession session) {
		return new SessionScope(session).getOnlineUser() == null ? false : true;
	}

	/**
	 * 如果找不到，則回覆一個 NullUser
	 * 
	 * @param account
	 * @return
	 */
	public User getUserByAccount(String account) {
		if (HashAccountUserid.containsKey(account)) {
			return this.getUserById(HashAccountUserid.get(account));
		} else {
			TreeMap<String, Object> fields = new TreeMap<String, Object>();
			fields.put("account", account);
			for (User user : new UserDAO().getUsersByFields(fields, null, 0)) {
				HashUsers.put(user.getId(), user);
				HashAccountUserid.put(user.getAccount(), user.getId());
				return user;
			}
			return UserFactory.getNullUser();
		}
	}

	/**
	 * 
	 * @param authhost
	 * @param email
	 * @return
	 */
	public User getUserByGoogle(GoogleUser googleUser) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("authhost", AUTHHOST.Google.name());
		fields.put("email", googleUser.getEmail());
		for (User user : new UserDAO().getUsersByFields(fields, null, 0)) {
			HashUsers.put(user.getId(), user);
			HashAccountUserid.put(user.getAccount(), user.getId());
			return user;
		}
		return UserFactory.getNullUser();
	}

	public ArrayList<User> getAllUsers() {
		return new UserDAO().getAllUsers();
	}

	public int getCountByAllUsers() {
		return new UserDAO().getCountByAllUsers();
	}


	public Hashtable<Integer, User> getHashUsers() {
		return HashUsers;
	}

	public User getUserById(int userid) {
		if (!HashUsers.containsKey(userid)) {
			User user = new UserDAO().getUserById(userid);
			HashUsers.put(userid, user);
		}
		return HashUsers.get(userid);
	}

	/**
	 * 在 login 時，給定 account , passwd
	 * 
	 * @param account
	 * @param passwd
	 * @return
	 */
	public User getUserByAccountPasswd(String account, String passwd) {
		User user = new UserDAO().getUserByAccountPasswd(account, passwd);
		if (user == null || user.isNullUser()) {
			throw new InfoException("帳號或密碼可能有誤！");
		}
		return user;
	}

	public int insert(User user) throws DataException {
		this.checkInsert(user);
		int userid = new UserDAO().insert(user);
		user.setId(userid);
		HashUsers.put(user.getId(), user);
		if (userid == 0) {
			throw new DataException("沒有新增！");
		}
		return userid;
	}

	/**
	 * 用 GoogleAccount 新增帳戶，若已經存在，則直接使用已存在的帳號。
	 * 
	 */
	public User insertGoogleUser(GoogleUser googleUser) {
		User newuser = this.getUserByAccount(googleUser.getEmail());
		if (newuser.isNullUser()) {
			newuser.setAccount(googleUser.getEmail());
			String pass = new Utils().randomPassword();
			newuser.setPasswd(pass, pass);
			int userid = new UserDAO().insert(newuser);
			newuser.setId(userid);
		}

		newuser.setAccount(googleUser.getEmail());
		newuser.setAuthhost(AUTHHOST.Google);
		newuser.setPicture(googleUser.getPicture());

		URL url;
		try {
			url = new URL(googleUser.getPicture());
			newuser.setPicturetype(url.openConnection().getContentType());
			newuser.setPictureblob(IOUtils.toByteArray(url));
		} catch (IOException e) {
			e.printStackTrace();
		}

		newuser.setEmail(googleUser.getEmail());
		newuser.setUsername(googleUser.getName());
		newuser.setTruename(googleUser.getName());
		newuser.setComment(googleUser.getId());
		newuser.setRole(ROLE.USER);

		new UserDAO().update(newuser);
		HashUsers.put(newuser.getId(), newuser);
		return newuser;

	}

	public boolean checkUpdate(User user) throws DataException {
		user.checkUsername();
		user.checkBirthyear();
		user.checkTruename();
		user.checkEmail();
		return true;
	}

	public boolean checkInsert(User user) throws DataException {
		if (this.isReservedAccount(user.getAccount())) {
			throw new DataException("錯誤!! (" + user.getAccount() + ") 這個帳號是系統保留帳號，請換一個帳號。");
		} else if (this.isexitAccount(user.getAccount())) {
			throw new DataException("錯誤!! (" + user.getAccount() + ") 這個帳號已被使用，請換一個帳號。");
		}
		User.checkAccount(user.getAccount());
		return this.checkUpdate(user);
	}

	public int update(User user) throws DataException {
		this.checkUpdate(user);
		int result = new UserDAO().update(user);
		HashUsers.put(user.getId(), user); 
		return result;
	}

	public void delete(int userid) throws DataException {
		HashUsers.remove(userid);
		if (!new UserDAO().delete(userid)) {
			throw new DataException("刪除失敗！");
		}
	}

	public void delete(String account) throws DataException {
		HashUsers.remove(this.getUserByAccount(account).getId());
		if (!new UserDAO().delete(this.getUserByAccount(account).getId())) {
			throw new DataException("刪除失敗！");
		}
	}

	public OnlineUser doLogin(HttpSession session, User user) throws DataException {
		if (user == null || user.isNullUser()) {
			throw new DataException("登入錯誤！");
		}

		if (!user.isCheckedConfig(CONFIG.ENABLE)) {
			throw new DataException("本帳號已失效，無法登入。");
		}
		if (this.getCountByMultiLogin(user) >= maxMultiLogin) {
			throw new DataException("重複登入次數過多。");
		}

		SessionScope sessionScope = new SessionScope(session);
		OnlineUser onlineUser = new OnlineUser(session, user);
		ArrayList<IpAddress> iplist = sessionScope.getSession_ipset();


		Loginlog loginlog = new Loginlog();
		loginlog.setUseraccount(user.getAccount());
		loginlog.setIpfrom(iplist);
		loginlog.setMessage("成功登入");
		int loginlogid = new LoginlogDAO().insert(loginlog);
		onlineUser.setLoginid(loginlogid);
		onlineUser.setLastlogin(new Timestamp(System.currentTimeMillis()));
		onlineUser.setIpset(iplist);
		onlineUser.setSessionid(session.getId());
		if (!onlineUser.getJoinedContest().getIsRunning()) {
			onlineUser.setJoinedcontestidToNONE();
		}
		new UserService().update((User) onlineUser);

		sessionScope.setSessionid(session.getId());
		sessionScope.setOnlineUser(onlineUser);
		sessionScope.setUnreadIMessages(new IMessageDAO().getUnreadIMessage(onlineUser, 1));
		return onlineUser;
	}

	/**
	 * @throws DataException
	 * 
	 * 
	 */
	private void doRelogin(HttpSession session) throws DataException {
		OnlineUser onlineUser = new SessionScope(session).getOnlineUser();
		OnlineUser newOnlineUser = new OnlineUser(session, this.getUserById(onlineUser.getId()));
		this.doLogin(session, newOnlineUser);
	}

	/**
	 * 使用者狀態有變化時要做一次 reload. 比如：Contest 結束後，要改變參賽狀態。
	 */
	public void doReload(HttpSession session) {
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		OnlineUser newOnlineUser = new OnlineUser(session, this.getUserById(onlineUser.getId()));
		this.doLogin(session, newOnlineUser);
	}

	/**
	 * 當題目有更動的時候，若題目隱藏或開放出來<br>
	 * User 的解題統計就要重算
	 * 
	 */
	public void rebuiltUserStatisticByProblem(Problemid problemid) {
		String sql = "SELECT userid FROM solutions WHERE pid="
				+ new ProblemService().getProblemByProblemid(problemid).getId() + " AND visible="
				+ Solution.VISIBLE.open.getValue() + " GROUP BY userid ORDER BY userid DESC";
		for (Solution solution : new SolutionDAO().executeQuery(sql, Solution.class)) {
			this.rebuiltUserStatisticByDataBase(this.getUserById(solution.getUserid()));
		}
	}

	/**
	 * 當一個 contest 要結束的時候，強制將所有 User 退出 Contest<br/>
	 * 直接從資料庫將已加入的人全部移出競賽。<br/>
	 * 
	 * @param joinedcontestid
	 */
	public void leaveJoinedContestid(Integer joinedcontestid) {
		for (OnlineUser onlineUser : ApplicationScope.getOnlineUsers().values()) {
			if (onlineUser.getJoinedcontestid().intValue() == joinedcontestid) {
				onlineUser.doLeaveContest();
			}
		}
		new UserDAO().leaveJoinedContestid(joinedcontestid);
	}

	/**
	 * 獲取某 user 所有的 "公開" submission
	 * 
	 * @return
	 */
	public int getCountByUserOpenSubmission(int userid) {
		TreeSet<String> rules = new TreeSet<String>();
		rules.add("userid=" + userid);
		rules.add("visible=" + Solution.VISIBLE.open.getValue());
		return new SolutionDAO().getCountByRules(rules);
	}

	public boolean isexitAccount(String account) {
		User user = this.getUserByAccount(account);
		if (user == null || user.isNullUser()) {
			return false;
		}
		return true;
	}

	/**
	 * 判斷保留帳號。
	 * 
	 * @param account
	 * @return
	 */
	public boolean isReservedAccount(String account) {
		account = account.toLowerCase();
		if ("all".equals(account) || account.matches(".*system.*") || account.matches(".*zerojudge.*")
				|| "null".equals(account)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param account
	 * @return
	 */
	public boolean isAvailableAccount(String account) {
		User.checkAccount(account);
		if (!this.isReservedAccount(account) && !this.isexitAccount(account)) {
			return true;
		}
		return false;
	}

	/**
	 * 全部重新計算 某個 user 的 statistic
	 * 
	 * 
	 * @param user
	 */
	public void rebuiltUserStatisticByDataBase(User user) {
		long starttime = System.currentTimeMillis();

		ArrayList<HashMap<String, Object>> list = new SolutionDAO().getHashMapForUserStatistic_SLOW(user.getId());
		if (list == null) {
			return;
		}

		int wacount = 0, recount = 0, tlecount = 0, mlecount = 0, olecount = 0, cecount = 0;

		ProblemService problemService = new ProblemService();
		TreeSet<Problemid> openedProblemids = ApplicationScope.getOpenedProblemidSet();

		TreeSet<Problemid> aclist = new TreeSet<Problemid>();
		TreeSet<Problemid> triedset = new TreeSet<Problemid>();

		for (HashMap<String, Object> map : list) {
			String pid = map.get("pid").toString().trim();
			Problem problem = problemService.getProblemByPid(Integer.parseInt(pid));
			if (!openedProblemids.contains(problem.getProblemid())) {
				continue;
			}
			triedset.add(problem.getProblemid());
			int StatusCount = Integer.parseInt(map.get("COUNT").toString());

			switch (ServerOutput.JUDGEMENT.valueOf((Integer) map.get("judgement"))) {
			case AC:
				aclist.add(problem.getProblemid());
				triedset.remove(problem.getProblemid());
				break;
			case CE:
				cecount += StatusCount;
				break;
			case DN:
				break;
			case MLE:
				mlecount += StatusCount;
				break;
			case NA:
				break;
			case OLE:
				olecount += StatusCount;
				break;
			case RE:
				recount += StatusCount;
				break;
			case RF:
				break;
			case SE:
				break;
			case TLE:
				tlecount += StatusCount;
				break;
			case WA:
				wacount += StatusCount;
				break;
			case Waiting:
				break;
			default:
				break;

			}

		}
		user.setAclist(aclist);
		int ac = 0;
		for (Problemid problemid : aclist) {
			if (openedProblemids.contains(problemid)) {
				ac++;
			}
		}
		user.setTriedset(triedset);
		user.setAc(ac);
		user.setWa(wacount);
		user.setRe(recount);
		user.setTle(tlecount);
		user.setMle(mlecount);
		user.setOle(olecount);
		user.setCe(cecount);
		try {
			this.update(user);
		} catch (DataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重新計算所有人的 rankpoint<br>
	 * 此處的格式, 計算方式, 都必須與 setUserStatus 相符
	 * 
	 */
	public void rebuiltAllUserStatistic() {
		long starttime = System.currentTimeMillis();

		for (User user : new UserService().getAllUsers()) {
			this.rebuiltUserStatisticByDataBase(user);
		}
	}

	public void currectAllUserStatistic() {
		for (User user : new UserService().getAllUsers()) {
			try {
				user.checkUsername();
			} catch (DataException e) {
				user.setUsername("USERNAME_" + StringTool.escapeHtmlTag(user.getUsername()));
			}
			try {
				user.checkBirthyear();
			} catch (DataException e) {
				user.setBirthyear(2006);
			}
			try {
				user.checkTruename();
			} catch (DataException e) {
				user.setTruename("TRUENAME_" + StringTool.escapeHtmlTag(user.getTruename()));
			}
			try {
				user.checkEmail();
			} catch (DataException e) {
				user.setEmail(new User().getEmail());
			}
			this.update(user);
		}
	}

	/**
	 * 取得 account 陣列裡的 User Instance
	 * 
	 * @param accountarray
	 * @return
	 */
	public ArrayList<User> getUsersByArray(String[] accountarray) {
		UserService userService = new UserService();
		ArrayList<User> users = new ArrayList<User>();
		for (String account : accountarray) {
			users.add(userService.getUserByAccount(account));
		}
		return users;
	}

	public ArrayList<User> getUsersByROLEUSER(int pagenum) {
		return new UserDAO().getUsersByRoleOrderById(User.ROLE.USER, pagenum);
	}

	public ArrayList<User> getUsersByROLEDEBUGGER() {
		return new UserDAO().getUsersByRoleOrderById(User.ROLE.DEBUGGER, 1);
	}

	public ArrayList<User> getUsersByLabel(String label) {
		return new UserDAO().getUsersByLabel(label);
	}

	/**
	 * 取得題目管理員列表。
	 * 
	 * @return
	 */
	public ArrayList<User> getProblemManagers() {
		return new UserDAO().getUsersByConfig(User.CONFIG.ProblemManager);
	}

//	/**
//	 * 取出站務管理員
//	 * 
//	 * @return
//	 */
//	public ArrayList<User> getGeneralManagers() {
//		return new UserDAO().getUsersByConfig(User.CONFIG.GeneralManager);
//	}

	/**
	 * 取得 "教師" 列表。
	 * 
	 * @return
	 */
	public ArrayList<User> getVClassManagers() {
		return new UserDAO().getUsersByConfig(User.CONFIG.VClassManager);
	}

	/**
	 * 取得前幾名。
	 * 
	 * @param top
	 * @return
	 */
	public ArrayList<User> getTopUsers(int top) {
		return new UserDAO().getTopUsers(top);
	}

	/**
	 * 計算重複登入的次數。
	 * 
	 * @return
	 */
	public int getCountByMultiLogin(User user) {
		int count = 0;
		for (OnlineUser onlineUser : ApplicationScope.getOnlineUsers().values()) {
			if (user.getAccount().equals(onlineUser.getAccount())) {
				count++;
			}
		}
		return count;
	}

	public void doCleanSchoolid(int schoolid) {
		String usersql = "UPDATE users SET schoolid=0 WHERE schoolid=" + schoolid;
		new UserDAO().execute(usersql);
	}

	public void doMergeSchool(int fromschoolid, School to) {
		String sql = "UPDATE users SET schoolid=" + to.getId() + " WHERE schoolid=" + fromschoolid;
		new UserDAO().execute(sql);
	}

	/**
	 * 插入預設的 user
	 */
	public void insertInitUsers() {
		if (new UserService().getCountByAllUsers() > 0) {
			return;
		}

		String[] accounts = { "zero", "guest" };
		String[] passwds = { "!@#$zerojudge", "!@#$guest" };
		String[] usernames = { "管理員", "訪客" };
		String[] truenames = { "管理員", "公開測試帳號" };
		ROLE[] roles = { ROLE.MANAGER, ROLE.GUEST };
		int[] birthyears = { 2006, 2006 };
		String[] emails = { "zero@domain.com", "guest@domain.com" };
		for (int i = 0; i < accounts.length; i++) {
			User user = new User();
			user.setAccount(accounts[i]);
			user.setPasswd(passwds[i], passwds[i]);
			user.setRole(roles[i]);
			user.setUsername(usernames[i]);
			user.setTruename(truenames[i]);
			user.setBirthyear(birthyears[i]);
			user.setEmail(emails[i]);
			new UserService().insert(user);
		}
	}

}
