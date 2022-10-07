<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
	<script type="text/javascript" src="jscripts/js_date.js?${applicationScope.built}"></script>
	<script type="text/javascript" src="ShowContest.js?${applicationScope.built}"></script>
	<script type="text/javascript" src="include/JoinContestButton.js?${applicationScope.built}"></script>
	<script type="text/javascript" src="include/ContestToolbar.js?${applicationScope.built}"></script>
</head>
<body class="Consolas">
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<jsp:include page="include/ContestHeader.jsp" />
			<c:choose>
				<c:when test="${contest.visible_JoinContestButton(sessionScope.onlineUser)}">
					<!-- 此處不需要提醒到另一個 Contest 交卷了。直接參加即可。 -->
					<!-- <c:if test="${contest.isJoinedOtherContest(sessionScope.onlineUser)}">
						<h2>
							您已經參加[
							<a href="./ShowContest?contestid=${sessionScope.onlineUser.joinedcontestid}">${sessionScope.onlineUser.joinedContest.title}</a>] 當中，請前往交卷後再參加。
						</h2>
						<hr />
					</c:if> -->
					<!-- <c:set var="contest" value="${contest}" scope="request" />
					<jsp:include page="include/JoinContestButton.jsp" /> -->
				</c:when>
				<c:when test="${contest.isStarting && contest.isJoinedThisContest(sessionScope.onlineUser)}">
					<fmt:message key="Contest.JoinInSuspendingContest1" />
					<br />
					<fmt:message key="Contest.JoinInSuspendingContest2">
						<fmt:param value="${contest.starttime}" />
					</fmt:message>
					<br />
					<br />
					<br />
					<c:if test="${contest.visible_LeaveContest(sessionScope.onlineUser)}">
						<form action="./Contest.api" method="post">
							<input name="action" type="hidden" value="leave" />
							<input name="account" type="hidden" value="${sessionScope.onlineUser.account}" />
							<input name="contestid" type="hidden" value="${param.contestid}" />
							<input type="submit" name="submit" class="button" value="暫時離開" />
						</form>
					</c:if>
				</c:when>
				<c:when test="${contest.visible_ContestProblems(sessionScope.onlineUser)}">
					<jsp:include page="include/ContestMenu_Bootstrap.jsp" />
					<table class="table table-hover">
						<tr>
							<td>
								<div style="text-align: left;">題目編號</div>
							</td>
							<td>&nbsp;</td>
							<td width="60%">
								<div style="text-align: left;">
									<fmt:message key="Contest.Problem" />
								</div>
							</td>
							<td>配分</td>
							<td>&nbsp;</td>
						</tr>
						<c:forEach var="problem" items="${contest.problems}" varStatus="varstatus">
							<tr>
								<td>
									<fmt:message key="Contest.ProblemCount">
										<fmt:param value="${varstatus.count}" />
									</fmt:message>
								</td>
								<td>&nbsp;</td>
								<td width="60%">
									<c:set var="contest" value="${contest}" scope="request" />
									<c:set var="contestant"
										value="${contest.getContestantByUserid(sessionScope.onlineUser.id)}"
										scope="request" />
									<c:set var="statuscount" value="${varstatus.count}" scope="request" />
									<button class="btn btn-default btn-xs">
										<jsp:include page="include/div/ContestScore.jsp" />
									</button>
									<c:set var="problem" value="${problem}" scope="request" />
									<jsp:include page="include/div/ProblemDisplay.jsp" />
									<jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
								</td>
								<td>
									${contest.scores[varstatus.count-1]}/${contest.totalScore }</td>
								<td>
									<c:if test="${contest.getIsOwner(sessionScope.onlineUser)}">
										<a href="./ContestSubmissions?contestid=${param.contestid}&problemid=${problem.problemid}">解題列表</a>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</table>
					<br />
					<br />
					<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
						<button type="button" class="btn btn-success" data-toggle="modal" data-target="#Modal_confirm"
							data-title="確定要交卷嗎？ (${contest.title})" data-content="交卷後就無法再進入 「${contest.title}」了，確定嗎?"
							data-type="POST" data-url="./Contest.api"
							data-qs="action=finish&contestid=${contest.id}&userid=${sessionScope.onlineUser.id}">
							做完交卷(交卷之後將無法再進入)</button>
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test="${contest.isSuspending }">
						<fmt:message key="Contest.ContestSuspendingPlaceWait" />
					</c:if>
					<c:if test="${contest.isPausing }">
						<fmt:message key="Contest.ContestPausingPlaceWait" />
					</c:if>
					<br />
					<br />
					<c:if test="${contest.visible_LeaveContest(sessionScope.onlineUser)}">
						<form action="./Contest.api" method="post">
							<input name="action" type="hidden" value="leave" /> <input name="account" type="hidden"
								value="${sessionScope.onlineUser.account}" /> <input name="contestid" type="hidden"
								value="${param.contestid}" /> <input type="submit" name="submit" class="button"
								value="先離開，稍候再回來。" />
						</form>
					</c:if>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
