package tw.zerojudge.Tables;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.codehaus.jackson.annotate.JsonIgnore;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.SchoolService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Servlets.CreateVClassByTemplateServlet;
import tw.zerojudge.Servlets.EditVClassTemplateServlet;
import tw.zerojudge.Servlets.ShowCodeServlet;
import tw.zerojudge.Servlets.ShowDetailsServlet;
import tw.zerojudge.Servlets.TestjudgeServlet;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Servlets.Ajax.ReJudgeAjaxServlet;
import tw.zerojudge.Utils.Utils;

/**
 * 
 * @author jiangsir
 *
 */
public class OnlineUser extends User implements Serializable, HttpSessionBindingListener {
	private static final long serialVersionUID = 1L;
	/**
	 * 紀錄該 SessionUser 所設定的 locale 語系。
	 */
	private Locale session_locale = Locale.getDefault();
	private int loginid = 0;
	private String session_ip = "";
	private String sessionid = "";

	public OnlineUser() {
	}

	/**
	 * 將 user 轉成 SessionUserBean 進行向下轉型。
	 * 
	 * @param user
	 */
	public OnlineUser(HttpSession session, User user) {
		if (session != null) {
			this.setSessionid(session.getId());


			this.session_locale = new SessionScope(session).getSession_locale();
		}


		Object value;
		for (Field field : User.class.getDeclaredFields()) {
			if (field.getAnnotation(Persistent.class) == null) {
				continue;
			}
			Method getter;
			try {
				getter = User.class.getMethod(
						"get" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1));
				value = getter.invoke((User) user);
				if (value == null) {
					continue;
				}
				Method setter = User.class.getMethod(
						"set" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1),
						new Class[] { value.getClass() });
				setter.invoke(this, new Object[] { value });
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}


	public String getSessionid() {
		if (sessionid == null || "null".equals(sessionid.toLowerCase())) {
			return "";
		}
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		if (sessionid == null || "null".equals(sessionid.toLowerCase())) {
			return;
		}
		this.sessionid = sessionid;
	}

	public Locale getSession_locale() {
		return session_locale;
	}

	public void setSession_locale(Locale session_locale) {
		this.session_locale = session_locale;
	}

	public ArrayList<ROLE> getEditableROLEs() {
		ArrayList<ROLE> editableRoles = new ArrayList<ROLE>();
		for (ROLE role : User.ROLE.values()) {
			if (this.getRole().isHigherEqualThan(role)) {
				editableRoles.add(role);
			}
		}
		return editableRoles;
	}

	public int getLoginid() {
		return loginid;
	}

	public void setLoginid(int loginid) {
		this.loginid = loginid;
	}

	/**
	 * 判斷是否可以參加某個“隨堂測驗”
	 * 
	 * @param contest
	 * @return
	 * @throws RoleException
	 */
	private boolean canJoinVContest(Contest contest) throws RoleException {
		if (this.isIsGroupGuest()) {
			throw new RoleException("訪客無法參加" + contest.getBundle_Contest(session_locale) + "。");
		}
		if (this.getIsDEBUGGER()) {
			throw new RoleException("不能參加！" + contest.getBundle_Contest(session_locale) + "。");
		}

		VClass vclass = new VClassDAO().getVClassById(this.getVclassid());
		if (!vclass.isStudent(this.getId()) && !vclass.getIsOwner(this)) {
			throw new RoleException("您無法加入本" + contest.getBundle_Contest(session_locale));
		}

		Contestant contestant = contest.getContestantByUserid(this.getId());
		if (Contestant.STATUS.finish == contestant.getStatus()) {
			throw new RoleException("您已交卷，無法再次進入。");
		}
		if (Contestant.STATUS.kicked == contestant.getStatus()) {
			throw new RoleException("您已被踢出" + contest.getBundle_Contest(session_locale) + "，無法進入。");
		}

		return true;
	}

