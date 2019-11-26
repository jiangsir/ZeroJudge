/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.Serializable;
import java.sql.Timestamp;
import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Tools.StringTool;

/**
 * @author jiangsir
 * 
 */
public class IMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8220852002578038352L;
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "sender")
	private String sender = "";
	@Persistent(name = "receiver")
	private String receiver = "";
	@Persistent(name = "subject")
	private String subject = "";
	@Persistent(name = "content")
	private String content = "";
	@Persistent(name = "senttime")
	private Timestamp senttime = new Timestamp(System.currentTimeMillis());

	public static enum SENDER_STATUS {
		open, //
		deleted;//
	}

	@Persistent(name = "sender_status")
	private SENDER_STATUS sender_status = SENDER_STATUS.open;


	public static enum RECEIVER_STATUS {
		unread, //
		readed, //
		deleted;//
	}

	@Persistent(name = "receiver_status")
	private RECEIVER_STATUS receiver_status = RECEIVER_STATUS.unread;

	public IMessage() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content == null) {
			return;
		}
		this.content = StringTool.escapeScriptStyle(content);
	}

	public SENDER_STATUS getSender_status() {
		return sender_status;
	}

	public void setSender_status(SENDER_STATUS sender_status) {
		this.sender_status = sender_status;
	}

	public void setSender_status(String sender_status) {
		if (sender_status == null) {
			return;
		}
		this.setSender_status(SENDER_STATUS.valueOf(sender_status));
	}

	public Timestamp getSenttime() {
		return senttime;
	}

	public void setSenttime(Timestamp senttime) {
		this.senttime = senttime;
	}

	public RECEIVER_STATUS getReceiver_status() {
		return receiver_status;
	}

	public void setReceiver_status(RECEIVER_STATUS receiver_status) {
		this.receiver_status = receiver_status;
	}

	public void setReceiver_status(String receiver_status) {
		if (receiver_status == null) {
			return;
		}
		this.setReceiver_status(RECEIVER_STATUS.valueOf(receiver_status));
	}

	public boolean getIsReceiver_status_readed() {
		return this.getReceiver_status() == RECEIVER_STATUS.readed;
	}

	public boolean getIsReceiver_status_unread() {
		return this.getReceiver_status() == RECEIVER_STATUS.unread;
	}

	public boolean getIsReceiver_status_deleted() {
		return this.getReceiver_status() == RECEIVER_STATUS.deleted;
	}

	/*
	 * 每一行的第一個字母加上 >
	 */
	public String getReplyContent() {
		String ss[] = this.getContent().split("\n");
		StringBuffer sb = new StringBuffer(5000);
		for (String s : ss) {
			sb.append("> " + s);
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.getId() + ": " + this.getSubject();
	}
}
