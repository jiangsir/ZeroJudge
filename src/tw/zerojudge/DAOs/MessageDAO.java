package tw.zerojudge.DAOs;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Message;

public class MessageDAO {

	public final String FILENAME_MESSAGE = "Message.jsp";

	public MessageDAO() {
	}

	public void dispatcher(HttpServletRequest request,
			HttpServletResponse response, Message message)
			throws ServletException, IOException {
		request.setAttribute("message", message);
		request.getRequestDispatcher(FILENAME_MESSAGE).forward(request,
				response);
	}

	public void dispatcher(HttpServletRequest request,
			HttpServletResponse response, Log log) throws ServletException,
			IOException {
		request.setAttribute("log", log);
		request.getRequestDispatcher(FILENAME_MESSAGE).forward(request,
				response);
	}

	/**
	 * 遇到 AccessException 自動加入 Log 。不需要自行處理。
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @throws ServletException
	 * @throws IOException
	 */
	public void dispatcher(HttpServletRequest request,
			HttpServletResponse response, AccessException e)
			throws ServletException, IOException {
		Log log = new Log(request, e);
		log.setStacktrace(e.getLocalizedMessage() + ", Method: "
				+ request.getMethod() + "\n" + log.getStacktrace());
		new LogDAO().insert(log);
		request.setAttribute("log", log);
		request.getRequestDispatcher(FILENAME_MESSAGE).forward(request,
				response);
	}

	/**
	 * 遇到 DataException。
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @throws ServletException
	 * @throws IOException
	 */
	public void dispatcher(HttpServletRequest request,
			HttpServletResponse response, DataException e)
			throws ServletException, IOException {
		Log log = new Log(request, e);
		log.setStacktrace(e.getLocalizedMessage() + ", Method: "
				+ request.getMethod() + "\n" + log.getStacktrace());
		new LogDAO().insert(log);
		request.setAttribute("log", log);
		request.getRequestDispatcher(FILENAME_MESSAGE).forward(request,
				response);
	}
}
