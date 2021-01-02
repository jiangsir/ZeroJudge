package tw.jiangsir.Utils.TLDs;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.SubmitCodeServlet;
import tw.zerojudge.Servlets.TestjudgeServlet;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Servlets.VerifyingProblemsServlet;
import tw.zerojudge.Servlets.Ajax.ReJudgeAjaxServlet;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Problem;

public class ProblemTLD {

	public static String getHtmlVerify(OnlineUser onlineUser, Problem problem) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			if (new VerifyingProblemsServlet().isAccessible(onlineUser, problem)) {
				if (Problem.DISPLAY.verifying == problem.getDisplay()) {
					return "<span id=\"verifying\" style=\"cursor:pointer; text-decoration:underline;\">已提交</span>";
				} else if (Problem.DISPLAY.open == problem.getDisplay()) {
					return "<span id=\"open\" style=\"cursor:pointer; text-decoration:underline\">已公開</span>";
				}
			}

		} catch (AccessException e) {
			if (Problem.DISPLAY.verifying == problem.getDisplay()) {
				return "<span>已提交</span>";
			} else if (Problem.DISPLAY.open == problem.getDisplay()) {
				return "<span>公開</span>";
			}
		}
		return "---";
	}

	public static boolean canSubmitCode(OnlineUser onlineUser, Problem problem) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		return new SubmitCodeServlet().isAccessible(onlineUser, problem);
	}

	public static boolean isCanRejudgeProblem(OnlineUser onlineUser, Problem problem) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		try {
			return onlineUser.isProblemRejudgable(problem);
		} catch (AccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isCanDownloadIndata(OnlineUser onlineUser, Problem problem) {
		try {
			new UpdateProblemServlet().AccessFilter(onlineUser, problem);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean isCanDownloadOutdata(OnlineUser onlineUser, Problem problem) {
		try {
			new UpdateProblemServlet().AccessFilter(onlineUser, problem);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean canShowDetails(OnlineUser onlineUser, Problem problem) {
		try {
			new UpdateProblemServlet().AccessFilter(onlineUser, problem);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public static boolean canShowCode(OnlineUser onlineUser, Problem problem) {
		try {
			new UpdateProblemServlet().AccessFilter(onlineUser, problem);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public static void xxxx() {
	}

}
