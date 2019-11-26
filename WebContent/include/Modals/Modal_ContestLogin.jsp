<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_ContestLogin" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">參加競賽！ #${contest.id}
					${contest.title}</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label"><fmt:message
								key="Login.Account" />：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="account"
								name="account" placeholder="帳號">
						</div>
					</div>
					<div class="form-group">
						<label for="tcontent" class="col-sm-3 control-label"><fmt:message
								key="Login.Password" />： </label>
						<div class="col-sm-9">
							<input type="password" id="tcontent" class="form-control"
								name="passwd" placeholder="請輸入密碼"></input>
						</div>
					</div>
					<input name="contestid" value="${contest.id}" type="hidden" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="button_ContestLogin">確定</button>
			</div>
		</div>
	</div>
</div>
