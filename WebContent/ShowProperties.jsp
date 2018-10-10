<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ShowProperties</title>
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<p>以下為自訂的 properties.xml</p>
	<table>
		<tr>
			<td>Property Name</td>
			<td>Property Value</td>
		</tr>
		<c:forEach var="prop" items="${list1}">
			<tr>
				<c:forEach var="map" items="${prop}">
					<td>${map.key}</td>
					<td>${map.value}</td>
				</c:forEach>
			</tr>
		</c:forEach>

	</table>
	<p>以下為 System.getProperties()</p>
	<table>
		<tr>
			<td>Property Name</td>
			<td>Property Value</td>
		</tr>
		<c:forEach var="prop" items="${list2}">
			<tr>
				<c:forEach var="map" items="${prop}">
					<td>${map.key}</td>
					<td>${map.value}</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
