<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/VClass.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/VclassCode.js?${applicationScope.built}"></script>

<script type="text/javascript"
	src="include/dialog/InsertUsers.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="EditVClasses.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/Modals/Modal_InsertUsers.js?${applicationScope.built }"></script>

</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<button type="button" class="btn btn-primary" data-toggle="modal"
				data-target="#Modal_EditVClass_0">新開一門課</button>
			<jsp:include page="include/Modals/Modal_EditVClass.jsp">
				<jsp:param value="0" name="vclassid" />
				<jsp:param value="InsertVClass" name="action" />
			</jsp:include>
			<%-- 			<button type="submit" class="btn btn-primary" id="InsertVClass"><jsp:include
					page="include/dialog/VClass.jsp" />
				新開一門課
			</button>
 --%>
			<%-- 			<button id="InsertVClass">
				<jsp:include page="include/dialog/VClass.jsp" />
				新開一門課
			</button>
 --%>
			<jsp:include page="include/div/DivInsertUsers.jsp" />
			<jsp:include page="include/Modals/Modal_InsertUsers.jsp" />

			<br /> <br /> 教師所有已開的課程
			<table class="table table-hover">
				<tr>
					<td class="col-md-1">編號</td>
					<td class="col-md-4">課程名稱</td>
					<td class="col-md-2">開課教師</td>
					<td class="col-md-3">課程說明</td>
					<td class="col-md-2">操作</td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(vclasses)!=0}">
						<c:forEach var="vclass" items="${vclasses}">
							<tr>
								<td>${vclass.id}</td>
								<td><a href="./ShowVClass?vclassid=${vclass.id}"> <c:if
											test="${vclass.visible==0}">
											<del>${vclass.vclassname}</del>
										</c:if> <c:if test="${vclass.visible!=0}">${vclass.vclassname}</c:if>
								</a> <%-- <c:set var="vclass" value="${vclass }" scope="request" /> <jsp:include
										page="include/div/VclassCode.jsp" /> --%></td>
								<td><c:set var="user" value="${vclass.owner}"
										scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /></td>
								<td><c:if test="${vclass.visible==0}">
										<del>${vclass.descript}</del>
									</c:if> <c:if test="${vclass.visible!=0}">${vclass.descript}</c:if></td>
								<td>
									<div class="btn-group btn-group-sm" role="group"
										aria-label="...">
										<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
											<button type="button" class="btn btn-default"
												data-toggle="modal" data-target="#Modal_confirm"
												data-title="重新計算"
												data-content="確定要對這個課程(${vclass.vclassname}) 重新計算嗎?"
												data-type="GET" data-url="./RebuiltVClass"
												data-qs="vclassid=${vclass.id}" title="重新計算">
												<span class="glyphicon glyphicon-refresh"></span>
											</button>
										</c:if>
										<input type="text" class="btn btn-default"
											value="${vclass.vclasscode }"
											size="${fn:length(vclass.vclasscode)+1}" title="課程代碼" style="font-family: Consolas, 'Courier New', monospace;" readonly>
										<button class="btn btn-default" type="button"
											id="RenewVclassCode" data-vclassid="${vclass.id }">
											<i class="fa fa-repeat" aria-hidden="true" title="重新產生「課程代碼」"></i>
										</button>
										<button type="button" class="btn btn-default"
											data-toggle="modal"
											data-target="#Modal_EditVClass_${vclass.id }" title="編輯這個課程">
											<span class="glyphicon glyphicon-pencil"></span>
										</button>
										<c:set var="vclass" value="${vclass }" scope="request" />
										<jsp:include page="include/Modals/Modal_EditVClass.jsp">
											<jsp:param value="${vclass.id }" name="vclassid" />
											<jsp:param value="UpdateVClass" name="action" />
										</jsp:include>
										<button type="button" class="btn btn-default"
											data-toggle="modal" data-target="#Modal_confirm"
											data-content="確定要關閉這個課程(${vclass.vclassname})嗎?"
											data-type="GET" data-url="./DeleteVClass"
											data-qs="vclassid=${vclass.id}" title="關閉這個課程">
											<span class="glyphicon glyphicon-remove"></span>
										</button>
									</div> 
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="12"><div align="center">
									<fmt:message key="NO_DATA" />
								</div></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
			<br /> <br />
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
