package tw.zerojudge.DAOs;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Tables.IMessage;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.User;
import tw.zerojudge.Utils.Mailer;

public class IMessageDAO extends SuperDAO<IMessage> {

	public IMessageDAO() {
	}

	public void sendIMessage(IMessage imessage, String hostURI) {
		int id = this.insert(imessage);
		User userto = new UserService().getUserByAccount(imessage.getReceiver());
		User userfrom = new UserService().getUserByAccount(imessage.getSender());
		if (id > 0 && userto.getId().intValue() != 0 && userfrom.getId().intValue() != 0) {
			String content = "請記得回覆站內信！\n\n";
			content += "主旨：" + imessage.getSubject() + "\n";
			content += "內容：\n" + imessage.getContent() + "\n";
			content += "\n\n\n\n請注意！這是系統通知信，「請勿」直接回信！請到「站內訊息收件夾」進行回覆！！";
			Thread mailer = new Thread(
					new Mailer(userto, "站內訊息通知：" + userfrom.getAccount() + "(" + userfrom.getUsername() + ") 發送站內訊息給您。",
							content, hostURI));
			mailer.start();
		}
	}

	/**
	 * 取得未讀過的 即時訊息
	 * 
	 * @param account
	 * @return
	 */
	public ArrayList<IMessage> getUnreadIMessage(OnlineUser onlineUser, int page) {
		String SQL = "SELECT * FROM imessages WHERE receiver='" + onlineUser.getAccount() + "' AND receiver_status='"
				+ IMessage.RECEIVER_STATUS.unread.name() + "' AND receiver_status!='"
				+ IMessage.RECEIVER_STATUS.deleted.name() + "' AND sender_status='" + IMessage.SENDER_STATUS.open
				+ "' ORDER BY senttime DESC LIMIT " + (page - 1) * ApplicationScope.getAppConfig().getPageSize() + ","
				+ ApplicationScope.getAppConfig().getPageSize();
		return executeQuery(SQL, IMessage.class);
	}

	/**
	 * 取得所有 已讀/未讀 的訊息, 除了已經刪除的以外
	 * 
	 * @param account
	 * @return
	 */
	public ArrayList<IMessage> getInboxIMessages(OnlineUser onlineUser, int page) {
		String SQL = "SELECT * FROM imessages WHERE receiver='" + onlineUser.getAccount() + "' AND receiver_status!='"
				+ IMessage.RECEIVER_STATUS.deleted.name() + "' ORDER BY senttime DESC LIMIT "
				+ (page - 1) * ApplicationScope.getAppConfig().getPageSize() + ","
				+ ApplicationScope.getAppConfig().getPageSize();
		return executeQuery(SQL, IMessage.class);
	}

	/**
	 * 取得寄件備份
	 * 
	 * @param account
	 * @return
	 */
	public ArrayList<IMessage> getSentIMessages(OnlineUser onlineUser, int page) {
		String SQL = "SELECT * FROM imessages WHERE sender='" + onlineUser.getAccount() + "' AND sender_status='"
				+ IMessage.SENDER_STATUS.open + "' ORDER BY senttime DESC LIMIT "
				+ (page - 1) * ApplicationScope.getAppConfig().getPageSize() + ","
				+ ApplicationScope.getAppConfig().getPageSize();
		return executeQuery(SQL, IMessage.class);
	}

	public IMessage getIMessageById(int id) {
		String sql = "SELECT * FROM imessages WHERE id=" + id;
		for (IMessage iMessage : this.executeQuery(sql, IMessage.class)) {
			return iMessage;
		}
		return new IMessage();
	}

	@Override
	public synchronized int insert(IMessage imessage) throws DataException {
		String sql = "INSERT INTO imessages (sender, receiver, subject, content, senttime) VALUES(?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, imessage.getSender());
			pstmt.setString(2, imessage.getReceiver());
			pstmt.setString(3, imessage.getSubject());
			pstmt.setString(4, imessage.getContent());
			pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			id = super.executeInsert(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			return 0;
		}
		return id;
	}

	@Override
	public synchronized int update(IMessage imessage) throws DataException {
		String sql = "UPDATE imessages SET receiver_status=?, sender_status=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setString(1, imessage.getReceiver_status().name());
			pstmt.setString(2, imessage.getSender_status().name());
			pstmt.setInt(3, imessage.getId());
			result = super.executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			return 0;
		}
		return result;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

}
