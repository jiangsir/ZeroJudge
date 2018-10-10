<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />


<div class="container-fluid">
	<div class="row">
		<div class="col-md-12">
			<h3 class="text-center">
				<a href="ShowContest?contestid=${contest.id}" title="The Contest was created By [${contest.owner.account}]">${fn:escapeXml(contest.title)}</a>
			</h3>
			<div class="row">
				<div class="col-md-8">
					<c:if test="${fn:escapeXml(fn:trim(contest.subtitle))!=''}">
						<pre>${fn:escapeXml(fn:trim(contest.subtitle))}</pre>
					</c:if>
				</div>
				<div class="col-md-4">
					<c:set var="contest" value="${contest}" scope="request" />
					<jsp:include page="div/SystemTimeNow.jsp" />
					<jsp:include page="ContestInfo.jsp" />
					<jsp:include page="ContestToolbar_Bootstrap.jsp" />
				</div>
				
			</div>
		</div>
	</div>
</div>


<%-- <div id="contestheader">
	<h2>
		<a href="ShowContest?contestid=${contest.id}">${fn:escapeXml(contest.title)}</a>
	</h2>
		<div
		style="width: 80%; margin: auto; text-align: center; font: bolder; font-size: 20px;">${fn:escapeXml(contest.title)}</div>

	<div class="subtitle">
		<div
			style="float: right; width: 25%; margin: auto; text-align: left; font-size: smaller; vertical-align: top;">
			<jsp:include page="ContestInfo.jsp" />
		</div>
		<div style="margin: 10px;">
			<pre>${fn:escapeXml(contest.subtitle)}</pre>
		</div>
		<div style="clear: both;"></div>
	</div>
	
	<c:if test="${contest.isRunning && contest.checkedConfig_AutoRunning}">
		<span id="countdown">${contest.countdown}</span>
	</c:if>
		<c:if
		test="${!contest:isFreezen(sessionScope.onlineUser, contest) && contest.freezelimit>0}">
		<div style="color: #FF3333;">將於競賽結束前
			${contest.freezelimit}分鐘停止更新其他使用者的解題動態</div>
	</c:if>
	<c:if test="${contest:isFreezen(sessionScope.onlineUser, contest)}">
		<div style="color: #FF3333; text-decoration: blink">已停止更新其它使用者解題動態。</div>
	</c:if>

</div> --%>
