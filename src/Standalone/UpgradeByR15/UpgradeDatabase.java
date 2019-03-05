/**
 * idv.jiangsir.Upgrade - UpgradeDatabase.java
 * 2011/12/19 下午5:38:56
 * jiangsir
 */
package Standalone.UpgradeByR15;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.Factories.UserFactory;

/**
 * @author jiangsir
 * 
 */
public class UpgradeDatabase {
	private final int FROM_140908 = 140908;

	private final int FROM_120321 = 120321;

	String from_version;
	String to_version;

	public UpgradeDatabase(String from_version, String to_version) {
		this.from_version = from_version;
		this.to_version = to_version;
	}

	private int parseInt(String version) {
		String intversion = "";
		for (int i = 0; i < version.length(); i++) {
			if (version.charAt(i) - '0' <= 9 && version.charAt(i) - '0' >= 0) {
				intversion += version.charAt(i);
			}
		}
		return Integer.parseInt(intversion);
	}

	public void upgrade() throws AccessException {
		if (this.parseInt(from_version) == this.FROM_140908) {
			new Upgrade140908to141124().upgrade();
		} else {
			throw new AccessException(UserFactory.getNullOnlineUser(),
					"沒有進行任何升級！！(from = " + from_version + ", to= " + to_version
							+ ")");
		}
	}
}
