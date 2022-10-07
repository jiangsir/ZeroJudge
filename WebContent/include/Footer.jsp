<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>
<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>

<!-- <blockquote class="blockquote-reverse">
	-->
<hr>

<footer class="text-right">


	<div class="well">
		<c:if test="${fn:length(applicationScope.appConfig.managers)>0}">
			站務管理員：
			<c:forEach var="manager" items="${applicationScope.appConfig.managers}">
				<a href="./UserStatistic?account=${manager}">${manager}</a> |
			</c:forEach>
		</c:if>
		${applicationScope.memoryInfo } |
		<c:if test="${ms!=null}">
			<span>${now.time-ms}ms</span>
		</c:if>
		| ${fn:length(applicationScope.onlineUsers)} 人上線 |
		Queue:${applicationScope.appConfig.judgeQueueSize}<br> Powered by
		<a href="http://zerojudge.tw/" target="_blank">ZeroJudge</a>
		<span title="${applicationScope.version}">${fn:split(applicationScope.version, ' ')[0]}</span> |
		<span
			title="${applicationScope.appConfig.serverConfig.version}">${fn:split(applicationScope.appConfig.serverConfig.version,
			' ')[0]}</span> | ${applicationScope.built}
	</div>
</footer>
