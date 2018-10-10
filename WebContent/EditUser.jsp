<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
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
	src="EditUser.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="./jscripts/jquery.timeout.interval.idle.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("#otherschool").focus(function() {
			$("#otherschoolradio").attr("checked", true);
		});
		jQuery("#schoolurl").focus(function() {
			$("#otherschoolradio").attr("checked", true);
		});

	}); // jQuery ready

	function CheckRadio(form) {
		for (i = 0; i < form.schoolid.length; i++) {
			if (form.schoolid[i].checked) {
				return true;
			}
		}
		return false;
	}
</script>
</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<c:if test="${applicationScope.appConfig.isGoogleLoginSetup}">
		<a href="./InsertGoogleUser" class="btn btn-primary btn-lg btn-block">
			以現有的 Google 帳號創建身份</a>
		<hr>
	</c:if>
	<div class="container">
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-3 control-label"><fmt:message
								key="Register.Account" /></label>
						<div class="col-sm-9">
							<c:choose>
								<c:when test="${user.id==null || user.id==0}">
									<input class="form-control" name="account"
										placeholder="account" value="${user.account}" />
									<%-- 								<input name="account" type="text" id="account"
									value="${user.account}" size="15" maxlength="20" />
 --%>
									<!--                                <span id="ajaxwaiting" style="display: none"><img
                                    src="images/Spinner.gif" /></span>
                                <span id="CheckAccount" style="color: red; font-size: 12px"></span>
 -->
								</c:when>
								<c:otherwise>
									<input class="form-control" name="account" type="text"
										placeholder="account" value="${user.account}" readonly />
									<p class="help-block">
										<c:if
											test="${sessionScope.isOnlineUser && !sessionScope.onlineUser.isAuthhost_localhost}">本帳號已綁定 ${sessionScope.onlineUser.email }</c:if>
									</p>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-3 control-label">密碼：</label>
						<div class="col-sm-9">
							<input type="password" class="form-control"
								placeholder="Password" name="passwd" value="${user.passwd}">
<%-- 								<c:if test="${sessionScope.isOnlineUser && !sessionScope.onlineUser.isAuthhost_localhost}">readonly</c:if>>
							<p class="help-block">
								<c:if
									test="${sessionScope.isOnlineUser && !sessionScope.onlineUser.isAuthhost_localhost}">本帳號已綁定 ${sessionScope.onlineUser.email}</c:if>
							</p>
 --%>						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-3 control-label">確認密碼：</label>
						<div class="col-sm-9">
							<input type="password" class="form-control"
								placeholder="Password2" name="passwd2" value="${user.passwd}">
