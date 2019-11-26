<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="ExportProblems_dialog"
	style="display: none; cursor: default; padding: 20px; margin: auto; text-align: left;">
	請選擇要匯出的題目編號：
	<div id="problems"></div>
	
	</div>
