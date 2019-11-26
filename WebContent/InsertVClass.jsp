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
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<form name="form1" method="post" action="">
		<div class="content_individual">
			編修課程資訊<br /> <br />
			<table align="center">
				<tr>
					<td width="112">課程名稱：</td>
					<td width="351"><input name="vclassname" type="text"
						id="vclassname" value="${vclass.vclassname}" size="50"
						maxlength="50" /></td>
				</tr>
				<tr>
					<td>課程說明：</td>
					<td><textarea name="descript" cols="40" rows="5" id="descript">${vclass.descript}</textarea></td>
				</tr>
			</table>
			<p>
				<input name="vclassid" type="hidden" value="${vclass.id}" />
				<input type="button" class="button" value=" 確認送出" />
			</p>
			<p>&nbsp;</p>
		</div>
	</form>
	<p></p>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
