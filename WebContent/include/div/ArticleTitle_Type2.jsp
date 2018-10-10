<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>


<c:if test="${article.articletype.is_marked}">
	<!-- <img src="images/marked.png" /> -->
	<i class="fa fa-star" aria-hidden="true" title="標記"></i>
</c:if>
<c:if test="${article.articletype.is_problemreport}">
	<button class="btn btn-default btn-xs">解題報告</button>
</c:if>
<c:if test="${article.articletype.is_announcement}">
	<button class="btn btn-default btn-xs">公告</button>
</c:if>
${fn:escapeXml(article.subject)}
