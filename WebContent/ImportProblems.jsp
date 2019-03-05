<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<script type="text/javascript"
	src="ImportProblems.js?${applicationScope.built }"></script>
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="content_individual">
		<div class="container">
			<div class="row">
				<div class="alert alert-warning">
					<h1>匯入題目須知，第一次匯入者請務必閱讀：</h1>
					<ul>
						<li>為避免題目編號衝突，匯入的題目編號不會與原題目編號相同。而是由新系統自動選定。</li>
						<li>進行匯入的使用者將會成為所匯入題目的管理者，不論原來題目管理者為何。</li>
						<li>匯入的題目將失去前測結果，應在新系統當中重新進行前測。</li>
						<li>匯入後題目預設為隱藏並且自動歸類在本系統的第一個題目分類當中，事後再進行分類調整。</li>
					</ul>
				</div>
				<form action="" method="post" enctype="multipart/form-data"
					name="form1" id="form1">
					<div>
						匯入題目： <input type="file" multiple="multiple" name="importproblems" />
						(可多選)
					</div>
					<div>請注意：單一檔案上傳上限為 ${maxFileSize/1024/1024}MB, 總檔案上傳上限為
						${maxRequestSize/1024/1024 }MB</div>
				</form>
				<button id="submit">開始上傳</button>
			</div>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
