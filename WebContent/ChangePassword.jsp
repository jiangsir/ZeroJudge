<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<script type="text/javascript"
	src="ChangePassword.js?${applicationScope.built }"></script>

<script type="text/javascript">
	jQuery(document).ready(function() {
	});
</script>

</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<form class="form-horizontal">
				<table class="table table-hover">
					<tr>
						<td width="112">帳號：</td>
						<td width="351">${user.account}</td>
					</tr>
					<tr>
						<td>設定系統密碼</td>
						<td><input type="password" class="form-control"
							placeholder="Password" name="passwd" value="${user.passwd}">
							<p class="help-block">請輸入密碼</p> <input type="password"
							class="form-control" placeholder="Password2" name="passwd2"
							value="${user.passwd}">
							<p class="help-block">請再次輸入密碼</p></td>
					</tr>
				</table>

				<input type="hidden" name="token" value="${token.base64}">
				<button class="btn btn-success btn-lg col-md-12" id="save">修改密碼</button>

			</form>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
