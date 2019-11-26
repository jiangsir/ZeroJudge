/**
 * idv.jiangsir.utils - XMLParser.java
 * 2009/3/21 上午 04:09:15
 * jiangsir
 */
package tw.zerojudge.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import tw.jiangsir.Utils.Exceptions.AccessException;
import tw.zerojudge.Configs.ConfigFactory;
import tw.zerojudge.Factories.AttributeFactory;
import tw.zerojudge.Factories.UserFactory;

/**
 * @author jiangsir
 * 
 */
public class XMLParser {
	private File webxml;
	private File contextxml;

	private Document doc_contextxml = null;
	private Document doc_webxml = null;


	/**
	 * web 使用
	 * 
	 */
	public XMLParser() {
		this.webxml = ConfigFactory.getWebxml();
		this.contextxml = new File(ConfigFactory.getMETA_INF(), "context.xml");
	}

	/**
	 * 外部使用
	 * 
	 * @param APP_REAL_PATH
	 */
	public XMLParser(File BASE_DIR) {
		SAXBuilder builder = new SAXBuilder();
		this.webxml = new File(BASE_DIR.getPath() + "/WEB-INF/", "web.xml");
		this.contextxml = new File(BASE_DIR.getPath() + "/META-INF/",
				"context.xml");
		try {
			this.doc_contextxml = builder.build(this.contextxml);
			this.doc_webxml = builder.build(this.webxml);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 讀取 context.xml 的資料
	 * 
	 * @param attribute
	 * @param key
	 * @return
	 */
	public String getContextParam(String attribute, String key) {
		Element root = this.doc_contextxml.getRootElement();
		Iterator<?> it = root.getChildren().iterator();
		while (it.hasNext()) {
			Element child = (Element) it.next();
			if (attribute.equals(child.getName())) {
				return child.getAttributeValue(key);
			}
		}
		return "";
	}

	/**
	 * 設定 context.xml 的資料。空白的話，則維持原設定
	 * 
	 * @param attribute
	 * @param key
	 * @param value
	 */
	public void setContextParam(String attribute, String key, String value) {
		if ("".equals(value)) {
			return;
		}
		Element root = this.doc_contextxml.getRootElement();
		List<?> children = root.getChildren();
		Iterator<?> i = children.iterator();
		while (i.hasNext()) {
			Element child = (Element) i.next();
			if (attribute.equals(child.getName())) {
				child.setAttribute(key, value);
				return;
			}
		}
	}

	/**
	 * 設定 context.xml 的 root (docBase)
	 * 
	 * @param resourcekey
	 * @param value
	 */
	public void setContextParam_docBase(String value) {
		if ("".equals(value)) {
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_contextxml == null) {
				this.doc_contextxml = builder.build(this.contextxml);
			}
			Element root = this.doc_contextxml.getRootElement();
			root.setAttribute("docBase", value);
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public String getDBHost() {
		String url = this.getContextParam("Resource", "url");
		return url.substring(url.indexOf("//") + 2, url.indexOf(":3306"));
	}

	public void setDBHost(String host) {
		if ("".equals(host)) {
			return;
		}
		String url = this.getContextParam("Resource", "url");
		url = url.replaceFirst(this.getDBHost(), host);
		this.setContextParam("Resource", "url", url);
	}

	public String getDBName() {
		String url = this.getContextParam("Resource", "url");
		return url.substring(url.indexOf("3306/") + 5, url.indexOf("?"));
	}

	public void setDBName(String name) {
		if ("".equals(name)) {
			return;
		}
		String url = this.getContextParam("Resource", "url");
		url = url.replaceFirst(this.getDBName(), name);
		this.setContextParam("Resource", "url", url);
	}

	/**
	 * 實際寫入 web.xml
	 * 
	 * @throws AccessException
	 * 
	 */
	public void writetoWebxml() throws AccessException {
		XMLOutputter outp = new XMLOutputter();
		FileOutputStream fo = null;
		if (this.doc_webxml == null) {
			throw new AccessException(UserFactory.getNullOnlineUser(),
					this.webxml.getPath() + " doc_webxml == null 不能寫入檔案");
		}
		try {
			fo = new FileOutputStream(this.webxml);
			outp.output(this.doc_webxml, fo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new AccessException(UserFactory.getNullOnlineUser(),
					this.webxml.getPath() + " 不存在或無法讀寫。");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得 web.xml 所有 context-param 的設定。
	 * 
	 * @param name
	 * @return
	 */
	public TreeMap<String, String> getInitParams() {
		TreeMap<String, String> params = new TreeMap<String, String>();
		Enumeration<?> enumeration = AttributeFactory.getServletContext()
				.getInitParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement().toString();
			params.put(name, AttributeFactory.getServletContext()
					.getInitParameter(name));
		}
		return params;
	}

	/**
	 * 取得 web.xml 所有 context-param 的設定。
	 * 
	 * @param name
	 * @return
	 */
	public TreeMap<String, String> getContextAttributes() {
		TreeMap<String, String> atts = new TreeMap<String, String>();
		Enumeration<?> enumeration = AttributeFactory.getServletContext()
				.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement().toString();
			atts.put(name,
					AttributeFactory.getServletContext().getAttribute(name)
							.toString());
		}
		return atts;
	}

	/**
	 * 取得 web.xml context-param 的設定。
	 * 
	 * @param name
	 * @return
	 */
	public String getInitParam(String param) {
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_webxml == null) {
				this.doc_webxml = builder.build(this.webxml);
			}
			Element root = this.doc_webxml.getRootElement();
			List<?> children = root.getChildren();
			Iterator<?> it = children.iterator();
			while (it.hasNext()) {
				Element child = (Element) it.next();
				if ("context-param".equals(child.getName())) {
					Element param_name = (Element) child.getChildren().get(0);
					Element param_value = (Element) child.getChildren().get(1);
					if (param_name.getValue().trim().equals(param)) {
						return param_value.getText().trim();
					}
				}
			}

		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return "";
	}

	/**
	 * 設定 web.xml context-param
	 * 
	 * @param name
	 * @param value
	 */
	public void setInitParam(String param, String value) {
		if (this.getInitParam(param) == null) {
			this.addInitParam(param, "");
		}
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_webxml == null) {
				this.doc_webxml = builder.build(this.webxml);
			}
			Element root = this.doc_webxml.getRootElement();
			List<?> children = root.getChildren();
			Iterator<?> i = children.iterator();
			while (i.hasNext()) {
				Element child = (Element) i.next();
				if ("context-param".equals(child.getName())) {
					Element param_name = (Element) child.getChildren().get(0);
					Element param_value = (Element) child.getChildren().get(1);
					if (param.equals(param_name.getValue().trim())) {
						param_value.setText(value);
						return;
					}
				}
			}
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void addInitParam(String name, String value) {
		SAXBuilder builder = new SAXBuilder();
		if (this.doc_webxml == null) {
			try {
				this.doc_webxml = builder.build(this.webxml);
			} catch (JDOMException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		Element root = this.doc_webxml.getRootElement();

		Element element = new Element("context-param");

		element.addContent(new Element("param-name").setText(name));
		element.addContent(new Element("param-value").setText(value));
		root.addContent(element);


	}

	/**
	 * 實際寫入 context.xml
	 * 
	 * @throws AccessException
	 * 
	 */
	public synchronized void writeContextxml() throws AccessException {
		XMLOutputter outp = new XMLOutputter();
		FileOutputStream fo = null;
		if (this.doc_contextxml == null) {
			throw new AccessException(UserFactory.getNullOnlineUser(),
					this.contextxml.getPath()
							+ " doc_contextxml == null 不能寫入檔案");
		}
		try {
			fo = new FileOutputStream(this.contextxml);
			outp.output(this.doc_contextxml, fo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new AccessException(UserFactory.getNullOnlineUser(),
					this.contextxml.getPath()
							+ " 不存在或無法讀寫(FileNotFoundException)。");
		} catch (IOException e) {
			e.printStackTrace();
			throw new AccessException(UserFactory.getNullOnlineUser(),
					this.contextxml.getPath() + " 不存在或無法讀寫(IOException)。");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
