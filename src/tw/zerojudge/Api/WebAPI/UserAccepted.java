package tw.zerojudge.Api.WebAPI;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Api.WebAPI.User.V1.UserAcceptedJson;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.User;

/**
 * @author jiangsir
 */

@WebServlet(urlPatterns = { "/User/V1.0/Accepted" })
public class UserAccepted extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserAccepted() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String account = request.getParameter("account");
		User user = new UserService().getUserByAccount(account);
		UserAcceptedJson solvedjson = new UserAcceptedJson(user);

		response.getWriter().append(mapper.writeValueAsString(solvedjson));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
