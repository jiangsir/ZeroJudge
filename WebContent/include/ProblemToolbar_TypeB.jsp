<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="btn-group btn-group-xs" role="group" aria-label="...">
	<c:if
		test="${sessionScope.onlineUser.accessFilter_UpdateProblem(problem)}">
		<button type="button" class="btn btn-default" data-toggle="modal"
			data-target="#Modal_ProblemSetting_${problem.problemid }"
			title="題目快速設定">
			<span class="glyphicon glyphicon-cog"></span>
		</button>
	</c:if>
	<c:if test="${sessionScope.onlineUser.isHigherEqualThanMANAGER}">
		<a
			href="./Problem.api?action=exportProblem&problemid=${problem.problemid}"
			class="btn btn-default" title="匯出題目"><i class="fa fa-download"
			aria-hidden="true"></i> </a>
	</c:if>
	<c:if
		test="${sessionScope.onlineUser.accessFilter_UpdateProblem(problem) || problem.isShown_setDifficulty(sessionScope.onlineUser)}">
		<jsp:include page="Modals/Modal_ShowSampleCode.jsp" />
		<button type="button" class="btn btn-default" data-toggle="modal"
			data-target="#Modal_ShowSampleCode_${problem.problemid}"
			title="參考程式碼">
			<i class="fa fa-code" aria-hidden="true"></i>
		</button>
	</c:if>
	<c:if test="${problem.isShown_setDifficulty(sessionScope.onlineUser)}">
		<select class="btn-default" name="difficulty"
			data-difficulty="${problem.difficulty}"
			data-problemid="${problem.problemid}" title="題目分級請以 APCS 難度為參考基準">
			<option value="0" class="bg-default">未分級</option>
			<option value="1" class="bg-info">APCS 1級</option>
			<option value="2" class="bg-success">APCS 2級</option>
			<option value="3" class="bg-primary">APCS 3級</option>
			<option value="4" class="bg-warning">APCS 4級</option>
			<option value="5" class="bg-danger">APCS 5級</option>
		</select>
	</c:if>
</div>
