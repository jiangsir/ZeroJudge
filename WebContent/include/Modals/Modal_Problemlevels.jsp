<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_Problemlevels_${problem.id}" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					題目分級歷程 題目編號#<span id="problemid">${problem.problemid}</span>
				</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<label for="tname"> <c:set var="problem"
									value="${problem}" scope="request" /> <jsp:include
									page="../div/ProblemTitle.jsp" />
							</label>
							<pre id="jsonProblemlevels"></pre>
							<table class="table table-hover">
								<thead>
									<tr>
										<th>userid</th>
										<th>level</th>
										<th>updatetime</th>
									</tr>
								</thead>
								<tbody>
									<tr id="problemlevel">
										<td id="userid"></td>
										<td id="level"></td>
										<td id="updatetime"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
