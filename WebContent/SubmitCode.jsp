<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>

<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
<script type="text/javascript"
	src="SubmitCode.js?${applicationScope.built }"></script>

</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<body>
	<jsp:include page="include/Header.jsp" />
	<div id="Testjudge_dialog" alt=""
		style="cursor: default; padding: 10px; display: none;">
		<fieldset>
			<legend style="text-align: left;">測試執行說明：</legend>
			<div style="text-align: left;">
				<ul>
					<li>“測試執行”讓您得以在送出程式碼之前先測試本系統的編譯器，以檢測編譯器之間的差異。</li>
					<li>自定測資一律以單測資點進行，測資長度限制在各 1000 字元以內。</li>
					<li>“測試執行”不會計入成績及送出次數。</li>
				</ul>
				<span style="font-size: larger; color: #FF0000">請注意：
					測試執行僅測試您所提供的輸入輸出測資，與實際題目測資並不相同。</span>
			</div>
		</fieldset>
		<form id="form_Testjudge">
			<br />
			<fieldset>
				<legend style="text-align: left;">測試程式碼及自訂測資</legend>
				<div style="display: block; clear: both; text-align: left;">
					<c:forEach var="compiler"
						items="${applicationScope.appConfig.serverConfig.enableCompilers}">
						<input name="language" type="radio" value="${compiler.language}"
							userlanguage="${sessionScope.onlineUser.userlanguage}" />
						<span style="font-weight: bold; font-size: large">${compiler.language}</span>: ${compiler.version}<br />
					</c:forEach>
				</div>
				<div style="float: left; width: 100%">
					<fmt:message key="Problem.Code" />
					：<br />
					<textarea id="testcode" name="testcode" rows="12"
						style="width: 100%"></textarea>
				</div>
				<div style="float: left; width: 100%">
					<span style="clear: both; float: left; width: 50%">輸入測資：(可自行修改)<br />
						<textarea id="testjudge_indata" name="testjudge_indata" rows="10"
							style="width: 100%">${problem.sampleinput}</textarea>
					</span> <span style="float: left; width: 50%">輸出測資：(可自行修改)<br /> <textarea
							id="testjudge_outdata" name="testjudge_outdata" rows="10"
							style="width: 100%">${problem.sampleoutput}</textarea>
					</span>
				</div>
			</fieldset>
			<input name="contestid" type="hidden"
				value="${sessionScope.onlineUser.joinedcontestid}" /> <input
				name="problemid" type="hidden" value="${problem.problemid}" />
		</form>
	</div>
	<div id="showdetail_dialog" alt=""
		style="cursor: default; padding: 10px; display: none; text-align: left">
		<img src="images/Spinner.gif" id="spinner" />
		<div id="jsondetail" style="text-align: left">
			<span id="judgement"></span> <span id="info"
				style="font-size: smaller;"></span><br /> <span id="reason"></span>
			<pre id="hint"></pre>
			<hr />
		</div>
	</div>

	<br />
	<div class="content_individual">
		<form name="form1" id="form1" method="post"
			enctype="multipart/form-data" action="">
			<table width="80%" height="150" border="0" align="center"
				cellpadding="0" cellspacing="0">
				<tr class="content_individual">
					<td><p align="left">
							<strong> <fmt:message key="Problem.ID" /> ：
							</strong>${param.problemid}
						</p>
						<p align="left">
							<strong> <fmt:message key="Problem.Title" /> ：
							</strong>${fn:escapeXml(problem.title)} <a
								href="Submissions?problemid=${problem.problemid}"></a>
						</p>
						<p align="left">
							<strong> <fmt:message key="Problem.Content" /> ：
							</strong><br /> ${problem.content}
						</p>
						<div style="text-align: left">
							<br />
							<div style="display: block; clear: both;">
								<fmt:message key="Problem.ProgramLanguage" />
								：<br />
								<c:forEach var="compiler"
									items="${applicationScope.appConfig.serverConfig.enableCompilers}">
									<input name="language" type="radio"
										value="${compiler.language}"
										userlanguage="${sessionScope.onlineUser.userlanguage}" />
									<span style="font-weight: bold; font-size: large">${compiler.language}</span>: ${compiler.version}<br />
								</c:forEach>
							</div>
							<fmt:message key="Problem.Code" />
							：<br />
							<textarea name="code" cols="80" rows="20" id="code"
								style="width: 100%"></textarea>
							<br />
							<c:if
								test="${sessionScope.onlineUser.joinedContest.checkedConfig_Exefile}">
								<br />
            請上傳編譯後的執行檔：
            <input name="Executable" type="file" />(檔案上限：${maxFileSize })
                            </c:if>
						</div></td>
				</tr>
			</table>
			<br />
			<div style="text-align: center">
				<input name="problemid" type="hidden" id="problemid"
					value="${param.problemid}" /> <input name="title" type="hidden"
					id="title" value="${fn:escapeXml(problem.title)}" /> <input
					type="submit"></input>
				<%-- 				<c:if
					test="${problem:getIsTestjudgeAccessible(sessionScope.onlineUser)}">
					<span id="Testjudge" type="button">測試執行</span>
				</c:if>
 --%>
				<br />
			</div>
			<br />
		</form>
	</div>
	<p></p>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
