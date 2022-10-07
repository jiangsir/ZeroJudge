<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<c:choose>
	<c:when test="${fn:length(codediffs)>0}">
		<c:forEach var="codediff" items="${codediffs}" varStatus="varstatus">
			<tr>
				<td>#${solution_0.id } <c:set var="user"
						value="${solution_0.user}" scope="request" /> <jsp:include
						page="../div/UserAccount_TypeA.jsp" /> <c:if
						test="${solution_0.contest.isVContest() && solution_0.contest.getVClass().getIsOwner(sessionScope.onlineUser)}">[${solution_0.contest.getContestantByUserid(solution_0.userid).toStudent().getComment()}]</c:if>
					<c:set var="solution" value="${solution_0}" scope="request" /><br>
					<jsp:include page="../div/DivSolutionStatusInfo_Bootstrap.jsp" />
					<fmt:formatDate value="${solution_0.submittime}"
						pattern="yyyy-MM-dd HH:mm:ss" />
					<hr> <pre id="code"
						class="language-${fn:toLowerCase(solution_0.language)}">${fn:escapeXml(solution_0.code)}</pre>
				</td>
				<td>#${codediff.solutionid } <c:set var="user"
						value="${codediff.solution.user}" scope="request" /> <c:set
						var="solution" value="${codediff.solution}" scope="request" /><jsp:include
						page="../div/UserAccount_TypeA.jsp" /> <c:if
						test="${solution.contest.isVContest() && solution.contest.getVClass().getIsOwner(sessionScope.onlineUser)}">[${solution.contest.getContestantByUserid(solution.userid).toStudent().getComment()}]</c:if>
					<br> <jsp:include
						page="../div/DivSolutionStatusInfo_Bootstrap.jsp" /> <fmt:formatDate
						value="${codediff.solution.submittime}" pattern="yyyy-MM-dd HH:mm:ss" />
<%-- 					<c:set var="sec"
						value="${(solution_0.submittime.time - codediff.solution.submittime.time)/1000 }" />
					<c:set var="min" value="${(sec-sec%60)/60 }" /> <c:set var="hr"
						value="${(min-min%60)/60 }" /> <c:set var="day"
						value="${(hr-hr%24)/24 }" /> (${day} 天 ${hr%24} 小時 ${min%60}分
					${sec%60}秒 前)
 --%>					<hr> <pre id="code"
						class="language-${fn:toLowerCase(codediff.solution.language)}">${fn:escapeXml(codediff.solution.code)}</pre></td>
			</tr>
		</c:forEach>
	</c:when>
	<c:otherwise>
	沒有更早的程式碼。
	</c:otherwise>
</c:choose>
