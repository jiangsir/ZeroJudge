package tw.jiangsir.Utils.TLDs;

import javax.servlet.annotation.WebServlet;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.RoleException;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.DAOs.UpfileDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.DiffServlet;
import tw.zerojudge.Servlets.ManualJudgeServlet;
import tw.zerojudge.Servlets.ShowCodeServlet;
import tw.zerojudge.Servlets.ShowDetailsServlet;
import tw.zerojudge.Servlets.Ajax.ReJudgeAjaxServlet;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Solution;
import tw.zerojudge.Tables.Upfile;

public class SolutionTLD {
	/**
	 * 
	 * @return
	 */
	public static boolean isCodelockAccessible(OnlineUser onlineUser, Solution solution) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (onlineUser.isIsGroupGuest() && (onlineUser.getIsDEBUGGER() || getIsOwner(onlineUser, solution)
				|| solution.getContest().getIsOwner(onlineUser))) {
			return true;
		}
		return false;
	}

	public static boolean getIsOwner(OnlineUser onlineUser, Solution solution) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		return onlineUser.getId() == solution.getUserid();
	}

	/**
	 * Judgement 的 HTML 碼
	 * 
	 * @return
	 */
	public static String getHtmlJudgement(OnlineUser onlineUser, Solution solution) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		try {
			new ShowDetailsServlet().AccessFilter(onlineUser, solution);
			return solution.parseOwnerHtmlStatus();
		} catch (AccessException e) {
			return solution.parseGuestHtmlStatus();
		}
	}

	public static String getHtmlShowCode(OnlineUser onlineUser, Solution solution) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		String htmlShowCode;
		try {
			htmlShowCode = "<a href=# title=\"觀看程式碼\">" + solution.getLanguage().getName() + "</a>";
		} catch (RoleException e) {
			htmlShowCode = solution.getLanguage().getName();
		}
		return htmlShowCode;
	}

	public static String getHtmlDiff(OnlineUser onlineUser, Solution solution) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (DiffServlet.isAccessible(onlineUser, solution.getId())) {
			return "<a href=\"." + DiffServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0] + "?solutionid="
					+ solution.getId() + "\"><img src=\"images/diff.png\" border=\"0\" /></a>";
		}
		return "";
	}

	public static boolean isVisible_Diff(OnlineUser onlineUser, Solution solution) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (DiffServlet.isAccessible(onlineUser, solution.getId())) {
			return true;
		}
		return false;
	}


	/**
	 * 取出屬於這個 solution 的 upfile
	 * 
	 * @return
	 */
	public static Upfile getUpfile(Solution solution) {
		return new UpfileDAO().getUpfileBySolutionid(solution.getId());
	}

}
