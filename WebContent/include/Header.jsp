<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<jsp:useBean id="envConfig" class="tw.zerojudge.Configs.EnvConfig" />

<div id="banner">
	<div class="FirstPageTitle">
		<img src="${applicationScope.appConfig.titleImageBase64}"
			title="${applicationScope.appConfig.title}"
			alt="${applicationScope.appConfig.title}"
			style="margin: 50px 0 10px 0;"><br />
		${applicationScope.appConfig.header}<br />
	</div>
</div>

<!-- jquery UI MENU -->
<ul id="nav" style="display: none;">
	<!-- 第二項 -->
	<li><a href="./"> <fmt:message key="HOME" /></a></li>

	<li><a href="#"> <fmt:message key="LANGUAGE" />(${sessionScope.session_locale})
	</a>
		<ul>
			<li><a href="./ChangeLocale.api?locale=zh_TW"> <fmt:message
						key="zh_TW" />
			</a></li>
			<li><a href="./ChangeLocale.api?locale=zh_CN"> <fmt:message
						key="zh_CN" />
			</a></li>
			<li><a href="./ChangeLocale.api?locale=en_US"> <fmt:message
						key="en_US" />
			</a></li>
		</ul></li>
	<c:choose>
		<c:when
			test="${sessionScope.onlineUser != null && !sessionScope.onlineUser.nullUser}">
			<!-- 登出 項 -->
			<li><a href="./Logout"> <fmt:message key="LOGOUT" /></a></li>

			<li><c:if test="${fn:length(sessionScope.unreadIMessages)!=0}">
					<a href="./ShowIMessages"><img
						src="./images/unread_twinkle.gif" /> <span id="session_account">${sessionScope.onlineUser.account}</span></a>
				</c:if> <c:if test="${fn:length(sessionScope.unreadIMessages)==0}">
					<a href="#">${sessionScope.onlineUser.account}</a>
				</c:if> <%--                        
                檢測使用者是否進入了一個競賽。
                <c:if test="${applicationScope.CacheUsers[sessionScope.onlineUser.account].joinedcontestid<=0}">
                    </c:if>
 --%>
				<ul id="menu">
					<li><a href="#"><IMG SRC="./images/flags/Unknown.gif"></a></li>
					<li><a href="./ShowIMessages?action=inboxIM"> <fmt:message
								key="Header.IMessageInbox" />
					</a></li>
					<li><a href="./UserStatistic"> <fmt:message
								key="Header.UserStatistic" />
					</a></li>
					<li><a href="./UpdateUser"> <fmt:message
								key="Header.ModifyInfo" />
					</a></li>
					<c:if test="${sessionScope.onlineUser.contestManager}">
						<li><a href="./EditContests">競賽管理</a></li>
					</c:if>
					<c:if test="${sessionScope.onlineUser.problemManager}">
						<li><a href="./EditProblems">題目管理</a></li>
					</c:if>
					<c:if test="${sessionScope.onlineUser.VClassManager}">
						<li><a href="./EditVClasses">課程管理</a></li>
					</c:if>
					<c:if
						test="${fn:length(sessionScope.onlineUser.belongedVClasses) > 0}">
						<li><a href="#">進入課程</a>
							<ul>
								<c:forEach var="vclass"
									items="${sessionScope.onlineUser.belongedVClasses }">
									<li><a href="./ShowVClass?vclassid=${vclass.id}">${vclass.vclassname
											}</a></li>
								</c:forEach>
							</ul></li>
					</c:if>
					<li type='separator'></li>
					<c:if test="${sessionScope.onlineUser.isHigherEqualThanMANAGER}">
						<li><a href="#">站務管理</a>
							<ul>

								<li><a href="./EditUsers">管理使用者</a></li>
								<li><a href="./EditProblems">管理題目</a></li>
								<li><a href="./EditContests">管理競賽</a></li>
								<li><a href="./EditSchools">管理學校</a></li>
								<li><a href="./EditAppConfig">管理系統參數</a></li>
							</ul></li>
					</c:if>
					<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
						<li><a href="#">Debug</a>
							<ul>
								<li><a href="./Debug">Debug資訊看版</a></li>
								<li><a href="./ShowOnlineUsers">管理線上使用者</a></li>
								<li><a href="./EditVClasses">管理課程</a></li>
								<li><a href="./ShowLogs">系統記錄</a></li>
							</ul></li>
					</c:if>

					<li type='separator'></li>
					<!-- 參賽 項 -->
					<c:if test="${sessionScope.onlineUser.joinedcontestid > 0}">
						<li><a
							href="./ShowContest?contestid=${sessionScope.onlineUser.joinedcontestid}">
								返回已加入的競賽『${sessionScope.onlineUser.joinedContest.title}』 </a></li>
					</c:if>
					<li><a href="./Logout"> <fmt:message key="LOGOUT" /></a></li>
				</ul></li>
		</c:when>
		<c:otherwise>
			<li><a href="./InsertUser"> <fmt:message key="REGISTER" />
			</a></li>
			<li><a href="./Login"> <fmt:message key="LOGIN" />
			</a></li>
		</c:otherwise>
	</c:choose>
	<c:if test="${sessionScope.onlineUser.isDEBUGGER }">
		<li><fmt:formatNumber value="${(envConfig.usedMemory)/1024/1024}"
				pattern="###,###" />MB/<fmt:formatNumber
				value="${(envConfig.totalMemory)/1024/1024}" pattern="###,###" />MB
			| ${applicationScope.appRoot.name} |</li>
		<li>Online User 共 ${fn:length(applicationScope.onlineUsers)}人
			<ul>
				<c:forEach var="onlineUser" items="${applicationScope.onlineUsers}">
					<li>${onlineUser}</li>
				</c:forEach>
			</ul>
		</li>
		<%--                <li>| 線上 sessions 共 ${fn:length(applicationScope.HashSessions)}
                    (returnPage=${sessionScope.returnPage})
                    <ul>
                        <c:forEach var="hashSession"
                            items="${applicationScope.HashSessions}">
                            <li><c:if test="${sessionScope.sessionid==hashSession.key}">*</c:if><a
                                href="ShowSessions?sessionid=${hashSession.key}">${hashSession.key}</a></li>
                        </c:forEach>
                    </ul>
                </li>
 --%>
	</c:if>
	<!-- 第一項 -->

