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
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

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

<%-- <c:set var="account" value="${param.account}" />
<c:set var="tab" value="${param.tab}" />
<c:if test="${param.tab==null || param.tab==''}">
	<c:set var="tab" value="tab02" />
</c:if>
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />
 --%>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<%-- 			<div id="monthly" class="hint">近一個月內曾經登入者的排名</div>
			<fmt:message key="Ranking.Descript" />
			<br>
 --%>
			<ul class="nav nav-tabs" role="tablist">
				<li class="active"><a href="./Ranking?tab=tab02#tab02"
					aria-controls="tab02" role="presentation"><fmt:message
							key="Ranking.AllUsers" /></a></li>
				<c:if
					test="${ sessionScope.onlineUser!=null || applicationScope.CacheUsers[sessionScope.onlineUser.account]!=null}">
					<li><a href="./Ranking?tab=tab03#tab03" aria-controls="tab03"
						role="presentation">校內排名</a></li>
				</c:if>
				<li><a href="./SchoolRanking?tab=tab04#tab04"
					aria-controls="tab04" role="presentation">校際排名</a></li>
			</ul>

			<%-- 			<ul id="tabmenu">
				<li class="tab02"><a href="./Ranking?tab=tab02"> <fmt:message
							key="Ranking.AllUsers" />
				</a></li>
				<c:if
					test="${ sessionScope.onlineUser!=null || applicationScope.CacheUsers[sessionScope.onlineUser.account]!=null}">
					<li class="tab03"><a href="./Ranking?tab=tab03">校內排名</a></li>
				</c:if>
				<li class="tab04"><a href="./SchoolRanking?tab=tab04">校際排名${lastpage
					}</a></li>
			</ul>
 --%>
			<br />
			<table class="table table-hover">
				<tr>
					<td class="col-md-1"><fmt:message key="Ranking.Rank" /></td>
					<td class="col-md-5"><fmt:message key="Ranking.User" /></td>
					<td colspan="7" class="col-md-6"><fmt:message
							key="Ranking.SolveStatus" /></td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(users)!=0}">
						<c:forEach var="user" items="${users}" varStatus="status">
							<tr>
								<%-- <c:set var="realtimerank" value="${status.count+((pagenum-1)*applicationScope.appConfig.pageSize)}" />--%>
								<c:set var="realtimerank" value="${user.currentRank}" />
								<c:if test="${param.tab=='tab02'}">
									<c:set var="realtimerank"
										value="${status.count+((pagenum-1)*applicationScope.appConfig.pageSize)}" />
								</c:if>
							<tr id="${user.account}">
								<td>${status.count+((pagenum-1)*applicationScope.appConfig.pageSize)}</td>
								<td><c:set var="user" value="${user}" scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /> <c:if
										test="${sessionScope.onlineUser.isHigherEqualThanMANAGER}">
										<span class="DEBUGGEROnly"> <c:if
												test="${user.problemManager}">
												<img src="images/ICO_problemsetter.png"
													style="height: 1.2em;" title="題目管理員" />
											</c:if> <c:if test="${user.isQualifiedAuthor}">
												<img src="images/ICO_QualifiedProblemSetter.png"
													title="題目管理員" />
											</c:if> <c:if test="${user.VClassManager}">
												<img src="images/ICO_VClassManager.png" title="課程管理員" />
											</c:if>
										</span>
									</c:if></td>
								<td><div align="left">
										AC: <a href="Submissions?account=${user.account}&status=AC">${user.ac}</a>
										<span style="">( <fmt:formatNumber
												value="${user.ac/problemnum}" type="percent" /> )
										</span>
									</div></td>
								<td><div style="">
										WA: <a href="Submissions?account=${user.account}&status=WA">${user.wa}</a>
									</div></td>
								<td><div style="">
										TLE: <a href="Submissions?account=${user.account}&status=TLE">${user.tle}</a>
									</div></td>
								<td><div style="">
										MLE: <a href="Submissions?account=${user.account}&status=MLE">${user.mle}</a>
									</div></td>
								<td><div style="">
										OLE: <a href="Submissions?account=${user.account}&status=OLE">${user.ole}</a>
									</div></td>
								<td><div style="">
										RE: <a href="Submissions?account=${user.account}&status=RE">${user.re}</a>
									</div></td>
								<td><div style="">
										CE: <a href="Submissions?account=${user.account}&status=CE">${user.ce}</a>
									</div></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="11" style="text-align: center;"><fmt:message
									key="NO_DATA" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>

			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
			<form style="display: inline;" name="form1" method="get"
				action="./UserStatistic" onsubmit="checkForm(this);">
				選擇使用者： <input id="account" name="account" type="text" size="15"
					maxlength="25" />
			</form>

			<ul id="tabmenu">
			</ul>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
