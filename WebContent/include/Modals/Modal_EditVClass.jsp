<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_EditVClass_${param.vclassid }"
	tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">編輯課程</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">課程名稱：</label>
						<div class="col-sm-9">
							<input type="text" class="hidden" id="tname" name="vclassid"
								value="${vclass.id }"> <input type="text"
								class="form-control" id="tname" name="vclassname"
								placeholder="請輸入課程名稱" value="${vclass.vclassname }">
						</div>
					</div>
					<div class="form-group">
						<label for="tcontent" class="col-sm-3 control-label">課程說明：</label>
						<div class="col-sm-9">
							<textarea id="tcontent" rows="5" class="form-control"
								name="descript" placeholder="請輸入課程說明">${vclass.descript}</textarea>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="modal_saveVClass"
					data-action="${param.action }">儲存</button>
			</div>
		</div>
	</div>
</div>
