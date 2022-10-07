<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="container-fluid">
	<div class="row">
		<div class="well well-sm">
			<!-- <strong>${fn:escapeXml(contest.title)}</strong> -->
			<h4>${fn:escapeXml(contest.title)}</h4>
			<c:if test="${fn:escapeXml(fn:trim(contest.subtitle))!=''}">
				<pre class="ContestHeader">${fn:escapeXml(fn:trim(contest.subtitle))}</pre>
			</c:if>
			<!-- <jsp:include page="JoinContestButton.jsp" /> -->
			<c:if test="${contest.isStarting || contest.isRunning}">
				<div id="JoinContest">
					<button class="btn btn-primary btn-sm" id="doJoinContestByOnlineUser" data-contestid=${contest.id}>
						參加${contest.getBundle_Contest(pageContext.session)}</button>
				</div>
			</c:if>
		</div>
	</div>
</div>