</ul>

<%-- <!-- SuperFish MENU For FireFox BEGIN **************************************************** -->
<c:if test="${!fn:contains(header['User-Agent'], 'MSIE 6')}">
	<div>
		<ul class="sf-menu">
		</ul>
	</div>
	<!-- SuperFish MENU For FireFox BEGIN **************************************************** -->
</c:if>
 --%>
<!-- MAIN MENU -->
<div class="mainmenu">
	<span style="float: left; width: 20%; text-align: left"
		class="header_info">| ${sessionScope.session_ipset } | </span> <span
		style="float: left; width: 60%; margin: auto; text-align: center; display: inline">
		<c:if
			test="${sessionScope.onlineUser.isDEBUGGER || applicationScope.appConfig.systemModeContestid==0}">
			<c:if test="${sessionScope.CurrentPage!=null}">
				<a href="${sessionScope.CurrentPage}"> <fmt:message
						key="PreviousPage" />
				</a> | </c:if>
			<a href="./Problems"> <fmt:message key="Header.Problems" />
			</a> | <a href="./Submissions"> <fmt:message key="Header.Submissions" />
			</a> | <a href="./Ranking"> <fmt:message key="Header.Ranking" />
			</a> | <a href="./Forum"> <fmt:message key="Header.Forum" />
			</a> | <a href="./Contests"> <fmt:message key="Header.Contest" />
			</a>
		</c:if>
	</span> <span style="float: right; width: 20%; text-align: right;"
		class="header_info"><fmt:message key="ONLINE_USERS" />: <span
		id="onlineusercount">${fn:length(applicationScope.onlineUsers)}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<fmt:message
			key="PROBLEM_NUM" />:
		${fn:length(applicationScope.openedProblemidSet)}&nbsp;&nbsp;</span>
</div>
<br />
