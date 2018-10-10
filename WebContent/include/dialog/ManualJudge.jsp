<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="manualjudge_dialog"
	style="display: none; cursor: default; padding: 20px; margin: auto;">
	<form id="manualjudge_form" method="post">
		<div style="text-align: left; width: 80%;">
			<h2>由評審決定該程式碼的評審結果：</h2>
			<input name="manualjudge_verdict" type="radio" value="AC" /> AC (通過)<br />
			<input name="manualjudge_verdict" type="radio" value="NA" /> NA
			(部分得分)，得 <input name="manualjudge_score" type="text" size="5" /> 分<br />
			<input name="manualjudge_verdict" type="radio" value="WA" /> WA
			(答案錯誤)<br /> <input name="manualjudge_verdict" type="radio"
				value="CE" /> CE (程式錯誤)<br /> <input name="manualjudge_verdict"
				type="radio" value="SE" /> SE (系統錯誤)<br /> <input
				name="manualjudge_verdict" type="radio" value="DN" checked="checked" />
			DN (拒絕。原因：涉嫌抄襲、未遵守題目規定、用不被允許的方法...等)<br /> <br /> 請說明評審結果：<br />
			<textarea name="manualjudge_hint" cols="80%" rows="6"></textarea>
		</div>
		<input name="solutionid" type="hidden" value="${solution.id}" />
	</form>
</div>
