package tw.zerojudge.Api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.JsonObjects.ContestSettings;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Servlets.ShowVClassServlet;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.VClass;
import tw.zerojudge.Tables.VClassStudent;

@WebServlet(urlPatterns = { "/Vclass.api" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class VclassApiServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;

	public static enum POST_ACTION {
		SaveVContestSortable, 
		getContestProblems_TypeB, 
		getStudentsForUpdateStudentsComments_CLASSMODE, 
		getStudentsForUpdateStudentsComments_TRAINMODE, 
		updateStudentsComments, 
		removeVContest, 
		cloneVContestById, 
		InsertVContestByJson, 
		InsertVContest, 
		RenewVclasscode, 
		RemoveStudent, 
		JoinVclassByVclasscode; //
	};

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {

	}

	public void AccessFilter_VClassOwner(HttpServletRequest request, String action) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		if (!onlineUser.isContestManager()) {
			throw new AccessException("您沒有「競賽管理者」權限！");
		}
		int contestid = Parameter.parseInteger(request.getParameter("contestid"));
		int vclassid = Parameter.parseInteger(request.getParameter("vclassid"));
		if (contestid < 0 && vclassid < 0) {
			throw new AccessException("參數錯誤！必須提供 「課程編號」vclassid");
		}
		if (vclassid < 0) {
			Contest contest = new ContestService().getContestById(contestid);
			vclassid = contest.getVclassid();
		}

		if (vclassid == 0) {
			throw new AccessException("錯誤！contestid#" + contestid + "並非一個「課程」。");
		}
		VClass vclass = new VClassDAO().getVClassById(vclassid);
		if (!vclass.getIsOwner(onlineUser)) {
			throw new AccessException("您(" + onlineUser + ")並非「課程擁有者」無法進行(" + action + ")！");
		}
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

	public void doPost_SaveVContestSortable(HttpServletRequest request, HttpServletResponse response) {
		SessionScope sessionScope = new SessionScope(request);
		String vclassid = request.getParameter("vclassid");
		try {
			VClass vclass = new VClassDAO().getVClassById(Parameter.parseInteger(vclassid));

			if (!vclass.getIsOwner(sessionScope.getOnlineUser())) {
				throw new JQueryException("權限不足，必須是開課人員才可以進行這個動作。");
			}
			String[] vcontestids = request.getParameter("vcontestids").split(",");

			ContestService contestService = new ContestService();
			int sortable = 0;
			for (String vcontestid : vcontestids) {
				Contest vcontest = contestService.getContestById(Parameter.parseInteger(vcontestid));
				if (vcontest.getVclassid().intValue() == vclass.getId()) {
					vcontest.setSortable(++sortable);
					contestService.update(vcontest);
				}
			}
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage() + ": vclassid=" + vclassid + " 有誤！");
		}
	}

	public void doPost_JoinVclassByVclasscode(HttpServletRequest request, HttpServletResponse response) {
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
			response.getWriter()
					.print(request.getContextPath()
							+ ShowVClassServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0] + "?vclassid="
							+ vclass.getId());
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
			new VClassDAO().removeVClassStudent(Integer.parseInt(vclassid), Integer.parseInt(userid));
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	public void doPost_getContestProblems_TypeB(HttpServletRequest request, HttpServletResponse response) {
		SessionScope sessionScope = new SessionScope(request);
		try {
			String vclassid = request.getParameter("vclassid");
			VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(vclassid));
			if (!vclass.getIsOwner(sessionScope.getOnlineUser())) {
				throw new JQueryException("權限不足，必須是開課人員才可以進行這個動作。");
			}
			int userid = Integer.parseInt(request.getParameter("userid"));

			request.setAttribute("vcontests",
					new ContestService().getContestsByVclassid(vclass.getId(), "sortable ASC, id ASC", 0));
			request.setAttribute("userid", userid);

			request.getRequestDispatcher("api/StudentStatistics.jsp").forward(request, response);

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

	public void doPost_InsertVContest(HttpServletRequest request, HttpServletResponse response) {
		try {
			AccessFilter_VClassOwner(request, request.getParameter("action"));

			int vclassid = Parameter.parseInteger(request.getParameter("vclassid"));

			VClassDAO vclassDao = new VClassDAO();
			VClass vclass = vclassDao.getVClassById(vclassid);

			vclassDao.insertVContest(vclass);
		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	/**
	 * 用 JSON 建立競賽。
	 * 
	 * @param request
	 * @param response
	 */
	public void doPost_InsertVContestByJson(HttpServletRequest request, HttpServletResponse response) {
		try {
			AccessFilter_VClassOwner(request, request.getParameter("action"));

			SessionScope sessionScope = new SessionScope(request);
			ObjectMapper mapper = new ObjectMapper(); 

			int vclassid = Parameter.parseInteger(request.getParameter("vclassid"));
			String VContestSettings = request.getParameter("VContestSettings");

			if (VContestSettings.trim().equals("")) {
				VClassDAO vclassDao = new VClassDAO();
				VClass vclass = vclassDao.getVClassById(vclassid);
				vclassDao.insertVContest(vclass);
			} else {
				ContestSettings contestSettings = mapper.readValue(VContestSettings, ContestSettings.class);

				Contest contest = new Contest();
				contest.setTitle(contestSettings.getTitle());
				contest.setSubtitle(contestSettings.getSubtitle());
				contest.setConfig(contestSettings.getConfig());
				contest.setTimelimit(contestSettings.getTimelimit());
				contest.setProblemids(contestSettings.getProblemids());
				contest.setScores(contestSettings.getScores());
				contest.setUserrules(contestSettings.getUserrules());
				contest.setSortable(contestSettings.getSortable());
				contest.setStarttime(contestSettings.getStarttime());
				contest.setVclasstemplateid(contestSettings.getVclasstemplateid());

				contest.setVclassid(vclassid);
				contest.setOwnerid(sessionScope.getOnlineUser().getId());
				new ContestService().insert(contest);
			}

		} catch (Exception e) {
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	public void doPost_cloneVContestById(HttpServletRequest request, HttpServletResponse response) {
		try {
			AccessFilter_VClassOwner(request, request.getParameter("action"));

			String cloneContestid = request.getParameter("contestid");
			if (cloneContestid == null || !cloneContestid.matches("[0-9]+")) {
			} else {
				new VClassDAO().cloneVContestById(Parameter.parseInteger(cloneContestid));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e);
		}
	}

	/**
	 * 刪除隨堂測驗
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void doPost_removeVContest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			AccessFilter_VClassOwner(request, request.getParameter("action"));

			Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
			contest.setVisible(Contest.VISIBLE.remove);
			new ContestService().update(contest);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e);
		}
	}

	/**
	 * 更新課程參與者註解，更新學生註解。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void doPost_updateStudentsComments(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			AccessFilter_VClassOwner(request, request.getParameter("action"));

			SessionScope sessionScope = new SessionScope(request);

			VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(request.getParameter("vclassid")));
			UserService userService = new UserService();
			String csv = request.getParameter("csv_UpdateStudentsComments");

			for (String csvline : csv.split("\n")) {
				csvline = csvline.trim();
				if (csvline.startsWith("#")) {
					continue;
				}
				String comment = "";
				User user;
				if (ApplicationScope.getAppConfig().getIsCLASS_MODE()) {
					String[] csvfields = csvline.split(",");
					if (csvfields.length != 3) {
						throw new DataException("欄位數目不正確, csvfields.length=" + csvfields.length);
					}
					String schoolid = csvfields[0].trim();
					String xuehao = csvfields[1].trim();
					user = userService.getUserByXuehaoSchoolid(xuehao, schoolid);
					comment = csvfields[2].trim();

				} else {
					String[] csvfields = csvline.split(",");
					if (csvfields.length != 2) {
						throw new DataException("欄位數目不正確, csvfields.length=" + csvfields.length);
					}
					String account = csvfields[0].trim();
					user = userService.getUserByAccount(account);
					comment = csvfields[1].trim();
				}

				if (vclass.getIsOwner(sessionScope.getOnlineUser()) && vclass.isStudent(user.getId())) {
					VClassStudentDAO dao = new VClassStudentDAO();
					VClassStudent student = dao.getStudent(vclass.getId(), user.getId());
					student.setComment(comment);
					dao.update(student);

				} else {
					throw new DataException("有學生不屬於本課程，無法更新。 user=" + user.getSchoolid() + "," + user.getComment() + ","
							+ user.getUsername());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e);
		}
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void doPost_getStudentsForUpdateStudentsComments_TRAINMODE(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			AccessFilter_VClassOwner(request, request.getParameter("action"));

			VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(request.getParameter("vclassid")));
			String restext = "";
			for (VClassStudent student : vclass.getStudents()) {
				String comment = student.getComment().trim();
				restext += student.getUser().getAccount() + "," + ("".equals(comment) ? "註解\n" : comment + "\n");
			}
			response.getWriter().write(restext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e);
		}
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void doPost_getStudentsForUpdateStudentsComments_CLASSMODE(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			AccessFilter_VClassOwner(request, request.getParameter("action"));

			VClass vclass = new VClassDAO().getVClassById(Integer.parseInt(request.getParameter("vclassid")));
			String restext = "";
			for (VClassStudent student : vclass.getStudents()) {
				restext += student.getUser().getSchoolid() + "," + student.getUser().getAccount() + "," + "學生註解\n";
			}
			response.getWriter().write(restext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e);
		}
	}

}
