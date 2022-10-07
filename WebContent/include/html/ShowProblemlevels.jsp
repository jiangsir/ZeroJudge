<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<c:choose>
	<c:when test="${fn:length(problemlevels)>0}">
		<c:forEach var="problemlevel" items="${problemlevels}"
			varStatus="varstatus">
			<c:set var="user" value="${problemlevel.user}" scope="request" />
			<tr>
				<td><jsp:include page="../div/UserAccount_TypeA.jsp" /></td>
				<td>${problemlevel.levelname}${problemlevel.level}級</td>
				<td>${problemlevel.updatetime}</td>
			</tr>
		</c:forEach>
	</c:when>
	<c:otherwise>
	沒有本題目分級紀錄。
	</c:otherwise>
</c:choose>
