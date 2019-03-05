package tw.zerojudge.Servlets.Utils;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Mailer;
import tw.zerojudge.Utils.Utils;

@WebServlet(urlPatterns = { "/ForgetPassword" })
public class ForgetPasswordServlet extends HttpServlet implements IAccessFilter {

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (!appConfig.getEnableMailer()) {
			throw new RoleException("郵件功能未開啟！");
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionScope sessionScope = new SessionScope(request);
		UserDAO userDao = new UserDAO();
		User user = new UserService().getUserByAccount(request.getParameter("account"));
		if (user != null && user.getEmail().equals(request.getParameter("email"))) {
			String newpasswd = new Utils().randomPassword();
			String content = "[" + ApplicationScope.getAppConfig().getTitle() + "(" + sessionScope.getHostURI()
					+ ")]\n";
			content += "帳號: " + user.getAccount() + "\n";
			content += "您的新密碼：" + newpasswd + "\n";
			try {
				Mailer mailer = new Mailer(user, "[" + sessionScope.getHostURI() + "] 密碼查詢結果回報！", content,
						sessionScope.getHostURI().toString());
				mailer.GmailSender();
			} catch (MessagingException e) {
				throw new JQueryException("很抱歉，郵件因某些原因無法寄送。MessagingException:" + e.getLocalizedMessage());
			} catch (Exception e) {
				throw new JQueryException("很抱歉，郵件因某些原因無法寄送。" + e.getLocalizedMessage());
			}
			user.setPasswd(newpasswd, newpasswd);
			new UserService().update(user);
			userDao.updatePasswd(user);
			throw new JQueryException("寄送程序完成，請即前往收信！");
		} else {
			throw new JQueryException("您所填寫的資料並不符合原先的註冊資料，請再次嘗試。");
		}
	}

}
