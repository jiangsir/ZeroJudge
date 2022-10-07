<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<c:choose>
	<c:when test="${fn:length(serveroutputs)>0}">
		<c:forEach var="serveroutput" items="${serveroutputs}"
			varStatus="varstatus">
			<%--     第 ${varstatus.count } 測資點(${serveroutput.score}%):
 --%>    第 ${varstatus.count } 測資點(${problem.scores[varstatus.count-1]}%): 
                <c:choose>
				<c:when test="${serveroutput.judgement==serveroutput.AC }">
					<span class="acstyle">${serveroutput.judgement }</span>
				</c:when>
				<c:otherwise>
					<span>${serveroutput.judgement }</span>
				</c:otherwise>
			</c:choose>
			<span id="summary">(${serveroutput.summary})</span>
			<br />
			<fmt:message key="Server.REASON.${serveroutput.reason }" />
			<br />
			<pre>${serveroutput.hint }</pre>
			<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
				<pre class="DEBUGGEROnly">${serveroutput.debug }</pre>
			</c:if>
			<hr />
		</c:forEach>
	</c:when>
	<c:otherwise>
    沒有任何結果可以呈現！
    </c:otherwise>
</c:choose>
