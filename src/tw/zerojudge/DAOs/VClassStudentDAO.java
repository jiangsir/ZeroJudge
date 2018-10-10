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
import java.util.ArrayList;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Tables.*;

/**
 * @author jiangsir
 * 
 */
public class VClassStudentDAO extends SuperDAO<VClassStudent> {
	public VClassStudentDAO() {
	}

	@Override
	public int insert(VClassStudent vstudent) throws DataException {
		String sql = "INSERT INTO vclassstudents (vclassid, userid, account, " + "vclassaclist, ac) VALUES(?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, vstudent.getVclassid());
			pstmt.setInt(2, vstudent.getUserid());
			pstmt.setString(3, vstudent.getAccount());
			pstmt.setString(4, vstudent.getVclassaclist().toString());
			pstmt.setInt(5, vstudent.getAc());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("Duplicate entry")) {
				throw new DataException("id 重複！" + e.getLocalizedMessage());
			}
			throw new DataException(e.getLocalizedMessage());
		}
		return id;
	}

	@Override
	public int update(VClassStudent student) throws DataException {
		String SQL = "UPDATE vclassstudents SET vclassaclist=?, ac=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setString(1, student.getVclassaclist().toString());
			pstmt.setInt(2, student.getVclassaclist().size());
			pstmt.setInt(3, student.getId());
			result = super.executeUpdate(pstmt);
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

	public boolean delete(int vclassid, int userid) {
		String sql = "DELETE FROM vclassstudents WHERE vclassid=" + vclassid + " AND userid=" + userid;
		return this.execute(sql);
	}

	public VClassStudent getStudent(int vclassid, int userid) {
		String sql = "SELECT * FROM vclassstudents WHERE vclassid=" + vclassid + " AND userid=" + userid;
		for (VClassStudent student : this.executeQuery(sql, VClassStudent.class)) {
			return student;
		}
		return null;
	}

	public ArrayList<VClassStudent> getStudentsByUserid(int userid) {
		String sql = "SELECT * FROM vclassstudents WHERE userid=" + userid + " ORDER BY vclassid DESC";
		return executeQuery(sql, VClassStudent.class);
	}

	public ArrayList<VClassStudent> getStudentsByVclassid(int vclassid) {
		String sql = "SELECT * FROM vclassstudents WHERE vclassid=" + vclassid + " ORDER BY ac DESC, id";
		return executeQuery(sql, VClassStudent.class);
	}

}
