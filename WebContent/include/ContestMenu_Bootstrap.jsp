<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<div id="contestmenu" class="text-center">
	<c:if test="${contest.vclassid!=0}">
		<a class="btn btn-primary"
			href="./ShowVClass?vclassid=${contest.vclassid}" type="button">回到所屬課程</a>
	</c:if>
	<a class="btn btn-primary"
		href="./ShowContest?contestid=${param.contestid}" type="button">題目列表</a>
	<c:if
		test="${contest:isAccessible_ContestSubmissions(sessionScope.onlineUser, contest)}">
		<a class="btn btn-primary"
			href="./ContestSubmissions?contestid=${param.contestid}"
			type="button"><fmt:message key="Contest.Submissions" /></a>
	</c:if>
	<c:if
		test="${contest:isAccessible_ContestRanking(sessionScope.onlineUser, contest)}">
		<a class="btn btn-primary"
			href="./ContestRanking?contestid=${param.contestid}" type="button"><fmt:message
				key="Contest.ContestResult" /></a>
	</c:if>
<%-- 	<c:if
		test="${contest:isAccessible_EditContests(sessionScope.onlineUser, contest)}">
		<a class="btn btn-primary"
			href="./EditContests?contestid=${param.contestid}" type="button">管理</a>
	</c:if>
 --%>	<hr>
</div>
