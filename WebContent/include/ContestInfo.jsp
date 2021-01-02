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
			<jsp:include page="div/TimeFormat_TypeA.jsp">
				<jsp:param name="mstime" value="${contest.runningtimeTotal}" />
			</jsp:include></c:when>
		<c:when test="${contest.isStopped}">
	${contest.getBundle_Contest(pageContext.session)}結束時間：${contest.stoptime }<br />
		</c:when>
	</c:choose>
	<br /> ${contest.getBundle_Contest(pageContext.session)}
	<fmt:message key="${contest.conteststatus.value}" />
	<br />
	<c:if test="${contest.getIsOwner(sessionScope.onlineUser) }">
		<div class="alert alert-success" role="alert">${contest.pausepoints_HTML}
		</div>
	</c:if>
	<c:if test="${!contest.checkedConfig_MultiSubmit }">
		<div class="alert alert-danger">
			請注意：本${contest.getBundle_Contest(pageContext.session)}設定「不允許」重複送出。</div>
	</c:if>
	<hr>
</div>
