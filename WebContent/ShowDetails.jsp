<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<a
		href="./SendIMessage?account=${author}&subject=appeals&solutionid=${param.solutionid}">向出題者申訴!!</a>
	<br />
	<c:if
		test="${sessionScope.onlineUser.isDEBUGGER && problem.wa_visible==0}">
		<div class="DEBUGGEROnly">本題目並未開放使用者觀看錯誤訊息。</div>
	</c:if>
	<br />
	<textarea cols="100" rows="20" readonly="readonly">${errmsg}</textarea>
	<br />
	<br />
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
