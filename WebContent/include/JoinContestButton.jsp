<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<c:if test="${contest.isStarting || contest.isRunning}">
	<div id="JoinContest">
		<button class="btn btn-primary" id="doJoinContestByOnlineUser" data-contestid=${contest.id}>
			參加${contest.getBundle_Contest(pageContext.session)}</button>
	</div>
</c:if>
