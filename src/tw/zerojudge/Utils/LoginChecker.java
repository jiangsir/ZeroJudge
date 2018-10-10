package tw.zerojudge.Utils;

import javax.servlet.ServletContext;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Objects.IpAddress;

public class LoginChecker {

	public LoginChecker(ServletContext context) {
	}

	public LoginChecker() {
	}

	public boolean isAllowedIP(IpAddress UserIP) {
		return UserIP
				.getIsSubnetOf(ApplicationScope.getAppConfig().getAllowedIP());
	}

}
