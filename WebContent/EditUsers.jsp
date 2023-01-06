<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
	<script type="text/javascript" src="include/dialog/Confirm.js?${applicationScope.built}"></script>
	<script type="text/javascript" src="include/Modals/Modal_InsertBatchedUsers.js?${applicationScope.built}"></script>
	<script type="text/javascript" src="include/UserToolbar.js?${applicationScope.built}"></script>
	<script type="text/javascript">
		jQuery(document).ready(function () {
		});
	</script>
</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<jsp:include page="include/div/Button_InsertBatchedUsers.jsp" />
			<jsp:include page="include/Modals/Modal_InsertBatchedUsers.jsp" />
			<a class="btn btn-primary" href="./InsertUser" role="button">註冊一個新帳號</a>
			<a class="btn btn-primary" href="./ShowOnlineUsers" role="button">線上使用者</a>
			<form style="display: inline;" name="form1" method="POST" action="./User.api?action=searchUsers"
				onsubmit="checkForm(this);">
				尋找使用者： <input name="keyword" type="text" />
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
				<c:forEach var="user" items="${users}">
					<tr>
						<td>${user.id }</td>
						<td>
							<c:set var="user" value="${user}" scope="request" />
							<jsp:include page="include/div/UserAccount_TypeA.jsp" />
							<c:if test="${user.getIsBatchedUser()}">
								<button type="button" class="btn btn-default btn-xs" id="BatchedUser"
									title="批次帳號 createby ${user.getCreatebyUser()}" style="float: right;"><i class="fa fa-users"
										aria-hidden="true"></i><i class="fa-solid fa-users-medical"></i>
								</button>
							</c:if>
						</td>
						<td>
							<div id="${user.account}hint" class="hint">
								AC=${user.ac}, WA=${user.wa}, TLE=${user.tle}, RE=${user.re},
								CE=${user.ce},
								<!-- RANKPOINT=user.rankpoint -->
							</div>
						</td>
						<td>${user.registertime }<br>
							<!-- 					user.rankbefore->user.ranknow&nbsp;
	-->
						</td>
						<td>
							<c:if test="${user.extraprivilege!=null && user.extraprivilege!=''}">
								<img src="images/extraprivilege.png" title="${user.extraprivilege}" />
							</c:if>&nbsp;
						</td>
						<td>
							<c:set var="user" value="${user}" scope="request" />
							<jsp:include page="include/UserToolbar_Bootstrap.jsp" />
						</td>
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
