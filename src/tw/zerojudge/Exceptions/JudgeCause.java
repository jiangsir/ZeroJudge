package tw.zerojudge.Exceptions;

import tw.zerojudge.Judges.ServerOutput;

/**
 * @author jiangsir
 * 
 */
public class JudgeCause extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	private String session_account = "";
	private ServerOutput.JUDGEMENT verdict = ServerOutput.JUDGEMENT.SE;
	private String info = "";
	private ServerOutput.REASON reason = ServerOutput.REASON.SYSTEMERROR;
	private String hint = "";

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

	public ServerOutput.JUDGEMENT getVerdict() {
		return verdict;
	}

	public void setVerdict(ServerOutput.JUDGEMENT verdict) {
		this.verdict = verdict;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public ServerOutput.REASON getReason() {
		return reason;
	}

	public void setReason(ServerOutput.REASON reason) {
		this.reason = reason;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

}
