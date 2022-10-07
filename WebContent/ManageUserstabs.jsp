<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<div align="center">
			User 標籤定義 逗號之前為 tab 順序，逗號之後為標籤說明文字<br />
			<form id="form1" name="form1" method="post" action="ManageUserstabs">
				<p>
					<c:forEach var="usertab" items="${usertabs}" varStatus="status">
	        ${usertab.tab} 改為-> <input name="olduserstabs" type="hidden"
							value="${usertab.tab}" />
						<input name="newuserstabs" type="text" value="${usertab.tab}" />
						<br />
					</c:forEach>
				</p>
				<p>
					<input name="ReturnPage" type="hidden"
						value="ManageUserstabs?${pageContext.request.queryString}" /> <input
						type="submit" class="button" name="Submit" value="送出" />
				</p>
			</form>
			<p>&nbsp;</p>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
