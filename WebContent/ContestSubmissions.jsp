<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>
<%@ taglib prefix="solution" uri="http://jiangsir.tw/jstl/solution"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="ContestSubmissions.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="ShowContest.js?${applicationScope.built }"></script>
<%-- <script type="text/javascript"
	src="doRejudge.js?${applicationScope.built }"></script>
 --%>
<!-- <script type="text/javascript" src="showDetails.js"></script>
 -->
<%-- <script type="text/javascript"
	src="include/dialog/ShowDetail.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/ShowCode.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/ManualJudge.js?${applicationScope.built }"></script>
 --%>
<script type="text/javascript"
	src="include/Modals/Modal_ManualJudge.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/DivSolutionStatusInfo.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="jscripts/js_date.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/SolutionCode.js?${applicationScope.built }"></script>

<script type="text/javascript">
	jQuery(document).ready(function() {

	});
</script>
</head>


<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<jsp:include page="include/ContestHeader.jsp" />

			<jsp:include page="include/ContestMenu_Bootstrap.jsp" />
			<table class="table table-hover">
				<tr>
					<td><fmt:message key="Contest.CodeID" /></td>
					<td><fmt:message key="Contest.User" /></td>
					<td width="35%"><fmt:message key="Contest.Problem" /></td>
					<td><fmt:message key="Contest.ContestTime" /></td>
					<c:if
						test="${contest:isVisible_ContestResult(sessionScope.onlineUser,contest)}">
						<td width="16%"><fmt:message key="Contest.Result" /></td>
					</c:if>
					<td><fmt:message key="Contest.Code" /></td>
					<td><fmt:message key="Contest.SubmitTime" /></td>
					<c:if test="${contest.getIsOwner(sessionScope.onlineUser)}">
						<td>操作</td>
					</c:if>
				</tr>
				<c:forEach var="solution" items="${solutions}" varStatus="varstatus">
					<tr solutionid="${solution.id}">
						<!-- ${fn:length(solutions)-varstatus.count+1} -->
						<td title="${solution.id}">${fn:length(solutions)-varstatus.count+1}
							<c:if test="${contest.getIsOwner(sessionScope.onlineUser)}">(${solution.id})</c:if><span
							id="solutionid" style="display: none;">${solution.id}</span>
						</td>
						<td><c:set var="user" value="${solution.user}"
								scope="request" /> <jsp:include
								page="include/div/UserAccount_TypeA.jsp" /> <a
							href="./ContestSubmissions?userid=${solution.userid}&contestid=${contest.id}"
							class="btn btn-default btn-xs">S</a></td>
						<td><c:set var="problem" value="${solution.problem}"
								scope="request" /> <jsp:include
								page="include/div/ProblemTitle_TypeB.jsp" /></td>
						<td><fmt:formatNumber value="${solution.spending/60000}"
								pattern="#####" /> <fmt:message key="Contest.Minute" /></td>
						<c:if
							test="${contest:isVisible_ContestResult(sessionScope.onlineUser, contest)}">
							<td width="12%" solutionid="${solution.id}"><c:set
									var="solution" value="${solution}" scope="request" /> <jsp:include
									page="include/div/DivSolutionStatusInfo_Bootstrap.jsp" /></td>
						</c:if>
						<td><c:set var="solution" value="${solution}" scope="request" />
							<jsp:include page="include/div/SolutionCode.jsp" /></td>
						<td style="font-size: smaller"><fmt:formatDate
								value="${solution.submittime}" pattern="yyyy-MM-dd HH:mm" /></td>
						<td><c:if
								test="${solution.getIsVisible_ManualJudge(sessionScope.onlineUser)}">
								<c:set var="solution" value="${solution}" scope="request" />
								<jsp:include page="include/Modals/Modal_ManualJudge.jsp" />
								<button id="ManualJudge" class="btn btn-default btn-xs"
									data-toggle="modal"
									data-target="#Modal_ManualJudge_${solution.id}" title="手動評分">
									<!-- <i class="fa fa-check-square-o" aria-hidden="true"></i> -->
									<img src="images/validate.svg" style="height: 18px;" />
								</button>


								<!-- <img src="images/validate.svg" style="height: 1.2em;"
										alt="手動評分" id="manualjudge" class="FakeLink" title="手動評分" /> -->
							</c:if></td>
					</tr>
				</c:forEach>
				<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
					<c:forEach var="solution" items="${contest.removedSolutions}"
						varStatus="varstatus">
						<tr solutionid="${solution.id}" class="alert alert-danger">
							<td>${solution.id}</td>
							<td><a href="./UserStatistic?id=${solution.userid}">${solution.user.id}</a></td>
							<td><a href="./ShowProblem?problemid=${solution.problemid}">${fn:escapeXml(applicationScope.CacheProblems[solution.problemid].title)}</a></td>
							<td><fmt:formatNumber value="${solution.spending/60000}"
									pattern="#####" /> <fmt:message key="Contest.Minute" /></td>
							<c:if
								test="${contest.getIsOwner(sessionScope.onlineUser) || sessionScope.onlineUser.isDEBUGGER || contest.checkedConfig_ShowResult}">
								<td width="12%"><c:set var="solution" value="${solution}"
										scope="request" /> <jsp:include
										page="include/div/DivSolutionStatusInfo_Bootstrap.jsp" /></td>
							</c:if>
							<td><c:set var="solution" value="${solution}"
									scope="request" /> <jsp:include
									page="include/div/SolutionCode.jsp" /></td>
							<td style="font-size: smaller"><fmt:formatDate
									value="${solution.submittime}" pattern="yyyy-MM-dd HH:mm" /></td>
							<c:if test="${ sessionScope.onlineUser.isDEBUGGER}">
								<td style="font-size: smaller"><jsp:include
										page="include/dialog/Confirm.jsp">
										<jsp:param name="title" value="確定要使這個程式碼重新計入競賽成績統計?" />
										<jsp:param name="type" value="POST" />
										<jsp:param name="url" value="Solution.api" />
										<jsp:param name="data"
											value="action=recoverSolution&solutionid=${solution.id}" />
									</jsp:include> <img src="images/rejudge2_18.png" id="RecoverSolution"
									class="confirm" title="使這個程式碼重新計入競賽成績統計" /></td>
							</c:if>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<c:if test="${fn:length(solutions)==0}">
				<br />
				<div align="center">
					<fmt:message key="NO_DATA" />
				</div>
			</c:if>
			<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
				<div class="alert alert-danger">testjudgeids:
					${contest.testJudgeids}</div>
			</c:if>
			<hr />
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
