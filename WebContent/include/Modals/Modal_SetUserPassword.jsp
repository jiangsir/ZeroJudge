<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_SetUserPassword" role="dialog"
	tabindex="-1" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">解除綁定，重新設定系統帳號的密碼</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal">
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">您的系統帳號 ID：</label>
						<div class="col-sm-9">
							<input class="form-control" id="disabledInput" type="text"
								placeholder="${sessionScope.onlineUser.id}" disabled>
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">您的系統帳號：</label>
						<div class="col-sm-9">
							<input class="form-control" id="disabledInput" type="text"
								placeholder="${sessionScope.onlineUser.account}" disabled>
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">您的帳號暱稱：</label>
						<div class="col-sm-9">
							<input class="form-control" id="disabledInput" type="text"
								placeholder="${sessionScope.onlineUser.username}" disabled>
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">設定新密碼：</label>
						<div class="col-sm-9">
							<input type="password" class="form-control" name="UserPassword1"
								placeholder="設定新密碼" value="">
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">設定新密碼：</label>
						<div class="col-sm-9">
							<input type="password" class="form-control" name="UserPassword2"
								placeholder="再打一次新密碼" value="">
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-primary"
					id="modal_setUserPassword_submit" data-action="UnbindGoogle"
					data-userid="${sessionScope.onlineUser.id }">設定</button>
			</div>
		</div>
	</div>
</div>
