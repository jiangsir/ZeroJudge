<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
<!-- <script language="JavaScript">
function CheckFields(form) {
    form.submit();
}
</script>
 -->
<script language="javascript">
	jQuery(document).ready(function() {
		jQuery("select#language").children().each(function() {
			//               alert("$(this).val()=" + $(this).val() + "\nget('language')=" + "\njQuery('#language')=" + jQuery("span[id=language]").text());

			// 處理 select option 
			if ($(this).val() == jQuery("span#language").text()) {
				//            alert($(this).val() + " selected!!");
				//jQuery給法
				$(this).attr("selected", "true"); //或是給selected也可
				return false;
			}
		});
		jQuery("select#expected_status").children().each(function() {
			// 處理 select option 
			if ($(this).val() == jQuery("span#expected_status").text()) {
				//jQuery給法
				$(this).attr("selected", "true"); //或是給selected也可
				return false;
			}
		});

	});
</script>
</head>
<%-- <jsp:useBean id="systemconfigBean"
	class="tw.zerojudge.Beans.SystemConfigBean" />
 --%>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<form name="form1" method="post" action="">
		<div class="content_individual">
			編輯 testcode <br /> <br />
			<table align="center" width="80%">
				<tr>
					<td>語言：</td>
					<td><span id="language" style="display: none;">${testcode.language}</span>
						<select name="language" id="language">
							<c:forEach var="compiler"
								items="${applicationScope.appConfig.serverConfig.enableCompilers}"
								varStatus="varstatus">
								<option value="${compiler.language}">${compiler.language}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>程式碼：</td>
					<td><textarea name="code" cols="70" rows="20">${testcode.code}</textarea>
					</td>
				</tr>
				<tr>
					<td>期望結果：</td>
					<td><span id="expected_status" style="display: none;">${testcode.expected_status}</span>
						<select id="expected_status" name="expected_status">
							<option value="AC">AC</option>
							<option value="!AC">非 AC</option>
							<option value="CE">CE</option>
							<option value="!CE">非 CE</option>
							<option value="RF">RF</option>
							<option value="!RF">非 RF</option>
							<option value="WA">WA</option>
							<option value="OLD">OLE</option>
							<option value="TLE">TLE</option>
							<option value="MLE">MLE</option>
					</select></td>
				</tr>
				<tr>
					<td>說明：</td>
					<td><label> <textarea name="descript" cols="50"
								rows="10" id="descript">${testcode.descript}</textarea>
					</label></td>
				</tr>
				<tr>
					<td>測資：</td>
					<td><label> </label>
						<table width="200" border="0">
							<tr>
								<th scope="col">indata</th>
								<th scope="col">outdata</th>
							</tr>
							<tr>
								<td><textarea name="indata" cols="25" rows="20" id="indata">${testcode.indata}</textarea></td>
								<td><textarea name="outdata" cols="25" rows="20"
										id="outdata">${testcode.outdata}</textarea></td>
							</tr>
						</table> <label></label></td>
				</tr>
				<tr>
					<td>執行結果：</td>
					<td>${testcode.actual_status}<br /> <pre>${testcode.actual_detail}</pre></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
			<p>
				<input name="testcodeid" type="hidden" value="${testcode.id}" /> <input
					type="submit" class="button" value=" 確認送出" />
			</p>
			<p>&nbsp;</p>
		</div>
	</form>
	<p></p>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