	/**
	 * 是否顯示 EditVClassTemplate 按鈕
	 * 
	 * @return
	 */
	public boolean isAccessible_EditVClassTemplate() {
		try {
			new EditVClassTemplateServlet().AccessFilter(this);
			return true;
		} catch (AccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 是否顯示 EditVClassTemplate 按鈕
	 * 
	 * @return
	 */
	public boolean isAccessible_CreateVClassByTemplate() {
		try {
			new CreateVClassByTemplateServlet().AccessFilter(this);
			return true;
		} catch (AccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 嘗試從 頁面傳入 servletURI 然後判斷該 servlet 是否有權限可以進入
	 * 
	 * @param servletURI
	 * @return
	 */
	public boolean IsAccessible(String servletURI) {
		return false;
	}

	/**
	 * 使用改版後的 parseRule! 20100202 <br>
	 * 右 > 左, regex
	 * 
	 * @param contest
	 * @return
	 * @throws RoleException
	 */
	public boolean canJoinContest(Contest contest) throws RoleException {
		if (this == null || this.getId().intValue() == 0) {
			throw new RoleException("您無法參加 " + contest.getBundle_Contest(session_locale) + "。");
		}
		if (this.isIsGroupGuest()) {
			throw new RoleException("訪客無法參加 " + contest.getBundle_Contest(session_locale) + "。");
		}
		if (this.getIsDEBUGGER() || contest.getIsOwner(this)) {
			throw new RoleException("主辦人無法參加 " + contest.getBundle_Contest(session_locale) + "。");
		}

		if (contest.isVContest()) { 
			VClass vclass = new VClassDAO().getVClassById(contest.getVclassid());
			if (!vclass.isStudent(this.getId())) {
				throw new RoleException("您無法加入本 " + contest.getBundle_Contest(session_locale) + "。");
			}

			Contestant contestant = contest.getContestantByUserid(this.getId());
			if (Contestant.STATUS.finish == contestant.getStatus()) {
				throw new RoleException("您已交卷，無法再次進入。");
			}
			if (Contestant.STATUS.kicked == contestant.getStatus()) {
				throw new RoleException("您已被踢出 " + contest.getBundle_Contest(session_locale) + "，無法進入。");
			}
			return true;
		} else { 
			switch (new Utils().parseRULE(contest.getUserrules(), this.getAccount())) {
			case ALLOW:
				Contestant contestant = contest.getContestantByUserid(this.getId());
				if (Contestant.STATUS.finish == contestant.getStatus()) {
					throw new RoleException("您已交卷，無法再次進入。");
				}
				if (Contestant.STATUS.kicked == contestant.getStatus()) {
					throw new RoleException("您已被踢出 " + contest.getBundle_Contest(session_locale) + "，無法進入。");
				}
				return true;
			case DENY:
				throw new RoleException("您無權限參加本 " + contest.getBundle_Contest(session_locale) + "。");
			case NOTDEFINE:
				throw new RoleException("您未被允許參加本 " + contest.getBundle_Contest(session_locale) + "。");
			default:
				throw new RoleException("您未被允許參加本 " + contest.getBundle_Contest(session_locale) + "。");
			}
		}

	}

	/**
	 * 某個 user 參加 某個 contest
	 * 
	 * @param contest
	 * @throws SQLException
	 */
	public void doJoinContest(Contest contest) throws RoleException {
		if (contest.isVContest()) {
			this.canJoinVContest(contest);
		} else {
			this.canJoinContest(contest);
		}

		ContestantDAO dao = new ContestantDAO();
		Contestant contestant = dao.getContestantByUseridContestid(this.getId(), contest.getId());

		contestant.setTeamname(getUsername());
		contestant.setEmail(getEmail());
		contestant.setSchool(new SchoolService().getSchoolById(getSchoolid()).getSchoolname());
		contestant.setIpset(getIpset());
		contestant.setStatus(Contestant.STATUS.online);
		contestant.setFinishtime(
				new Timestamp(new Date(contest.getStarttime().getTime() + contest.getTimelimit()).getTime()));

		if (contestant.getIsNull()) {
			contestant.setContestid(contest.getId());
			contestant.setUserid(this.getId());
			int contestantid = dao.insert(contestant);
			contestant.setId(contestantid);
		} else {
			dao.update(contestant);
		}

		this.setJoinedcontestid(contest.getId());
		new UserService().update(this);
	}

	/**
	 * 使用者主動離開測驗。
	 * 
	 * @throws DataException
	 */
	public void doLeaveContest() throws DataException {
		Contest contest = new ContestService().getContestById(this.getJoinedcontestid());
		Contestant contestant = contest.getContestantByUserid(this.getId());
		contestant.setStatus(Contestant.STATUS.leave);
		new ContestantDAO().update(contestant);

		this.setJoinedcontestidToNONE();
		new UserService().update(this);
	}

	/**
	 * 登出相關動作, 包含 session 逾時也執行 doLogout <br>
	 * 
	 * @param session
	 * @throws DataException
	 * @throws AccessException
	 */
	public void doLogout() throws DataException, AccessException {


		this.setSessionid(null);
	}

	/**
	 * 取得使用者目前所加入的 Contest
	 * 
	 * @return
	 */
	@JsonIgnore
	public Contest getJoinedContest() {
		return new ContestService().getContestById(this.getJoinedcontestid());
	}

	public String getSession_ip() {
		return session_ip;
	}

	public void setSession_ip(String session_ip) {
		this.session_ip = session_ip;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getIsLoopback() {
		for (IpAddress ip : this.getIpset()) {
			if (ip.isLoopbackAddress()) {
				return true;
			}
		}
		return false;
	}

	public long getIdle() {
		HttpSession session = SessionFactory.getSessionById(sessionid);
		if (session == null) {
			return -1;
		}
		return System.currentTimeMillis() - session.getLastAccessedTime();
	}

	@Override
	public String toString() {
		return "userid=" + this.getId() + ", account=" + this.getAccount() + "(ROLE=" + this.getRole() + ")";
	}

	public boolean isNullOnlineUser() {
		if (this.getSessionid().equals(new User().getSessionid())
				&& this.getAccount().equals(new User().getAccount())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否可以將 solution 移入 Contest 的 removed solution 當中
	 * 
	 * @return
	 */
	public boolean isCanRemoveSolution() {
		return this.getIsDEBUGGER();
	}

	public boolean isSolutionRejudgable(Solution solution) {
		try {
			new ReJudgeAjaxServlet().AccessFilter(this, solution);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public boolean getIsTestjudgeAccessible() {
		try {
			new TestjudgeServlet().AccessFilter(this, new ContestService().getContestById(this.getJoinedcontestid()));
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param problem
	 * @return
	 */
	public boolean getIsAccepted(Problem problem) {
		return this.getAclist().contains(problem.getProblemid());
	}

	/**
	 * 根據使用者權限。判斷該 solution 是否可以重測。
	 * 
	 * @return
	 */
	public boolean isRejudgable(Solution solution) {
		return this.isSolutionRejudgable(solution);
	}

	public boolean canShowDetails(Solution solution) {
		try {
			new ShowDetailsServlet().AccessFilter(this, solution);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public boolean isProblemRejudgable(Problem problem) throws AccessException {
		try {
			new UpdateProblemServlet().AccessFilter(this, problem);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public boolean isShowCodeAccessible(Solution solution) {
		return this.isProblemLevelManager() || new ShowCodeServlet().isAccessible(this, solution);
	}

	public boolean accessFilter_UpdateProblem(Problem problem) {
		try {
			new UpdateProblemServlet().AccessFilter(this, problem);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	/**
	 * 取得該登入的使用者所屬的課程
	 * 
	 * @return
	 */
	public ArrayList<VClass> getBelongedVClasses() {
		return new VClassDAO().getVClassesByUserid(getId());
	}


	//

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		synchronized (ApplicationScope.getOnlineUsers()) {
			ApplicationScope.getOnlineUsers().put(event.getSession().getId(), this);
		}
		ApplicationScope.getOnlineSessions().put(event.getSession().getId(), event.getSession());
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		synchronized (ApplicationScope.getOnlineUsers()) {
			ApplicationScope.getOnlineUsers().remove(event.getSession().getId());
		}
	}
}
