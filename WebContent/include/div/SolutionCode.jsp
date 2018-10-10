<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="solution" uri="http://jiangsir.tw/jstl/solution"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<jsp:include page="../Modals/Modal_ShowSolutionCode.jsp" />

<div class="SolutionCode" data-solutionid="${solution.id }">
	<c:choose>
		<c:when
			test="${sessionScope.onlineUser.isShowCodeAccessible(solution)}">
			<jsp:include page="../Modals/Modal_ShowSolutionCode.jsp">
				<jsp:param value="Code ${solution.id} xxxxx" name="title" />
				<jsp:param value="${solution.code}" name="code" />
			</jsp:include>

			<%-- 			<a href="#" class="showcode" data-toggle="modal"
				data-target="#Modal_ShowSolutionCode_${solution.id }">${solution.language}</a>
 --%>
			<button type="button" class="btn btn-default btn-xs"
				data-toggle="modal"
				data-target="#Modal_ShowSolutionCode_${solution.id}" title="程式碼"
				data-solutionid="${solution.id}" id="btn_SolutionCode">${solution.language}</button>
		</c:when>
		<c:otherwise>
			<button type="button" class="btn btn-default btn-xs" title="程式碼">${solution.language}</button>
		</c:otherwise>
	</c:choose>
	<%-- 	<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
		<span class="alert-danger"><a
			href="./InsertTestcode?solutionid=${solution.id}"><img
				src="images/save.svg" style="height: 1.2em;" border="0" /></a></span>
	</c:if>
 --%>
</div>
