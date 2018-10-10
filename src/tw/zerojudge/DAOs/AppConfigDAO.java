/**
 * idv.jiangsir.DAOs - UserDAO.java
 * 2008/4/29 下午 05:46:51
 * jiangsir
 */
package tw.zerojudge.DAOs;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.DAO.SuperDAO;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.Configs.AppConfig.FIELD;

/**
 * @author jiangsir
 * 
 */
public class AppConfigDAO extends SuperDAO<AppConfig> {
	ObjectMapper mapper = new ObjectMapper(); 

	@Override
	protected synchronized int insert(AppConfig appConfig) throws SQLException {
		String sql = "INSERT INTO appconfigs(" + FIELD.title + ", " + FIELD.header + ", " + FIELD.pageSize + ", "
				+ FIELD.manager_ip + ", " + FIELD.rankingmode_HSIC + ", " + FIELD.EnableMailer + ", "
				+ FIELD.CandidateManager + ", " + FIELD.systemMode + ", " + FIELD.systemModeContestid + ", "
				+ FIELD.consolePath + ", " + FIELD.system_closed_message + ", " + FIELD.problemid_prefix + ", "
				+ FIELD.problemtabs + ", " + FIELD.serverUrl + ", " + FIELD.cryptKey + ", " + FIELD.rsyncAccount + ", "
				+ FIELD.system_monitor_ip + ", " + FIELD.competition_mode + ", " + FIELD.rankingmode_NPSC + ", "
				+ FIELD.rankingmode_CSAPC + ", " + FIELD.SystemMonitorAccount + ", " + FIELD.titleImage + ", "
				+ FIELD.logo + ", " + FIELD.threshold + ", " + FIELD.Locales + ", `" + FIELD.schemas + "`, "
				+ FIELD.exclusiveSchoolids + ", " + FIELD.cachedUser + ", " + FIELD.cachedContest + ", "
				+ FIELD.cachedProblem + ", " + FIELD.rejudgeable + ", " + FIELD.allowedIP + ", " + FIELD.judgeQueueSize
				+ ", " + FIELD.maxConnectionByIP + ", " + FIELD.maxCodeLength + ", " + FIELD.maxTestdata + ", "
				+ FIELD.SearchEngines + ", " + FIELD.bannedIPSet + ", " + FIELD.managers + ", " + FIELD.banners + ", "
				+ FIELD.SystemMail + ", " + FIELD.SystemMailPassword + ", " + FIELD.JVM + ","
				+ FIELD.last_solutionid_for_RecountProblemidset + "," + FIELD.httpscrt + "," + FIELD.client_id + ","
				+ FIELD.client_secret + "," + FIELD.redirect_uri + ", timestamp) VALUES ("
				+ "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?," + "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, "
				+ "?,?,?,?,?, ?,?,?,now());";
		PreparedStatement pstmt = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		pstmt.setString(1, appConfig.getTitle());
		pstmt.setString(2, appConfig.getHeader());
		pstmt.setInt(3, appConfig.getPageSize());
		pstmt.setString(4, appConfig.getManager_ip().toString());
		pstmt.setString(5, appConfig.getRankingmode_HSIC());
		pstmt.setBoolean(6, appConfig.getEnableMailer());
		pstmt.setString(7, appConfig.getCandidateManager());
		pstmt.setString(8, appConfig.getSystemMode().name());
		pstmt.setInt(9, appConfig.getSystemModeContestid());
		pstmt.setString(10, appConfig.getConsolePath());
		pstmt.setString(11, appConfig.getSystem_closed_message());
		pstmt.setString(12, appConfig.getProblemid_prefix());
		try {
			String problemtabs = mapper.writeValueAsString(appConfig.getProblemtabs());
			pstmt.setString(13, problemtabs);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pstmt.setString(14, appConfig.getServerUrl().toString());
		pstmt.setString(15, appConfig.getCryptKey());
		pstmt.setString(16, appConfig.getRsyncAccount());
		pstmt.setString(17, appConfig.getSystem_monitor_ip());
		pstmt.setString(18, appConfig.getCompetition_mode());
		pstmt.setString(19, appConfig.getRankingmode_NPSC());
		pstmt.setString(20, appConfig.getRankingmode_CSAPC());
		pstmt.setString(21, appConfig.getSystemMonitorAccount());
		pstmt.setBytes(22, appConfig.getTitleImage());
		pstmt.setBytes(23, appConfig.getLogo());
		pstmt.setDouble(24, appConfig.getThreshold());
		try {
			String locales = mapper.writeValueAsString(appConfig.getLocales());
			pstmt.setString(25, locales);
			String schemas = mapper.writeValueAsString(appConfig.getSchemas());
			pstmt.setString(26, schemas);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pstmt.setString(27, appConfig.getExclusiveSchoolids().toString());
		pstmt.setBoolean(28, appConfig.getCachedUser());
		pstmt.setBoolean(29, appConfig.getCachedContest());
		pstmt.setBoolean(30, appConfig.getCachedProblem());
		pstmt.setBoolean(31, appConfig.getRejudgeable());
		pstmt.setString(32, appConfig.getAllowedIP().toString());
		pstmt.setInt(33, appConfig.getJudgeQueueSize());
		pstmt.setInt(34, appConfig.getMaxConnectionByIP());
		pstmt.setInt(35, appConfig.getMaxCodeLength());
		pstmt.setInt(36, appConfig.getMaxTestdata());
		pstmt.setString(37, appConfig.getSearchEngines().toString());
		pstmt.setString(38, appConfig.getBannedIPSet().toString());
		pstmt.setString(39, appConfig.getManagers().toString());
		try {
			String banners = mapper.writeValueAsString(appConfig.getBanners());
			pstmt.setString(40, banners);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pstmt.setString(41, appConfig.getSystemMail());
		pstmt.setString(42, appConfig.getSystemMailPassword());
		pstmt.setInt(43, appConfig.getJVM());
		pstmt.setInt(44, appConfig.getLast_solutionid_for_RecountProblemidset());
		pstmt.setBytes(45, appConfig.getHttpscrt());
		pstmt.setString(46, appConfig.getClient_id());
		pstmt.setString(47, appConfig.getClient_secret());
		pstmt.setString(48, appConfig.getRedirect_uri());
		return this.executeInsert(pstmt);
	}

	protected synchronized int update(AppConfig appConfig) throws SQLException {
		int result = 0;
		return result;
	}

	protected ArrayList<AppConfig> getAppConfigByFields(TreeMap<String, Object> fields, String orderby, int page) {
		String sql = "SELECT * FROM appconfigs " + this.makeFields(fields, orderby, page);
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(sql);
			int i = 1;
			for (String field : fields.keySet()) {
				pstmt.setObject(i++, fields.get(field));
			}
			return executeQuery(pstmt, AppConfig.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * 清空 appconfig
	 * 
	 * @return
	 */
	protected boolean truncate() {
		return this.execute("TRUNCATE TABLE `appconfigs`");
	}

	/**
	 * 計算 appconfigs 資料表共有幾筆資料。
	 * 
	 * @return
	 */
	protected int countAll() {
		return this.executeCount("SELECT * FROM appconfigs");
	}

	@Override
	protected boolean delete(int i) {
		return false;
	}

}
