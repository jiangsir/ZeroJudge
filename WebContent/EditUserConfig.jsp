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
	src="EditUserConfig.js?${applicationScope.built }"></script>

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
						<td>密碼</td>
						<td><input type="password" class="form-control"
							placeholder="Password" name="passwd" value="${user.passwd}"
							<c:if test="${!user.isAuthhost_localhost}">readonly</c:if>>
							<p class="help-block">
								<c:if test="${!user.isAuthhost_localhost}">本帳號已綁定 ${user.email}</c:if>
							</p> <input type="password" class="form-control"
							placeholder="Password2" name="passwd2" value="${user.passwd}"
							<c:if test="${!user.isAuthhost_localhost}">readonly</c:if>>
							<p class="help-block">
								<c:if test="${!user.isAuthhost_localhost}">本帳號已綁定 ${user.email }</c:if>
							</p>
							<p class="help-block">兩個密碼欄均不輸入，則代表不修改密碼。</p></td>
					</tr>
					<tr>
						<td>公開暱稱：</td>
						<td><input name="username" type="text" id="username"
							value="${fn:escapeXml(user.username)}" size="30" maxlength="100" /></td>
					</tr>
					<tr>
						<td>真實姓名：</td>
						<td><input name="truename" type="text" id="truename"
							value="${fn:escapeXml(user.truename)}" size="20" maxlength="20" /></td>
					</tr>
					<tr>
						<td>出生年次:</td>
						<td><input name="birthyear" type="text" id="birthyear"
							value="${user.birthyear}" size="5" maxlength="5" /></td>
					</tr>
					<tr>
						<td>學校:</td>
						<td><select name="schoolid">
								<option value="0">選擇...</option>
								<c:forEach var="school" items="${schools}">
									<option value="${school.id}"
										<c:if test="${user.schoolid==school.id}">selected="selected"</c:if>>${school.id}.
										${fn:escapeXml(school.schoolname)}</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td>常用電子郵件：</td>
						<td><input type="text" class="form-control"
							placeholder="email" name="email" type="text"
							value="${fn:escapeXml(user.email)}"
							<c:if test="${!user.isAuthhost_localhost}">readonly</c:if>>
							<p class="help-block">
								<c:if test="${!user.isAuthhost_localhost}">本帳號已綁定 ${user.email }</c:if>
								<c:if test="${!user.isAuthhost_localhost}">請輸入 email</c:if>

							</p></td>
					</tr>
					<tr>
						<td>備註：</td>
						<td><textarea name="comment" cols="50" rows="10" id="comment">${fn:escapeXml(user.comment)}</textarea></td>
					</tr>
					<tr>
						<td><div id="userconfig" data-userconfig="${user.config }">使用者參數：</div></td>
						<td><input type="checkbox" name="config" value="0" />#0
							該帳號是否有效？<br /> 若設定為失效，則該帳號無法登入系統，也看不到這個帳號。<br /> <input
							type="checkbox" name="config" value="1" />#1 是否讓該題目管理員可自行「公開」題目？<br />
							<br /> <input name="config" type="checkbox" id="$ContestManager"
							value="2" />#2 管理競賽的權限<br /> <input name="config"
							type="checkbox" id="InsertProblem" value="3" />#3 新增題目的權限
							(InsertProblem) <br /> <input name="config" type="checkbox"
							id="$ProblemManager" value="4" />#4 修改題目的權限<br /> <input
							name="config" type="checkbox" id="$VClassManager" value="5" />#5
							課程管理權限(可開設課程，進行“隨堂測驗”)<br /> <br />
							<hr> * 教師權限應有：#2, #3, #4, #5<br></td>
					</tr>
					<tr>
						<td>使用者角色</td>
						<td><c:forEach var="role"
								items="${sessionScope.onlineUser.editableROLEs}">
								<input name="role" type="radio" role="${user.role}"
									value="${role}" /> ${role}<br />
							</c:forEach></td>
					</tr>
				</table>


				<input name="userid" type="hidden" value="${user.id}" /> <input
					name="account" type="hidden" value="${user.account}" />
				<button class="btn btn-success btn-lg col-md-12" id="save">儲存</button>

			</form>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
