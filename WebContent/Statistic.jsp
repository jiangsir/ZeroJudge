<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${map.Title} -- 統計資訊</title>
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<p></p>
		<c:choose>
			<c:when test="${browsers!=null}">
				<table>
					<c:forEach var="browser" items="${browsers}" varStatus="varstatus">
						<c:if test="${varstatus.count==1}">
							<c:set var="maxbrowser" scope="page" value="${browser.COUNT}" />
						</c:if>
						<tr>
							<td>${browser.browser}: ${browser.COUNT}</td>
							<td width="500"><table
									width="${browser.COUNT/maxbrowser*100}%">
									<tr>
										<td width="" bgcolor="#0033CC">&nbsp;</td>
									</tr>
								</table></td>
						</tr>
					</c:forEach>
				</table>
				<br>
			</c:when>
			<c:when test="${ipfrom!=null}">
				<table>
					<c:forEach var="domain" items="${ipfrom}" varStatus="varstatus">
						<c:if test="${varstatus.count==1}">
							<c:set var="maxdomain" scope="page" value="${domain.COUNT}" />
						</c:if>
						<tr>
							<td><a
								href="http://api.hostip.info/get_html.php?ip=${domain.ipfrom}&position=true"><IMG
									SRC="http://api.hostip.info/flag.php?ip=${domain.ipfrom}"
									ALT="?" height="14" border="0"></a> &nbsp; <a
								href="http://www.hostip.info/correct.html?spip=${domain.ipfrom}"
								target="_blank">更正</a> ${domain.ipfrom}: ${domain.domain}:
								${domain.COUNT}</td>
							<td width="500"><table
									width="${domain.COUNT/maxdomain*100}%">
									<tr>
										<td width="" bgcolor="#0033CC">&nbsp;</td>
									</tr>
								</table></td>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>未知的統計</c:otherwise>
		</c:choose>
		<p>
			<c:if test="${Link!=null}">${param.target}</c:if>
		</p>
	</div>
</body>
</html>
