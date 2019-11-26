<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<style style="text/css">
.tag a {
	text-decoration: none;
	color: inherit;
}

.tag a:hover {
	text-decoration: underline;
	color: inherit;
}

.tag a:VISITED {
	text-decoration: underline;
	color: inherit;
}
</style>
<span class="tag"> <c:forEach var="tag" items="${problem.tags}">
		<a href="./Problems?tag=${fn:trim(tag)}">${fn:trim(tag)}</a>
	</c:forEach>
</span>
