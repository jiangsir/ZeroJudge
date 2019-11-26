package tw.zerojudge.Api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.SchoolService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.School;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/Manager.api" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class ManagerApi extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	public enum POSTACTION {
		InsertSchool, //
		UpdateSchool, //
		DeleteSchool, //
	}


	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		OnlineUser onlineUser = UserFactory.getOnlineUser(request.getSession(false));

		String action = request.getParameter("action");
		switch (POSTACTION.valueOf(action)) {

		default:
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SchoolService schoolService = new SchoolService();
		String action = request.getParameter("action");
		switch (POSTACTION.valueOf(action)) {
		case DeleteSchool:
			Integer schoolid = Parameter.parseInteger(request.getParameter("schoolid"));
			School school = schoolService.getSchoolById(schoolid);
			schoolService.deleteSchool(schoolid);
			throw new InfoException("刪除學校資訊", "", "將『" + schoolid + ". " + school.getSchoolname() + "，URL:"
					+ school.getUrl() + "』學校刪除。所有屬於該學校的 User 將會改成\"不是學生\"");
		case InsertSchool:
			try {
				School newschool = new School();
				newschool.setSchoolname(request.getParameter("schoolname"));
				newschool.setUrl(request.getParameter("url"));
				newschool.setImgsrc(request.getParameter("imgsrc"));
				newschool.setDescript(request.getParameter("descript"));
				newschool.setCheckid(School.CHECKID_CHECKED);
				schoolService.insert(newschool);
			} catch (Exception e) {
				throw new JQueryException(e.getLocalizedMessage());
			}
			break;
		case UpdateSchool:
			try {
				schoolid = Parameter.parseInteger(request.getParameter("schoolid"));
				school = schoolService.getSchoolById(schoolid);
				school.setSchoolname(request.getParameter("schoolname"));
				school.setUrl(request.getParameter("url"));
				school.setImgsrc(request.getParameter("imgsrc"));
				school.setDescript(request.getParameter("descript"));
				school.setCheckid(request.getParameter("checkid"));
				schoolService.update(school);
			} catch (Exception e) {
				throw new JQueryException(e.getLocalizedMessage());
			}
			break;
		default:
			throw new DataException("參數錯誤！" + action);
		}

	}
}
