package Standalone.Upgrade201307;

import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.DAOs.UserDAO;
import tw.zerojudge.DAOs.UserService;
import tw.zerojudge.Tables.User;

public class UpgradeUsers {

	UserDAO userDao;
	Logger logger = Logger.getLogger(UpgradeUsers.class.getName());
	ObjectMapper mapper = new ObjectMapper(); 

	static enum ACTION {
		upgradeAllUsers("升級 Users 的資料結構 aclist"), //
		preloadAllUsers("preload 所有User，不做任何資料更動。僅用於測試執行速度。"), //
		exit("退出。");
		private String value;

		private ACTION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

	public UpgradeUsers(UserDAO userDao) {
		this.userDao = userDao;
	}

	public void preloadAllUsers() {
		this.userDao.getAllUsers();
	}

	public void upgradeAllUsers() {
		for (User user : this.userDao.getAllUsers()) {
			try {
				new UserService().update(user);
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
	}
}
