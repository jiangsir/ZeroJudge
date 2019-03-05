<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<p>
			<br /> BaseCode
		</p>
		<p>${basecode.solutionid} ${basecode.problemid}
			${basecode.account}</p>
		<form id="form1" name="form1" method="post" action="">
			<textarea name="textarea" cols="80" rows="15">${basecode.code}</textarea>
		</form>
		<p>&nbsp;</p>
		<p>CompareCode</p>
		<table width="100%" align="center">
			<tr>
				<td>solutionid</td>
				<td>Account</td>
				<td>ipfrom</td>
				<td>language</td>
				<td>codelocker</td>
				<td>contestid</td>
				<td>status</td>
				<td>comparestatus</td>
				<td>相似性</td>
			</tr>
			<c:forEach var="comparecode" items="${comparecodes}">
				<tr>
					<td>${comparecode.solutionid }</td>
					<td><a href="./UserStatistic?id=${comparecode.userid}">${comparecode.userid}</a></td>
					<td>${comparecode.ipfrom}</td>
					<td><a href="./ShowCode?solutionid=${comparecode.solutionid}">${comparecode.language}</a></td>
					<td>${comparecode.codelocker}</td>
					<td>${comparecode.contestid}</td>
					<td>${comparecode.status}</td>
					<td>${comparecode.comparestatus}</td>
					<td><a
						href="./Similarity?basecodeid=${basecode.solutionid}&comparecodeid=${comparecode.solutionid}">
							<c:choose>
								<c:when
									test="${comparecode.resultlinenum>=comparecode.baselinenum}">0%</c:when>
								<c:otherwise>
									<fmt:formatNumber
										value="${(comparecode.baselinenum-comparecode.resultlinenum)/comparecode.baselinenum}"
										type="percent" />
								</c:otherwise>
							</c:choose>
					</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
