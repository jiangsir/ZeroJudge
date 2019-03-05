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
	<div class="content_individual">
		<p>更動題目編號</p>
		<form id="form1" name="form1" method="post" action="UpdateProblemid">
			<p align="center">
				原本的題目編號為：<input name="oldid" type="text" value="${param.problemid}" />
				，要更改為： <input name="newid" type="text" id="newid" />
			</p>
			<p>
				<input type="submit" class="button" name="Submit" value="確定更改" />
			</p>
		</form>
	</div>
</body>
</html>
