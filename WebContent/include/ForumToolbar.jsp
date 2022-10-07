<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="article" uri="http://jiangsir.tw/jstl/article"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div id="forumToolbar" class="pull-right" data-articleid=${article.id }>
	<jsp:include page="ForumToolbar_Setting.jsp">
		<jsp:param value="${origid}" name="origid" />
		<jsp:param value="${param.reply}" name="reply" />
	</jsp:include>
	| <a href="./Reply?origid=${param.origid}&previd=${article.id}"> <fmt:message
			key="Forum.Reply" />
	</a> | <a href="#${param.reply}"> <fmt:message key="Forum.Back" />
	</a>
</div>
