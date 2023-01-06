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
public class VClassTemplateDAO extends SuperDAO<VClassTemplate> {
	public VClassTemplateDAO() {
	}

	@Override
	public int insert(VClassTemplate vclassTemplate) throws DataException {
		String sql = "INSERT INTO vclasstemplates (name, title, ownerid, problemids, "
				+ "descript, visible) VALUES(?,?,?,?,?, ?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, vclassTemplate.getName());
			pstmt.setString(2, vclassTemplate.getTitle());
			pstmt.setInt(3, vclassTemplate.getOwnerid());
			pstmt.setString(4, vclassTemplate.getProblemids().toString());
			pstmt.setString(5, vclassTemplate.getDescript());
			pstmt.setInt(6, vclassTemplate.getVisible());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("Duplicate entry")) {
				throw new DataException("重複加入！" + e.getLocalizedMessage());
			}
			throw new DataException(e.getLocalizedMessage());
		}
		return id;
	}

	@Override
	public int update(VClassTemplate vclassTemplate) throws DataException {
		String SQL = "UPDATE vclasstemplates SET ownerid=?, problemids=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setInt(1, vclassTemplate.getOwnerid());
			pstmt.setString(2, vclassTemplate.getProblemids().toString());
			pstmt.setInt(3, vclassTemplate.getId());
			result = super.executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 用 template id 刪除所有相同 name 的 template
	 */
	@Override
	public boolean delete(int id) {
		VClassTemplate template = this.getTemplateById(id);

		String SQL = "DELETE FROM vclasstemplates WHERE name=?";
		boolean result = false;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setString(1, template.getName());
			result = super.execute(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return result;
	}

	public VClassTemplate getVClassTemplate(int id) {
		String sql = "SELECT * FROM vclasstemplates WHERE id=" + id;
		for (VClassTemplate template : this.executeQuery(sql, VClassTemplate.class)) {
			return template;
		}
		return null;
	}

	/**
	 * 用 name 取得課程模板，用來取得所有 vcontest 的描述、題目
	 * 
	 * @param name
	 * @return
	 */
	public ArrayList<VClassTemplate> getTemplatesByName(String name) {
		String sql = "SELECT * FROM vclasstemplates WHERE name=? ORDER BY id";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, name);
			return executeQuery(pstmt, VClassTemplate.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e.getLocalizedMessage());
		}
	}

	/**
	 * 用 template id 取得一個 template
	 * 
	 * @param id
	 * @return
	 */
	public VClassTemplate getTemplateById(int id) {
		String sql = "SELECT * FROM vclasstemplates WHERE id=?";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, id);
			for (VClassTemplate template : executeQuery(pstmt, VClassTemplate.class)) {
				return template;
			}
			throw new DataException("id=" + id + " Not found.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 用 template id 取得一個 template 並取得相同 name 的所有 templates
	 * 
	 * @param id
	 * @return
	 */
	public ArrayList<VClassTemplate> getTemplatesById(int id) {
		return this.getTemplateById(id).getTemplatesByName();
	}

	/**
	 * 用 name 取得來自某 ownerid 所建立的 課程模板。
	 * 
	 * @return
	 */
	public ArrayList<VClassTemplate> getTemplatesByOwnerid(int ownerid) {
		String sql = "SELECT min(id) as id, name " + "FROM vclasstemplates WHERE ownerid=" + ownerid
				+ " GROUP BY name ORDER BY id DESC";
		return executeQuery(sql, VClassTemplate.class);
	}

	/**
	 * 取得公開的課程模板
	 * 
	 * @param visible
	 * @return
	 */
	public ArrayList<VClassTemplate> getTemplatesByVisible(int visible) {
		String sql = "SELECT min(id) as id, name FROM vclasstemplates WHERE visible=1"
				+ " GROUP BY name ORDER BY id DESC";
		return executeQuery(sql, VClassTemplate.class);
	}

}
