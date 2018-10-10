<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
		<br />
		<div class="contestbox">
			<div align="center">
				<br /> 參加競賽注意事項：
			</div>
			<ul>
				<li>進行競賽時，請勿與組員以外的人員交談</li>
				<li>當您決定參加一個競賽時，您的帳號權限就會有部分限縮，如：無法觀看程式碼、無法解鎖程式碼、無法使用討論區、無法傳送訊息、無法解該競賽以外的題目...等。</li>
				<li>當決定參加競賽後，您就無法自行離開(恢復正常權限)，必須由主辦人處理或等競賽結束。</li>
			</ul>
			<form id="form1" name="form1" method="post"
				action="JoinContest?contestid=${param.contestid}">
				<div align="center">
					<input type="submit" class="button" name="Submit" value="確定參加" />
				</div>
			</form>
			<br />
		</div>
		<br /> <br />
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
