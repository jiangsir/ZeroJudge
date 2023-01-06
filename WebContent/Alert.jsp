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
	<div class="container" id="alert">
		<div class="row">
			<div class="jumbotron">
				<h2 id="title" title="title">${alert.title}</h2>
				<pre id="subtitle" title="subtitle">${alert.subtitle}</pre>
				<hr>
				<c:if test="${alert.content!=''}">
					<div id="content" title="content">${alert.content}</div>
					<hr>
				</c:if>
				<ul>
					<c:forEach var="map" items="${alert.map}">
						<li>${map.key}:${map.value}</li>
					</c:forEach>
				</ul>
				<c:if test="${fn:length(alert.uris)>0}">
					<hr style="margin-top: 3em;" title="uris" />
					<div style="text-align: center;" title="uris">
						<c:forEach var="uri" items="${alert.uris}">
							<a href="${uri.value}" class="btn btn-primary btn-lg" role="button">${uri.key}</a>
						</c:forEach>
					</div>
				</c:if>
				<a href="javascript:history.back()" class="btn btn-primary btn-lg" role="button">回前頁</a>
				<!-- <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn more</a></p> -->
			</div>
		</div>
		<a class="btn btn-default pull-right" role="button" data-toggle="collapse" href="#collapseExample" aria-expanded="false"
			aria-controls="collapseExample">
			...
		</a>
		<div class="collapse" id="collapseExample">
			<div class="well">
				<c:forEach var="debug" items="${alert.getDebugs()}">
					<pre title="debug">${debug}</pre>
				</c:forEach>
				<hr>
				<c:forEach var="stacktrace" items="${alert.getStackTracelist()}">
					<pre title="stacktrace">${stacktrace}</pre>
				</c:forEach>
			</div>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>

</html>
