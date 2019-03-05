package tw.jiangsir.Utils.TLDs;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.Api.ContestApiServlet;
import tw.zerojudge.DAOs.ContestService;
import tw.zerojudge.Servlets.*;
import tw.zerojudge.Tables.Contestant;
import tw.zerojudge.Tables.OnlineUser;

public class ContestantTLD {
	public static boolean isVisible_KickUser(OnlineUser onlineUser, Contestant contestant) {
		try {
			new ContestApiServlet().new KickedUser().AccessFilter(onlineUser,
					new ContestService().getContestById(contestant.getContestid()),
					contestant.getUserid());
			return true;
		} catch (AccessException e) {
			return false;
		}
	}
}
