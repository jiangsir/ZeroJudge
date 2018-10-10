package tw.jiangsir.Utils.Exceptions;

import tw.zerojudge.Objects.IpAddress;

public class IpException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum TITLE {
		YOUR_IP_WAS_BANNED; 
	}

	public IpException(String title, IpAddress bannedIP) {
		super(title, new Alert(Alert.TYPE.IPERROR, title,
				IpException.class.getSimpleName(),
				"您的位置(" + bannedIP + ")無法進入", null, null));
	}


}
