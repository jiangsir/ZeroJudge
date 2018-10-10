<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<script type="text/javascript"
	src="./jscripts/jquery.timeout.interval.idle.js"></script>
<script type="text/javascript"
	src="BestSolutions.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/ShowCode.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="include/dialog/ShowDetail.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="doRejudge.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/ManualJudge.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/DivSolutionStatusInfo.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/SolutionCode.js?${applicationScope.built }"></script>

<script type="text/javascript">
	jQuery(document).ready(
			function() {
				var url = document.location.toString();
				console.log("url=" + url);
				if (url.match('#')) {
					console.log("url.split('#')[1]=" + url.split('#')[1]);
					console.log('.nav-tabs a[aria-controls="'
							+ url.split('#')[1] + '"]');
					//$('.nav-tabs').find('[aria-controls="' + url.split('#')[1] + '"]').tab('show');
					$('.nav-tabs a[aria-controls="' + url.split('#')[1] + '"]')
							.tab('show');
				}
			});
</script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<ul class="nav nav-tabs" role="tablist">
				<c:forEach var="compiler"
					items="${applicationScope.appConfig.serverConfig.enableCompilers}"
					varStatus="varstatus">
					<li><a
						href="?problemid=${param.problemid}&language=${compiler.language}#tab_${compiler.language}"
						aria-controls="tab_${compiler.language}" role="presentation">${compiler.language}</a>
					</li>
				</c:forEach>
			</ul>
			<br>
			<table class="table table-hover">
				<tr>
					<td><fmt:message key="Status.ID" /></td>
					<td width="20%"><fmt:message key="Status.User" /></td>
					<td width="22%"><fmt:message key="Status.Result" /></td>
					<td><fmt:message key="Status.Code" /></td>
					<td>程式碼</td>
					<td><fmt:message key="Status.Time" /></td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(solutions)!=0}">
						<c:forEach var="solution" items="${solutions}"
							varStatus="varstatus">
							<tr>
								<c:if test="${param.solutionid==solution.id}">
									<td id="solutionid" style="font-weight: bold;">${solution.id}</td>
								</c:if>
								<c:if test="${param.solutionid!=solution.id}">
									<td id="solutionid">${solution.id}</td>
								</c:if>
								<td><c:set var="user" value="${solution.user}"
										scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /> <%-- <c:if
										test="${sessionScope.onlineUser.id==solution.userid}">
										<a href="./UserStatistic?id=${solution.userid}"
											style="font-weight: bolder">${solution.user.account}</a>
									</c:if> <c:if
										test="${sessionScope.onlineUser.id!=solution.userid}">
										<a href="./UserStatistic?id=${solution.userid}">${solution.user.account}</a>
									</c:if> --%></td>
								<td><c:set var="solution" value="${solution}"
										scope="request" /> <jsp:include
										page="include/div/DivSolutionStatusInfo_Bootstrap.jsp" /></td>
								<td><c:set var="solution" value="${solution}"
										scope="request" /> <jsp:include
										page="include/div/SolutionCode.jsp" /></td>
								<td><span style="font-size: smaller"><fmt:formatNumber
											value="${solution.codelength}" pattern="####,###,###" />
										Bytes</span></td>
								<td style="font-size: smaller"><fmt:formatDate
										value="${solution.submittime}" pattern="yyyy-MM-dd HH:mm" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="7"><div align="center">
									<fmt:message key="NO_DATA" />
								</div></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
			<br />
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
