package tw.zerojudge.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import tw.jiangsir.Utils.Annotations.RoleSetting;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Interfaces.IAccessFilter;
import tw.jiangsir.Utils.TLDs.ContestTLD;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.VClassDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Tables.*;

@WebServlet(urlPatterns = { "/ShowContest" })
@RoleSetting(allowHigherThen = User.ROLE.USER)
public class ShowContestServlet extends HttpServlet implements IAccessFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1367744237230556382L;

	@Override
	public void AccessFilter(HttpServletRequest request) throws AccessException {
		HttpSession session = request.getSession(false);
		OnlineUser onlineUser = UserFactory.getOnlineUser(session);
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));
		this.AccessFilter(onlineUser, contest);
	}

	/**
	 * 
	 * @param onlineUser
	 * @param contest
	 * @return
	 * @throws AccessException
	 */
	public void AccessFilter(OnlineUser onlineUser, Contest contest) throws AccessException {
		if (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) {
			return;
		}

		if (contest.getVclassid().intValue() > 0) {
			if (new VClassDAO().getVClassById(contest.getVclassid())
					.getStudent(onlineUser.getId()) == null) {
				throw new AccessException(
						"您(" + onlineUser + ")可能並非這個隨堂測驗(" + contest.getTitle() + ")的參與者");
			} else {
				return;
			}
		} else {
//			if (ContestTLD.isInOtherContest(onlineUser, contest)) {
//				throw new AccessException("您已參加其他競賽，請先交卷再參加本競賽。",
//						"您(" + onlineUser + ")已經參加其它比賽中，無法再參加這個比賽，請退出原競賽("
//								+ onlineUser.getJoinedContest() + ")再參加本競賽(" + contest + ")。",
//						"原競賽: " + onlineUser.getJoinedContest() + "\n" + "本競賽: " + contest + "\n",
//						null);
//			}
		}
		if (contest.getIsPausing()) {
			throw new AccessException("競賽已暫停，無法瀏覽！");
		}

		if (contest.getIsSuspending()) {
			throw new AccessException("競賽尚未開始，無法瀏覽！");
		}
		if (contest.getIsStopped() && contest.getVisible() == Contest.VISIBLE.nondetail) {
			throw new AccessException("競賽已結束，且設定為不開放！");
		}
		if (contest.getVisible() == Contest.VISIBLE.hide
				|| (contest.getIsStopped() && !contest.isCheckedConfig(Contest.CONFIG.Visible))) {
			throw new AccessException("競賽未開放瀏覽！");
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long starttime = System.currentTimeMillis();
		Contest contest = new ContestService().getContestById(request.getParameter("contestid"));

		request.setAttribute("contest", contest);
		request.setAttribute("TimeUsage", System.currentTimeMillis() - starttime);
		request.getRequestDispatcher("ShowContest.jsp").forward(request, response);
	}

}
