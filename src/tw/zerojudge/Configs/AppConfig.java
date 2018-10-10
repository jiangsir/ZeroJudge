package tw.zerojudge.Configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import javax.net.ssl.HttpsURLConnection;

import tw.jiangsir.Utils.Annotations.AppConfigField;
import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Tools.RunCommand;
import tw.jiangsir.Utils.Tools.StringTool;
import tw.zerojudge.Api.ProblemApiServlet;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Factories.JudgeFactory;
import tw.zerojudge.JsonObjects.Banner;
import tw.zerojudge.JsonObjects.Compiler;
import tw.zerojudge.JsonObjects.Problemtab;
import tw.zerojudge.JsonObjects.Schema;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Problemid;
import tw.zerojudge.Servlets.DeleteVClassServlet;
import tw.zerojudge.Servlets.DeprecateProblemServlet;
import tw.zerojudge.Servlets.EditProblemsServlet;
import tw.zerojudge.Servlets.InsertContestServlet;
import tw.zerojudge.Servlets.InsertStudentsServlet;
import tw.zerojudge.Servlets.InsertUsersServlet;
import tw.zerojudge.Servlets.InsertVClassServlet;
import tw.zerojudge.Servlets.InsertVContestServlet;
import tw.zerojudge.Servlets.EditContestsServlet;
import tw.zerojudge.Servlets.EditContestantsServlet;
import tw.zerojudge.Servlets.EditVClassesServlet;
import tw.zerojudge.Servlets.RebuiltContestServlet;
import tw.zerojudge.Servlets.RebuiltVClassServlet;
import tw.zerojudge.Servlets.RemoveVClassStudentServlet;
import tw.zerojudge.Servlets.ShowVClassServlet;
import tw.zerojudge.Servlets.UpdateForumServlet;
import tw.zerojudge.Servlets.UpdateProblemServlet;
import tw.zerojudge.Servlets.UpdateVClassServlet;
import tw.zerojudge.Servlets.VClassContestServlet;
import tw.zerojudge.Servlets.Ajax.PreJudgeAjax;
import tw.zerojudge.Servlets.Utils.KickedUserServlet;
import tw.zerojudge.Tables.Log;
import tw.zerojudge.Utils.DES;

public class AppConfig {
	Logger logger = Logger.getLogger(this.getClass().getName());

	public static enum FIELD {
		manager_ip, //
		rankingmode_HSIC, //
		EnableMailer, 
		CandidateManager, //
		systemMode, //
		systemModeContestid, //
		consolePath, //
		system_closed_message, //
		problemid_prefix, //
		problemtabs, //
		serverUrl, 
		rsyncAccount, 
		cryptKey, 
		title, //
		system_monitor_ip, //
		competition_mode, //
		rankingmode_NPSC, //
		rankingmode_CSAPC, //
		SystemMonitorAccount, //
		header, //
		titleImage, //
		logo, threshold, //
		Locales, //
		schemas, //
		exclusiveSchoolids, //
		cachedUser, //
		cachedContest, //
		cachedProblem, //
		JDBC, //
		rejudgeable, //
		allowedIP, //
		pageSize, //
		judgeQueueSize, //
		maxConnectionByIP, //
		maxCodeLength, //
		maxTestdata, //
		SearchEngines, //
		banners, //
		SystemMail, //
		SystemMailPassword, //
		JVM, //
		cacheSolutionPage, //
		hashSolutionPage, //
		bannedIPSet, 
		managers, 
		last_solutionid_for_RecountProblemidset, 
		httpscrtinfo, 
		httpscrt, 
		client_id, 
		client_secret, 
		redirect_uri, 
	}

	public static enum PATH {
		Testdata, //
		Special, //
		Bin, //
		Executable, //
		Compiler; //
	}

	@AppConfigField(name = FIELD.manager_ip)
	private TreeSet<IpAddress> manager_ip = new TreeSet<IpAddress>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5354900944796241866L;

