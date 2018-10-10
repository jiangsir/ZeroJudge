package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.commons.lang.StringEscapeUtils;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Factories.SchoolFactory;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.School;

public class SchoolDAO extends SuperDAO<School> {

	public SchoolDAO() {
	}




	@Override
	protected int insert(School school) throws DataException {
		String sql = "INSERT INTO schools(url, schoolname, imgsrc, descript, checkid) VALUES(?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, school.getUrl().toString());
			pstmt.setString(2, school.getSchoolname());
			pstmt.setString(3, school.getImgsrc());
			pstmt.setString(4, school.getDescript());
			pstmt.setInt(5, school.getCheckid());
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
	protected int update(School school) throws DataException {
		String sql = "UPDATE schools SET url=?, schoolname=?, imgsrc=?, descript=?, checkid=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setString(1, school.getUrl().toString());
			pstmt.setString(2, school.getSchoolname());
			pstmt.setString(3, school.getImgsrc());
			pstmt.setString(4, school.getDescript());
			pstmt.setInt(5, school.getCheckid());
			pstmt.setInt(6, school.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected boolean delete(int schoolid) {
		String schoolsql = "DELETE FROM schools WHERE id=" + schoolid;
		return this.execute(schoolsql);
	}


	public ArrayList<School> getSchoolsByRules(TreeSet<String> rules, String orderby, int page) {
		String sql = "SELECT * FROM schools " + this.makeRules(rules, orderby, page);
		return this.executeQuery(sql, School.class);
	}

	public School getSchoolByName(String schoolname) {
		String SQL = "SELECT * FROM schools WHERE schoolname=?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(SQL);
			pstmt.setString(1, schoolname);
			for (School school : executeQuery(pstmt, School.class)) {
				return school;
			}
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			throw new DataException(e);
		}
		return SchoolFactory.getNullSchool();
	}

	/**
	 * 依據 school 取得排行榜
	 * 
	 * @param pagenum
	 * @param school
	 * @return
	 */
	public ArrayList<?> getRankingbySchool(int pagenum) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT users.schoolid, schools.id, schools.schoolname, schools.url, "
				+ "schools.descript, schools.checkid, " + "COUNT(*) AS count,SUM(ac) AS schoolac, "
				+ "AVG(ac) as schoolavg FROM users,schools WHERE users.schoolid=schools.id AND users.schoolid>0"
				+ " AND schools.checkid=" + School.CHECKID_CHECKED);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		for (Integer schoolid : appConfig.getExclusiveSchoolids()) {
			sql.append(" AND users.schoolid!=" + schoolid + " ");
		}
		sql.append(" GROUP BY users.schoolid");
		sql.append(" ORDER BY schoolac DESC LIMIT " + (pagenum - 1) * appConfig.getPageSize() + ","
				+ appConfig.getPageSize());
		return this.executeQueryByMap(sql.toString());
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public ArrayList<?> getRankingbyKeywords(String keywords) {
		if (keywords == null || "".equals(keywords.trim())) {
			return new ArrayList<>();
		}
		keywords = StringEscapeUtils.escapeSql(keywords.trim());
		String schoolname_LIKE = "";
		for (String keyword : keywords.split(" ")) {
			if (!schoolname_LIKE.equals("")) {
				schoolname_LIKE += " OR ";
			}
			schoolname_LIKE += "schoolname LIKE '%" + keyword + "%'";
		}

		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT users.schoolid, schools.id, schools.schoolname, schools.url, schools.descript,"
				+ " schools.checkid, COUNT(*) AS count,SUM(ac) AS schoolac, AVG(ac) as schoolavg "
				+ "FROM users,schools WHERE users.schoolid=schools.id AND users.schoolid>0" + " AND schools.checkid="
				+ School.CHECKID_CHECKED + " AND (" + schoolname_LIKE + ")");
		AppConfig appConfig = ApplicationScope.getAppConfig();
		for (Integer schoolid : appConfig.getExclusiveSchoolids()) {
			sql.append(" AND users.schoolid!=" + schoolid + " ");
		}
		sql.append(" GROUP BY users.schoolid");
		sql.append(" ORDER BY schoolac DESC");
		return this.executeQueryByMap(sql.toString());
	}

	public ArrayList<?> getTopSchools(int number) {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		StringBuffer sql = new StringBuffer(5000);
		sql.append("SELECT users.schoolid,schools.schoolname, COUNT(*) AS count,SUM(ac) AS schoolac, "
				+ "AVG(ac) as schoolavg " + "FROM users,schools WHERE users.schoolid=schools.id AND users.schoolid>0"
				+ " AND schools.checkid= " + School.CHECKID_CHECKED + " ");
		for (Integer schoolid : appConfig.getExclusiveSchoolids()) {
			sql.append("AND users.schoolid!=" + schoolid + " ");
		}
		sql.append("GROUP BY users.schoolid ORDER BY schoolac DESC LIMIT 0," + number);
		return this.executeQueryByMap(sql.toString());
	}

}
