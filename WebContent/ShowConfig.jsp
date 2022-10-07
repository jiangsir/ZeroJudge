<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<table width="200" border="1" align="center">
		<tr>
			<th scope="col">ConfigID</th>
			<th scope="col">Name</th>
			<th scope="col">Value</th>
			<th scope="col">Comment</th>
			<th scope="col">&nbsp;</th>
		</tr>
		<c:forEach var="config" items="${list}">
			<tr>
				<th scope="row">${config.configid}</th>
				<td>${config.name}</td>
				<td>${config.value}</td>
				<td>${config.comment}</td>
				<td>&nbsp;</td>
			</tr>
		</c:forEach>
	</table>
	<p>&nbsp;</p>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
