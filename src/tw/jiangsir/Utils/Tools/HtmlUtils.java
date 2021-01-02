package tw.jiangsir.Utils.Tools;

import java.util.List;

import org.owasp.html.ElementPolicy;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlUtils {
	private static final String[] allowedTags = { "h1", "h2", "h3", "h4", "h5", "h6", "span", "strong", "img", "video",
			"source", "blockquote", "p", "div", "ul", "ol", "li", "table", "thead", "caption", "tbody", "tr", "th",
			"td", "br", "a", "code", "pre", "sup", "sub" };

	private static final String[] needTransformTags = { "article", "aside", "command", "datalist", "details",
			"figcaption", "figure", "footer", "header", "hgroup", "section", "summary" };

	private static final String[] linkTags = { "img", "video", "source", "a" };

	/**
	 * 過濾 HTML
	 * 
	 * @param htmlContent
	 * @return
	 */
	public static String sanitizeHtml(String htmlContent) {
		PolicyFactory policy = new HtmlPolicyBuilder()
				.allowElements(allowedTags)
				.allowElements(new ElementPolicy() {
					@Override
					public String apply(String elementName, List<String> attributes) {
						return "div";
					}
				}, needTransformTags).allowAttributes("src", "width", "height", "href", "target").onElements(linkTags)
				.allowUrlProtocols("https").toFactory();
		String safeHTML = policy.sanitize(htmlContent);
		return safeHTML;
	}
}
