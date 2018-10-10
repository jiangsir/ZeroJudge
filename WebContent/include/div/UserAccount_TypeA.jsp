<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<c:if test="${user.isEnabled==true}">
	<div style="display: inline-block;">
		<c:if
			test="${user.isAuthhost_Google && fn:length(user.pictureblob)>0 }">
			<img src="${user.pictureBase64 }" class="img-rounded"
				style="width: 1.4em;" title="${user.email }">
		</c:if>
		<span> <a href="./UserStatistic?id=${user.id}"
			title="${user.account}">${fn:substring(user.account, 0, 15)}<c:if
					test="${fn:length(user.account)>15}">...</c:if></a>
		</span> <span title="${fn:escapeXml(user.username)}">(${fn:substring(fn:escapeXml(user.username), 0, 20)}<c:if
				test="${fn:length(fn:escapeXml(user.username))>20}">...</c:if>)
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
