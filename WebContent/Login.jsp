<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<script type="text/javascript" src="Login.js?${applicationScope.built }"></script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<jsp:include page="include/Modals/Modal_ForgetPassword.jsp" />

	<div class="container">
		<div class="row"></div>
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 text-center">
				<form class="form-horizontal" method="post" action="Login">
					<div class="form-group">
						<label class="sr-only" for="account">account</label>
						<div class="input-group">
							<div class="input-group-addon">帳號：</div>
							<input type="text" class="form-control" id="account"
								placeholder="請輸入帳號" name="account">
						</div>
					</div>
					<div class="form-group">
						<label class="sr-only" for="password">password</label>
						<div class="input-group">
							<div class="input-group-addon">密碼：</div>
							<input type="password" class="form-control" id="passwd"
								placeholder="請輸入密碼" name="passwd">
						</div>
					</div>
					<button type="submit" class="btn btn-primary">登入</button>
					<button type="button" class="btn btn-default" data-toggle="modal"
						data-target="#Modal_ForgetPassword">忘記密碼？</button>
					<input type="hidden" name="returnPage"
						value="${sessionScope.returnPages[0]}" /><input type="hidden"
						name="token" value="${token}" />
				</form>
				<c:if test="${applicationScope.appConfig.isGoogleLoginSetup}">
					<hr>
					<a href="./GoogleLogin" class="btn btn-primary btn-lg btn-block">用
						Google 登入</a>
					<hr>
					<a href="./InsertGoogleUser"
						class="btn btn-primary btn-lg btn-block"> 以現有的 Google 帳號創建身份</a>
				</c:if>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
