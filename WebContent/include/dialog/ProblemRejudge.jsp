<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div id="rejudge_dialog" problemid="${problem.problemid}"
	style="display: none; cursor: default; padding: 20px; margin: auto;">
	<h2>
		本動作會將本題目(${problem.problemid})的所有程式碼全部重測，共 <span id="submitnum"></span>
		筆<br /> <br /> 請問要繼續進行嗎？
	</h2>
	<br />
</div>
