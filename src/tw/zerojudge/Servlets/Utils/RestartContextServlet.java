package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.zerojudge.Configs.ConfigFactory;

@WebServlet(urlPatterns = { "/RestartContext" })
@RoleSetting
public class RestartContextServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String[] CMD_restart = new String[] { "/bin/sh", "-c",
				"touch " + ConfigFactory.getWebxml() };
		RunCommand runCompile = new RunCommand(CMD_restart, 0);
		Thread runThread = new Thread(runCompile);
		runThread.start();
		response.sendRedirect("." + new SessionScope(session).getPreviousPage());
	}
}
