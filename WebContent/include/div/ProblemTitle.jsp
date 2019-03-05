<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>



<c:choose>
	<c:when test="${problem.display == problem.DISPLAY_OPEN}">
	   ${problem.problemid}. <a
			href="./ShowProblem?problemid=${problem.problemid}"
			title="${problem.backgrounds}">${fn:escapeXml(problem.title)}</a>
	</c:when>
	<c:when test="${problem.display == problem.DISPLAY_PRACTICE}">
		<i class="fa fa-cc" aria-hidden="true" title="練習題"></i>	
	${problem.problemid}. <a
			href="./ShowProblem?problemid=${problem.problemid}"
			title="${problem.backgrounds}">${fn:escapeXml(problem.title)}</a>
	</c:when>
	<c:otherwise>
		<span class="glyphicon glyphicon-eye-close" title="隱藏題"></span>
		<span style="text-decoration: line-through;">
			${problem.problemid}. <a
			href="./ShowProblem?problemid=${problem.problemid}"
			title="${problem.backgrounds}">${fn:escapeXml(problem.title)}</a>
		</span>
	</c:otherwise>
</c:choose>


<%-- <c:if test="${problem.display == problem.DISPLAY_OPEN}">
	${problem.problemid}. <a
		href="./ShowProblem?problemid=${problem.problemid}"
		title="${problem.backgrounds}">${fn:escapeXml(problem.title)}</a>
</c:if>

<c:if test="${problem.display == problem.DISPLAY_PRACTICE}">
	<span style="text-decoration: line-through;">
		${problem.problemid}. <a
		href="./ShowProblem?problemid=${problem.problemid}"
		title="${problem.backgrounds}">${fn:escapeXml(problem.title)}</a>
	</span>
</c:if>

<c:if test="${problem.display == problem.DISPLAY_HIDE}">
	<span style="text-decoration: line-through;">
		${problem.problemid}. <a
		href="./ShowProblem?problemid=${problem.problemid}"
		title="${problem.backgrounds}">${fn:escapeXml(problem.title)}</a>
	</span>
</c:if>
 --%>
<c:if test="${fn:length(problem.reference)>0}">
	<span style="font-size: 0.8em; color: #AAAAAA"> -- <jsp:include
			page="./ProblemReference.jsp" />
	</span>
</c:if>
