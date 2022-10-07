package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Log.TABID;

public class LogDAO extends SuperDAO<Log> {
	Logger logger = Logger.getLogger(this.getClass().getName());

	public LogDAO() {
	}

	public int getCount() {
		return this.executeCount("logs", new TreeMap<String, Object>());
	}

	public ArrayList<Log> getLogs(TreeSet<String> rules, int page) {
		return this.getLogs(rules, "id DESC", page);
	}

	public ArrayList<Log> getLogByTabid(TreeSet<Log.TABID> tabids, long timeslice) throws DataException {
		String tabidstring = "";
		Log.TABID[] tabidarray = tabids.toArray(new TABID[tabids.size()]);
		for (Log.TABID tabid : tabidarray) {
			if ("".equals(tabidstring)) {
				tabidstring = "tabid=?";
			} else {
				tabidstring += " OR tabid=?";
			}
		}
		String sql = "SELECT * FROM logs WHERE (" + tabidstring + ") AND timestamp>=?";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			for (int i = 0; i < tabidarray.length; i++) {
				pstmt.setString(i + 1, tabidarray[i].name());
			}
			pstmt.setTimestamp(tabidarray.length + 1, new Timestamp(System.currentTimeMillis() - timeslice));
			return executeQuery(pstmt, Log.class);
		} catch (SQLException e) {
			logger.info("SQLException=" + sql);
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	public ArrayList<Log> getLogByTabid(Log.TABID tabid, long timeslice) throws DataException {
		String sql = "SELECT * FROM logs WHERE tabid=? AND timestamp>=?";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, tabid.name());
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis() - timeslice));
			return executeQuery(pstmt, Log.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 
	 * @param rule
	 * @param orderby
	 *            傳入 null 代表不指定
	 * @param page
	 * @return
	 */
	public ArrayList<Log> getLogs(TreeSet<String> rules, String orderby, int page) {
		int pagesize = ApplicationScope.getAppConfig().getPageSize() * 5;
		StringBuffer sql = new StringBuffer(5000);

		sql.append("SELECT * FROM logs " + this.makeRules(rules, orderby, page));

		return executeQuery(sql.toString(), Log.class);
	}

	public ArrayList<Log> getLogsByIP(String ip) {
		String SQL = "SELECT * FROM logs WHERE ipaddr='" + ip + "' ORDER BY id DESC";
		return executeQuery(SQL, Log.class);
	}

	public int insert(Log log) {
		if (log.getStacktrace().contains("INSERT INTO logs")) {
			return 0;
		}

		String SQL = "INSERT INTO logs (method, uri, session_account, ipaddr, tabid, "
				+ "title, message, stacktrace, timestamp) VALUES (?,?,?,?,?,?,?,?,?)";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setString(1, log.getMethod());
			pstmt.setString(2, log.getUri());
			pstmt.setString(3, log.getSession_account());
			pstmt.setString(4, log.getIpaddr().toString());
			pstmt.setString(5, log.getTabid().name());
			pstmt.setString(6, log.getTitle());
			pstmt.setString(7, log.getMessage());
			pstmt.setString(8, log.getStacktrace());
			pstmt.setTimestamp(9, new Timestamp(new Date().getTime()));
			id = this.executeInsert(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public int update(Log t) throws DataException {
		return 0;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

	/**
	 * 刪減 logs 資料項
	 * 
	 * @param left
	 *            剩下幾筆
	 */
	public boolean reduce(int left) {
		String sql = "DELETE FROM `logs` WHERE id < (SELECT id FROM (SELECT MAX(id) as id FROM `logs`) as tt)-" + left;
		return this.execute(sql);
	}
}
