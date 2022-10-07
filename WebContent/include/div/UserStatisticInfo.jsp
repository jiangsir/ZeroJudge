<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<%--
<c:if test="${user.isAuthhost_Google }">
	<img src="./ShowImage?userid=${user.id}" class="img-responsive ">
</c:if>
<br />
 --%>
<c:set var="user" value="${user}" scope="request" />
<h4>
	<jsp:include page="UserAccount_TypeB.jsp" />
</h4>
<fmt:message key="User.School" />
:
<c:choose>
	<c:when test="${user.schoolid==0}">
		<fmt:message key="User.NotStudent" />
	</c:when>
	<c:otherwise>${fn:escapeXml(user.schoolname)}

	</c:otherwise>
</c:choose>
<br>
<fmt:message key="User.ID" />
: ${user.id}

<button type="button" class="btn btn-default btn-xs"
	data-target="#Modal_SendIMessage" data-receiver="${user.account }">
	<i class="fa fa-envelope" aria-hidden="true"></i>
</button>
<jsp:include page="../Modals/Modal_SendIMessage.jsp" />
<br />
<fmt:message key="User.IPaddress" />
: ${user.ipset}
<br />
<fmt:message key="User.LastLogin" />
：
<br />
<fmt:formatDate value="${user.lastlogin}" pattern="yyyy-MM-dd HH:mm:ss" />
<br />
