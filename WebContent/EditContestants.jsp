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
					<th scope="col">參賽帳號</th>
					<th scope="col">選手姓名</th>
					<th scope="col">學校</th>
					<th width="10%" scope="col">IP 來源</th>
					<th scope="col">狀態</th>
					<th width="20%" scope="col">操作</th>
				</tr>
				<c:forEach var="contestant" items="${contestants}"
					varStatus="varstatus">
					<tr>
						<td id="account">#${varstatus.count}: <c:set var="user"
								value="${contestant.user}" scope="request" /> <jsp:include
								page="include/div/UserAccount_TypeA.jsp" />  <%-- 							<c:if
										test="${contestant.isOnline}">
										<i class="fa fa-user" aria-hidden="true" title="在線上"></i>
									</c:if> <c:if test="${contestant.joinedcontestid == 0}">
										<i class="fa fa-minus-square-o" aria-hidden="true"></i>
									</c:if> <c:if test="${contestant.joinedcontestid != 0}">
										<a href="./ShowContest?contestid=${contestant.joinedcontestid}" class="btn btn-default btn-xs" title="參賽中">${contestant.joinedcontestid}</a></c:if>
 --%>
						</td>
						<td>${fn:escapeXml(contestant.teamname)}&nbsp;</td>
						<td>${fn:escapeXml(contestant.school)}&nbsp;</td>
						<td width="10%">${contestant.ipset}</td>
						<td><c:if
                                test="${contestant.isOnline}">(在線上)</c:if> <c:if
                                test="${!contestant.isOnline}">(不在線上)</c:if
                                >｜${contestant.status.value}#${contestant.contestid }<c:if
								test="${contestant.isRejoin }">(<fmt:formatNumber
									value="${(contestant.finishtime.time-contest.starttime.time)/1000/60}"
									pattern="#####" /> 分鐘) 
									<div class="btn-group btn-group-sm" role="group"
									aria-label="...">
									<button type="button" class="btn btn-default"
										data-target="#Modal_confirm" title="使用者 重新加入測驗"
										data-title="使用者 重新加入測驗"
										data-content="${contestant.user.account} 將可以重新登入競賽，確定嗎？"
										data-type="POST" data-url="Contest.api"
										data-qs="action=registed&userid=${contestant.userid}&contestid=${contest.id }">
										<i class="fa fa-user-plus" aria-hidden="true"></i>
									</button>
								</div>
							</c:if></td>
						<td width="20%">
							<div class="btn-group btn-group-sm" role="group" aria-label="...">
								<button type="button" class="btn btn-default"
									data-target="#Modal_confirm" title="將使用者踢出測驗"
									data-title="將使用者踢出測驗"
									data-content="使用者將從排行榜中除名，且無法再加入競賽。確定踢出 （${contestant.user.account}）？"
									data-type="POST" data-url="Contest.api"
									data-qs="action=kick&userid=${contestant.userid}&contestid=${contest.id}">
									<i class="fa fa-user-times" aria-hidden="true"></i>
								</button>
								<c:if
									test="${contest:isVisible_FinishContest(sessionScope.onlineUser, contest)}">
									<button type="button" class="btn btn-default"
										data-target="#Modal_confirm" title="立即交卷" data-title="立即交卷"
										data-content="使用者將無法再繼續解題。確定讓 ${contestant.user.account} 強制交卷？"
										data-type="POST" data-url="Contest.api"
										data-qs="action=finish&userid=${contestant.userid}&contestid=${contest.id}">
										<i class="fa fa-user-o" aria-hidden="true"></i>
									</button>
								</c:if>
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
