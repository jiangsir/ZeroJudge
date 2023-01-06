<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<script type="text/javascript" src="include/ContestToolbar.js?${applicationScope.built}"></script>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="container-fluid">
	<div class="well well-lg">
		<div class="row">
			<div class="col-md-8">
				<c:if test="${contest.isVContest()}">
					<a href="ShowVClass?vclassid=${contest.vclassid}">${fn:escapeXml(contest.getVClass().vclassname)}</a>
				</c:if>
				<h1>${fn:escapeXml(contest.title)}</h1>
				
				<c:if test="${fn:escapeXml(fn:trim(contest.subtitle))!=''}">
					<pre>${fn:escapeXml(fn:trim(contest.subtitle))}</pre>
				</c:if>
				<hr />
				<jsp:include page="JoinContestButton.jsp" />
			<!-- <h2 class="text-center">
					<c:if test="${contest.isVContest()}">
						<a href="ShowVClass?vclassid=${contest.vclassid}">${contest.getVClass().getVclassname()}</a>
						<span class="glyphicon glyphicon-menu-right"></span>
					</c:if>
					<a href="ShowContest?contestid=${contest.id}"
						title="The Contest was created By [${contest.owner.account}]">${fn:escapeXml(contest.title)}</a>
				</h2> -->
				<!-- <div class="col-md-8">
						<c:if test="${fn:escapeXml(fn:trim(contest.subtitle))!=''}">
							<pre>${fn:escapeXml(fn:trim(contest.subtitle))}</pre>
						</c:if>
					</div> -->
			</div>
			<div class="col-md-4">
				<div class="pull-right">
					<c:set var="contest" value="${contest}" scope="request" />
					<!-- <jsp:include page="div/SystemTimeNow.jsp" /> -->
					<jsp:include page="ContestInfo.jsp" />
					<jsp:include page="ContestToolbar.jsp" />
				</div>
			</div>
		</div>
	</div>
</div>
