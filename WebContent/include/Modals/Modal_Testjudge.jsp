<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_Testjudge" tabindex="-1"
	data-focus-on="input:first">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="title">測試執行說明：</h4>
			</div>
			<div class="modal-body" id="content">
				<form id="form_Testjudge">
					<div class="row">
						<div class="col-md-12">
							<ul>
								<li>“測試執行”讓您得以在送出程式碼之前先測試系統的編譯器。</li>
								<li>自定測資一律以單測資點進行，測資長度限制在各 1000 字元以內。</li>
								<li>“測試執行”不會計入成績及送出次數。</li>
							</ul>
							<span style="font-size: larger; color: #FF0000">請注意：
								測試執行僅測試您所提供的輸入輸出測資，並非實際題目測資。</span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<jsp:include page="../div/ServerConfig_Compilers.jsp" />
							測試的
							<fmt:message key="Problem.Code" />
							：<br />
							<textarea id="testcode" name="testcode" rows="10"
								class="form-control"></textarea>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							自定輸入測資：(可自行修改)
							<textarea id="testjudge_indata" name="testjudge_indata" rows="8"
								class="form-control">${problem.sampleinput}</textarea>
						</div>
						<div class="col-md-6">
							自定輸出測資：(可自行修改)
							<textarea id="testjudge_outdata" name="testjudge_outdata"
								rows="8" class="form-control">${problem.sampleoutput}</textarea>
						</div>
					</div>
					<input name="contestid" type="hidden"
						value="${sessionScope.onlineUser.joinedcontestid}" /> <input
						name="problemid" type="hidden" value="${problem.problemid}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="runTestjudge">測試執行</button>
			</div>
		</div>
	</div>
</div>
