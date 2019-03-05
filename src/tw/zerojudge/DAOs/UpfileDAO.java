package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Tables.Upfile;

public class UpfileDAO extends SuperDAO<Upfile> {

	public UpfileDAO() {
	}

	public synchronized int insert(Upfile upfile) {
		String sql = "INSERT INTO upfiles(filename, filetype, `bytes`, "
				+ "ipfrom, `timestamp`, solutionid) VALUES(?,?,?,?,?,?)";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, upfile.getFilename());
			pstmt.setString(2, upfile.getFiletype());
			pstmt.setBytes(3, upfile.getBytes());
			pstmt.setString(4, upfile.getIpfrom());
			pstmt.setTimestamp(5, upfile.getTimestamp());
			pstmt.setInt(6, upfile.getSolutionid());
			return this.executeInsert(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	public synchronized int update(Upfile upload) {
		return -1;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

	public Upfile getUpfileById(int id) {
		String sql = "SELECT * FROM upfiles WHERE id=" + id;
		try {
			PreparedStatement pstmt = this.getConnection()
					.prepareStatement(sql);
			pstmt.setInt(1, id);
			for (Upfile upfile : this.executeQuery(pstmt, Upfile.class)) {
				return upfile;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 用 solutionid 來取得一個 upfile
	 * 
	 * @param solutionid
	 * @return
	 */
	public Upfile getUpfileBySolutionid(int solutionid) {
		String sql = "SELECT * FROM upfiles WHERE solutionid=?";
		try {
			PreparedStatement pstmt = this.getConnection()
					.prepareStatement(sql);
			pstmt.setInt(1, solutionid);
			for (Upfile upfile : this.executeQuery(pstmt, Upfile.class)) {
				return upfile;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

}
