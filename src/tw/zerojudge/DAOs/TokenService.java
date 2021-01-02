package tw.zerojudge.DAOs;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Tables.Token;

/**
 * @author jiangsir
 *
 */
public class TokenService {

	public Token getTokenByBase64(String base64) {
		if (base64 == null) {
			return null;
		}
		return new TokenDAO().getTokenByBase64(base64);
	}

	public int insert(Token token) throws DataException {
		return new TokenDAO().insert(token);
	}

	public int update(Token token) throws DataException {
		return new TokenDAO().update(token);
	}

}
