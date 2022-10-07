<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<%-- 
<c:forEach var="cacheUser" items="${applicationScope.CacheUsers}">
 ${cacheUser} ${cacheUser.value.account}: ${cacheUser.value.aclist}<br>
</c:forEach>

${applicationScope.CacheUsers[sessionScope.onlineUser.id].account}
--%>

<%-- ${fn:contains(sessionScope.onlineUser.aclist,problem.problemid)} --%>

<c:if
	test="${fn:contains(applicationScope.CacheUsers[sessionScope.onlineUser.id].aclist,problem.problemid)}">
	<a
		href="./Submissions?problemid=${problem.problemid}&account=${sessionScope.onlineUser.account}&status=AC"><img
		src="images/accept16.svg" style="height: 1em;" title="已成功解出" /></a>
</c:if>

