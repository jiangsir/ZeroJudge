<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
	<c:if test="${user.isEnabled==true}">
		<div style="display: inline-block;">
			<c:if
				test="${user.isAuthhost_Google && fn:length(user.pictureblob)>0 }">
				<img src="./ShowImage?userid=${user.id}" class="img-rounded"
					style="width: 1.4em;" title="${user.email }">
			</c:if>
			<span> <a href="./UserStatistic?id=${user.id}"
				title="${user.account}">${fn:substring(user.account, 0, 12)}<c:if
						test="${fn:length(user.account)>12}">...</c:if></a>
			</span> <span title="${fn:escapeXml(user.username)}">(${fn:substring(fn:escapeXml(user.username), 0, 15)}<c:if
					test="${fn:length(fn:escapeXml(user.username))>15}">...</c:if>)
			</span>
			<%-- (${fn:substring(fn:escapeXml(user.username),0,15)}) --%>
		</div>
		<%-- <a href="./UserStatistic?id=${user.id}">${fn:escapeXml(user.account)}</a> --%>
	</c:if>

	<c:if test="${user.isEnabled==false}">
		<span style="text-decoration: line-through;"> <a
			href="./UserStatistic?id=${user.id}">${fn:escapeXml(user.account)}</a>
		</span>
	</c:if>
</c:if>


<c:if test="${applicationScope.appConfig.isCLASS_MODE}">
	<%-- 	<c:set var="schoolid_account" value="${user.account}"></c:set>
	<c:if test="${fn:contains(user.account, '@schoolid')}">
		<c:set var="schoolid_account"
			value="${fn:split(user.account, '@schoolid')[1]}"></c:set>
	</c:if>
	<c:if test="${!fn:contains(user.account, '@schoolid')}">
		<c:set var="schoolid_account" value="${user.account}"></c:set>
	</c:if>
 --%>
	<div style="display: inline-block;">
		<c:if
			test="${user.isAuthhost_Google && fn:length(user.pictureblob)>0 }">
			<img src="./ShowImage?userid=${user.id}" class="img-rounded"
				style="width: 1.4em;" title="${user.email }">
		</c:if>
		<a href="./UserStatistic?id=${user.id}" title="${user.account}">${fn:substring(user.username, 0, 15)}<c:if
				test="${fn:length(user.username)>15}">...</c:if></a>
		<c:if test="${user.schoolid>0}">
			<button class="btn btn-default btn-xs" type="submit"
				title="schoolid= ${user.school.id}">${user.school.schoolname }</button>
		</c:if>
	</div>
</c:if>
