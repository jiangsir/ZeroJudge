package tw.zerojudge.DAOs;

import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.sql.DataSource;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Factories.AttributeFactory;
import tw.zerojudge.JsonObjects.Schema;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Utils.ENV;
import tw.zerojudge.Utils.Utils;

public class GeneralDAO {

	public static Connection conn = null;
	private ServletContext sc = null;
	private DataSource ds = null;

	/**
	 * 
	 * @param config
	 */
	public GeneralDAO(Connection conn) {
		GeneralDAO.conn = conn;
	}

	/**
	 * 只有 ContextListener 能夠呼叫
	 * 
	 * @param event
	 */
	public GeneralDAO(ServletContextEvent event) {
		this.sc = event.getServletContext();
	}

	public GeneralDAO(ServletContext context) {
		this.sc = context;
	}

	public GeneralDAO() {
		this.sc = AttributeFactory.getServletContext();
	}

	/**
	 * 供 Local application 使用
	 * 
	 * @param driver
	 *            Example: com.mysql.jdbc.Driver
	 * @param jdbc
	 *            Example: jdbc:mysql://localhost:3306/myDB
	 * @param dbaccount
	 * @param dbpasswd
	 */
	public GeneralDAO(String driver, String jdbc, String dbaccount,
			String dbpasswd, int pagesize) {
		try {
			Class.forName(driver); 
			GeneralDAO.conn = DriverManager.getConnection(jdbc, dbaccount,
					dbpasswd);
			ApplicationScope.getAppConfig().setPageSize(pagesize);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 處理進入資料庫的字串資料
	 * 
	 * @param text
	 * @return
	 */

	public static String parseString(String text, int MAX_LENGTH) {
		if (text.length() >= MAX_LENGTH) {
			text = text.substring(0, MAX_LENGTH);
		}
		return text;
	}

	public static Integer parseInteger_NODEFAULT(Integer defaulttext,
			String text) {
		Integer result = defaulttext;
		if (text != null && text.matches("[0-9]+")) {
			result = Integer.valueOf(text);
		}
		return result;
	}

	public static Long parseLong(Long defaulttext, String text) {
		Long result = defaulttext;
		if (text != null && text.matches("[0-9]+")) {
			result = Long.valueOf(text);
		}
		return result;
	}

	public static Date parseDate(Date defaultvalue, String value) {
		Date result = defaultvalue;
		try {
			if (value != null && new Utils().isLegalDatestring(value)) {
				result = new Utils().parseDatetime(value);
			}
		} catch (DataException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws DataException {
		try {
			if (GeneralDAO.conn != null && !GeneralDAO.conn.isClosed()) {
				return GeneralDAO.conn;
			} else {
				this.initConnection(AttributeFactory.getServletContext());
				return conn;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 由 Initialized Listener 來取得 connection <br>
	 * 資料庫在應用程式初始化中放入 connection
	 * 
	 * @param event
	 * @return
	 */
	public void initConnection(ServletContext context) throws DataException {
		if (context == null) {
			context = this.sc;
		}
		try {
			if (ds == null) {
				InitialContext icontext = new InitialContext();
				ds = (DataSource) icontext.lookup("java:comp/env/mysql");
				try {
					GeneralDAO.conn = ds.getConnection();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DataException(e);
				}
				synchronized (context) {
					context.setAttribute("conn", GeneralDAO.conn);
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	public HashMap<String, Object> getObject(ResultSet rs) {
		ResultSetMetaData rsmd;
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		try {
			rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				hashmap.put(rsmd.getColumnName(i), rs.getObject(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

	/**
	 * 執行 SQL 查詢 SELECT
	 * 
	 * @param sql
	 * @return 回報一個 ArrayList 的 iterator
	 */
	public ArrayList<HashMap<String, Object>> executeQuery(String sql) {
		long starttime = System.currentTimeMillis();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Connection conn = this.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<HashMap<String, Object>>();
		}
		return list;
	}

	/**
	 * 執行 SQL 指令 DELETE
	 * 
	 * @param sql
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public boolean execute(String sql) throws DataException {
		long starttime = System.currentTimeMillis();

		boolean result = false;
		Connection conn = this.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			result = stmt.execute(sql);
			stmt.close();
		} catch (SQLException e) {
			Log log = new Log(this.getClass(), e);
			new LogDAO().insert(log);
			e.printStackTrace();
			throw new DataException("資料庫語句執行錯誤！(" + e.getLocalizedMessage()
					+ ")");
		}
		return result;
	}

	/**
	 * 處理 pstmt 的 executeQuery 並計算時間
	 * 
	 * @param pstmt
	 * @param SQL
	 * @return
	 * @throws SQLException
	 * @throws SQLException
	 */
	public ResultSet executeQuery(PreparedStatement pstmt) throws SQLException {
		long starttime = System.currentTimeMillis();
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}

	public int executeUpdate(PreparedStatement pstmt) throws SQLException {
		long starttime = System.currentTimeMillis();
		int result = pstmt.executeUpdate();
		return result;
	}

	/**
	 * UPDATE 專用
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public synchronized int executeUpdate(String sql) throws SQLException {
		int result = 0;
		Connection conn = this.getConnection();
		Statement stmt = null;
		stmt = conn.createStatement();
		result = stmt.executeUpdate(sql);
		stmt.close();
		return result;
	}

	/**
	 * 專門提供 INSERT 使用, 並回傳新增最後一筆的 ID
	 * 
	 */
	public synchronized int executeInsert(String sql) {
		Connection conn = this.getConnection();
		Statement stmt = null;
		int result = 0;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				do {
					for (int i = 1; i <= colCount; i++) {
						String key = rs.getString(i);
						result = Integer.parseInt(key);
					}
				} while (rs.next());
			} else {
				result = -1;
			}
		} catch (SQLException e) {
			Log log = new Log(this.getClass(), e);
			new LogDAO().insert(log);
			e.printStackTrace();
			return -1;
		}
		return result;
	}

	/**
	 * 所傳入的 sql 必須是 SELECT COUNT(*) AS COUNT ...才會被接受 <br>
	 * 只用來獲取總筆數
	 * 
	 * @param sql
	 * @return
	 */
	public int executeCount(String sql) {
		Connection conn = this.getConnection();
		Statement stmt = null;
		int result = 0;
		if (sql.matches("^SELECT.+FROM.*")) {
			sql = sql.replaceFirst("^SELECT.+FROM",
					"SELECT COUNT(*) AS COUNT FROM");
		} else {
			return -1;
		}
		try {
			stmt = conn.createStatement();
			if (sql.contains("ORDER")) {
				sql = sql.substring(0, sql.indexOf("ORDER") - 1);
			}
			if (!sql.toUpperCase().startsWith("SELECT COUNT(*) AS COUNT FROM")) {
				return -1;
			}
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			result = rs.getInt("COUNT");
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			Log log = new Log(this.getClass(), e);
			new LogDAO().insert(log);
			e.printStackTrace();
			return -1;
		}
		return result;
	}

	public Schema getSchema() throws DataException {
		Schema schema = new Schema();
		LinkedHashMap<String, LinkedHashMap<String, String>> tables = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		Connection conn = this.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("show tables");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String sql = "SHOW COLUMNS FROM " + rs.getString(1);
				PreparedStatement pstmt2 = conn.prepareStatement(sql);
				ResultSet rs2 = pstmt2.executeQuery();
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				while (rs2.next()) {
					map.put(rs2.getString(1), rs2.getString(2));
				}
				tables.put(rs.getString(1), map);
				rs2.close();
				pstmt2.close();
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		schema.setTables(tables);
		return schema;
	}

}
