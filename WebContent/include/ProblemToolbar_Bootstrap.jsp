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
	jQuery(document)
			.ready(
					function() {
						$("select[name=problemdisplay]").each(function() {
							// var problemid = $(this).attr("problemid");
							var display = $(this).data("problemdisplay");
							$(this).children().each(function() {
								if (display == $(this).val()) {
									$(this).attr("selected", "true");
									return;
								}
							});
						});
						$("select[name=problemdisplay]")
								.change(
										function() { // 事件發生
											// alert($(this).val());
											var problemid = $(this).data(
													"problemid");
											jQuery(
													'select[name=problemdisplay] option:selected')
													.each(
															function() { // 印出選到多個值
																if (problemid == $(
																		this)
																		.parent()
																		.data(
																				"problemid")) {
																	// alert(problemid+", "+$(this).val());
																	setProblemDisplay(
																			problemid,
																			$(
																					this)
																					.val());
																}
															});
											location.reload();
										});

					});

	function setProblemDisplay(problemid, display) {
		jQuery.ajax({
			type : "GET",
			url : "./Problem.api",
			data : "action=setProblemDisplay&problemid=" + problemid
					+ "&display=" + display,
			async : false,
			timeout : 5000,
			success : function(result) {
			}
		});
	}
</script>

<c:if
	test="${sessionScope.onlineUser.isDEBUGGER || sessionScope.onlineUser.isCanUpdateProblem(problem)}">
	<div id="ProblemToolbar" data-problemid="${problem.problemid}">
		<c:set var="problem" value="${problem}" scope="request" />

		<jsp:include page="Modals/Modal_ProblemSetting.jsp" />
		<jsp:include page="Modals/Modal_ShowSampleCode.jsp" />
		<jsp:include page="Modals/Modal_ServerOutputs.jsp" />
		<jsp:include page="Modals/Modal_RejudgeProblem.jsp" />

		<select name="problemdisplay" class="form-control"
			data-problemdisplay="${problem.display}"
			data-problemid="${problem.problemid}">
			<option value="---">---</option>
			<!-- 20180110 改成通過 30% 一律可以公開。教師不可以公開。 -->
			<c:if
				test="${sessionScope.onlineUser.isHigherEqualThanMANAGER || sessionScope.onlineUser.isQualifiedAuthor}">
				<option value="open">公開題</option>
			</c:if>
			<!-- 			<option value="verifying">提交</option>
 -->
			<option value="practice">練習題</option>
			<option value="hide">隱藏</option>
		</select>
		<div class="btn-group btn-group-sm" role="group" aria-label="...">
			<div class="btn btn-default" id="problemStatusInfo">
				<c:set var="problem" value="${problem}" scope="request" />
				<jsp:include page="div/DivProblemStatusInfo_Bootstrap.jsp" />
			</div>
			<button type="button" class="btn btn-default" data-toggle="modal"
				data-target="#Modal_ShowSampleCode_${problem.problemid}"
				title="參考程式碼">
				<i class="fa fa-code" aria-hidden="true"></i>
			</button>
			<div class="btn btn-default" title="解題統計">
				<a href="./Submissions?problemid=${problem.problemid}&status=AC"
					title="不重複的通過人數">${problem.acusers}人</a>/<a
					href="./Submissions?problemid=${problem.problemid}"
					title="不重複的嘗試解題人數">${problem.submitusers}人 </a>
				<c:choose>
					<c:when test="${problem.submitusers==0}">(0%)</c:when>
					<c:otherwise>(
                    <fmt:formatNumber
							value="${problem.acusers/problem.submitusers}" type="percent" />
                    )</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="btn-group btn-group-sm" role="group" aria-label="...">
			<button type="button" class="btn btn-default" data-toggle="modal"
				data-target="#Modal_ProblemSetting_${problem.problemid }"
				title="ProblemSetting">
				<span class="glyphicon glyphicon-cog"></span>
			</button>

			<!-- 	<a class="btn btn-default" href="#" role="button"><span
		class="glyphicon glyphicon-cog"></span></a>
 -->
			<!-- 	<button type="button" class="btn btn-default" data-toggle="modal"
		data-target="#Modal_EditProblem">
		<span class="glyphicon glyphicon-pencil"></span>
	</button>
 -->
			<a href="./UpdateProblem?problemid=${problem.problemid}"
				class="btn btn-default" title="編輯題目"><i
				class="fa fa-pencil-square-o" aria-hidden="true"></i></a>

			<%-- 		<button type="button" class="btn btn-default" id="prejudgeProblem"
			data-problemid="${problem.problemid}" title="用題目 samplecode 進行前測">
			<span class="glyphicon glyphicon-repeat"></span>
		</button>
 --%>
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
		</div>
		<div class="btn-group btn-group-sm" role="group" aria-label="...">
			<button type="button" class="btn btn-default" title="立即將本題目測資同步到裁判機"
				id="rsyncTestfiles" data-problemid="${problem.problemid }">
				<span class="glyphicon glyphicon-transfer"></span>
			</button>

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

			<%-- 			<button type="button" class="btn btn-default" title="刪除題目"
				data-toggle="modal" data-target="#Modal_confirm" data-title="刪除題目"
				data-content="確定要刪除這個題目嗎(${problem.problemid })？" data-type="GET"
				data-url="./DeleteProblem" data-qs="problemid=${problem.problemid }">
				<span class="glyphicon glyphicon-remove"></span>
			</button>
 --%>
		</div>
	</div>
</c:if>
