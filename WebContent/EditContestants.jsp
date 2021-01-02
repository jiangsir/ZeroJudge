<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<jsp:include page="include/ContestHeader.jsp" />
			<jsp:include page="include/ContestMenu_Bootstrap.jsp" />


			<!--
  <p>管理參加者 -- 共 ${fn:length(contestants)} 個帳號註冊，共 ${contest.onlineContestantsCount} 已登入，共 ${contest.offlineContestantsCount} 不在線上。其餘則尚未登入過 </p>
  -->
			<table class="table table-hover">
				<tr>
					<th scope="col">ID</th>
					<th width="25%" scope="col">帳號</th>
					<th scope="col">姓名</th>
					<th width="10%" scope="col">IP 來源</th>
					<th scope="col">狀態</th>
					<th scope="col">操作</th>
				</tr>
				<c:forEach var="contestant" items="${contestants}"
					varStatus="varstatus">
					<tr>
						<td title="contestant ID">#${contestant.id}<br> <c:if
								test="${contestant.isOnline}">
								<button type="button" class="btn btn-success btn-xs">ONLINE</button>
							</c:if> <c:if test="${!contestant.isOnline}">
								<button type="button" class="btn btn-danger btn-xs">offline</button>
							</c:if></td>
						<td id="account">#${varstatus.count}: <c:set var="user"
								value="${contestant.user}" scope="request" /> <jsp:include
								page="include/div/UserAccount_TypeA.jsp" /> <c:if
								test="${contestant.school!='' }">
								<br />${fn:escapeXml(contestant.school)}</c:if></td>
						<td>${fn:escapeXml(contestant.teamname)}&nbsp;</td>
						<td width="10%">${contestant.ipset}</td>
						<td>
							<div class="btn-group btn-group-sm" role="group">
								<c:if test="${contestant.isStatusFinish}">
									<button type="button" class="btn btn-primary"
										title="${contestant.status.value}#${contestant.contestid }">${contestant.status.value}</button>
								</c:if>
								<c:if test="${contestant.isStatusKicked}">
									<button type="button" class="btn btn-danger"
										title="${contestant.status.value}#${contestant.contestid }">${contestant.status.value}</button>
								</c:if>
								<c:if test="${contestant.isStatusLeave}">
									<button type="button" class="btn btn-warning"
										title="${contestant.status.value}#${contestant.contestid }">${contestant.status.value}</button>
								</c:if>
							</div> <c:if test="${contestant.isRejoin }">
								<div class="btn-group btn-group-sm" role="group"
									aria-label="...">
									<button type="button" class="btn btn-default"
										data-target="#Modal_confirm" title="恢復使用者/重新加入"
										data-title="恢復使用者/重新加入"
										data-content="恢復 ${contestant.user.account} 將可以重新加入，確定嗎？"
										data-type="POST" data-url="Contest.api"
										data-qs="action=registed&userid=${contestant.userid}&contestid=${contest.id }">
										<%--
										<fmt:formatNumber
											value="${(contestant.finishtime.time-contest.starttime.time)/1000/60}"
											pattern="#####" />
										分鐘  --%>
										<i class="fa fa-user-plus" aria-hidden="true"></i>
									</button>
								</div>
							</c:if>
						</td>
						<td width="20%">
							<div class="btn-group btn-group-sm" role="group" aria-label="...">
								<button type="button" class="btn btn-default"
									data-target="#Modal_confirm" title="將使用者踢出"
									data-title="將使用者踢出"
									data-content="使用者將從排行榜中除名，且無法自行再加入。確定踢出 （${contestant.user.account}）？"
									data-type="POST" data-url="Contest.api"
									data-qs="action=kick&userid=${contestant.userid}&contestid=${contest.id}">
									<i class="fa fa-user-times" aria-hidden="true"></i>
								</button>
								<button type="button" class="btn btn-default"
									data-target="#Modal_confirm" title="立即交卷" data-title="立即交卷"
									data-content="使用者將無法再繼續解題。確定讓 ${contestant.user.account} 強制交卷？"
									data-type="POST" data-url="Contest.api"
									data-qs="action=finish&userid=${contestant.userid}&contestid=${contest.id}">
									<i class="fa fa-user-o" aria-hidden="true"></i>
								</button>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
