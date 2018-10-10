/**
 * idv.jiangsir.DAOs - ClassesDAO.java
 * 2009/2/18 下午 08:13:10
 * jiangsir
 */
package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.*;

/**
 * @author jiangsir
 * 
 */
public class VClassDAO extends SuperDAO<VClass> {
	public VClassDAO() {
	}

	public int insert(VClass vclass) {
		String sql = "INSERT INTO vclasses (vclassname, vclasscode, ownerid, firstclasstime, problemids, descript, "
				+ "visible) VALUES(?,?,?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, vclass.getVclassname());
			pstmt.setString(2, this.getRandomVclassCode());
			pstmt.setInt(3, vclass.getOwnerid());
			pstmt.setTimestamp(4, new Timestamp(vclass.getFirstclasstime().getTime()));
			pstmt.setString(5, vclass.getProblemids().toString());
			pstmt.setString(6, vclass.getDescript());
			pstmt.setInt(7, vclass.getVisible());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public int update(VClass vclass) {
		String SQL = "UPDATE vclasses SET vclassname=?, ownerid=?, firstclasstime=?,"
				+ " problemids=?, descript=?, visible=?, vclasscode=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setString(1, vclass.getVclassname());
			pstmt.setInt(2, vclass.getOwnerid());
			pstmt.setTimestamp(3, vclass.getFirstclasstime());
			pstmt.setString(4, vclass.getProblemids().toString());
			pstmt.setString(5, vclass.getDescript());
			pstmt.setInt(6, vclass.getVisible());
			pstmt.setString(7, vclass.getVclasscode());
			pstmt.setInt(8, vclass.getId());
			result = super.executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 新增一個 Vclass 的 contest "隨堂測驗"
	 * 
	 * @return
	 * @throws AccessException
	 */
	public int insertVContest(VClass vclass) throws DataException {
		if (vclass.getId().intValue() <= 0) {
			return -1;
		}
		Contest lastvcontest = new ContestDAO().getLastVContest(vclass.getId());
		Contest newvcontest = new Contest();
		newvcontest.setOwnerid(vclass.getOwnerid());
		newvcontest.setTitle(vclass.getVclassname());
		newvcontest.setVisible(Contest.VISIBLE.hide);
		newvcontest.setTimelimit(60000 * 60 * 2L);
		newvcontest.setVclassid(vclass.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastvcontest.getStarttime());
		calendar.add(Calendar.DATE, 7);
		newvcontest.setStarttime(new Timestamp(calendar.getTime().getTime()));
		try {
			newvcontest.setFreezelimit("" + Contest.FreezeLimit.zero.getValue());
		} catch (DataException e) {
		}
		return new ContestService().insert(newvcontest);
	}

	public ArrayList<VClass> getVClasses(Set<String> rules, String orderby) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT * FROM vclasses WHERE 1=1");
		Iterator<String> ruleit = rules.iterator();
		while (ruleit.hasNext()) {
			sql.append(" AND (" + ruleit.next() + ")");
		}
		if (orderby == null || "".equals(orderby)) {
			sql.append(" ORDER BY " + orderby);
		}
		return executeQuery(sql.toString(), VClass.class);
	}

	public ArrayList<VClass> getVClasses() {
		String sql = "SELECT * FROM vclasses ORDER BY id DESC";
		return executeQuery(sql, VClass.class);
	}


	public ArrayList<VClass> getVClassesByOwnerid(int ownerid) {
		String sql = "SELECT * FROM vclasses WHERE ownerid=" + ownerid + " AND visible=" + VClass.VISIBLE_OPEN
				+ " ORDER BY id DESC";
		return executeQuery(sql, VClass.class);
	}


	private String getRandomVclassCode() {
		StringBuilder sb = new StringBuilder();
		String ss = "_+0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < 6; i++) {
			int x = (int) (Math.random() * ss.length());
			sb.append(ss.charAt(x));
		}
		return sb.toString();
	}
	/**
	 * 
	 * @param vclassid
	 */
	public String renewVClassCode(Integer vclassid) {
		String vclasscode = this.getRandomVclassCode();
		VClass vclass = this.getVClassById(vclassid);
		vclass.setVclasscode(vclasscode);
		this.update(vclass);
		return vclass.getVclasscode();
	}

	/**
	 * 20090416<br> 將一個學生移出某課程，但若該生屬於不止一個課程，就直接將他的 vclassid 改為另一個課程
	 * 
	 * @throws
	 * 
	 */
	public void removeVClassStudent(Integer vclassid, int userid) {
		for (Contest vcontest : this.getVContests(vclassid)) {
			new ContestService().doFinishByContestUserid(vcontest, userid);
			//
		}

		new VClassStudentDAO().delete(vclassid, userid);

		User user = new UserService().getUserById(userid);
		user.setVclassid(0);
		for (VClass vclass : this.getVClassesByUserid(userid)) {
			user.setVclassid(vclass.getId());
		}
		new UserService().update(user);
	}

	private boolean checkStudentLine(String line) {
		if (line == null || "".equals(line.trim())) {
			return false;
		}
		line = line.trim();
		String[] student = line.split(",");
		if (new UserService().isexitAccount(line.split(",")[0])) {
			return true;
		}
		if (student.length != 6) {
			return false;
		} else if (!new UserService().isAvailableAccount(student[0].trim())) {
			return false;
		} else if (!User.islegelBirthyear(Integer.parseInt(student[4].trim()))) {
			return false;
		} else if (!new User().islegalEmail(student[5].trim())) {
			return false;
		}
		return true;
	}

