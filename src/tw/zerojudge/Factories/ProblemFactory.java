package tw.zerojudge.Factories;

import java.util.TreeSet;
import javax.servlet.http.HttpSession;
//import tw.zerojudge.JsonObjects._ProblemBean;
import tw.zerojudge.Tables.Problem;

public class ProblemFactory {

	//






	public static Problem getNullproblem() {
		return new Problem();
	}




	/**
	 * 以 TreeSet 放置所有 problems 所有存在的 reference
	 * 
	 * @return
	 */
	public static TreeSet<String> getSuggestReferences(HttpSession session,
			String servletPath) {
		//
		TreeSet<String> references = new TreeSet<String>();

		return references;
	}


}
