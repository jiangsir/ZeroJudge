package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Problemlevel;

/**
 * @author jiangsir
 * 
 */
public class ProblemlevelDAO extends SuperDAO<Problemlevel> {

	public ProblemlevelDAO() {
	}

	/**
	 * 用 id 搜尋 problemlevel
	 * 
	 * @param id
	 * @return
	 */
	public Problemlevel geProblemlevelById(Integer id) {
		String sql = "SELECT * FROM problemlevels WHERE id=" + id;
		for (Problemlevel problemlevel : this.executeQuery(sql, Problemlevel.class)) {
			return problemlevel;
		}
		return new Problemlevel();
	}

	/**
	 * 用 特定 level 取得題目列表。
	 * 
	 * @param level
	 * @return
	 */
	public ArrayList<Problem> getProblemsByLevel(int level) {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		String sql = "SELECT * FROM problemlevels WHERE id IN (SELECT MAX(id) as id FROM problemlevels GROUP BY pid) AND level="
				+ level + ";";
		ProblemService problemservice = new ProblemService();
		for (Problemlevel problemlevel : this.executeQuery(sql, Problemlevel.class)) {
			problems.add(problemservice.getProblemByPid(problemlevel.getPid()));
		}
		return problems;
	}

	/**
	 * 
	 * @param pid
	 * @return
	 */
	public ArrayList<Problemlevel> getProblemlevelsByPid(int pid) {
		String sql = "SELECT * FROM problemlevels WHERE pid=? ORDER BY id DESC;";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, pid);
			return executeQuery(pstmt, Problemlevel.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 用 pid 取得最後一次設定的 problem level
	 * 
	 * @param pid
	 * @return
	 */
	public Problemlevel getProblemlevelByPid(int pid) {
		String sql = "SELECT * FROM problemlevels WHERE pid=? ORDER BY id DESC;";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, pid);
			for (Problemlevel problemlevel : executeQuery(pstmt, Problemlevel.class)) {
				return problemlevel;
			}
			return new Problemlevel();
		} catch (Exception e) {
			e.printStackTrace();
//			throw new DataException(e);
			return new Problemlevel();
		}
	}

	/**
	 * 用 userid 取得設定 level 的紀錄。
	 * 
	 * @param userid
	 * @return
	 */
	public ArrayList<Problemlevel> getProblemlevelsByUserid(int userid) {
		String sql = "SELECT * FROM problemlevels WHERE userid=? ORDER BY id DESC;";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, userid);
			return executeQuery(pstmt, Problemlevel.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 取得所有題目的 level count
	 * 
	 * @param topn
	 * @return
	 */
	public LinkedHashMap<Integer, Integer> getAllProblemlevelCounts() {


		LinkedHashMap<Integer, Integer> levelMap = new LinkedHashMap<Integer, Integer>();
		levelMap.put(0, 0);
		levelMap.put(1, 0);
		levelMap.put(2, 0);
		levelMap.put(3, 0);
		levelMap.put(4, 0);
		levelMap.put(5, 0);
		levelMap.put(6, 0);
		ArrayList<HashMap<String, Object>> levelCounts = this.executeQueryByMap(
				"select l.pid, GROUP_CONCAT(l.id), GROUP_CONCAT(l.level) as levels, p.problemid, p.title, display from problemlevels as l LEFT OUTER JOIN problems as p on l.pid=p.id WHERE display='open' GROUP BY l.pid;");
		for (HashMap<String, Object> levelCount : levelCounts) {
			String[] levels = ((String) levelCount.get("levels")).split(",");
			int key = Integer.parseInt(levels[levels.length - 1]);
			levelMap.put(key, levelMap.get(key) + 1);
		}
		return levelMap;
	}

	/**
	 * 新增一筆 problem level
	 */
	public synchronized int insert(Problemlevel problemlevel) {
		String sql = "INSERT INTO problemlevels(pid, levelname, level, userid, updatetime) VALUES (?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, problemlevel.getPid());
			pstmt.setString(2, problemlevel.getLevelname());
			pstmt.setInt(3, problemlevel.getLevel());
			pstmt.setInt(4, problemlevel.getUserid());
			pstmt.setTimestamp(5, problemlevel.getUpdatetime());
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
	public int update(Problemlevel problemlevel) throws DataException {
		return -1;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

}
