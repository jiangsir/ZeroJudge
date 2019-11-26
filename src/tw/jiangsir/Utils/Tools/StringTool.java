package tw.jiangsir.Utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tw.zerojudge.Objects.IpAddress;
import tw.zerojudge.Objects.Problemid;

public class StringTool {
	/**
	 * 將 set 自動轉出的字串轉回 TreeSet。字串格式如下：["aa", "bb", "cc"]
	 * 
	 * @param setstring
	 * @return
	 */
	public static TreeSet<String> String2TreeSet(String setstring) {
		setstring = setstring.trim();
		if (setstring.startsWith("[") && setstring.endsWith("]")) {
			setstring = setstring.substring(1, setstring.length() - 1);
		}
		setstring = setstring.replaceAll("\"", "").trim();
		TreeSet<String> set = new TreeSet<String>();
		if (setstring.equals("")) {
			return set;
		}
		for (String s : setstring.split(",")) {
			set.add(s.trim());
		}
		return set;
	}

	public static TreeSet<Problemid> String2Problemidset(String setstring) {
		setstring = setstring.replaceAll("\\[", "");
		setstring = setstring.replaceAll("\\]", "");
		setstring = setstring.replaceAll("\"", "").trim();
		TreeSet<Problemid> set = new TreeSet<Problemid>();
		if (setstring.equals("")) {
			return set;
		}
		for (String s : setstring.split(",")) {
			set.add(new Problemid(s.trim()));
		}
		return set;
	}


	/**
	 * 將 set 自動轉出的字串轉回 TreeSet。字串格式如下：["aa", "bb", "cc"]
	 * 
	 * @param setstring
	 * @return
	 */
	public static LinkedHashSet<String> String2LinkedHashSet(String setstring) {
		setstring = setstring.trim();
		if (setstring.startsWith("[") && setstring.endsWith("]")) {
			setstring = setstring.substring(1, setstring.length() - 1);
		}
		setstring = setstring.replaceAll("\"", "").trim();
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		if (setstring.equals("")) {
			return set;
		}
		for (String s : setstring.split(",")) {
			set.add(s.trim());
		}
		return set;
	}

	public static LinkedHashSet<IpAddress> String2IpAddressSet(String setstring) {
		LinkedHashSet<IpAddress> set = new LinkedHashSet<IpAddress>();
		if (setstring == null) {
			return set;
		}
		setstring = setstring.replaceAll("\\[", "");
		setstring = setstring.replaceAll("\\]", "");
		setstring = setstring.replaceAll("\"", "").trim();
		if (setstring.equals("")) {
			return set;
		}
		for (String s : setstring.split(",")) {
			set.add(new IpAddress(s));
		}
		return set;
	}

	public static ArrayList<IpAddress> String2IpAddressList(String string) {
		ArrayList<IpAddress> list = new ArrayList<IpAddress>();
		if (string == null) {
			return list;
		}
		string = string.replaceAll("\\[", "");
		string = string.replaceAll("\\]", "");
		string = string.replaceAll("\"", "").trim();
		if (string.equals("")) {
			return list;
		}
		for (String s : string.split(",")) {
			list.add(new IpAddress(s));
		}
		return list;
	}

	public static TreeSet<IpAddress> String2IpAddressTreeSet(String setstring) {
		TreeSet<IpAddress> set = new TreeSet<IpAddress>();
		if (setstring == null) {
			return set;
		}
		setstring = setstring.replaceAll("\\[", "");
		setstring = setstring.replaceAll("\\]", "");
		setstring = setstring.replaceAll("\"", "").trim();
		if (setstring.equals("")) {
			return set;
		}
		for (String s : setstring.split(",")) {
			set.add(new IpAddress(s));
		}
		return set;
	}

	public static LinkedHashSet<Problemid> String2LinkedHashSetProblemid(String setstring) {
		LinkedHashSet<Problemid> set = new LinkedHashSet<Problemid>();
		if (setstring == null || "".equals(setstring)) {
			return set;
		}
		setstring = setstring.replaceAll("\\[", "");
		setstring = setstring.replaceAll("\\]", "");
		setstring = setstring.replaceAll("\"", "").trim();

		for (String s : setstring.split(",")) {
			set.add(new Problemid(s));
		}
		return set;
	}

	/**
	 *  還原 Arrays.toString 自動轉出的字串 String[]。字串格式如下：[aa, bb, cc]
	 * 
	 * @param setstring
	 * @return
	 */
	public static String[] String2Array(String string) {
		string = string.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").trim();
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
	 * 將 set 自動轉出的字串轉回 Set。字串格式如下：[1, 3, 5]
	 * 
	 * @param setstring
	 * @return
	 */
	public static Set<Integer> String2IntegerSet(String integerset) {
		if (integerset.matches("^\\[.*\\]$")) {
			integerset = integerset.replaceAll("\\[", "");
			integerset = integerset.replaceAll("\\]", "");
		}
		integerset = integerset.replaceAll("\"", "").trim();
		TreeSet<Integer> set = new TreeSet<Integer>();
		if (integerset.equals("")) {
			return set;
		}
		for (String s : integerset.split(",")) {
			set.add(Integer.parseInt(s.trim()));
		}
		return set;
	}

	/**
	 * 過濾 script 以及 style
	 * 
	 * @param inputString
	 * @return
	 */
	public static String escapeScriptStyle(String inputString) {

		String htmlStr = inputString; 
		String textStr = "";
		java.util.regex.Pattern p_script, p_style, p_input, p_iframe;
		java.util.regex.Matcher m_script, m_style, m_input, m_iframe;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			String regEx_input = "<[\\s]*?input[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?input[\\s]*?>";
			String regEx_iframe = "<[\\s]*?iframe[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?iframe[\\s]*?>";

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); 

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); 

