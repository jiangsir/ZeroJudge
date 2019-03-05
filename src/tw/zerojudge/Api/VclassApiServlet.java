package tw.zerojudge.Api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.JsonObjects.Redirect;
import tw.zerojudge.Servlets.ContestsServlet;
import tw.zerojudge.Servlets.ShowVClassServlet;
import tw.zerojudge.Servlets.SubmissionsServlet;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Tables.VClass;

@WebServlet(urlPatterns = { "/Vclass.api" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class VclassApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;

	public static enum POST_ACTION {
		RenewVclasscode, 
		RemoveStudent, 
		JoinVclassByVclasscode; //
	};

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String action = request.getParameter("action");
		try {
			Method method = this.getClass().getMethod("doPost_" + POST_ACTION.valueOf(action),
					new Class[] { HttpServletRequest.class, HttpServletResponse.class });
			method.invoke(this, new Object[] { request, response });
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new JQueryException(new Alert(e.getTargetException()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
		return;
	}

	public void doPost_JoinVclassByVclasscode(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			SessionScope sessionScope = new SessionScope(request);
			String vclasscode = request.getParameter("vclasscode");
			VClass vclass = new VClassDAO().getVClassByVclasscode(vclasscode);
			if (vclass == null || vclass.getIsNull()) {
				throw new DataException("這個課程代碼無法找到合適的課程，代碼(" + vclasscode + ")可能有誤。");
			}

			try {
				new VClassDAO().insertStudent(vclass, sessionScope.getOnlineUser());
			} catch (DataException e) {
				if (!e.getLocalizedMessage().startsWith("重複加入")) {
					throw e;
				}
			}
			response.getWriter().print(request.getContextPath()
					+ ShowVClassServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]
					+ "?vclassid=" + vclass.getId());
			return;
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	public void doPost_RemoveStudent(HttpServletRequest request, HttpServletResponse response) {
		SessionScope sessionScope = new SessionScope(request);

		try {
			String userid = request.getParameter("userid");
			String vclassid = request.getParameter("vclassid");
			VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(vclassid));
			if (!vclass.getIsOwner(sessionScope.getOnlineUser())) {
				throw new JQueryException("權限不足，必須是開課人員才可以進行這個動作。");
			}
			new VClassDAO().removeVClassStudent(Integer.parseInt(vclassid),
					Integer.parseInt(userid));
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	public void doPost_RenewVclasscode(HttpServletRequest request, HttpServletResponse response) {
		SessionScope sessionScope = new SessionScope(request);
		try {
			String vclassid = request.getParameter("vclassid");
			VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(vclassid));
			if (!vclass.getIsOwner(sessionScope.getOnlineUser())) {
				throw new JQueryException("權限不足，必須是開課人員才可以進行這個動作。");
			}
			new VClassDAO().renewVClassCode(Integer.parseInt(vclassid));
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
