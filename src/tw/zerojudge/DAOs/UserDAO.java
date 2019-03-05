package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Judges.JudgeObject;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Resource;
import tw.zerojudge.Utils.Utils;

public class UserDAO extends SuperDAO<User> {
	static class UserSql {
		private HashSet<String> where = new HashSet<String>();
		private HashSet<String> orderby = new HashSet<String>();
		private int page = 0;

		@Override
		public String toString() {
			String sql = "SELECT * FROM users";
			String wherestring = "";
			if (where.size() != 0) {
				for (String s : where) {
					if (!"".equals(wherestring)) {
						s = " AND " + s;
					}
					wherestring += s;
				}
			}

			String orderbystring = "";
			if (orderby.size() != 0) {
				for (String s : orderby) {
					if (!"".equals(orderbystring)) {
						s = ", " + s;
					}
					orderbystring += s;
				}
			}

			String limit = "";
			if (page > 0) {
				limit = " LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE;
			}
			return sql + wherestring + orderbystring + limit;
		}
	}

	Logger logger = Logger.getLogger(this.getClass().getName());
	private static int PAGESIZE = ApplicationScope.getAppConfig().getPageSize();

	public UserDAO() {
	}

	public int executeCount(TreeMap<String, Object> fields) {
		return super.executeCount("users", fields);
	}

	/**
	 * 應該由 CacheUsers 來處理應該會比較快
	 * 
	 * @param account
	 * @param limit
	 * @return
	 */
	public String getAccountSuggest(String account, int limit) {
		if (account == null || "".equals(account)) {
			return "";
		}
		account = StringEscapeUtils.escapeSql(account);

		if (limit > PAGESIZE) {
			limit = PAGESIZE;
		}
		String sql = "SELECT * FROM users WHERE account LIKE ? LIMIT 0, " + limit;

		String result = "";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, account + "%");
			for (User user : executeQuery(pstmt, User.class)) {
				result += user.getAccount() + "\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<User> getUsersByConfig(User.CONFIG config) {
		String sql = "SELECT * FROM users WHERE config&(1 << " + config.ordinal() + ");";
		return this.executeQuery(sql, User.class);
	}

	//


	//

	/**
	 * 為了使用 jQuery 的 autocomplete 要傳入一個 ["","","",""] 形式的 array
	 * 
	 * @return
	 */
	public String getUsersCSV() {
		StringBuffer array = new StringBuffer(new UserService().getCountByAllUsers() * 5);
		array.append("[");
		for (User user : new UserService().getAllUsers()) {
			array.append((array.length() == 1 ? "" : ",") + user.getAccount());
		}
		array.append("]");
		return array.toString();
	}

	/**
	 * 以累計的方式計算 user statistic 必須要知道這個 solution 是不是新發出來的，如果是新的，就要往上累計。 可是如果是原本的經由
	 * rejudge 而來的，就必須先扣除原本的統計數字，再往上累計。 <br>
	 * 20100512 因此，關鍵在於如何認定是不是新的 solution。
	 * 
	 */
	private User rebuiltUser_accumulate(User user, Solution solution) {
		Problem problem = new ProblemService().getProblemByPid(solution.getPid());
		if (Problem.DISPLAY.hide == problem.getDisplay()) {
			return user;
		}
		switch (solution.getJudgement()) {
		case AC:
			TreeSet<Problemid> aclist = user.getAclist();
			aclist.add(problem.getProblemid());
			user.setAclist(aclist);
			user.setAc(aclist.size());
			break;
		case CE:
			user.setCe(user.getCe() + 1);
			break;
		case DN:
			break;
		case MLE:
			user.setMle(user.getMle() + 1);
			break;
		case NA:
			break;
		case OLE:
			user.setOle(user.getOle() + 1);
			break;
		case RE:
			user.setRe(user.getRe() + 1);
			break;
		case RF:
			break;
		case SE:
			break;
		case TLE:
			user.setTle(user.getTle() + 1);
			break;
		case WA:
			user.setWa(user.getWa() + 1);
			break;
		case Waiting:
			break;
		default:
			break;

		}
		return user;
	}

	/**
	 * 以累計的方式計算 user statistic 必須要知道這個 solution 是不是新發出來的，如果是新的，就要往上累計。 可是如果是原本的經由
	 * rejudge 而來的，就必須先扣除原本的統計數字，再往上累計。 <br>
	 * 20100512 因此，關鍵在於如何認定是不是新的 solution。
	 * 
	 */
	public void rebuiltUserStatistic_SPEEDUP(User user, Solution solution, JudgeObject.PRIORITY from) {
		long starttime = System.currentTimeMillis();
		user = this.rebuiltUser_accumulate(user, solution);
		user.setLastsolutionid(Math.max(solution.getId(), user.getLastsolutionid()));
		try {
			this.update(user);
		} catch (DataException e) {
			e.printStackTrace();
		}
		logger.info("solutionid=" + solution.getId() + " rebuiltUserStatistic_SPEEDUP 費時："
				+ (System.currentTimeMillis() - starttime) + "ms");
	}

