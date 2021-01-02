<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!-- <div id="contest_dialog"
	style="cursor: default; padding: 10px; display: none;">
	<h2 id="dialog_h2"></h2>
</div>
 -->
<div id="contestToolbar" data-contestid="${contest.id }">
	<div class="btn-group btn-group-sm" role="group" aria-label="...">
		<c:if
			test="${contest:isVisible_startContest(sessionScope.onlineUser, contest)}">
			<button type="button" class="btn btn-default"
				data-target="#Modal_confirm" data-title="立即開始"
				data-content="<h3>立即開始 - 「${contest.title }」</h3>" data-type="POST"
				data-url="Contest.api"
				data-qs="action=forcedstart&contestid=${contest.id}">
				<span class="glyphicon glyphicon-play" title="立即開始"> </span>
			</button>
		</c:if>
		<c:if
			test="${contest:isVisible_pauseContest(sessionScope.onlineUser, contest)}">
			<button type="button" class="btn btn-default"
				data-target="#Modal_confirm" data-title="立即暫停"
				data-content="<h3>立即暫停 - 「${contest.title }」</h3>" data-type="POST"
				data-url="Contest.api"
				data-qs="action=doPause&contestid=${contest.id}">
				<span class="glyphicon glyphicon-pause"
					title="立即暫停(${contest.title })！"> </span>
			</button>
		</c:if>
		<c:if
			test="${contest:isVisible_resumeContest(sessionScope.onlineUser, contest)}">
			<button type="button" class="btn btn-default"
				data-target="#Modal_confirm" data-title="繼續"
				data-content="<h3>繼續 - 「${contest.title }」</h3>" data-type="POST"
				data-url="Contest.api"
				data-qs="action=doResume&contestid=${contest.id}">
				<span class="glyphicon glyphicon-chevron-right"
					title="繼續(${contest.title })！"> </span>
			</button>
		</c:if>

		<c:if
			test="${contest:isVisible_stopContest(sessionScope.onlineUser, contest)}">
			<button type="button" class="btn btn-default"
				data-target="#Modal_confirm" data-title="強制結束"
				data-content="<h3>強制結束 - 「${contest.title }」</h3>
				(請注意，強制結束後無法再啟動！)"
				data-type="POST" data-url="Contest.api"
				data-qs="action=forcedstop&contestid=${contest.id }">
				<span class="glyphicon glyphicon-stop"> </span>
			</button>
		</c:if>

	</div>
	<div class="btn-group btn-group-sm" role="group" aria-label="...">
		<c:if test="${contest.visible_UpdateContest(pageContext.session)}">
			<a href="./UpdateContest?contestid=${contest.id}"
				class="btn btn-default" id="editContest"
				title="編輯(${contest.title })！"> <i
				class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i>
			</a>
		</c:if>
		<c:if
			test="${contest:isVisible_EditContestants(sessionScope.onlineUser, contest)}">
			<a href="./EditContestants?contestid=${contest.id }"
				class="btn btn-default" id="editContestants" title="管理參加者"> <i
				class="fa fa-users fa-lg" aria-hidden="true"></i>
			</a>
		</c:if>
		<c:if
			test="${contest:isVisible_EditContestants(sessionScope.onlineUser, contest)}">
			<button class="btn btn-default btn-xs" id="rejudgeContest"
				data-contestid="${contest.id }" title="重測">
				<i class="fa fa-repeat" aria-hidden="true" title="重測"></i>
			</button>
		</c:if>
		<c:if
			test="${contest:isVisible_EditContestants(sessionScope.onlineUser, contest)}">
			<%-- 			<button class="btn btn-default btn-xs" id="ShowContestJSONSettings"
				data-contestid="${contest.id }" title="Contest JSON設定">
				<i class="glyphicon glyphicon-cog" aria-hidden="true"
					title="Contest JSON設定"></i>
			</button>
 --%>
			<%-- 			<jsp:include page="./Modals/Modal_ShowContestSettings.jsp">
				<jsp:param value="Contestid: ${contest.id}" name="title" />
			</jsp:include>
			<button type="button" class="btn btn-default btn-xs"
				data-toggle="modal" data-target="#Modal_ShowContestSettings_${contest.id}"
				title="JSON Settings" id="btn_ContestSettings"
				data-contestid="${contest.id}" title="Contest JSON設定">
				<i class="glyphicon glyphicon-cog" aria-hidden="true"></i>
			</button>
 --%>
		</c:if>

		<%-- 		<c:if
			test="${contest:isVisible_rebuiltContest(sessionScope.onlineUser)}">
			<a href="./RebuiltContest?contestid=${contest.id }"
				class="btn btn-default" id="editContestants"> <span
				class="glyphicon glyphicon-refresh" title="重新計算 contest"> </span>
			</a>
		</c:if>
 --%>
		<c:if test="${contest.visible_rebuiltContest(pageContext.session)}">
			<a
				href="javascript:if(confirm('若您在競賽進行當中更改了題目或者配分，請務必重新計算，否則成績結果將會錯誤！'))location='./RebuiltContest?contestid=${contest.id}'"
				title="重新計算配分：若您在競賽進行當中更改了題目或者配分，請務必重新計算，否則成績結果將會錯誤！"
				class="btn btn-default" id="editContestants"> <span
				class="glyphicon glyphicon-refresh"> </span>
			</a>
		</c:if>
		<%-- 		<a
			href="./Problem.api?action=exportProblem&problemid=${problem.problemid}"
			class="btn btn-default" title="匯出題目"><i class="fa fa-download"
			aria-hidden="true"></i> </a>
		<c:if test="${contest.visible_ExportContest(pageContext.session)}">
			<a class="btn btn-danger"
				href="./Contest.api?action=doExportContest&contestid=${contest.id }"
				title="匯出競賽"><i class="fa fa-download" aria-hidden="true"></i></a>
		</c:if>
 --%>

	</div>

	<div class="btn-group btn-group-sm" role="group" aria-label="...">
		<a class="btn btn-default"
			href="./ShowContest?contestid=${contest.id}" type="button">題目列表</a>

		<c:if
			test="${contest:isAccessible_ContestSubmissions(sessionScope.onlineUser, contest)}">
			<a class="btn btn-default" id="editContestants"
				href="./ContestSubmissions?contestid=${contest.id}"> <fmt:message
					key="Contest.Submissions" />
			</a>
		</c:if>
		<c:if
			test="${contest:isAccessible_ContestRanking(sessionScope.onlineUser, contest)}">
			<a class="btn btn-default" id="editContestants"
				href="./ContestRanking?contestid=${contest.id}"> <c:if
					test="${!applicationScope.appConfig.isCLASS_MODE}">
					<fmt:message key="Contest.ContestResult" />
				</c:if> <c:if test="${applicationScope.appConfig.isCLASS_MODE}">
				作答結果
				</c:if>
			</a>
		</c:if>
		<c:if test="${contest:isVisible_DownloadCSV(onlineUser, contest)}">
			<a class="btn btn-default"
				href="./Download.api?target=ContestCSV&contestid=${contest.id}"
				type="button">匯出結果</a>
		</c:if>
	</div>
</div>
