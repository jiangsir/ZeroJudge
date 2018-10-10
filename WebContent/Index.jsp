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

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("form[id='searchAuthor']").click(function() {
			$(this).submit();
		});
	});
</script>
<script type="text/javascript"
	src="include/JoinContest.js?${applicationScope.built }"></script>

</head>
<body>
	<%-- 	<jsp:include page="include/Header_Fixed_Top.jsp" />
 --%>
	<jsp:include page="include/Header_bsdocs.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-md-9">
				<div class="row">
					<div class="col-md-8">
						<h3>
							<fmt:message key="Index.MainBox_Title" />
						</h3>
						<fmt:message key="Index.MainBox_Line2" />
						<br />
						<fmt:message key="Index.MainBox_Line3" />
						<br />
						<fmt:message key="Index.MainBox_Line4" />
						<br />
						<fmt:message key="Index.MainBox_Line5" />
						<br />
						<fmt:message key="Index.MainBox_Line6" />
						<br />
						<fmt:message key="Index.MainBox_Line7" />
						<br />
						<fmt:message key="Index.MainBox_Line8" />
						<br />
						<fmt:message key="Index.MainBox_Line9" />
						<br />
						<fmt:message key="Index.MainBox_Line10" />
						<a href="./UserGuide.jsp#compiler"><fmt:message
								key="Index.AboutCompiler" /></a><br />
						<fmt:message key="Index.MainBox_Line11" />
						<br /> 
						<jsp:include page="include/div/ShowServerConfig_TypeA.jsp" />
						<br /> <a href="UserGuide.jsp"><fmt:message
								key="Index.UserGuide" /> </a> | <a href="./Application">我要加題目</a>
						<c:if test="${fn:length(applicationScope.appConfig.httpscrt)!=0 }">
                                ｜ <a
								href="./Download.api?target=HttpscrtFile">下載憑證檔</a>
						</c:if>


					</div>
					<div class="col-md-4">
						<div class="row">
							<div class="col-md-12">
								<c:choose>
									<c:when
										test="${fn:length(startingContests)>0 || fn:length(runningContests)>0}">
										<div>
											<c:forEach var="contest" items="${runningContests}">
												<strong>${fn:escapeXml(contest.title)}</strong>
												<pre>說明：${fn:escapeXml(contest.subtitle)}</pre>
												<c:set var="contest" value="${contest}" scope="request" />
												<jsp:include page="include/JoinContest.jsp" />
												<hr />
											</c:forEach>
											<c:forEach var="contest" items="${startingContests}">
												<strong>${fn:escapeXml(contest.title)}</strong>
												<br />
  說明：
 <div class="subtitle" style="margin: 15px;">${fn:escapeXml(contest.subtitle)}</div>

												<a href="./ShowContest?contestid=${contest.id}">進入...</a>
												<hr />
											</c:forEach>
											<c:if test="${fn:trim(VBANNER)!=''}">
												<div class="VerticalBanner">${VBANNER}</div>
											</c:if>
										</div>
									</c:when>
									<c:otherwise>
										<%-- <jsp:include page="${VBANNER}.jsp" /> --%>
										<c:if test="${fn:trim(VBANNER)!=''}">
											<div class="VerticalBanner">${VBANNER}</div>
										</c:if>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<c:if test="${fn:length(Announcements) !=0}">
						<h4>
							<fmt:message key="Index.Announcement" />
						</h4>
						<table>
							<tbody>
								<c:forEach var="announcement" items="${Announcements}">
									<tr>
										<td><i class="fa fa-circle-o" aria-hidden="true"></i> <a
											href="./ShowThread?postid=${announcement.id}">${announcement.subject}</a>
											<span style="font-size: small; color: #AAAAAA"> (<fmt:formatDate
													value="${announcement.timestamp}"
													pattern="yyyy-MM-dd HH:mm" />)
										</span></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
					<div id="LatestProblem">
						<c:if test="${fn:length(newproblems)==0}">
							<h4>
								<fmt:message key="NO_DATA" />
							</h4>
						</c:if>
						<c:if test="${fn:length(newproblems)!=0}">
							<h4>
								<fmt:message key="Index.LatestProblem" />
							</h4>
							<table class="table table-condensed">
								<tbody>
									<tr>
										<td width="50%"><c:forEach var="problem"
												items="${newproblems}"
												end="${(fn:length(newproblems)+fn:length(newproblems)%2)/2-1}"
												varStatus="varstatus">
												<c:set var="problem" value="${problem}" scope="request" />
												<jsp:include page="include/div/ProblemTitle.jsp" /><br />
											</c:forEach></td>
										<td width="50%"><c:forEach var="problem"
												items="${newproblems}"
												begin="${(fn:length(newproblems)+fn:length(newproblems)%2)/2}"
												varStatus="varstatus">
												<c:set var="problem" value="${problem}" scope="request" />
												<jsp:include page="include/div/ProblemTitle.jsp" /><br />
											</c:forEach></td>
									</tr>
								</tbody>
							</table>
						</c:if>
					</div>
				</div>
			</div>
			<div class="col-md-3">
				<div class="row">
					<div class="col-md-12">
						<table class="table table-hover table-condensed">
							<thead>
								<tr>
									<th><fmt:message key="Index.TopCoder" /></th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(top10)!=0}">
										<c:forEach var="top10" items="${top10}" varStatus="varstatus">
											<tr>
												<td><c:set var="user" value="${top10}" scope="request" />
													<jsp:include page="include/div/UserAccount_TypeA.jsp" /> <%-- 												<a href="./UserStatistic?id=${top10.id}"
													title="${top10.username}">${top10.account}</a>
 --%></td>
												<td title="通過題數 / 點數" style="text-align: right">${top10.ac}/<fmt:formatNumber
														value="${top10.ac/fn:length(applicationScope.openedProblemidSet)}"
														type="percent" />
												</td>
											</tr>
										</c:forEach>
										<tr>
											<td></td>
											<td style="text-align: right;"><a href="./Ranking">more...</a></td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr>
											<td><fmt:message key="NO_DATA" /></td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="table table-hover table-condensed">
							<thead>
								<tr>
									<th>Top Author</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(topauthors)!=0}">
										<c:forEach var="topauthor" items="${topauthors}"
											varStatus="varstatus">
											<tr>
												<td><c:set var="user" value="${topauthor.key}"
														scope="request" /> <jsp:include
														page="include/div/UserAccount_TypeA.jsp" /> <%-- <a
													href="./UserStatistic?account=${topauthor.key}"
													title="${topauthor.key}">${topauthor.key}</a> --%></td>
												<td title="">
													<div align="right">
														<form id="searchAuthor"
															action="Problems?ownerid=${topauthor.key.id}"
															method="post" class="btn btn-link btn-xs">${topauthor.value}</form>
													</div>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td><fmt:message key="NO_DATA" /></td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="table table-hover table-condensed">
							<thead>
								<tr>
									<th>Top School</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(topschools)!=0}">
										<c:forEach var="school" items="${topschools}"
											varStatus="varstatus">
											<tr>
												<td>${varstatus.count}:
													${fn:escapeXml(school.schoolname)}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td><fmt:message key="NO_DATA" /></td>
										</tr>
									</c:otherwise>
								</c:choose>
								<tr>
									<td style="text-align: right;"><a
										href="./SchoolRanking?tab=tab04">more...</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-center">
				相關網站<br /> <a href="http://www.csie.ntnu.edu.tw/~u91029/"
					target="_blank">演算法筆記</a> | <a
					href="http://www.tcgs.tc.edu.tw/~sagit/cpp/" target="_blank">
					Sagit's C++ 程式設計</a> | <a href="http://www3.tcgs.tc.edu.tw/npsc/"
					target="_blank">NPSC補完計劃</a> | <a
					href="http://luckycat.kshs.kh.edu.tw/" target="_blank">Lucky 貓的
					ACM 園地</a>
			</div>
		</div>
	</div>

	<jsp:include page="include/Footer.jsp" />
</body>
</html>
