package tw.zerojudge.Servlets.Ajax;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Utils.Utils;

@WebServlet(urlPatterns = { "/Translate.ajax" })
public class TranslateAjax extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		String data = request.getParameter("data");
		try {
			response.getWriter().print(
					new Utils().translate(data, new Locale(from), new Locale(to)));
		} catch (Exception e) {
			Log log = new Log(request, e);
			log.setMessage(from + "轉成 " + to + "出錯！" + log.getMessage());
			log.setStacktrace(data);
			new LogDAO().insert(log);
			response.getWriter().print(data);
		}
		return;
	}
}
