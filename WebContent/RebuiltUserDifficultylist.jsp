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
		<p>重整 Difficultylist</p>
		<form id="form1" name="form1" method="post"
			action="RebuiltUserDifficultylist">
			<p align="center">
				<br /> 全部重整？ <input name="rebuiltAll" type="checkbox"
					id="rebuiltAll" value="yes" /> <br /> <br /> account: <input
					name="account" type="text" id="account" size="30" maxlength="30" />
			</p>
			<p>
				<input type="submit" class="button" name="Submit" value="確定" />
			</p>
		</form>
	</div>
</body>
</html>
