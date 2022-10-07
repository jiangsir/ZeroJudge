<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<c:if test="${contest.isRunning || contest.isPausing || contest.isStopped}">
	<c:forEach var="problem" items="${contest.problems}"
		varStatus="varstatus">
		<c:set var="statuscount" value="${varstatus.count}" scope="request" />
		<c:set var="problem" value="${problem}" scope="request" />
		<button class="btn btn-default btn-xs"
			title="${problem.problemid}. ${problem.title}">
			<jsp:include page="ContestScore.jsp" />
		</button>
	</c:forEach>
	<%-- 
<a href="./ShowContest?contestid=${contest.id}" class="btn btn-success">檢視<fmt:message key="Contest.VContest" /></a>
<br />
 --%>
</c:if>
