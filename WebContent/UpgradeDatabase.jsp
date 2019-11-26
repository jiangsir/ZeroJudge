<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
<script language="javascript">
	jQuery(document).ready(function() {
		jQuery("input[name='upgrade']").click(function() {
			var from_version = $(this).attr("from_version");
			if (!confirm("確定開始升級資料庫(" + from_version + ")？")) {
				return;
			}
			$(this).parent().submit();
		});
	});
</script>
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<p>升級資料庫</p>
		<h2>
			目前系統版本為 applicationScope.appConfig.version <br /> 現行資料表屬於
			${DatabaseVersion}
		</h2>
		<div>
			若您的資料庫無法取得版本號，請自行挑選您此前所使用的系統版本，並進行升級。若您此前所使用的系統版本不在以下列表當中，則代表您的版本無法升級到目前這個版本。</div>
		<table width="50%" border="0" align="center">
			<tr>
				<th scope="col">&nbsp;</th>
				<th scope="col">版本號</th>
				<th scope="col">&nbsp;</th>
			</tr>
			<tr>
				<th scope="row">&nbsp;</th>
				<tr>
					<th scope="row">&nbsp;</th>
					<td>從 R1.4 Built0908</td>
					<td><form id="form" name="form1" method="post" action="">
							<input name="from_version" type="hidden" value="R1.4 Built0908" />
							<input name="to_version" type="hidden"
								value="applicationScope.appConfig.version" /> <input
								id="upgrade" name="upgrade" type="button"
								from_version="R1.4 Built0908" value="升級" />
						</form></td>
				</tr>
				<tr>
					<th scope="row">&nbsp;</th>
					<td>從 R1.2 Built0321</td>
					<td>升級</td>
				</tr>
			</tr>
		</table>
		<br /> <br />
		<p>&nbsp;</p>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
