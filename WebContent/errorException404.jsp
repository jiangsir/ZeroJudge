<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<div style="font-size: large">
			糟糕囉！網頁發生錯誤${pageContext.errorData.statusCode}<br /> 請通知管理員！謝謝！<a
				href="mailto:${applicationScope.appConfig.systemMail}">${applicationScope.appConfig.systemMail}</a>
		</div>
		<p>
			Remote Address: ${pageContext.request.remoteAddr}<br />
			${pageContext.exception}<br /> Request that failed:
			${pageContext.errorData.requestURI}
			<c:if test="${pageContext.request.queryString!=null}">?${pageContext.request.queryString}</c:if>
			<br /> Status code: ${pageContext.errorData.statusCode}<br />
			Exception: ${pageContext.errorData.throwable}<br /> <br /> <br />
		</p>
		<div style="font-size: large">顯示例外堆疊追蹤：</div>
		<div
			style="text-align: left; width: 70%; font-size: 12px; padding: 10px; margin: auto">
			<c:forEach var="trace" items="${pageContext.exception.stackTrace}">
${trace}<br />
			</c:forEach>
		</div>

	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
