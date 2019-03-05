<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="error_dialog"
	style="cursor: default; padding: 10px; text-align: center; display: none;">
	<h2></h2>
</div>


<div id="InsertUsers_dialog"
	style="cursor: default; padding: 10px; display: none;">
	請依據一下格式指定要新增的使用者。<br /> 帳號,匿稱,真實姓名,密碼明碼,生日,E-mail,學校 id<br /> 例：
	Tommy091,Tommy,王湯米,tommy1234,1990,tommy091@gmail.com,0<br /> <br />
	<textarea id="userscripts" name="userscripts" cols="80" rows="25">#Tommy091,Tommy,王湯米,tommy1234,1990,tommy091@gmail.com,0
</textarea>
	<br />
</div>
