<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<table class="table table-hover">
	<tr>
		<th scope="col">tab id</th>
		<th scope="col">tab name</th>
		<th scope="col">descript</th>
		<th scope="col">排序</th>
		<th scope="col"></th>
	</tr>
	<c:forEach var="tab" items="${applicationScope.appConfig.problemtabs}"
		varStatus="varstatus">
		<tr id="tab">
			<td width="15%"><input type="text" name="tabid"
				style="width: 100%" value="${tab.id}" /></td>
			<td width="15%"><input type="text" name="tabname"
				style="width: 100%" value="${tab.name }" /></td>
			<td width="30%"><input type="text" name="tabdescript"
				style="width: 100%" value="${tab.descript }" /></td>
			<td><c:forEach var="i" begin="1" end="3" step="1">${i}. <select
						name="orderby" orderby="${tab.orderby}">
						<option value="">---</option>
						<option value="problemid ASC">problemid欄位順序</option>
						<option value="problemid DESC">problemid欄位倒序</option>
						<option value="sortable ASC">sortable 欄位順序</option>
						<option value="sortable DESC">sortable 欄位倒序</option>
						<option value="reference ASC">出處欄位順序</option>
						<option value="reference DESC">出處欄位倒序</option>
						<option value="updatetime ASC">更新日期順序</option>
						<option value="updatetime DESC">更新日期倒序</option>
					</select>
					<br />
				</c:forEach></td>
			<td>
				<%-- 				<div id="delete_confirm"
					style="cursor: default; padding: 10px; display: none; text-align: left;">
					刪除這個「<span id="delete_tabid"></span>」標籤後，這些題目標籤要改成<br />
					<c:forEach var="renametotab"
						items="${applicationScope.appConfig.problemtabs}">
						<input type="radio" name="renametotabid" value="${renametotab.id}" />${renametotab.id}<br />
					</c:forEach>

				</div>
 --%>
				<button type="button" class="btn btn-default btn-xs"
					id="modal_deleteProblemTab" data-toggle="modal"
					data-target="#Modal_deleteProblemtab_${tab.id}" data-tabid="${tab.id}"
					data-action="deleteProblemtab" title="刪除">
					<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
				</button> <!-- <a id="deleteTab">刪除</a> --> <c:set var="tabid"
					value="${tab.id}" scope="request" /> <jsp:include
					page="../Modals/Modal_DeleteProblemtab.jsp" />
			</td>
		</tr>
	</c:forEach>
</table>
<button type="button" class="btn btn-primary" id="addTab">新增標籤</button>

<!-- <p>@TODO: tab 刪除時需詢問將原 tab 改為哪一個 tab</p> -->
<hr>
