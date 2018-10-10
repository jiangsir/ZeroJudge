<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="solution" uri="http://jiangsir.tw/jstl/solution"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"><jsp:include
	page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="Submissions.js?${applicationScope.built }"></script>
<%-- <script type="text/javascript"
	src="doRejudge.js?${applicationScope.built }"></script>
 --%>
<!-- <script type="text/javascript" src="showDetails.js"></script>
 -->
<script type="text/javascript"
	src="include/dialog/ShowDetail.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/ManualJudge.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/ShowCode.js?${applicationScope.built }"></script>
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
				<li class="active"><a href="./Submissions#tab01"
					aria-controls="tab01" role="presentation"><fmt:message
							key="Status.Submissions" /></a></li>
				<c:forEach var="compiler"
					items="${applicationScope.appConfig.serverConfig.enableCompilers}"
					varStatus="varstatus">
					<li><a
						href="?language=${compiler.language}${querystring}#tab_${compiler.language}"
						aria-controls="tab_${compiler.language}" role="presentation">${compiler.language}</a>
					</li>
				</c:forEach>

				<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
					<li><a href="?status=RF#tab_RF" aria-controls="tab_RF"
						role="presentation">RF</a></li>
					<li><a href="?status=SE#tab_SE" aria-controls="tab_SE"
						role="presentation">SE</a></li>
					<li><a href="?status=Waiting#tab_Waiting"
						aria-controls="tab_Waiting" role="presentation">Waiting</a></li>
					<%--                <li class="tab14"><a href="?codelocker=0${querystring}">開源</a></li>
 --%>
				</c:if>
			</ul>
			<br>
			<table class="table table-hover">
				<tr>
					<td width="8%"><fmt:message key="Status.ID" /></td>
					<td width="15%"><fmt:message key="Status.User" /></td>
					<td><fmt:message key="Status.Problem" /></td>
					<td width="18%"><fmt:message key="Status.Result" /></td>
					<td width="10%"><fmt:message key="Status.Code" /></td>
					<td width="10%"><fmt:message key="Status.Time" /></td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(solutions)!=0}">
						<c:forEach var="solution" items="${solutions}"
							varStatus="varstatus">
							<tr solutionid="${solution.id}">
								<c:if test="${param.solutionid==solution.id}">
									<td id="solutionid" style="font-weight: bold;">${solution.id}</td>
								</c:if>
								<c:if test="${param.solutionid!=solution.id}">
									<td id="solutionid">${solution.id}</td>
								</c:if>
								<td><c:set var="user" value="${solution.user}"
										scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /> </td>
								<td width="38%"><c:set var="problem"
										value="${solution.problem}" scope="request" /> <jsp:include
										page="include/div/ProblemTitle.jsp" /></td>
								<td width="12%"><c:set var="solution" value="${solution}"
										scope="request" /> <jsp:include
										page="include/div/DivSolutionStatusInfo_Bootstrap.jsp" /></td>
								<td><c:set var="solution" value="${solution}"
										scope="request" /> <jsp:include
										page="include/div/SolutionCode.jsp" /></td>
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
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
			<form name="form1" method="post" action="./Submissions"
				style="margin: 0px; display: inline;" onsubmit="checkForm(this);">
				<fmt:message key="MOVETO_CODEID" />
				<input name="solutionid" type="text" id="solutionid" size="8"
					maxlength="11" style="display: inline" />
			</form>

		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
