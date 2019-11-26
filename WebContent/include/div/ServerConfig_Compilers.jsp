<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<div id="ServerConfig_Compilers" style="text-align: left;">
	<c:choose>
		<c:when
			test="${fn:length(applicationScope.appConfig.serverConfig.enableCompilers)>0}">
			<div style="display: block; clear: both;">
				<fmt:message key="Problem.ProgramLanguage" />
				：<br />
				<c:forEach var="compiler"
					items="${applicationScope.appConfig.serverConfig.enableCompilers}">
					<input name="language" type="radio" value="${compiler.language}"
						userlanguage="${sessionScope.onlineUser.userlanguage}" />
					<span style="font-weight: bold; font-size: large">${compiler.language}</span>: ${compiler.version}<br />
				</c:forEach>
			</div>
		</c:when>
		<c:otherwise>目前無法取得評分機器的資訊，請通知管理員。<br />
		</c:otherwise>
	</c:choose>
</div>
