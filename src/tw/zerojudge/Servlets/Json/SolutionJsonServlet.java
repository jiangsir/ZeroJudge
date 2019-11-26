package tw.zerojudge.Servlets.Json;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Servlets.ShowCodeServlet;
import tw.zerojudge.Servlets.ShowDetailsServlet;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = {"/Solution.json"})
@RoleSetting(allowHigherThen = ROLE.USER)
public class SolutionJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper(); 
	Logger logger = Logger.getAnonymousLogger();

	public static enum DATA {
		Code, 
		ServerOutputs; 
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String data = request.getParameter("data");
		try {
			Method Method_isVisible = this.getClass().getMethod("isVisible_" + DATA.valueOf(data),
					HttpServletRequest.class);
			boolean isVisible = (boolean) Method_isVisible.invoke(this, new Object[]{request});
			if (!isVisible) {
				throw new JQueryException("權限不足！無法顯示！");
			}
			Method getJSON = this.getClass().getMethod("getJSON_" + DATA.valueOf(data), HttpServletRequest.class);
			String jsonString = (String) getJSON.invoke(this, new Object[]{request});
			response.getWriter().write(jsonString);
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

	/**
	 * 提供ajax 讀取
	 * 
	 * @param request
	 * @return
	 */
	public boolean isVisible_Code(HttpServletRequest request) {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		String solutionid = request.getParameter("solutionid");
		Solution solution = new SolutionService().getSolutionById(solutionid);
		return new ShowCodeServlet().isAccessible(onlineUser, solution);
	}

	public String getJSON_Code(HttpServletRequest request) {
		String solutionid = request.getParameter("solutionid");
		Solution solution = new SolutionService().getSolutionById(solutionid);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", solution.getId());
		map.put("userid", solution.getUserid());
		map.put("pid", solution.getPid());
		map.put("code", StringEscapeUtils.escapeHtml(solution.getCode()));
		map.put("language", solution.getLanguage());
		try {
			return mapper.writeValueAsString(map);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	/**
	 * 提供ajax 讀取
	 * 
	 * @param request
	 * @return
	 */
	public boolean isVisible_ServerOutputs(HttpServletRequest request) {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		String solutionid = request.getParameter("solutionid");
		Solution solution = new SolutionService().getSolutionById(solutionid);
		try {
			new ShowDetailsServlet().AccessFilter(onlineUser, solution);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	/**
	 * 提供 ajax 讀取
	 * 
	 * @return
	 */
	public String getJSON_ServerOutputs(HttpServletRequest request) {
		OnlineUser onlineUser = new SessionScope(request).getOnlineUser();
		String solutionid = request.getParameter("solutionid");
		Solution solution = new SolutionService().getSolutionById(solutionid);

		try {
			new ShowDetailsServlet().AccessFilter(onlineUser, solution);
		} catch (AccessException e) {
			e.printStackTrace();
			throw new JQueryException(
					"您(" + onlineUser + ")無權限觀看本資訊(#" + solution.getId() + ")。\n" + e.getLocalizedMessage());
		}
		try {
			logger.info("solutionid=" + solutionid + ", serveroutputs="
					+ mapper.writeValueAsString(solution.getServeroutputs()));
			ResourceBundle resource = ResourceBundle.getBundle("resource",
					new SessionScope(request).getSession_locale());
			ServerOutput[] serverOutputs = solution.getServeroutputs();
			for (ServerOutput serverOutput : serverOutputs) {
				String hint = serverOutput.getHint().equals("")
						? resource.getString("Server.REASON." + serverOutput.getReason().name())
						: serverOutput.getHint();
				serverOutput.setHint(hint);

				if (solution.getContestid() > 0 && serverOutput.getJudgement() == ServerOutput.JUDGEMENT.WA) {
					if (!solution.getContest().isVContest()) {
						serverOutput.setHint("競賽／測驗中的測資一律為「不公開」");
					} else if (solution.getProblem().getWa_visible().intValue() == Problem.WA_visible_HIDE) {
						serverOutput.setHint("本題目的測資設定為「不公開」");
					} else if (!solution.getContest().isCheckedConfig_ShowWA()) {
						serverOutput.setHint("本「隨堂測驗」設定不顯示 WA 正確答案！");
					}
				}
			}
			return mapper.writeValueAsString(serverOutputs);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

}
