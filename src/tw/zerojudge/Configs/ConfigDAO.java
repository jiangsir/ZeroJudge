package tw.zerojudge.Configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.TreeMap;
import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.DAOs.GeneralDAO;
import tw.zerojudge.JsonObjects.Schema;
import tw.zerojudge.Tools.CONFIG;

public class ConfigDAO extends SuperDAO<AppConfig> {

	private Properties props;
	public File file;


	public ConfigDAO() {
	}


	public CONFIG getConfig() {
		return new CONFIG();
	}

	//

	public int insert(String key, String value) {
		String SQL = "INSERT INTO configs (`key`, value) VALUES(?,?)";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setString(1, key);
			pstmt.setString(2, value);
			new GeneralDAO().executeUpdate(pstmt);
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

	public int update(String key, String value) {
		String sql = "UPDATE configs SET value=? WHERE `key`=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setString(1, value);
			pstmt.setString(2, key);
			result = new GeneralDAO().executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}



	public Schema getCurrentDBSchema() {
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
				ResultSet rs2;
				try {
					rs2 = pstmt2.executeQuery();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
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
			throw new DataException(e);
		}
		schema.setTables(tables);
		return schema;
	}

	//
	//

	/**
	 * 取得所有 properties
	 */
	private TreeMap<String, String> getProperties() {
		TreeMap<String, String> list = new TreeMap<String, String>();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			props.loadFromXML(fis);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Enumeration<?> enumeration = props.keys();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement().toString();
			list.put(name, props.getProperty(name));
		}
		return list;
	}

	/**
	 * 以 key 取得單一 property 的值
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return props.getProperty(key) == null ? "" : props.getProperty(key).trim();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public void setProperty(String key, String value) {
		if (key == null || value == null) {
			return;
		}
		if (!props.containsKey(key)) {
			try {
				throw new DataException("key=" + key + " 不存在！");
			} catch (DataException e) {
				e.printStackTrace();
			}
			return;
		}
		props.setProperty(key, value);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			props.storeToXML(fos, "系統參數");
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeProperty(String key) {
		if (key == null) {
			return;
		}
		props.remove(key);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			props.storeToXML(fos, "系統參數");
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	//

	@Override
	public int insert(AppConfig t) throws DataException {
		return 0;
	}

	@Override
	public int update(AppConfig t) throws DataException {
		return 0;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

}
