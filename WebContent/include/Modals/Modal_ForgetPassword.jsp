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
<div class="modal fade" id="Modal_ForgetPassword"
	data-focus-on="input:first" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">忘記密碼？</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label"><fmt:message
								key="ForgetPassword.Account" />：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="account"
								name="account" placeholder="account" value="">
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label"><fmt:message
								key="ForgetPassword.Email" />：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="email"
								placeholder="請輸入 email" value="">
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_ForgetPassword_save" data-action="ForgetPassword">寄出密碼信</button>
			</div>
		</div>
	</div>
</div>
