<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="include/CommonHead.jsp" />
<!-- <script type="text/javascript" src="./jscripts/jquery.timeout.interval.idle.js"></script>
<script type="text/javascript" src="./jscripts/jquery.blockUI.js"></script>
<script type="text/javascript" src="./jscripts/jquery.query-2.1.7.js"></script>
 -->
<script type="text/javascript" src="TestRunning.js"></script>
<script language="JavaScript">
	jQuery(document).ready(function() {
		jQuery("span[id='logfile']").click(function() {
			jQuery.ajax({
				type : "GET",
				url : "./Debugger.api",
				data : "action=getCatalina&logfile=" + $(this).text() + "&line=1000",
				async : false,
				timeout : 5000,
				success : function(result) {
					jQuery("#outdata").html(result)
				}
			});

		});
	});
</script>
</head>
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<p>
			<br />
		</p>
		<div style="">
			<div style="float: left; text-align: left; width: 20%;">
				log 資料夾：${logpath}<br /> <br /> 內含檔案：<br />
				<c:forEach var="logfile" items="${logfiles}" varStatus="varstatus">
					<span id="logfile">${logfile.key}</span>
					<br />
				</c:forEach>
			</div>
			<div style="float: right; width: 80%">
				<textarea name="outdata" cols="100%" rows="50" id="outdata"></textarea>
			</div>
		</div>
		<div style="clear: both; padding: 20px;"></div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
