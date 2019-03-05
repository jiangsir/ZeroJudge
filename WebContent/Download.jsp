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
</head>
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<p>&nbsp;</p>
		<div class="contestbox">
			<div align="center">
				<p>系統下載說明：</p>
			</div>
			<p style="padding: 0px 10px 0px 10px; font-size: small">系統採自由下載。</p>
			<form id="form1" name="form1" method="post" action="Application">
				<div align="center">
					<input type="submit" class="button" name="Submit" value="確定申請" />
				</div>
			</form>
			<br />
		</div>
		<p>
			<br />
		</p>
	</div>
	<br />
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
