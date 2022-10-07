<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
	<script type="text/javascript" src="ImportContest.js?${applicationScope.built}"></script>
</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="content_individual">
		<div class="container">
			<div class="row">
				<div class="alert alert-warning">
					<h1>匯入「競賽」須知，第一次匯入者請務必閱讀：</h1>
					<ul>
						<li>匯出的競賽包含競賽本身、競賽題目、參與者</li>
						<li>匯入之後會建立新的競賽，新的題目，新的參與者。與原來的題目編號不同，與原來的 user 也不同。</li>
						<li>本功能產生的競賽無法讓使用者繼續使用。只能作為轉移資料留底使用。</li>
					</ul>
				</div>
				<form action="" method="post" enctype="multipart/form-data" name="form1" id="form1">

					<div class="form-group row">
						<label class="col-sm-2 col-form-label">匯入競賽
							targetVclassid:</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" name="targetVclassid" placeholder="vclassid"
								value="${param.targetVclassid}">
						</div>
					</div>

					<div class="form-group">
						<label for="exampleFormControlFile1">匯入競賽： </label> <input type="file" multiple="multiple"
							class="form-control-file" name="importContest" /><small id="passwordHelpBlock"
							class="form-text text-muted"> (可多選) 請注意：單一檔案上傳上限為
							${maxFileSize/1024/1024}MB, 總檔案上傳上限為 ${maxRequestSize/1024/1024 }MB
						</small>
					</div>
					<hr>
					<button type="submit" class="btn btn-success btn-lg col-md-12">開始上傳</button>
				</form>
				<!-- 				<button id="submit">開始上傳</button>
	-->
			</div>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>

</html>
