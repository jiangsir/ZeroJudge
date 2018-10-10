<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />


<script type="text/javascript"
	src="jscripts/jquery.timeout.interval.idle.js"></script>
<script type="text/javascript"
	src="ShowContest.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="Contests.js?${applicationScope.built }"></script>

</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<c:forEach var="runningcontest" items="${runningContests}">
				<c:set var="contest" value="${runningcontest}" scope="request" />
				<jsp:include page="include/ContestHeader.jsp" />
			</c:forEach>
			<c:if test="${fn:length(pausingContests)!=0}">
				<hr />
				<h2>${fn:length(pausingContests)}個競賽已暫停</h2>
				<table class="table table-hover">
					<tr>
						<td  class="col-md-3"><fmt:message key="Contest.Owner" /></td>
						<td  class="col-md-5"><fmt:message key="Contest.Descript" /></td>
						<td  class="col-md-2"><div align="center">
								<fmt:message key="Contest.StartTime" />
							</div></td>
						<td  class="col-md-1"><fmt:message key="Contest.ContestPeriod" /></td>
						<td  class="col-md-1"><fmt:message key="Contest.NowStatus" /></td>
					</tr>

					<c:forEach var="pausingcontest" items="${pausingContests}">
						<tr>
							<td><c:set var="user" value="${pausingcontest.owner}"
									scope="request" /> <jsp:include
									page="include/div/UserAccount_TypeA.jsp" /></td>
							<td><a href="./ShowContest?contestid=${pausingcontest.id}">${fn:escapeXml(pausingcontest.title)}</a></td>
							<td style="font-size: smaller"><fmt:formatDate
									value="${pausingcontest.starttime}" pattern="yyyy-MM-dd HH:mm" /></td>
							<td><fmt:formatNumber
									value="${(pausingcontest.timelimit-pausingcontest.timelimit%3600000)/3600000}"
									pattern="##" /> 小時 <fmt:formatNumber
									value="${pausingcontest.timelimit%3600000/60000}" pattern="##" />
								分</td>
							<td><fmt:message key="${pausingcontest.conteststatus.value}" /></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>


			<%-- 		</c:if>
 --%>
			<c:if test="${fn:length(startingContests)!=0}">
				<hr />
				<h2>${fn:length(startingContests)}個競賽即將開始</h2>
				<table class="table table-hover">
					<tr>
						<td><fmt:message key="Contest.Owner" /></td>
						<td width="45%"><fmt:message key="Contest.Descript" /></td>
						<td><div align="center">
								<fmt:message key="Contest.StartTime" />
							</div></td>
						<td width="10%"><fmt:message key="Contest.ContestPeriod" /></td>
						<td><fmt:message key="Contest.NowStatus" /></td>
					</tr>
					<c:forEach var="startingcontest" items="${startingContests}">
						<tr>
							<td><c:set var="user" value="${startingcontest.owner}"
									scope="request" /> <jsp:include
									page="include/div/UserAccount_TypeA.jsp" /></td>
							<td><a href="./ShowContest?contestid=${startingcontest.id}">${fn:escapeXml(startingcontest.title)}</a></td>
							<td style="font-size: smaller"><fmt:formatDate
									value="${startingcontest.starttime}" pattern="yyyy-MM-dd HH:mm" /></td>
							<td><fmt:formatNumber
									value="${(startingcontest.timelimit-startingcontest.timelimit%3600000)/3600000}"
									pattern="##" /> 小時 <fmt:formatNumber
									value="${startingcontest.timelimit%3600000/60000}" pattern="##" />
								分</td>
							<td><fmt:message
									key="${startingcontest.conteststatus.value}" /></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<br />
			<hr />
			<h2>
				<fmt:message key="Contest.StoppedContest" />
			</h2>
			<br />
			<table class="table table-hover">
				<tr>
					<td class="col-md-3"><fmt:message key="Contest.Owner" /></td>
					<td class="col-md-6"><fmt:message key="Contest.Descript" /></td>
					<td class="col-md-2"><fmt:message key="Contest.StartTime" /></td>
					<td class="col-md-1"><fmt:message key="Contest.NowStatus" /></td>
				</tr>
				<c:forEach var="stopedcontest" items="${stopedContests}">
					<tr>
						<td><c:set var="user" value="${stopedcontest.owner}"
								scope="request" /> <jsp:include
								page="include/div/UserAccount_TypeA.jsp" /></td>
						<td><c:choose>
								<c:when test="${stopedcontest.checked_Config_Visible}">
									<a href="./ShowContest?contestid=${stopedcontest.id}">${fn:escapeXml(stopedcontest.title)}</a>
								</c:when>
								<c:otherwise>
			${fn:escapeXml(stopedcontest.title)}
			</c:otherwise>
							</c:choose> <span style="font-size: smaller">[${stopedcontest.contestantsCount}]</span></td>
						<td><c:choose>
								<c:when test="${stopedcontest.isStarting}">
									<div align="center">
										<fmt:message key="${stoppedcontest.conteststatus.value}" />
									</div>
								</c:when>
								<c:otherwise>
									<div style="font-size: smaller">
										<fmt:formatDate value="${stopedcontest.starttime}"
											pattern="yyyy-MM-dd HH:mm" />
									</div>
								</c:otherwise>
							</c:choose></td>
						<td style="vertical-align: middle"><c:choose>
								<c:when test="${stopedcontest.isRunning}">
									<a href="./JoinContest?contestid=${stopedcontest.id}"> <fmt:message
											key="Contest.Join" />
									</a>
								</c:when>
								<c:otherwise>
									<fmt:message key="${stopedcontest.conteststatus.value}" />
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</table>
			<c:if
				test="${fn:length(stopedContests)==0 && fn:length(runningContests)==0 && fn:length(startingContests)==0 && fn:length(pausingContests)==0}">
				<div style="font-size: xx-large; margin: 1em;">
					<fmt:message key="NO_DATA" />
				</div>
			</c:if>
			<br />
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
			<br />
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
