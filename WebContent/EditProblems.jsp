<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>

<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="EditProblems.js?${applicationScope.built }"></script>
<!-- <script type="text/javascript" src="showDetails.js"></script>
 -->
<script type="text/javascript"
	src="include/dialog/AdvSearch.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
<%-- <script type="text/javascript"
	src="include/dialog/ShowDetail.js?${applicationScope.built }"></script>
 --%>
<%-- <script type="text/javascript"
	src="include/dialog/ShowCode.js?${applicationScope.built }"></script>
 --%>
<script type="text/javascript"
	src="include/Modals/Modal_ProblemSetting.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="include/ProblemToolbar.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/DivProblemStatusInfo.js?${applicationScope.built }"></script>

</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<%-- 	<jsp:include page="include/dialog/ShowDetail.jsp" />
 --%>
	<jsp:include page="include/dialog/AdvSearch.jsp" />

	<div class="container">
		<div class="row">
			<a class="btn btn-primary" href="./InsertProblem">新增題目</a> <a
				class="btn btn-primary" href="./ImportProblems">匯入題目</a>
			<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
				<a class="btn btn-primary" href="./ExportProblems">匯出題目</a>
				<a class="btn btn-primary" href="#">同步所有測資到裁判機上</a>
				<a class="btn btn-primary" href="#">重新計算題目答對人數</a>
			</c:if>

			<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
				<span class="DEBUGGEROnly"> <span id="advsearch"
					class="btn btn-primary">進階搜尋</span> <jsp:include
						page="include/dialog/ExportProblems.jsp" /> <a href="#"
					id="ExportProblems" type="button">匯出題目</a> <jsp:include
						page="include/dialog/Confirm.jsp">
						<jsp:param name="title"
							value="這個動作會將目前所有題目的測資同步到裁判機上，並且清除所有在裁判機上原有的測資，請問是否確定？" />
						<jsp:param name="type" value="GET" />
						<jsp:param name="url" value="Problem.api" />
						<jsp:param name="data" value="action=rsyncAllTestdatas" />
					</jsp:include>
					<button id="rsyncAllTestdatas" class="confirm">同步所有測資到裁判機上</button>
					<jsp:include page="include/dialog/Confirm.jsp">
						<jsp:param name="title" value="將會重新計算所有題目的答對人數、送出人數，可能花費較長時間，確定嗎？" />
						<jsp:param name="type" value="GET" />
						<jsp:param name="url" value="Problem.api" />
						<jsp:param name="data" value="action=recountAllAcceptedProblem" />
					</jsp:include>
					<button id="recountAllAcceptedProblem" class="confirm">重新計算題目答對人數</button>
				</span>
			</c:if>
			<%-- 		<jsp:include page="include/div/ProblemSearch.jsp" />
 --%>
			<!-- 			<form name="form2" method="post" action=""
				style="margin: 0px; display: inline;" onsubmit="checkForm(this);">
				全文檢索： <input name="searchword" type="text" value="" size="10" />
			</form>
 -->

			<ul class="nav nav-tabs" role="tablist">
				<li class="active"><a href="?tabid=MYPROBLEM#MYPROBLEM"
					aria-controls="MYPROBLEM" role="presentation">我的所有題目</a></li>
				<c:forEach var="tab"
					items="${applicationScope.appConfig.problemtabs}"
					varStatus="varstatus">
					<%-- 						<li class="tab0${varstatus.count-1}"><a
							href="?tabid=${tab.id}">${tab.name}</a></li>
 --%>
					<%-- 						<li role="presentation"><a href="?tabid=${tab.id}#${tab.id}"
							aria-controls="${tab.id}" role="tab" data-toggle="tab">${tab.name}</a></li>
 --%>
					<li><a href="?tabid=${tab.id}#${tab.id}"
						aria-controls="${tab.id}" role="presentation">${tab.name}</a></li>

				</c:forEach>


				<li><a href="?tabid=VERIFYING#VERIFYING"
					aria-controls="VERIFYING" role="presentation">已提交</a></li>
				<li><a href="?tabid=NOTOPEN#NOTOPEN" aria-controls="NOTOPEN"
					role="presentation"><span class="glyphicon glyphicon-eye-close"></span>
						非開放題</a></li>
				<li><a href="?tabid=SPECIAL#SPECIAL" aria-controls="SPECIAL"
					role="presentation"> Special</a></li>
			</ul>

			<%-- 				<ul id="tabmenu">
					<li class="tab10"><a href="#tabs-1"> <a
							href="?tabid=MYPROBLEM">我的所有題目</a></a></li>
					<c:forEach var="tab"
						items="${applicationScope.appConfig.problemtabs}"
						varStatus="varstatus">
						<li class="tab0${varstatus.count-1}"><a
							href="?tabid=${tab.id}">${tab.name}</a></li>
					</c:forEach>
					<c:if
						test="${sessionScope.onlineUser.isDEBUGGER }">
						<li class="tab13"><a href="?tabid=VERIFYING">已提交</a></li>
					</c:if>
					<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
						<li class="tab11"><a href="?tabid=NOTOPEN">非開放題</a></li>
					</c:if>
				</ul>
 --%>
			<h2>管理題目</h2>
			<table class="table table-hover">
				<tr>
					<th><input type="checkbox" title="全選" id="selectAll"></input></th>
					<th class="col-md-1">題號</th>
					<th class="col-md-6">題目</th>
					<th class="col-md-2">tags</th>
					<th class="col-md-3">操作</th>
				</tr>
				<c:choose>
					<c:when test="${fn:length(problems)!=0}">
						<c:forEach var="problem" items="${problems}" varStatus="varstatus">
							<tr problemid="${problem.problemid}">
								<td style="text-align: right;">
									<%-- 								<input type="checkbox"
									name="problems" value="${problem.problemid}"></input>	
 --%>
								</td>
								<td id="problemid">${problem.problemid}</td>
								<td><c:set var="problem" value="${problem}" scope="request" />
									<jsp:include page="include/div/ProblemDisplay.jsp" /> <c:set
										var="problem" value="${problem}" scope="request" /> <jsp:include
										page="include/div/ProblemTitle.jsp" /> (管理: <c:set var="user"
										value="${problem.owner}" scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /> ) <span
									title="資料庫圖片" style="color: green;">${problemBean.imageInfos[0]}</span>
									<span title="站內圖片" style="color: yellow;">${problemBean.imageInfos[1]}</span>
									<span title="站外圖片" style="color: red;">${problemBean.imageInfos[2]}</span>
								</td>
								<td>${fn:escapeXml(problem.tags)}</td>
								<td><c:set var="problem" value="${problem}" scope="request" />
									<jsp:include page="include/ProblemToolbar_Bootstrap.jsp" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="14"><div align="center">
									<hr />
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
