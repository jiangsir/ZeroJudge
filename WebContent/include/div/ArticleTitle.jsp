<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<script type="text/javascript">
	jQuery(document).ready(function() {

	});
</script>
<div data-articleid="${article.id }">
	<c:if test="${article.articletype.is_marked}">
		<!-- <img src="images/marked.png" /> -->
		<i class="fa fa-star" aria-hidden="true" title="標記"></i>
	</c:if>
	<c:if test="${article.articletype.is_problemreport}">
		<button class="btn btn-info btn-xs">解題報告</button>
	</c:if>
	<c:if test="${article.articletype.is_announcement}">
		<button class="btn btn-info btn-xs">公告</button>
	</c:if>
	<span id="articletitle">  <c:choose>
			<c:when test="${article.reply=='0'}">
				<a href="./ShowThread?postid=${article.id}&reply=${article.reply}">${fn:escapeXml(article.subject)}</a>
			</c:when>
			<c:otherwise>
				<a
					href="./ShowThread?postid=${article.id}&reply=${article.reply}#${article.id}">${fn:escapeXml(article.subject)}</a>
			</c:otherwise>
		</c:choose> <c:if test="${article.isHidden}"><span class="btn btn-warning btn-xs">已隱藏</span></c:if>
	</span>
</div>
