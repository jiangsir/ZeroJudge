package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Loginlog;

public class LoginlogDAO extends SuperDAO<Loginlog> {

	public LoginlogDAO() {
	}

	/**
	 * 
	 * @return
	 */
	public int getCount() {
		return this.executeCount("loginlog", new TreeMap<String, Object>());
	}

	/**
	 * 取出數個最近的 login 紀錄
	 * 
	 * @param quantity
	 * @return
	 */
	public ArrayList<Loginlog> getLoginlogs(int quantity) {
		String SQL = "SELECT * FROM loginlog ORDER BY logintime "
				+ "DESC LIMIT 0," + quantity;
		return executeQuery(SQL, Loginlog.class);
	}

	public Loginlog getLoginlogById(int id) {
		String sql = "SELECT * FROM loginlog WHERE id=" + id;
		for (Loginlog loginlog : executeQuery(sql, Loginlog.class)) {
			return loginlog;
		}
		return new Loginlog();
	}

	public ArrayList<Loginlog> getLoginlogsByIP(String ip) {
		String SQL = "SELECT * FROM loginlog WHERE ipfrom='" + ip
				+ "' ORDER BY logintime DESC";
		return executeQuery(SQL, Loginlog.class);
	}

	@Override
	public synchronized int insert(Loginlog loginlog) throws DataException {
		String sql = "INSERT INTO loginlog (useraccount, ipfrom, "
				+ "message, logintime) VALUES(?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, loginlog.getUseraccount());
			pstmt.setString(2, loginlog.getIpfrom().toString());
			pstmt.setString(3, loginlog.getMessage());
			pstmt.setTimestamp(4, loginlog.getLogintime());
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

	@Override
	public synchronized int update(Loginlog loginlog) throws DataException {
		String sql = "UPDATE loginlog SET logouttime=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setTimestamp(1, loginlog.getLogouttime());
			pstmt.setLong(2, loginlog.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

}
