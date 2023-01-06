package tw.zerojudge.Servlets.Json;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.JsonObjects.ContestSettings;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/Contest.json" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class ContestJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper mapper = new ObjectMapper(); 
	Logger logger = Logger.getAnonymousLogger();

	public static enum DATA {
		Settings; 
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String data = request.getParameter("data");
		try {
			Method getJSON = this.getClass().getMethod("getJSON_" + DATA.valueOf(data), HttpServletRequest.class);
			String jsonString = (String) getJSON.invoke(this, new Object[] { request });
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

	public String getJSON_Settings(HttpServletRequest request) {
		String contestid = request.getParameter("contestid");
		Contest contest = new ContestService().getContestById(contestid);
		ContestSettings contestSettings = new ContestSettings();
		contestSettings.setTitle(contest.getTitle());
		contestSettings.setSubtitle(contest.getSubtitle());
		contestSettings.setConfig(contest.getConfig());
		contestSettings.setProblemids(contest.getProblemids());
		contestSettings.setScores(contest.getScores());
		contestSettings.setStarttime(contest.getStarttime());
		contestSettings.setUserrules(contest.getUserrules());
		contestSettings.setTimelimit(contest.getTimelimit());
		contestSettings.setSortable(contest.getSortable());
		contestSettings.setVclasstemplateid(contest.getVclasstemplateid());
		try {
			return mapper.writeValueAsString(contestSettings);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

}
