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
	src="./EditSchools.js?${applicationScope.built }"></script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<!-- 
			<div id="monthly" class="hint">近一個月內曾經登入者的排名</div>
			<fmt:message key="Ranking.Descript" />
			-->
			<ul class="nav nav-tabs" role="tablist">
				<li><a href="./Ranking?tab=tab02#tab02" aria-controls="tab02"
					role="presentation"><fmt:message key="Ranking.AllUsers" /></a></li>
				<c:if
					test="${ sessionScope.onlineUser!=null || applicationScope.CacheUsers[sessionScope.onlineUser.account]!=null}">
					<li><a href="./Ranking?tab=tab03#tab03" aria-controls="tab03"
						role="presentation">校內排名</a></li>
				</c:if>
				<li class="active"><a href="./SchoolRanking?tab=tab04#tab04"
					aria-controls="tab04" role="presentation">校際排名</a></li>
			</ul>
			<br />


			<form class="form" role="search" method="POST"
				action="./SchoolRanking">
				<div class="form-group">
					<div class="input-group col-lg-4">
						<input type="text" class="form-control"
							placeholder="搜尋學校(空格隔開不同關鍵字)..." name="searchschool"> <span
							class="input-group-btn">
							<button class="btn btn-default" type="submit">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</button>
						</span>
					</div>
				</div>
			</form>

			<table class="table table-hover">
				<tr>
					<td width="50"><fmt:message key="Ranking.Rank" /></td>
					<td>學校</td>
					<td colspan="6"><div align="center">統計</div></td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(RankingSchools)!=0}">
						<c:forEach var="rankingSchool" items="${RankingSchools}"
							varStatus="status">
							<tr id="${rankingSchool.schoolid}">
								<td width="7%">${status.count+((pagenum-1)*fn:length(RankingSchools))}</td>
								<td title="${rankingSchool.schoolid}"><a
									href="./Ranking?tab=tab03&schoolid=${rankingSchool.schoolid}#tab03">${fn:escapeXml(rankingSchool.schoolname)}</a>
									<span style="font-size: smaller">(AVG: <fmt:formatNumber
											value="${rankingSchool.schoolavg}" pattern="#.#" />)
								</span> <a class="btn btn-default btn-xs" href="${rankingSchool.url}"
									target="_blank" role="button"><span
										class="glyphicon glyphicon-home"></span></a> <c:if
										test="${sessionScope.onlineUser.isMANAGER}">
										<c:set var="school" value="${rankingSchool}" scope="request" />
										<jsp:include page="include/Modals/Modal_EditSchool.jsp">
											<jsp:param name="action" value="UpdateSchool" />
											<jsp:param name="schoolid" value="${school.schoolid }" />
										</jsp:include>
										<button type="button" class="btn btn-default btn-xs"
											data-toggle="modal"
											data-target="#Modal_EditSchool_${school.schoolid}">
											<span class="glyphicon glyphicon-pencil"></span>
										</button>
									</c:if></td>
								<td width="25%"><div style="text-align: right;">
										<span class="acstyle">AC</span>: ${rankingSchool.schoolac}次 /
										${rankingSchool.count}人
									</div></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="10" style="text-align: center;"><fmt:message
									key="NO_DATA" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
			<ul id="tabmenu">
			</ul>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
