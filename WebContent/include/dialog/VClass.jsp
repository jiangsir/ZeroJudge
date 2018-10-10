<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="VClass_dialog"
	style="cursor: default; padding: 10px; display: none;">
	<form class="form_VClass">
		<input id="vclassid" name="vclassid" type="hidden"
			value="${vclass.id}" />
		<table align="center">
			<tr>
				<td width="112">課程名稱：</td>
				<td width="351"><input name="vclassname" type="text"
					id="vclassname" value="${vclass.vclassname }" size="50"
					maxlength="50" /></td>
			</tr>
			<tr>
				<td>課程說明：</td>
				<td><textarea name="descript" cols="50" rows="5" id="descript">${vclass.descript }</textarea></td>
			</tr>
		</table>
	</form>
</div>
