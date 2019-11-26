package tw.jiangsir.Utils.Listeners.Tasks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.jiangsir.Utils.Scopes.SessionScope;
import tw.zerojudge.Configs.AppConfig;
import tw.zerojudge.DAOs.AppConfigService;
import tw.zerojudge.DAOs.LogDAO;
import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Tables.Log;

public class BannedTask extends TimerTask {
	Logger logger = Logger.getLogger(this.getClass().getName());


	@Override
	public void run() {
		try {
			logger.info(this.getClass().getSimpleName() + " Running...."
					+ new Timestamp(System.currentTimeMillis()));
			TreeMap<IpAddress, ArrayList<SessionScope>> ipTreeMap = this
					.builtIPTreeMap();
			logger.info(this.getSortedIPlist(ipTreeMap).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TreeMap<IpAddress, ArrayList<SessionScope>> builtIPTreeMap() {
		AppConfig appConfig = ApplicationScope.getAppConfig();
		TreeMap<IpAddress, ArrayList<SessionScope>> iptreeMap = new TreeMap<IpAddress, ArrayList<SessionScope>>();
		for (HttpSession session : ApplicationScope.getOnlineSessions()
				.values()) {
			try {
				session.getCreationTime();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			SessionScope sessionScope = new SessionScope(session);
			List<IpAddress> session_ipset = sessionScope.getSession_ipset();
			for (IpAddress session_ip : session_ipset) {
				if (session_ip == null || session_ip.getIp() == null) {
					continue;
				}
				if (iptreeMap.containsKey(session_ip)) {
					int size = iptreeMap.get(session_ip).size();
					if (size >= appConfig.getMaxConnectionByIP()
							&& !iptreeMap.containsKey(session_ip)) {
						appConfig.getBannedIPSet().add(session_ip);
						new AppConfigService().update(appConfig);

						Log log = new Log();
						log.setTabid(Log.TABID.BANNED);
						log.setTitle("封鎖" + session_ip + " (" + size + ")");
						StringBuffer message = new StringBuffer(65535);
						for (SessionScope ipsessionScope : iptreeMap
								.get(session_ip)) {
							message.append(ipsessionScope + "\n");
						}
						log.setMessage(message.toString());
						new LogDAO().insert(log);
					}
					ArrayList<SessionScope> s = iptreeMap.get(session_ip);
					s.add(sessionScope);
					iptreeMap.put(session_ip, s);
				} else {
					ArrayList<SessionScope> s = new ArrayList<SessionScope>();
					s.add(sessionScope);
					iptreeMap.put(session_ip, s);
				}
			}
		}
		return iptreeMap;
	}

	/**
	 * 只顯示 最多連線的 20 筆
	 * 
	 * @param ipTreeMap
	 * @return
	 */
	public List<Map.Entry<IpAddress, ArrayList<SessionScope>>> getSortedIPlist(
			TreeMap<IpAddress, ArrayList<SessionScope>> ipTreeMap) {
		int max = 20;
		ArrayList<Map.Entry<IpAddress, ArrayList<SessionScope>>> list = new ArrayList<Map.Entry<IpAddress, ArrayList<SessionScope>>>(
				ipTreeMap.entrySet());
		Collections
				.sort(list,
						new Comparator<Map.Entry<IpAddress, ArrayList<SessionScope>>>() {
							public int compare(
									Entry<IpAddress, ArrayList<SessionScope>> o1,
									Entry<IpAddress, ArrayList<SessionScope>> o2) {
								return Integer.valueOf(o2.getValue().size())
										.compareTo(o1.getValue().size());
							}
						});
		return list.size() < max ? list : list.subList(0, max);
	}
}
