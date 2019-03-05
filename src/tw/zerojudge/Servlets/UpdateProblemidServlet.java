package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.Objects.Problemid;

@WebServlet(urlPatterns = { "/UpdateProblemid" })
@RoleSetting
public class UpdateProblemidServlet extends HttpServlet implements
		IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws RoleException {
		throw new RoleException("本項功能已取消。");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("UpdateProblemid.jsp").forward(request,
				response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Problemid oldid = new Problemid(request.getParameter("oldid"));
		Problemid newid = new Problemid(request.getParameter("newid"));
		if (!oldid.getIsLegal()) {
			throw new DataException(oldid + " 編號不正確！");
		}
		if (!newid.getIsLegal()) {
			throw new DataException(newid + " 編號不正確！");
		}
		throw new DataException("本功能暫時停止使用！");
	}

}
