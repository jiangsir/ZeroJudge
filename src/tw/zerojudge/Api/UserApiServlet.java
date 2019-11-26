package tw.zerojudge.Api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/User.api" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class UserApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7225313752592602748L;


	public static enum GET_ACTION {
		getUserStatistic; 
	};

	public static enum POST_ACTION {
		doRebuiltUserStatistic; 
	};

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String action = request.getParameter("action");
		try {
			Method method = this.getClass().getMethod("doGet_" + GET_ACTION.valueOf(action),
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
	}

	public void doGet_getUserStatistic(HttpServletRequest request, HttpServletResponse response) {

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

	public void doPost_doRebuiltUserStatistic(HttpServletRequest request, HttpServletResponse response) {
		try {
			long begin = System.currentTimeMillis();
			int userid = Integer.parseInt(request.getParameter("userid"));
			User user = new UserService().getUserById(userid);
			new UserService().rebuiltUserStatisticByDataBase(user);
			throw new JQueryException("User rebuilt 重建完畢！(" + (System.currentTimeMillis() - begin) + "ms)");
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
