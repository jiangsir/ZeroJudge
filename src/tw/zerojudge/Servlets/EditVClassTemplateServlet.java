package tw.zerojudge.Servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.zerojudge.DAOs.VClassTemplateDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/EditVClassTemplate" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class EditVClassTemplateServlet extends HttpServlet implements IAccessFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		OnlineUser onlineUser = UserFactory.getOnlineUser(request);
		this.AccessFilter(onlineUser);
	}

	public void AccessFilter(OnlineUser onlineUser) throws AccessException {
		if (onlineUser.getIsHigherEqualThanMANAGER() || onlineUser.isVClassTemplate()) {
		} else {
			throw new AccessException("您無權限建立「課程模版」");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String templateid = request.getParameter("id");
		if (templateid == null || "".equals(templateid) || "0".equals(templateid)) {
			ArrayList<VClassTemplate> templates = new ArrayList<VClassTemplate>();
			templates.add(new VClassTemplate());
			request.setAttribute("vclassTemplates", templates);
		} else {
			ArrayList<VClassTemplate> templates = new VClassTemplateDAO()
					.getTemplatesById(Integer.parseInt(templateid));
			request.setAttribute("vclassTemplates", templates);
		}
		request.getRequestDispatcher("EditVClassTemplate.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String templateid = request.getParameter("id");
		if (templateid == null || "".equals(templateid) || "0".equals(templateid)) {
			this.insert(request, response);
			response.sendRedirect(request.getContextPath()
					+ ShowVClassesServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0]);
			return;
		} else {
			this.update(request, response);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("儲存成功!!");
			response.flushBuffer();
		}

	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String templateid = request.getParameter("id");
		if (templateid == null || "".equals(templateid) || "0".equals(templateid)) {
		} else {
			new VClassTemplateDAO().delete(Integer.parseInt(templateid));
		}
	}

	private ArrayList<String> checkInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		String[] titles = request.getParameterValues("title");
		String[] descripts = request.getParameterValues("descript");
		String[] problemidsArray = request.getParameterValues("TemplateContestProblemids");
		ArrayList<String> errors = new ArrayList<String>();
		for (int i = 0; i < titles.length; i++) {
			try {

				VClassTemplate vclassTemplate = new VClassTemplate();
				vclassTemplate.setName(request.getParameter("templateName"));
				vclassTemplate.setTitle(titles[i]);
				vclassTemplate.setOwnerid(onlineUser.getId());
				vclassTemplate.setDescript(descripts[i]);
				vclassTemplate.setProblemids(problemidsArray[i]);
				vclassTemplate.setVisible(request.getParameter("templateVisible"));
			} catch (Exception e) {
				errors.add(e.getLocalizedMessage());
			}
		}
		return errors;
	}

	private void checkInsertException(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<String> errors = checkInsert(request, response);
		String errorString = "";
		if (errors.size() != 0) {
			for (String error : errors) {
				errorString += error;
			}
			throw new DataException(errorString);
		}
	}

	private void insert(HttpServletRequest request, HttpServletResponse response) {
		try {
			checkInsertException(request, response);
			HttpSession session = request.getSession(false);
			OnlineUser onlineUser = UserFactory.getOnlineUser(session);

			String[] titles = request.getParameterValues("title");
			String[] descripts = request.getParameterValues("descript");
			String[] problemidsArray = request.getParameterValues("TemplateContestProblemids");
			for (int i = 0; i < titles.length; i++) {
				VClassTemplate vclassTemplate = new VClassTemplate();
				vclassTemplate.setName(request.getParameter("templateName"));
				vclassTemplate.setTitle(titles[i]);
				vclassTemplate.setOwnerid(onlineUser.getId());
				vclassTemplate.setDescript(descripts[i]);
				vclassTemplate.setProblemids(problemidsArray[i]);
				vclassTemplate.setVisible(request.getParameter("templateVisible"));
				new VClassTemplateDAO().insert(vclassTemplate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}
	}

	/**
	 * 編輯 templates 全部刪除之後再新增。
	 * 
	 * @param request
	 * @param response
	 */
	private void update(HttpServletRequest request, HttpServletResponse response) {
		try {
			checkInsertException(request, response);

			String templateid = request.getParameter("id");
			new VClassTemplateDAO().delete(Integer.parseInt(templateid));
			this.insert(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getLocalizedMessage());
		}

	}

}
