<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>
<%@ taglib prefix="contesttoolbar" tagdir="/WEB-INF/tags"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<%-- <script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
 --%>
<script type="text/javascript">
	jQuery(document).ready(function() {
		$("div.panel-collapse:first").collapse();
	});
</script>
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<jsp:include page="include/div/SystemTimeNow.jsp" />
			<a href="./InsertContest" class="btn btn-primary">新增競賽</a>
			<hr />
			<c:if test="${fn:length(contests)==0}">
				<fmt:message key="NO_DATA" />
			</c:if>
			<div class="panel-group" id="accordion" role="tablist"
				aria-multiselectable="true">
				<c:forEach var="contest" items="${contests}" varStatus="varstatus">
					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="heading${contest.id }">
							<h4 class="panel-title">
								${varstatus.count+((pagenum-1)*applicationScope.appConfig.pageSize)}:
								<a href="#contestid_${contest.id}" data-toggle="collapse"
									data-parent="#accordion" aria-expanded="false"
									aria-controls="collapse">#${contest.id}
									${fn:escapeXml(contest.title)}</a> BY
								<c:set var="user" value="${contest.owner}" scope="request" />
								<jsp:include page="include/div/UserAccount_TypeA.jsp" />
							</h4>
						</div>
						<div id="contestid_${contest.id}" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="heading${contest.id }">
							<div class="panel-body">
								<div class="col-md-7">
									<c:forEach var="problem" items="${contest.problems}">
										<c:set var="problem" value="${problem}" scope="request" />
										<jsp:include page="include/div/ProblemDisplay.jsp" />
										<jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
										<br />
									</c:forEach>
								</div>
								<div class="col-md-5">
									<span id="contestid" style="display: none;">${contest.id}</span>
									<c:set var="contest" value="${contest}" scope="request" />
									<jsp:include page="include/ContestInfo.jsp" />
									<br />
									<br />
									<c:set var="contest" value="${contest}" scope="request" />
									<jsp:include page="include/ContestToolbar_Bootstrap.jsp" />
								</div>
							</div>
						</div>
					</div>


					<%-- 					<fieldset style="text-align: left; margin: auto; width: 80%;">
						<legend>
							${varstatus.count+((pagenum-1)*applicationScope.appConfig.pageSize)}:
							<a href="./ShowContest?contestid=${contest.id}">${fn:escapeXml(contest.title)}</a>
							BY <a href="UserStatistic?account=${contest.owner.account}">${contest.owner}</a>
							: ${contest.visible }
						</legend>
						<span style="float: left;"> <c:forEach var="problem"
								items="${contest.problems}">
								<c:set var="problem" value="${problem}" scope="request" />
								<jsp:include page="include/div/ProblemDisplay.jsp" />
								<jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
								<br />
							</c:forEach>
						</span>
						<div class="TeachersOnly" style="float: right; text-align: left;">
							<span id="contestid" style="display: none;">${contest.id}</span>
							<c:set var="contest" value="${contest}" scope="request" />
							<jsp:include page="include/ContestInfo.jsp" />

							<span>${contest:getPausepoints(sessionScope.onlineUser,
							contest)}</span>
							<br /> <br />
							<c:set var="contest" value="${contest}" scope="request" />
							<jsp:include page="include/ContestToolbar.jsp" />

						</div>
						<br />
					</fieldset>
					<br /> --%>
				</c:forEach>
			</div>
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
			<br />
		</div>
		<p></p>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
