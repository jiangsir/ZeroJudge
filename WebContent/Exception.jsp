<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8" />
<title><fmt:message key="ErrorPage.ErrorPage" /></title>
<jsp:include page="include/CommonHead.jsp" />
</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<body>
	<fmt:message key="ErrorPage.StatusCode" />
	：${requestScope.javax.servlet.error.status_code}
	<br />
	<fmt:message key="ErrorPage.Message" />
	：${requestScope["javax.servlet.error.message"]}
	<br />
	<fmt:message key="ErrorPage.Exception" />
	：${requestScope.javax.servlet.error.exception_type}
	<br />
</body>
</html>
