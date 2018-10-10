<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="solution" uri="http://jiangsir.tw/jstl/solution"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div id="CheckSchool_dialog"
	style="cursor: default; padding: 10px; display: none;">
	名稱及網址均空白的話，即代表刪除該學校資料<br />
	<form id="form_checkschool">
		<table align="center">
			<tr>
				<td width="112">學校名稱：</td>
				<td width="351"><input id="schoolid" name="schoolid"
					type="hidden" value="${school.id }" /> <input
					name="schoolname" type="text" id="schoolname"
					value="${fn:escapeXml(school.schoolname)}" size="50" /></td>
			</tr>
			<tr>
				<td>學校網址：</td>
				<td><input name="url" type="text" id="url"
					value="${school.url}" size="50"></td>
			</tr>
			<tr>
				<td>學校 Logo：</td>
				<td><input name="imgsrc" type="text" id="imgsrc"
					value="${school.imgsrc}" size="50" /></td>
			</tr>
			<tr>
				<td>簡述:</td>
				<td><input name="descript" type="text" id="descript"
					value="${school.descript}" size="50" maxlength="50" /></td>
			</tr>
			<tr>
				<td>Check：</td>
				<td><input name="checkid" type="checkbox" id="checkid"
					value="1" schoolcheckid="${school.checkid }" />
					校名及網址確認正確，確認之後會出現在『校際排名』當中。</td>
			</tr>
		</table>
	</form>
</div>
