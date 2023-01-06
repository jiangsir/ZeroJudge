package tw.zerojudge.Objects;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author jiangsir
 * 
 */
public class WebObject {
	private URL url;
	private String[] querystrings;
	private String method;
	private String charset;

	public WebObject(URL url, String[] querystrings, String method,
			String charset) {
		this.setUrl(url);
		this.setMethod(method);
		this.setQuerystrings(querystrings);
		this.setCharset(charset);
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String[] getQuerystrings() {
		return querystrings;
	}

	public void setQuerystrings(String[] querystrings) {
		for (int i = 0; i < querystrings.length; i++) {
		}
		this.querystrings = querystrings;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		if (!"POST".equals(method)) {
			method = "GET";
		}
		this.method = method;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		if (!"UTF8".equals(charset) && !"big5".equals(charset)) {
			charset = "UTF8";
		}
		this.charset = charset;
	}

	public String getHtml() {
		try {
			if ("POST".equals(method)) {
				return getPostdata();
			} else {
				return getGetdata();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getGetdata() throws IOException {

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true); 
		conn.getOutputStream().write(url.getQuery().getBytes());
		conn.getOutputStream().flush();
		conn.getOutputStream().close();

		conn.setRequestProperty("Cookie", "");
		conn.setRequestProperty("Connection", "Keep-Alive");


		java.io.BufferedReader rd = new java.io.BufferedReader(
				new java.io.InputStreamReader(conn.getInputStream(), charset));
		StringBuffer webdata = new StringBuffer(10000);
		webdata.append("METHOD=" + conn.getRequestMethod() + "\n");
		webdata.append("回應訊息" + conn.getResponseMessage() + "\n");
		webdata.append("回應代號" + conn.getResponseCode() + "\n");
		webdata.append("表頭第一行" + conn.getHeaderField(0) + "\n");
		webdata.append("內容長度" + conn.getContentLength() + "\n");
		webdata.append("內容種類" + conn.getContentType() + "\n");
		String line;
		while ((line = rd.readLine()) != null) {
			webdata.append(line + "\n");
		}
		rd.close();
		return webdata.toString();
	}

	public String getPostdata() throws IOException {

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		String qs = "";
		for (int i = 0; i < querystrings.length; i++) {
			qs += "&" + querystrings[i];
		}
		outStream.write(qs.getBytes());
		outStream.flush();

		java.io.BufferedReader br = new java.io.BufferedReader(
				new java.io.InputStreamReader(conn.getInputStream(), charset));

		StringBuffer webdata = new StringBuffer(10000);
		webdata.append("METHOD=" + conn.getRequestMethod() + "\n");
		webdata.append("回應訊息" + conn.getResponseMessage() + "\n");
		webdata.append("回應代號" + conn.getResponseCode() + "\n");
		webdata.append("表頭第一行" + conn.getHeaderField(0) + "\n");
		webdata.append("內容長度" + conn.getContentLength() + "\n");
		webdata.append("內容種類" + conn.getContentType() + "\n");
		String line;
		while ((line = br.readLine()) != null) {
			webdata.append(line + "\n");
		}
		br.close();
		return webdata.toString();
	}
}
