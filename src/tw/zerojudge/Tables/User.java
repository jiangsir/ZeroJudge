/**
 * idv.jiangsir.objects - User.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.annotate.JsonIgnore;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Annotations.Privilege;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Filters.RoleFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Language;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.DAOs.SchoolService;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.SessionFactory;
import tw.zerojudge.Utils.*;

/**
 * @author jiangsir
 * 
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5415896803375058059L;

	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "account")
	private String account = "";

	public enum AUTHHOST {
		Google, 
		localhost; 
	}

	@Persistent(name = "authhost")
	private AUTHHOST authhost = AUTHHOST.localhost;
	@Persistent(name = "username")
	private String username = "unknown";
	@Persistent(name = "md5passwd")
	private String md5passwd = "";
	@Persistent(name = "truename")
	private String truename = "";
	@Persistent(name = "birthyear")
	private Integer birthyear = 2006;
	@Persistent(name = "schoolid")
	private Integer schoolid = 0;
	@Persistent(name = "vclassid")
	private Integer vclassid = 0;
	@Persistent(name = "email")
	private String email = "UNKNOWN@zerojudge.tw";
	@Persistent(name = "picture")
	private String picture = "";
	@Persistent(name = "pictureblob")
	private byte[] pictureblob = new byte[] {};
	@Persistent(name = "picturetype")
	private String picturetype = "";
	@Persistent(name = "sessionid")
	private String sessionid = "";
	@Persistent(name = "comment")
	private String comment = "";

	//

	public enum ROLE {
		DEBUGGER, 
		MANAGER, 
		USER, 
		GUEST; 
		public boolean isHigherEqualThan(ROLE role) {
			return this.ordinal() <= role.ordinal();
		}

		public boolean isLowerEqualThan(ROLE role) {
			return this.ordinal() >= role.ordinal();
		}
	}

	@Persistent(name = "role")
	private ROLE role = ROLE.GUEST;

	@Persistent(name = "createby")
	private Integer createby = 0; 
	@Persistent(name = "label")
	private String label = "";
	private static int NONE_CONTESTID = 0;
	@Persistent(name = "joinedcontestid")
	private Integer joinedcontestid = NONE_CONTESTID;
	@Persistent(name = "extraprivilege")
	private String extraprivilege = "";
	@Persistent(name = "ipset")
	private ArrayList<IpAddress> ipset = new ArrayList<IpAddress>();
	@Persistent(name = "country")
	private String country = "";
	@Persistent(name = "ipinfo")
	private String ipinfo = "";
	@Persistent(name = "registertime")
	private Timestamp registertime = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "lastlogin")
	private Timestamp lastlogin = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "lastsolutionid")
	private Integer lastsolutionid = 0;
	@Persistent(name = "userlanguage")
	private Language userlanguage = new Language("CPP", "cpp");
	@Persistent(name = "ac")
	private Integer ac = 0;
	@Persistent(name = "aclist")
	private TreeSet<Problemid> aclist = new TreeSet<Problemid>();
	@Persistent(name = "triedset")
	private TreeSet<Problemid> triedset = new TreeSet<Problemid>();
	@Persistent(name = "wa")
	private Integer wa = 0;
	@Persistent(name = "tle")
	private Integer tle = 0;
	@Persistent(name = "mle")
	private Integer mle = 0;
	@Persistent(name = "ole")
	private Integer ole = 0;
	@Persistent(name = "re")
	private Integer re = 0;
	@Persistent(name = "ce")
	private Integer ce = 0;
	@Persistent(name = "config")
	private Integer config = Integer.valueOf("0000001", 2);

	public static final String UserRankRule = "ac DESC, ce ASC, wa ASC";
	private String passwd = ""; 

	private HashSet<Class<? extends HttpServlet>> denyServlet = new HashSet<Class<? extends HttpServlet>>();

	private HashSet<Class<? extends HttpServlet>> allowServlet = new HashSet<Class<? extends HttpServlet>>();

	public static enum CONFIG {
		ENABLE, 
		QualifiedAuthor, 
		ContestManager, 
		InsertProblem, 
		ProblemManager, 
		VClassManager, 
	}

	public static enum MANAGER {
		GeneralManager, ProblemManager, VClassManager, ContestManager
	}

	Logger logger = Logger.getLogger(this.getClass().getName());

	public User() {
	}

	/** ********************************************************************* */

	public Integer getAc() {
		return ac;
	}

	public String getAccount() {
		return account;
	}

	public AUTHHOST getAuthhost() {
		return authhost;
	}

	public Integer getBirthyear() {
		return birthyear;
	}

	public Integer getCe() {
		return ce;
	}

	public String getComment() {
		return comment;
	}

	public Integer getJoinedcontestid() {
		return joinedcontestid;
	}

	public void setJoinedcontestid(String joinedcontestid) throws DataException {
		if (joinedcontestid == null) {
			throw new NullPointerException();
		}
		if (!joinedcontestid.matches("[0-9]+")) {
			throw new DataException("不正確的 contestid=" + joinedcontestid);
		}
		this.joinedcontestid = Integer.valueOf(joinedcontestid);
	}

	public void setJoinedcontestid(Integer joinedcontestid) {
		this.joinedcontestid = joinedcontestid;
	}

	public void setJoinedcontestidToNONE() {
		this.setJoinedcontestid(User.NONE_CONTESTID);
	}

	public String getCountry() {
		return country;
	}

	public String getEmail() {
		return email;
	}

	public String getExtraprivilege() {
		return extraprivilege;
	}

	public void setExtraprivilege(String extraprivilege) throws DataException {
		if (extraprivilege == null) {
			return;
		}
		this.extraprivilege = extraprivilege;
	}


	public String getIpinfo() {
		return ipinfo;
	}

	public String getLabel() {
		return label;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public Integer getLastsolutionid() {
		return lastsolutionid;
	}

	public void setLastsolutionid(Integer lastsolutionid) {
		this.lastsolutionid = lastsolutionid;
	}

	public Integer getMle() {
		return mle;
	}

	public Integer getOle() {
		return ole;
	}

	public Integer getRe() {
		return re;
	}

	public Date getRegistertime() {
		return registertime;
	}

	public Integer getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(Integer schoolid) {
		this.schoolid = schoolid;
	}

	public void setSchoolid(String schoolid) {
		if (schoolid == null) {
			return;
		}
		if (!schoolid.matches("[0-9]+")) {
			this.setSchoolid(new SchoolService().getSchoolByName(schoolid).getId());
			return;
		}
		this.setSchoolid(Integer.parseInt(schoolid));
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

	public Integer getTle() {
		return tle;
	}

	public String getTruename() {
		return truename;
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getMd5passwd() {
		return md5passwd;
	}

	public void setMd5passwd(String md5passwd) {
		this.md5passwd = md5passwd;
	}

	public Integer getWa() {
		return wa;
	}


	public void setAc(Integer ac) {
		if (ac == null) {
			return;
		}
		this.ac = ac;
	}

	public void setAccount(String account) {
		if (account == null) {
			return;
		}
		if ("".equals(account.trim())) {
			throw new DataException("account 不可為空字串。");
		}
		this.account = account;
	}


	public static void checkAccount(String account) throws DataException {
		int length = 4;
		if (account != null && account.length() >= length && account.matches("[a-zA-Z_0-9]+")) {
		} else {
			throw new DataException("錯誤!! (" + account + ") 不符合帳號規則，請使用文數字及_，並至少用 " + length + " 個字元");
		}

	}

	public TreeSet<Problemid> getAclist() {
		return aclist;
	}

	public void setAclist(TreeSet<Problemid> aclist) {
		this.aclist = aclist;
	}

	public void setAclist(String aclist) {
		if (aclist == null) {
			return;
		}
		this.setAclist(StringTool.String2Problemidset(aclist));
	}

	public TreeSet<Problemid> getTriedset() {
		return triedset;
	}

	public void setTriedset(TreeSet<Problemid> triedset) {
		this.triedset = triedset;
	}

	public void setTriedset(String triedset) {
		if (triedset == null) {
			return;
		}
		this.setTriedset(StringTool.String2Problemidset(triedset));
	}

	public void setAuthhost(String authhost) {
		if (authhost == null) {
			return;
		}
		this.setAuthhost(AUTHHOST.valueOf(authhost));
	}

	public void setAuthhost(AUTHHOST authhost) {
		this.authhost = authhost;
	}

	public void setBirthyear(Integer birthyear) {
		this.birthyear = birthyear;
	}

	public void setBirthyear(String birthyear) throws DataException {
		if (birthyear == null || !birthyear.matches("[0-9]+")) {
			return;
		}
		this.setBirthyear(Integer.parseInt(birthyear));
	}

	public void checkBirthyear() throws DataException {
		if (!islegelBirthyear(birthyear)) {
			throw new DataException("錯誤!! 出生年不正確!! (" + birthyear + "), userid=" + this.getId());
		}
	}

	public void setCe(Integer ce) {
		if (ce == null) {
			return;
		}
		this.ce = ce;
	}

	public void setComment(String comment) {
		if (comment == null) {
			return;
		}
		this.comment = StringTool.escapeHtmlTag(comment);
	}

	public void setCountry(String country) {
		if (country == null) {
			return;
		}
		this.country = country;
	}

	public void setEmail(String email) {
		if (email == null) {
			return;
		}
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		if (picture == null)
			return;
		this.picture = picture;
	}

	public byte[] getPictureblob() {
		return pictureblob;
	}

	public void setPictureblob(byte[] pictureblob) {
		if (pictureblob == null || pictureblob.length == 0) {
			return;
		}
		this.pictureblob = pictureblob;
	}

	public void setPictureblob(String pictureblob) {
		if (pictureblob == null) {
			return;
		}
		this.setPictureblob(pictureblob.getBytes());
	}

	public String getPicturetype() {
		return picturetype;
	}

	public void setPicturetype(String picturetype) {
		if (picturetype == null) {
			return;
		}
		this.picturetype = picturetype;
	}

	/**
	 * inline image
	 * 
	 * @return
	 */
	public String getPictureBase64() {
		final Base64 base64 = new Base64();
		String imageBase64 = base64.encodeToString(this.getPictureblob());
		return "data:" + this.getPicturetype() + ";base64," + imageBase64;
	}

	public void checkEmail() throws DataException {
		if (email == null) {
			throw new DataException("錯誤!! email 為 null!");
		} else if ("".equals(email)) {
			throw new DataException("錯誤!! email 不可為空!!");
		} else if (!islegalEmail(email)) {
			throw new DataException("(" + email + ") 錯誤!! email 格式錯誤!!");
		}
	}

	public Integer getVclassid() {
		return vclassid;
	}

	public void setVclassid(Integer vclassid) {
		if (vclassid == null) {
			return;
		}
		this.vclassid = vclassid;
	}

	//

	public void setIpset(String ipset) {
		if (ipset == null || "".equals(ipset.trim())) {
			return;
		}
		this.setIpset(StringTool.String2IpAddressList(ipset));
	}


	public ArrayList<IpAddress> getIpset() {
		return ipset;
	}

	public void setIpset(ArrayList<IpAddress> ipset) {
		this.ipset = ipset;
	}

	public void setIpinfo(String ipinfo) {
		if (ipinfo == null) {
			return;
		}
		this.ipinfo = ipinfo;
	}

	public void setLabel(String label) throws DataException {
		if (label == null) {
			return;
		}
		this.label = label;
	}

	public void setLastlogin(Timestamp lastlogin) {
		if (lastlogin == null) {
			return;
		}
		this.lastlogin = lastlogin;
	}

	public void setLastlogin(String lastlogin) throws DataException {
		try {
			this.setLastlogin(Timestamp.valueOf(lastlogin));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new DataException(e.getLocalizedMessage());
		}
	}

	public void setMle(Integer mle) {
		if (mle == null) {
			return;
		}
		this.mle = mle;
	}

	public void setOle(Integer ole) {
		if (ole == null) {
			throw new NullPointerException();
		}
		this.ole = ole;
	}

	public void setRe(Integer re) {
		if (re == null) {
			return;
		}
		this.re = re;
	}

	public void setRegistertime(Timestamp registertime) {
		if (registertime == null) {
			return;
		}
		this.registertime = registertime;
	}

	public void setRegistertime(String registertime) throws DataException {
		try {
			this.setRegistertime(Timestamp.valueOf(registertime));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new DataException(e.getLocalizedMessage());
		}
	}

	public void setTle(Integer tle) {
		if (tle != null) {
			this.tle = tle;
		}
	}

	public void setTruename(String truename) {
		if (truename == null) {
			return;
		}
		this.truename = StringTool.escapeHtmlTag(truename);
	}

	public void checkTruename() throws DataException {
		truename = truename.trim();
		String truename2 = StringTool.escapeHtmlTag(truename);
		if (!truename.equals(truename2)) {
			throw new DataException("(" + truename + ") 您的姓名可能含有 html 標籤！");
		}
	}

	@JsonIgnore
	public ROLE[] getROLEs() {
		return User.ROLE.values();
	}

	public ROLE getRole() {
		return role;
	}

	public void setRole(ROLE role) {
		this.role = role;
	}

	public void setRole(String role) throws DataException {
		if (role == null) {
			return;
		}
		this.setRole(ROLE.valueOf(role));
	}

	public Integer getCreateby() {
		return createby;
	}

	public void setCreateby(Integer createby) {
		this.createby = createby;
	}

	public void setId(Integer id) {
		if (id == null) {
			return;
		}
		this.id = id;
	}

	public Language getUserlanguage() {
		return userlanguage;
	}

	public void setUserlanguage(Language userlanguage) {
		if (userlanguage == null) {
			return;
		}
		this.userlanguage = userlanguage;
	}

	public void setUserlanguage(String userlanguage) {
		if (userlanguage == null) {
			return;
		}
		this.setUserlanguage(new Language(userlanguage, userlanguage.toLowerCase()));
	}

	public void setUsername(String username) {
		if (username == null) {
			return;
		}
		this.username = StringTool.escapeHtmlTag(username);
	}

	public void checkUsername() throws DataException {
		username = username.trim();
		String username2 = StringTool.escapeHtmlTag(username);
		if ("".equals(username)) {
			throw new DataException("(" + account + ") 錯誤！您可能未填使用者名字");
		} else if (!username.equals(username2)) {
			throw new DataException("(" + username + ") 錯誤！您的姓名可能使用了  html 標籤。");
		}
	}

	public void setWa(Integer wa) {
		if (wa == null) {
			throw new NullPointerException();
		}
		this.wa = wa;
	}

	public Integer getConfig() {
		return config;
	}

	public void setConfig(Integer config) {
		if (config == null) {
			return;
		}
		this.config = config;
	}

	public boolean getIsVClassStudent() {
		if (new VClassStudentDAO().getStudent(vclassid, id) == null) {
			return false;
		}
		return true;
	}

	public String getSchoolname() {
		return new SchoolService().getSchoolById(this.getSchoolid()).getSchoolname();
	}

	public School getSchool() {
		return new SchoolService().getSchoolById(this.getSchoolid());
	}

	//
	//
	//

	/**
	 * 取得總 submit 數
	 */

	public void setPasswd(String passwd, String passwd2) {
		if (passwd == null || passwd2 == null || "".equals(passwd) || "".equals(passwd2)) {
			throw new DataException("未輸入密碼！");
		} else if (!passwd.equals(passwd2)) {
			throw new DataException("密碼兩次輸入不相同！");
		}
		this.passwd = passwd;
	}

	/**
	 * 取得 config 的某個 index 的設定
	 * 
	 * @param index
	 * @return
	 */
	public boolean isCheckedConfig(CONFIG config) {
		int x = this.getConfig() & (1 << config.ordinal());
		return x > 0 ? true : false;
	}

	/**
	 * 判斷帳號是否有效。
	 * 
	 * @return
	 */
	public boolean getIsEnabled() {
		return this.isCheckedConfig(CONFIG.ENABLE);
	}

	public boolean getIsQualifiedAuthor() {
		return isCheckedConfig(CONFIG.QualifiedAuthor);
	}

	public boolean isNullUser() {
		return this.getId() == 0;
	}



	public boolean isContestManager() {
		return isCheckedConfig(CONFIG.ContestManager);
	}

	public boolean isInsertProblem() {
		return isCheckedConfig(CONFIG.InsertProblem);
	}

	public boolean isProblemManager() {
		return isCheckedConfig(CONFIG.ProblemManager);
	}

	public boolean isVClassManager() {
		return isCheckedConfig(CONFIG.VClassManager);
	}

//	public boolean isGeneralManager() {
//		return (isCheckedConfig(CONFIG.GeneralManager) || this.getIsHigherEqualThanMANAGER());
//	}

	public boolean getIsDEBUGGER() {
		return ROLE.DEBUGGER == this.getRole();
	}

	public boolean getIsHigherEqualThanMANAGER() {
		return this.getRole().isHigherEqualThan(ROLE.MANAGER);
	}

	public boolean getIsMANAGER() {
		return ROLE.MANAGER == this.getRole();
	}

	public boolean isIsGroupGuest() {
		return ROLE.GUEST == this.getRole();
	}

	public boolean isIsInContest() {
		return getJoinedcontestid().intValue() > User.NONE_CONTESTID;
	}

	public boolean isIsInVClass() {
		return getVclassid().intValue() > 0;
	}

	public boolean isOnline() {
		for (OnlineUser onlineUser : ApplicationScope.getOnlineUsers().values()) {
			if (this.getAccount().equals(onlineUser.getAccount())) {
				return true;
			}

		}
		return false;
	}


	public HashSet<Class<? extends HttpServlet>> getDenyServlet() {
		if (this.isIsInContest()) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.remove_UserInContest()) {
				this.denyServlet.add(servletClazz);
			}
		}
		return denyServlet;
	}

	@JsonIgnore
	public HashSet<Class<? extends HttpServlet>> getAllowServlet() {
		for (Class<? extends HttpServlet> servletClazz : ApplicationScope.getRoleMap().get(this.getRole())) {
			this.allowServlet.add(servletClazz);
		}
		if (this.getIsDEBUGGER()) {
			return this.allowServlet;
		}
		if (!this.isCheckedConfig(CONFIG.ENABLE)) {
			this.getAllowServlet().clear();
			return this.allowServlet;
		}

//		if (this.isCheckedConfig(CONFIG.GeneralManager)) {
//			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
//					.add_GeneralManager()) {
//				this.allowServlet.add(servletClazz);
//			}
//		}
		if (this.isCheckedConfig(CONFIG.ContestManager)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_ContestManager()) {
				this.allowServlet.add(servletClazz);
			}
		}
		if (this.isCheckedConfig(CONFIG.ProblemManager)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_ProblemManager()) {
				this.allowServlet.add(servletClazz);
			}
		}
		if (this.isCheckedConfig(CONFIG.VClassManager)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_VClassManager()) {
				this.allowServlet.add(servletClazz);
			}
		}
		if (this.isCheckedConfig(CONFIG.InsertProblem)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_InsertProblem()) {
				this.allowServlet.add(servletClazz);
			}
		}
		return allowServlet;
	}

	@JsonIgnore
	public HashSet<Class<? extends HttpServlet>> getPrivileges_NOUSE() {
		for (Class<? extends HttpServlet> servletClazz : ApplicationScope.getRoleMap().get(this.getRole())) {
			this.getAllowServlet().add(servletClazz);
		}
		if (this.getIsDEBUGGER()) {
			return this.getAllowServlet();
		}
		if (!this.isCheckedConfig(CONFIG.ENABLE)) {
			this.getAllowServlet().clear();
			return this.getAllowServlet();
		}

//		if (this.isCheckedConfig(CONFIG.GeneralManager)) {
//			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
//					.add_GeneralManager()) {
//				this.getAllowServlet().add(servletClazz);
//			}
//		}
		if (this.isCheckedConfig(CONFIG.ContestManager)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_ContestManager()) {
				this.getAllowServlet().add(servletClazz);
			}
		}
		if (this.isCheckedConfig(CONFIG.ProblemManager)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_ProblemManager()) {
				this.getAllowServlet().add(servletClazz);
			}
		}
		if (this.isCheckedConfig(CONFIG.VClassManager)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_VClassManager()) {
				this.getAllowServlet().add(servletClazz);
			}
		}
		if (this.isCheckedConfig(CONFIG.InsertProblem)) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.add_InsertProblem()) {
				this.getAllowServlet().add(servletClazz);
			}
		}
		if (this.isIsInContest()) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.remove_UserInContest()) {
				this.getAllowServlet().remove(servletClazz);
			}
		}
		if (this.isIsInContest()) {
			for (Class<? extends HttpServlet> servletClazz : RoleFilter.class.getAnnotation(Privilege.class)
					.remove_UserInContest()) {
				this.getDenyServlet().add(servletClazz);
			}
		}
		return this.getAllowServlet();
	}

	/**
	 * privilege 改用 HashSet<HttpServlet.class>
	 * 
	 * @return
	 */
	//
	//
	//
	//

	public int getCurrentRank() {
		return 0;
	}

	/**
	 * @deprecated 準備移動到 OnlineUser 內，這樣 returnPage 才有意義。
	 * @return
	 */
	public String getReturnPage() {
		if (this.sessionid == null || "".equals(sessionid)) {
			return "沒有 sessionid!";
		}
		return (String) SessionFactory.getSessionById(sessionid).getAttribute("returnPage");
	}

	public boolean islegalEmail(String email) {
		if (email != null && email.toLowerCase()
				.matches("^([_a-z0-9-]+)(\\.[_a-z0-9-]+)*@([a-z0-9-]+)(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) {
			return true;
		}
		return false;
	}

	/**
	 * * 根據 User 的群組權限、整站模式、參賽模式等計算使用者的權限。 servletPath 如 "/Index"
	 * 
	 * 20130111 改用 LinkedHashSet 來處理 privilege <br>
	 * 判斷 User 的 privilege <br>
	 * <br>
	 * 判定規則為：<br>
	 * 1. 越靠右邊優先權越高，蓋過左邊的設定 <br>
	 * 2. 允許 regex 寫法。<br>
	 * 3. null, "" 均回覆 NOTDEFINE
	 * 
	 * @param privilegeSet
	 * @param servletPath
	 * @return
	 * @throws AccessException
	 * @Deprecated 由 RoleFilter.getServletSet(User) 來處理權限問題。不應由 User 本身來處理。
	 * 
	 */
	public boolean isAccessible_Deprecated20131006(String servletPath) throws AccessException {
		LinkedHashSet<String> privileges = new LinkedHashSet<String>();
		Resource.MESSAGE result = Resource.MESSAGE.Privilege_NOTDEFINE;
		if (privileges == null || privileges.isEmpty()) {
			result = Resource.MESSAGE.Privilege_NOTDEFINE;
		}
		for (String privilege : privileges) {
			if (privilege.contains("#")) {
				privilege = privilege.substring(0, privilege.indexOf("#"));
			}

			if (privilege.startsWith("!")) {
				if (privilege.equals("!" + servletPath) || new String("!" + servletPath).matches(privilege)) {
					result = Resource.MESSAGE.Privilege_FORBIDDEN;
				}
			} else {
				if (privilege.equals(servletPath) || servletPath.matches(privilege)) {
					result = Resource.MESSAGE.Privilege_ALLOWED;
				}
			}
		}

		if (result == Resource.MESSAGE.Privilege_ALLOWED) {
			return true;
		} else if (result == Resource.MESSAGE.Privilege_NOTDEFINE) {
			throw new DataException("您沒有存取本頁的權限(" + servletPath + ")");
		} else if (result == Resource.MESSAGE.Privilege_FORBIDDEN) {
			throw new DataException("您已被禁止存取本頁(" + servletPath + ")");
		} else {
			throw new DataException("因某些原因，您無法存取本頁(" + servletPath + ")");
		}
	}

	//

	/**
	 * islegalXXXXXX 檢查是否符合特定規則
	 * 
	 * @param account
	 * @return
	 */

	public static boolean islegelBirthyear(int birthyear) {
		if ((Calendar.getInstance().get(Calendar.YEAR) - birthyear >= 0 && birthyear > 1900)) {
			return true;
		}
		return false;
	}

	public static CONFIG[] getCONFIGs() {
		return User.CONFIG.values();
	}

	public boolean getIsAuthhost_Google() {
		return this.getAuthhost() == AUTHHOST.Google && ApplicationScope.getAppConfig().getIsGoogleLoginSetup();
	}

	public boolean getIsAuthhost_localhost() {
		return this.getAuthhost() == AUTHHOST.localhost;
	}
}
