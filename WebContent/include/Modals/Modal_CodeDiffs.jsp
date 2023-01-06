<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_CodeDiffs_${solution.id}"
	tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					程式碼比較 #<span id="solutionid">${solution.id}</span>
				</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<h4>使用者程式碼(準備拿出來跟先前的程式碼比較的程式碼)</h4>
							<h4>
								#${solution.id}
								<c:set var="user" value="${solution.user}" scope="request" />
								<jsp:include page="../div/UserAccount_TypeA.jsp" /></h4>
							<h4>
								<c:set var="problem" value="${solution.problem}" scope="request" />
								<jsp:include page="../div/ProblemTitle.jsp" />
							</h4>
							<%-- 							<pre id="code"
								class="language-${fn:toLowerCase(solution.language)}">${fn:escapeXml(solution.code)}</pre>
 --%>
							<table class="table table-hover">
								<thead>
									<tr>
										<th>code 0</th>
										<th>code 1</th>
									</tr>
								</thead>
								<tbody>
									<tr id="CodeDiff">
										<td id="solutionid"></td>
										<td id="code"></td>
										<td id="submittime"></td>
										<td id="similarity"></td>
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