	/**
	 * 取得目前存在的 user 的 tabs
	 * 
	 * @deprecated user tab 已經廢止
	 * @return
	 */

	/**
	 * 當一個 contest 要結束的時候，強制將所有 User 退出 Contest<br/>
	 * 直接從資料庫將已加入的人全部移出競賽。<br/>
	 * 
	 * @param joinedcontestid
	 */
	public void leaveJoinedContestid(Integer joinedcontestid) {
		String sql = "UPDATE users SET joinedcontestid=0 WHERE joinedcontestid=" + joinedcontestid;
		this.execute(sql);
	}


	/**
	 * 透過 General Filter 的分析 privilege.<br>
	 * 差別在，GroupUser 的權限裡面，沒有登記的視為允許。因為要讓一些不經過 Login Filter <br>
	 * 的 servlet 也能被 privilege 所控制。
	 * 
	 * @param privileges
	 * @param URI
	 * @return
	 */
	public static Resource.MESSAGE parseGeneralPrivilege_NOUSE(LinkedHashSet<String> privileges, String URI) {
		URI = URI.substring(URI.lastIndexOf('/') + 1);
		if (URI.equals("")) {
			return Resource.MESSAGE.Privilege_ALLOWED;
		}
		switch (new Utils().parseRule(privileges, URI)) {
		case Utils.rule_ALLOW:
			return Resource.MESSAGE.Privilege_ALLOWED;
		case Utils.rule_NOTDEFINE:
			return Resource.MESSAGE.Privilege_ALLOWED;
		case Utils.rule_DENY:
			return Resource.MESSAGE.Privilege_FORBIDDEN;
		default:
			return Resource.MESSAGE.Privilege_UNKNOWN;
		}
	}


	/**
	 * 強迫所有 Online User logout
	 * 
	 */
	public void doForcedLogout() {
		for (String sessionid : ApplicationScope.getOnlineUsers().keySet()) {
			HttpSession session = SessionFactory.getSessionById(sessionid);
			UserFactory.getOnlineUser(session).doLogout();
		}
	}

	/**
	 * 取得當前(即時)排名
	 * 
	 * @return
	 */
	public int getCurrentRank(User user) {

		String sql = "SELECT id FROM users WHERE role='" + User.ROLE.USER + "' ORDER BY " + User.UserRankRule;
		int count = 0;
		for (long userid : this.executeQueryId(sql)) {
			if (userid == user.getId()) {
				return count + 1;
			}
			count++;
		}
		return 0;
	}

	/**
	 * 重新計算 某使用者的 排名
	 * 
	 * @param userid
	 * @return
	 */

