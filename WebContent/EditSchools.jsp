<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
<%-- <script type="text/javascript"
	src="include/dialog/CheckSchool.js?${applicationScope.built }"></script>
 --%>
<script type="text/javascript"
	src="./EditSchools.js?${applicationScope.built }"></script>
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<div class="contestbox">
				<p>* 學校名稱及資料必須經由站長確認過後才會出現在『校際排名』中。</p>
			</div>
			<hr></hr>

			<jsp:include page="include/Modals/Modal_EditSchool.jsp">
				<jsp:param name="action" value="InsertSchool" />
				<jsp:param name="schoolid" value="0" />
			</jsp:include>

			<form class="form-inline" role="search" method="POST"
				action="./EditSchools">
				<button type="button" class="btn btn-primary" data-toggle="modal"
					data-target="#Modal_EditSchool_0" data-action="InsertSchool">新增一個學校</button>
				<a href="./EditSchools" class="btn btn-primary" type="button">列出全部學校</a>
				<div class="form-group">
					<div class="input-group">
						<input type="text" class="form-control" placeholder="搜尋學校關鍵字..."
							name="searchschool"> <span class="input-group-btn">
							<button class="btn btn-default" type="submit">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</button>
						</span>
					</div>
				</div>
			</form>
			<%-- 			<button id="insertSchool">
				新增學校
				<jsp:include page="include/dialog/CheckSchool.jsp" />
			</button>
 --%>
			<br>
			<table class="table table-hover">
				<tr>
					<th style="text-align: left;">學校</th>
				</tr>
				<c:forEach var="school" items="${schools}">
					<tr>
						<td id="${school.id}"><span style="float: left">${school.id}.
								<a href="./Ranking?tab=tab03&schoolid=${school.id}">${fn:escapeXml(school.schoolname)}</a>
								<%-- [${school.schoolac}AC/${school.count}人] --%>
						</span>
							<div style="float: right">

								<div class="btn-group" role="group" aria-label="...">
									<c:if test="${school.isChecked}">
										<button type="button" class="btn btn-default">
											<span class="glyphicon glyphicon-ok-circle"></span>
										</button>
									</c:if>
									<a class="btn btn-default" href="${school.url}" role="button"><span
										class="glyphicon glyphicon-home"></span></a>

									<c:set var="school" value="${school}" scope="request" />
									<jsp:include page="include/Modals/Modal_EditSchool.jsp">
										<jsp:param name="action" value="UpdateSchool" />
										<jsp:param name="schoolid" value="${school.id }" />
									</jsp:include>
									<button type="button" class="btn btn-default"
										data-toggle="modal"
										data-target="#Modal_EditSchool_${school.id}">
										<span class="glyphicon glyphicon-pencil"></span>
									</button>

									<%-- 								<c:set var="vclass" value="${vclass }" scope="request" />
									<jsp:include page="include/Modals/Modal_UpdateVClass.jsp" />
	 --%>
									<button type="button" class="btn btn-default"
										data-toggle="modal" data-target="#Modal_confirm"
										data-title="確定要關閉這個學校(${school.schoolname})嗎?"
										data-content="確定要關閉這個學校(${school.schoolname})嗎?"
										data-type="POST" data-url="./Manager.api"
										data-qs="action=DeleteSchool&schoolid=${school.id}">
										<span class="glyphicon glyphicon-remove"></span>
									</button>

								</div>
							</div></td>
					</tr>
				</c:forEach>
			</table>
			<br />
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
			<br />
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
