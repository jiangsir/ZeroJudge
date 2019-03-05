package tw.zerojudge.Servlets;

import java.io.IOException;
import java.net.URL;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.zerojudge.Objects.WebObject;

@WebServlet(urlPatterns = { "/GetWebdata" })
@RoleSetting
public class GetWebdataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String site = request.getParameter("url");
		if (!site.startsWith("http")) {
			site = "http://" + site;
		}
		URL url = new URL(site);
		String[] querystrings = request.getParameterValues("querystring");
		String charset = request.getParameter("charset");
		String method = request.getParameter("method");

		WebObject webobject = new WebObject(url, querystrings, method, charset);
		response.getWriter().print(webobject.getHtml());
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
