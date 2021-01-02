<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<c:choose>
	<c:when test="${fn:length(vcontests)>0}">
		<c:forEach var="vcontest" items="${vcontests}" varStatus="varstatus">
			<c:set var="contest" value="${vcontest}" scope="request" />
			<c:set var="contestant"
				value="${contest.getContestantByUserid(userid)}" scope="request" />
			<div title="${fn:escapeXml(contest.title)}">
				<fmt:formatNumber type="number" pattern="00"
					value="${varstatus.count}" />
				.
				<jsp:include page="/include/div/ContestProblems_TypeB.jsp" />
				<fmt:formatNumber value="${contestant.score}" pattern="#" />
				分
			</div>

		</c:forEach>
	</c:when>
	<c:otherwise>
    沒有任何結果可以呈現！
    </c:otherwise>
</c:choose>
