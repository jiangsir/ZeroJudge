<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<script type="text/javascript">
	jQuery(document).ready(function() {
	});
</script>

<span id="TimeFormat_TypeA">
	<c:set var="runningtime" value="${param.mstime}" />
	<c:set var="secs"
		value="${(runningtime - (runningtime % 1000)) / 1000}" />
	<c:set var="mins" value="${(secs - (secs % 60)) / 60}" />
	<c:set var="hours" value="${(mins - (mins % 60)) / 60}" />
	<c:set var="days" value="${(hours - (hours % 24)) / 24}" />
	${days}天${hours%24}小時${mins%60}分鐘${secs%60}秒
</span>
