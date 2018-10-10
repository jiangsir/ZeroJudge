<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!--         <div
            style="width: 70; margin: auto; vertical-align: top; margin-left: auto; margin-right: auto;">
 -->

<div id="pagging" style="padding: 10px;">
	<c:choose>
		<c:when test="${param.page==null || param.page<=0}">
			<c:set var="pagenum" value="1" />
		</c:when>
		<c:otherwise>
			<c:set var="pagenum" value="${param.page}" />
		</c:otherwise>
	</c:choose>
	<%-- 	<c:if test="${param.page==null}">
		<c:set var="pagenum" value="1" />
	</c:if>
	<c:if test="${param.page>=1}">
		<c:set var="pagenum" value="${param.page}" />
	</c:if>
 --%>
	<%-- 	<c:if test="${pagenum==0}">
		<c:set var="pagenum" value="${param.lastpage}" />
	</c:if>
 --%>
	<c:choose>
		<c:when test="${pagenum!=1}">
			<a href="?${param.querystring}&page=1"><fmt:message
					key="FIRST_PAGE" /></a>
		</c:when>
		<c:otherwise>
			<fmt:message key="FIRST_PAGE" />
		</c:otherwise>
	</c:choose>
	|
	<c:choose>
		<c:when test="${pagenum>1}">
			《- <a href="?${param.querystring}&page=${pagenum-1}"><fmt:message
					key="PREV_PAGE" /></a>
		</c:when>
		<c:otherwise>
			《- <fmt:message key="PREV_PAGE" />
		</c:otherwise>
	</c:choose>
	|
	<fmt:message key="THIS_PAGE">
		<fmt:param value="${pagenum}" />
	</fmt:message>
	|
	<c:choose>
		<c:when
			test="${empty param.tag && empty param.author && (empty lastpage || lastpage==0 || pagenum<lastpage) }">
			<a href="?${param.querystring}&page=${pagenum+1}"
				title="lastpage=${lastpage}"><fmt:message key="NEXT_PAGE" /></a>
		</c:when>
		<c:otherwise>
			<fmt:message key="NEXT_PAGE" />
		</c:otherwise>
	</c:choose>
	-》
	<c:if test="${not empty lastpage && lastpage>1 && pagenum<lastpage}">
		| <a href="?${param.querystring}&page=${lastpage}"><fmt:message
				key="LAST_PAGE" /></a>
	</c:if>

	<%-- 			${pageContext.request.queryString.querystring}
 --%>
</div>
