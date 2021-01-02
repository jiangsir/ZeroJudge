package tw.jiangsir.Utils.Servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet implementation class ZJServlet
 */
public abstract class ZJServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	abstract public void AccessFilter(HttpServletRequest request);

}
