<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="SetTool" uri="http://jiangsir.tw/jstl/settool"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<%-- <c:forEach var="ip" items="${ipset }">
 --%>	<c:if
		test="${SetTool:contains(applicationScope.appConfig.bannedIPSet, ip) }">

		<span style="text-decoration: line-through;" title="已封鎖">${ip}</span>
		<img src="./images/ipallow.png" alt="解鎖這個IP" border="0"
			title="解鎖 ${ip }" style="cursor: pointer" id="unbannedIP" ip="${ip}">
	</c:if>
	<c:if
		test="${!SetTool:contains(applicationScope.appConfig.bannedIPSet, ip) }">

		<span>${ip }</span>
		<img src="./images/ipdeny.png" alt="封鎖這個IP" border="0"
			title="封鎖 ${ip }" style="cursor: pointer" id="bannedIP" ip="${ip}">
	</c:if>
<%-- </c:forEach>
 --%>