	/**
	 * 獲取 DB 當中的 User
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(Integer id) {
		String sql = "SELECT * FROM users WHERE id=" + id;
		for (User user : new UserDAO().executeQuery(sql, User.class)) {
			return user;
		}
		return UserFactory.getNullUser();
	}

	//

	/**
	 * 
	 * @param fields
	 * @param orderby
	 * @param page
	 * @return
	 */
	public ArrayList<User> getUsersByFields(TreeMap<String, Object> fields, String orderby, int page) {
		String sql = "SELECT * FROM users " + this.makeFields(fields, orderby, page);
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			int i = 1;
			for (String key : fields.keySet()) {
				pstmt.setObject(i++, fields.get(key));
			}
			return executeQuery(pstmt, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}



	public ArrayList<User> getAllUsers() {
		String sql = "SELECT * FROM users";
		return this.executeQuery(sql, User.class);
	}

	public int getCountByAllUsers() {
		return this.executeCount("users", new TreeMap<String, Object>());
	}

	/**
	 * 用帳號及密碼來取 User, 不經 Cache 直接由資料庫取用。並放入 Cache
	 * 
	 * @param account
	 * @param passwd
	 * @return
	 * @throws SQLException
	 */
	public User getUserByAccountPasswd(String account, String passwd) {
		if (account == null || "".equals(account) || passwd == null) {
			return UserFactory.getNullUser();
		}
		String sql = "SELECT * FROM users WHERE account=? AND md5passwd=md5(?);";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, account);
			pstmt.setString(2, passwd);
			for (User user : executeQuery(pstmt, User.class)) {
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return UserFactory.getNullUser();
	}

	/**
	 * 取前幾名
	 * 
	 * @param number
	 * @return
	 */
	public ArrayList<User> getTopUsers(int number) {
		String sql = "SELECT * FROM users WHERE role='" + User.ROLE.USER + "' " + " ORDER BY " + User.UserRankRule
				+ " LIMIT 0," + number;
		return executeQuery(sql, User.class);
	}

	public ArrayList<User> getUsersByROLEUSER(int page) {
		if (page == 0) {
			return new ArrayList<User>();
		}
		String sql = "SELECT * FROM users WHERE role='" + User.ROLE.USER + "' ORDER BY " + User.UserRankRule;
		sql += " LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			return this.executeQuery(pstmt, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<User>();
	}

	public ArrayList<User> getusersBySchoolid(int schoolid, int page) {
		String sql = "SELECT * FROM users WHERE role='" + User.ROLE.USER + "' AND schoolid=? ORDER BY "
				+ User.UserRankRule;
		if (page != 0) {
			sql += " LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE;
		}
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, schoolid);
			return this.executeQuery(pstmt, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<User>();
	}

	public ArrayList<User> getUsersByTabLIKE(String tabn, int page) {
		String sql = "SELECT * FROM users WHERE role='" + User.ROLE.USER + "' AND tab LIKE ? ORDER BY "
				+ User.UserRankRule;
		if (page != 0) {
			sql += " LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE;
		}
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, tabn + "%");
			return this.executeQuery(pstmt, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<User>();
	}


	//

	@Override
	protected synchronized int insert(User user) throws DataException {

		String sql = "INSERT INTO users (account, authhost, md5passwd, username, "
				+ "truename, birthyear, schoolid, label, email, picture, pictureblob, picturetype, comment, "
				+ "`role`, createby, registertime, lastlogin, lastsolutionid, "
				+ "extraprivilege, ipset, aclist, triedset, config) VALUES"
				+ "(?,?,md5(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, user.getAccount());
			pstmt.setString(2, user.getAuthhost().name());
			pstmt.setString(3, user.getPasswd());
			pstmt.setString(4, user.getUsername());
			pstmt.setString(5, user.getTruename());
			pstmt.setInt(6, user.getBirthyear());
			pstmt.setInt(7, user.getSchoolid());
			pstmt.setString(8, user.getLabel());
			pstmt.setString(9, user.getEmail());
			pstmt.setString(10, user.getPicture());
			pstmt.setBytes(11, user.getPictureblob());
			pstmt.setString(12, user.getPicturetype());
			pstmt.setString(13, user.getComment());
			pstmt.setString(14, user.getRole().name());
			pstmt.setInt(15, user.getCreateby());
			pstmt.setTimestamp(16, new Timestamp(user.getRegistertime().getTime()));
			pstmt.setTimestamp(17, new Timestamp(user.getLastlogin().getTime()));
			pstmt.setInt(18, user.getLastsolutionid());
			pstmt.setString(19, user.getExtraprivilege());
			pstmt.setString(20, user.getIpset().toString());
			pstmt.setString(21, user.getAclist().toString());
			pstmt.setString(22, user.getTriedset().toString());
			pstmt.setInt(23, user.getConfig());
			this.executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();

			if (e.getLocalizedMessage().contains("Duplicate entry")) {
				throw new DataException("使用者(#" + user.getId() + ":" + user.getAccount() + ")已存在！", e);
			} else if (e.getLocalizedMessage().toLowerCase().contains("data too long")) {
				throw new DataException("資料欄位過長！(" + e.getLocalizedMessage() + ")", e);
			} else {
				throw new DataException("使用者新增失敗！請再次確認。(" + e.getLocalizedMessage() + ")");
			}
		}
		return id;
	}

