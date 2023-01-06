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
<div class="modal fade" id="Modal_ExportContest_${contest.id}"
	data-focus-on="input:first" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">匯出競賽</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">原競賽名稱：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="title"
								name="prev_title" value="${fn:escapeXml(contest.title)}" readonly>
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label">指定匯出競賽名稱：</label>
						<div class="col-sm-9">
							<input id="schoolid" name="contestid" type="hidden"
								value="${param.contestid }" /> <input type="text"
								class="form-control" id="tname" name="new_title"
								placeholder="請輸入測驗名稱" value="${fn:escapeXml(contest.title)}">
						</div>
					</div>
					<!-- <div class="form-group">
						<label for="tname" class="col-sm-3 control-label">參數：</label>
						<div class="col-sm-9">
							<div class="radio">
								<label>
									<input type="radio" name="radio_exportContest" value="1" checked>#1 僅匯出完整題目(不含使用者與解題紀錄)?
								</label>
							</div>
							<div class="radio">
								<label>
									<input type="radio" name="radio_exportContest" value="2">#2 僅匯出競賽設定?
								</label>
							</div>
							<div class="radio">
								<label>
									<input type="radio" name="radio_exportContest" value="3">#3 匯出完整解題紀錄(包含使用者名稱與完整題目)?
								</label>
							</div>
						</div>
					</div> -->
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_exportContest_submit" data-contestid="${contest.id}">匯出</button>
			</div>
		</div>
	</div>
</div>
