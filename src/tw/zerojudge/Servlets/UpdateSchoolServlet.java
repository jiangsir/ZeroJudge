package tw.zerojudge.Servlets;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.InfoException;
import tw.zerojudge.DAOs.SchoolService;
import tw.zerojudge.Objects.Parameter;
import tw.zerojudge.Tables.*;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/UpdateSchool" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class UpdateSchoolServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SchoolService schoolService = new SchoolService();
		Integer schoolid = Parameter.parseInteger(request.getParameter("schoolid"));
		String schoolurl = request.getParameter("url");
		String schoolname = request.getParameter("schoolname");

		if ("".equals(schoolurl) && "".equals(schoolname)) {
			School school = schoolService.getSchoolById(schoolid);
			schoolService.deleteSchool(schoolid);
			throw new InfoException("刪除學校資訊", "", "將『" + schoolid + ". " + school.getSchoolname() + "，URL:"
					+ school.getUrl() + "』學校刪除。所有屬於該學校的 User 將會改成\"不是學生\"");
		}
		School samenameSchool = schoolService.getOtherSchoolByName(schoolname, schoolid);

		if (!samenameSchool.isNullSchool()) {
			schoolService.mergeSchool(schoolid, samenameSchool);
			throw new InfoException("修改學校資訊", "",
					"因為指定了已經存在的學校名稱『" + schoolname + " (id:" + samenameSchool.getId() + ")』。因此將目前使用本校名的 user 全數合併到『"
							+ samenameSchool.getSchoolname() + " (URL:" + samenameSchool.getUrl()
							+ ")』當中，並且刪除目前的 School(id=" + schoolid + ")");
		}
		while (schoolurl.matches(".*\\/$")) {
			schoolurl = schoolurl.substring(0, schoolurl.length() - 1);
		}
		if (!schoolurl.startsWith("http")) {
			schoolurl = "http://" + schoolurl;
		}
		School sameurlSchool;
		sameurlSchool = new SchoolService().getOtherSchoolByUrl(new URL(schoolurl), schoolid);

		if (!sameurlSchool.isNullSchool()) {

			schoolService.mergeSchool(schoolid, sameurlSchool);
			throw new InfoException("修改學校資訊", "",
					"因為指定了已經存在的URL『" + schoolurl + " (id:" + sameurlSchool.getId() + ")』。因此將目前使用本 URL 的 user 全數合併到『"
							+ sameurlSchool.getSchoolname() + " (URL:" + sameurlSchool.getUrl()
							+ ")』當中，並且刪除目前的 School(id=" + schoolid + ")");
		}

		String imgsrc = Parameter.parseString(request.getParameter("imgsrc"));
		String descript = Parameter.parseString(request.getParameter("descript"));
		Integer checkid = Parameter.parseInteger(request.getParameter("checkid"));

		School school = new SchoolService().getSchoolById(schoolid);
		school.setUrl(schoolurl);
		school.setSchoolname(schoolname);
		school.setImgsrc(imgsrc);
		school.setDescript(descript);
		school.setCheckid(checkid);

		schoolService.update(school);

	}
}
