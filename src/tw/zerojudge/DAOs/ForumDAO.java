package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Tables.Article;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.OnlineUser;
import tw.zerojudge.Tables.Article.HIDDEN;

public class ForumDAO extends SuperDAO<Article> {
	private int lastpage = 0;
	private int PAGESIZE = ApplicationScope.getAppConfig().getPageSize();

	public ForumDAO() {
	}

	/**
	 * 取得 lastpage
	 * 
	 * @return
	 */
	public int getLastPage() {
		return this.lastpage;
	}

	/**
	 * 根據原始 SQL 來取得 lastpage
	 * 
	 * @param SQL
	 */
	private void setLastPage(String SQL) {
		int count;
		count = this.executeCount(SQL);
		if (count % PAGESIZE == 0) {
			this.lastpage = count / PAGESIZE;
		} else {
			this.lastpage = count / PAGESIZE + 1;
		}
	}

	public void increaseClicknum(Article article) {
		article.setClicknum(article.getClicknum() + 1);
		this.update(article);
	}

	/**
	 * 列出討論區中的 沒有依附題目的文章
	 * 
	 * @return
	 */
	public ArrayList<Article> getArticlesWithoutProblem(OnlineUser onlineUser, int pagenum) {
		String sql = "SELECT * FROM forum WHERE problemid='' AND " + "hidden!='" + Article.HIDDEN.hide.name()
				+ "' ORDER BY id DESC LIMIT " + (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		if (onlineUser != null && onlineUser.getIsDEBUGGER()) {
			sql = "SELECT * FROM forum WHERE problemid='' ORDER BY " + "id DESC LIMIT " + (pagenum - 1) * PAGESIZE + ","
					+ PAGESIZE;
		}
		this.setLastPage(sql);
		return executeQuery(sql, Article.class);
	}

	/**
	 * 列出討論區中的所有公開討論文章。
	 * 
	 * @return
	 */
	public ArrayList<Article> getArticles1(OnlineUser onlineUser, int pagenum) {
		String sql = "SELECT * FROM forum WHERE hidden!='" + Article.HIDDEN.hide.name() + "' ORDER BY id DESC LIMIT "
				+ (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		if (onlineUser != null && onlineUser.getIsDEBUGGER()) {
			sql = "SELECT * FROM forum ORDER BY id DESC LIMIT " + (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		}
		this.setLastPage(sql);
		return executeQuery(sql, Article.class);
	}

	/**
	 * 列出討論區中的的“解題報告”
	 * 
	 * @return
	 */
	public ArrayList<Article> getProblemReports(boolean isDEBUGGER, int pagenum) {
		String sql = "SELECT * FROM forum WHERE articletype='" + Article.TYPE.problemreport.name() + "'";
		if (!isDEBUGGER) {
			sql += " AND hidden!='" + Article.HIDDEN.hide.name() + "' ";
		}
		sql += " ORDER BY id DESC LIMIT " + (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		this.setLastPage(sql);
		return executeQuery(sql, Article.class);
	}

	/**
	 * 讀取標示為 "announcement" 的討論串
	 * 
	 * @return
	 */
	public ArrayList<Article> getAnnouncements() {
		String sql = "SELECT * FROM forum WHERE articletype='" + Article.TYPE.announcement.name() + "' "
				+ "AND hidden='" + Article.HIDDEN.open.name() + "' ORDER BY id DESC";
		return executeQuery(sql, Article.class);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Article> getArticlesByMarked() {
		String sql = "SELECT * FROM forum WHERE articletype='" + Article.TYPE.marked.name() + "' AND hidden!='"
				+ Article.HIDDEN.hide.name() + "' ORDER BY id DESC";
		return executeQuery(sql, Article.class);
	}

	/**
	 * 取得依附題目的所有討論
	 * 
	 * @return
	 */
	public ArrayList<Article> getArticlesWithProblem(OnlineUser onlineUser, int pagenum) {
		String sql = "SELECT * FROM forum WHERE problemid!='' AND " + "hidden!='" + Article.HIDDEN.hide.name()
				+ "' ORDER BY id DESC LIMIT " + (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		if (onlineUser != null && onlineUser.getIsDEBUGGER()) {
			sql = "SELECT * FROM forum WHERE problemid!='' " + "ORDER BY id DESC LIMIT " + (pagenum - 1) * PAGESIZE
					+ "," + PAGESIZE;
		}
		this.setLastPage(sql);
		return executeQuery(sql, Article.class);
	}

	/**
	 * 取得某個題目的討論
	 * 
	 * @param problemid
	 * @return
	 */
	public ArrayList<Article> getArticlesByProblemid(OnlineUser onlineUser, Problemid problemid, int pagenum) {
		String sql = "SELECT * FROM forum WHERE problemid='" + problemid + "' AND hidden!='"
				+ Article.HIDDEN.hide.name() + "' ORDER BY id DESC";
		if (onlineUser != null && onlineUser.getIsDEBUGGER()) {
			sql = "SELECT * FROM forum WHERE problemid='" + problemid + "' ORDER BY id DESC";
		}
		sql += " LIMIT " + (pagenum - 1) * PAGESIZE + "," + PAGESIZE;
		return executeQuery(sql, Article.class);
	}

	/**
	 * 解題報告
	 * 
	 * @param problemid
	 * @return
	 */
	public ArrayList<Article> getArticlesByProblemReport(Problemid problemid, int length) {
		String sql = "SELECT * FROM forum WHERE problemid='" + problemid + "'" + " AND articletype='"
				+ Article.TYPE.problemreport.name() + "' AND hidden!='" + Article.HIDDEN.hide.name() + "' "
				+ "ORDER BY id DESC LIMIT 0," + length;
		return executeQuery(sql, Article.class);
	}

	public synchronized int insert(Article article) {
		String sql = "INSERT INTO forum(userid, account, reply, problemid, "
				+ "subject, articletype, content, ipfrom, timestamp, hidden)" + " VALUES(?,?,?,?,?,?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, article.getUserid());
			pstmt.setString(2, article.getAccount());
			pstmt.setInt(3, article.getReply());
			pstmt.setString(4, article.getProblemid().toString());
			pstmt.setString(5, article.getSubject());
			pstmt.setString(6, article.getArticletype().name());
			pstmt.setString(7, article.getContent());
			pstmt.setString(8, article.getIpfrom().toString());
			pstmt.setTimestamp(9, article.getTimestamp());
			pstmt.setString(10, article.getHidden().name());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			return 0;
		}
		return id;
	}

	public synchronized int update(Article article) {
		String sql = "UPDATE forum SET userid=?, account=?, reply=?, problemid=?, subject=?, "
				+ "articletype=?, content=?, ipfrom=?, timestamp=?, hidden=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setInt(1, article.getUserid());
			pstmt.setString(2, article.getAccount());
			pstmt.setInt(3, article.getReply());
			pstmt.setString(4, article.getProblemid().toString());
			pstmt.setString(5, article.getSubject());
			pstmt.setString(6, article.getArticletype().name());
			pstmt.setString(7, article.getContent());
			pstmt.setString(8, article.getIpfrom().toString());
			pstmt.setTimestamp(9, article.getTimestamp());
			pstmt.setString(10, article.getHidden().name());
			pstmt.setInt(11, article.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return result;
	}

	public synchronized Integer insertReply_PSTMT(Article article) {
		String sql = "INSERT INTO forum(userid, account, reply, problemid, "
				+ "subject, articletype, content, ipfrom, timestamp, " + "hidden) VALUES (?,?,?,?,?,?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, article.getUserid());
			pstmt.setString(2, article.getAccount());
			pstmt.setInt(3, article.getReply());
			pstmt.setString(4, article.getProblemid().toString());
			pstmt.setString(5, article.getSubject());
			pstmt.setString(6, article.getArticletype().name());
			pstmt.setString(7, article.getContent());
			pstmt.setString(8, article.getIpfrom().toString());
			pstmt.setTimestamp(9, article.getTimestamp());
			pstmt.setString(10, article.getHidden().name());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
			return 0;
		}
		return id;
	}

	/**
	 * 取得 某個 postid 以及他的所有 reply
	 * 
	 * @param postid
	 * @param reply
	 * @return
	 */
	public ArrayList<Article> getArticles(Integer postid, Integer reply) {
		this.execute("UPDATE forum SET clicknum=clicknum+1 WHERE id=" + postid);
		int id = reply == 0 ? postid : reply;
		String sql = "SELECT * FROM forum WHERE hidden!='" + Article.HIDDEN.hide.name() + "' AND (id=" + id
				+ " OR reply=" + id + ")";
		//
		return this.executeQuery(sql, Article.class);
	}

	public Article getArticleById(int id) {
		String sql = "SELECT * FROM forum WHERE id=" + id;
		for (Article article : this.executeQuery(sql, Article.class)) {
			return article;
		}
		return new Article();
	}

	/**
	 * @deprecated
	 * @param hidden
	 */
	public void updateHidden(HIDDEN hidden) {
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

}
