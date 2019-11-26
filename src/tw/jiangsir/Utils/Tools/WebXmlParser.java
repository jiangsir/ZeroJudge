/**
 * idv.jiangsir.utils - XMLParser.java
 * 2009/3/21 上午 04:09:15
 * jiangsir
 */
package tw.jiangsir.Utils.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import tw.jiangsir.Utils.Scopes.ApplicationScope;
import tw.zerojudge.Configs.ConfigFactory;

/**
 * @author jiangsir
 * 
 */
public class WebXmlParser {
	private Document doc_webxml = null;
	private File webxml;

	/**
	 * 對 Web.xml 進行解析
	 * 
	 */
	public WebXmlParser() {
		ApplicationScope.setAppRoot(new File(System.getProperty("user.dir")
				+ "/WebContent/"));
		this.webxml = ConfigFactory.getWebxml();

	}

	/**
	 * 外部使用
	 * 
	 * @param APP_REAL_PATH
	 */
	public WebXmlParser(String APP_REAL_PATH) {
		this.webxml = new File(APP_REAL_PATH + "/WEB-INF/", "web.xml");
	}

	/**
	 * 實際寫入 web.xml
	 * 
	 */
	public void writetoWebxml() {
		XMLOutputter outp = new XMLOutputter();
		FileOutputStream fo = null;

		try {
			fo = new FileOutputStream(this.webxml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			outp.output(this.doc_webxml, fo);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				this.doc_webxml = builder.build(webxml);
			}
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Element root = this.doc_webxml.getRootElement();
		List children = root.getChildren();
		Iterator i = children.iterator();
		while (i.hasNext()) {
			Element child = (Element) i.next();
			if ("context-param".equals(child.getName())) {
				Element param_name = (Element) child.getChildren().get(0);
				Element param_value = (Element) child.getChildren().get(1);
				if (param_name.getValue().equals(param)) {
					return param_value.getText().trim();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param urlpattern
	 * @return
	 */
	public String getServletName_ByJDOM(String urlpattern) {
		Element root = this.doc_webxml.getRootElement();
		Iterator<?> it = root.getChildren().iterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();

			if (element.getName().equals("servlet-mapping")) {
				List mapping = element.getChildren();
				if (((Element) mapping.get(1)).getTextTrim().equals(urlpattern)) {
					return ((Element) mapping.get(0)).getTextTrim();
				}
			}
		}
		return null;
	}

	/**
	 * 設定 web.xml context-param
	 * 
	 * @param name
	 * @param value
	 */
	public void setInitParam(String param, String value) {
		if ("".equals(value)) {
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		try {
			if (this.doc_webxml == null) {
				this.doc_webxml = builder.build(webxml);
			}
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Element root = this.doc_webxml.getRootElement();
		List children = root.getChildren();
		Iterator i = children.iterator();
		while (i.hasNext()) {
			Element child = (Element) i.next();
			if ("context-param".equals(child.getName())) {
				Element param_name = (Element) child.getChildren().get(0);
				Element param_value = (Element) child.getChildren().get(1);
				if (param.equals(param_name.getValue())) {
					param_value.setText(value);
					return;
				}
			}
		}
	}
}
