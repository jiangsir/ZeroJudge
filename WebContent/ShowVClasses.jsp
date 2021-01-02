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
	src="ShowVClasses.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="EditVClasses.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="include/Modals/Modal_InsertUsers.js?${applicationScope.built }"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		$('#myTab a:first').tab('show')

	});
</script>

</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<ul id="myTab" class="nav nav-tabs" role="tablist">
				<c:if test="${sessionScope.onlineUser.isHigherEqualThanMANAGER }">
					<li role="presentation" style="font-size: 1.3em;"><a
						href="#all_vclasses" aria-controls="all_vclasses" role="tab"
						data-toggle="tab"><i class="fa fa-certificate"></i> 所有的課程</a></li>
				</c:if>
				<c:if test="${sessionScope.onlineUser.VClassManager}">
					<li role="presentation" style="font-size: 1.3em;"><a
						href="#owned_vclasses" aria-controls="owned_vclasses" role="tab"
						data-toggle="tab">開設的課程</a></li>
				</c:if>
				<li role="presentation" style="font-size: 1.3em;"><a
					href="#joined_vclasses" aria-controls="joined_vclasses" role="tab"
					data-toggle="tab">參加的課程</a></li>
				<c:if
					test="${sessionScope.onlineUser.accessible_EditVClassTemplate }">
					<li role="presentation" style="font-size: 1.3em;"><a
						href="#vclasstemplates" aria-controls="vclasstemplates" role="tab"
						data-toggle="tab">課程模板</a></li>
				</c:if>
			</ul>

			<div class="tab-content">
				<div role="tabpanel" class="tab-pane" id="vclasstemplates">
					<!-- 公開的課程模板 -->
					<br />
					<c:if
						test="${sessionScope.onlineUser.accessible_EditVClassTemplate }">
						<a href="./EditVClassTemplate" type="button"
							class="btn btn-primary">設計公開"課程模板"</a>
					</c:if>
					<hr>
					<h3>
						您(
						<c:set var="user" value="${sessionScope.onlineUser}"
							scope="request" />
						<jsp:include page="include/div/UserAccount_TypeA.jsp" />)所設計的「課程模板」
					</h3>
					<table class="table table-hover">
						<tr>
							<td class="col-md-1">模板名稱</td>
							<td class="col-md-2">設計者</td>
							<td class="col-md-7">課程描述</td>
							<td class="col-md-2">操作</td>
						</tr>
						<c:forEach var="template" items="${vclassTemplates}">
							<tr>
								<td><h3>${template.name}</h3></td>
								<td>${template.owner.account}</td>
								<td><c:forEach var="vcontestTemplate"
										items="${template.getTemplatesByName()}" varStatus="status">
										<h4>${status.count}.${vcontestTemplate.title}</h4>
										<div>${vcontestTemplate.descript}</div>
										<%-- 						problemids: ${vcontestTemplate.problemids}<br>
 --%>
										<c:forEach var="problem" items="${vcontestTemplate.problems}">
											<c:set var="problem" value="${problem}" scope="request" />
											<jsp:include page="include/div/ProblemTitle.jsp" /><br />
										</c:forEach>
										<hr>
									</c:forEach></td>
								<td><a href="./EditVClassTemplate?id=${template.id }"
									class="btn btn-default">編輯這個模板</a>
									<button class="btn btn-default" id="deleteTemplates"
										data-templateid="${template.id}">刪除這個模板</button></td>
							</tr>
						</c:forEach>
					</table>
				</div>

				<div role="tabpanel" class="tab-pane" id="owned_vclasses">
					<br />
					<c:if
						test="${sessionScope.onlineUser.accessible_CreateVClassByTemplate}">
						<button type="button" class="btn btn-primary" data-toggle="modal"
							data-target="#Modal_CreateVClassByTemplate">開設一門新課程</button>
						<jsp:include
							page="include/Modals/Modal_CreateVClassByTemplate.jsp">
							<jsp:param value="0" name="vclassid" />
							<jsp:param value="CreateVClassByTemplate" name="action" />
						</jsp:include>
					</c:if>
					<hr>
					<h3>
						您(
						<c:set var="user" value="${sessionScope.onlineUser}"
							scope="request" />
						<jsp:include page="include/div/UserAccount_TypeA.jsp" />)所開設的課程
					</h3>
					<table class="table table-hover">
						<tr>
							<td class="col-md-1">
								<!-- 編號 -->
							</td>
							<td class="col-md-4">課程名稱</td>
							<td class="col-md-3">課程說明</td>
							<td class="col-md-2">開課教師</td>
							<td class="col-md-2">操作</td>
						</tr>
						<c:forEach var="vclass" items="${owned_vclasses}">
							<tr>
								<td>
									<%-- ${vclass.id} --%>
								</td>
								<td><a href="./ShowVClass?vclassid=${vclass.id}"> <c:if
											test="${vclass.visible==0}">
											<del>${vclass.vclassname}</del>
										</c:if> <c:if test="${vclass.visible!=0}">${vclass.vclassname}</c:if>
								</a> <%-- <c:set var="vclass" value="${vclass }" scope="request" /> <jsp:include
										page="include/div/VclassCode.jsp" /> --%></td>
								<td><c:if test="${vclass.visible==0}">
										<del>${vclass.descript}</del>
									</c:if> <c:if test="${vclass.visible!=0}">${vclass.descript}</c:if></td>
								<td><c:set var="user" value="${vclass.owner}"
										scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /></td>
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
											size="${fn:length(vclass.vclasscode)+1}" title="課程代碼"
											style="font-family: Consolas, 'Courier New', monospace;" readonly>
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
					</table>

				</div>

				<div role="tabpanel" class="tab-pane" id="joined_vclasses">
					<br />
					<button type="button" class="btn btn-primary" data-toggle="modal"
						data-target="#Modal_JoinVclassid">
						<span class="fa fa-plus" aria-hidden="true"></span> 參加課程
					</button>
					<hr>
					<h3>
						您(
						<c:set var="user" value="${sessionScope.onlineUser}"
							scope="request" />
						<jsp:include page="include/div/UserAccount_TypeA.jsp" />)已參加的課程
					</h3>
					<table class="table table-hover">
						<tr>
							<td class="col-md-1">
								<!-- 編號 -->
							</td>
							<td class="col-md-4">課程名稱</td>
							<td class="col-md-3">課程說明</td>
							<td class="col-md-2">開課教師</td>
						</tr>
						<c:forEach var="vclass" items="${joined_vclasses}">
							<tr>
								<td>
									<%-- ${vclass.id} --%>
								</td>
								<td><a href="./ShowVClass?vclassid=${vclass.id}"> <c:if
											test="${vclass.visible==0}">
											<del>${vclass.vclassname}</del>
										</c:if> <c:if test="${vclass.visible!=0}">${vclass.vclassname}</c:if>
								</a> <%-- <c:set var="vclass" value="${vclass }" scope="request" /> <jsp:include
										page="include/div/VclassCode.jsp" /> --%></td>
								<td><c:if test="${vclass.visible==0}">
										<del>${vclass.descript}</del>
									</c:if> <c:if test="${vclass.visible!=0}">${vclass.descript}</c:if></td>
								<td><c:set var="user" value="${vclass.owner}"
										scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /></td>
							</tr>
						</c:forEach>
					</table>

				</div>
				<div role="tabpanel" class="tab-pane" id="all_vclasses">
					<c:if test="${sessionScope.onlineUser.isMANAGER }">
						<hr>
						<jsp:include page="include/div/DivInsertUsers.jsp" />
						<jsp:include page="include/Modals/Modal_InsertUsers.jsp" />

						<hr>
						<h3>
							您(
							<c:set var="user" value="${sessionScope.onlineUser}"
								scope="request" />
							<jsp:include page="include/div/UserAccount_TypeA.jsp" />)
							可觀察所有課程
						</h3>
						<table class="table table-hover">
							<tr>
								<td class="col-md-1">
									<!-- 編號 -->
								</td>
								<td class="col-md-4">課程名稱</td>
								<td class="col-md-3">課程說明</td>
								<td class="col-md-2">開課教師</td>
							</tr>
							<c:forEach var="vclass" items="${all_vclasses}">
								<tr>
									<td>
										<%-- ${vclass.id} --%>
									</td>
									<td><a href="./ShowVClass?vclassid=${vclass.id}"> <c:if
												test="${vclass.visible==0}">
												<del>${vclass.vclassname}</del>
											</c:if> <c:if test="${vclass.visible!=0}">${vclass.vclassname}</c:if>
									</a> <%-- <c:set var="vclass" value="${vclass }" scope="request" /> <jsp:include
										page="include/div/VclassCode.jsp" /> --%></td>
									<td><c:if test="${vclass.visible==0}">
											<del>${vclass.descript}</del>
										</c:if> <c:if test="${vclass.visible!=0}">${vclass.descript}</c:if></td>
									<td><c:set var="user" value="${vclass.owner}"
											scope="request" /> <jsp:include
											page="include/div/UserAccount_TypeA.jsp" /></td>
								</tr>
							</c:forEach>
						</table>
					</c:if>

				</div>
			</div>

		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
