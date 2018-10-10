package tw.jiangsir.Utils.TLDs;

import java.util.Locale;
import java.util.ResourceBundle;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.ContestRankingServlet;
import tw.zerojudge.Servlets.ContestSubmissionsServlet;
import tw.zerojudge.Servlets.DownloadServlet;
import tw.zerojudge.Servlets.EditContestsServlet;
import tw.zerojudge.Servlets.RebuiltContestServlet;
import tw.zerojudge.Servlets.Utils.LeaveContestServlet;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Message;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Contest.Pausepoint;
import tw.zerojudge.Utils.Utils;

public class ContestTLD {

	public static boolean isCanShowContestNote(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) {
			return false;
		}
		if (onlineUser.getJoinedcontestid().intValue() != contest.getId() && !contest.getIsStopped()) {
			return true;
		}
		return false;
	}

	public static boolean isCanShowContestProblems(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) {
			return true;
		}
		if (contest.getIsRunning() && isJoinedinThisContest(onlineUser, contest)) {
			return true;
		}
		if (contest.getIsStopped()) {
			return true;
		}
		return false;
	}

	public static boolean isJoinedinThisContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		return onlineUser.getJoinedcontestid().intValue() == contest.getId();
	}

	public static boolean isVisible_FinishContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if ((contest.getIsRunning() || contest.getIsStarting())
				&& (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)
						|| onlineUser.getJoinedcontestid().intValue() == contest.getId().intValue())) {
			return true;
		}

		return false;
	}

	public static boolean isVisible_UnfinishContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if ((contest.getIsRunning() || contest.getIsStarting())
				&& (onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)
						|| onlineUser.getJoinedcontestid().intValue() == contest.getId().intValue())) {
			return true;
		}
		return false;
	}

	public static boolean isVisible_ContestResult(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (contest.getIsOwner(onlineUser) || onlineUser.getIsDEBUGGER()
				|| contest.isCheckedConfig(Contest.CONFIG.ShowResult)) {
			return true;
		}
		return false;
	}

	public static boolean isVisible_startContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		if (onlineUser.getIsDEBUGGER() && contest.getIsStopped()) {
			return true;
		}
		if ((onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser))
				&& (contest.getIsStarting() || contest.getIsSuspending())) {
			return true;
		}
		return false;
	}

	public static boolean isVisible_pauseContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (onlineUser.getIsDEBUGGER() && contest.getIsStopped() && contest.getProblemids().size() != 0) {
			return true;
		}
		if ((onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) && (contest.getIsRunning())
				&& contest.getProblemids().size() != 0) {
			return true;
		}
		return false;
	}

	public static boolean isVisible_resumeContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (onlineUser.getIsDEBUGGER() && contest.getIsStopped() && contest.getProblemids().size() != 0) {
			return true;
		}
		if ((onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser)) && (contest.getIsPausing())
				&& contest.getProblemids().size() != 0) {
			return true;
		}
		return false;
	}

	public static boolean isVisible_stopContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if ((onlineUser.getIsDEBUGGER() || contest.getIsOwner(onlineUser))
				&& (contest.getIsRunning() || contest.getIsPausing())) {
			return true;
		}
		return false;
	}

	public static boolean isVisible_editContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (onlineUser.getIsDEBUGGER()) {
			return true;
		}
		if (contest.getIsOwner(onlineUser)) {
			return true;
		}
		return false;
	}

	public static boolean isVisible_EditContestants(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (onlineUser.getIsDEBUGGER()) {
			return true;
		}
		if (contest.getIsOwner(onlineUser)
				&& (contest.getIsRunning() || contest.getIsSuspending() || contest.getIsStarting())) {
			return true;
		}
		return false;
	}


	public static boolean isAccessible_ContestSubmissions(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new ContestSubmissionsServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isAccessible_ContestRanking(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new ContestRankingServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean isAccessible_EditContests(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new EditContestsServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	//
	//

	public static boolean isVisible_LeaveContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new LeaveContestServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean isVisible_rebuiltContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new RebuiltContestServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isVisible_DownloadCSV(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			return DownloadServlet.isAccessible_DownloadCSV(onlineUser, contest);
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean isVisible_DownloadExefile(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			return DownloadServlet.isAccessible_DownloadExefile(onlineUser, contest);
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean getIsStudent(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		if (new VClassStudentDAO().getStudent(onlineUser.getVclassid(), onlineUser.getId()) == null) {
			return false;
		}
		return true;
	}

	public static boolean isInOtherContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser != null && onlineUser.getJoinedcontestid() != 0
				&& onlineUser.getJoinedcontestid() != contest.getId()) {
			return true;
		}
		return false;
	}

	public static String getResource_Conteststatus(Locale locale, Contest contest) {
		if (locale == null) {
			locale = new Locale("zh", "TW");
		}

		if (contest == null) {
			return "";
		}
		ResourceBundle resource = ResourceBundle.getBundle("resource", locale);
		return resource.getString(contest.getConteststatus().toString());
	}

	public static String getResource_Countdown(Locale locale, Contest contest) {
		if (locale == null) {
			locale = new Locale("zh", "TW");
		}

		ResourceBundle resource = ResourceBundle.getBundle("resource", locale);

		long countdown = contest.getCountdown();
		int secs = (int) ((countdown - (countdown % 1000)) / 1000);
		int mins = (secs - (secs % 60)) / 60;
		int hours = (mins - (mins % 60)) / 60;
		StringBuffer s = new StringBuffer(500);
		s.append(resource.getString(Message.Resource_Contest_Countdown) + ": ");
		if (hours != 0) {
			s.append(hours + " " + resource.getString(Message.Resource_Codelock_Hour) + " ");
		}
		s.append(mins % 60 + " " + resource.getString(Message.Resource_Codelock_Minute) + " " + secs % 60 + " "
				+ resource.getString(Message.Resource_Codelock_Second));
		return s.toString();
	}

	public static boolean isConfigChecked(Contest.CONFIG config) {
		return new Contest().isCheckedConfig(config);
	}

//	/**
//	 * 顯示暫停點
//	 * 
//	 * @param onlineUser
//	 * @param contest
//	 * @return
//	 */
//	public static String getPausepoints(OnlineUser onlineUser, Contest contest) {
//		
//		
//		
//		
//		if (onlineUser == null) {
//			onlineUser = UserFactory.getNullOnlineUser();
//		}
//
//		if (!onlineUser.getIsDEBUGGER()) {
//			return "";
//		}
//
//		StringBuffer html = new StringBuffer(5000);
//		int index = 1;
//		for (Pausepoint pausepoint : contest.getPausepoints()) {
//			int secs = (int) (pausepoint.getPausetime() / 1000);
//			int mins = secs / 60;
//			int hours = mins / 60;
//			html.append("暫停" + index++ + ": " + (hours == 0 ? "" : hours + "H") + (mins == 0 ? "" : mins % 60 + "M")
//					+ secs % 60 + "S" + "@" + new Utils().parseDatetime(pausepoint.getBegintime()) + "<br/>");
//		}
//		return html.toString();
//	}
}
