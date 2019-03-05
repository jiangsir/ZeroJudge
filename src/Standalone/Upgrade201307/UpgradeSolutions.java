package Standalone.Upgrade201307;

import java.io.IOException;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import tw.zerojudge.DAOs.ProblemDAO;
import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.DAOs.SolutionService;
import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Tables.Solution;

public class UpgradeSolutions {

	SolutionDAO solutionDao;
	ProblemDAO problemDao;
	Logger logger = Logger.getLogger(UpgradeSolutions.class.getName());
	ObjectMapper mapper = new ObjectMapper(); 
	int PAGESIZE = 20;

	static enum ACTION {
		updateServerOutputs("更新 serverOutputs 的資料結構。"),
		updateTLE("新版的 serveroutput 使得 TLE 會讀出 0ms，將題目時限寫入 timeusage 裡面"),
		exit("退出。");
		private String value;

		private ACTION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

	public UpgradeSolutions(SolutionDAO solutionDao, ProblemDAO problemDao) {
		this.solutionDao = solutionDao;
		this.problemDao = problemDao;
	}

	/**
	 * 因為，從 2011-09-08 開始評分結果就改用 serveroutputs 的 JSON 資料來儲存，但TLE 的秒數並未寫入
	 * timeusage 欄位，完全從 serveroutput.info 裡面取得。 本程式讀出 info 的秒數，再寫入 timeusage 欄位。
	 * 
	 * @param page
	 *            page==0 代表全部
	 */
	public void updateTLE(int page) {
		ServerOutput.JUDGEMENT[] judgements = new ServerOutput.JUDGEMENT[] { ServerOutput.JUDGEMENT.TLE };
		if (page == 0) {
			int count = solutionDao.countSolutionsByJudgement(judgements);
			page = count / PAGESIZE + (count % PAGESIZE == 0 ? 0 : 1);
		}
		for (int i = 1; i <= page; i++) {
			for (Solution solution : solutionDao.getSolutionsByJudgement(judgements, i)) {
				if (solution.getJudgement() == ServerOutput.JUDGEMENT.TLE) {
					ServerOutput[] serveroutputs = solution.getServeroutputs();
					long timeusage = 0;
					for (ServerOutput serverOutput : serveroutputs) {
						if (serverOutput == null) {
							continue;
						}
						String info = serverOutput.getInfo();
						if (info.endsWith("s")) {
							try {
								timeusage = Long.parseLong(info.substring(0, info.length() - 1)) * 1000;
								serverOutput.setTimeusage(timeusage);
							} catch (NumberFormatException e) {
								e.printStackTrace();
								try {
									timeusage = (long) (Double.parseDouble(info.substring(0, info.length() - 1))
											* 1000);
									serverOutput.setTimeusage(timeusage);
								} catch (NumberFormatException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
					solution.setTimeusage(timeusage);
				}
				new SolutionService().update(solution);
			}
		}
	}

	/**
	 * 20130410<br>
	 * 更新 ServerOutputs 的 json 資料。以往 serverOutputs 對於多測資題目，如果結果為 SE, CE 時，
	 * 只會有第一個 serverOutput, 後面的都會是 null. 長度仍為測資個數。<br>
	 * 本次更新，是要讓只有一個 serverOutput 的多測資結果，縮短為一個。也就是把 null 全部消除。
	 * 
	 * @param page
	 *            page==0 代表全部一起做
	 */
	public void updateServerOutputs(int page) {
		ServerOutput.JUDGEMENT[] judgements = new ServerOutput.JUDGEMENT[] { ServerOutput.JUDGEMENT.SE,
				ServerOutput.JUDGEMENT.CE, ServerOutput.JUDGEMENT.RF };
		if (page == 0) {
			int count = solutionDao.countSolutionsByJudgement(judgements);
			page = count / PAGESIZE + (count % PAGESIZE == 0 ? 0 : 1);
		}
		for (int i = 1; i <= page; i++) {
			for (Solution solution : solutionDao.getSolutionsByJudgement(judgements, i)) {
				if (solution.getServeroutputs().length > 1
						&& problemDao.getProblemByPid(solution.getPid()).getTestfilelength() > 1) {
					try {
						logger.info(mapper.writeValueAsString(solution.getServeroutputs()));
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (ServerOutput serverOutput : solution.getServeroutputs()) {
						solution.setServeroutputs(new ServerOutput[] { serverOutput });
						new SolutionService().update(solution);
						break;
					}
				}
			}
		}
	}
}
