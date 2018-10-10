<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_TestdataPair" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">本測資檔的輸入、輸出</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<form class="form-horizontal">
						<div class="form-group col-md-6">
							<label for="tname">輸入測資 <span id="infilename"></span></label>
							<textarea id="indata" rows="18" class="form-control"
								name="indata" style="font-family: monospace;"></textarea>
						</div>
						<div class="form-group col-md-6">
							<label for="tname">輸出測資 <span id="outfilename"></span></label>
							<textarea id="outdata" rows="18" class="form-control"
								name="outdata" style="font-family: monospace;"></textarea>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
