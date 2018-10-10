package tw.zerojudge.Utils;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import tw.jiangsir.Utils.Exceptions.DataException;
import tw.zerojudge.Objects.IpAddress;

public class Utils {

	public String randomPassword() {
		final String code = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuffer passwd = new StringBuffer(10);
		for (int i = 0; i < 8; i++) {
			passwd.append(code.charAt((int) (Math.random() * code.length())));
		}
		return passwd.toString();
	}

	/**
	 * 將 LinkedHashSet 轉成 CSV 字串
	 * 
	 * @param set
	 * @return
	 */
	public String LinkedHashSet2CSV(LinkedHashSet<String> set) {
		StringBuffer result = new StringBuffer(5000);
		for (String s : set) {
			result.append(result.length() == 0 ? s : ", " + s.trim());
		}
		return result.toString();
	}

	/**
	 * 將 CSV 字串 轉成 LinkedHashSet
	 * 
	 * @param csv
	 * @return
	 */
	public LinkedHashSet<String> CSV2LinkedHashSet(String csv) {
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		for (String s : csv.split(",")) {
			set.add(s.trim());
		}
		return set;
	}

	/**
	 * 將 CSV 字串 轉成 TreeSet
	 * 
	 * @param csv
	 * @return
	 */
	public TreeSet<String> CSV2TreeSet(String csv) {
		TreeSet<String> set = new TreeSet<String>();
		for (String s : csv.split(",")) {
			set.add(s.trim());
		}
		return set;
	}

//	/**
//	 * 將 set 自動轉出的字串轉回 Set。字串格式如下：["abc", "def"]
//	 * 
//	 * @param setstring
//	 * @return
//	 */
//	public LinkedHashSet<String> String2LinkedHashSet(String setstring) {
//		LinkedHashSet<String> set = new LinkedHashSet<String>();
//		if (setstring.matches("^\\[.*\\]$")) {
//			setstring = setstring.replaceAll("\\[", "");
//			setstring = setstring.replaceAll("\\]", "");
//		}
//		if (setstring.trim().equals("")) {
//			return set;
//		}
//		for (String s : setstring.split(",")) {
//			s = s.replaceAll("\"", "");
//			set.add(s.trim());
//		}
//		return set;
//	}


	/**
	 *  還原 Arrays.toString 自動轉出的字串 String[]。字串格式如下：[aa, bb, cc]
	 * 
	 * @param setstring
	 * @return
	 */
	public String[] String2Array(String string) {
		if (string.matches("^\\[.*\\]$")) {
			string = string.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
		}
		if (string.equals("")) {
			return new String[] {};
		}
		String[] array = string.split(",");
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
		return array;
	}

	/**
	 *  還原 ArrayList.toString 自動轉出的字串。字串格式如下：[aa, bb, cc]
	 * 
	 * @param liststring
	 * @return
	 */
	public ArrayList<String> String2StringList(String string) {
		if (string.matches("^\\[.*\\]$")) {
			string = string.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
		}
		if (string.equals("")) {
			return new ArrayList<String>();
		}
		ArrayList<String> list = new ArrayList<String>();
		for (String s : string.split(",")) {
			list.add(s.trim());
		}
		return list;
	}

	/**
	 *  還原 ArrayList.toString 自動轉出的字串。字串格式如下：[1, 2, 1]
	 * 
	 * @param string
	 * @return
	 */
	public ArrayList<Integer> String2IntegerList(String string) {
		if (string.matches("^\\[.*\\]$")) {
			string = string.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
		}
		if (string.equals("")) {
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (String s : string.split(",")) {
			list.add(Integer.parseInt(s.trim()));
		}
		return list;
	}

	/**
	 * 還原 Arrays.toString 自動轉出的字串轉回 int[]。字串格式如下：[1, 2, 3]
	 * 
	 * @param setstring
	 * @return
	 */
	public int[] String2IntArray(String intstring) throws NumberFormatException {
		if (intstring.matches("^\\[.*\\]$")) {
			intstring = intstring.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
		}
		if (intstring.equals("")) {
			return new int[] {};
		}
		String[] sarray = intstring.split(",");
		int[] ints = new int[sarray.length];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = Integer.parseInt(sarray[i].trim());
		}
		return ints;
	}

	/**
	 * 還原 Arrays.toString 自動轉出的字串轉回 double[]。字串格式如下：[1.0, 2.0, 3.0]
	 * 
	 * @param doublestring
	 * @return
	 */
	public double[] String2DoubleArray(String doublestring) throws NumberFormatException {
		if (doublestring.matches("^\\[.*\\]$")) {
			doublestring = doublestring.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
		}
		if (doublestring.equals("")) {
			return new double[] {};
		}
		String[] sarray = doublestring.split(",");
		double[] doubles = new double[sarray.length];
		for (int i = 0; i < doubles.length; i++) {
			doubles[i] = Double.parseDouble(sarray[i].trim());
		}
		return doubles;
	}

