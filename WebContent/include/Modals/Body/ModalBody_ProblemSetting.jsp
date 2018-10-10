<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<form class="form-horizontal"
	id="Form_ProblemSetting_${problem.problemid }"
	action="./Problem.api?action=setProblemSettings&problemid=${problem.problemid}">
	<div class="form-group">
		<label for="tname" class="col-sm-2 control-label">指定本題目分類：</label>
		<div class="col-sm-10">
			<script type="text/javascript">
				jQuery(document).ready(function() {
					$("input[name='tabid']").each(function() {
						if ($(this).val() == $(this).data("tabid")) {
							$(this).prop("checked", true);
						}
					});
				});
			</script>
			<c:forEach var="tab"
				items="${applicationScope.appConfig.problemtabs}">
				<input name="tabid" type="radio" value="${tab.id}"
					data-tabid="${problem.tabid }" />
				<span id="tabname">${tab.name}</span>: <span id="tabdescript">${tab.descript}</span>
				<br />
			</c:forEach>
		</div>
	</div>
	<div class="form-group">
		<label for="tname" class="col-sm-2 control-label">答案比對方式：</label>
		<div class="col-sm-10">
			<script type="text/javascript">
				jQuery(document).ready(function() {
					$("input[name='judgemode']").each(function() {
						if ($(this).val() == $(this).data("judgemode")) {
							$(this).prop("checked", true);
						}
					});
					$("input[name='wa_visible']").each(function() {
						if ($(this).val() == $(this).data("wa_visible")) {
							$(this).prop("checked", true);
						}
					});
				});
			</script>
			<input name="judgemode" type="radio" value="Tolerant"
				data-judgemode="${problem.judgemode }" /> 寛鬆比對：每一個答案行都經過 trim
			處理，忽略前後不可見字元及空白，並忽略空行。 <br />
			<input name="judgemode" type="radio" value="Strictly"
				data-judgemode="${problem.judgemode }" /> 嚴格比對：就如 UVa/ACM
			的比對方式，所有字元必須完全相同。 <br /> <input name="judgemode" type="radio"
				value="Special" data-judgemode="${problem.judgemode }" />
			自訂比對：Special Judge 由出題者自行撰寫 judge 程式來判斷解題者答案是否正確。<br />
		</div>
		<label for="tname" class="col-sm-2 control-label">WA 時
			是否公開正確答案？：</label>
		<div class="col-sm-10">
			<input name="wa_visible" type="radio" value="1"
				data-wa_visible="${problem.wa_visible}" /> 公開 <input
				name="wa_visible" type="radio" value="0"
				data-wa_visible="${problem.wa_visible}" /> 不公開<br /> <span
				style="font-size: smaller">(若想保護測試資料，可選擇"不公開"，此時解題者可得知每一個測資點的結果，但無法看到正確答案為何)</span>
		</div>
	</div>
	<div class="input-group">
		<div class="input-group-addon">記憶體限制：</div>
		<input type="text" class="form-control" placeholder="記憶體限制"
			name="memorylimit" value='${problem.memorylimit}' pattern='###'>
		<div class="input-group-addon">MBytes</div>
	</div>
	<jsp:include page="../../div/ProblemBackgrounds_INPUT.jsp" />
	<jsp:include page="../../div/ProblemReference_INPUT.jsp" />

	<c:if
		test="${sessionScope.onlineUser.isDEBUGGER}">
		<div class="alert-danger">
			<div class="input-group">
				<div class="input-group-addon">
					<fmt:message key="Problem.Keywords" />
					:
				</div>
				<input type="text" class="form-control"
					placeholder="<fmt:message
                key="Problem.Keywords" />:"
					name="keywords" value='${problem.keywords}' />
			</div>
			<small>(請使用 , 作為分隔。例： [DFS, 台北市資訊學科競賽, 資訊學科競賽] )</small>
			<div class="input-group">
				<div class="input-group-addon">sortable:</div>
				<input type="text" class="form-control" placeholder="排序"
					name="sortable" value='${problem.sortable}' />
			</div>
			<small>(例： 2011 TOI初賽) <br /> (目前題目分類當中的“競賽題庫”, "NPSC",
				"TOI/NOI" 都是以 sortable 倒序作為第一排序原則， problemid 順序作為第二排序原則。因此可用年份為
				sortable，因而可以讓較新的題目在前。)<br /></small>
		</div>
	</c:if>
</form>
