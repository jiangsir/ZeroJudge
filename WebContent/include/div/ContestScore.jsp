<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<div align="center">
	<c:choose>
		<c:when test="${contestant.aclist[statuscount-1]=='AC'}">
<!-- 			<img src="./images/accept16.png" border="0" />
 -->			<img src="./images/accept.svg" border="0" style="height: 1em;" />
		</c:when>
		<c:when
			test="${contestant.aclist[statuscount-1]=='-' || contestant.aclist[statuscount-1]=='-1' || contestant.aclist[statuscount-1]=='' || contestant.aclist[statuscount-1]=='null' || contestant.aclist[statuscount-1]==null}">
			-
		</c:when>
		<c:otherwise>${contestant.aclist[statuscount-1]}<span
				style="font-size: smaller;">%</span>
		</c:otherwise>
	</c:choose>
</div>
