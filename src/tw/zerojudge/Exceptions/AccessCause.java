package tw.zerojudge.Exceptions;

import tw.zerojudge.Judges.ServerOutput;
import tw.zerojudge.Tables.OnlineUser;

/**
 * @author jiangsir
 * 
 */
public class AccessCause extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static enum TYPE {
		ALERT, INFORMATION, WARNING, ERROR, SEVERE;
	}

	private TYPE type = TYPE.WARNING;
	private OnlineUser onlineUser;
	private ServerOutput.REASON resource_message = ServerOutput.REASON.SYSTEMERROR;
	private String text_message = "";
	private String debug_message = "";

	public AccessCause(TYPE type, OnlineUser onlineUser, String debug) {
		this.setType(type);
		this.setOnlineUser(onlineUser);
		this.setDebug_message(debug);
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	public OnlineUser getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(OnlineUser onlineUser) {
		this.onlineUser = onlineUser;
	}

	public ServerOutput.REASON getResource_message() {
		return resource_message;
	}

	public void setResource_message(ServerOutput.REASON resource_message) {
		this.resource_message = resource_message;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getText_message() {
		return text_message;
	}

	public void setText_message(String textMessage) {
		text_message = textMessage;
	}

	public String getDebug_message() {
		return debug_message;
	}

	public void setDebug_message(String debugMessage) {
		debug_message = debugMessage;
	}

}
