<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<style style="text/css">
#reference a {
	text-decoration: none;
	color: inherit;
}

#reference a:hover {
	text-decoration: underline;
	color: inherit;
}

#reference a:VISITED {
	text-decoration: underline;
	color: inherit;
}
</style>

<c:if test="${fn:length(problem.reference)>0}">
	<%-- 	<c:set var="R" value=""></c:set>
	<c:forEach var="reference" items="${problem.reference}">
		<c:set var="R" value="${R}${reference}" />
	</c:forEach>
	<span id="reference"> <a href="./Problems?tag=${R}">${R}</a>
	</span>
 --%>
	<span id="reference"><c:forEach var="reference"
			items="${problem.reference}"><a href="./Problems?tag=${fn:trim(reference)}">${fn:trim(reference)}</a></c:forEach>
	</span>
</c:if>
