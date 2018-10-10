<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<table width="100%" align="center">
			<tr>
				<td>ID</td>
				<td>Account</td>
				<td>ipfrom</td>
				<td>ipinfo</td>
				<td>message</td>
				<td>Logintime</td>
				<td>Logout</td>
				<td>Staymin</td>
				<td>操作</td>
			</tr>
			<c:forEach var="list" items="${loginlog}">
				<tr>
					<td>${list.id }</td>
					<td><a href="./UserStatistic?id=${list.userid}"
						title="${list.username}">${list.useraccount}</a></td>
					<td>${list.ipfrom}</td>
					<td>${list.ipinfo}</td>
					<td>${list.message}</td>
					<td><fmt:formatDate value="${list.logintime}"
							pattern="yyyy-MM-dd HH:mm" /></td>
					<td><fmt:formatDate value="${list.logouttime}"
							pattern="yyyy-MM-dd HH:mm" /></td>
					<td><div align="right">
							<c:if test="${list.logouttime.time>list.logintime.time}">
								<fmt:formatNumber
									value="${(list.logouttime.time - list.logintime.time)/60000}"
									pattern="##,###" />分鐘</c:if>
						</div></td>
					<td></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