	public boolean checkScripts(String scripts) throws AccessException {
		if (scripts == null || "".equals(scripts)) {
			throw new AccessException(UserFactory.getNullOnlineUser(), "沒有資料");
		}
		String[] lines = scripts.split("\n");
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].trim();
			if (lines[i] != null && !"".equals(lines[i]) && !lines[i].startsWith("#") && !checkStudentLine(lines[i])) {
				Message message = new Message();
				message.setType(Message.MessageType_ALERT);
				message.setPlainTitle("格式有誤！");
				message.setPlainMessage("您的批次文件<br>第 " + (i + 1) + "行有誤<br>" + lines[i] + "<br>可能的原因包括\"帳號不合法、帳號已存在、"
						+ "出生年格式不正確" + "(不可少於 0 歲，也不可大於100歲)、Email格式不正確\"<br>");
				throw new AccessException(UserFactory.getNullOnlineUser(),
						"您的批次文件<br>第 " + (i + 1) + "行有誤<br>" + lines[i] + "<br>可能的原因包括\"帳號不合法、帳號已存在、" + "出生年格式不正確"
								+ "(不可少於 0 歲，也不可大於100歲)、Email格式不正確\"<br>");
			}
		}
		return true;
	}

	public boolean BatchInsertVClassStudents(VClass vclass, String scripts) {
		String[] lines = scripts.split("\n");
		VClassStudentDAO vclassStudentDao = new VClassStudentDAO();
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].trim();
			if (!"".equals(lines[i]) && !lines[i].startsWith("#")) {
				String[] datas = lines[i].split(",");
				User user = new UserService().getUserByAccount(datas[0].trim());
				user.setVclassid(vclass.getId());
				new UserService().update(user);

				VClassStudent vclassstudent = new VClassStudent();
				vclassstudent.setVclassid(vclass.getId());
				vclassstudent.setUserid(user.getId());
				vclassstudent.setAccount(datas[0].trim());
				vclassStudentDao.insert(vclassstudent);
			}
		}
		return true;
	}

	/**
	 * 重新計算 vclass 內的學生解題統計以及該課程題目的列表
	 * 
	 * @param vclassid
	 */
	public void rebuiltVClass(VClass vclass) {
		GeneralDAO db = new GeneralDAO();

		TreeSet<Problemid> problemids = new TreeSet<Problemid>();
		try {
			db.executeUpdate("UPDATE solutions SET vclassid=0 WHERE vclassid=" + vclass.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		ArrayList<Contest> vcontests = new ContestDAO().getVContests(vclass.getId());
		for (Contest vcontest : vcontests) {
			try {
				new GeneralDAO().executeUpdate(
						"UPDATE solutions SET vclassid=" + vclass.getId() + " WHERE contestid=" + vcontest.getId());
			} catch (SQLException e) {
				e.printStackTrace();
				continue;
			}
			if (vcontest.getIsStopped()) {
				for (Problemid problemid : vcontest.getProblemids()) {
					problemids.add(problemid);
				}
			}
		}
		vclass.setProblemids(problemids);
		new VClassDAO().update(vclass);

		/* FIXME rebuilt 未完成; vclassstudent 也要重整 */
		for (VClassStudent student : new VClassStudentDAO().getStudentsByVclassid(vclass.getId())) {

			TreeSet<Problemid> vclassaclist = new TreeSet<Problemid>();
			for (Integer solutionid : new SolutionDAO().getSolutionsByVclassidUserid(vclass.getId(),
					student.getUserid())) {
				vclassaclist.add(new SolutionDAO().getSolutionById(solutionid).getProblemid());
			}
			student.setVclassaclist(vclassaclist);
			try {
				new VClassStudentDAO().update(student);
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Contest> getVContests(Integer vclassid) {
		return new ContestDAO().getVContests(vclassid);
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

	public VClass getVClassById(int vclassid) {
		String sql = "SELECT * FROM vclasses WHERE id=" + vclassid;
		for (VClass vclass : executeQuery(sql, VClass.class)) {
			return vclass;
		}
		return new VClass();
	}

	public VClass getVClassByVclasscode(String vclasscode) {
		if (vclasscode == null || "".equals(vclasscode.trim())) {
			return new VClass();
		}
		String sql = "SELECT * FROM vclasses WHERE vclasscode=?;";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, vclasscode.trim());
			for (VClass vclass : this.executeQuery(pstmt, VClass.class)) {
				return vclass;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new VClass();
	}

	/**
	 * 將某 user 加入 本 vclass 當中成為 vclassstudent
	 * 
	 * @param user
	 */
	public void insertStudent(VClass vclass, User user) {
		if (vclass.getIsNull()) {
			return;
		}
		user.setVclassid(vclass.getId());
		new UserService().update(user);

		VClassStudent vclassstudent = new VClassStudent();
		vclassstudent.setVclassid(vclass.getId());
		vclassstudent.setUserid(user.getId());
		vclassstudent.setAccount(user.getAccount());
		new VClassStudentDAO().insert(vclassstudent);

	}

	/**
	 * 取得某個 userid 所加入的 vclasses
	 * 
	 * @param userid
	 * @return
	 */
	public ArrayList<VClass> getVClassesByUserid(int userid) {
		ArrayList<VClass> vclasses = new ArrayList<VClass>();
		VClassStudentDAO vclassStudentDao = new VClassStudentDAO();
		for (VClassStudent student : vclassStudentDao.getStudentsByUserid(userid)) {
			vclasses.add(this.getVClassById(student.getVclassid()));
		}
		return vclasses;
	}

}
