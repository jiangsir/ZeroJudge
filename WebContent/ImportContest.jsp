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
				<form action="" method="post" enctype="multipart/form-data" name="form1" class="form-horizontal">
					<!-- <div class="form-group row">
										<label class="col-sm-2 col-form-label">匯入競賽
											targetVclassid:</label>
										<div class="col-sm-2">
											<input type="text" class="form-control" name="targetVclassid" placeholder="vclassid"
												value="${param.targetVclassid}">
										</div>
									</div> -->
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">匯入競賽進入隨堂測驗 id：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="targetVclassid" placeholder="vclassid"
								value="${empty param.targetVclassid?'0':param.targetVclassid}">
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">參數：</label>
						<div class="col-sm-9">
							<div class="btn-group" data-toggle="buttons">
								[ <input name="importContest_config1" type="radio" value="true" checked /> 是 <input
									name="importContest_config1" type="radio" value="false" /> 否] #1 匯入競賽時，是否在本地建立新題目?
									<small>選擇 "否" ，請必須確定系統內確實有這個題目再行匯入</small>
							</div>
							<div class="btn-group" data-toggle="buttons">
								[ <input name="importContest_config2" type="radio" value="true" checked /> 是 <input
									name="importContest_config2" type="radio" value="false" /> 否] #2 匯入競賽時，是否包含"參與者"與"解題紀錄"? ()
									<small>選擇 "是"，在系統上會建立新的使用者，並且匯入解題紀錄。</small>
									<small>選擇 "否"，不含使用者與解題紀錄，算是競賽轉移。</small>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">匯入競賽：</label>
						<div class="col-sm-9">
							<input type="file" multiple="multiple" class="form-control-file" name="importContest_JsonFile" /><small
								id="passwordHelpBlock" class="form-text text-muted">
								(可多選) 請注意：單一檔案上傳上限為
								${maxFileSize/1024/1024}MB, 總檔案上傳上限為 ${maxRequestSize/1024/1024 }MB
							</small>
						</div>
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
