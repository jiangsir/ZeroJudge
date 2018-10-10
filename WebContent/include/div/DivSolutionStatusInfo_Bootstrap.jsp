<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="solution" uri="http://jiangsir.tw/jstl/solution"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<jsp:include page="../Modals/Modal_ServerOutputs.jsp" />

<span id="statusinfo" data-solutionid="${solution.id}"
	data-problemscores="${solution.problem.scoresByJSON }"> <img
	src="images/Spinner.gif" id="Spinner" style="display: none;" /> <span
	id="judgement" data-solutionid="${solution.id }"> <c:if
			test="${sessionScope.onlineUser.canShowDetails(solution) }">
			<c:choose>
				<c:when test="${solution.isJudgement_AC }">
					<a href="#" class="acstyle"
						data-solutionid="${solution.id }">${solution.judgement }</a>
				</c:when>
				<c:when test="${solution.isJudgement_Waiting}">
					${solution.judgement }
				</c:when>
				<c:otherwise>
					<a href="#" data-solutionid="${solution.id }">${solution.judgement }</a>
				</c:otherwise>
			</c:choose>
		</c:if> <c:if test="${!sessionScope.onlineUser.canShowDetails(solution) }">
			<c:choose>
				<c:when test="${solution.isJudgement_AC }">
					<span class="acstyle">${solution.judgement }</span>
				</c:when>
				<c:otherwise>
					<span>${solution.judgement }</span>
				</c:otherwise>
			</c:choose>
		</c:if>
</span> <span id="summary">${solution.summary}</span>
	<c:if test="${sessionScope.onlineUser.isRejudgable(solution)}">
		<%-- &nbsp;<img
			src="./images/rejudge.svg" style="height: 1.2em;" alt="重測" border="0"
			title="重測" style="cursor: pointer" id="solution_rejudge"
			solutionid="${solution.id}" /> --%>
		<div class="btn btn-default btn-xs" id="rejudgeSolution"
			data-solutionid="${solution.id }">
			<i class="fa fa-repeat" aria-hidden="true" title="XX進行重測"></i>
		</div>

	</c:if> <%-- 	<c:if
		test="${solution.isVisible_ManualJudge(sessionScope.onlineUser)}">
		<c:set var="solution" value="${solution}" scope="request" />
		<jsp:include page="../dialog/ManualJudge.jsp" />
		<img src="images/validate.svg" style="height: 1em;" alt="手動評分"
			id="manualjudge" class="FakeLink" title="XX手動評分" />
	</c:if> --%>
</span>
