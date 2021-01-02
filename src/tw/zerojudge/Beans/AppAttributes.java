package tw.zerojudge.Beans;

import java.util.Date;
import java.util.Hashtable;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Annotations.Attribute;
import tw.zerojudge.Tables.Contest;
import tw.zerojudge.Tables.Solution;

public class AppAttributes {
	@Attribute(name = "lastSolutionid")
	private Integer lastSolutionid = 0;
	@Attribute(name = "lastContextRestart")
	private Date lastContextRestart = new Date();
	@Attribute(name = "HashSessions")
	private Hashtable<String, HttpSession> HashSessions = null;
	@Attribute(name = "HashContests")
	private Hashtable<Integer, Contest> HashContests = null;
	@Attribute(name = "HashSolutions")
	private Hashtable<Integer, Solution> HashSolutions = null;


	public AppAttributes() {
	}

	//

	public int getLastSolutionid() {
		return lastSolutionid;
	}

	public void setLastSolutionid(Integer lastSolutionid) {
		this.lastSolutionid = lastSolutionid;
	}

	//

	public Date getLastContextRestart() {
		return lastContextRestart;
	}

	public void setLastContextRestart(Date lastContextRestart) {
		this.lastContextRestart = lastContextRestart;
	}

	//

	//

	public Hashtable<String, HttpSession> getHashSessions() {
		return HashSessions;
	}

	public void setHashSessions(Hashtable<String, HttpSession> hashSessions) {
		HashSessions = hashSessions;
	}

	//

	//

	public Hashtable<Integer, Contest> getHashContests() {
		return HashContests;
	}

	public void setHashContests(Hashtable<Integer, Contest> hashContests) {
		HashContests = hashContests;
	}

	//

	//

	public Hashtable<Integer, Solution> getHashSolutions() {
		return HashSolutions;
	}

	public void setHashSolutions(Hashtable<Integer, Solution> hashSolutions) {
		HashSolutions = hashSolutions;
	}


	//


	//
	//

}
