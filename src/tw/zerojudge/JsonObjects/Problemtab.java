package tw.zerojudge.JsonObjects;

/**
 * @author jiangsir
 * 
 */
public class Problemtab {
	private String id = "BASIC";
	private String name = "基礎題庫";
	private String descript = "基本語法題目";
	private String orderby = "problemid ASC";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	@Override
	public String toString() {
		return this.getId() + ":" + this.getName();
	}

}
