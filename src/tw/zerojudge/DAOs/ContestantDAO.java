package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Tables.*;

public class ContestantDAO extends SuperDAO<Contestant> {
	private int lastpage = 0;

	public ContestantDAO() {
	}

	/**
	 * 取得 lastpage
	 * 
	 * @return
	 */
	public int getLastpage() {
		return this.lastpage;
	}

	private void cleanContestants(Contest contest) {
		String[] aclist = new String[contest.getProblemids().size()];
		Arrays.fill(aclist, "-");
		execute("UPDATE contestants SET ac=0, aclist='" + Arrays.toString(aclist)
				+ "', submits=0, penalty=0 WHERE contestid=" + contest.getId());
	}

	/**
	 * 重新計算 contestants 內的 ac,aclist or penalty?
	 * 
	 * @throws AccessException
	 * @throws DataException
	 * 
	 */
	public void rebuiltContestants(Integer contestid) {
		Contest contest = new ContestService().getContestById(contestid);
		this.cleanContestants(contest);
		new ContestDAO().rebuiltContestRanking(contest);
	}

	public synchronized int update(Contestant contestant) {
		String sql = "UPDATE contestants SET contestid=?, userid=?, teamname=?, password=?, "
				+ "email=?, school=?, ipset=?, language=?, ac=?, aclist=?, "
				+ "submits=?, penalty=?, score=?, status=?, finishtime=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setInt(1, contestant.getContestid());
			pstmt.setInt(2, contestant.getUserid());
			pstmt.setString(3, contestant.getTeamname());
			pstmt.setString(4, contestant.getPassword());
			pstmt.setString(5, contestant.getEmail());
			pstmt.setString(6, contestant.getSchool());
			pstmt.setString(7, contestant.getIpset().toString());
			pstmt.setString(8, contestant.getLanguage());
			pstmt.setInt(9, contestant.getAc());
			pstmt.setString(10, Arrays.toString(contestant.getAclist()));
			pstmt.setInt(11, contestant.getSubmits());
			pstmt.setInt(12, contestant.getPenalty());
			pstmt.setInt(13, contestant.getScore());
			pstmt.setString(14, contestant.getStatus().name());
			pstmt.setTimestamp(15, contestant.getFinishtime());
			pstmt.setInt(16, contestant.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return result;

	}

	public synchronized int insert(Contestant contestant) {
		String sql = "INSERT INTO contestants(contestid, userid, teamname, password, email, school, ipset, "
				+ "language, aclist, status, finishtime) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
		int id = 0;
		PreparedStatement pstmt;
		try {
			pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, contestant.getContestid());
			pstmt.setInt(2, contestant.getUserid());
			pstmt.setString(3, contestant.getTeamname());
			pstmt.setString(4, contestant.getPassword());
			pstmt.setString(5, contestant.getEmail());
			pstmt.setString(6, contestant.getSchool());
			pstmt.setString(7, contestant.getIpset().toString());
			pstmt.setString(8, contestant.getLanguage());
			pstmt.setString(9, Arrays.toString(contestant.getAclist()));
			pstmt.setString(10, contestant.getStatus().name());
			pstmt.setTimestamp(11, contestant.getFinishtime());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return id;
	}

	/**
	 * @deprecated 不再使用 teamaccount
	 * @param contestid
	 * @param teamaccount
	 * @param password
	 * @return
	 */
	private boolean isContestTeamaccount(Integer contestid, String teamaccount, String password) {
		String sql = "SELECT * FROM contestants WHERE teamaccount=? AND password=? AND contestid=?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, teamaccount);
			pstmt.setString(2, password);
			pstmt.setInt(3, contestid);
			if (this.executeQuery(pstmt, Contestant.class).size() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException("參賽者團隊帳號密碼有誤！");
		}
		return false;
	}

	public void removeContestant(Integer contestid, String teamaccount) {
		String sql = "DELETE from contestants WHERE contestid=? AND teamaccount=?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, contestid);
			pstmt.setString(2, teamaccount);
			this.executeUpdate(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 取得競賽名單。顯示成績看板，依照成績排序。被踢出的人就不列入其中。
	 * 
	 * @param contestid
	 * @return
	 */
	public ArrayList<Contestant> getContestantsForRanking(int contestid) {
		String sql = "SELECT * FROM contestants WHERE contestid=" + contestid + " AND status!='"
				+ Contestant.STATUS.kicked + "' AND submits>0 ORDER BY score DESC, ac "
				+ "DESC, penalty ASC, submits ASC ";
		return this.executeQuery(sql, Contestant.class);
	}

	/**
	 * 取得沒有送出任何解題的參賽者
	 * 
	 * @param contestid
	 * @return
	 */
	public ArrayList<Contestant> getContestantWithoutSubmit(int contestid) {
		String sql = "SELECT * FROM contestants WHERE contestid=" + contestid + " AND status!='"
				+ Contestant.STATUS.kicked + "' AND submits=0 ORDER BY score DESC, ac "
				+ "DESC, penalty ASC, submits ASC ";
		return this.executeQuery(sql, Contestant.class);
	}

	/**
	 * 取得本次競賽的 user , 包含已經被踢出的。只有在後端管理才需要這個。
	 * 
	 * @param contestid
	 * @return
	 */
	public ArrayList<Contestant> getAllContestants(int contestid) {
		String sql = "SELECT * FROM contestants WHERE contestid=" + contestid;
		return executeQuery(sql, Contestant.class);
	}

	@Override
	public boolean delete(int i) {
		return false;
	}

	public Contestant getContestant(int id) {
		String sql = "SELECT * FROM contestants WHERE id=" + id;
		for (Contestant contestant : executeQuery(sql, Contestant.class)) {
			return contestant;
		}
		return new Contestant();
	}


	/**
	 * @deprecated 不使用 team
	 * @param contest
	 * @param teamaccount
	 * @param passwd
	 * @return
	 * @throws DataException
	 */
	public Contestant getContestantByAccountPasswd(Contest contest, String teamaccount, String passwd)
			throws DataException {
		String sql = "SELECT * FROM contestants WHERE teamaccount=? AND password=? AND contestid=?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setString(1, teamaccount);
			pstmt.setString(2, passwd);
			pstmt.setInt(3, contest.getId());
			for (Contestant contestant : this.executeQuery(pstmt, Contestant.class)) {
				return contestant;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		throw new DataException("參賽者的團隊帳號密碼有誤！");
	}

	public Contestant getContestantByUserid(int userid, int contestid) throws DataException {
		String sql = "SELECT * FROM contestants WHERE  userid=? AND contestid=?";
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			pstmt.setInt(1, userid);
			pstmt.setInt(2, contestid);
			for (Contestant contestant : this.executeQuery(pstmt, Contestant.class)) {
				return contestant;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Contestant();
	}

	public int getContestantCountByContestid(int contestid) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("contestid", contestid);
		return this.executeCount("contestants", fields);
	}

	/**
	 * 取得已經在線上的 contestant count
	 * 
	 * @param contestid
	 * @return
	 */
	public int getJoinedContestantCountByContestid(int contestid) {
		String SQL = "SELECT * FROM contestants WHERE contestid=" + contestid + " AND (status='"
				+ Contestant.STATUS.online + "' OR status='" + Contestant.STATUS.offline + "')";
		return executeCount(SQL);
	}

	public int getOnlineContestantsCount(int contestid) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("contestid", contestid);
		fields.put("status", Contestant.STATUS.online.name());
		return this.executeCount("contestants", fields);
	}

	public int getOfflineContestantsCount(int contestid) {
		TreeMap<String, Object> fields = new TreeMap<String, Object>();
		fields.put("contestid", contestid);
		fields.put("status", Contestant.STATUS.offline.name());
		return this.executeCount("contestants", fields);
	}

}
