<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<div id="ShowServerConfig" style="text-align: left;">
	<jsp:include page="ServerConfigCheck.jsp" />
	<c:if test="${!applicationScope.appConfig.serverConfig.isNull}">
		<hr>
		<div>本系統目前支援的程式語言如下：</div>
		<c:forEach var="compiler"
			items="${applicationScope.appConfig.serverConfig.enableCompilers}">
			<button type="button" class="btn btn-default">${compiler.language}</button>
		</c:forEach>
	</c:if>
</div>
