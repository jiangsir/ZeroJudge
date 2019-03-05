<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>

<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<strong>常見的程式錯誤說明</strong><br />
		<table width="90%" cellpadding="5" class="content_individual">
			<tr>
				<td valign="top">*</td>
				<td><p>void main(){</p>
					<p>}</p>
					<p>會導致 CE?</p></td>
			</tr>
			<tr>
				<td valign="top">&nbsp;</td>
				<td>因為系統是以 GNU G++ 以及 GCC 來編譯 C/C++ 程式，因此必須遵循 ANSI/ISO
					的標準，也就是主程式必須回傳一個整數。具體寫法請參考 a001 的範例程式。</td>
			</tr>
			<tr>
				<td valign="top">*</td>
				<td>long long int 的問題</td>
			</tr>
			<tr>
				<td valign="top">&nbsp;</td>
				<td>用 %lld 取代 %I64d</td>
			</tr>
		</table>
	</div>
	<br />
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
