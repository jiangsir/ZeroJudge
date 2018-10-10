package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Annotations.AppConfigField;
import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.Alert;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Exceptions.JQueryException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.AppConfigService;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.Tables.User.ROLE;
import tw.zerojudge.Utils.*;
import tw.zerojudge.JsonObjects.Banner;
import tw.zerojudge.JsonObjects.Problemtab;

@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 5 * 1024 * 1024)
@WebServlet(urlPatterns = { "/EditAppConfig" })
@RoleSetting(allowHigherThen = ROLE.MANAGER)
public class EditAppConfigServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ObjectMapper mapper = new ObjectMapper(); 


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppConfig appConfig = ApplicationScope.getAppConfig();

		try {
			ArrayList<Problemtab> problemtabs = appConfig.getProblemtabs();
			request.setAttribute("tabs", problemtabs);
			request.setAttribute("json_tabs", mapper.writeValueAsString(problemtabs));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		request.setAttribute("appConfig", appConfig);

		request.setAttribute("request_headers", new Utils().getRequestHeaders(request));
		request.getRequestDispatcher("EditAppConfig.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			AppConfig appConfig = ApplicationScope.getAppConfig();
			for (Field field : appConfig.getClass().getDeclaredFields()) {
				AppConfigField parameter = field.getAnnotation(AppConfigField.class);
				if (parameter == null)
					continue;
				Method method;
				try {
					method = appConfig.getClass().getMethod(
							"set" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1),
							String.class);
					method.invoke(appConfig, new Object[] { request.getParameter(parameter.name().toString()) });
				} catch (SecurityException e) {
					e.printStackTrace();
					throw new JQueryException(new Alert(e));
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new JQueryException(new Alert(e));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new JQueryException(new Alert(e));
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					throw new JQueryException(new Alert(e.getTargetException()));
				}
			}

			for (Part part : request.getParts()) {
				if ("titleImage".equals(part.getName()) && part.getSize() > 0) {
					appConfig.setTitleImageFileType(part.getContentType());
					appConfig.setTitleImage(IOUtils.toByteArray(part.getInputStream()));
				} else if ("logo".equals(part.getName()) && part.getSize() > 0) {
					appConfig.setLogoFileType(part.getContentType());
					appConfig.setLogo(IOUtils.toByteArray(part.getInputStream()));
				} else if ("httpscrt".equals(part.getName()) && part.getSize() > 0) {
					appConfig.setHttpscrt(IOUtils.toByteArray(part.getInputStream()));
				}
			}

			appConfig.setProblemtabs(this.makeProblemtabs(request));
			appConfig.setBanners(this.makeBanners(request));

			new AppConfigService().update(appConfig);

			if (appConfig.getSystemMode() == AppConfig.SYSTEM_MODE.CLOSE_MODE) {
				new UserDAO().doForcedLogout();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JQueryException(e.getClass() + ": " + e.getLocalizedMessage());
		}

		return;
	}

	private ArrayList<Banner> makeBanners(HttpServletRequest request) {
		ArrayList<Banner> banners = new ArrayList<Banner>();
		String[] percents = request.getParameterValues("percent");
		String[] bannerContents = request.getParameterValues("bannerContent");
		if (percents == null || bannerContents == null || percents.length == 0 || bannerContents.length == 0) {
			return null;
		}
		for (int i = 0; i < percents.length; i++) {
			percents[i] = percents[i].trim();
			try {
				banners.add(new Banner(bannerContents[i], Integer.parseInt(percents[i])));
			} catch (NumberFormatException e) {
				throw new DataException("「比率」欄位必須是整數。 " + e.getLocalizedMessage());
			}
		}
		return banners;
	}

	private ArrayList<Problemtab> makeProblemtabs(HttpServletRequest request) {
		int orderbySize = 3;

		String[] tabids = request.getParameterValues("tabid");
		String[] tabnames = request.getParameterValues("tabname");
		String[] tabdescripts = request.getParameterValues("tabdescript");
		String[] taborderby = request.getParameterValues("orderby");

		if (!(tabids.length == tabnames.length && tabnames.length == tabdescripts.length)) {
			throw new JQueryException("參數數目不搭配！tabids.length=" + tabids.length + ", tabnames.length=" + tabnames.length
					+ ", tabdescripts.length=" + tabdescripts.length);
		}

		ArrayList<Problemtab> NEW_tabs = new ArrayList<Problemtab>();
		int i = 0;
		for (String tabid : tabids) {
			Problemtab newProblemtab = new Problemtab();
			if (tabid == null || "".equals(tabid)) {
				continue;
			}

			newProblemtab.setId(tabid);
			newProblemtab.setName(tabnames[i]);
			newProblemtab.setDescript(tabdescripts[i]);
			String orderby = "";
			for (int j = 0; j < orderbySize && !"".equals(taborderby[i * 3 + j]); j++) {
				if (j == 0 || "".equals(orderby)) {
					orderby += taborderby[i * 3 + j];
				} else {
					orderby += ", " + taborderby[i * 3 + j];
				}
			}
			newProblemtab.setOrderby(orderby);
			NEW_tabs.add(newProblemtab);
			i++;
		}

		ArrayList<Problemtab> problemtabs = ApplicationScope.getAppConfig().getProblemtabs();
		for (Problemtab ORIG_tab : problemtabs) {
			if (!contains(NEW_tabs, ORIG_tab.getId())) {
				new ProblemDAO().updateProblemTab(ORIG_tab, NEW_tabs.get(0));
			}
		}

		return NEW_tabs;

	}

	private boolean contains(ArrayList<Problemtab> problemtabs, String tabid) {
		for (Problemtab problemtab : problemtabs) {
			if (problemtab.getId().equals(tabid)) {
				return true;
			}
		}
		return false;
	}

}
