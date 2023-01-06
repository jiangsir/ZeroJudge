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
<div id="contestToolbar" data-contestid="${contest.id}">
	<div class="btn-group btn-group-sm" role="group" aria-label="...">
		<c:if test="${contest.visible_startContest(sessionScope.onlineUser)}">
			<button type="button" class="btn btn-default" data-target="#Modal_confirm" data-title="立即開始"
				data-content="<h3>立即開始 - 「${contest.title}」</h3>" data-type="POST" data-url="Contest.api"
				data-qs="action=forcedstart&contestid=${contest.id}">
				<span class="glyphicon glyphicon-play" title="立即開始競賽(${contest.title})！"> </span>
			</button>
		</c:if>
		<c:if test="${contest.visible_pauseContest(sessionScope.onlineUser)}">
			<button type="button" class="btn btn-default" data-target="#Modal_confirm" data-title="立即暫停"
				data-content="<h3>立即暫停 - 「${contest.title }」</h3>" data-type="POST" data-url="Contest.api"
				data-qs="action=doPause&contestid=${contest.id}">
				<span class="glyphicon glyphicon-pause" title="立即暫停競賽(${contest.title})！"> </span>
			</button>
		</c:if>
		<c:if test="${contest.visible_resumeContest(sessionScope.onlineUser)}">
			<button type="button" class="btn btn-default" data-target="#Modal_confirm" data-title="繼續"
				data-content="<h3>繼續 - 「${contest.title}」</h3>" data-type="POST" data-url="Contest.api"
				data-qs="action=doResume&contestid=${contest.id}">
				<span class="glyphicon glyphicon-chevron-right" title="繼續競賽(${contest.title})！"> </span>
			</button>
		</c:if>
		<c:if test="${contest.visible_stopContest(sessionScope.onlineUser)}">
			<button type="button" class="btn btn-default" data-target="#Modal_confirm" data-title="強制結束" data-content="<h3>強制結束 - 「${contest.title}」</h3>
						(請注意，強制結束後無法再啟動！)" data-type="POST" data-url="Contest.api" data-qs="action=forcedstop&contestid=${contest.id}">
				<span class="glyphicon glyphicon-stop" title="停止競賽(${contest.title})！"> </span>
			</button>
		</c:if>

	</div>
	<div class="btn-group btn-group-sm" role="group" aria-label="...">
		<c:if test="${contest.visible_UpdateContest(pageContext.session)}">
			<a href="./UpdateContest?contestid=${contest.id}" class="btn btn-default" id="editContest"
				title="編輯(${contest.title})！"> <i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i>
			</a>
		</c:if>
		<c:if test="${contest.visible_EditContestants(sessionScope.onlineUser)}">
			<a href="./EditContestants?contestid=${contest.id}" class="btn btn-default" id="editContestants"
				title="管理參加者"> <i class="fa fa-users fa-lg" aria-hidden="true"></i>
			</a>
		</c:if>
		<%-- 取消 contest rejudge
		<c:if
			test="${contest.visible_EditContestants(sessionScope.onlineUser)}">
			<button class="btn btn-default btn-xs" id="rejudgeContest"
				data-contestid="${contest.id }" title="重測">
				<i class="fa fa-repeat" aria-hidden="true" title="重測"></i>
			</button>
		</c:if>
		--%>

		<jsp:include page="Modals/Modal_ShowContestSettings.jsp">
			<jsp:param value="Contestid: ${contest.id}" name="title" />
			<jsp:param value="${contest.id}" name="contestid" />
		</jsp:include>
		<c:if test="${contest.visible_EditContestants(sessionScope.onlineUser)}">
			<button type="button" class="btn btn-default btn-xs" data-toggle="modal"
				data-target="#Modal_ShowContestSettings_${contest.id}" id="btn_ContestSettings" data-contestid="${contest.id}"
				title="Contest JSON設定">
				<i class="glyphicon glyphicon-cog" aria-hidden="true"></i>
			</button>
		</c:if>

		<c:set var="contest" value="${contest}" scope="request" />
		<jsp:include page="Modals/Modal_ExportContest.jsp">
			<jsp:param name="title" value="Contestid: ${contest.id}" />
			<jsp:param name="contestid" value="${contest.id}" />
		</jsp:include>
		<c:if test="${contest.visible_ExportContest(pageContext.session)}">
			<!-- <a class="btn btn-danger bg-manager" href="./Contest.api?action=doExportContest&contestid=${contest.id }" title="匯出競賽(未完成)"><i
					class="fa fa-download" aria-hidden="true"></i></a> -->
			<button type="button" class="btn btn-danger bg-manager" data-toggle="modal"
				data-target="#Modal_ExportContest_${contest.id}" id="btn_exportContest" data-contestid="${contest.id}"
				title="匯出 Contest 設定">
				<i class="fa fa-download" aria-hidden="true"></i>
			</button>
		</c:if>
	</div>

	<div class="btn-group btn-group-sm" role="group" aria-label="...">
		<a class="btn btn-default" href="./ShowContest?contestid=${contest.id}" type="button">題目列表</a>

		<c:if test="${contest:isAccessible_ContestSubmissions(sessionScope.onlineUser, contest)}">
			<a class="btn btn-default" id="editContestants" href="./ContestSubmissions?contestid=${contest.id}">
				<fmt:message key="Contest.Submissions" />
			</a>
		</c:if>
		<c:if test="${contest:isAccessible_ContestRanking(sessionScope.onlineUser, contest)}">
			<a class="btn btn-default" id="editContestants" href="./ContestRanking?contestid=${contest.id}">
				<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
					<fmt:message key="Contest.ContestResult" />
				</c:if>
				<c:if test="${applicationScope.appConfig.isCLASS_MODE}">
					作答結果
				</c:if>
			</a>
		</c:if>
		<c:if test="${contest:isVisible_DownloadCSV(onlineUser, contest)}">
			<a class="btn btn-default" href="./Download.api?target=ContestCSV&contestid=${contest.id}"
				type="button">匯出結果</a>
		</c:if>
	</div>
</div>
