package tw.zerojudge.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Tables.Task;

public class TaskDAO extends SuperDAO<Task> {

	public TaskDAO() {
	}


	public long getThreadId_NOUSE(String parameter) {
		String sql = "SELECT * FROM tasks WHERE parameter='" + parameter
				+ "' AND status='" + Task.STATUS.Running.name() + "'";
		for (Task task : this.executeQuery(sql, Task.class)) {
			return task.getThreadid();
		}
		return 0;
	}

	public Task getTaskByThreadid(long threadid) {
		String sql = "SELECT * FROM tasks WHERE threadid=" + threadid;
		for (Task task : this.executeQuery(sql, Task.class)) {
			return task;
		}
		return null;
	}

	/**
	 * 取得 running 的 task
	 * 
	 * @param name
	 * @return
	 */
	public ArrayList<Task> getRunningTasksByName(Task.NAME name) {
		String sql = "SELECT * FROM tasks WHERE name='" + name
				+ "' AND status='" + Task.STATUS.Running
				+ "' ORDER BY stoptime ASC";
		return this.executeQuery(sql, Task.class);
	}

	public ArrayList<Task> getTasksByCodeLocker() {
		return this.getRunningTasksByName(Task.NAME.CodeLocker);
	}

	public ArrayList<Task> getTasksByContestCloser() {
		return this.getRunningTasksByName(Task.NAME.ContestCloser);
	}

	public ArrayList<Task> getTasksByContestStarter() {
		return this.getRunningTasksByName(Task.NAME.ContestStarter);
	}

	/**
	 * 取得 running 的 task
	 * 
	 * @return
	 */
	public ArrayList<Task> getRunningTasks() {
		String sql = "SELECT * FROM tasks WHERE status='" + Task.STATUS.Running
				+ "' ORDER BY stoptime ASC";
		return this.executeQuery(sql, Task.class);
	}

	/**
	 * 用 solutionid 取出 task, 用在 codelocker 中
	 * 
	 * @param solutionid
	 * @return
	 */
	public ArrayList<Task> getRunningTasksBySolutionid_NOUSE(int solutionid) {
		String sql = "SELECT * FROM tasks WHERE status='" + Task.STATUS.Running
				+ "' AND parameter='" + solutionid + "' ORDER BY id DESC";
		return this.executeQuery(sql, Task.class);
	}

	public Task getTaskById(int id) {
		String sql = "SELECT * FROM tasks WHERE id=" + id;
		for (Task task : this.executeQuery(sql, Task.class)) {
			return task;
		}
		return new Task();
	}

	public synchronized int insert(Task task) {
		String sql = "INSERT INTO tasks (name, useraccount, "
				+ "firststart, period, threadid, parameter, starttime, "
				+ "stoptime, status) VALUES(?,?,?,?,?,?,?,?,?);";
		int id = 0;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, task.getName().name());
			pstmt.setString(2, task.getUseraccount());
			pstmt.setTimestamp(3, new Timestamp(task.getFirststart().getTime()));
			pstmt.setLong(4, task.getPeriod());
			pstmt.setLong(5, task.getThreadid());
			pstmt.setString(6, task.getParameter());
			pstmt.setTimestamp(7, new Timestamp(task.getStarttime().getTime()));
			pstmt.setTimestamp(8, new Timestamp(task.getStoptime().getTime()));
			pstmt.setString(9, task.getStatus().name());
			executeUpdate(pstmt);
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public synchronized int update(Task task) {
		String SQL = "UPDATE tasks SET name=?, useraccount=?, firststart=?, period=?, "
				+ "threadid=?, parameter=?, starttime=?, stoptime=?, status=? WHERE id=?";
		int result = -1;
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(SQL);
			pstmt.setString(1, task.getName().name());
			pstmt.setString(2, task.getUseraccount());
			pstmt.setTimestamp(3, new Timestamp(task.getFirststart().getTime()));
			pstmt.setLong(4, task.getPeriod());
			pstmt.setLong(5, task.getThreadid());
			pstmt.setString(6, task.getParameter());
			pstmt.setTimestamp(7, new Timestamp(task.getStarttime().getTime()));
			pstmt.setTimestamp(8, new Timestamp(task.getStoptime().getTime()));
			pstmt.setString(9, task.getStatus().name());
			pstmt.setInt(10, task.getId());
			result = executeUpdate(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			new LogDAO().insert(new Log(this.getClass(), e));
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean delete(int i) {
		return false;
	}
}
