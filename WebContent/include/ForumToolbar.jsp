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
	<c:choose>
		<c:when test="${sessionScope.onlineUser.isForumManager()}">
			<fmt:message key="Forum.ArticleTypeSetting" />：
				<form name="form1" method="post" action="Article.api?action=setType"
				style="margin: 0px; display: inline; text-align: center"
				title="設定文章性質">
				<select name="articletype" id="articletype"
					data-articletype="${article.articletype}"
					data-articleid="${article.id }">
					<c:forEach var="value"
						items="${article.getAccessibleTypes(sessionScope.onlineUser)}">
						<option value="${value}">
							<fmt:message key="Forum.ArticleType_${value}" />
						</option>
					</c:forEach>
				</select> <input name="postid" type="hidden" value="${article.id}" /> <input
					name="ReturnPage" type="hidden"
					value="ShowThread?${fn:escapeXml(pageContext.request.queryString)}" />
			</form>
              | 
				<form name="form2" method="post"
				action="Article.api?action=setHidden"
				style="margin: 0px; display: inline; text-align: center"
				title="設定公開/隱藏">
				<select name="articlehidden" id="articlehidden"
					data-articlehidden="${article.hidden}"
					data-articleid="${article.id }">
					<c:forEach var="value"
						items="${article.getAccessibleHiddens(sessionScope.onlineUser)}">
						<option value="${value}">
							<fmt:message key="Forum.Hidden_${value}" />
						</option>
					</c:forEach>
				</select> <input name="postid" type="hidden" value="${article.id}" /> <input
					name="ReturnPage" type="hidden"
					value="ShowThread?${fn:escapeXml(pageContext.request.queryString)}" />
			</form>
			
               | <a href="./UpdateThread?postid=${article.id}">修改文章</a>
		</c:when>
		<c:otherwise>
			<fmt:message key="Forum.ArticleTypeSetting" />：<fmt:message
				key="Forum.ArticleType_${article.articletype}" /> | <fmt:message
				key="Forum.Hidden_${article.hidden}" />
		</c:otherwise>
	</c:choose>
	| <a href="./Reply?origid=${param.origid}&previd=${article.id}"> <fmt:message
			key="Forum.Reply" />
	</a> | <a href="#${param.reply}"> <fmt:message key="Forum.Back" />
	</a>
</div>
