<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("span[id='ShowStacktrace'] a").click(function() {
			jQuery("#stacktrace").val($(this).attr("stacktrace"));
			var $dialog = $("#ShowStacktrace_dialog").dialog({
				autoOpen : false,
				width : '80%',
				title : 'Stack Trace',
				buttons : {
					"返回" : function() {
						$(this).dialog("close");
					}
				}
			});
			$dialog.dialog('open');
			return false;
		});
	});
</script>

</head>

<fmt:setBundle basename="resource" />
<c:set var="tab" value="${tab}" />
<body id="${tab}">
	<div id="ShowStacktrace_dialog" alt=""
		style="cursor: default; padding: 10px; display: none;">
		<span>Stacktrace</span><br />
		<textarea name="stacktrace" cols="100%" rows="20" id="stacktrace"></textarea>
	</div>

	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<form name="form" method="post" action=""
				style="margin: 0px; display: inline;" onsubmit="checkForm(this);">
				全文檢索： <input name="searchword" type="text" value="" size="10" />
			</form>
			<hr />
			<ul class="nav nav-tabs" role="tablist">
				<c:forEach var="tab" items="${tabs}" varStatus="varstatus">
					<li role="presentation"><a href="?tabid=${tab}"
						aria-controls="tab0${varstatus.count}" role="tab"
						data-toggle="tab">${tab }</a></li>
				</c:forEach>
			</ul>

			<%-- 			<ul id="tabmenu">
				<c:forEach var="tab" items="${tabs}" varStatus="varstatus">
					<li class="tab0${varstatus.count}"><a href="?tabid=${tab}"
						title="${tab}">${tab}</a></li>
				</c:forEach>
			</ul>
 --%>
			<table class="table table-hover">
				<tr>
					<td>ID</td>
					<td width="10%">session_account: uri</td>
					<td>title, message</td>
					<td>timestamp</td>
					<td>操作</td>
				</tr>
				<c:forEach var="log" items="${logs}">
					<tr>
						<td>${log.id }</td>
						<td width="20%">${log.session_account}at${log.ipaddr}<br />
							${log.method}: <a href="${log.uri}">${log.uri}</a></td>
						<td>Title: ${log.title}<br /> Message: ${log.message}<br />
							<span id="ShowStacktrace"><a href="#"
								stacktrace="${fn:escapeXml(log.stacktrace)}">詳情</a></span></td>
						<td><fmt:formatDate value="${log.timestamp}"
								pattern="yyyy-MM-dd HH:mm" /></td>
						<td></td>
					</tr>
				</c:forEach>
			</table>
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
