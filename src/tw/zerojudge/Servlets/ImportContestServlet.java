package tw.zerojudge.Servlets;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User.ROLE;

@MultipartConfig(maxFileSize = 200 * 1024 * 1024, maxRequestSize = 500 * 1024 * 1024)
@WebServlet(urlPatterns = { "/ImportContest" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class ImportContestServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.getIsHigherEqualThanMANAGER()) {
			throw new AccessException("您沒有權限匯入「競賽」！");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MultipartConfig config = this.getClass().getAnnotation(MultipartConfig.class);
		long maxFileSize = config.maxFileSize();
		long maxRequestSize = config.maxRequestSize();
		request.setAttribute("maxFileSize", maxFileSize);
		request.setAttribute("maxRequestSize", maxRequestSize);
		request.setAttribute("targetVclassid", request.getParameter("targetVclassid"));
		request.getRequestDispatcher("ImportContest.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession(false);
			OnlineUser onlineUser = UserFactory.getOnlineUser(session);
			int targetVclassid = Parameter.parseInteger(request.getParameter("targetVclassid"));
			request.setCharacterEncoding("UTF-8"); 
			int count = 0;
			String plainMessage = "";

			for (Part part : request.getParts()) {
				if ("importContest".equals(part.getName())) {
					String header = part.getHeader("Content-Disposition");
					String filename = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
					File tmpfile = new File(ApplicationScope.getSystemTmp(), filename);

					count++;
					new ContestService().ImportContestByJson(onlineUser, tmpfile, targetVclassid);
					plainMessage += "匯入 " + tmpfile.getName() + "完成<br>";
				}
			}
			throw new InfoException("匯入成功！", "您共匯入了以下 " + count + " 個 Contest。", plainMessage);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

	}
}
