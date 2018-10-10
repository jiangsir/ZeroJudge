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
<script type="text/javascript"
	src="./jscripts/jquery.timeout.interval.idle.js"></script>

<script type="text/javascript" src="./jscripts/jquery.query-2.1.7.js"></script>
<script type="text/javascript"
	src="TestRunning.js?${applicationScope.built }"></script>
</head>
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />
<%-- <jsp:useBean id="systemconfigBean"
	class="tw.zerojudge.Beans.SystemConfigBean" />
 --%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<p>
			<br />
		</p>
		<p align="left">
			<input name="userlanguage" type="text"
				value="${applicationScope.CacheUsers[sessionScope.onlineUser.account].userlanguage}"
				style="display: none;" />
			<fmt:message key="Problem.ProgramLanguage" />
			：<br />
			<fmt:message key="Problem.ProgramLanguage" />
			：<br />
			<c:forEach var="compiler"
				items="${applicationScope.appConfig.serverConfig.enableCompilers}">
				<input name="language" type="radio" value="${compiler.language}" />
				<span style="font-weight: bold; font-size: large">${compiler.language}</span>: ${compiler.version}<br />
			</c:forEach>
		</p>
		<p align="center">
			本測試功能提供您測試系統編譯器與您電腦上安裝之編譯器的差別。<br /> 如: C/C++ 在系統上的編譯器為 GCC ，在
			Windows 上面最常見到的移植版則為 MinGW, 但雖然為移植版，但是仍然許多重要的差異。比如
			printf(&quot;I64&quot;); sqrt(double, double); 等。 <br /> <br />
			請將您想要測試的程式碼貼在下方：
		</p>
		<textarea name="code" cols="80" rows="15" id="code"></textarea>
		<table width="80%" border="0" align="center">
			<tr>
				<td width="45%" scope="col"><p>請貼上想進行測試的輸入測資：</p> <textarea
						name="indata" cols="50" rows="20" id="indata"></textarea></td>
				<td width="10%" scope="col"></td>
				<td width="45%" scope="col"><p>結果：</p> <img
					src="images/exec18.png" title="編譯並執行" name="execute" id="execute"
					style="cursor: pointer" /><img src="images/Spinner.gif"
					id="waiting" style="display: none;" />
					<div
						style="background: #000000; color: #FFFFFF; width: 100%; height: 330px;">
						<pre id="outdata"></pre>
						<span
							style="text-decoration: blink; font: 'Courier New', Courier, monospace"><strong>_</strong></span>
					</div></td>
			</tr>
		</table>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
