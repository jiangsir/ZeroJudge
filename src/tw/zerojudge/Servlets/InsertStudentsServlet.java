package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.MessageDAO;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Tables.VClass;
import tw.zerojudge.Tables.VClassStudent;
import tw.zerojudge.Tables.User.ROLE;

@WebServlet(urlPatterns = { "/InsertStudents" })
@RoleSetting(allowHigherThen = ROLE.USER)
public class InsertStudentsServlet extends HttpServlet implements IAccessFilter {
	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		int vclassid = Integer.parseInt(request.getParameter("vclassid"));
		VClass vclass = new VClassDAO().getVClassById(vclassid);
		if (!vclass.getIsOwner(onlineUser)) {
			throw new AccessException("您的權限無法批次新增課程學生！");
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("BatchInsertVClassStudents.jsp").forward(request, response);
	}

	private boolean checkStudentLine(String line) {
		String[] student = line.split(",");
		if (student.length != 6) {
			return false;
		} else if (!new UserService().isAvailableAccount(student[0].trim())) {
			return false;
		} else if (!User.islegelBirthyear(Integer.parseInt(student[4].trim()))) {
			return false;
		} else if (!new User().islegalEmail(student[5].trim())) {
			return false;
		}
		return true;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int vclassid = Integer.parseInt(request.getParameter("vclassid"));

		String scripts = request.getParameter("scripts");
		String[] lines = scripts.split("\n");
		try {
			for (int i = 0; i < lines.length; i++) {
				lines[i] = lines[i].trim();
				if (lines[i] != null && "".equals(lines[i]) && !lines[i].startsWith("#")) {
					if (new UserService().isexitAccount(lines[i].split(",")[0])) {
					} else {
						if (!checkStudentLine(lines[i])) {
							throw new DataException("您的批次文件<br>第 " + (i + 1) + "行有誤<br>" + lines[i] + "<br>可能的原因包括\""
									+ "帳號不合法、帳號已存在、出生年格式不正確" + "(不可少於 0 歲，也不可大於100歲)、Email格式不正確\"<br>");
						}
					}
				}
			}

			VClassStudentDAO vclassStudentDao = new VClassStudentDAO();
			VClass vclass = new VClassDAO().getVClassById(vclassid);
			for (int i = 0; i < lines.length; i++) {
				if (!lines[i].startsWith("#")) {
					String[] datas = lines[i].split(",");
					if (!new UserService().isexitAccount(datas[0].trim())) {
						continue;
					}
					User user = new UserService().getUserByAccount(datas[0].trim());
					//
					new VClassDAO().insertStudent(vclass, user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

}
