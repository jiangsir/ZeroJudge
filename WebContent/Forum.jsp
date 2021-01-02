<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript" src="Forum.js?${applicationScope.built }"></script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<c:set var="pp" value=""></c:set>
			<c:if test="${param.problemid!=null && param.problemid!=''}">
				<c:set var="pp" value="?problemid=${param.problemid}"></c:set>
			</c:if>
			<a href="./NewThread${pp }" type="button" class="btn btn-primary"><fmt:message
					key="Forum.NewThread" /></a><br />
			<hr></hr>
			<table class="table table-hover">
				<tr>
					<td><fmt:message key="Forum.ID" /></td>
					<td><fmt:message key="Forum.User" /></td>
					<td><c:if test="${param.tab!='tab02'}">
							<fmt:message key="Forum.Problem" />
						</c:if></td>
					<td width="50%"><fmt:message key="Forum.Subject" /></td>
					<td><fmt:message key="Forum.Hitnum" /></td>
					<td width="18%"><fmt:message key="Forum.PostDate" /></td>
				</tr>
				<c:forEach var="markedArticle" items="${markedArticles}">
					<tr>
						<td>${markedArticle.id }</td>
						<td><c:set var="user" value="${markedArticle.user}"
								scope="request" /> <jsp:include
								page="include/div/UserAccount_TypeA.jsp" /></td>
						<td><a
							href="./ShowProblem?problemid=${markedArticle.problemid}">${markedArticle.problemid}</a></td>
						<td width="50%">
							<div align="left">
								<c:set var="article" value="${markedArticle}" scope="request" />
								<jsp:include page="include/div/ArticleTitle.jsp" />
							</div>
						</td>
						<td>${markedArticle.clicknum}</td>
						<td width="18%" style="font-size: smaller"><fmt:formatDate
								value="${markedArticle.timestamp}" pattern="yyyy-MM-dd HH:mm" /></td>
					</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${fn:length(articles)!=0}">
						<c:forEach var="article" items="${articles}" varStatus="varstatus">
							<tr>
								<td>${article.id}</td>
								<td><c:set var="user" value="${article.user}"
										scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /></td>
								<td><a href="./ShowProblem?problemid=${article.problemid}">${article.problemid}</a></td>
								<td width="50%">
									<div align="left">
										<c:set var="article" value="${article}" scope="request" />
										<jsp:include page="include/div/ArticleTitle.jsp" />
									</div>
								</td>
								<td>${article.clicknum}</td>
								<td width="18%" style="font-size: smaller"><fmt:formatDate
										value="${article.timestamp}" pattern="yyyy-MM-dd HH:mm" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="6"><div align="center">
									<fmt:message key="NO_DATA" />
								</div></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
