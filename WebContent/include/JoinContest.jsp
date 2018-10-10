<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div id="JoinContest">
	<%-- 	<a class="btn btn-success" href="" type="button" data-toggle="modal"
		data-target="#Modal_ContestLogin">參加競賽</a>
	<jsp:include page="Modals/Modal_ContestLogin.jsp" />
 --%>
	<button class="btn btn-success" id="doJoinContestByOnlineUser" data-contestid=${contest.id}>參加競賽</button>
<%-- 	<form id="form1" name="form1" method="post"
		action="Contest.api?action=doJoinContestByOnlineUser&contestid=${contest.id}">
		<input type="submit" value="參加">
	</form>
 --%>
</div>
