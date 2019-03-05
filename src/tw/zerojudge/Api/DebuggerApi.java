package tw.zerojudge.Api;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Configs.ServerConfig;
import tw.zerojudge.DAOs.AppConfigService;
import tw.zerojudge.DAOs.GeneralDAO;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.JsonObjects.Schema;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Tables.Message;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Utils.Mailer;

@WebServlet(urlPatterns = { "/Debugger.api" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class DebuggerApi extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	public enum ACTION {
		deleteHttpscrt, 
		updateDatabaseVersion, //
		removeSchema, //
		compareSchema, //
		addSchema, //
		showDbSchema, //
		getCatalina, //
		currect_ProblemScores, //
		setIpallow, //
		setIpdeny, //
		isGmailAccount, //
		readServerConfig, 
		deleteProblemtab, //
		getProblemtabs, 
		checkNopassRsync, 
		checkAllowedIP, 
		checkReadServerConfig, 
		checkLocker, 
	}


	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		OnlineUser onlineUser = UserFactory.getOnlineUser(request.getSession(false));

		String action = request.getParameter("action");
		switch (ACTION.valueOf(action)) {
		case setIpallow:
			break;
		case setIpdeny:
			break;
		default:
			break;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		String action = request.getParameter("action");
		switch (ACTION.valueOf(action)) {
		case deleteHttpscrt:
			appConfig.setHttpscrt(new byte[] {});
			new AppConfigService().update(appConfig);
			return;
		case addSchema:
			Schema[] schemas = appConfig.getSchemas();

			Schema dbschema = new GeneralDAO().getSchema();
			dbschema.setVersion(request.getParameter("version"));
			Schema[] newschemas = new Schema[schemas.length + 1];
			for (int i = 0; i < schemas.length; i++) {
				newschemas[i] = schemas[i];
			}
			newschemas[newschemas.length - 1] = dbschema;
			appConfig.setSchemas(newschemas);

			new AppConfigService().update(appConfig);
			return;
		case compareSchema:
			int index = Integer.parseInt(request.getParameter("index"));
			appConfig = ApplicationScope.getAppConfig();
			schemas = appConfig.getSchemas();
			Schema[] compareSchemas = new Schema[2];
			compareSchemas[0] = schemas[index];
			compareSchemas[1] = new GeneralDAO().getSchema();

			response.getWriter().print(mapper.writeValueAsString(compareSchemas));
			return;
		case currect_ProblemScores:
			String result = new ProblemDAO().currect_ProblemScores();
			Message message = new Message();
			message.setPlainMessage(result);
			new MessageDAO().dispatcher(request, response, message);
			return;
		case getCatalina:
			String logfile = request.getParameter("logfile");
			String logpath = System.getProperty("catalina.home") + File.separator + "logs" + File.separator;
			StringBuffer s = new StringBuffer(50000);
			for (String line : FileUtils.readLines(new File(logpath), logfile)) {
				s.append(line);
			}
			response.getWriter().print(s.toString());
			return;
		case isGmailAccount:
			String email = request.getParameter("email");
			String passwd = request.getParameter("passwd");
			message = new Message();
			message.setPlainTitle(email + " 是否為 Gmail Account? " + new Mailer().isGmailAccount(email, passwd));
			new MessageDAO().dispatcher(request, response, message);
			return;
		case removeSchema:
			index = Integer.parseInt(request.getParameter("index"));
			appConfig = ApplicationScope.getAppConfig();
			schemas = appConfig.getSchemas();

			newschemas = new Schema[schemas.length - 1];
			for (int i = 0; i < newschemas.length; i++) {
				if (i == index) {
					i--;
					continue;
				}
				newschemas[i] = schemas[i];
			}
			appConfig.setSchemas(newschemas);
			new AppConfigService().update(appConfig);
			return;
		case setIpallow:
			IpAddress ip = new IpAddress(request.getParameter("ip"));
			appConfig = ApplicationScope.getAppConfig();
			appConfig.getBannedIPSet().remove(ip);
			new AppConfigService().update(appConfig);
			break;
		case setIpdeny:
			ip = new IpAddress(request.getParameter("ip"));
			appConfig = ApplicationScope.getAppConfig();
			appConfig.getBannedIPSet().add(ip);
			new AppConfigService().update(appConfig);
			break;
		case showDbSchema:
			dbschema = new GeneralDAO().getSchema();
			response.getWriter().print(mapper.writeValueAsString(dbschema));
			return;
		case updateDatabaseVersion:

			break;
		case readServerConfig:
			URL serverUrl = ApplicationScope.getAppConfig().getServerUrl();
			ServerConfig serverConfig = ApplicationScope.getAppConfig().readServerConfig(serverUrl);
			ApplicationScope.getAppConfig().setServerConfig(serverConfig);
			break;
		case deleteProblemtab:
			String tabid = request.getParameter("tabid");
			String renametotabid = request.getParameter("renametotabid");
			Problemtab from_tab = null;
			Problemtab to_tab = null;
			if (ApplicationScope.getAppConfig().getProblemtabs().size() <= 1) {
				throw new JQueryException("不能刪除全部的「題目標籤」。");
			}
			for (Problemtab problemtab : ApplicationScope.getAppConfig().getProblemtabs()) {
				if (problemtab.getId().equals(tabid)) {
					from_tab = problemtab;
					continue;
				}
				if (problemtab.getId().equals(renametotabid)) {
					to_tab = problemtab;
					continue;
				}
			}
			new AppConfigService().deleteProblemTab(from_tab, to_tab);
			return;
		case getProblemtabs:
			request.getRequestDispatcher("include/div/Problemtabs.jsp").forward(request, response);
			return;
		case checkAllowedIP:
			this.GET_checkAllowedIP(request, response);
			break;
		case checkReadServerConfig:
			this.GET_checkReadServerConfig(request, response);
			break;
		case checkNopassRsync:
			this.GET_checkNopassRsync(request, response);
			break;
		case checkLocker:
			this.GET_checkLocker(request, response);
			break;
		default:
			break;
		}
		return;
	}

	private void GET_checkAllowedIP(HttpServletRequest request, HttpServletResponse response) {
		String judgerUrl = request.getParameter("judgerUrl");
		try {
			response.getWriter().print(ApplicationScope.getAppConfig().getCheckAllowedIP(judgerUrl));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void GET_checkReadServerConfig(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String judgeUrl = request.getParameter("judgerUrl");
		URL judgeURL;
		try {
			judgeURL = new URL(judgeUrl);
			ApplicationScope.getAppConfig().readServerConfig(judgeURL);
			response.getWriter().print(true);
		} catch (DataException e) {
			e.printStackTrace();
			response.getWriter().print(false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			response.getWriter().print(false);
		}
	}

	private void GET_checkNopassRsync(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String urlstring = request.getParameter("serverUrl");
		URL serverUrl = null;
		if (urlstring == null) {
			serverUrl = ApplicationScope.getAppConfig().getServerUrl();
		} else {
			try {
				serverUrl = new URL(urlstring);
			} catch (MalformedURLException e) {
				response.getWriter().print(false);
			}
		}
		response.getWriter().print(ApplicationScope.getAppConfig().getCheckNopassRsync(serverUrl));
	}

	private void GET_checkLocker(HttpServletRequest request, HttpServletResponse response) {
		try {
			URL url = new URL(request.getParameter("judgerUrl"));
			String cryptKey = request.getParameter("cryptKey");
			response.getWriter().print(ApplicationScope.getAppConfig().getCheckLocker(url, cryptKey));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
