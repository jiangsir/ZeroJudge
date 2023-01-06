package tw.jiangsir.Utils.Wrappers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import tw.jiangsir.Utils.Filters.Filter1_EncodingFilter;
import tw.jiangsir.Utils.Filters.Filter1_EncodingFilter.ENCODING;

public class EncodingWrapper extends HttpServletRequestWrapper {
	Filter1_EncodingFilter.ENCODING encoding;

	public EncodingWrapper(HttpServletRequest request,
			Filter1_EncodingFilter.ENCODING encoding) {
		super(request);
		this.encoding = encoding;
	}

	/**
	 * 判断字符串的编码
	 * 
	 * @param str
	 * @return
	 */
	public ENCODING getEncoding(String str) {
		if (str == null) {
			return null;
		}
		for (ENCODING encoding : ENCODING.values()) {
			try {
				if (str.equals(new String(str.getBytes(encoding.getValue()),
						encoding.getValue()))) {
					return encoding;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getParameter(String name) {
		String value = this.getRequest().getParameter(name);
		if (value != null && this.getEncoding(value) != null
				&& this.getEncoding(value) == ENCODING.ISO_8859_1) {
			byte[] b;
			try {
				b = value.getBytes(ENCODING.ISO_8859_1.getValue());
				value = new String(b, ENCODING.UTF_8.getValue());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return value;
	}
}
