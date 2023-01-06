<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />


<!-- <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
 -->
<link rel="stylesheet" href="jscripts/jquery-ui-1.7.2.custom.css">
<script type="text/javascript"
	src="jscripts/jquery-ui-1.7.3.custom.min.js"></script>


<link href="jscripts/DatetimePicker/jquery-ui-timepicker-addon.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="jscripts/DatetimePicker/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="EditVClassTemplate.js"></script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<h1>編輯【課程模板】</h1>
			<jsp:include page="include/div/SystemTimeNow.jsp" />
			<form class="form-horizontal">
				<hr />
				<div class="form-group">
					<!-- 
					<label for="tname" class="col-sm-1 control-label">使否公開?</label>
					<div class="col-sm-1">
						<input type="text" class="form-control" id="tname"
							name="templateVisible" placeholder="是否公開?"
							value="${vclassTemplates[0].visible}"
							title="公開則可供其他人開課使用，不公開則供自己開課使用">
					</div>
				 -->
					<label for="tname" class="col-sm-2 control-label">課程模板名稱</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="tname"
							name="templateName" placeholder="模板名稱"
							value="${vclassTemplates[0].name}" style="font-size: x-large;">
					</div>
					<input type="hidden" name="id" value="${vclassTemplates[0].id}">
				</div>

				<!-- 				<button type="button" class="btn btn-primary"
					id="addNewTemplateContest">新增一個【<fmt:message key="Contest.VContest" />】</button>
 -->
				<div class="form-group">
					<label for="tname" class="col-sm-1 control-label">編號</label>
					<div class="col-sm-3">
						<label for="tcontent" class="control-label">標題</label>
					</div>
					<div class="col-sm-3">
						<label for="tcontent" class="control-label">課程進度說明</label>
					</div>
					<div class="col-sm-4">
						<label for="tcontent" class="control-label">請輸入題目編號以逗點隔開，如:
							[a001, a002]</label>
					</div>
					<div class="col-sm-1">
						<label for="tcontent" class="control-label">操作</label>
					</div>
				</div>
				<hr>
				<div id="sortable">
					<c:forEach var="vclassTemplate" items="${vclassTemplates}"
						varStatus="status">
						<div id="TemplateContestForm">
							<div class="form-group">
								<label for="tname" class="col-sm-1 control-label"><span
									class="count">${status.count}.</span>
								</label>
								<div class="col-sm-3">
									<input id="TemplateContestTitle" class="form-control"
										name="title" placeholder="請輸入課堂標題"
										value="${vclassTemplate.title}" />
								</div>
								<!-- 							<label for="tcontent" class="col-sm-1 control-label">進度說明：</label>
 -->
								<div class="col-sm-3">
									<textarea id="TemplateContestDescript" rows="3"
										class="form-control" name="descript" placeholder="請輸入課程進度說明">${vclassTemplate.descript}</textarea>
								</div>
								<!-- 						<label for="tname" class="col-sm-1 control-label">課堂練習題：</label>
	 -->
								<div class="col-sm-4">
									<input type="text" class="form-control" id="tname"
										name="TemplateContestProblemids"
										placeholder="請輸入題目編號以逗點隔開，如: a001, a002"
										value="${vclassTemplate.problemids}">
									<div id="contest_problems">請輸入題目編號以逗點隔開，如: a001, a002</div>
								</div>
								<div class="col-sm-1">
									<button class="btn btn-default" id="deleteTemplateContest">刪除</button>
								</div>
							</div>
							<hr>
						</div>
					</c:forEach>
				</div>

				<button type="button" class="btn btn-primary"
					id="addNewTemplateContest">
					新增一個【
					<fmt:message key="Contest.VContest" />
					】
				</button>
				<label class="control-label" for="inputSuccess1">拖曳可調整順序。</label>
				<hr>
				<button class="btn btn-success btn-lg col-md-12" id="save">儲存課程模板</button>
			</form>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
