<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_InsertVContest" tabindex="-1"
	data-focus-on="input:first">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="title">建立一個新的隨堂測驗</h4>
			</div>
			<div class="modal-body" id="content">
				<form id="form_InsertVContest">
					<div class="row">
						<div class="col-md-12">
							<h4 class="modal-title" id="title">這裡貼上 contest settings 的
								json 格式。若此處留白，則建立預設測驗。</h4>
							<br>
							<textarea id="VContestSettings" name="VContestSettings" rows="10"
								class="form-control" style="font-family: Consolas, monospace; font-size: 1.1em;"
								placeholder="這裡貼上 contest setting 的 json 格式。若此處留白，則建立預設測驗。"></textarea>
						</div>
					</div>
					<input name="vclassid" type="hidden" value="${param.vclassid}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="ModalSubmit_InsertVContest">新增一個隨堂測驗</button>
			</div>
		</div>
	</div>
</div>