	/**
	 * 路徑容錯。要處理連續 
	 */
	public String parsePath(String path) {
		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			path = path + System.getProperty("file.separator"); 
			Matcher m = Pattern.compile("['" + System.getProperty("file.separator") + "']+").matcher(path);
			return m.replaceAll(System.getProperty("file.separator"));
		} else {
			path = System.getProperty("file.separator") + path + System.getProperty("file.separator"); 
			Matcher m = Pattern.compile("['" + System.getProperty("file.separator") + "']+").matcher(path);
			return m.replaceAll(System.getProperty("file.separator"));
		}
	}

	public HashMap<String, String> getRequestHeaders(HttpServletRequest request) {
		HashMap<String, String> headers = new HashMap<String, String>();
		Enumeration<?> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			headers.put(name, request.getHeader(name));
		}
		return headers;
	}

	/**
	 * 获得客户端真实IP地址的方法二：
	 * 
	 * @param request
	 * @return
	 */

	/**
	 * 透過 request 取得遠端 IP
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList<IpAddress> getIpList(HttpServletRequest request) {
		ArrayList<IpAddress> ipSet = new ArrayList<IpAddress>();
		ipSet.add(new IpAddress(request.getRemoteAddr()));
		return ipSet;
	}


	//

	public String treeremove(String orig, String remove) {
		orig = orig.replaceAll(" ", "");
		remove = remove.replaceAll(" ", "");
		String[] origarray = orig.split(",");
		String[] removearray = remove.split(",");

		TreeSet<String> set = new TreeSet<String>();
		for (int i = 0; i < origarray.length; i++) {
			if (!"".equals(origarray[i].trim())) {
				set.add(origarray[i].trim());
			}
		}
		for (int i = 0; i < removearray.length; i++) {
			set.remove(removearray[i].trim());
		}
		Iterator<String> iterator = set.iterator();
		String result = "";
		while (iterator.hasNext()) {
			if ("".equals(result)) {
				result += iterator.next();
			} else {
				result += ", " + iterator.next();
			}
		}
		return result;
	}

	/**
	 * 處理 以逗點分割的 String 的串接。以 Tree Set 實作，因此不會有重復值<br>
	 * problemid, 及 privilege 使用
	 * 
	 * @return
	 */
	public String treemerge(String first, String second) {
		first = first.replaceAll(" ", "");
		second = second.replaceAll(" ", "");
		String[] firstarray = first.split(",");
		String[] secondarray = second.split(",");
		TreeSet<String> set = new TreeSet<String>();
		for (int i = 0; i < firstarray.length; i++) {
			if (!"".equals(firstarray[i].trim())) {
				set.add(firstarray[i].trim());
			}
		}
		for (int i = 0; i < secondarray.length; i++) {
			if (!"".equals(secondarray[i].trim())) {
				set.add(secondarray[i].trim());
			}
		}
		Iterator<String> iterator = set.iterator();
		String result = "";
		while (iterator.hasNext()) {
			if ("".equals(result)) {
				result += iterator.next();
			} else {
				result += ", " + iterator.next();
			}
		}
		return result;
	}

	public boolean isLegalDatestring(String datestring) throws DataException {
		if (datestring == null || !datestring.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")) {
			throw new DataException("時間格式錯誤！");
		} else {
			return true;
		}
	}


	/**
	 * 驗證 account 是否合法
	 * 
	 * @param account
	 * @return
	 */


	/**
	 * 所有要 SQL 指令都要經過處理, 以避免 SQL Injection 攻擊
	 * 
	 * @param s
	 * @return
	 */

	/**
	 * 從 sql 取出後, 再改回原狀才能正常顯示
	 * 
	 * @param s
	 * @return
	 */

	/**
	 * 寫入資料庫的資料如果需要用 HTML 的方式顯示, 許多特殊符號就必須轉換才行
	 * 
	 * @param content
	 * @return
	 */

	/**
	 * 判斷檔案是否存在
	 * 
	 * @param path
	 * @return
	 */

	public boolean isFileExist(String path, String filename) {
		return new File(path, filename).exists();
	}


	/**
	 * 刪除檔案
	 * 
	 * @param path
	 */

	//

	/**
	 * 刪除資料夾底下的一些由 regex 指定出來的檔案
	 * 
	 * @param path
	 * @param regex
	 */
	public void delRegexFiles(String path, String regex) {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; files != null && i < files.length; i++) {
				delRegexFiles(files[i].getPath(), regex);
			}
		} else if (file.exists() && file.getPath().matches(regex)) {
			if (file.delete()) {
			} else {
			}
		}
	}

	/**
	 * 取得指定目錄下的檔案, 不含目錄
	 */
	public TreeSet<String> getTestfilenames(String path, String regex) {
		File file = new File(path);
		TreeSet<String> fileList = new TreeSet<String>();
		if (!file.exists()) {
			return fileList;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filestring = files[i].toString();
			if (!files[i].isDirectory() && filestring.matches(regex)) { 
				String filename = filestring
						.substring(filestring.lastIndexOf(System.getProperty("file.separator")) + 1);
				fileList.add(filename);
			}
		}
		return fileList;
	}

	/**
	 * 回傳字串在目標陣列的位置。 若存在則回傳 < 0 的數
	 * 
	 * @param array
	 * @param s
	 * @return
	 */
	public int indexOf(String[] array, String s) {
		if (array == null || s == null || array.length == 0 || "".equals(s)
				|| (array.length == 1 && "".equals(array[0]))) {
			return -1;
		}
		for (int i = 0; i < array.length; i++) {
			if (s.trim().equals(array[i].trim())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 回傳字串 s 是否 match 目標陣列裡的字串。 若與陣列內的字串 match 則回傳其 index
	 * 
	 * @param array
	 * @param s
	 * @return
	 */
	public int matches(String[] array, String s) {
		if (array == null || s == null || array.length == 0) {
			return -1;
		}
		for (int i = 0; i < array.length; i++) {
			if (s.trim().matches(array[i].trim())) {
				return i;
			}
		}
		return -1;
	}

	//

	/**
	 * 
	 * @param iprule
	 * @param currentip
	 * @return
	 * @deprecated
	 */
	private boolean isIpInRule(String iprule, IpAddress currentip) {
		iprule = iprule.trim();
		if (iprule == null || "".equals(iprule)) {
			return false;
		}
		if ("*".equals(iprule) || "127.0.0.1".equals(currentip) || currentip.getIp().isLoopbackAddress()) {
			return true;
		}
		String[] subip = iprule.split("\\.");
		String[] curriparray = currentip.toString().split("\\.");
		if (subip.length != 4 || curriparray.length != 4) {
			return false;
		}
		for (int i = 0; i < subip.length; i++) {
			if (!"*".equals(subip[i]) && !subip[i].equals(curriparray[i])) {
				return false;
			}
		}
		return true;
	}

	public static final int rule_DENY = -1;
	public static final int rule_NOTDEFINE = 0;
	public static final int rule_ALLOW = 1;

	public enum RULE {
		DENY, 
		NOTDEFINE, 
		ALLOW;
	}

	/**
	 * 20100202 <br>
	 * 判斷Contest 的參與名單<br>
	 * <br>
	 * 判定規則為：<br>
	 * 1. 越靠右邊優先權越高，蓋過左邊的設定 <br>
	 * 2. 允許 regex 寫法。<br>
	 * 3. null, "" 均回覆 NOTDEFINE
	 * 
	 * @param rulecvs
	 * @param item
	 * @return
	 */
	public RULE parseRULE(LinkedHashSet<String> rules, String item) {
		if (rules == null) {
			return RULE.NOTDEFINE;
		}
		RULE result = RULE.NOTDEFINE;
		for (String rule : rules) {
			if (rule.contains("#")) {
				rule = rule.substring(0, rule.indexOf("#"));
			}
			if (rule.startsWith("!")) {
				if (rule.equals("!" + item) || new String("!" + item).matches(rule)) {
					result = RULE.DENY;
				}
			} else {
				if (rule.equals(item) || item.matches(rule)) {
					result = RULE.ALLOW;
				}
			}
		}
		return result;
	}

	/**
	 * 20100202 <br>
	 * 判斷Contest 的參與名單<br>
	 * <br>
	 * 判定規則為：<br>
	 * 1. 越靠右邊優先權越高，蓋過左邊的設定 <br>
	 * 2. 允許 regex 寫法。<br>
	 * 3. null, "" 均回覆 NOTDEFINE
	 * 
	 * @param rulecvs
	 * @param item
	 * @return
	 */
	public int parseRule(LinkedHashSet<String> rules, String item) {
		if (rules == null) {
			return Utils.rule_NOTDEFINE;
		}
		int result = Utils.rule_NOTDEFINE;
		for (String rule : rules) {
			if (rule.contains("#")) {
				rule = rule.substring(0, rule.indexOf("#"));
			}
			if (rule.startsWith("!")) {
				if (rule.equals("!" + item) || new String("!" + item).matches(rule)) {
					result = Utils.rule_DENY;
				}
			} else {
				if (rule.equals(item) || item.matches(rule)) {
					result = Utils.rule_ALLOW;
				}
			}
		}
		return result;
	}

	/**
	 * 分析 權限規則
	 * 
	 * @param rule
	 * @param string
	 * @return
	 */
	//

	/**
	 * 讀取 URL HTML 並回傳 ArrayList
	 * 
	 * @param URL
	 * @return
	 */
	public ArrayList<String> readURL(String urlstring) {
		ArrayList<String> buffer = new ArrayList<String>();
		try {
			URL url = new URL(urlstring);
			URLConnection conn = url.openConnection();
			InputStream stream = conn.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = in.readLine()) != null) {
				buffer.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return buffer;
		}
		return buffer;
	}

	/**
	 * 從 URL 取得檔案的 InputStream
	 * 
	 * @param url
	 * @return
	 */
	public InputStream getInputStream(URL url) {
		URLConnection conn;
		try {
			conn = url.openConnection();
			return conn.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}





	/**
	 * 濾掉 querystring 內的 page 參數
	 * 
	 * @param querystring
	 * @return
	 */
	//

	public boolean isDigits(String s) {
		if (s == null || "".equals(s.trim())) {
			return false;
		}
		return s.matches("[\\d]+");
	}

	/**
	 * 將 exception 的 stacktrace 做成 string, 以方便寫入 errorlog
	 * 
	 * @param e
	 * @return
	 */
	public String printStackTrace(Exception e, String sql) {
		StringBuffer sb = new StringBuffer(5000);
		StackTraceElement[] st = e.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			sb.append(st[i] + "\n");
		}
		return sb.toString();
	}

	/**
	 * 將 exception 的 stacktrace 做成 string, 以方便寫入 errorlog
	 * 
	 * @param e
	 * @return
	 */
	public String printStackTrace(Throwable throwable) {
		StringBuffer sb = new StringBuffer(5000);
		StackTraceElement[] st = throwable.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			sb.append(st[i] + "\n");
		}
		return sb.toString();
	}

	public Date parseDatetime(String datestring) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(datestring);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public String parseDatetime(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * yyyyMMddHHmmss
	 * 
	 * @param date
	 * @return
	 */
	public String parseDatetime2(Date date) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}

	/**
	 * 把連續空格縮減為一個空格
	 * 
	 * @param s
	 * @return
	 */
	public String toOneSpace(String s) {
		return Pattern.compile("[ ]+").matcher(s).replaceAll(" ");
	}

	public String translate(String text, Locale locale_from, Locale locale_to) {
		return text;
	}

	/**
	 * 加入 index 位置的 config, 不管原本改設定是否存在，都改為 1
	 * 
	 * @param index
	 * @return
	 */
	public Integer enableConfig(int config, int index) {
		return config | (1 << index);
	}

	/**
	 * 移除 index 位置的 config, 不管原本該設定是否存在，都改為 0
	 * 
	 * @param index
	 * @return
	 */
	public Integer disableConfig(int config, int index) {
		return config & (Integer.MAX_VALUE - (1 << index));
	}

	/**
	 * 變更 config[index] 的值， 0 -> 1 or 1 -> 0
	 * 
	 * @param index
	 * @return
	 */
	public Integer changeConfig(int config, int index) {
		return config ^ (1 << index);
	}


	/**
	 * 
	 * @param url
	 * @return
	 */
	public String URL_encode(String url) {
		return url;
	}

	public boolean hasFullSize(String s) {
		if (s.getBytes().length != s.length()) {
			return true;
		}
		return false;
	}

	/**
	 * URL检查<br>
	 * <br>
	 * 
	 * @param pInput
	 *            要检查的字符串<br>
	 * @return boolean 返回检查结果<br>
	 */
	public boolean isUrl(String url) {
		if (url == null) {
			return false;
		}
		String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
				+ "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
				+ "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
				+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
				+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
				+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
				+ "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
				+ "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(url);
		return matcher.matches();
	}

	/**
	 * 讀取 xml 設定檔
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws InvalidPropertiesFormatException
	 */
	public Properties readProperties(File file) throws InvalidPropertiesFormatException, IOException {
		Properties props = new Properties();
		FileInputStream fis = null;
		fis = new FileInputStream(file);
		props.loadFromXML(fis);
		return props;
	}

	/** *************************************************************** */
	public static void main(String[] args) {
		Utils utils = new Utils();
		String result = "";
		result = utils.treeremove("abc,aab,bbc", "aaa");
		String querystring = "tab=tab01&pagenum=1&name=ssss";
	}
}
