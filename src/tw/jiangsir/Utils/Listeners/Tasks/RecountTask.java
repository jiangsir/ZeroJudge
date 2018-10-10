package tw.jiangsir.Utils.Listeners.Tasks;

import java.sql.Timestamp;
import java.util.TimerTask;
import java.util.logging.Logger;

import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.DAOs.ProblemService;

public class RecountTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run() {
		try {
			logger.info(this.getClass().getSimpleName() + " Running.... @"
					+ new Timestamp(System.currentTimeMillis()));

			ProblemService problemService = new ProblemService();
			problemService.recountProblemidAfter_LastSolutionid();

			new LogDAO().reduce(100000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
