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
<script type="text/javascript"
	src="include/div/BannedIP.js?${applicationScope.built }"></script>
<style type="text/css" media="screen">
.tr_background {
	background-color: #f2f2f2;
}
</style>

<script type="text/javascript">
	jQuery(document).ready(function() {
		$("tr").hover(function() {
			$(this).addClass("tr_background");
		}, function() {
			$(this).removeClass("tr_background");
		});
	});
</script>
</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<p>線上的使用者 ${fn:length(onlineUserSessionScopes)}</p>
			<table class="table table-hover">
				<tr>
					<td><strong>userid</strong></td>
					<td>onlineUser</td>
					<td>session_ipset</td>
					<td width="50%">returnPage</td>
					<td>競賽</td>
					<td>IdleMIN</td>
					<td>動作</td>
				</tr>
				<c:forEach var="onlineUserSessionScope"
					items="${onlineUserSessionScopes}">
					<tr>
						<td>${onlineUserSessionScope.onlineUser.id}</td>
						<td><c:set var="user"
								value="${onlineUserSessionScope.onlineUser}" scope="request" />
							<jsp:include page="include/div/UserAccount.jsp" /><br />
							${onlineUserSessionScope.onlineUser.schoolname } </td>
						<td>
							<%-- 					onlineUser.ipaddress=${onlineUserSessionScope.onlineUser.ipaddress}<br />
						sessionScope.session_ipset=${onlineUserSessionScope.session_ipset}<br />
 --%> <%-- <a
						href="http://api.hostip.info/get_html.php?ip=${onlineuser.session_ip}&position=true">
							<img
							src="http://api.hostip.info/flag.php?ip=${onlineuser.session_ip}"
							alt="?" height="16" border="0">
					</a> --%> <%-- <c:set var="ipset"
							value="${onlineUserSessionScope.session_ipset}" scope="request" />
						<jsp:include page="include/div/BannedIP.jsp" /> --%>
							${onlineUserSessionScope.session_ipset}
						</td>
						<td width="50%"><c:forEach var="returnPage"
								items="${onlineUserSessionScope.returnPages}">
								<a href="./${returnPage}" style="font-size: xx-small">${returnPage}</a>
								<br />
							</c:forEach></td>
						<td>${onlineUserSessionScope.onlineUser.joinedcontestid}</td>
						<td><fmt:formatNumber
								value="${onlineUserSessionScope.onlineUser.idle/60000}"
								pattern="#####" /> 分鐘</td>
						<td><a
							href="ShowSessions?sessionid=${onlineUserSessionScope.sessionid}">session</a>|
							<a
							href="./Logout?account=${onlineUserSessionScope.onlineUser.account}&sessionid=${onlineUserSessionScope.sessionid}">登出</a>
						</td>
					</tr>
				</c:forEach>
			</table>
			<hr>
			<div>統計：${fn:length(sortediplist)}</div>
			<div>
				<c:forEach var="sortedip" items="${sortediplist}">
					<c:set var="ip" value="${sortedip.key }" scope="request" />
					<jsp:include page="include/div/BannedIP.jsp" />
			: ${fn:length(sortedip.value) }<br />
				</c:forEach>
			</div>
			<hr>
			<div>所有進站者 ${fn:length(onlineSessionScopes)}</div>
			<table class="table table-hover">
				<tr>
					<td>onlineUser</td>
					<td>session_ipset</td>
					<td>session_returnPage</td>
					<td>Idle</td>
					<td>操作</td>
					<td>requestheaders</td>
				</tr>
				<c:forEach var="onlineSessionScope" items="${onlineSessionScopes}">
					<tr>
						<td title="${onlineSessionScope.sessionid }">${onlineSessionScope.onlineUser.account}</td>
						<td><c:forEach var="ip"
								items="${onlineSessionScope.session_ipset}">
								<c:set var="ip" value="${ip}" scope="request" />
								<jsp:include page="include/div/BannedIP.jsp" /></c:forEach></td>
						<td width="50%"><c:forEach var="returnPage"
								items="${onlineSessionScope.returnPages}">
								<a href="./${returnPage}">${returnPage}</a>
								<br />
							</c:forEach></td>
						<td>${onlineSessionScope.idle_min}分鐘</td>
						<td><a
							href="ShowSessions?sessionid=${onlineSessionScope.sessionid}">session</a>|
							<a href="KillSession?sessionid=${onlineSessionScope.sessionid}">刪除</a></td>
						<td>${onlineSessionScope.session_requestheaders["user-agent"]}<br />
							${onlineSessionScope.session_requestheaders["cookie"]}
						</td>
					</tr>
				</c:forEach>
			</table>
			<p>&nbsp;</p>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
