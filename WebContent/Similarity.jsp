<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<table align="center">
			<tr>
				<td width="50%">要比較的程式碼</td>
				<td width="50%">他人的程式碼</td>
			</tr>
			<tr>
				<td colspan="2"><br /> <br /> 差異性報告(sdiff):
					差異行數：${diff.resultlinenum} | 程式行數：${diff.baselinenum} | 相似性：<c:choose>
						<c:when test="${diff.resultlinenum>=diff.baselinenum}">0%</c:when>
						<c:otherwise>
							<fmt:formatNumber
								value="${(diff.baselinenum-diff.resultlinenum)/diff.baselinenum}"
								type="percent" />
						</c:otherwise>
					</c:choose> <pre style="font-size: 14px;">${fn:escapeXml(diff.compareresult)}
  </pre></td>
			</tr>
			<tr>
				<td style="border-right: dotted; padding-right: 10px;">basecodeid
					: ${param.basecodeid} : ${basecode.account}<br /> <pre>${fn:escapeXml(basecode.code)}</pre>
				</td>
				<td style="padding-left: 10px;">comparecodeid :
					${param.comparecodeid} : ${comparecode.account}<br /> <pre>${fn:escapeXml(comparecode.code)}</pre>
				</td>
			</tr>
		</table>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
