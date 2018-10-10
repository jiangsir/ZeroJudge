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
	src="include/UserToolbar.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="ShowIMessages.js?${applicationScope.built }"></script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
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
						<c:if test="${user.isAuthhost_Google }">
							<img src="${user.pictureBase64 }"
								class="img-responsive ">
						</c:if>
						<fmt:message key="User.ID" />
						: ${user.id}<br />
						<fmt:message key="User.Account" />
						:
						<c:set var="user" value="${user}" scope="request" />
						<jsp:include page="include/div/UserAccount.jsp" />
						<button type="button" class="btn btn-default btn-xs"
							data-target="#Modal_SendIMessage"
							data-receiver="${user.account }">
							<i class="fa fa-envelope" aria-hidden="true"></i>
						</button>
						<jsp:include page="include/Modals/Modal_SendIMessage.jsp" />
						<br />
<%-- 						<fmt:message key="User.Username" />
						: ${fn:escapeXml(user.username)}<br />
 --%>						<fmt:message key="User.School" />
						:
						<c:choose>
							<c:when test="${user.schoolid==0}">
								<fmt:message key="User.NotStudent" />
							</c:when>
							<c:otherwise>${fn:escapeXml(user.schoolname)}

							</c:otherwise>
						</c:choose>
						<br />
						<fmt:message key="User.IPaddress" />
						: ${user.ipset} <br />
						<fmt:message key="User.LastLogin" />
						：<br />
						<fmt:formatDate value="${user.lastlogin}"
							pattern="yyyy-MM-dd HH:mm:ss" />
						<br />
						<p align="left">
							<fmt:message key="User.AC" />
							<a href="./Submissions?account=${user.account}&status=AC">${user.ac}</a>
							<fmt:message key="User.Ti" />
							(
							<fmt:formatNumber value="${user.ac/problemnum}" type="percent" />
							) <br />
							<fmt:message key="User.WA" />
							<a href="./Submissions?account=${user.account}&status=WA">${user.wa}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.TLE" />
							<a href="./Submissions?account=${user.account}&status=TLE">${user.tle}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.MLE" />
							<a href="./Submissions?account=${user.account}&amp;status=MLE">${user.mle}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.OLE" />
							<a href="./Submissions?account=${user.account}&amp;status=OLE">${user.ole}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.RE" />
							<a href="./Submissions?account=${user.account}&status=RE">${user.re}</a>
							<fmt:message key="User.Times" />
							<br />
							<fmt:message key="User.CE" />
							<a href="./Submissions?account=${user.account}&status=CE">${user.ce}</a>
							<fmt:message key="User.Times" />
						</p>
						<a href="Submissions?account=${user.account}"
							class="btn btn-success">解題列表</a>
						<c:set var="user" value="${user}" scope="request" />
						<jsp:include page="include/UserToolbar_Bootstrap.jsp" /></div>
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
											href="./Submissions?problemid=${tab_problemid.key}&account=${user.account}"
											id="acstyle" class="acstyle"
											title="${fn:escapeXml(applicationScope.CacheProblems[tab_problemid.key].title)}">${tab_problemid.key}</a>
									</c:when>
									<c:when test="${tab_problemid.value==2}">
										<a
											href="./Submissions?problemid=${tab_problemid.key}&account=${user.account}"
											style="color: #666666; font-weight: bold;"
											title="${fn:escapeXml(applicationScope.CacheProblems[tab_problemid.key].title)}">${tab_problemid.key}</a>
									</c:when>
									<c:otherwise>
										<a href="./ShowProblem?problemid=${tab_problemid.key}"
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
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
