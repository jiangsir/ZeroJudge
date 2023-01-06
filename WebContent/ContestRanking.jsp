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
	<script type="text/javascript" src="ShowContest.js?${applicationScope.built}"></script>
</head>

<body class="Consolas">
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<jsp:include page="include/ContestHeader.jsp" />

			<jsp:include page="include/ContestMenu_Bootstrap.jsp" />
			<c:if test="${contest.rankingmodeInfo!=''}">
				<div class="contestbox">${contest.rankingmodeInfo}</div>
			</c:if>

			<table class="table table-hover">
				<tr>
					<td width="5%" style="vertical-align: bottom">S/N</td>
					<td width="30%" style="vertical-align: bottom">
						<fmt:message key="Contest.User" />
					</td>
					<c:set var="problemids" value="" />
					<c:if test="${contest.isRunning || contest.isStopped}">
						<c:set var="problemids" value="${contest.problemids}" />
					</c:if>
					<c:forEach var="problem" items="${contest.problems}" varStatus="vstatus">
						<td style="vertical-align: bottom; text-align: center">
							<div>
								<a href="./ContestSubmissions?contestid=${contest.id}&problemid=${problem.problemid}"
									title="${fn:escapeXml(problem.title)}">
									<fmt:message key="Contest.ProblemCount">
										<fmt:param value="${vstatus.count}" />
									</fmt:message>
								</a>
							</div> <span style="text-align: center; font-size: smaller">${contest.scores[vstatus.count-1]}分</span>
						</td>
					</c:forEach>
					<td width="7%" style="vertical-align: bottom; text-align: right" title="多次送出時以最高分為準">
						<fmt:message key="Contest.Score" />
					</td>
					<td width="6%" style="vertical-align: bottom; text-align: right">
						AC數
					</td>
					<td width="6%" style="vertical-align: bottom; text-align: right" title="第一次AC時間 + 第一次AC前錯誤次數*20">
						耗時
					</td>
					<td width="6%" class="text-center" style="vertical-align: bottom;" title="第一順位: 分數，第二順位: 答對，第三順位: 耗時">
						<fmt:message key="Contest.Ranking" />
					</td>
					<c:if test="${contest.isVContest() && contest.getVClass().getIsOwner(sessionScope.onlineUser)}">
						<td width="8%" style="vertical-align: bottom; text-align: right;">註解</td>
					</c:if>
				</tr>
				<c:forEach var="contestant" items="${contestants}" varStatus="status">
					<tr>
						<td width="5%">${status.count}</td>
						<td width="30%">
							<c:if test="${contestant.user.id==0}">
								<div><span title="teamaccount">${fn:escapeXml(contestant.teamaccount)}</span> <span
										title="teamname">(${fn:escapeXml(contestant.teamname)})</span></div>
							</c:if>
							<c:if test="${contestant.user.id!=0}">
								<c:set var="user" value="${contestant.user}" scope="request" />
								<jsp:include page="include/div/UserAccount_TypeA.jsp" />
								<!-- <a href="./ContestSubmissions?userid=${contestant.user.id}&contestid=${contest.id}"
									class="btn btn-default btn-xs">S</a> -->
							</c:if>
						</td>
						<c:forEach var="problemid" items="${contest.problemids}" varStatus="status">
							<td>
								<div align="center" style="font-family: Consolas, 'Courier New', monospace;">
									<c:set var="contest" value="${contest}" scope="request" />
									<c:set var="contestant" value="${contestant}" scope="request" />
									<c:set var="statuscount" value="${status.count}" scope="request" />
									<jsp:include page="include/div/ContestScore.jsp" />
								</div>
							</td>
						</c:forEach>
						<td width="7%">
							<c:if test="${contest.getIsOwner(sessionScope.onlineUser) || contest.checkedConfig_ShowResult}">
								<div class="Consolas text-right" style="font-weight: bold;" title="${contestant.score}">
									<fmt:formatNumber value="${contestant.score}" pattern="#.#" />
								</div>
							</c:if>
						</td>
						<td width="6%">
							<div class="Consolas text-right">
								<c:if test="${contest.getIsOwner(sessionScope.onlineUser) || contest.checkedConfig_ShowResult}">
									${contestant.ac}
								</c:if>
							</div>
						</td>
						<td width="6%" class="text-right" style="font-size: smaller;">
							${contestant.penalty}</td>
						<td width="6%" class="text-center" style="font-weight: bold;">
							<a href="./ContestSubmissions?userid=${contestant.userid}&contestid=${contest.id}"
										class="btn btn-default btn-xs">${contestant.curr_rank}</a></td>
						<c:if test="${contest.isVContest() && contest.getVClass().getIsOwner(sessionScope.onlineUser)}">
							<td class="Consolas text-right">
								<span>${contestant.toStudent().getComment()}</span>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
			<c:if test="${fn:length(contestants)==0}">
				<br />
				<div align="center">
					<fmt:message key="NO_DATA" />
				</div>
			</c:if>
			<br />
			<hr />
		</div>

	</div>
	<jsp:include page="include/Footer.jsp" />
</body>

</html>