	@Override
	protected synchronized int update(User user) throws DataException {

		String SQL = "UPDATE users SET authhost=?, username=?, truename=?, "
				+ "birthyear=?, schoolid=?, vclassid=?, email=?, picture=?,pictureblob=?,picturetype=?, sessionid=?, comment=?, "
				+ "role=?, label=?, joinedcontestid=?, extraprivilege=?, ipset=?, "
				+ "country=?, ipinfo=?, registertime=?, lastlogin=?, lastsolutionid=?, "
				+ "userlanguage=?, ac=?, aclist=?, triedset=?, wa=?, tle=?, mle=?, ole=?,"
				+ " re=?, ce=?, config=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setString(1, user.getAuthhost().name());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getTruename());
			pstmt.setInt(4, user.getBirthyear());
			pstmt.setInt(5, user.getSchoolid());
			pstmt.setInt(6, user.getVclassid());
			pstmt.setString(7, user.getEmail());
			pstmt.setString(8, user.getPicture());
			pstmt.setBytes(9, user.getPictureblob());
			pstmt.setString(10, user.getPicturetype());
			pstmt.setString(11, user.getSessionid());
			pstmt.setString(12, user.getComment());
			pstmt.setString(13, user.getRole().name());
			pstmt.setString(14, user.getLabel());
			pstmt.setInt(15, user.getJoinedcontestid());
			pstmt.setString(16, user.getExtraprivilege());
			pstmt.setString(17, user.getIpset().toString());
			pstmt.setString(18, user.getCountry());
			pstmt.setString(19, user.getIpinfo());
			pstmt.setTimestamp(20, new Timestamp(user.getRegistertime().getTime()));
			pstmt.setTimestamp(21, new Timestamp(user.getLastlogin().getTime()));
			pstmt.setInt(22, user.getLastsolutionid());
			pstmt.setString(23, user.getUserlanguage().getName());
			pstmt.setInt(24, user.getAc());
			pstmt.setString(25, user.getAclist().toString());
			pstmt.setString(26, user.getTriedset().toString());
			pstmt.setInt(27, user.getWa());
			pstmt.setInt(28, user.getTle());
			pstmt.setInt(29, user.getMle());
			pstmt.setInt(30, user.getOle());
			pstmt.setInt(31, user.getRe());
			pstmt.setInt(32, user.getCe());
			pstmt.setInt(33, user.getConfig());
			pstmt.setInt(34, user.getId());
			result = super.executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			user = this.getUserById(user.getId());
			UserService.HashUsers.put(user.getId(), user);
			e.printStackTrace();
			throw new DataException("使用者更新錯誤！(" + e.getLocalizedMessage() + ")");
		}
		UserService.HashUsers.put(user.getId(), user);
		return result;
	}

	/**
	 * 專用於更新 user role
	 * 
	 * @param user
	 * @return
	 * @throws DataException
	 */
	public synchronized int updateUserRole(User user) throws DataException {
		String SQL = "UPDATE users SET role=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setString(1, user.getRole().toString());
			pstmt.setInt(2, user.getId());
			result = super.executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			user = this.getUserById(user.getId());
			UserService.HashUsers.put(user.getId(), user);
			e.printStackTrace();
			throw new DataException("使用者更新錯誤！(" + e.getLocalizedMessage() + ")");
		}
		UserService.HashUsers.put(user.getId(), user);
		return result;

	}

	/**
	 * 專用於更新 user config
	 * 
	 * @param user
	 * @return
	 * @throws DataException
	 */
	public synchronized int updateUserConfig(User user) throws DataException {
		String SQL = "UPDATE users SET config=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setInt(1, user.getConfig());
			pstmt.setInt(2, user.getId());
			result = super.executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			user = this.getUserById(user.getId());
			UserService.HashUsers.put(user.getId(), user);
			e.printStackTrace();
			throw new DataException("使用者更新錯誤！(" + e.getLocalizedMessage() + ")");
		}
		UserService.HashUsers.put(user.getId(), user);
		return result;
	}

	/**
	 * 將密碼明碼 update 為 md5 編碼
	 * 
	 * @param user
	 * @return
	 * @throws AccessException
	 */
	public synchronized int updatePasswd(User user) throws DataException {

		String SQL = "UPDATE users SET md5passwd=md5(?) WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = super.getConnection().prepareStatement(SQL);
			pstmt.setString(1, user.getPasswd());
			pstmt.setInt(2, user.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			throw new DataException("變更密碼有誤！(" + e.getMessage() + ")");
		}
		return result;
	}

	public synchronized int updateAccount(User user) {
		String SQL = "UPDATE users SET account=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = super.getConnection().prepareStatement(SQL);
			pstmt.setString(1, user.getAccount());
			pstmt.setInt(2, user.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			throw new DataException("變更 account 有誤！(" + e.getMessage() + ")");
		}
		return result;
	}

	@Override
	public boolean delete(int id) {
		return this.execute("DELETE FROM users WHERE id=" + id);
	}

	/**
	 * 用分頁方式取出 GroupUser
	 * 
	 * @param pagenum
	 * @return
	 * @throws DataException
	 */
	protected ArrayList<User> getUsersByRoleOrderById(User.ROLE role, int pagenum) {
		String query = "FROM users WHERE role='" + role + "' ";
		String sql = "SELECT * " + query + "ORDER BY id DESC LIMIT " + (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		return new UserDAO().executeQuery(sql, User.class);
	}

	/**
	 * 依 label 來取得 user list
	 * 
	 * @param label
	 * @return
	 */
	public ArrayList<User> getUsersByLabel(String label) {
		if (label == null || "".equals(label)) {
			return new ArrayList<User>();
		}
		String sql = "SELECT * FROM users WHERE role='" + User.ROLE.USER + "' AND label LIKE ? ORDER BY id ASC";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, "%" + label + "%");
			return this.executeQuery(pstmt, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<User>();
		}
	}

	/**
	 * 取得已經加入某個 contest 的 User 列表。
	 * 
	 * @return
	 */
	public ArrayList<User> getUsersByContestid_NOTUSED(int contestid) {
		String sql = "SELECT * FROM users WHERE joinedcontestid=" + contestid;
		return this.executeQuery(sql, User.class);
	}

}
