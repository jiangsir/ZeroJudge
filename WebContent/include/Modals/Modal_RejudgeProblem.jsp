<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<div class="modal fade" id="Modal_RejudgeProblem" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="title">重測本題目(${problem.problemid})的程式碼</h4>
			</div>
			<div class="modal-body" id="content">
				<form class="was-validated" data-problemid="${problem.problemid}">
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_ALL" name="rejudge_checkbox" value="ALL"> <label
							class="custom-control-label" for="customControlValidation1">
							重測全部程式碼? (請謹慎使用)</label>
					</div>
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_AC" name="rejudge_checkbox" value="AC"> <label
							class="custom-control-label" for="customControlValidation1">
							重測 AC? (請謹慎使用)</label>
					</div>
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_NA" name="rejudge_checkbox" value="NA"> <label
							class="custom-control-label" for="customControlValidation1">
							重測 NA?</label>
					</div>
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_WA" name="rejudge_checkbox" value="WA"> <label
							class="custom-control-label" for="customControlValidation1">
							重測 WA?</label>
					</div>
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_TLE" name="rejudge_checkbox" value="TLE"> <label
							class="custom-control-label" for="customControlValidation1">
							重測 TLE?</label>
					</div>
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_RE" name="rejudge_checkbox" value="RE"> <label
							class="custom-control-label" for="customControlValidation1">
							重測 RE?</label>
					</div>
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_CE" name="rejudge_checkbox" value="CE"> <label
							class="custom-control-label" for="customControlValidation1">
							重測 CE?</label>
					</div>
					<div class="custom-control custom-checkbox mb-3">
						<input type="checkbox" class="custom-control-input"
							id="rejudge_SE" name="rejudge_checkbox" value="SE"> <label
							class="custom-control-label" for="customControlValidation1">
							重測 SE?</label> <span id="judgements" hidden></span>
					</div>
					<h4>
						共有 <span id="SubmissionCount">0</span> 個程式碼將被重測，確定進行重測嗎？
					</h4>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_RejudgeProblem_confirm" data-problemid="">確定</button>
			</div>
		</div>
	</div>
</div>
