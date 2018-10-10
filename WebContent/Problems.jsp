<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"><jsp:include
	page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript">
	jQuery(document).ready(
			function() {

				/* 		jQuery("span[id='keyword']").click(function() {
				 var index = jQuery("span[id='keyword']").index(this);
				 var keyword = jQuery("span[id='keyword']:eq(" + index + ")").text();
				 jQuery("input[name='keyword']").val(keyword);
				 });
				 jQuery("span[id='background']").click(function() {
				 var index = jQuery("span[id='background']").index(this);
				 var background = jQuery("span[id='background']:eq(" + index + ")").text();
				 jQuery("input[name='backgrounds']").val(background);
				 });
				 */
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
<script type="text/javascript"
	src="include/div/ProblemTags.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="include/dialog/AdvSearch.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="include/Modals/Modal_ProblemSetting.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/ProblemToolbar.js?${applicationScope.built }"></script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<ul class="nav nav-tabs" role="tablist">
				<c:forEach var="tab"
					items="${applicationScope.appConfig.problemtabs}"
					varStatus="varstatus">
					<%-- 					<li class="tab0${varstatus.count-1}"><a
						href="./Problems?tabid=${tab.id}" title="${tab.descript}">${tab.name}</a></li>
 --%>
					<li><a
						href="./Problems?tabid=${tab.id}#tab0${varstatus.count-1}"
						aria-controls="tab0${varstatus.count-1}" role="presentation">${tab.name}</a></li>

				</c:forEach>
			</ul>
			<div class="row text-center">

				第
				<c:forEach var="problempage" begin="1" end="${lastpage}" step="1">
					<a href="./Problems?${querystring}&page=${problempage}">${problempage}</a>
				</c:forEach>
				頁 |
				<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
					<span class="DEBUGGEROnly"> | <span id="advsearch"
						class="FakeLink">進階搜尋</span></span>
				</c:if>
			</div>

			<%-- 			<ul id="tabmenu">
				<c:forEach var="tab"
					items="${applicationScope.appConfig.problemtabs}"
					varStatus="varstatus">
					<li class="tab0${varstatus.count-1}"><a
						href="./Problems?tabid=${tab.id}" title="${tab.descript}">${tab.name}</a></li>
				</c:forEach>
			</ul>
 --%>
			<c:choose>
				<c:when test="${fn:length(problems)!=0}">
					<table class="table table-hover">
						<tr>
							<td width="5%"><fmt:message key="Problem.SN" /></td>
							<td width="1%">&nbsp;</td>
							<td style="text-align: left"><fmt:message
									key="Problem.Problem" /></td>
							<td width="16%" style="text-align: right"><fmt:message
									key="Problem.Tags" /></td>
							<td width="12%" style="text-align: center">加入時間</td>
						</tr>
						<c:forEach var="problem" items="${problems}" varStatus="status">
							<tr>
								<td>${status.count+((pagenum-1)*applicationScope.appConfig.pageSize)}</td>
								<td><c:if
										test="${fn:contains(sessionScope.onlineUser.aclist,problem.problemid)}">
										<a
											href="./Submissions?problemid=${problem.problemid}&account=${sessionScope.onlineUser.account}&status=AC"><img
											src="images/accept.svg" style="height: 1em;" title="已成功解出" /></a>
									</c:if></td>
								<td><c:set var="problem" value="${problem}" scope="request" />
									<jsp:include page="include/Modals/Modal_ProblemSetting.jsp" />
									<jsp:include page="include/div/ProblemTitle.jsp" /> <span
									style="float: right; font-size: xx-small;"> <fmt:formatNumber
											value="${problem.acusers/problem.submitusers}" type="percent" />
										/ ${problem.acusers} <fmt:message key="Problem.Users" /><jsp:include
											page="include/ProblemToolbar_TypeB.jsp" />
								</span></td>
								<td>
									<div align="right" style="font-size: 11px">
										<c:set var="problem" value="${problem}" scope="request" />
										<jsp:include page="include/div/ProblemTags.jsp" />
										<c:if
											test="${sessionScope.onlineUser.isHigherEqualThanMANAGER}">
											<span title="sortable">${fn:escapeXml(problem.sortable)}</span>
										</c:if>
										<%-- 									<a
										href="./Submissions?problemid=${problem.problemid}&status=AC"
										title="不重複的通過人數">${problem.acusers}人</a>/<a
										href="./Submissions?problemid=${problem.problemid}"
										title="不重複的嘗試解題人數">${problem.submitusers}人 </a>
 --%>
									</div>
								</td>
								<td><div style="text-align: right;">
										<fmt:formatDate value="${problem.inserttime}"
											pattern="yyyy-MM-dd" />
									</div></td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="include/Pagging.jsp">
						<jsp:param name="querystring" value="${querystring}" />
					</jsp:include>
				</c:when>
				<c:otherwise>
					<div style="text-align: center; font-size: x-large; padding: 10px;">
						<fmt:message key="NO_DATA" />
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
