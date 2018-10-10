<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<c:if
	test="${(sessionScope.onlineUser.isHigherEqualThanMANAGER)}">
	<div class="btn-group btn-group-sm" role="group" aria-label="...">
		<button type="button" class="btn btn-default"
			id="rebuiltUserStatistic" data-userid="${user.id }"
			title="重新計算 user 統計資訊">
			<span class="glyphicon glyphicon-refresh"></span>
		</button>
		<a class="btn btn-default"
			href="./EditUserConfig?userid=${user.id}" role="button"
			title="管理使用者參數"><span class="glyphicon glyphicon-pencil"></span></a>
		<a class="btn btn-default" href="./Problems?ownerid=${user.id }"
			role="button" title="出題列表"><span
			class="glyphicon glyphicon-tasks"></span></a>
<!-- 			<a class="btn btn-default"
			href="#" role="button" title="寄送新密碼">寄送新密碼</a>
 -->	</div>
</c:if>
