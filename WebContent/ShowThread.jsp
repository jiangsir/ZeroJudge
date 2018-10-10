<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="article" uri="http://jiangsir.tw/jstl/article"%>

<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<script type="text/x-mathjax-config">
MathJax.Hub.Config({
  tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}
});
</script>
<!-- <script type="text/javascript"
	src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
	
</script>
 -->
<script type="text/javascript"
	src="./jscripts/MathJax-2.5-mini/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
	
</script>
</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<div class="col-md-12">

				<a href="./NewThread?problemid=${articles[0].problemid}"
					class="btn btn-primary"> <fmt:message key="Forum.NewThread" />
				</a>
				<hr>
				<table class="table">
					<c:forEach var="article" items="${articles}">
						<tr bgcolor="#DCEDF5">
							<td width="15%">#${article.id}</td>
							<td><c:set var="article" value="${article}" scope="request" />
								<jsp:include page="include/div/ArticleTitle.jsp" /></td>
						</tr>
						<tr>
							<td><c:set var="user" value="${article.user}"
									scope="request" /> <jsp:include
									page="include/div/UserAccount_TypeB.jsp" /></td>

							<td><div align="right" style="font-size: smaller">
									<c:set var="problem" value="${article.problem}" scope="request" />
									<jsp:include page="include/div/ProblemTitle.jsp" />
									| From: ${article.ipfrom} |
									<fmt:message key="Forum.PostDate" />
									:
									<fmt:formatDate value="${article.timestamp}"
										pattern="yyyy-MM-dd HH:mm" />
									<br />
								</div>
								<hr /> <c:choose>
									<c:when test="${article.hidden.is_block}">
										<span style="color: red;">本文章已被封鎖！</span>
									</c:when>
									<c:otherwise> ${article.content}&nbsp; </c:otherwise>
								</c:choose> <c:set var="article" value="${article}" scope="request" /> <c:set
									var="origid" value="${param.reply}" /> <c:if
									test="${param.reply==0}">
									<c:set var="origid" value="${param.postid}" />
								</c:if> <jsp:include page="include/ForumToolbar.jsp">
									<jsp:param value="${origid}" name="origid" />
									<jsp:param value="${param.reply}" name="reply" />
								</jsp:include></td>
						</tr>
					</c:forEach>
				</table>
				<div align="right">
					ZeroJudge Forum <br />
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="include/Footer.jsp" />
</body>
</html>