			p_input = Pattern.compile(regEx_input, Pattern.CASE_INSENSITIVE);
			m_input = p_input.matcher(htmlStr);
			htmlStr = m_input.replaceAll(""); 

			p_iframe = Pattern.compile(regEx_iframe, Pattern.CASE_INSENSITIVE);
			m_iframe = p_iframe.matcher(htmlStr);
			htmlStr = m_iframe.replaceAll(""); 


			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;
	}

	/**
	 * 功能：去掉所有的<*>标记,去除html标签<br>
	 * 代表這個欄位完全不允許 HTML 標記
	 * 
	 * @param content
	 * @return
	 */
	public static String escapeHtmlTag(String content) {
		Pattern p = null;
		Matcher m = null;
		String value = null;

		p = Pattern.compile("(<[^>]*>)");
		m = p.matcher(content);
		String temp = content;
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, "");
		}

		p = Pattern.compile("(\r+|\n+)");
		m = p.matcher(temp);
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, " ");
		}

		return temp;
	}

	/**
	 * 去除 str 中危險的 HTML 標簽。
	 * 
	 * @param str
	 * @return
	 */
	private static String removeDangerousHtml(String str) {
		if (str == null) {
			return "";
		}
		str = str.toLowerCase();
		str = str.replaceAll("</?span[^>]*>", "");
		str = str.replaceAll("&#[^>]*;", "");
		str = str.replaceAll("</?marquee[^>]*>", "");
		str = str.replaceAll("</?object[^>]*>", "");
		str = str.replaceAll("</?param[^>]*>", "");
		str = str.replaceAll("</?embed[^>]*>", "");
		str = str.replaceAll("</?table[^>]*>", "");
		str = str.replaceAll("&nbsp;", "");
		str = str.replaceAll("</?tr[^>]*>", "");
		str = str.replaceAll("</?th[^>]*>", "");
		str = str.replaceAll("</?p[^>]*>", "");
		str = str.replaceAll("</?a[^>]*>", "");
		str = str.replaceAll("</?img[^>]*>", "");
		str = str.replaceAll("</?tbody[^>]*>", "");
		str = str.replaceAll("</?li[^>]*>", "");
		str = str.replaceAll("</?div[^>]*>", "");
		str = str.replaceAll("</?td[^>]*>", "");
		str = str.replaceAll("</?script[^>]*>", "");
		str = str.replaceAll("(javascript|jscript|vbscript|vbs):", "");
		str = str.replaceAll("on(mouse|exit|error|click|key)", "");
		str = str.replaceAll("<\\?xml[^>]*>", "");
		str = str.replaceAll("<\\?[a-z]+:[^>]*>", "");
		str = str.replaceAll("</?font[^>]*>", "");
		str = str.replaceAll("</?b[^>]*>", "");
		str = str.replaceAll("</?u[^>]*>", "");
		str = str.replaceAll("</?i[^>]*>", "");
		str = str.replaceAll("</?strong[^>]*>", "");
		str = str.replaceAll("</?(a|A)( .*?>|>)", "");
		return str;
	}

	/**
	 * 避免 querystring 出現重複的參數，若有重複以最後一個為準
	 * 
	 * @param querystring
	 * @return
	 */
	public static String querystingMerge(String querystring) {
		if (querystring == null || "".equals(querystring)) {
			return "";
		}
		String[] array = querystring.split("&");
		TreeMap<String, String> tmap = new TreeMap<String, String>();
		for (int i = 0; i < array.length; i++) {
			if ("".equals(array[i].trim())) {
				continue;
			}
			String[] query = array[i].trim().split("=");
			if (query.length == 2 && !"".equals(query[0]) && !"".equals(query[1])
					&& !query[0].matches("^[Pp][Aa][Gg][Ee].*")) {
				tmap.remove(query[0]);
				tmap.put(query[0], query[1]);
			}
		}
		String result = "";
		String key;
		Iterator<?> it = tmap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			result += "&" + key + "=" + tmap.get(key);
		}
		return result;
	}

	/**
	 * 避免 querystring 出現重複的參數，若有重複以『第一個』為準 專用於 submissions 多個 parameter 組合。
	 * 
	 * @param querystring
	 * @return
	 */
	public static String querystingMergeFirst(String querystring) {
		if (querystring == null || "".equals(querystring)) {
			return "";
		}
		String[] array = querystring.split("&");
		TreeMap<String, String> tmap = new TreeMap<String, String>();
		for (int i = 0; i < array.length; i++) {
			if ("".equals(array[i].trim())) {
				continue;
			}
			String[] query = array[i].trim().split("=");
			if (query.length == 2 && !"".equals(query[0]) && !tmap.containsKey(query[0]) && !"".equals(query[1])
					&& !query[0].matches("^[Pp][Aa][Gg][Ee].*")) {
				tmap.remove(query[0]);
				tmap.put(query[0], query[1]);
			}
		}
		String result = "";
		String key;
		Iterator<?> it = tmap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			result += "&" + key + "=" + tmap.get(key);
		}
		return result;
	}

}