		{
			add(new IpAddress("192.168.1.1/24"));
		}
	};
	@AppConfigField(name = FIELD.rankingmode_HSIC)
	private String rankingmode_HSIC = "";
	@AppConfigField(name = FIELD.EnableMailer)
	private boolean enableMailer = false;
	@AppConfigField(name = FIELD.CandidateManager)
	private String candidateManager = "";

	public static enum SYSTEM_MODE {
		TRAINING_MODE, 
		CONTEST_MODE, 
		CLOSE_MODE; 
	}

	@AppConfigField(name = FIELD.systemMode)
	private SYSTEM_MODE systemMode = SYSTEM_MODE.TRAINING_MODE;
	@AppConfigField(name = FIELD.systemModeContestid)
	private int systemModeContestid = 0;
	@AppConfigField(name = FIELD.consolePath)
	private String consolePath = "/ZeroJudge_CONSOLE";
	@AppConfigField(name = FIELD.system_closed_message)
	private String system_closed_message = "System is now closed!";
	@AppConfigField(name = FIELD.problemid_prefix)
	private String problemid_prefix = "a";
	@AppConfigField(name = FIELD.problemtabs)
	private ArrayList<Problemtab> problemtabs = new ArrayList<Problemtab>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -644125378669820075L;

		{
			add(new Problemtab());
		}
	};
	@AppConfigField(name = FIELD.serverUrl)
	private URL serverUrl = null;
	@AppConfigField(name = FIELD.rsyncAccount)
	private String rsyncAccount = "zero";
	@AppConfigField(name = FIELD.cryptKey)
	private String cryptKey = "ZZEERROO";
	@AppConfigField(name = FIELD.title)
	private String title = "A Title for Your Site";
	@AppConfigField(name = FIELD.system_monitor_ip)
	private String system_monitor_ip = "";
	@AppConfigField(name = FIELD.competition_mode)
	private String competition_mode = "";
	@AppConfigField(name = FIELD.rankingmode_NPSC)
	private String rankingmode_NPSC = "";
	@AppConfigField(name = FIELD.rankingmode_CSAPC)
	private String rankingmode_CSAPC = "";
	@AppConfigField(name = FIELD.SystemMonitorAccount)
	private String systemMonitorAccount = "";
	@AppConfigField(name = FIELD.header)
	private String header = "A Header For Your Site";
	@AppConfigField(name = FIELD.titleImage)
	private byte[] titleImage = new byte[] {};
	private String titleImageFileType = "image/svg+xml";
	@AppConfigField(name = FIELD.logo)
	private byte[] logo = new byte[] {};
	private String logoFileType = "image/svg+xml";
	@AppConfigField(name = FIELD.threshold)
	private double threshold = 3;
	@AppConfigField(name = FIELD.Locales)
	private Locale[] locales = new Locale[] { new Locale("en", "US"), new Locale("zh", "TW"), new Locale("zh", "CN") };
	@AppConfigField(name = FIELD.schemas)
	private Schema[] schemas = new Schema[] {};
	@AppConfigField(name = FIELD.exclusiveSchoolids)
	private TreeSet<Integer> exclusiveSchoolids = new TreeSet<Integer>();

	@AppConfigField(name = FIELD.cachedUser)
	private boolean cachedUser = false; 
	@AppConfigField(name = FIELD.cachedContest)
	private boolean cachedContest = false;
	@AppConfigField(name = FIELD.cachedProblem)
	private boolean cachedProblem = false;

	private String author = "Jiangsir";
	private String authorEmail = "jiangsir@zerojudge.tw";
	@AppConfigField(name = FIELD.JDBC)
	private String JDBC = "mysql";
	@AppConfigField(name = FIELD.rejudgeable)
	private boolean rejudgeable = false;
	@AppConfigField(name = FIELD.allowedIP)
	private TreeSet<IpAddress> allowedIP = new TreeSet<IpAddress>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5354900944796241866L;

		{
			add(new IpAddress("192.168.1.1/0"));
		}
	};
	@AppConfigField(name = FIELD.pageSize)
	private int pageSize = 20;
	@AppConfigField(name = FIELD.judgeQueueSize)
	private int judgeQueueSize = 100;
	@AppConfigField(name = FIELD.maxConnectionByIP)
	private int maxConnectionByIP = 1000;
	@AppConfigField(name = FIELD.maxCodeLength)
	private int maxCodeLength = 10 * 1000;
	@AppConfigField(name = FIELD.maxTestdata)
	private int maxTestdata = 10 * 1000 * 1000; 

	@AppConfigField(name = FIELD.SearchEngines)
	private TreeSet<IpAddress> searchEngines = new TreeSet<IpAddress>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -644125378669820075L;

		{
			add(new IpAddress("72.30.0.0/16"));
			add(new IpAddress("66.249.0.0/16"));
			add(new IpAddress("74.6.0.0/16"));
		}
	};
	@AppConfigField(name = FIELD.bannedIPSet)
	private TreeSet<IpAddress> bannedIPSet = new TreeSet<IpAddress>();
	@AppConfigField(name = FIELD.managers)
	private TreeSet<String> managers = new TreeSet<String>();

	@AppConfigField(name = FIELD.banners)
	private ArrayList<Banner> banners = new ArrayList<Banner>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2517201797036769339L;

		{
			add(new Banner("banner1", 100));
			add(new Banner("banner2", 0));
			add(new Banner("banner3", 0));
		}
	};

	@AppConfigField(name = FIELD.SystemMail)
	private String SystemMail = "sysop@yourdomain";
	@AppConfigField(name = FIELD.SystemMailPassword)
	private String SystemMailPassword = "";
	@AppConfigField(name = FIELD.JVM)
	private int JVM = 1000; 
	@AppConfigField(name = FIELD.cacheSolutionPage)
	private int cacheSolutionPage = 4;
	@AppConfigField(name = FIELD.hashSolutionPage)
	private int hashSolutionPage = 4;
	@AppConfigField(name = FIELD.last_solutionid_for_RecountProblemidset)
	private int last_solutionid_for_RecountProblemidset = 0;
	@AppConfigField(name = FIELD.httpscrtinfo)
	private String httpscrtinfo = "";
	@AppConfigField(name = FIELD.httpscrt)
	private byte[] httpscrt = new byte[] {};

	@AppConfigField(name = FIELD.client_id)
	private String client_id = "";
	@AppConfigField(name = FIELD.client_secret)
	private String client_secret = "";
	@AppConfigField(name = FIELD.redirect_uri)
	private String redirect_uri = "";


	ObjectMapper mapper = new ObjectMapper(); 
	private ServerConfig serverConfig = new ServerConfig();

	final private LinkedHashSet<Class<? extends HttpServlet>> PRIVILEGE_ContestManager = new LinkedHashSet<Class<? extends HttpServlet>>() {
		private static final long serialVersionUID = 1L;

		{
			add(InsertContestServlet.class);
			add(EditContestsServlet.class);
			add(RebuiltContestServlet.class);
			add(InsertContestServlet.class);
			add(KickedUserServlet.class);
			add(EditContestantsServlet.class);
		}
	};
