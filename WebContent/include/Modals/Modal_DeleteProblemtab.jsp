<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_deleteProblemtab_${tabid}"
	tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					刪除題目標籤<br />
				</h4>
			</div>
			<div class="modal-body">
				<h4 id="myModalLabel">
					刪除這個「<span id="delete_tabid"></span>${tabid }」標籤後，這些題目標籤要改成<br />
					<%-- <input
						type="text" class="hidden" name="tabid" value="${tabid }"> --%>
				</h4>
				<c:forEach var="renametotab"
					items="${applicationScope.appConfig.problemtabs}">
					<input type="radio" name="renametotabid" value="${renametotab.id}" />${renametotab.id}<br />
				</c:forEach>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_deleteProblemtab" data-tabid="${tabid}">刪除</button>
			</div>
		</div>
	</div>
</div>
