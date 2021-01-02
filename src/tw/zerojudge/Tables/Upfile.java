/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Tables;

import java.io.Serializable;
import java.sql.Timestamp;
import tw.jiangsir.Utils.Annotations.Persistent;

/**
 * @author jiangsir
 * 
 */
public class Upfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Persistent(name = "id")
	private Integer id = 0;
	@Persistent(name = "filename")
	private String filename = "";
	@Persistent(name = "filetype")
	private String filetype = "";
	@Persistent(name = "bytes")
	private byte[] bytes;
	@Persistent(name = "ipfrom")
	private String ipfrom = "";
	@Persistent(name = "timestamp")
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	@Persistent(name = "solutionid")
	private Integer solutionid = 0;

	public Upfile() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		this.filetype = filetype;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getIpfrom() {
		return ipfrom;
	}

	public void setIpfrom(String ipfrom) {
		this.ipfrom = ipfrom;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getSolutionid() {
		return solutionid;
	}

	public void setSolutionid(Integer solutionid) {
		this.solutionid = solutionid;
	}

	public String getFilesize() {
		int size = this.getBytes().length;
		String filesize = "";
		if (size < 1024) {
			filesize = size + "bytes";
		} else if (size < 1024 * 1024) {
			filesize = size / 1024 + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			filesize = size / 1024 / 1024 + "MB";
		}
		return filesize;
	}

}
