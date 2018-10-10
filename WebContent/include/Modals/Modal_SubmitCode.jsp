<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_SubmitCode" tabindex="-1"
	data-focus-on="input:first">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="title">送出程式碼：</h4>
			</div>
			<div class="modal-body" id="content">
				<form id="form_SubmitCode">
					<div class="row">
						<div class="col-md-12">
							<jsp:include page="../div/ServerConfig_Compilers.jsp" />

							<fmt:message key="Problem.Code" />
							：<br />
							<textarea id="code" name="code" rows="10"
								class="form-control"></textarea>
						</div>
					</div>
					<input name="contestid" type="hidden"
						value="${sessionScope.onlineUser.joinedcontestid}" /> <input
						name="problemid" type="hidden" value="${problem.problemid}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="submitCode">送出</button>
			</div>
		</div>
	</div>
</div>
