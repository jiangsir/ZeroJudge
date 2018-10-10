package tw.zerojudge.Objects;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TreeSet;

import org.codehaus.jackson.annotate.JsonIgnore;

public class IpAddress implements Comparable<IpAddress>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4512555348283990017L;
	private InetAddress inet = null;
	private int cidr = 32;

	public static enum CIDR {
		ALL, 
		A, 
		B, 
		C, 
		ONE, 
	}

	/**
	 * 預設取得 loopback address
	 */
	public IpAddress() {
		this.inet = InetAddress.getLoopbackAddress();
	}

	/**
	 * 接受 192.168.1.1 or 192.168.1.1/24 兩種格式。
	 * 
	 * @param ipcidr
	 */
	public IpAddress(String ipcidr) {
		if (ipcidr == null || "".equals(ipcidr.trim()) || "null".equals(ipcidr.trim())) {
			return;
		}
		ipcidr = ipcidr.trim();
		if (ipcidr.contains("/")) {
			String[] array = ipcidr.split("/");
			try {
				this.setIp(InetAddress.getByName(array[0].trim()));
				this.setCidr(Integer.parseInt(array[1].trim()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			try {
				this.setIp(InetAddress.getByName(ipcidr.trim()));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param ip
	 * @param cidr
	 *            代表 mask 有多少個 1, cidr==0 代表所有 ip
	 */
	public IpAddress(String ip, int cidr) {
		if (ip == null || "".equals(ip.trim()) || "null".equals(ip.trim())) {
			return;
		}
		try {
			this.setIp(InetAddress.getByName(ip.trim()));
			this.setCidr(cidr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ip
	 * @param cidr
	 *            代表 mask 有多少個 1, cidr==0 代表所有 ip
	 */
	public IpAddress(String ip, CIDR cidr) {
		if (ip == null || "".equals(ip.trim()) || "null".equals(ip.trim())) {
			return;
		}
		try {
			this.setIp(InetAddress.getByName(ip.trim()));
			this.setCidr(cidr.ordinal() * 8);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public InetAddress getIp() {
		return inet;
	}

	public void setIp(InetAddress ip) {
		this.inet = ip;
	}

	public int getCidr() {
		return cidr;
	}

	public void setCidr(int cidr) {
		if (cidr >= 0 && cidr <= 32) {
			this.cidr = cidr;
		}
	}

	/**
	 * 必須存在
	 */
	public void setIsIpv4() {

	}
	/**
	 * 必須存在
	 */
	public void setIsIpv6() {

	}
	@JsonIgnore
	public boolean getIsIpv4() {
		return this.getIp() instanceof Inet4Address;
	}

	@JsonIgnore
	public boolean getIsIpv6() {
		return this.getIp() instanceof Inet6Address;
	}

	private int toInt() {
		byte[] address = this.getIp().getAddress();
		int net = 0;
		for (byte addres : address) {
			net <<= 8;
			net |= addres & 0xFF;
		}
		return net;
	}

	private int getMask() {
		return 0x80000000 >> cidr - 1;
	}

	/**
	 * 判斷這個 ip 是否是 ipAddress 的子網域
	 * 
	 * @param ipgroup
	 * @return
	 */
	public boolean getIsSubnetOf(IpAddress ipgroup) {
		if (ipgroup.cidr == 0) {
			return true;
		}
		return (this.toInt() & ipgroup.getMask()) == (ipgroup.toInt() & ipgroup.getMask());
	}

	/**
	 * 判斷這個 IP 是否屬於 一個 network 如：192.168.*.*, 203.1.2.3
	 * 
	 * @param iprules
	 * @return
	 */
	public boolean getIsSubnetOf(TreeSet<IpAddress> iprules) {
		if (iprules == null || iprules.size() == 0 || this.isLoopbackAddress()) {
			return true;
		}
		for (IpAddress ip : iprules) {
			if (this.getIsSubnetOf(ip)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		if (inet == null) {
			return "0.0.0.0";
		}
		return cidr == 32 ? inet.getHostAddress() : inet.getHostAddress() + "/" + cidr;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) {
			return false;
		}
		return this.toString().equals(arg0.toString());
	}

	@Override
	public int compareTo(IpAddress o) {
		if (o == null || o.getIp() == null) {
			return -1;
		}
		return this.getIp().toString().compareTo(o.getIp().toString());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	@JsonIgnore
	public boolean isLoopbackAddress() {
		return inet.isLoopbackAddress();
	}

}
