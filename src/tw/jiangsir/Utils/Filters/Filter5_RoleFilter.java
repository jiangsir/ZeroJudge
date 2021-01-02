package tw.jiangsir.Utils.Filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.Privilege;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.jiangsir.Utils.Exceptions.SystemClosedException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Api.ContestApiServlet;
import tw.zerojudge.Api.ProblemApiServlet;
import tw.zerojudge.Api.SolutionApiServlet;
import tw.zerojudge.Api.VclassApiServlet;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.*;
import tw.zerojudge.Servlets.Ajax.ReJudgeAjaxServlet;
import tw.zerojudge.Servlets.Json.ContestJsonServlet;
import tw.zerojudge.Servlets.Json.ProblemJsonServlet;
import tw.zerojudge.Servlets.Json.SolutionJsonServlet;
import tw.zerojudge.Servlets.Utils.*;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.GoogleLogin.GoogleLoginServlet;
import tw.zerojudge.Utils.GoogleLogin.InsertGoogleUserServlet;
import tw.zerojudge.Utils.GoogleLogin.CallbackGoogleLoginServlet;
import tw.zerojudge.Utils.GoogleLogin.CallbackInsertGoogleUserServlet;

@WebFilter(filterName = "RoleFilter", urlPatterns = { "/*" }, asyncSupported = true)
@Privilege
public class Filter5_RoleFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public Filter5_RoleFilter() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		String servletPath = request.getServletPath();
		HttpServlet httpServlet = ApplicationScope.getUrlpatterns().get(servletPath);
		if (httpServlet == null) {
			chain.doFilter(request, response);
			return;
		}
		if (request.getMethod().toUpperCase().equals("GET")) {
			new SessionScope(session).setReturnPage(servletPath, request.getQueryString());
		}
		AppConfig appConfig = ApplicationScope.getAppConfig();
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);

		/**
		 * 先檢查整站模式。
		 * 
		 */
		switch (appConfig.getSystemMode()) {
		case CLOSE_MODE:
			if (httpServlet.getClass() == LoginServlet.class || httpServlet.getClass() == LogoutServlet.class
					|| httpServlet.getClass() == ReJudgeServlet.class
					|| httpServlet.getClass() == ReJudgeAjaxServlet.class
					|| httpServlet.getClass() == ShowImageServlet.class
					|| httpServlet.getClass() == SolutionApiServlet.class
					|| httpServlet.getClass() == GoogleLoginServlet.class
					|| httpServlet.getClass() == CallbackGoogleLoginServlet.class
					|| onlineUser.getIsHigherEqualThanMANAGER()) {
				chain.doFilter(request, response);
				return;
			}
			throw new SystemClosedException(appConfig.getSystem_closed_message(), "");
		case CLASS_MODE:

			if (httpServlet.getClass() == LoginServlet.class || httpServlet.getClass() == GoogleLoginServlet.class
					|| httpServlet.getClass() == CallbackGoogleLoginServlet.class
					|| httpServlet.getClass() == CallbackInsertGoogleUserServlet.class
					|| httpServlet.getClass() == InsertGoogleUserServlet.class
					|| httpServlet.getClass() == InsertUserServlet.class
					|| httpServlet.getClass() == ForgetPasswordServlet.class
					|| httpServlet.getClass() == ChangePasswordServlet.class) {
				chain.doFilter(request, response);
				return;
			}

			onlineUser = UserFactory.getOnlineUser(session);
			if (onlineUser.isNullOnlineUser()) {
				response.sendRedirect(
						request.getContextPath() + LoginServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
				return;
			}

			if (!onlineUser.getIsHigherEqualThanMANAGER()) {
				@SuppressWarnings("unchecked")
				HashSet<Class<? extends HttpServlet>> servletSet = (HashSet<Class<? extends HttpServlet>>) ApplicationScope
						.getRoleMap().get(onlineUser.getRole()).clone();
				servletSet.clear();
				servletSet.add(LoginServlet.class);
				servletSet.add(LogoutServlet.class);
				servletSet.add(GoogleLoginServlet.class);
				servletSet.add(CallbackGoogleLoginServlet.class);
				servletSet.add(InsertGoogleUserServlet.class);
				servletSet.add(CallbackInsertGoogleUserServlet.class);
				servletSet.add(InsertUserServlet.class);
				servletSet.add(ForgetPasswordServlet.class);
				servletSet.add(ChangePasswordServlet.class);

				servletSet.add(InsertUsersServlet.class);
				servletSet.add(UpdateUserServlet.class);
				servletSet.add(UserStatisticServlet.class);
				servletSet.add(ShowImageServlet.class);
				servletSet.add(ReloadUserinfoServlet.class);
				servletSet.add(ShowIMessagesServlet.class);
				servletSet.add(SendIMessageServlet.class);

				servletSet.add(EditVClassTemplateServlet.class);
				servletSet.add(ShowVClassesServlet.class);
				servletSet.add(ShowVClassServlet.class);
				servletSet.add(CreateVClassByTemplateServlet.class);
				servletSet.add(DeleteVClassServlet.class);
				servletSet.add(InsertVContestServlet.class);
				servletSet.add(UpdateVClassServlet.class);
				servletSet.add(UpdateContestServlet.class);
				servletSet.add(VclassApiServlet.class);
				servletSet.add(EditContestantsServlet.class);
				servletSet.add(DownloadServlet.class);
				servletSet.add(RebuiltContestServlet.class);
				servletSet.add(ContestsServlet.class);
				servletSet.add(ContestSubmissionsServlet.class);
				servletSet.add(ContestApiServlet.class);
				servletSet.add(ContestJsonServlet.class);
				servletSet.add(ContestRankingServlet.class);
				servletSet.add(ShowContestServlet.class);

				servletSet.add(TestjudgeServlet.class);
				servletSet.add(SolutionApiServlet.class);
				servletSet.add(SolutionJsonServlet.class);
				servletSet.add(SubmitCodeServlet.class);

				servletSet.add(ProblemJsonServlet.class);
				servletSet.add(InsertProblemServlet.class);
				servletSet.add(ImportProblemsServlet.class);
				servletSet.add(EditProblemsServlet.class);
				servletSet.add(ProblemApiServlet.class);
				servletSet.add(UpdateProblemServlet.class);
				servletSet.add(ShowProblemServlet.class);

				if (!servletSet.contains(httpServlet.getClass())) {
					response.sendRedirect(request.getContextPath()
							+ ShowVClassesServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
					return;
				}
			}

			chain.doFilter(request, response);
			break;

		case PROBLEMS_MODE:
			if (httpServlet.getClass() == LoginServlet.class || httpServlet.getClass() == GoogleLoginServlet.class
					|| httpServlet.getClass() == CallbackGoogleLoginServlet.class
					|| httpServlet.getClass() == CallbackInsertGoogleUserServlet.class
					|| httpServlet.getClass() == InsertGoogleUserServlet.class
					|| httpServlet.getClass() == ForgetPasswordServlet.class
					|| httpServlet.getClass() == ChangePasswordServlet.class
					|| httpServlet.getClass() == ProblemsServlet.class) {
				chain.doFilter(request, response);
				return;
			}

			onlineUser = UserFactory.getOnlineUser(session);

			if (!onlineUser.getIsHigherEqualThanMANAGER()) {
				@SuppressWarnings("unchecked")
				HashSet<Class<? extends HttpServlet>> servletSet = (HashSet<Class<? extends HttpServlet>>) ApplicationScope
						.getRoleMap().get(onlineUser.getRole()).clone();
				servletSet.clear();
				servletSet.add(LoginServlet.class);
				servletSet.add(LogoutServlet.class);
				servletSet.add(GoogleLoginServlet.class);
				servletSet.add(CallbackGoogleLoginServlet.class);

				servletSet.add(ProblemApiServlet.class);
				servletSet.add(ShowProblemServlet.class);
				servletSet.add(ProblemJsonServlet.class);
				servletSet.add(ShowImageServlet.class);

				if (!servletSet.contains(httpServlet.getClass())) {
					response.sendRedirect(request.getContextPath()
							+ ProblemsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
					return;
				}
			}

			chain.doFilter(request, response);
			break;

		case CONTEST_MODE:
			if (httpServlet.getClass() == LoginServlet.class || httpServlet.getClass() == GoogleLoginServlet.class
					|| httpServlet.getClass() == CallbackGoogleLoginServlet.class
					|| httpServlet.getClass() == CallbackInsertGoogleUserServlet.class
					|| httpServlet.getClass() == InsertGoogleUserServlet.class
					|| httpServlet.getClass() == InsertUserServlet.class
					|| httpServlet.getClass() == ForgetPasswordServlet.class
					|| httpServlet.getClass() == ChangePasswordServlet.class) {
				chain.doFilter(request, response);
				return;
			}

			onlineUser = UserFactory.getOnlineUser(session);
			if (onlineUser.isNullOnlineUser()) {
				response.sendRedirect(
						request.getContextPath() + LoginServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
				return;
			}

			if (!onlineUser.getIsHigherEqualThanMANAGER()) {
				@SuppressWarnings("unchecked")
				HashSet<Class<? extends HttpServlet>> servletSet = (HashSet<Class<? extends HttpServlet>>) ApplicationScope
						.getRoleMap().get(onlineUser.getRole()).clone();
				servletSet.clear();
				servletSet.add(LoginServlet.class);
				servletSet.add(ForgetPasswordServlet.class);
				servletSet.add(GoogleLoginServlet.class);
				servletSet.add(CallbackGoogleLoginServlet.class);
				servletSet.add(LogoutServlet.class);

				servletSet.add(ContestsServlet.class);
				servletSet.add(ContestSubmissionsServlet.class);
				servletSet.add(ContestApiServlet.class);
				servletSet.add(ContestJsonServlet.class);
				servletSet.add(ContestRankingServlet.class);
				servletSet.add(ShowProblemServlet.class);
				servletSet.add(SubmitCodeServlet.class);
				servletSet.add(ReloadUserinfoServlet.class);
				servletSet.add(TestjudgeServlet.class);
				servletSet.add(SolutionApiServlet.class);
				servletSet.add(ShowContestServlet.class);
				servletSet.add(ShowIMessagesServlet.class);
				servletSet.add(SendIMessageServlet.class);
				servletSet.add(ShowVClassServlet.class);
				servletSet.add(SolutionJsonServlet.class);
				servletSet.add(ProblemJsonServlet.class);
				servletSet.add(ShowImageServlet.class);

				if (!servletSet.contains(httpServlet.getClass())) {
					response.sendRedirect(request.getContextPath()
							+ ContestsServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
					return;
				}
			}

			onlineUser = UserFactory.getOnlineUser(session);
			if (onlineUser.isNullOnlineUser()) {
				response.sendRedirect(
						request.getContextPath() + LoginServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
				return;
			}

			chain.doFilter(request, response);
			break;
		case TRAINING_MODE:

			if (httpServlet.getClass().getAnnotation(RoleSetting.class) == null) {
				chain.doFilter(request, response);
				return;
			}

			onlineUser = UserFactory.getOnlineUser(session);
			if (onlineUser.isNullOnlineUser()) {
				response.sendRedirect(
						request.getContextPath() + LoginServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
				return;
			}
			if (!this.isUserInRoles(onlineUser, httpServlet)) {
				throw new RoleException("您沒有權限瀏覽這個頁面。");
			}

			chain.doFilter(request, response);
			break;
		default:
			ArrayList<String> debugs = new ArrayList<String>();
			debugs.add("SYSTEM_MODE=" + appConfig.getSystemMode());

			Alert alert = new Alert(Alert.TYPE.ROLEERROR, "系統模式有誤！", "SYSTEM_MODE 設定錯誤，請聯繫管理員修正。", "", null, debugs,
					onlineUser);
			throw new RoleException(alert);
		}

	}

	public void destroy() {

	}

	/**
	 * 取得某一個 servlet 所有“允許”存取的 roles<br>
	 * 例：<br>
	 * IndexServlet = HashSet{User.ROLE.DEBUGGER, User.ROLE.MANAGER, User.ROLE.USER,
	 * User.ROLE.GUEST}<br>
	 * AdminServlet = HashSet{User.ROLE.DEBUGGER}<br>
	 * 
	 * @param httpServlet
	 * @return
	 */
	public HashSet<User.ROLE> getRoleSet(HttpServlet httpServlet) {
		HashSet<User.ROLE> roleSet = new HashSet<User.ROLE>();
		RoleSetting roleSetting = httpServlet.getClass().getAnnotation(RoleSetting.class);
		if (roleSetting == null) {
			for (User.ROLE role : User.ROLE.values()) {
				roleSet.add(role);
			}
			return roleSet;
		}

		for (User.ROLE role : User.ROLE.values()) {
			if (role.isHigherEqualThan(roleSetting.allowHigherThen())) {
				roleSet.add(role);
			}
		}

		for (User.ROLE role : roleSetting.allows()) {
			roleSet.add(role);
		}

		for (User.ROLE role : User.ROLE.values()) {
			if (role.isLowerEqualThan(roleSetting.denyLowerThen())) {
				roleSet.remove(role);
			}
		}

		for (User.ROLE role : roleSetting.denys()) {
			roleSet.remove(role);
		}
		return roleSet;

	}

	/**
	 * 只判斷該使用者是否是 某個 servlet 的 RoleSetting 允許 ROLE
	 * 
	 * @param user
	 * @param httpServlet
	 * @return
	 */
	private boolean isUserInRoles(OnlineUser onlineUser, HttpServlet httpServlet) {
		return this.getRoleSet(httpServlet).contains(onlineUser.getRole());
	}

}
