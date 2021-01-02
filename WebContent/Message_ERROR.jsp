<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<table style="width: 50%; text-align: center;">
	<tr>
		<td scope="col"><img src="images/ERROR.png" width="64"
			height="64"></td>
		<td scope="col" style="vertical-align: middle; text-align: center;">
			<c:forEach var="resource" items="${ResourceMessage}"
				varStatus="varstatus">
				<fmt:message key="${fn:trim(resource)}">
					<c:forEach var="paramitem" items="${ResourceMessage_param}"
						varStatus="paramcount">
						<fmt:param value="${paramitem}" />
					</c:forEach>
				</fmt:message>
				<br>
			</c:forEach> <c:if test="${PlainMessage!=null && PlainMessage!=''}">
				<p>${PlainMessage}</p>
			</c:if> <c:if test="${Message!=null && Message!=''}">
				<p>${Message}</p>
			</c:if> <c:if test="${Link!=null}">
				<p>&nbsp;</p>
        ${Link} </c:if> <c:if test="${Linkmap!=null}">
				<c:forEach var="map" items="${Linkmap}" varStatus="varstatus">
					<c:if test="${varstatus.count!=1}"> | </c:if>
					<a href="${map.key}"> <fmt:message key="${map.value}" />
					</a>
				</c:forEach>
			</c:if> <c:if test="${Links!=null}">
				<c:forEach var="link" items="${Links}" varStatus="varstatus">
					<c:if test="${varstatus.count!=1}"> | </c:if>
					<a href="${link[0]}">${link[1]}</a>
				</c:forEach>
			</c:if>
		</td>
	</tr>
</table>
<p>&nbsp;</p>
