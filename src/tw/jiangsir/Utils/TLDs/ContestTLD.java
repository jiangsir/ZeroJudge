package tw.jiangsir.Utils.TLDs;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.Api.ContestApiServlet;
import tw.zerojudge.DAOs.VClassStudentDAO;
import tw.zerojudge.Factories.UserFactory;
import tw.zerojudge.Servlets.ContestRankingServlet;
import tw.zerojudge.Servlets.ContestSubmissionsServlet;
import tw.zerojudge.Servlets.DownloadServlet;
import tw.zerojudge.Servlets.EditContestsServlet;
import tw.zerojudge.Servlets.RebuiltContestServlet;
import tw.zerojudge.Servlets.UpdateContestServlet;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Message;
import tw.zerojudge.Tables.OnlineUser;

public class ContestTLD {

	public static boolean isVisible_FinishContest(HttpServletRequest request) {
		try {
			new ContestApiServlet().new FinishContest().AccessFilter(request);
			return true;
		} catch (AccessException e) {
			return false;
		}
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


	private static boolean isVisible_editContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}
		try {
			new UpdateContestServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean isAccessible_ContestSubmissions(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new ContestSubmissionsServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
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

	/**
	 * 移動到 contest
	 * 
	 * @param onlineUser
	 * @param contest
	 * @return
	 */
	private static boolean isVisible_rebuiltContest(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new RebuiltContestServlet().AccessFilter(onlineUser, contest);
			return true;
		} catch (AccessException e) {
			return false;
		}
	}

	public static boolean isVisible_DownloadCSV(OnlineUser onlineUser, Contest contest) {
		if (onlineUser == null) {
			onlineUser = UserFactory.getNullOnlineUser();
		}

		try {
			new UpdateContestServlet().AccessFilter(onlineUser, contest);
			return true;
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

	//
	//
}
