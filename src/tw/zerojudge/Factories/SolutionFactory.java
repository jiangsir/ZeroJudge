package tw.zerojudge.Factories;

import tw.zerojudge.DAOs.SolutionDAO;
import tw.zerojudge.Tables.Solution;

public class SolutionFactory extends SuperFactory<Solution> {






	//






	/**
	 * 取出目前 solutions 總筆數, 首頁統計資訊用
	 */
	public static int getLastSolutionid() {
		String sql = "SELECT id FROM solutions ORDER BY id DESC LIMIT 0,1";
		for (long solutionid : new SolutionDAO().executeQueryId(sql)) {
			return (int) solutionid;
		}
		return new Solution().getId();
	}





}
