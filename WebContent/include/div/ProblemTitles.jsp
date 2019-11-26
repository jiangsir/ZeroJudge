<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<div>
	<c:forEach var="contestproblem" items="${problems}">
		<c:set var="problem" value="${contestproblem}" scope="request" />
		<jsp:include page="ProblemTitle.jsp" /><br />
	</c:forEach>
</div>

