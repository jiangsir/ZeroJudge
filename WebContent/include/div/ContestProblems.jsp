<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<c:if
	test="${contest.isRunning || contest.isPausing || contest.isStopped}">
	<c:forEach var="problem" items="${contest.problems}"
		varStatus="varstatus">
		<c:set var="contestant"
			value="${contest.getContestantByUserid(sessionScope.onlineUser.id)}"
			scope="request" />
		<c:set var="statuscount" value="${varstatus.count}" scope="request" />
		<button class="btn btn-default btn-xs">
			<jsp:include page="ContestScore.jsp" />
		</button>

#${varstatus.count}. 
    <c:set var="problem" value="${problem}" scope="request" />
		<%-- 		<jsp:include page="Accepted.jsp" />
 --%>
		<jsp:include page="ProblemDisplay.jsp" />
		<%-- <a href="./ShowProblem?problemid=${problem.problemid}"> --%>
		<c:if test="${!applicationScope.appConfig.getIsCLASS_MODE()}">
 ${problem.problemid}. 
 </c:if> ${fn:escapeXml(problem.title)}
		<!-- </a> -->
         (${contest.scores[varstatus.count-1]}/${contest.totalScore })
        <br />
	</c:forEach>
	<br />
	<%-- 
<a href="./ShowContest?contestid=${contest.id}" class="btn btn-success">檢視<fmt:message key="Contest.VContest" /></a>
<br />
 --%>
</c:if>
