package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Testcode;

public class TestcodeDAO extends SuperDAO<Testcode> {

	public TestcodeDAO() {
	}

	public int insert(Testcode testcode) {
		String sql = "INSERT INTO testcodes (code, language, indata, outdata, "
				+ "expected_status, actual_status, actual_detail, "
				+ "descript) VALUES(?,?,?,?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, testcode.getCode());
			pstmt.setString(2, testcode.getLanguage());
			pstmt.setString(3, testcode.getIndata());
			pstmt.setString(4, testcode.getOutdata());
			pstmt.setString(5, testcode.getExpected_status());
			pstmt.setString(6, testcode.getActual_status());
			pstmt.setString(7, testcode.getActual_detail());
			pstmt.setString(8, testcode.getDescript());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return id;
	}

	public int update(Testcode testcode) {
		String sql = "UPDATE testcodes SET code=?, language=?, indata=?, outdata=?, "
				+ "expected_status=?, actual_status=?, actual_detail=?, "
				+ "descript=? WHERE id=" + testcode.getId();
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setString(1, testcode.getCode());
			pstmt.setString(2, testcode.getLanguage());
			pstmt.setString(3, testcode.getIndata());
			pstmt.setString(4, testcode.getOutdata());
			pstmt.setString(5, testcode.getExpected_status());
			pstmt.setString(6, testcode.getActual_status());
			pstmt.setString(7, testcode.getActual_detail());
			pstmt.setString(8, testcode.getDescript());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 取全部
	 * 
	 * @return
	 */
	public ArrayList<Testcode> getTestcodes() {
		String sql = "SELECT * FROM testcodes ORDER BY id DESC";
		return this.executeQuery(sql, Testcode.class);
	}

	public Testcode getTestcodeById(int id) {
		String sql = "SELECT * FROM testcodes WHERE id=" + id;
		for (Testcode testcode : executeQuery(sql, Testcode.class)) {
			return testcode;
		}
		return new Testcode();
	}

	/**
	 * 真正刪除一個 testcode
	 * 
	 * @param problemid
	 */
	@Override
	public boolean delete(int id) {
		String SQL = "DELETE FROM testcodes WHERE id=" + id;
		return execute(SQL);
	}

}
