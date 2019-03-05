<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<div id="contestInfo">
	參與人數：${contest.contestantsCount} 人<br />
	<c:if test="${contest.checkedConfig_AutoRunning}">
		<div id="autoRunning">
			進行時間： ${(contest.timelimit - contest.timelimit % 3600000) / 3600000}
			小時 ${contest.timelimit % 3600000 / 60000} 分<br />
		</div>
	</c:if>
	<c:choose>
		<c:when test="${contest.isStarting || contest.isRunning}">
                                開始時間：${contest.starttime }<br />
			<c:if test="${contest.checkedConfig_AutoRunning}">
                結束時間：${contest.stoptime }<br />
			</c:if>
			競賽進行時間：
			<jsp:include page="div/TimeFormat_TypeA.jsp">
				<jsp:param name="mstime" value="${contest.runningtimeTotal}" />
			</jsp:include>
		</c:when>
		<c:when test="${contest.isStopped}">
	結束時間：${contest.stoptime }<br />
		</c:when>
	</c:choose>
	<br /> 競賽
	<fmt:message key="${contest.conteststatus.value}" />
	<br />
	<c:if test="${contest.getIsOwner(sessionScope.onlineUser) }">
		<div class="alert alert-success" role="alert">${contest.pausepoints_HTML}
			<c:if test="${contest.isRunning}">
				<c:set var="runningtime" value="${contest.runningtime }" />
				<c:set var="secs"
					value="${(runningtime - (runningtime % 1000)) / 1000}" />
				<c:set var="mins" value="${(secs - (secs % 60)) / 60}" />
				<c:set var="hours" value="${(mins - (mins % 60)) / 60}" />
				<c:set var="days" value="${(hours - (hours % 24)) / 24}" />
                    競賽已進行：${days}天${hours%24}小時${mins%60}分鐘${secs%60}秒
    	</c:if>
		</div>
	</c:if>
	<c:if test="${!contest.checkedConfig_MultiSubmit }">
		<div class="alert alert-danger">請注意：本競賽設定「不允許」重複送出。</div>
	</c:if>
	<hr>
</div>
