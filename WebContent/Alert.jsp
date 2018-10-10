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
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<fieldset>
				<legend >${alert.type}</legend>
				<h1>${alert.title}</h1>
				<p></p>
				<h2>${alert.subtitle }</h2>
				<div>${alert.content}</div>
				<ul>
					<c:forEach var="list" items="${alert.list}">
						<li>${list}</li>
					</c:forEach>
				</ul>
				<ul>
					<c:forEach var="map" items="${alert.map}">
						<li>${map.key}:${map.value}</li>
					</c:forEach>
				</ul>
				<hr style="margin-top: 3em;" />
				<div style="text-align: center;">
					<c:forEach var="uri" items="${alert.uris}">
						<a href="${uri.value}" class="btn btn-primary">${uri.key}</a>
					</c:forEach>
				</div>
			</fieldset>
		</div>

		<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
			<fieldset style="text-align: left; background-color: maroon;">
				<legend>Debug: </legend>
				<ul>
					<c:forEach var="debug" items="${alert.debugs}">
						<li>${debug}</li>
					</c:forEach>
				</ul>
				<div>
					<c:if test="${fn:length(alert.stacktrace)>0}">
						<div style="text-align: left; margin-top: 1em;">
							<h3>stacktrace:</h3>
							<div style="font-family: monospace;">
								<c:forEach var="stacktrace" items="${alert.stacktrace}">${stacktrace.className}.${stacktrace.methodName}(${stacktrace.fileName}:${stacktrace.lineNumber})<br />
								</c:forEach>
							</div>
						</div>
					</c:if>
				</div>
			</fieldset>
		</c:if>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
