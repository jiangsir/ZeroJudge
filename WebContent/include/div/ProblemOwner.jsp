<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>


<c:if test="${!problem.owner.nullUser && !problem.owner.isDEBUGGER}">
                [管理者：<c:set var="user" value="${problem.owner}"
		scope="request" />
	<jsp:include page="UserAccount_TypeA.jsp" />]
</c:if>
