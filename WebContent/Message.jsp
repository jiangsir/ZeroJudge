<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
<script type="text/javascript"
	src="jscripts/jquery.timeout.interval.idle.js"></script>
<script language="javascript">
	jQuery(document).ready(function() {
		jQuery(document).keydown(function(e) {
			if (e == null) {
				keycode = event.keyCode;
			} else {
				keycode = e.which;
			}
			if (keycode == 116) {
				alert("請勿使用 F5 重新發送!!");
				return false;
			}
		});
	});
</script>
</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">

		<c:choose>
			<c:when test="${log!=null}">
				<table width="50%" border="0" align="center">
					<tr>
						<td scope="col"><img src="images/${log.tabid}.png" width="64"
							height="64" /></td>
						<td scope="col"
							style="vertical-align: middle; text-align: center;">
							<h1 style="padding-top: 10px;">
								<c:if test="${log.message!=''}">${log.message}</c:if>
							</h1> <br /> <c:if test="${sessionScope.onlineUser.isDEBUGGER}">
								<div class="DEBUGGEROnly" style="padding-left: 20px;">${log.stacktrace}</div>
							</c:if> <br /> <br /> <a href="javascript:history.back();">上一頁</a>
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table width="50%" border="0" align="center">
					<tr>
						<td scope="col"><img src="images/${message.type}.png"
							width="64" height="64" /></td>
						<td scope="col"
							style="vertical-align: middle; text-align: center;"><h1
								style="padding-top: 10px;">
								<c:if test="${message.plainTitle!=''}">${message.plainTitle}</c:if>
								<c:forEach var="title" items="${message.resourceTitle}"
									varStatus="varstatus">
									<fmt:message key="${fn:trim(title)}">
										<c:forEach var="paramitem" items="${message.resourceParam}"
											varStatus="paramcount">
											<fmt:param value="${paramitem}" />
										</c:forEach>
									</fmt:message>
									<br />
								</c:forEach>
							</h1> <br /> <c:forEach var="resource"
								items="${message.resourceMessage}" varStatus="varstatus">
								<fmt:message key="${fn:trim(resource)}">
									<c:forEach var="paramitem" items="${message.resourceParam}"
										varStatus="paramcount">
										<fmt:param value="${paramitem}" />
									</c:forEach>
								</fmt:message>
								<br />
							</c:forEach> <c:if test="${message.plainMessage!=''}">
								<p style="text-align: left;">${message.plainMessage}</p>
							</c:if> <c:if test="${sessionScope.onlineUser.isDEBUGGER}">
								<div class="DEBUGGEROnly">debug: ${message.debug}</div>
							</c:if>
							<p>&nbsp;</p>
							<p></p> <c:forEach var="link" items="${message.links}"
								varStatus="varstatus">
								<c:if test="${varstatus.count!=1}"> | </c:if>
								<a href="${link.key}">${link.value}</a>
							</c:forEach> <br /></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>
		<p>&nbsp;</p>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
