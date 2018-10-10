/**
 * idv.jiangsir.objects - User.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import tw.jiangsir.Utils.Annotations.Persistent;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.jiangsir.Utils.Tools.StringTool;

/**
 * @author jiangsir
 * 
 */
public class School {
	public enum ID {
		NOTSTUDENT; 
	}

	@Persistent(name = "id")
	private Integer id = ID.NOTSTUDENT.ordinal();
	@Persistent(name = "url")
	private URL url = null;
	@Persistent(name = "schoolname")
	private String schoolname = "";
	@Persistent(name = "imgsrc")
	private String imgsrc = "";
	@Persistent(name = "descript")
	private String descript = "";

	public static final int CHECKID_CHECKED = 1;
	public static final int CHECKID_NONECHECK = 0;
	@Persistent(name = "checkid")
	private Integer checkid = School.CHECKID_NONECHECK;


	public School() {
	}

	public boolean isNullSchool() {
		if (new School().getId().equals(this.getId()) && this.getUrl() == null) {
			return true;
		}
		return false;
	}

	public boolean getIsNull() {
		School nullSchool = new School();
		if (nullSchool.getId().equals(this.getId()) && nullSchool.getUrl() == this.getUrl()
				&& nullSchool.getSchoolname().equals(this.getSchoolname())) {
			return true;
		}
		return false;
	}

	/** ******************************************************************** */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		if (descript == null) {
			return;
		}
		this.descript = StringTool.escapeScriptStyle(descript);
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		if (imgsrc == null) {
			return;
		}
		this.imgsrc = StringTool.escapeScriptStyle(imgsrc);
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) throws DataException {
		if (schoolname == null) {
			return;
		}
		schoolname = StringTool.escapeHtmlTag(schoolname);
		if (schoolname == null || "".equals(schoolname)) {
			throw new DataException("學校名稱不可為空或含有 html 標籤！");
		}
		this.schoolname = schoolname;
	}


	public void setUrl(String url) {
		if (url == null) {
			return;
		}
		try {
			this.setUrl(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().startsWith("no protocol")) {
				this.setUrl("http://" + url);
			} else {
				throw new DataException(e);
			}
		}
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Integer getCheckid() {
		return checkid;
	}

	public void setCheckid(Integer checkid) {
		this.checkid = checkid;
	}

	public void setCheckid(String checkid) {
		if (checkid == null || "".equals(checkid)) {
			this.setCheckid(CHECKID_NONECHECK);
			return;
		}
		if (!checkid.matches("[0-9]+")) {
			throw new DataException("資料錯誤！ checkid 必須為數字。");
		}
		this.setCheckid(Integer.parseInt(checkid));
	}

	public boolean getIsChecked() {
		return this.getCheckid() == CHECKID_CHECKED;
	}

	public boolean islegalSchoolurl(String schoolurl) {
		URL url;
		try {
			url = new URL(schoolurl);
			URLConnection urlconn = url.openConnection();
			try {
				urlconn.connect();
				return true;
			} catch (Exception e) {
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

}
