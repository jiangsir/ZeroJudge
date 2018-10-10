/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.DAOs.ProblemService;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Problemid;

/**
 * @author jiangsir
 * 
 */
public class Article {
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "userid")
	private Integer userid = 0;
	@Persistent(name = "account")
	private String account = "";
	@Persistent(name = "reply")
	private Integer reply = 0;
	@Persistent(name = "problemid")
	private Problemid problemid = new Problem().getProblemid();
	@Persistent(name = "subject")
	private String subject = "";
	public static final int CONFIG_ProblemAuthor = 3;


	public static enum TYPE {
		announcement, 
		marked, 
		problemreport, 
		forum; 
		public boolean isHigherThan(TYPE type) {
			return this.ordinal() <= type.ordinal();
		}

		public boolean isLowerThan(TYPE type) {
			return this.ordinal() > type.ordinal();
		}

		public boolean getIs_problemreport() {
			return this == problemreport;
		}

		public boolean getIs_marked() {
			return this == marked;
		}

		public boolean getIs_announcement() {
			return this == announcement;
		}

		public boolean getIs_forum() {
			return this == forum;
		}

	}

	@Persistent(name = "articletype")
	private TYPE articletype = Article.TYPE.forum;
	@Persistent(name = "content")
	private String content = "";
	@Persistent(name = "ipfrom")
	private List<IpAddress> ipfrom = new ArrayList<IpAddress>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7261428254835822340L;
		{
			new IpAddress("127.0.0.1");
		}
	};
	@Persistent(name = "timestamp")
	private Timestamp timestamp = new Timestamp(new Date().getTime());
	@Persistent(name = "clicknum")
	private Integer clicknum = 0;

	public static enum HIDDEN {
		open, 
		hide, 
		block;
		public boolean isHigherEqualThan(HIDDEN hidden) {
			return this.ordinal() <= hidden.ordinal();
		}

		public boolean isLowerThan(HIDDEN hidden) {
			return this.ordinal() > hidden.ordinal();
		}

		public boolean getIs_open() {
			return this == open;
		}

		public boolean getIs_hide() {
			return this == hide;
		}

		public boolean getIs_block() {
			return this == block;
		}

	}

	@Persistent(name = "hidden")
	private HIDDEN hidden = Article.HIDDEN.open;


	public Article() {
	}

	public static Integer parseArticleId(String articleid) {
		if (articleid != null && articleid.matches("[0-9]+")) {
			return Integer.valueOf(articleid);
		}
		return new Article().getId();
	}

	public static Integer parseReply(String reply) {
		if (reply != null && reply.matches("[0-9]+")) {
			return Integer.valueOf(reply);
		}
		return new Article().getReply();
	}







	public TYPE getArticletype() {
		return articletype;
	}

	public void setArticletype(TYPE articletype) {
		this.articletype = articletype;
	}

	public void setArticletype(String articletype) {
		if (articletype == null) {
			return;
		}
		this.setArticletype(TYPE.valueOf(articletype));
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public HIDDEN getHidden() {
		return hidden;
	}

	public void setHidden(HIDDEN hidden) {
		this.hidden = hidden;
	}

	public void setHidden(String hidden) {
		if (hidden == null) {
			return;
		}
		this.setHidden(HIDDEN.valueOf(hidden));
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		if (account == null || "".equals(account)) {
			throw new DataException("錯誤！沒有作者資訊。");
		}
		this.account = account;
	}

	public Integer getClicknum() {
		return clicknum;
	}

	public void setClicknum(Integer clicknum) {
		this.clicknum = clicknum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content == null || "".equals(content)) {
			throw new DataException("錯誤！文章沒有內容！");
		}
		this.content = StringTool.escapeScriptStyle(content);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) throws DataException {
		if (subject == null || "".equals(subject)) {
			throw new DataException("錯誤！文章沒有標題！");
		}
		this.subject = subject;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<IpAddress> getIpfrom() {
		return ipfrom;
	}

	public void setIpfrom(List<IpAddress> ipfrom) {
		this.ipfrom = ipfrom;
	}

	public void setIpfrom(String ipfrom) {
		if (ipfrom == null || "".equals(ipfrom.trim())) {
			return;
		}
		this.setIpfrom(StringTool.String2IpAddressList(ipfrom));
	}

	public boolean getHasProblemid() {
		return !(problemid == null || "".equals(problemid));
	}

	public Problem getProblem() {
		return new ProblemService().getProblemByProblemid(problemid);
	}

	public Problemid getProblemid() {
		return problemid;
	}

	public void setProblemid(String problemid) {
		if (problemid == null || "".equals(problemid)) {
			this.setProblemid(new Problem().getProblemid());
		} else {
			this.setProblemid(new Problemid(problemid));
		}
	}

	public void setProblemid(Problemid problemid) {
		this.problemid = problemid;
	}

	public Integer getReply() {
		return reply;
	}

	public void setReply(Integer reply) {
		this.reply = reply;
	}

	public void setReply(String reply) {
		if (reply == null || !reply.matches("[0-9]+")) {
			return;
		}
		this.reply = Integer.parseInt(reply);
	}

	public Integer getUserid() {
		return userid;
	}

	public User getUser() {
		return new UserService().getUserById(userid);
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}


	public boolean getIsHidden() {
		return this.getHidden() == HIDDEN.hide;
	}

	/**
	 * 取得附加在 privilege 裡面 xxxx.do#5 #字號後面的 config 值
	 * 
	 * @return
	 */
	public int getConfig() {
		int config = 0;

		return config;
	}

}
