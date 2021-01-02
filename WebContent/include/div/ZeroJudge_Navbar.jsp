<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<style>
<!-- /* navbar */
.navbar-default {
	/* background-color: #F8F8F8; */
	/*background-color: rgba(51, 122, 183, 1);*/
	/*background-color: #A8BDDB;*/ /* 原 ZeroJudge 色*/
	background-color: #8ca3c3;
	border-color: #E7E7E7;
	font-size: 18px;
}
/* title */
.navbar-default .navbar-brand {
	color: #777;
}

.navbar-default .navbar-brand:hover, .navbar-default .navbar-brand:focus
	{
	color: #5E5E5E;
}
/* link */
.navbar-default .navbar-nav>li>a {
	/* color: #777; */
	color: #FFFFFF; /*qx*/
}

.navbar-default .navbar-nav>li>a:hover, .navbar-default .navbar-nav>li>a:focus
	{
	color: #333;
}

.navbar-default .navbar-nav>.active>a, .navbar-default .navbar-nav>.active>a:hover,
	.navbar-default .navbar-nav>.active>a:focus {
	/* color: #555; */
	color: #FFFFFF;
	background-color: #E7E7E7;
}

.navbar-default .navbar-nav>.open>a, .navbar-default .navbar-nav>.open>a:hover,
	.navbar-default .navbar-nav>.open>a:focus {
	/* color: #555; */
	color: #FFFFFF;
	background-color: #D5D5D5;
}
/* caret */
.navbar-default .navbar-nav>.dropdown>a .caret {
	border-top-color: #777;
	border-bottom-color: #777;
}

.navbar-default .navbar-nav>.dropdown>a:hover .caret, .navbar-default .navbar-nav>.dropdown>a:focus .caret
	{
	border-top-color: #333;
	border-bottom-color: #333;
}

.navbar-default .navbar-nav>.open>a .caret, .navbar-default .navbar-nav>.open>a:hover .caret,
	.navbar-default .navbar-nav>.open>a:focus .caret {
	border-top-color: #555;
	border-bottom-color: #555;
}
/* mobile version */
.navbar-default .navbar-toggle {
	border-color: #DDD;
}

.navbar-default .navbar-toggle:hover, .navbar-default .navbar-toggle:focus
	{
	background-color: #DDD;
}

.navbar-default .navbar-toggle .icon-bar {
	background-color: #CCC;
}

@media ( max-width : 767px) {
	.navbar-default .navbar-nav .open .dropdown-menu>li>a {
		color: #777;
	}
	.navbar-default .navbar-nav .open .dropdown-menu>li>a:hover,
		.navbar-default .navbar-nav .open .dropdown-menu>li>a:focus {
		color: #333;
	}
}

.vcenter {
	display: inline-block;
	vertical-align: middle;
	/*float: none;*/
}

.navbar-nav-center {
	width: 100%;
	text-align: center; > li { float : none;
	display: inline-block;
}

}
/* .navbar-brand {
	padding-top: 8px;
	height: 40px;
}
 */
