package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.InfoException;

@WebServlet(urlPatterns = { "/RebuiltUserDifficultylist" })
@RoleSetting
public class RebuiltUserDifficultylistServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("RebuiltUserDifficultylist.jsp").forward(
				request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String rebuiltAll = request.getParameter("rebuiltAll");
		if (!"yes".equals(rebuiltAll)) {
			String account = request.getParameter("account");
			throw new InfoException(account + " 重整完成");
		} else {
			throw new InfoException("全體重建完成");
		}
	}

}
