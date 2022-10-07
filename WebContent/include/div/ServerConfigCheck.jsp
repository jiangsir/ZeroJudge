<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<c:choose>
	<c:when test="${applicationScope.appConfig.serverConfig.isNull }">
		<div class="alert alert-danger" role="alert">
			<h3>裁判機無法正確讀取！</h3>
			<div>無法取得裁判機資訊！請與管理員聯繫設定正確裁判機資訊或檢查裁判機是否正常運作。</div>
		</div>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>
