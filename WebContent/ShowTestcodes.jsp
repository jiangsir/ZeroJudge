<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	pageContext.setAttribute("br", "\n");
%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
<script type="text/javascript" src="./jscripts/jquery.blockUI.js"></script>

<script language="javascript">
	jQuery(document).ready(function() {
		jQuery("span[id=language]").click(function() {
			var index = jQuery("span[id=language]").index(this);
			jQuery('td[name=code]:eq(' + index + ') pre').toggle("slow");
		});

		jQuery("img[id=delete]").click(function() {
			var index = jQuery("img[id=delete]").index(this);
			var id = jQuery("span[id=testcodeid]:eq(" + index + ")").text();
			//		if(!prompt("這個動作會刪除這個 testcode , 確定要繼續嗎？")){
			//			return;
			//		}

			if (!confirm("這個動作會刪除這個 testcode , 確定要繼續嗎？")) {
				return;
			}
			jQuery.ajax({
				type : "POST",
				url : "./Testcode.ajax",
				data : "action=delete&id=" + id,
				async : false,
				timeout : 5000,
				success : function(result) {
					jQuery("tr:eq(" + (index + 1) + ")").remove();
				}
			});
		});

		jQuery("img[id=run]").click(function() {
			var index = jQuery("img[id=run]").index(this);
			var id = jQuery("span[id=testcodeid]:eq(" + index + ")").text();
			jQuery.ajax({
				type : "POST",
				url : "./Testcode.ajax",
				data : "action=run&id=" + id,
				async : false,
				timeout : 5000,
				success : function(result) {
				}
			});
		});

		jQuery("a[id=readtestdata]").click(function() {
			var index = jQuery("a[id=readtestdata]").index(this);
			$.blockUI({
				message : $('#readtestdata_dialog'),
				css : {
					top : 3 + '%',
					left : 10 + '%',
					width : '80%'
				}
			});
			var id = jQuery("span[id=testcodeid]:eq(" + index + ")").text();
			jQuery.ajax({
				type : "POST",
				url : "./Testcode.ajax",
				data : "action=readindata&id=" + id,
				async : false,
				timeout : 5000,
				success : function(result) {
					jQuery("#indata").val(result);
				}
			});
			jQuery.ajax({
				type : "POST",
				url : "./Testcode.ajax",
				data : "action=readoutdata&id=" + id,
				async : false,
				timeout : 5000,
				success : function(result) {
					jQuery("#outdata").val(result);
				}
			});
		});
		$('#return').click(function() {
			$.unblockUI();
			return false;
		});
	});
</script>
</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />

<body>
	<jsp:include page="include/Header.jsp" />
	<div id="readtestdata_dialog"
		style="cursor: default; padding: 10px; display: none;">
		<p>本測資檔的輸入、輸出</p>
		<table width="80%" border="0"
			style="margin: auto; text-align: center;">
			<tr>
				<th scope="col">輸入測資 <span id="indataInfo"></span></th>
				<th scope="col">輸出測資 <span id="outdataInfo"></span></th>
			</tr>
			<tr>
				<td><textarea name="indata" id="indata" cols="30" rows="18"></textarea></td>
				<td><textarea name="outdata" id="outdata" cols="30" rows="18"></textarea></td>
			</tr>
		</table>
		<br /> <input type="button" id="return" value="返回" />
	</div>

	<br>
	<div class="content_individual">
		<div>
			<a href="./InsertTestcode">新增一筆 testcode</a> | 檢測所有 testcode
		</div>
		<hr />
		<table width="90%">
			<tr>
				<th scope="col">&nbsp;</th>
				<th scope="col">code</th>
				<th scope="col">正確結果</th>
				<th scope="col">運行結果</th>
				<th width="30%" scope="col">運行結果細節</th>
				<th scope="col">測資</th>
				<th width="20%" scope="col">說明</th>
				<th scope="col">執行</th>
			</tr>
			<c:forEach var="testcode" items="${testcodes}" varStatus="status">
				<jsp:useBean id="testcodebean"
					class="tw.zerojudge.Beans.TestcodeBean" />
				<jsp:setProperty name="testcodebean" property="expected_status"
					value="${testcode.expected_status}" />
				<jsp:setProperty name="testcodebean" property="actual_status"
					value="${testcode.actual_status}" />

				<tr>
					<th scope="row"><c:if test="${testcodebean.assertEqual==true}">
							<img src="images/checked18.png" />
						</c:if>
						<c:if test="${testcodebean.assertEqual==false}">
							<img src="images/alert18.png" width="18" height="17" />
						</c:if> ${status.count}</th>
					<td name="code"><span id="testcodeid">${testcode.id}</span>: <span
						id="language" style="cursor: pointer; text-decoration: underline;">${testcode.language}</span><br />
						<pre style="background: #eeeeee; display: none;">${fn:escapeXml(testcode.code)}</pre></td>
					<td>${testcode.expected_status}</td>
					<td>${testcode.actual_status}</td>
					<td width="30%"><pre>${fn:escapeXml(testcode.actual_detail)}</pre></td>
					<td><a href="#" id="readtestdata">展開</a></td>
					<td width="20%"><pre>${fn:escapeXml(testcode.descript)}</pre></td>
					<td><img id="run" src="images/exec18.png"
						style="cursor: pointer;" /> | <a
						href="./UpdateTestcode?testcodeid=${testcode.id}"><img
							src="images/svg/Edit.svg" style="height: 1.2em" border="0" /></a> | <img id="delete"
						src="images/delete18.svg" style="cursor: pointer; height: 1.2em;" /></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<p></p>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