<%-- 								<c:if test="${sessionScope.isOnlineUser && !sessionScope.onlineUser.isAuthhost_localhost}">readonly</c:if>>
							<p class="help-block">
								<c:if
									test="${sessionScope.isOnlineUser && !sessionScope.onlineUser.isAuthhost_localhost}">本帳號已綁定 ${sessionScope.onlineUser.email }</c:if>
							</p>
 --%>						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-3 control-label"><fmt:message
								key="Register.Username" />：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" placeholder="username"
								name="username" type="text"
								value="${fn:escapeXml(user.username)}">
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-3 control-label"><fmt:message
								key="Register.Truename" />：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" placeholder="真實姓名"
								name="truename" type="text"
								value="${fn:escapeXml(user.truename)}">
							<p class="help-block">真實姓名不公開</p>
						</div>
					</div>

					<div class="form-group">
						<label for="inputPassword3" class="col-sm-3 control-label"><fmt:message
								key="Register.BirthYear" />：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" placeholder="出生年份"
								name="birthyear" type="text"
								value="${fn:escapeXml(user.birthyear)}">
							<p class="help-block"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-3 control-label">email：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" placeholder="email"
								name="email" type="text" value="${fn:escapeXml(user.email)}"
								<c:if test="${sessionScope.isOnlineUser && !sessionScope.onlineUser.isAuthhost_localhost}">readonly</c:if>>

							<p class="help-block">
								<c:if
									test="${sessionScope.isOnlineUser && !sessionScope.onlineUser.isAuthhost_localhost}">本帳號已綁定 ${sessionScope.onlineUser.email }</c:if>
								<c:if
									test="${!sessionScope.isOnlineUser || sessionScope.onlineUser.isAuthhost_localhost}">請輸入 email</c:if>

							</p>
						</div>
					</div>

					<div class="form-group">
						<label for="inputPassword3" class="col-sm-3 control-label"><fmt:message
								key="Register.School" />：</label>
						<div class="col-sm-9">

							<fmt:message key="Register.SchoolDescript" />
							<br /> <br /> <input name="schoolid" type="radio" value="0"
								<c:if test="${user.schoolid==0}">checked="checked" </c:if> />
							<fmt:message key="User.NotStudent" />
							<br />
							<c:forEach var="school" items="${schools}">
								<c:if test="${school.id!=0}">
									<input name="schoolid" type="radio" value="${school.id}"
										<c:if test="${user.schoolid==school.id}">checked="checked" </c:if> /> ${fn:escapeXml(school.schoolname)}<br />
								</c:if>
							</c:forEach>
							<hr />
							<input name="schoolid" id="otherschoolradio" type="radio"
								value="-1" /> 請注意：學校不在上方列表當中時，才填寫以下學校資料。 <br /> 校名： <input
								name="otherschoolname" id="otherschoolname" type="text"
								size="30" maxlength="50" /><br /> 學校網頁：<input
								name="otherschoolurl" id="otherschoolurl" type="text" size="30"
								maxlength="50" />
						</div>
					</div>





					<div class="form-group">
						<%-- 					<div class="col-sm-offset-2 col-sm-10">
						<input type="hidden" name="token" value="${token}" />
						<button type="submit" class="btn btn-success btn-lg col-md-12">儲存</button>
					</div>
 --%>
						<input type="hidden" name="token" value="${token}" />
						<button type="submit" class="btn btn-success btn-lg col-md-12">儲存</button>
					</div>
				</form>
			</div>
			<div class="col-md-3"></div>

			<%-- 			<form id="form1" name="form1" method="post" action="">
				<table class="table table-hover">
					<tr>
						<th class="col-md-3"><fmt:message key="Register.Account" />
							：</th>
						<td class="col-md-9"><c:choose>
								<c:when test="${user.userid==null || user.userid==0}">
									<input name="account" type="text" id="account"
										value="${user.account}" size="15" maxlength="20" />
									<!-- 								<span id="ajaxwaiting" style="display: none"><img
									src="images/Spinner.gif" /></span>
								<span id="CheckAccount" style="color: red; font-size: 12px"></span>
 -->
								</c:when>
								<c:otherwise>
									<input name="account" type="text" id="account"
										value="${user.account}" size="15" maxlength="20" readonly />
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th><fmt:message key="Register.Password" /> ：</th>
						<td><input name="passwd" type="password" id="password"
							value="${user.passwd}" size="15" maxlength="30" /></td>
					</tr>
					<tr>
						<th><fmt:message key="Register.Password2" /> ：</th>
						<td><div align="left">
								<input name="passwd2" type="password" id="password2"
									value="${user.passwd}" size="15" maxlength="30" />
							</div></td>
					</tr>
					<tr>
						<th width="30%" scope="row">
							<div align="right">
								<fmt:message key="Register.Username" />
								：
							</div>
						</th>
						<td><div align="left">
								<input name="username" type="text" id="username"
									value="${fn:escapeXml(user.username)}" size="20" maxlength="50" />
							</div></td>
					</tr>
					<tr>
						<th width="30%" scope="row">
							<div align="right">
								<fmt:message key="Register.Truename" />
								：
							</div>
						</th>
						<td><div align="left">
								<input name="truename" type="text" id="truename"
									value="${fn:escapeXml(user.truename)}" size="20" maxlength="20" />
							</div></td>
					</tr>
					<tr>
						<th width="30%" scope="row">
							<div align="right">
								<fmt:message key="Register.BirthYear" />
								：
							</div>
						</th>
						<td><div align="left">
								<input name="birthyear" type="text" value="${user.birthyear}"
									size="5" maxlength="5" />
							</div></td>
					</tr>
					<tr>
						<th width="30%" scope="row"><div align="right">
								<fmt:message key="Register.Email" />
								：
							</div></th>
						<td><div align="left">
								<input name="email" type="text" id="email" value="${user.email}"
									size="40" maxlength="50" /> <br />
							</div></td>
					</tr>
					<tr>
						<th width="30%" valign="top" scope="row"><p>&nbsp;</p>
							<p></p>
							<div align="right">
								<fmt:message key="Register.School" />
								：
							</div></th>
						<td><div align="left">
								<fmt:message key="Register.SchoolDescript" />
								<br /> <br /> <input name="schoolid" type="radio" value="0"
									<c:if test="${user.schoolid==0}">checked="checked" </c:if> />
								<fmt:message key="User.NotStudent" />
								<br />
								<c:forEach var="school" items="${schools}">
									<c:if test="${school.id!=0}">
										<input name="schoolid" type="radio" value="${school.id}"
											<c:if test="${user.schoolid==school.id}">checked="checked" </c:if> /> ${fn:escapeXml(school.schoolname)}<br />
									</c:if>
								</c:forEach>
								<hr />
								<input name="schoolid" id="otherschoolradio" type="radio"
									value="-1" /> 請注意：學校不在上方列表當中時，才填寫以下學校資料。 <br /> 校名： <input
									name="otherschoolname" id="otherschoolname" type="text"
									size="30" maxlength="50" /><br /> 學校網頁：<input
									name="otherschoolurl" id="otherschoolurl" type="text" size="30"
									maxlength="50" />
							</div></td>
					</tr>
				</table>
				<br /> <input name="userid" type="hidden" value="${user.userid}" />
				<input name="authhost" type="hidden" value="${user.authhost}" /> <input
				name="comment" type="hidden" value="${fn:escapeXml(user.comment)}" />
			<input name="label" type="hidden" value="${user.label}" /> <input
				name="extraprivilege" type="hidden" value="${user.extraprivilege}" />
				<input type="hidden" name="token" value="${token}" />
				<div class="text-center">
					<button type="submit" class="btn btn-success">儲存</button>
				</div>
			</form>
 --%>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
