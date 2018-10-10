<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<!-- <script language="JavaScript">
	jQuery(document).ready(function() {
		jQuery("form[id='searchAuthor']").click(function() {
			$(this).submit();
		});
	});
</script>
 -->

<c:if test="${user.isEnabled==true}">
	<span> <c:if test="${user.isAuthhost_Google }">
			<img src="${user.pictureBase64 }" class="img-rounded"
				style="width: 1.5em;" title="${user.email }">
		</c:if> <a href="./UserStatistic?id=${user.id}">${user.account}</a>(${fn:escapeXml(user.username)})
		<%-- (${fn:substring(fn:escapeXml(user.username),0,15)}) --%>
	</span>
	<%-- <a href="./UserStatistic?id=${user.id}">${fn:escapeXml(user.account)}</a> --%>
</c:if>
<c:if test="${user.isEnabled==false}">
	<span style="text-decoration: line-through;"> <a
		href="./UserStatistic?id=${user.id}">${fn:escapeXml(user.account)}</a>
	</span>
</c:if>
<%--  
<c:if test="${user.generalManager}">
	<img src="images/ICO_GeneralManager.png" title="站務管理員" />
</c:if>
--%>
<c:if test="${user.problemManager}">
	<form style="display: inline;" id="searchAuthor"
		action="./Problems?author=${user.account}" method="post">
		<img style="height: 1.2em;" class="FakeLink"
			src="images/ICO_problemsetter.png" title="題目管理員" />
	</form>
</c:if>
<c:if test="${user.isQualifiedAuthor}">
	<img src="images/ICO_QualifiedProblemSetter.png" title="題目管理員" />
</c:if>
<c:if test="${user.VClassManager}">
	<img src="images/ICO_VClassManager.png" title="課程管理員" />
</c:if>

<div id="${user.account}hint" class="hint">School: ${user.school.schoolname}</div>
<%-- <div style="color: white;">config:${sessionScope.onlineUser.config},
	general:${sessionScope.onlineUser.generalManager},
	isDEBUGGER:${sessionScope.onlineUser.isDEBUGGER}</div> --%>
