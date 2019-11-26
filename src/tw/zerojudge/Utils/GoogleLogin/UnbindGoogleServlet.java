package tw.zerojudge.Utils.GoogleLogin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/UnbindGoogle" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class UnbindGoogleServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			AppConfig appConfig = ApplicationScope.getAppConfig();
			if (!appConfig.getIsGoogleLoginSetup()) {
				throw new DataException("GoogleLogin 並未設定完成！無法使用。請通知管理員。");
			}

			SessionScope sessionScope = new SessionScope(request);
			OnlineUser onlineUser = sessionScope.getOnlineUser();
			onlineUser.setPasswd(request.getParameter("UserPassword1"), request.getParameter("UserPassword2"));
			new UserDAO().updatePasswd(onlineUser);

			GoogleUser googleUser = sessionScope.getGoogleUser();
			if (onlineUser != null && googleUser != null && onlineUser.getIsAuthhost_Google()
					&& onlineUser.getEmail().equals(googleUser.getEmail())) {

				onlineUser.setAuthhost(User.AUTHHOST.localhost);
				sessionScope.removeGoogleUser();
				new UserService().update(onlineUser);
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
