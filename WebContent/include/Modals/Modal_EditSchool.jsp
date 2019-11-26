<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<script type="text/javascript">
	jQuery(document).ready(function() {
	});
</script>
<div class="modal fade" id="Modal_EditSchool_${param.schoolid }"
	data-focus-on="input:first" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">編輯學校資訊</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">學校名稱：</label>
						<div class="col-sm-9">
							<input id="schoolid" name="schoolid" type="hidden"
								value="${param.schoolid }" /> <input type="text"
								class="form-control" id="tname" name="schoolname"
								placeholder="請輸入學校名稱" value="${fn:escapeXml(school.schoolname)}">
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">學校網址：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="url"
								placeholder="請輸入學校網址" value="${school.url}">
						</div>
					</div>
					<!-- 
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">學校 logo：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="imgsrc"
								placeholder="學校 logo" value="${school.imgsrc}">
						</div>
					</div>
					 -->
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">學校簡述：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="descript"
								placeholder="學校簡述" value="${school.descript}">
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">認證：</label>
						<div class="col-sm-9">
							<input type="checkbox" name="checkid" value="1"
								data-checkid="${school.checkid}">
							校名及網址確認正確，確認之後會出現在『校際排名』當中。
						</div>
					</div>

				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_EditSchool_save" data-action="${param.action}">儲存</button>
			</div>
		</div>
	</div>
</div>
