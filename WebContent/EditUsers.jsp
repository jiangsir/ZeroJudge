<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/Modals/Modal_InsertUsers.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/UserToolbar.js?${applicationScope.built }"></script>

<script type="text/javascript">
	jQuery(document).ready(function() {

	});
</script>
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<!-- 	<div id="InsertUsers_dialog"
		style="cursor: default; padding: 10px; display: none;">
		請依據一下格式指定要新增的使用者。<br /> 帳號,匿稱,真實姓名,密碼明碼,生日,E-mail,學校 id<br /> 例：
		Tommy091,Tommy,王湯米,tommy1234,1990,tommy091@gmail.com,0<br /> <br />
		<textarea id="userscripts" name="userscripts" style="width: 100%"
			rows="25"></textarea>
		<br />
	</div>
	<br />
 -->
	<div class="container">
		<div class="row">
			<button type="button" class="btn btn-primary" data-toggle="modal"
				data-target="#Modal_InsertUsers">批次新增使用者</button>
			<jsp:include page="include/Modals/Modal_InsertUsers.jsp" />
			<a class="btn btn-primary" href="./InsertUser" role="button">註冊一個新帳號</a>
			<a class="btn btn-primary" href="./ShowOnlineUsers" role="button">線上使用者</a>
			<form style="display: inline;" name="form1" method="get"
				action="./UserStatistic" onsubmit="checkForm(this);">
				選擇使用者： <input id="account" name="account" type="text" />
			</form>
			<br />
			<table class="table table-hover">
				<tr>
					<td>UserID</td>
					<td>Account</td>
					<td>解題統計</td>
					<td></td>
					<td>特權</td>
					<td>操作</td>
				</tr>
				<c:forEach var="user" items="${list }">
					<tr>
						<td>${user.id }</td>
						<td><c:set var="user" value="${user}" scope="request" /> <jsp:include
								page="include/div/UserAccount_TypeA.jsp" /></td>
						<td>
							<div id="${user.account}hint" class="hint">AC=${user.ac},
								WA=${user.wa}, TLE=${user.tle}, RE=${user.re}, CE=${user.ce},
								<!-- RANKPOINT=user.rankpoint --></div>
						</td>
						<td>${user.registertime }<br>
							<!-- 					user.rankbefore->user.ranknow&nbsp;
 -->
						</td>
						<td><c:if
								test="${user.extraprivilege!=null && user.extraprivilege!=''}">
								<img src="images/extraprivilege.png"
									title="${user.extraprivilege}" />
							</c:if>&nbsp;</td>
						<td><c:set var="user" value="${user}" scope="request" /> <jsp:include
								page="include/UserToolbar_Bootstrap.jsp" /></td>
					</tr>
				</c:forEach>
			</table>
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
