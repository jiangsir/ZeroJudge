package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import tw.zerojudge.DAOs.*;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ShowImage" })
public class ShowImageServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void doGET_ShowProblemImage(HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream out = null;

		int id = Integer.parseInt(request.getParameter("id"));
		Problemimage image = new ProblemimageDAO().getImage(id);
		try {
			out = response.getOutputStream();
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType(image.getFiletype()); 
			out.write(new ProblemimageDAO().getFile(id));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void doGET_ShowUserImage(HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream out = null;

		int userid = Integer.parseInt(request.getParameter("userid"));
		User user = new UserDAO().getUserById(userid);
		try {
			out = response.getOutputStream();
			response.setContentType(user.getPicturetype()); 
			out.write(user.getPictureblob());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null && id.matches("[0-9]+")) {
			doGET_ShowProblemImage(request, response);
		} else if (request.getParameter("userid") != null) {
			doGET_ShowUserImage(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
