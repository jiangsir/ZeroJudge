package tw.jiangsir.Utils.Listeners.Tasks;

import java.sql.Timestamp;
import java.util.TimerTask;
import java.util.logging.Logger;

import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Tables.Solution;

public class InitSolutionsTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run() {
		logger.info(this.getClass().getSimpleName() + " Running...." + new Timestamp(System.currentTimeMillis()));
		new SolutionService().insertInitSolutions();
		for (Solution solution : new SolutionDAO().getSolutionsByJudgementWaiting()) {
			solution.doRejudgeByListener();
		}
	}

}
