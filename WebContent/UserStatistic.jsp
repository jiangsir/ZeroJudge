<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/include/UserToolbar.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ShowIMessages.js?${applicationScope.built }"></script>

</head>

<body>
	<jsp:include page="/include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<c:if test="${user==null}">
				<fmt:message key="NO_DATA" />
			</c:if>

			<div class="col-md-3">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<fmt:message key="User.BasicInfomation" />
						</h3>
					</div>
					<div class="panel-body">
						<c:set var="user" value="${user}" scope="request" />
						<jsp:include page="/include/div/UserStatisticInfo.jsp" />
						<div>
							<fmt:message key="User.AC" />
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}&status=AC">${user.ac}</a>
							<fmt:message key="User.Ti" />
							(
							<fmt:formatNumber value="${user.ac/problemnum}" type="percent" />
							) <br />
							<fmt:message key="User.WA" />
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}&status=WA">${user.wa}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.TLE" />
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}&status=TLE">${user.tle}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.MLE" />
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}&amp;status=MLE">${user.mle}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.OLE" />
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}&amp;status=OLE">${user.ole}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.RE" />
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}&status=RE">${user.re}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.CE" />
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}&status=CE">${user.ce}</a>
							<fmt:message key="User.Times" />
						</div>
						<c:if
							test="${sessionScope.onlineUser.isHigherEqualThanMANAGER || sessionScope.onlineUser.isProblemLevelManager()}">
							<div>
								難度統計：<br />
								<c:forEach var="difficulty" items="${allDifficultyCounts }"
									varStatus="status">
									<c:if test="${difficulty.key==0}">未分級：</c:if>
									<c:if test="${difficulty.key>0}">
										APCS ${difficulty.key}級：</c:if> (${difficultyCounts[difficulty.key]} / <a
										href="./Problems?difficulty=${difficulty.key}">${difficulty.value}</a>)
							<c:if test="${difficulty.value!=0}">
										<fmt:formatNumber type="percent" maxIntegerDigits="3"
											value="${difficultyCounts[difficulty.key]/difficulty.value}" />
									</c:if>
									<c:if test="${difficulty.value==0}">	0%</c:if>

									<br />
								</c:forEach>
							</div>
						</c:if>
						<div>
							<a
								href="${pageContext.request.contextPath}/Submissions?account=${user.account}"
								class="btn btn-success">解題列表</a>
						</div>
						<c:set var="user" value="${user}" scope="request" />
						<jsp:include page="/include/UserToolbar_Bootstrap.jsp" /></div>
					<div class="panel-footer"></div>
				</div>
			</div>
			<div class="col-md-9">
				<c:forEach var="tab" items="${tabs}">

					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 class="panel-title">${tab.key}(${fn:length(tab.value)})</h3>
						</div>
						<div class="panel-body">
							<c:forEach var="tab_problemid" items="${tab.value}">
								<c:choose>
									<c:when test="${tab_problemid.value==1}">
										<a
											href="${pageContext.request.contextPath}/Submissions?problemid=${tab_problemid.key}&account=${user.account}"
											id="acstyle" class="acstyle"
											title="${fn:escapeXml(applicationScope.CacheProblems[tab_problemid.key].title)}">${tab_problemid.key}</a>
									</c:when>
									<c:when test="${tab_problemid.value==2}">
										<a
											href="${pageContext.request.contextPath}/Submissions?problemid=${tab_problemid.key}&account=${user.account}"
											style="color: #666666; font-weight: bold;"
											title="${fn:escapeXml(applicationScope.CacheProblems[tab_problemid.key].title)}">${tab_problemid.key}</a>
									</c:when>
									<c:otherwise>
										<a
											href="${pageContext.request.contextPath}/ShowProblem?problemid=${tab_problemid.key}"
											style="color: #666666"
											title="${fn:escapeXml(applicationScope.CacheProblems[tab_problemid.key].title)}">${tab_problemid.key}</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</div>
						<div class="panel-footer">
							<span id="acstyle">a001</span>: 表示通過 | <span
								style="color: #666666; font-weight: bold;">a001</span>: 表示嘗試但未通過
							| <span>a001</span>: 不曾嘗試
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
	<jsp:include page="/include/Footer.jsp" />
</body>
</html>
