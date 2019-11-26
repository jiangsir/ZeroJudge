<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="solution" uri="http://jiangsir.tw/jstl/solution"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="showdetail_dialog"
	style="cursor: default; padding: 10px; display: none; text-align: left">
	<span id="spinner" style="display: none;"> 可能會有數個 submission
		在排隊進行，請耐心等候。<br /> <br /> <img src="images/Spinner.gif">
	</span>

	<div id="serverOutputs" style="text-align: left;"></div>
</div>
