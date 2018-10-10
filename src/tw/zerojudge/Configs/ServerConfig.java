package tw.zerojudge.Configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.JsonObjects.Compiler;
import tw.zerojudge.Objects.Problemid;

public class ServerConfig {
	private Compiler[] Compilers = new Compiler[] {};
	private File CONSOLE_PATH = new File("/JudgeServer_CONSOLE/");
	private File TestdataPath = null;
	private File BinPath = null;
	private File CompilerPath = null;
	private File SpecialPath = null;
	private File ExecutablePath = null;
	private int JVM_MB = 512;
	private String servername = "ZeroJudgeServer";
	private String serverOS = "Debian"; 
	private String serverInfo = ""; 
	private String rsyncAccount = "zero";
	private int sshport = 22;

	ObjectMapper mapper = new ObjectMapper(); 

	public Compiler[] getCompilers() {
		return Compilers;
	}

	/**
	 * 取得目前開放使用的 Compilers
	 * 
	 * @return
	 */
	@JsonIgnore
	public ArrayList<Compiler> getEnableCompilers() {
		ArrayList<Compiler> compilers = new ArrayList<Compiler>();
		for (Compiler compiler : getCompilers()) {
			if (compiler.getLanguage().equals(compiler.getEnable())) {
				compilers.add(compiler);
			}
		}
		return compilers;
	}

	public void setCompilers(Compiler[] compilers) {
		Compilers = compilers;
	}

	public File getCONSOLE_PATH() {
		return CONSOLE_PATH;
	}

	public void setCONSOLE_PATH(File cONSOLE_PATH) {
		CONSOLE_PATH = cONSOLE_PATH;
	}

	public int getJVM_MB() {
		return JVM_MB;
	}

	public void setJVM_MB(int jVM_MB) {
		JVM_MB = jVM_MB;
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

	public int getSshport() {
		return sshport;
	}

	public void setSshport(int sshport) {
		this.sshport = sshport;
	}

	@JsonIgnore
	public void setSshport(String sshport) {
		if (sshport == null || !sshport.matches("[0-9]+")) {
			return;
		}
		this.sshport = Integer.parseInt(sshport);
	}


	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getServerOS() {
		return serverOS;
	}

	public void setServerOS(String serverOS) {
		this.serverOS = serverOS;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}

	public File getTestdataPath() {
		return TestdataPath;
	}

	public File getTestdataPath(Problemid problemid) {
		return new File(this.getTestdataPath(), problemid + File.separator);
	}

	public void setTestdataPath(File testdataPath) {
		TestdataPath = testdataPath;
	}

	public File getBinPath() {
		return BinPath;
	}

	public void setBinPath(File binPath) {
		BinPath = binPath;
	}

	public File getCompilerPath() {
		return CompilerPath;
	}

	public void setCompilerPath(File compilerPath) {
		CompilerPath = compilerPath;
	}

	public File getSpecialPath() {
		return SpecialPath;
	}

	public File getSpecialPath(Problemid problemid) {
		return new File(this.getSpecialPath(), problemid + File.separator);
	}

	public void setSpecialPath(File specialPath) {
		SpecialPath = specialPath;
	}

	public File getExecutablePath() {
		return ExecutablePath;
	}

	public void setExecutablePath(File executablePath) {
		ExecutablePath = executablePath;
	}

	/**
	 * 判斷是否有成功讀取 Server Config 資訊。
	 * 
	 * @return
	 */
	public boolean getIsNull() {
		ServerConfig newServerConfig = new ServerConfig();
		if (newServerConfig.getServername().equals(this.getServername())
				&& newServerConfig.getServerOS().equals(this.getServerOS())
				&& newServerConfig.getServerInfo().equals(this.getServerInfo())
				&& newServerConfig.getCompilers().length == this.getCompilers().length) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
