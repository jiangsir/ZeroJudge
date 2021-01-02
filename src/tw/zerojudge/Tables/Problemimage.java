package tw.zerojudge.Tables;

import java.io.InputStream;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import tw.zerojudge.Objects.Problemid;

/**
 * @author jiangsir
 * 
 */
@JsonIgnoreProperties(value = {"file"})
public class Problemimage {
	private int id = 0;
	private Problemid problemid = new Problemid("");
	private String filename = "";
	private String filetype = "";
	private int filesize = 0;
	private InputStream file;
	private byte[] filebytes;
	private String descript = "";
	private boolean visible = true;

	public Problemimage() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Problemid getProblemid() {
		return problemid;
	}

	@JsonIgnore
	public void setProblemid(String problemid) {
		if (problemid == null) {
			return;
		}
		this.setProblemid(new Problemid(problemid));
	}
	public void setProblemid(Problemid problemid) {
		this.problemid = problemid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		if (filetype != null) {
			this.filetype = filetype;
		}
	}

	public int getFilesize() {
		return filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public byte[] getFilebytes() {
		return filebytes;
	}

	public void setFilebytes(byte[] filebytes) {
		this.filebytes = filebytes;
	}

}
