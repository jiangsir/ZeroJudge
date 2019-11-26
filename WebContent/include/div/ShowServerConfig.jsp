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
		<p>目前連結的評分主機為：${applicationScope.appConfig.serverConfig.servername}</p>
		<p>評分主機資訊：${applicationScope.appConfig.serverConfig.serverInfo}</p>
		<p>本系統目前支援的程式語言如下：</p>
		<c:forEach var="compiler"
			items="${applicationScope.appConfig.serverConfig.enableCompilers}">
			<span style="font-weight: bold; font-size: large">${compiler.language}</span>: ${compiler.version}<br />
			<div>${compiler.language}解題範例</div>
			<textarea name="textarea" cols="70" rows="10">${compiler.samplecode}</textarea>
			<br></br>
		</c:forEach>
	</c:if>
</div>
