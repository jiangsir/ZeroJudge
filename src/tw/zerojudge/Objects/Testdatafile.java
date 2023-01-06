package tw.zerojudge.Objects;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import tw.jiangsir.Utils.Scopes.ApplicationScope;

public class Testdatafile extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9087389801460266450L;

	public enum SUFFIX {
		in, 
		out, 
		ans, 
		OTHER;
	}
	private Problemid problemid = null;
	final public int MAX_INDEX = 25;
	private int index = 0; 
	private SUFFIX suffix = SUFFIX.in;
	private String summary = "";

	public Testdatafile(File parent) {
		super(parent, "");
		if (this.getName().endsWith(".in")) {
			this.setSuffix(SUFFIX.in);
		} else if (this.getName().endsWith(".out")) {
			this.setSuffix(SUFFIX.out);
		} else if (this.getName().endsWith(".ans")) {
			this.setSuffix(SUFFIX.ans);
		} else {
			this.setSuffix(SUFFIX.OTHER);
		}
		if (this.getName().contains(".")) {
			String[] name = this.getName().split("\\.");
			if (name[0].contains("_")) {
				this.setProblemid(new Problemid(name[0].split("_")[0]));
				this.setIndex(Integer.parseInt(name[0].split("_")[1]));
			} else {
				this.setProblemid(new Problemid(name[0]));
				this.setIndex(0);
			}
		}

		try {
			this.setSummary("(" + FileUtils.readLines(this).size() + " lines, " + this.length() + " Bytes)");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Problemid getProblemid() {
		return problemid;
	}

	public void setProblemid(Problemid problemid) {
		this.problemid = problemid;
	}

	public SUFFIX getSuffix() {
		return suffix;
	}

	public void setSuffix(SUFFIX suffix) {
		this.suffix = suffix;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getRange() {
		if (this.length() < 1024) {
			return "<1K";
		} else if (this.length() < 1024 * 1024) {
			return "<1M";
		} else if (this.length() < 10 * 1024 * 1024) {
			return "<10M";
		} else if (this.length() < 50 * 1024 * 1024) {
			return "<50M";
		}
		return ">50M";
	}

	public boolean getIsInfile() {
		return this.getSuffix() == SUFFIX.in;
	}
	public boolean getIsOutfile() {
		return this.getSuffix() == SUFFIX.out;
	}

	/**
	 * 取得一部分測資，作為預覽用。
	 * 
	 * @return
	 */
	public String getData() {
		int MAX_SIZE = ApplicationScope.getAppConfig().getMaxCodeLength();
		try {
			String data = FileUtils.readFileToString(this);
			if (data.length() >= MAX_SIZE) {
				return FileUtils.readFileToString(this).substring(0, MAX_SIZE - 1) + "\n超過 " + MAX_SIZE
						+ " Bytes，以下略過.....";
			} else {
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
	}

	/**
	 * 取得完整 testdata
	 * 
	 * @return
	 */
	public String getFullData() {
		try {
			return FileUtils.readFileToString(this);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public Date getLastModified() {
		return new Date(this.lastModified());
	}

	public String getDownloadName() {
		DecimalFormat formatter = new DecimalFormat("00");
		if (this.getIsInfile()) {
			return problemid + "_" + formatter.format(this.getIndex()) + "." + SUFFIX.in;
		} else if (this.getIsOutfile()) {
			return problemid + "_" + formatter.format(this.getIndex()) + "." + SUFFIX.out;
		}
		return "";
	}
}
