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

			if (onlineUser != null && onlineUser.getIsAuthhost_Google()) {
				GoogleUser googleUser = sessionScope.getGoogleUser();
				if (googleUser == null) {
					throw new DataException("請通過 「Google 登入」後才能進行「解除綁定」。");
				} else if (!onlineUser.getEmail().equals(googleUser.getEmail())) {
					throw new DataException("User 的 email 設定與登入的google email 不相符，無法解除綁定。");
				} else {
					onlineUser.setAuthhost(User.AUTHHOST.localhost);
					new UserService().update(onlineUser);
					sessionScope.removeGoogleUser();
				}
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
