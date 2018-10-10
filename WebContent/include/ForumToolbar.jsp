<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="article" uri="http://jiangsir.tw/jstl/article"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<hr />
<div id="forumToolbar"
	style="display: inline; font-size: smaller; float: right; margin-bottom: 1em;">
	<c:if
		test="${fn:length(article:getAccessibleTypes(sessionScope.onlineUser, article))>0 && fn:length(article:getAccessibleHiddens(sessionScope.onlineUser, article))>0}">
		<fmt:message key="Forum.ArticleTypeSetting" />
              : </c:if>

	<c:if
		test="${fn:length(article:getAccessibleTypes(sessionScope.onlineUser, article))>0}">
		<form id="form1" name="form1" method="post"
			action="Article.api?action=setType"
			style="margin: 0px; display: inline; text-align: center"
			title="設定文章性質">
			<select name="articletype" id="articletype"
				onChange="this.form.submit();">
				<c:forEach var="value"
					items="${article:getAccessibleTypes(sessionScope.onlineUser, article)}">
					<c:set var="selected" value="" />
					<c:if test="${article.articletype==value}">
						<c:set var="selected" value="selected" />
					</c:if>
					<option value="${value}" ${selected}>
						<fmt:message key="Forum.ArticleType_${value}" />
					</option>
				</c:forEach>
			</select> <input name="postid" type="hidden" value="${article.id}" /> <input
				name="ReturnPage" type="hidden"
				value="ShowThread?${fn:escapeXml(pageContext.request.queryString)}" />
		</form>
              | </c:if>
	<c:if
		test="${fn:length(article:getAccessibleHiddens(sessionScope.onlineUser, article))>0}">
		<form id="form2" name="form2" method="post"
			action="Article.api?action=setHidden"
			style="margin: 0px; display: inline; text-align: center"
			title="設定公開/隱藏">
			<select name="articlehidden" id="articlehidden"
				onChange="this.form.submit();">
				<c:forEach var="value"
					items="${article:getAccessibleHiddens(sessionScope.onlineUser, article)}">
					<c:set var="selected" value="" />
					<c:if test="${article.hidden==value}">
						<c:set var="selected" value="selected" />
					</c:if>
					<option value="${value}" ${selected}>
						<fmt:message key="Forum.Hidden_${value}" />
					</option>
				</c:forEach>
			</select> <input name="postid" type="hidden" value="${article.id}" /> <input
				name="ReturnPage" type="hidden"
				value="ShowThread?${fn:escapeXml(pageContext.request.queryString)}" />
		</form>
	</c:if>
	<c:if test="${sessionScope.onlineUser.isHigherEqualThanMANAGER }"> | <a
			href="./UpdateThread?postid=${article.id}">修改文章</a>
	</c:if>
	| <a href="./Reply?origid=${param.origid}&previd=${article.id}"> <fmt:message
			key="Forum.Reply" />
	</a> | <a href="#${param.reply}"> <fmt:message key="Forum.Back" />
	</a>
</div>
