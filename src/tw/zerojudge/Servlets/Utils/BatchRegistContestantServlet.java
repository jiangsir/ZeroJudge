package tw.zerojudge.Servlets.Utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.ContestantDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.User;

@WebServlet(urlPatterns = { "/BatchRegistContestant" })
@RoleSetting
public class BatchRegistContestantServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("BatchInsertContestants.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String scripts = request.getParameter("scripts");
		String[] lines = scripts.split("\n");
		ContestantDAO contestantDao = new ContestantDAO();
		for (int i = 0; i < lines.length; i++) {
			if (!lines[i].startsWith("#")) {
				String[] datas = lines[i].split(",");
				Contestant newcontestant = new Contestant();
				newcontestant.setContestid(Integer.valueOf(datas[0].trim()));
				Contest contest = new ContestService().getContestById(newcontestant.getContestid());
				String[] aclist = new String[contest.getProblemids().size()];
				Arrays.fill(aclist, "-");
				newcontestant.setAclist(aclist);
				User user = new UserService().getUserByAccount(datas[1].trim());
				newcontestant.setUserid(user.getId());
				newcontestant.setTeamname(datas[2].trim());
				newcontestant.setPassword(datas[3].trim());
				newcontestant.setEmail(datas[4].trim());
				newcontestant.setSchool(datas[5].trim());
				newcontestant.setFinishtime(new Timestamp(contest.getStarttime().getTime() + contest.getTimelimit()));
				contestantDao.insert(newcontestant);
			}
		}
		response.sendRedirect(new SessionScope(session).getPreviousPage());
	}
}
