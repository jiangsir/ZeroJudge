<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<span id="statusinfo" data-problemid="${problem.problemid }"
	data-problemscores="${problem.scoresByJSON }"> <%-- <jsp:include
		page="../dialog/ShowDetail.jsp" /> 
 --%> <img src="images/Spinner.gif" id="prejudge_spinner"
	style="display: none;" /> <span id="problemJudgement"
	data-problemid="${problem.problemid }"> <c:if
			test="${problem:canShowDetails(sessionScope.onlineUser, problem) }">
			<c:choose>
				<c:when test="${problem.isPrejudgement_AC }">
					<%-- 				<c:set var="serveroutputs" value="${problem.serveroutputs}"
					scope="request" />
				<jsp:include page="../dialog/ShowDetail.jsp" />
 --%>
					<a href="#" class="acstyle" data-problemid="${problem.problemid }">${problem.prejudgement }</a>
				</c:when>
				<c:when test="${problem.isPrejudgement_Waiting }">
					<span>${problem.prejudgement }</span>
				</c:when>
				<c:otherwise>
					<%-- 				<c:set var="serveroutputs" value="${problem.serveroutputs}"
					scope="request" />
				<jsp:include page="../dialog/ShowDetail.jsp" />
 --%>
					<a href="#" data-problemid="${problem.problemid }">${problem.prejudgement }</a>
				</c:otherwise>
			</c:choose>
		</c:if> <c:if
			test="${!problem:canShowDetails(sessionScope.onlineUser, problem) }">
			<c:choose>
				<c:when test="${problem.isPrejudgement_AC }">
					<span class="acstyle">${problem.prejudgement }</span>
				</c:when>
				<c:otherwise>
					<span>${problem.prejudgement }</span>
				</c:otherwise>
			</c:choose>
		</c:if>
</span> <span id="summary">${problem.summary}</span> <span
	class="glyphicon glyphicon-repeat" title="進行前測" id="ICON_prejudge" data-problemid="${problem.problemid}"></span>
</span>