-->
.truncate {
	width: 100px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
</style>
<div class="container-fluid">
	<div class="navbar-header">
		<a class="navbar-brand" href="${pageContext.request.contextPath}/">
			<img src="${applicationScope.appConfig.logoBase64 }" style="" />
		</a>
	</div>
	<!-- Brand and toggle get grouped for better mobile display -->

	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		<c:choose>
			<c:when test="${applicationScope.appConfig.isCLASS_MODE()}">
				<ul class="nav navbar-nav">
					<li><a href="${pageContext.request.contextPath}/ShowVClasses"><img
							src="${pageContext.request.contextPath}/images/CLASS_MODE.svg"
							title="CLASS_MODE" alt="CLASS_MODE"></a></li>
				</ul>
			</c:when>
			<c:when test="${applicationScope.appConfig.isPROBLEMS_MODE()}">
				<ul class="nav navbar-nav">
					<li><a href="#" style="font-size: 1.5em;">題  庫  模  式</a></li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul class="nav navbar-nav">
					<li><a href="${pageContext.request.contextPath}/Problems">
							<fmt:message key="Header.Problems" />
					</a></li>
					<li><a href="${pageContext.request.contextPath}/Submissions">
							<fmt:message key="Header.Submissions" />
					</a></li>
					<li><a href="${pageContext.request.contextPath}/Ranking">
							<fmt:message key="Header.Ranking" />
					</a></li>
					<li><a href="${pageContext.request.contextPath}/Forum"> <fmt:message
								key="Header.Forum" />
					</a></li>
					<li><a href="${pageContext.request.contextPath}/Contests">
							<fmt:message key="Header.Contest" />
					</a></li>
				</ul>
				<form name="form1" class="navbar-form navbar-center" role="search"
					method="POST" action="${pageContext.request.contextPath}/Problems">
					<div class="input-group">
						<input type="text" class="form-control"
							placeholder="搜尋題目關鍵字、題號..." name="searchword"> <span
							class="input-group-btn">
							<button class="btn btn-default" type="submit">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</button>
						</span>
					</div>
				</form>
			</c:otherwise>
		</c:choose>


		<ul class="nav navbar-nav navbar-right">
			<%-- <li class="dropdown"><a href="#" class="dropdown-toggle"
                    data-toggle="dropdown" role="button" aria-expanded="false"> <fmt:message
                            key="LANGUAGE" />(${sessionScope.session_locale}) <span
                        class="caret"></span>
                </a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="./ChangeLocale.api?locale=zh_TW"> <fmt:message
                                    key="zh_TW" />
                        </a></li>
                        <li><a href="./ChangeLocale.api?locale=zh_CN"> <fmt:message
                                    key="zh_CN" />
                        </a></li>
                        <li><a href="./ChangeLocale.api?locale=en_US"> <fmt:message
                                    key="en_US" />
                        </a></li>
                    </ul></li> --%>
			<%-- 			<li><a href="./"><span class="glyphicon glyphicon-home"
					aria-hidden="true"></span> <fmt:message key="HOME" /></a></li>
 --%>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<%-- <li><a><fmt:message key="ONLINE_USERS" />:
						${fn:length(applicationScope.onlineUsers)} | <fmt:message
							key="PROBLEM_NUM" />:
						${fn:length(applicationScope.openedProblemidSet)}</a></li> --%>
			<c:choose>
				<c:when
					test="${sessionScope.onlineUser != null && !sessionScope.onlineUser.nullUser}">
					<%--                        <c:if test="${user.isAdmin(sessionScope.currentUser)}">
                            <li><a href="./EditAppConfig"><span
                                    class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
                                    管理頁</a></li>
                        </c:if>
 --%>
					<li class="dropdown"><a href="#" class="dropdown-toggle "
						data-toggle="dropdown" role="button" aria-expanded="false"> <c:if
								test="${sessionScope.onlineUser.isAuthhost_Google }">
								<img
									src="${pageContext.request.contextPath}/ShowImage?userid=${sessionScope.onlineUser.id}"
									class="img-rounded" style="width: 1.5em;">
							</c:if> ${fn:escapeXml(sessionScope.onlineUser.username)} <span
							class="badge">${fn:length(sessionScope.unreadIMessages) }
								<%-- (${fn:substring(fn:escapeXml(user.username),0,15)}) --%>
						</span><span class="caret"></span>
					</a>
						<ul class="dropdown-menu" role="menu">
							<c:if test="${applicationScope.appConfig.isGoogleLoginSetup }">
								<c:if test="${!sessionScope.onlineUser.isAuthhost_Google }">
									<li><a
										href="${pageContext.request.contextPath}/GoogleLogin"><i
											class="fa fa-link" aria-hidden="true"></i> 綁定 Google 帳號</a></li>
								</c:if>
								<c:if test="${sessionScope.onlineUser.isAuthhost_Google}">
									<li><img
										src="${pageContext.request.contextPath}/ShowImage?userid=${sessionScope.onlineUser.id}"
										class="img-responsive" style="height: 200px;"></li>
									<li><a href="#" type="button" data-toggle="modal"
										data-target="#Modal_SetUserPassword"><i
											class="fa fa-chain-broken" aria-hidden="true"></i> 解除綁定
											Google 帳號</a></li>
								</c:if>
							</c:if>
							<li><a href="${pageContext.request.contextPath}/Logout"><span
									class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
									<fmt:message key="LOGOUT" /></a></li>
							<li role="separator" class="divider"></li>
							<c:if
								test="${sessionScope.onlineUser.contestManager && !applicationScope.appConfig.getIsCLASS_MODE()}">
								<li><a
									href="${pageContext.request.contextPath}/EditContests"><i
										class="fa fa-certificate"></i> 競賽管理</a></li>
							</c:if>
							<c:if test="${sessionScope.onlineUser.problemManager}">
								<li><a
									href="${pageContext.request.contextPath}/EditProblems"><i
										class="fa fa-certificate"></i> 題目管理</a></li>
							</c:if>
							<c:if test="${sessionScope.onlineUser.VClassManager}">
								<li><a
									href="${pageContext.request.contextPath}/ShowVClasses"><i
										class="fa fa-certificate"></i> 課程管理</a></li>
							</c:if>
							<c:if test="${sessionScope.onlineUser.isHigherEqualThanMANAGER}">

								<li><a href="${pageContext.request.contextPath}/EditUsers"><i
										class="fa fa-certificate"></i> 管理使用者</a></li>
								<li><a
									href="${pageContext.request.contextPath}/EditSchools"><i
										class="fa fa-certificate"></i> 管理學校</a></li>
								<li><a
									href="${pageContext.request.contextPath}/EditAppConfig"><i
										class="fa fa-certificate"></i> 管理系統參數</a></li>
							</c:if>
							<li role="separator" class="divider"></li>
							<!-- <li><a href="#"><IMG SRC="${pageContext.request.contextPath}/images/flags/Unknown.gif"></a></li> -->
							<c:if test="${!applicationScope.appConfig.getIsCLASS_MODE() }">
								<li><a
									href="${pageContext.request.contextPath}/ShowIMessages?action=inboxIM">
										<fmt:message key="Header.IMessageInbox" />
								</a></li>
								<li><a
									href="${pageContext.request.contextPath}/UserStatistic"> <fmt:message
											key="Header.UserStatistic" />
								</a></li>
							</c:if>
							<li><a href="${pageContext.request.contextPath}/UpdateUser">
									<fmt:message key="Header.ModifyInfo" />
							</a></li>
							<li><a href="#" type="button" data-toggle="modal"
								data-target="#Modal_JoinVclassid"><span class="fa fa-plus"
									aria-hidden="true"></span> 參加課程</a></li>
							<c:if
								test="${fn:length(sessionScope.onlineUser.belongedVClasses) > 0}">
								<li>
									<%-- <li class="dropdown-submenu">  --%> <a href="#">已參加的課程</a>
									<ul>
										<%-- <ul class="dropdown-menu"> --%>
										<c:forEach var="vclass"
											items="${sessionScope.onlineUser.belongedVClasses }">
											<li><a
												href="${pageContext.request.contextPath}/ShowVClass?vclassid=${vclass.id}">${vclass.vclassname
                                            }</a></li>
										</c:forEach>
									</ul>
								</li>
							</c:if>

							<%-- 							<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
								<li class="dropdown-submenu"><a href="#">Debug</a>
									<ul class="dropdown-menu">
										<li><a href="./Debug">Debug資訊看版</a></li>
										<li><a href="./ShowOnlineUsers">管理線上使用者</a></li>
										<li><a href="./EditVClasses">管理課程</a></li>
										<li><a href="./ShowLogs">系統記錄</a></li>
									</ul></li>
							</c:if>
 --%>
							<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
								<li><a href="#">Debug</a>
									<ul>
										<li><a href="${pageContext.request.contextPath}/Debug">Debug資訊看版</a></li>
										<li><a
											href="${pageContext.request.contextPath}/ShowOnlineUsers">管理線上使用者</a></li>
										<li><a href="${pageContext.request.contextPath}/ShowLogs">系統記錄</a></li>
									</ul></li>
							</c:if>
							<c:if test="${sessionScope.onlineUser.joinedcontestid > 0}">
								<li><a
									href="${pageContext.request.contextPath}/ShowContest?contestid=${sessionScope.onlineUser.joinedcontestid}">
										返回已加入的競賽<br />『${sessionScope.onlineUser.joinedContest.title}』
								</a></li>
							</c:if>
						</ul></li>
				</c:when>
				<c:otherwise>
					<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
						<li><a href="${pageContext.request.contextPath}/Login"> <fmt:message
									key="LOGIN" />
						</a></li>
						<li><a href="${pageContext.request.contextPath}/InsertUser">
								<fmt:message key="REGISTER" />
						</a></li>
					</c:if>
				</c:otherwise>
			</c:choose>
			<li><a href="${pageContext.request.contextPath}/"><span
					class="glyphicon glyphicon-home" aria-hidden="true"></span></a></li>
			<li>
		</ul>
	</div>
	<!-- /.navbar-collapse -->
</div>
<!-- /.container-fluid -->
