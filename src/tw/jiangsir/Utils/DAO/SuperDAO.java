package tw.jiangsir.Utils.DAO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.DataConnectionException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;

abstract public class SuperDAO<T> {
	private static Connection conn = null;
	private static DataSource source = null;

	abstract protected int insert(T t) throws SQLException;

	abstract protected int update(T t) throws SQLException;

	abstract protected boolean delete(int i);

	Logger logger = Logger.getLogger(this.getClass().getName());
	HashMap<String, Field> fields = new HashMap<String, Field>();
	int PAGESIZE = 20;

	public Connection getConnection() {
		try {
			if (conn == null || conn.isClosed()) {
				if (source == null) {
					InitialContext icontext = new InitialContext();
					source = (DataSource) icontext.lookup("java:comp/env/mysql");
				}
				conn = source.getConnection();
			} else {
				return conn;
			}
		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataException("資料庫名稱設定有誤！", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataConnectionException("無法連接資料庫，請通知管理員！", e);
		}
		return conn;
	}

	public void setConnection(String driver, String jdbc, String dbaccount, String dbpasswd) {
		try {
			Class.forName(driver); 
			SuperDAO.conn = DriverManager.getConnection(jdbc, dbaccount, dbpasswd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getSetMethodName(String columnName) {
		String firstchar = columnName.substring(0, 1);
		firstchar = firstchar.toUpperCase();
		return "set" + firstchar + columnName.substring(1);
	}

	/**
	 * 取得某個 class 內某個 Persistent name 的 Field
	 * 
	 * @param theclass
	 * @return
	 */
	private Field getField(Class<T> theclass, String persistentname) {
		if (this.fields.size() == 0) {
			for (Field field : theclass.getDeclaredFields()) {
				Persistent persistent = field.getAnnotation(Persistent.class);
				if (persistent != null) {
					fields.put(persistent.name(), field);
				}
			}
		}
		return fields.get(persistentname);
	}

	public ArrayList<T> executeQueryByAnnotations(PreparedStatement pstmt, Class<T> clazz) {
		long starttime = System.currentTimeMillis();
		logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
				+ ".executeQueryByAnnotations()=" + pstmt.toString());

		ResultSet rs = null;
		ArrayList<T> list = new ArrayList<T>(); 
		try {
			rs = pstmt.executeQuery(); 
			ResultSetMetaData rsmd = rs.getMetaData(); 
			int columnCount = rsmd.getColumnCount(); 
			String columnName;
			Field field;
			Object value;
			while (rs.next()) {
				T t = (T) clazz.newInstance(); 
				for (int i = 1; i <= columnCount; i++) {
					columnName = rsmd.getColumnName(i);

					field = this.getField(clazz, columnName);
					if (field == null) {
						continue;
					}
					if (rsmd.getColumnType(i) == Types.BOOLEAN || rsmd.getColumnType(i) == Types.TINYINT) {
						value = rs.getBoolean(columnName);
					} else if (rsmd.getColumnType(i) == Types.INTEGER) {
						value = rs.getInt(columnName);
					} else if (rsmd.getColumnType(i) == Types.BIGINT) {
						value = rs.getLong(columnName);
					} else if (rsmd.getColumnType(i) == Types.BLOB || rsmd.getColumnType(i) == Types.LONGVARBINARY) {
						value = rs.getBytes(rsmd.getColumnName(i));
					} else {
						value = rs.getObject(columnName);
					}

					try {
						String settername = "set" + field.getName().toUpperCase().substring(0, 1)
								+ field.getName().substring(1);
						Method settermethod = clazz.getMethod(settername, new Class[] { value.getClass() });
						settermethod.invoke(t, new Object[] { value });
					} catch (SecurityException e) {
						e.printStackTrace();
						continue;
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						continue;
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
				list.add(t);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQueryByAnnotations()=" + pstmt.toString() + " 共耗時 "
					+ (System.currentTimeMillis() - starttime) + " ms");
			try {

				if (rs != null) {
					rs.close();
				}
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public ArrayList<T> executeQuery(PreparedStatement pstmt, Class<T> theclass) {
		long starttime = System.currentTimeMillis();
		ResultSet rs = null;
		ArrayList<T> list = new ArrayList<T>(); 
		try {
			rs = pstmt.executeQuery(); 
			ResultSetMetaData rsmd = rs.getMetaData(); 
			int columnCount = rsmd.getColumnCount(); 
			while (rs.next()) {
				T t = (T) theclass.newInstance(); 
				for (int i = 1; i <= columnCount; i++) {
					String settername = this.getSetMethodName(rsmd.getColumnName(i));
					Object value;
					if (rsmd.getColumnType(i) == Types.BLOB || rsmd.getColumnType(i) == Types.LONGVARBINARY) {
						value = rs.getBytes(rsmd.getColumnName(i));
					} else {
						value = rs.getObject(rsmd.getColumnName(i));
					}

					try {
						Method m = t.getClass().getMethod(settername, new Class[] { value.getClass() });
						m.invoke(t, new Object[] { value });
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
						continue;
					} catch (NoSuchMethodException e1) {
						continue;
					}
				}
				list.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			logger.info(
					ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName() + ".executeQuery()="
							+ pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 僅回傳該 table 的第一個欄位，並且回傳 long, 通常第一個欄位都是一個 table 的 id.
	 * 
	 * @param sql
	 * @return
	 */
	public ArrayList<Long> executeQueryId(String sql) {
		long starttime = System.currentTimeMillis();
		ResultSet rs = null;
		PreparedStatement pstmt;
		ArrayList<Long> list = new ArrayList<Long>();
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			rs = pstmt.executeQuery(); 
			ResultSetMetaData rsmd = rs.getMetaData(); 
			while (rs.next()) {
				String idname = rsmd.getColumnName(1);
				Long id = rs.getLong(idname);
				list.add(id);
			}
			logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQueryId()=" + pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime)
					+ " ms");
		} catch (SQLException e) {
			logger.severe(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQueryId()=" + sql + " 執行失敗！");

			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 僅回傳該 table 的第一個欄位，並且回傳 long, 通常第一個欄位都是一個 table 的 id.
	 * 
	 * @param sql
	 * @return
	 */
	public ArrayList<Long> executeQueryId(PreparedStatement pstmt) {
		long starttime = System.currentTimeMillis();
		ResultSet rs = null;
		ArrayList<Long> list = new ArrayList<Long>();
		try {
			rs = pstmt.executeQuery(); 
			ResultSetMetaData rsmd = rs.getMetaData(); 
			while (rs.next()) {
				String idname = rsmd.getColumnName(1);
				Long id = rs.getLong(idname);
				list.add(id);
			}
			logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQueryId()=" + pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime)
					+ " ms");
		} catch (SQLException e) {
			logger.severe(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQuery()=" + pstmt.toString() + " 執行失敗！");
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<T> executeQuery(String sql, Class<T> theclass) {
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			ArrayList<T> t = this.executeQueryByAnnotations(pstmt, theclass);
			return t;
		} catch (SQLException e) {
			logger.severe(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQuery()=" + sql + " 執行失敗！");
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}

	public ArrayList<HashMap<String, Object>> executeQueryByMap(String sql) {
		long starttime = System.currentTimeMillis();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Connection conn = this.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQueryByMap()=" + sql);
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
			logger.severe(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeQueryByMap()=" + sql + " 執行失敗！");
			e.printStackTrace();
			return new ArrayList<HashMap<String, Object>>();
		}
		logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
				+ ".executeQueryByMap()=" + sql + ", 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
		return list;
	}

	public synchronized int executeUpdate(PreparedStatement pstmt) throws SQLException {
		long starttime = System.currentTimeMillis();

		int result = -1;
//		logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
//				+ ".executeUpdate()=" + pstmt.toString());
		try {
			result = pstmt.executeUpdate();
			logger.info("(耗時 " + (System.currentTimeMillis() - starttime) + " ms) "
					+ ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeUpdate()=" + pstmt.toString());
		} catch (SQLException e) {
			logger.warning("[SQL錯誤] " + ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName()
					+ ".executeUpdate()=" + pstmt.toString());
			throw new SQLException(e);
		}
		return result;
	}

	/**
	 * 執行 SQL 指令
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sql) {
		long starttime = System.currentTimeMillis();
		boolean result = false;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName() + ".execute()="
					+ pstmt.toString());
			result = pstmt.execute(sql);
			logger.info(ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName() + ".execute()="
					+ pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}

	public synchronized boolean execute(PreparedStatement pstmt) throws SQLException {
		return pstmt.execute();
	}

	/**
	 * 新的 executeCount 不再去更改原始 sql 字串，而是直接製作 count 語法，並利用 fields 來加入條件。
	 * 
	 * @param tablename
	 * @param fields
	 * @return
	 */
	public int executeCount(String tablename, TreeMap<String, Object> fields) {
		long starttime = System.currentTimeMillis();
		String sql = "SELECT COUNT(*) AS COUNT FROM " + tablename + this.makeFields(fields, null, 0);
		int count = 0;
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			int i = 1;
			if (fields != null)
				for (String key : fields.keySet()) {
					pstmt.setObject(i++, fields.get(key));
				}

			ResultSet rs = pstmt.executeQuery();
			logger.info(this.getClass().getSimpleName() + ".executeCount()=" + pstmt.toString() + " 共耗時 "
					+ (System.currentTimeMillis() - starttime) + " ms");
			rs.next();
			count = rs.getInt("COUNT");
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			logger.info(this.getClass().getSimpleName() + ".executeCount()=" + sql + " 執行錯誤！");
			e.printStackTrace();
			return -1;
		}
		return count;
	}

	/**
	 * 只用來獲取總筆數
	 * 
	 * @param sql
	 * @return
	 */
	public int executeCount(String sql) {
		long starttime = System.currentTimeMillis();

		int result = 0;
		if (sql.matches("^SELECT.+FROM.*")) {
			sql = sql.replaceFirst("^SELECT.+FROM", "SELECT COUNT(*) AS COUNT FROM");
		} else {
			return -1;
		}
		try {

			if (sql.contains("ORDER")) {
				sql = sql.substring(0, sql.indexOf("ORDER") - 1);
			}
			if (!sql.toUpperCase().startsWith("SELECT COUNT(*) AS COUNT FROM")) {
				return -1;
			}

			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery(sql);
			logger.info(
					ApplicationScope.getAppRoot().getName() + "/" + this.getClass().getSimpleName() + ".executeCount()="
							+ pstmt.toString() + " 共耗時 " + (System.currentTimeMillis() - starttime) + " ms");
			rs.next();
			result = rs.getInt("COUNT");
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		return result;
	}

	/**
	 * 
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public synchronized int executeInsert(PreparedStatement pstmt) throws SQLException {
		long starttime = System.currentTimeMillis();
		int id = -1;
		try {
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			logger.info(this.getClass().getSimpleName() + ".executeInsert()=" + pstmt.toString() + " 共耗時 "
					+ (System.currentTimeMillis() - starttime) + " ms");
			rs.close();
			pstmt.close();
			return id;
		} catch (SQLException e) {
			logger.warning("執行失敗!! " + this.getClass().getSimpleName() + ".executeInsert()=" + pstmt.toString());
			throw new SQLException(e);
		}
	}

	/**
	 * 執行 SQL 指令
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public synchronized boolean executeDelete(PreparedStatement pstmt) throws SQLException {
		int result = pstmt.executeUpdate();
		logger.info(pstmt.toString());
		pstmt.close();
		return result == 0 ? false : true;
	}

	/**
	 * 將 rules 組合成 sql 語法。 <br>
	 * 注意：只有 WHERE 以後的部分進行組合。
	 * 
	 * @param rules
	 * @param orderby
	 * @param page
	 * @return
	 */
	public String makeRules(TreeSet<String> rules, String orderby, int page) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append(" ");
		Iterator<String> ruleit = rules.iterator();
		if (ruleit.hasNext()) {
			sql.append("WHERE ");
		}
		while (ruleit.hasNext()) {
			sql.append(" (" + ruleit.next() + ")");
			if (ruleit.hasNext()) {
				sql.append(" AND ");
			}
		}
		if (orderby != null && !"".equals(orderby.trim())) {
			sql.append(" ORDER BY " + orderby);
		}
		if (page > 0) {
			sql.append(" LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE);
		}
		return sql.toString();
	}

	/**
	 * 將 fields 組合成 sql 語法。 <br>
	 * 注意：只有 WHERE 以後的部分進行組合。
	 * 
	 * @param fields
	 * @param orderby
	 * @param page
	 * @return
	 */
	public String makeFields(TreeMap<String, Object> fields, String orderby, int page) {
		StringBuffer sql = new StringBuffer(5000);
		sql.append(" ");
		if (fields != null) {
			Iterator<String> keys = fields.keySet().iterator();
			if (keys.hasNext()) {
				sql.append("WHERE ");
			}
			while (keys.hasNext()) {
				sql.append(" (" + keys.next() + "=?)");
				if (keys.hasNext()) {
					sql.append(" AND ");
				}
			}
		}
		if (orderby != null && !"".equals(orderby.trim())) {
			sql.append(" ORDER BY " + orderby);
		}
		if (page > 0) {
			sql.append(" LIMIT " + ((page > 1 ? page : 1) - 1) * PAGESIZE + "," + PAGESIZE);
		}
		return sql.toString();
	}

}
