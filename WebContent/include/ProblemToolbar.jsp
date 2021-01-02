<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<script type="text/javascript">
	jQuery(document).ready(function() {

	});
</script>

<c:if
	test="${sessionScope.onlineUser.accessFilter_UpdateProblem(problem)}">
	<div id="ProblemToolbar" data-problemid="${problem.problemid}">
		<c:set var="problem" value="${problem}" scope="request" />

		<jsp:include page="Modals/Modal_ProblemSetting.jsp" />
		<jsp:include page="Modals/Modal_ShowSampleCode.jsp" />
		<jsp:include page="Modals/Modal_ServerOutputs.jsp" />
		<jsp:include page="Modals/Modal_RejudgeProblem.jsp" />

		<div class="btn-group btn-group-sm" role="group" aria-label="...">
			<select class="btn-default" name="problemdisplay"
				data-problemdisplay="${problem.display}"
				data-problemid="${problem.problemid}">
				<option value="---">---</option>
				<c:if
					test="${sessionScope.onlineUser.isHigherEqualThanMANAGER || sessionScope.onlineUser.isQualifiedAuthor}">
					<option value="open">公開題</option>
				</c:if>
				<option value="practice">練習題</option>
				<option value="hide">隱藏</option>
			</select>
			<%-- 			<c:if test="${sessionScope.onlineUser.isHigherEqualThanMANAGER}">
 --%>
			<c:if
				test="${problem.isShown_setDifficulty(sessionScope.onlineUser)}">
				<select class="btn-default" name="difficulty"
					data-difficulty="${problem.difficulty}"
					data-problemid="${problem.problemid}" title="題目分級請以 APCS 難度為參考基準">
					<option value="0">未分級</option>
					<option value="1">APCS 1級</option>
					<option value="2">APCS 2級</option>
					<option value="3">APCS 3級</option>
					<option value="4">APCS 4級</option>
					<option value="5">APCS 5級</option>
				</select>
			</c:if>
			<%-- 			</c:if>
 --%>
			<div class="btn btn-default" id="problemStatusInfo">
				<c:set var="problem" value="${problem}" scope="request" />
				<jsp:include page="div/DivProblemStatusInfo_Bootstrap.jsp" />
			</div>
		</div>
		<div class="btn-group btn-group-sm" role="group" aria-label="...">
			<button type="button" class="btn btn-default" data-toggle="modal"
				data-target="#Modal_ProblemSetting_${problem.problemid }"
				title="ProblemSetting">
				<span class="glyphicon glyphicon-cog"></span>
			</button>
			<a href="./UpdateProblem?problemid=${problem.problemid}"
				class="btn btn-default" title="編輯題目"><i
				class="fa fa-pencil-square-o" aria-hidden="true"></i></a>

			<button type="button" class="btn btn-default" data-toggle="modal"
				data-target="#Modal_ShowSampleCode_${problem.problemid}"
				title="參考程式碼">
				<i class="fa fa-code" aria-hidden="true"></i>
			</button>

			<button type="button" class="btn btn-default"
				title="rejudge 本題目全部程式碼。" id="rejudgeProblem"
				data-problemid="${problem.problemid }">
				<img src="images/Spinner.gif" id="ProblemRejudge_spinner"
					style="display: none;" /> <span
					class="glyphicon glyphicon-refresh" id="ProblemRejudge_icon"></span>
				<span id="WaitingSubmissionCount"></span>
			</button>
			<a
				href="./Problem.api?action=exportProblem&problemid=${problem.problemid}"
				class="btn btn-default" title="匯出題目"><i class="fa fa-download"
				aria-hidden="true"></i> </a>
			<%-- 			<button type="button" class="btn btn-default" title="立即將本題目測資同步到裁判機"
				id="rsyncTestfiles" data-problemid="${problem.problemid }">
				<span class="glyphicon glyphicon-transfer"></span>
			</button>
 --%>
			<button type="button" class="btn btn-default"
				id="recountAcceptedProblem" data-problemid="${problem.problemid }"
				title="重新計算通過的人數">
				<i class="fa fa-cogs" aria-hidden="true"></i>
			</button>

			<button type="button" class="btn btn-default" title="放棄題目？"
				data-toggle="modal" data-target="#Modal_confirm" data-title="放棄題目？"
				data-content="確定要放棄這個題目嗎(${problem.problemid })？" data-type="GET"
				data-url="./DeprecateProblem"
				data-qs="problemid=${problem.problemid }">
				<span class="glyphicon glyphicon-trash"></span>
			</button>
		</div>
	</div>
</c:if>
