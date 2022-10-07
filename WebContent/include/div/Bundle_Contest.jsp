<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<%-- <c:if test="${applicationScope.appConfig.isCLASS_MODE}">
	<fmt:message key="Contest.VContest" />
</c:if>
 --%>
<c:choose>
	<c:when test="${contest.vclassid>0}">
		<fmt:message key="Contest.VContest" />
	</c:when>
	<c:otherwise>
		<fmt:message key="Contest.Contest" />
	</c:otherwise>
</c:choose>

