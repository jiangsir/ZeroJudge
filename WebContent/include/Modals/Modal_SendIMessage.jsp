<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_SendIMessage" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">傳送站內訊息</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label"><fmt:message
								key="IMessage.To" />：</label>
						<div class="col-sm-9">
							<input id="schoolid" name="schoolid" type="hidden"
								value="${school.id }" /> <input type="text"
								class="form-control" id="to" name="to" placeholder="收件人"
								value="${fn:escapeXml(to)}">
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label"><fmt:message
								key="IMessage.Subject" />：</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="subject"
								placeholder="主題" value="${fn:escapeXml(subject)}">
						</div>
					</div>
					<div class="form-group">
						<label for="tname" class="col-sm-3 control-label"><fmt:message
								key="IMessage.Content" />：</label>
						<div class="col-sm-9">
							<textarea class="form-control" rows="10" name="content"></textarea>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_sendIMessage" data-action="SendIMessage">傳送</button>
			</div>
		</div>
	</div>
</div>
