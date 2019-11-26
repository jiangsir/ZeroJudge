package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Tables.*;

/**
 * @author jiangsir
 *
 */
public class TokenDAO extends SuperDAO<Token> {
	ObjectMapper mapper = new ObjectMapper(); 

	public TokenDAO() {
	}

	public Token getTokenByBase64(String base64) throws DataException {
		String sql = "SELECT * FROM tokens WHERE base64=?";
		PreparedStatement pstmt;
		try {
			pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, base64);
			for (Token token : executeQuery(pstmt, Token.class)) {
				return token;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return null;
	}

	protected synchronized int insert(Token token) throws DataException {
		String sql = "INSERT INTO tokens(base64, userid, descript, isdone, timestamp) "
				+ "VALUES (?,?,?,?,?);";
		int id = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, token.getBase64());
			pstmt.setInt(2, token.getUserid());
			pstmt.setString(3, token.getDescript());
			pstmt.setBoolean(4, token.getIsdone());
			pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		token.setId(id);
		return id;
	}

	protected synchronized int update(Token token) throws DataException {

		String SQL = "UPDATE tokens SET isdone=?, descript=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setBoolean(1, token.getIsdone());
			pstmt.setString(2, token.getDescript());
			pstmt.setInt(3, token.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return result;
	}

	@Override
	protected boolean delete(int i) {
		return false;
	}

}