//	final private LinkedHashSet<Class<? extends HttpServlet>> PRIVILEGE_GeneralManager = new LinkedHashSet<Class<? extends HttpServlet>>() {
//		private static final long serialVersionUID = 1L;
//
//		{
//			
//			
//			
//			
//			add(VerifyingProblemsServlet.class);
//			add(EditUserConfigServlet.class);
//			add(SystemMonitorServlet.class);
//		}
//	};
	final private LinkedHashSet<Class<? extends HttpServlet>> PRIVILEGE_ProblemManager = new LinkedHashSet<Class<? extends HttpServlet>>() {
		private static final long serialVersionUID = 1L;

		{
			add(EditProblemsServlet.class);
			add(UpdateProblemServlet.class);
			add(DeprecateProblemServlet.class);
			add(ProblemApiServlet.class);
			add(PreJudgeAjax.class);
			add(UpdateForumServlet.class);
		}
	};
	final private LinkedHashSet<Class<? extends HttpServlet>> PRIVILEGE_VClassManager = new LinkedHashSet<Class<? extends HttpServlet>>() {
		private static final long serialVersionUID = 1L;

		{
			add(EditVClassesServlet.class);
			add(InsertVClassServlet.class);
			add(UpdateVClassServlet.class);
			add(InsertStudentsServlet.class);
			add(InsertUsersServlet.class);
			add(ShowVClassServlet.class);
			add(VClassContestServlet.class);
			add(RemoveVClassStudentServlet.class);
			add(DeleteVClassServlet.class);
			add(RebuiltVClassServlet.class);
			add(InsertVContestServlet.class);
		}
	};


	public AppConfig() {
	}

	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public void setId(Integer id) {

	}

	public String getRankingmode_HSIC() {
		return rankingmode_HSIC;
	}

	public TreeSet<IpAddress> getManager_ip() {
		return manager_ip;
	}

	public void setManager_ip(TreeSet<IpAddress> manager_ip) {
		if (manager_ip == null || manager_ip.size() == 0) {
			return;
		}
		this.manager_ip = manager_ip;
	}

	public void setManager_ip(String manager_ip) {
		if (manager_ip == null || "".equals(manager_ip.trim()) || "[]".equals(manager_ip.trim())) {
			return;
		}
		this.setManager_ip(StringTool.String2IpAddressTreeSet(manager_ip));
	}

	public void setRankingmode_HSIC(String rankingmode_HSIC) {
		if (rankingmode_HSIC == null) {
			return;
		}
		this.rankingmode_HSIC = rankingmode_HSIC;
	}

	public boolean getEnableMailer() {
		return enableMailer;
	}

	public void setEnableMailer(Boolean enableMailer) {
		this.enableMailer = enableMailer;
	}

	public void setEnableMailer(String enableMailer) {
		if (enableMailer == null) {
			return;
		}
		this.setEnableMailer(Boolean.valueOf(enableMailer));
	}

	//


	public String getCandidateManager() {
		return candidateManager;
	}

	public void setCandidateManager(String candidateManager) {
		if (candidateManager == null) {
			return;
		}
		this.candidateManager = candidateManager;
	}

	public SYSTEM_MODE getSystemMode() {
		return systemMode;
	}

	public void setSystemMode(SYSTEM_MODE systemMode) {
		this.systemMode = systemMode;
	}

	public void setSystemMode(String systemMode) {
		try {
			this.setSystemMode(SYSTEM_MODE.valueOf(systemMode));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getSystemModeContestid() {
		return systemModeContestid;
	}

	public void setSystemModeContestid(Integer systemModeContestid) {
		this.systemModeContestid = systemModeContestid;
	}

	public void setSystemModeContestid(String systemModeContestid) {
		if (systemModeContestid == null || !systemModeContestid.matches("[0-9]+")) {
			return;
		}
		this.setSystemModeContestid(Integer.parseInt(systemModeContestid));
	}

	public String getConsolePath() {
		return consolePath;
	}

	public void setConsolePath(String consolePath) {
		this.consolePath = consolePath;
	}

	public String getSystem_closed_message() {
		return system_closed_message;
	}

	public void setSystem_closed_message(String system_closed_message) {
		if (system_closed_message == null) {
			return;
		}
		this.system_closed_message = system_closed_message;
	}

	public String getProblemid_prefix() {
		return problemid_prefix;
	}

	public void setProblemid_prefix(String problemid_prefix) {
		if (problemid_prefix == null) {
			return;
		}
		this.problemid_prefix = problemid_prefix;
	}

	public ArrayList<Problemtab> getProblemtabs() {
		return problemtabs;
	}

	public void setProblemtabs(ArrayList<Problemtab> problemtabs) {
		if (problemtabs == null || problemtabs.size() == 0) {
			return;
		}
		this.problemtabs = problemtabs;
	}

	@SuppressWarnings("unchecked")
	public void setProblemtabs(String problemtabs) {
		if (problemtabs == null) {
			return;
		}
		try {
			this.setProblemtabs(
					(ArrayList<Problemtab>) mapper.readValue(problemtabs, new TypeReference<ArrayList<Problemtab>>() {
					}));
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new DataException(" KEY \"" + FIELD.problemtabs + "\" " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new DataException(" KEY \"" + FIELD.problemtabs + "\" " + e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataException(" KEY \"" + FIELD.problemtabs + "\" " + e.getLocalizedMessage());
		}

	}

	private URL getDefaultUrl() {
		try {
			return new URL("http://127.0.0.1:8080/ZeroJudge_Server/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public URL getServerUrl() {
		if (serverUrl == null) {
			return this.getDefaultUrl();
		}
		return serverUrl;
	}

	public void setServerUrl(URL serverUrl) {
		if (serverUrl == null) {
			return;
		}
		this.serverUrl = serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		try {
			this.setServerUrl(new URL(serverUrl));
		} catch (MalformedURLException e) {
			if (e.getLocalizedMessage().startsWith("no protocol")) {
				try {
					this.setServerUrl(new URL("http://" + serverUrl));
					return;
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			this.setServerUrl(this.getDefaultUrl());
		}
	}

	public String getRsyncAccount() {
		return rsyncAccount;
	}

	public void setRsyncAccount(String rsyncAccount) {
		if (rsyncAccount == null || rsyncAccount.trim().equals("")) {
			return;
		}
		this.rsyncAccount = rsyncAccount;
	}

	public String getCryptKey() {
		return cryptKey;
	}

	public void setCryptKey(String cryptKey) {
		if (cryptKey == null || cryptKey.trim().equals("")) {
			return;
		}
		this.cryptKey = cryptKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		title = title.trim();
		if (title == null || "".equals(title.trim())) {
			return;
		}
		int MAX = 255;
		if (title.getBytes().length > MAX) {
			throw new DataException("title 設定值超過長度限制 (" + title.getBytes().length + ">" + MAX + "字元)。");
		}
		this.title = title;
	}

	public String getSystem_monitor_ip() {
		return system_monitor_ip;
	}

	public void setSystem_monitor_ip(String system_monitor_ip) {
		if (system_monitor_ip == null) {
			return;
		}
		this.system_monitor_ip = system_monitor_ip;
	}

	public String getCompetition_mode() {
		return competition_mode;
	}

	public void setCompetition_mode(String competition_mode) {
		if (competition_mode == null) {
			return;
		}
		this.competition_mode = competition_mode;
	}

	public String getRankingmode_NPSC() {
		return rankingmode_NPSC;
	}

	public void setRankingmode_NPSC(String rankingmode_NPSC) {
		if (rankingmode_NPSC == null) {
			return;
		}
		this.rankingmode_NPSC = rankingmode_NPSC;
	}

	public String getRankingmode_CSAPC() {
		return rankingmode_CSAPC;
	}

	public void setRankingmode_CSAPC(String rankingmode_CSAPC) {
		if (rankingmode_CSAPC == null) {
			return;
		}
		this.rankingmode_CSAPC = rankingmode_CSAPC;
	}

	public String getSystemMonitorAccount() {
		return systemMonitorAccount;
	}

	public void setSystemMonitorAccount(String systemMonitorAccount) {
		if (systemMonitorAccount == null) {
			return;
		}
		this.systemMonitorAccount = systemMonitorAccount;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		if (header == null) {
			return;
		}
		this.header = header;
	}

	/**
	 * inline image
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getTitleImageBase64() throws IOException {
		if (this.getTitleImage().length == 0) {
			File titleImageFile = new File(ApplicationScope.getAppRoot() + File.separator + "images", "TitleImage.svg");
			this.setTitleImage(FileUtils.readFileToByteArray(titleImageFile));
		}
		final Base64 base64 = new Base64();
		String imageBase64 = base64.encodeToString(this.getTitleImage());
		return "data:" + this.getTitleImageFileType() + ";base64," + imageBase64;
	}

	public byte[] getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(byte[] titleImage) {
		if (titleImage == null) {
			return;
		}
		this.titleImage = titleImage;
	}

	public void setTitleImage(String titleImage) {
		if (titleImage == null) {
			return;
		}
		this.setTitleImage(titleImage.getBytes());
	}

	/**
	 * inline image
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getLogoBase64() throws IOException {
		if (this.getLogo().length == 0) {
			File logoFile = new File(ApplicationScope.getAppRoot() + File.separator + "images", "Logo.svg");
			this.setLogo(FileUtils.readFileToByteArray(logoFile));
		}
		return "data:" + this.getLogoFileType() + ";base64," + new Base64().encodeToString(this.getLogo());
	}

	public String getLogoFileType() {
		return logoFileType;
	}

	public void setLogoFileType(String logoFileType) {
		this.logoFileType = logoFileType;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		if (logo == null)
			return;
		this.logo = logo;
	}

	public void setLogo(String logo) {
		if (logo == null) {
			return;
		}
		this.setLogo(logo.getBytes());
	}

	public String getTitleImageFileType() {
		return titleImageFileType;
	}

	public void setTitleImageFileType(String titleImageFileType) {
		this.titleImageFileType = titleImageFileType;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public void setThreshold(String threshold) throws DataException {
		if (threshold == null) {
			return;
		}
		try {
			this.setThreshold(Double.parseDouble(threshold));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new DataException(" KEY \"" + FIELD.threshold + "\" " + e.getLocalizedMessage());
		}
	}

	public Locale[] getLocales() {
		return locales;
	}

	public void setLocales(Locale[] locales) {
		this.locales = locales;
	}

	public void setLocales(String locales) throws DataException {
		if (locales == null) {
			return;
		}
		try {
			this.setLocales(mapper.readValue(locales, Locale[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Schema[] getSchemas() {
		return schemas;
	}

	public void setSchemas(Schema[] schemas) {
		this.schemas = schemas;
	}

	public void setSchemas(String schemas) {
		if (schemas == null) {
			return;
		}
		try {
			this.setSchemas(mapper.readValue(schemas, Schema[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new DataException(" KEY \"" + FIELD.schemas + "\" " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new DataException(" KEY \"" + FIELD.schemas + "\" " + e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataException(" KEY \"" + FIELD.schemas + "\" " + e.getLocalizedMessage());
		}

	}

	public TreeSet<Integer> getExclusiveSchoolids() {
		return exclusiveSchoolids;
	}

	public void setExclusiveSchoolids(TreeSet<Integer> exclusiveSchoolids) {
		this.exclusiveSchoolids = exclusiveSchoolids;
	}

	public void setExclusiveSchoolids(String exclusiveSchoolids) {
		if (exclusiveSchoolids == null) {
			return;
		}
		this.setExclusiveSchoolids((TreeSet<Integer>) StringTool.String2IntegerSet(exclusiveSchoolids));
	}

	public boolean getCachedUser() {
		return cachedUser;
	}

	public void setCachedUser(Boolean cachedUser) {
		this.cachedUser = cachedUser;
	}

	public void setCachedUser(String cachedUser) {
		this.setCachedUser(Boolean.valueOf(cachedUser));
	}

	public boolean getCachedContest() {
		return cachedContest;
	}

	public void setCachedContest(Boolean cachedContest) {
		this.cachedContest = cachedContest;
	}

	public void setCachedContest(String cachedContest) {
		if (cachedContest == null) {
			return;
		}
		this.setCachedContest(Boolean.valueOf(cachedContest));
	}

	public boolean getCachedProblem() {
		return cachedProblem;
	}

	public void setCachedProblem(Boolean cachedProblem) {
		this.cachedProblem = cachedProblem;
	}

	public void setCachedProblem(String cachedProblem) {
		this.setCachedProblem(Boolean.valueOf(cachedProblem));
	}

	public String getAuthor() {
		return author;
	}


	public String getAuthorEmail() {
		return authorEmail;
	}


	public String getJDBC() {
		return JDBC;
	}

	public void setJDBC(String jDBC) {
		if (jDBC == null) {
			return;
		}
		JDBC = jDBC;
	}

	public boolean getRejudgeable() {
		return rejudgeable;
	}

	public void setRejudgeable(Boolean rejudgeable) {
		this.rejudgeable = rejudgeable;
	}

	public void setRejudgeable(String rejudgeable) {
		try {
			this.setRejudgeable(Boolean.valueOf(rejudgeable));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//

	public int getPageSize() {
		return pageSize;
	}

	public TreeSet<IpAddress> getAllowedIP() {
		return allowedIP;
	}

	public void setAllowedIP(TreeSet<IpAddress> allowedIP) {
		if (allowedIP == null || allowedIP.size() == 0) {
			return;
		}
		this.allowedIP = allowedIP;
	}

	public void setAllowedIP(String allowedIP) {
		if (allowedIP == null || "".equals(allowedIP.trim()) || "[]".equals(allowedIP.trim())) {
			return;
		}
		this.setAllowedIP(StringTool.String2IpAddressTreeSet(allowedIP));
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageSize(String pageSize) {
		if (pageSize == null || !pageSize.matches("[0-9]+")) {
			return;
		}
		this.setPageSize(Integer.parseInt(pageSize));
	}

	public int getJudgeQueueSize() {
		return JudgeFactory.getJudgeQueue().size();

	}

	public void setJudgeQueueSize(Integer judgeQueueSize) {
		this.judgeQueueSize = judgeQueueSize;
	}

	public void setJudgeQueueSize(String judgeQueueSize) {
		if (judgeQueueSize == null || !judgeQueueSize.matches("[0-9]+")) {
			return;
		}
		this.setJudgeQueueSize(Integer.parseInt(judgeQueueSize));
	}

	public int getMaxConnectionByIP() {
		return maxConnectionByIP;
	}

	public void setMaxConnectionByIP(Integer maxConnectionByIP) {
		this.maxConnectionByIP = maxConnectionByIP;
	}

	public void setMaxConnectionByIP(String maxConnectionByIP) {
		if (maxConnectionByIP == null || !maxConnectionByIP.matches("[0-9]+")) {
			return;
		}
		this.setMaxConnectionByIP(Integer.parseInt(maxConnectionByIP));
	}

	public int getMaxCodeLength() {
		return maxCodeLength;
	}

	public void setMaxCodeLength(Integer maxCodeLength) {
		this.maxCodeLength = maxCodeLength;
	}

	public void setMaxCodeLength(String maxCodeLength) {
		if (maxCodeLength == null || !maxCodeLength.matches("[0-9]+")) {
			return;
		}
		this.setMaxCodeLength(Integer.parseInt(maxCodeLength));
	}

	public int getMaxTestdata() {
		return maxTestdata;
	}

	public void setMaxTestdata(Integer maxTestdata) {
		this.maxTestdata = maxTestdata;
	}

	public void setMaxTestdata(String maxTestdata) {
		if (maxTestdata == null || !maxTestdata.matches("[0-9]+")) {
			return;
		}
		this.setMaxTestdata(Integer.parseInt(maxTestdata));
	}

	//

	public TreeSet<IpAddress> getBannedIPSet() {
		return bannedIPSet;
	}

	public TreeSet<IpAddress> getSearchEngines() {
		return searchEngines;
	}

	public void setSearchEngines(TreeSet<IpAddress> searchEngines) {
		this.searchEngines = searchEngines;
	}

	public void setSearchEngines(String searchEngines) {
		if (searchEngines == null || "".equals(searchEngines.trim())) {
			return;
		}
		this.setSearchEngines(StringTool.String2IpAddressTreeSet(searchEngines));
	}

	public void setBannedIPSet(TreeSet<IpAddress> bannedIPSet) {
		this.bannedIPSet = bannedIPSet;
	}

	public void setBannedIPSet(String bannedIPSet) {
		if (bannedIPSet == null) {
			return;
		}
		this.setBannedIPSet(StringTool.String2IpAddressTreeSet(bannedIPSet));
	}

	public TreeSet<String> getManagers() {
		return managers;
	}

	public void setManagers(TreeSet<String> managers) {
		this.managers = managers;
	}

	public void setManagers(String managers) {
		if (managers == null) {
			return;
		}
		this.setManagers(StringTool.String2TreeSet(managers));
	}

	//

	public ArrayList<Banner> getBanners() {
		return banners;
	}

	public void setBanners(ArrayList<Banner> banners) {
		if (banners == null) {
			return;
		}
		this.banners = banners;
	}

	public String getBannerContent() {
		String bannerContent = "";
		int total = 0;
		for (Banner banner : this.getBanners()) {
			total += banner.getPercent();
		}
		int rate = 0;
		double random = Math.random() * total;
		for (Banner banner : this.getBanners()) {
			rate = banner.getPercent();
			if (random > 0 && random <= rate) {
				bannerContent = banner.getContent();
				break;
			} else {
				total -= rate;
				random = Math.random() * total;
			}
		}
		return bannerContent;
	}

	@SuppressWarnings("unchecked")
	public void setBanners(String banners) {
		if (banners == null) {
			return;
		}
		try {
			this.setBanners((ArrayList<Banner>) mapper.readValue(banners, new TypeReference<ArrayList<Banner>>() {
			}));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//

	public String getSystemMail() {
		return SystemMail;
	}

	public void setSystemMail(String systemMail) {
		if (systemMail == null) {
			return;
		}
		this.SystemMail = systemMail;
	}

	public String getSystemMailPassword() {
		return SystemMailPassword;
	}

	public void setSystemMailPassword(String systemMailPassword) {
		if (systemMailPassword == null) {
			return;
		}
		this.SystemMailPassword = systemMailPassword;
	}

	public int getJVM() {
		return JVM;
	}

	public void setJVM(Integer jVM) {
		JVM = jVM;
	}

	public void setJVM(String jVM) {
		if (jVM == null || "".equals(jVM.trim())) {
			return;
		}
		if (!jVM.trim().matches("[0-9]+")) {
			throw new DataException("JVM 輸入必須是整數。");
		}
		this.setJVM(Integer.parseInt(jVM.trim()));
	}

	public int getCacheSolutionPage() {
		return cacheSolutionPage;
	}

	public void setCacheSolutionPage(int cacheSolutionPage) {
		this.cacheSolutionPage = cacheSolutionPage;
	}

	public void setCacheSolutionPage(String cacheSolutionPage) {
		if (cacheSolutionPage == null || !cacheSolutionPage.matches("[0-9]+")) {
			return;
		}
		this.setCacheSolutionPage(Integer.parseInt(cacheSolutionPage));
	}

	public int getHashSolutionPage() {
		return hashSolutionPage;
	}

	public void setHashSolutionPage(int hashSolutionPage) {
		this.hashSolutionPage = hashSolutionPage;
	}

	public void setHashSolutionPage(String hashSolutionPage) {
		if (hashSolutionPage == null || !hashSolutionPage.matches("[0-9]+")) {
			return;
		}
		this.setHashSolutionPage(Integer.parseInt(hashSolutionPage));
	}

	public void setTimestamp(Timestamp time) {

	}

	public Integer getLast_solutionid_for_RecountProblemidset() {
		return last_solutionid_for_RecountProblemidset;
	}

	public void setLast_solutionid_for_RecountProblemidset(Integer last_solutionid_for_RecountProblemidset) {
		this.last_solutionid_for_RecountProblemidset = last_solutionid_for_RecountProblemidset;
	}

	public void setLast_solutionid_for_RecountProblemidset(String last_solutionid_for_RecountProblemidset) {
		if (last_solutionid_for_RecountProblemidset == null
				|| !last_solutionid_for_RecountProblemidset.matches("[0-9]+")) {
			return;
		}
		this.setLast_solutionid_for_RecountProblemidset(Integer.parseInt(last_solutionid_for_RecountProblemidset));
	}

	public File getTestdataPath() {
		return new File(this.getConsolePath(), PATH.Testdata.name());
	}

	public File getTestdataPath(Problemid problemid) {
		return new File(this.getTestdataPath(), problemid.toString() + File.separator);
	}

	public File getBinPathRestart_sh() {
		return new File(this.getBinPath(), "restart.sh");
	}

	public File getSpecialPath() {
		return new File(this.getConsolePath(), PATH.Special.name());
	}

	public File getSpecialPath(Problemid problemid) {
		return new File(this.getSpecialPath(), problemid + File.separator);
	}

	public File getBinPath() {
		return new File(this.getConsolePath(), PATH.Bin.name());
	}

	public File getExecutablePath() {
		return new File(this.getConsolePath(), PATH.Executable.name());
	}

	public File getCompilerPath() {
		return new File(this.getConsolePath(), PATH.Compiler.name());
	}

	public String getHttpscrtinfo() {
		return httpscrtinfo;
	}

	public void setHttpscrtinfo(String httpscrtinfo) {
		this.httpscrtinfo = httpscrtinfo;
	}

	public byte[] getHttpscrt() {
		return httpscrt;
	}

	public void setHttpscrt(byte[] httpscrt) {
		this.httpscrt = httpscrt;
	}

	public void setHttpscrt(String httpscrt) {
		if (httpscrt == null) {
			return;
		}
		this.setHttpscrt(httpscrt.getBytes());
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		if (client_id == null)
			return;
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		if (client_secret == null)
			return;
		this.client_secret = client_secret;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		if (redirect_uri == null)
			return;
		this.redirect_uri = redirect_uri.replaceAll("/$", "");
	}


	/**
	 * 確實的讀取遠端評分主機的相關資訊
	 */
	public ServerConfig readServerConfig(URL serverUrl) throws DataException {
		String serverConfigString = "";
		URL url;
		try {
			url = new URL(serverUrl + "/api/?action=getServerConfig");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new DataException("裁判機網址格式設定錯誤！", e);
		}
		serverConfigString = this.read(url, 5);
		logger.info("獲取 ServerConfig=" + serverConfigString);

		try {
			return mapper.readValue(serverConfigString, ServerConfig.class);
		} catch (JsonParseException e) {
			logger.severe("serverConfigString=" + serverConfigString);
			e.printStackTrace();
			throw new DataException("「裁判機參數」解析錯誤，可能使用了錯誤版本的裁判機程式。", e);
		} catch (JsonMappingException e) {
			logger.severe("serverConfigString=" + serverConfigString);
			e.printStackTrace();
			throw new DataException("「裁判機參數」解析錯誤，可能使用了錯誤版本的裁判機程式。", e);
		} catch (IOException e) {
			logger.severe("serverConfigString=" + serverConfigString);
			e.printStackTrace();
			throw new DataException("無法讀取「裁判機參數」！", e);
		}

	}

	/**
	 * 從 URL 讀取資料
	 * 
	 * @return
	 */
	private String read(URL url, int timeoutSec) throws DataException {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String result = "";
		BufferedReader in = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setReadTimeout(timeoutSec * 1000);
			int responsecode = conn.getResponseCode();
			if (responsecode == 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer lines = new StringBuffer(10000);
				String line;
				while ((line = in.readLine()) != null) {
					lines.append(line);
				}
				result = lines.toString();
			} else {
				throw new DataException("無法連接 URL : " + url.toString() + ", responsecode=" + responsecode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			readCount++;
			ArrayList<String> debugs = new ArrayList<String>();
			debugs.add("無法讀取的裁判機 url=" + url.toString());
			throw new DataException("無法讀取裁判機的資料。(" + url.getHost() + " " + e.getLocalizedMessage() + ")", debugs);
		}
		try {
			return new String(result.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new String(result.getBytes());
		}
	}

	/**
	 * 判斷 Server 端是否支援該語言。
	 * 
	 * @param language
	 * @return
	 * @throws AccessException
	 */
	public boolean getIsEnableCompiler(String language) {
		if (language == null) {
			return false;
		}
		for (Compiler compiler : this.serverConfig.getEnableCompilers()) {
			if (compiler.getLanguage().equals(language.trim())) {
				return true;
			}
		}
		return false;
	}

	private int readCount = 0;

	public void cleanReadCount() {
		readCount = 0;
	}


	/**
	 * reloadServerConfig 為了避免無限讀取無效的 server, 設置 readCount 為上限。<br/>
	 * 已經存在的話就不再重讀。
	 * 
	 * @return
	 */
	public ServerConfig getServerConfig() {
		if (this.serverConfig != null && !this.serverConfig.getIsNull()) {
			return this.serverConfig;
		}
		logger.info("serverConfig 重新讀取！ readCount=" + readCount);
		logger.info("this.serverConfig=" + this.serverConfig + " | this.serverConfig.getIsNull()="
				+ this.serverConfig.getIsNull() + " | this.serverConfig=" + (this.serverConfig != null));
		try {
			if (readCount <= 1) {
				readCount++;
				logger.severe(new Date(System.currentTimeMillis()) + " !!!!! serverConfig==null !!!!，重新讀取(#" + readCount
						+ ")。");
				ServerConfig serverConfig = ApplicationScope.getAppConfig().readServerConfig(serverUrl);
				ApplicationScope.getAppConfig().setServerConfig(serverConfig);
			} else {
				logger.severe(new Date(System.currentTimeMillis()) + " !!!!! serverConfig==newServerConfig !!!!，重新讀取(#"
						+ readCount + ")。");
				return new ServerConfig();
			}
		} catch (DataException e) {
			e.printStackTrace();
			return new ServerConfig();
		}

		Log log = new Log();
		log.setTabid(Log.TABID.WATCHING);
		log.setTitle("serverConfig 重新讀取成功！");
		log.setMessage("serverConfig=" + serverConfig);
		new LogDAO().insert(log);
		return this.serverConfig;
	}

	public boolean getCheckNopassRsync() {
		return this.getCheckNopassRsync(this.getServerUrl());
	}

	/**
	 * 透過 ssh 到遠端執行 ls 以確定是否有完成免密碼連線。
	 * 
	 * @return
	 */
	public boolean getCheckNopassRsync(URL serverUrl) {
		logger.info("serverurl=" + serverUrl);
		if (new IpAddress(serverUrl.getHost()).isLoopbackAddress()) {
			return true;
		}
		ServerConfig serverConfig = this.readServerConfig(serverUrl);
		AppConfig appConfig = ApplicationScope.getAppConfig();
		RunCommand run = new RunCommand();
		run.exec(RunCommand.Command.sudo + " -u " + appConfig.getRsyncAccount() + " " + RunCommand.Command.ssh + " -p "
				+ serverConfig.getSshport() + " " + serverConfig.getRsyncAccount() + "@" + serverUrl.getHost() + " \""
				+ RunCommand.Command.ls + "\"");
		return run.getErrStream().size() == 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getCheckAllowedIP(String urlstring) {
		URL url;
		try {
			url = new URL(urlstring);
			Logger.getAnonymousLogger().info("STEP1: urlstring= " + urlstring);
			Logger.getAnonymousLogger().info("STEP1: url.getPort()= " + url.getPort());
			Logger.getAnonymousLogger().info("STEP1: url.getDefaultPort()= " + url.getDefaultPort());
			Logger.getAnonymousLogger().info("STEP1: url.getAuthority()= " + url.getAuthority());
			Logger.getAnonymousLogger().info("STEP1: url.getProtocol()= " + url.getProtocol());
			Logger.getAnonymousLogger().info("STEP1: url.getHost()= " + url.getHost());
			Logger.getAnonymousLogger().info("STEP1: url.getUserInfo()= " + url.getUserInfo());
			Logger.getAnonymousLogger().info("STEP1: url.getContent()= " + url.getContent().toString());
			Logger.getAnonymousLogger().info("STEP1: url.getRef()= " + url.getRef());
			if (InetAddress.getByName(url.getHost()).isLoopbackAddress()) {
				return true;
			}
			if ("https".equals(url.getProtocol())) {
				HttpsURLConnection httpsconn = (HttpsURLConnection) url.openConnection();
				httpsconn.setRequestMethod("GET");
				httpsconn.connect();
				int httpscode = httpsconn.getResponseCode();
				Logger.getAnonymousLogger().info("STEP1: httpscode= " + httpscode);
				if (httpscode == 200) {
					return true;
				} else
					return false;

			} else {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();

				int code = connection.getResponseCode();
				Logger.getAnonymousLogger().info("STEP1: code= " + code);
				if (code == 200) {
					return true;
				}
				return false;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean getCheckLocker(URL url, String KEY) throws Exception {
		String seed = String.valueOf(Math.random());
		URLConnection conn;
		OutputStreamWriter wr;
		try {
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setReadTimeout(100 * 1000);
			wr = new OutputStreamWriter(conn.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataException("無法連接裁判機，可能是裁判機有誤或網路不通！請管理員檢查。", e);
		}
		wr.write("check=" + new DES(KEY).encrypt(seed));
		wr.flush();
		wr.close();

		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer lines = new StringBuffer(10000);
		String line;
		while ((line = in.readLine()) != null) {
			lines.append(line);
		}

		logger.info("已讀取：" + lines.toString());
		String newseed = new DES().decrypt(lines.toString());
		logger.info("newseed：" + newseed + ", check=" + seed.equals(newseed));
		return seed.equals(newseed);
	}

	public boolean getIsGoogleLoginSetup() {
		if (!new AppConfig().getClient_id().equals(this.getClient_id())
				&& !new AppConfig().getClient_secret().equals(this.getClient_secret())) {
			try {
				new URI(this.getRedirect_uri());
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	public LinkedHashSet<Class<? extends HttpServlet>> getPRIVILEGE_ContestManager() {
		return PRIVILEGE_ContestManager;
	}

//	public LinkedHashSet<Class<? extends HttpServlet>> getPRIVILEGE_GeneralManager() {
//		return PRIVILEGE_GeneralManager;
//	}

	public LinkedHashSet<Class<? extends HttpServlet>> getPRIVILEGE_ProblemManager() {
		return PRIVILEGE_ProblemManager;
	}

	public LinkedHashSet<Class<? extends HttpServlet>> getPRIVILEGE_VClassManager() {
		return PRIVILEGE_VClassManager;
	}

}
