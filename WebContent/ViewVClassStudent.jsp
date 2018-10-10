<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />

<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<table width="80%" border="0" align="center">
			<tr>
				<th><fmt:message key="User.BasicInfomation" /></th>
				<th width="70%">『${vclass.vclassname}』 答對題目統計
					(${fn:length(vproblems)}題)</th>
			</tr>
			<tr>
				<td height="230" valign="top"><table border="0">
						<tr>
							<td width="40"><div align="right">
									<fmt:message key="User.ID" />
									:
								</div></td>
							<td>${user.id}</td>
						</tr>
						<tr valign="top">
							<td width="40"><div align="right">
									<fmt:message key="User.Account" />
									:
								</div></td>
							<td>${user.account}</td>
						</tr>
						<tr>
							<td width="40"><div align="right">
									<fmt:message key="User.Username" />
									:
								</div></td>
							<td>${fn:escapeXml(user.username)}</td>
						</tr>
						<tr>
							<td width="40"><div align="right">
									<fmt:message key="User.School" />
									:
								</div></td>
							<td>${fn:escapeXml(user.schoolname)}</td>
						</tr>
						<tr>
							<td width="40"><div align="right">
									<fmt:message key="User.IPaddress" />
									:
								</div></td>
							<td><div align="left">${user.ipset}</div></td>
						</tr>
						<tr>
							<td width="40"><div align="right">
									<fmt:message key="User.Point" />
									:
								</div></td>
							<td><div align="left">user.rankpoint</div></td>
						</tr>
					</table> <br /> <fmt:message key="User.LastLogin" />：<br /> <fmt:formatDate
						value="${user.lastlogin}" pattern="yyyy-MM-dd HH:mm:ss" /><br />
					<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
						<span class="DEBUGGEROnly"><a
							href="./UpdateUser?account=${user.account}">修改個人資訊</a></span>
					</c:if><br /> <br /></td>
				<td valign="top" style="font-family: Arial, sans-serif;"><c:choose>
						<c:when test="${fn:length(vproblems)==0}">沒有任何題目</c:when>
						<c:otherwise>
							<c:forEach var="vproblem" items="${vproblems}"
								varStatus="varstatus">
								<c:if test="${vproblem[2]=='AC'}">
									<a href="./ShowProblem?problemid=${vproblem[0]}" id="acstyle"
										title="${fn:escapeXml(vproblem[1])}">${vproblem[0]}</a>
								</c:if>
								<c:if test="${vproblem[2]==''}">
									<a href="./ShowProblem?problemid=${vproblem[0]}"
										style="color: #666666" title="${fn:escapeXml(vproblem[1])}">${vproblem[0]}</a>
								</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose> <br />
					<hr /> * 本統計表的題目僅記錄本課程當中所有使用過的題目<br /> *
					在本課程所進行的&quot;隨堂測驗&quot;過程當中答對的題目才列入統計，在平常練習當中答對者並不會列入統計，請注意。</td>
			</tr>
		</table>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
