package tw.zerojudge.Servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.User.ROLE;

@MultipartConfig(maxFileSize = 200 * 1024 * 1024, maxRequestSize = 500 * 1024 * 1024)
@WebServlet(urlPatterns = {"/ImportProblems"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class ImportProblemsServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.getIsHigherEqualThanMANAGER() && !onlineUser.isInsertProblem()) {
			throw new AccessException("您沒有權限匯入題目！");
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
		request.getRequestDispatcher("ImportProblems.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		request.setCharacterEncoding("UTF-8"); 
		int count = 0;
		String plainMessage = "";

		for (Part part : request.getParts()) {
			if ("importproblems".equals(part.getName())) {
				String header = part.getHeader("Content-Disposition");
				String filename = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));
				InputStream in = part.getInputStream();
				File tmpfile = new File(ApplicationScope.getSystemTmp(), filename);
				OutputStream out = new FileOutputStream(tmpfile);
				byte[] buffer = new byte[1024];
				int length = -1;
				while ((length = in.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();

				count++;
				Problem problem = new ProblemService().ImportProblemJson(onlineUser, tmpfile);
				plainMessage += "匯入 " + tmpfile.getName() + "完成，新的題目編號為 " + problem.getProblemid() + "<br>";
			}
		}

		throw new InfoException("匯入成功！", "您共匯入了以下 " + count + " 個題目。", plainMessage);
	}
}
