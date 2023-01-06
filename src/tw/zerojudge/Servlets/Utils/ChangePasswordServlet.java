package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.zerojudge.DAOs.TokenService;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.Token;
import tw.zerojudge.Tables.User;

/**
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/ChangePassword" })
public class ChangePasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String base64 = request.getParameter("token");
		Token token = new TokenService().getTokenByBase64(base64);
		if (token == null) {
			throw new JQueryException("很抱歉，您沒有提供 token=" + base64);
		}
		if (token.getIsdone()) {
			throw new DataException("很抱歉，這個 token 已完成任務!");
		}
		if (System.currentTimeMillis() - token.getTimestamp().getTime() > 30 * 60 * 1000) {
			throw new DataException("很抱歉，這個 token 已逾時!");
		}

		String method = request.getMethod();
		if ("GET".equals(method)) {
			doGet(request, response);
		} else if ("POST".equals(method)) {
			doPost(request, response);
		} else {
			throw new DataException("無效的要求! (method=" + method + ")");
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String base64 = request.getParameter("token");
		Token token = new TokenService().getTokenByBase64(base64);
		User user = new UserService().getUserById(token.getUserid());

		request.setAttribute("user", user);
		request.setAttribute("token", token);
		request.getRequestDispatcher("ChangePassword.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String base64 = request.getParameter("token");
			Token token = new TokenService().getTokenByBase64(base64);
			User user = new UserService().getUserById(token.getUserid());

			String passwd = request.getParameter("passwd");
			String passwd2 = request.getParameter("passwd2");
			if ((passwd != null && !passwd.equals("")
					|| (passwd2 != null && !passwd2.equals("")))) {
				user.setPasswd(passwd, passwd2);
				new UserDAO().updatePasswd(user);
				token.setIsdone(true);
				new TokenService().update(token);
				throw new JQueryException("恭喜您，修改密碼完成!!,請進入登入頁面再次登入。");
			}
		} catch (DataException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

}
