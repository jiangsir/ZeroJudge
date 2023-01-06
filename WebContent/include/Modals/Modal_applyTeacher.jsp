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
<div class="modal fade" id="Modal_applyTeacher"
	data-focus-on="input:first" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">申請成為教師？</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<h4 class="modal-title" id="myModalLabel">申請成為教師，將擁有以下權限：</h4>
						<ul>
							<li>開設課程</li>
							<li>開設隨堂測驗</li>
							<li>設計練習題</li>
						</ul>
						<p>開課權限說明：您可以自行設計「練習題」來作為課堂所需的題目。因為公開題目常常並不剛好適合課堂進度，因此可以自行設計適合的題目以便作為開課或考試使用。</p>
						<p></p>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_applyTeacher_submit" data-action="applyTeacher">我同意！</button>
			</div>
		</div>
	</div>
</div>
