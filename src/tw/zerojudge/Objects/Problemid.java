package tw.zerojudge.Objects;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import tw.jiangsir.Utils.Exceptions.DataException;

public class Problemid implements Comparable<Problemid>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1727108406217650826L;
	private String problemid = "";
	@JsonIgnore
	private boolean isNull = false;
	@JsonIgnore
	private boolean isLegal = true;
	@JsonIgnore
	private boolean isEmpty = false;

	public Problemid() {
	}
	public Problemid(String problemid) {
		if (problemid == null) {
			this.isNull = true;
			this.isEmpty = true;
			this.isLegal = false;
			return;
		}
		problemid = problemid.trim();
		if ("".equals(problemid)) {
			this.isEmpty = true;
			this.isNull = false;
			this.isLegal = false;
			return;
		}
		if (!problemid.toLowerCase().matches("[a-z]+[0-9][0-9][0-9]")) {
			this.isLegal = false;
			throw new DataException("題目編號格式有誤(" + problemid + ")！");
		}
		this.problemid = problemid;
	}

	public static Problemid parseProblemid(String problemid) {
		if (problemid == null) {
			throw new DataException("problemid = null, 無法解析。");
		}
		problemid = problemid.trim();
		if ("".equals(problemid)) {
			throw new DataException("problemid = 空字串, 無法解析。");
		}
		if (!problemid.toLowerCase().matches("[a-z]+[0-9][0-9][0-9]")) {
			throw new DataException("題目編號格式有誤(" + problemid + ")！");
		}
		return new Problemid(problemid);
	}

	public String getProblemid() {
		return problemid;
	}

	public void setProblemid(String problemid) {
		if (problemid == null) {
			return;
		}
		this.problemid = problemid;
	}

	public boolean getIsNull() {
		return isNull;
	}

	public void setIsNull(boolean isNull) {
		this.isNull = isNull;
	}

	public boolean getIsLegal() {
		return isLegal;
	}

	public void setIsLegal(boolean isLegal) {
		this.isLegal = isLegal;
	}

	public boolean getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	@Override
	public String toString() {
		return this.problemid;
	}

	@Override
	public int compareTo(Problemid p) {
		return this.toString().compareTo(p.toString());
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		/**
		 * Problemid 會需要作為 Hashtable 的 key 因此要 override hashCode
		 * 才能正確判斷。containsKey, get 等 method
		 */
		return this.toString().hashCode();
	}

}
