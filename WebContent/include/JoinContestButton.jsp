<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>

<c:if test="${contest.isStarting || contest.isRunning}">
	<div id="JoinContest">
		<button class="btn btn-success" id="doJoinContestByOnlineUser"
			data-contestid=${contest.id}>
			參加${contest.getBundle_Contest(pageContext.session)}</button>
	</div>
</c:if>
