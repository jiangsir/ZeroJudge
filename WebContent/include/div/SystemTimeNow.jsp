<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<script type="text/javascript" src="jscripts/js_date.js"></script>

<script type="text/javascript">
	jQuery(document).ready(function() {
		//		mytime(parseInt(${now.time}));
	});

	function mytime(nowtime) {
		var nowdate = new Date();
		nowdate.setTime(nowtime);
		jQuery("span#nowdate").text(formatDate(nowdate, "y-MM-dd HH:mm:ss"));
		jQuery.interval(function() {
			var nowdate = new Date();
			nowtime = nowtime + 1000;
			// alert("nowtime="+nowtime);
			nowdate.setTime(nowtime);
			jQuery("span#nowdate")
					.text(formatDate(nowdate, "y-MM-dd HH:mm:ss"));
		}, 1000);
	}
</script>

<div style="text-align: right;">
	<%-- 	<fmt:message key="Contest.SystemTimeNow" />
	： <span id="nowdate"></span>

 --%>
	<fmt:message key="Contest.SystemTimeNow" />
	：
	<jsp:useBean id="now" class="java.util.Date" />

	<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />

</div>
