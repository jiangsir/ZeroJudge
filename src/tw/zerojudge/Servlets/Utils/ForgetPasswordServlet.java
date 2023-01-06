package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.TokenTool;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.TokenService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.Token;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Mailer;

/**
 * @author jiangsir
 *
 */
@WebServlet(urlPatterns = { "/ForgetPassword" })
public class ForgetPasswordServlet extends HttpServlet {


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
		AppConfig appConfig = ApplicationScope.getAppConfig();
		if (!appConfig.getEnableMailer()) {
			throw new JQueryException("郵件功能未開啟，無法寄送密碼認證信！");
		}

		SessionScope sessionScope = new SessionScope(request);
		User user = new UserService().getUserByAccount(request.getParameter("account"));
		if (user != null && user.getEmail().equals(request.getParameter("email"))) {
		} else {
			throw new JQueryException("您所填寫的資料並不符合原先的註冊資料，請再次嘗試。");
		}

		try {
			Token token = new Token();
			token.setBase64(TokenTool.generateTokenUrlBase64());
			token.setUserid(user.getId());
			token.setDescript("ForgetPassword: " + user.getAccount() + " " + user.getEmail());
			new TokenService().insert(token);

			String content = "";
			content += "<html><head></head><body>";
			content += "<h1>[" + ApplicationScope.getAppConfig().getTitle() + "(" + sessionScope.getHostURI()
					+ ")]</h1>";
			content += "您好：<br><br>" + "這封認證信為處理您忘記密碼，當您收到本「認證信函」後，" + "請直接點選下方連結重新設置您的密碼，無需回信。";
			String ChangePassword = ChangePasswordServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];
			content += "<h2><a href=\"" + sessionScope.getHostURI() + "/" + ChangePassword + "?token="
					+ token.getBase64() + "\">修改密碼</a></h2>";
			content += "</body></html>";

			String subject = "[密碼查詢驗證信] FROM " + ApplicationScope.getAppConfig().getTitle() + "("
					+ sessionScope.getHostURI() + ") ";
			Mailer mailer = new Mailer(user, subject, content, sessionScope.getHostURI().toString());
			mailer.GmailSender();
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new JQueryException("很抱歉，郵件因某些原因無法寄送。MessagingException:"
					+ (e.getLocalizedMessage() == null ? e.getClass() : e.getLocalizedMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException("很抱歉，郵件因某些原因無法寄送。" + e.getLocalizedMessage());
		}
		throw new JQueryException("寄送程序完成，請即前往收信！");
	}

}
