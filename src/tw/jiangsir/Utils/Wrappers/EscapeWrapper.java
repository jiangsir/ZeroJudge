package tw.jiangsir.Utils.Wrappers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang.StringEscapeUtils;

public class EscapeWrapper extends HttpServletRequestWrapper {

	public EscapeWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		String before = this.getRequest().getParameter(name);
		String after = before;
		if (name.equals("problemid") || name.equals("solutionid")
				|| name.equals("origid") || name.equals("reply")
				|| name.equals("language")) {
			after = StringEscapeUtils.escapeHtml(before);
			if (after != null && before != null && !after.equals(before)) {
			}
		}
		if (name.equals("action")) {
			after = StringEscapeUtils.escapeSql(before);
			if (after != null && before != null && !after.equals(before)) {
			}
		}
		return after;
	}

}
