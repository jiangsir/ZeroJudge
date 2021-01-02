<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="contest" uri="http://jiangsir.tw/jstl/contest"%>
<%@ taglib prefix="vclass" uri="http://jiangsir.tw/jstl/vclass"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="ShowVClass.js?${applicationScope.built}"></script>
<script type="text/javascript"
	src="include/div/VclassCode.js?${applicationScope.built}"></script>
<script type="text/javascript"
	src="include/ContestToolbar.js?${applicationScope.built }"></script>
<%-- <script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
 --%>
<script type="text/javascript"
	src="include/Modals/Modal_UpdateStudentsComments.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="include/JoinContestButton.js?${applicationScope.built }"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("button[id='showall_panels']").click(function() {
			var panels = $("div.panel-collapse");
			console.log(panels)
			panels.each(function() {
				if ($(this).hasClass('in')) {
					$(this).collapse('hide');
				} else {
					$(this).collapse('show');
				}
			});
		});

	});
</script>

</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<h2>課程名稱: ${vclass.vclassname}</h2>
			<c:set var="user" value="${vclass.owner}" scope="request" />
			<h3>
				開課人員:
				<jsp:include page="include/div/UserAccount_TypeA.jsp" /></h3>
			<c:if test="${vclass.descript!=''}">
				<div>
					<pre>${vclass.descript}</pre>
				</div>
			</c:if>
		</div>
		<hr>

		<div class="row">
			<div class="col-md-6">
				<div class="row">
					<div class="col-md-12">
						<c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
							<%-- 							<button class="btn btn-success" id="InsertVContest"
								data-vclassid="${param.vclassid }" title="InsertVContest">
								新增一個
								<fmt:message key="Contest.VContest" />
							</button>
 --%>
							<jsp:include page="include/Modals/Modal_InsertVContest.jsp" />
							<button data-vclassid="${param.vclassid }"
								class="btn btn-success" data-toggle="modal"
								data-target="#Modal_InsertVContest">
								新增一個
								<fmt:message key="Contest.VContest" />
							</button>
						</c:if>
<%-- 						<c:if test="${vclass.visible_ImportContest(pageContext.session)}">
							<a class="btn btn-danger"
								href="./ImportContest?targetVclassid=${vclass.id }"
								type="button">匯入競賽</a>
						</c:if>
 --%>
						<button class="btn btn-default" id="showall_panels"
							title="展開全部<fmt:message key="Contest.VContest" />">
							<span class="fa fa-bars"></span>
						</button>
						<hr>

						<div class="panel-group" id="accordion" role="tablist"
							aria-multiselectable="true">
							<div id="sortable" data-vclassid="${param.vclassid }">
								<c:forEach var="contest" items="${vcontests}"
									varStatus="varstatus">
									<div class="panel panel-default" id="${contest.id}">
										<div class="panel-heading" role="tab"
											id="heading${contest.id }">
											<h4 class="panel-title">
												<a href="#contestid_${contest.id}" data-toggle="collapse"
													data-parent="#accordion" aria-expanded="false"
													aria-controls="collapse"> ${varstatus.count}.
													${fn:escapeXml(contest.title)}<%-- ${varstatus.count+((pagenum-1)*applicationScope.appConfig.pageSize)}:
												#${contest.id} ${fn:escapeXml(contest.title)} --%>
												</a> <span class="pull-right"> <c:if
														test="${!vclass.getIsOwner(sessionScope.onlineUser)}">
														<c:set var="contest" value="${contest}" scope="request" />
														<c:set var="contestant"
															value="${contest.getContestantByUserid(sessionScope.onlineUser.id)}"
															scope="request" />
														<jsp:include page="include/div/ContestProblems_TypeB.jsp" />
													</c:if>
												</span>
												<%-- BY
											<c:set var="user" value="${contest.owner}" scope="request" />
											<jsp:include page="include/div/UserAccount_TypeA.jsp" /> --%>
												<span class="pull-right"><c:if
														test="${contest.getIsSuspending()}">
														<span class="glyphicon glyphicon-play" title="準備中">
														</span>
													</c:if> <c:if test="${contest.getIsPausing()}">
														<span class="glyphicon glyphicon-chevron-right"
															title="暫停中"> </span>
													</c:if> <c:if test="${contest.getIsRunning()}">
														<span class="glyphicon glyphicon-pause" title="進行中">
														</span>
													</c:if> <c:if test="${contest.getIsStopped()}">
													</c:if> <c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
														<jsp:include
															page="./include/Modals/Modal_ShowContestSettings.jsp">
															<jsp:param value="Contestid: ${contest.id}" name="title" />
															<jsp:param value="${contest.id}" name="contestid" />
														</jsp:include>
														<button type="button" class="btn btn-default btn-xs"
															data-toggle="modal"
															data-target="#Modal_ShowContestSettings_${contest.id}"
															title="JSON Settings" id="btn_ContestSettings"
															data-contestid="${contest.id}" title="Contest JSON設定">
															<i class="glyphicon glyphicon-cog" aria-hidden="true"></i>
														</button>
														<%-- 													<button id="cloneVContestById" data-vclassid="${vclass.id}"
														data-contestid="${contest.id}"
														class="btn btn-default btn-xs">
														<i class="fa fa-clone"
															title="再製一個<fmt:message key="Contest.VContest" /> #${contest.id}"></i>
													</button>
 --%>
														<button id="removeVContest" class="btn btn-default btn-xs"
															title="將<fmt:message key="Contest.VContest" />移除"
															data-contestid="${contest.id }">
															<i class="fa fa-times"></i>
														</button>
													</c:if> </span>
											</h4>
										</div>
										<div id="contestid_${contest.id}"
											class="panel-collapse collapse" role="tabpanel"
											aria-labelledby="heading${contest.id }">
											<div class="panel-body">
												<div class="col-md-7">
													<%-- 												<h4>${fn:escapeXml(contest.title)}</h4>
 --%>
													<pre>${contest.subtitle }</pre>
													<hr />
													<c:set var="contest" value="${contest}" scope="request" />
													<jsp:include page="include/div/ContestProblems.jsp" />
													<c:set var="contest" value="${contest}" scope="request" />
													<jsp:include page="include/JoinContestButton.jsp" />

												</div>
												<div class="col-md-5">
													<span id="contestid" style="display: none;">${contest.id}</span>
													<c:set var="contest" value="${contest}" scope="request" />
													<jsp:include page="include/ContestInfo.jsp" />
												</div>
												<c:if
													test="${vclass.getIsOwner(sessionScope.onlineUser) || sessionScope.onlineUser.isHigherEqualThanMANAGER}">
													<div class="col-md-12">
														<br />
														<c:set var="contest" value="${contest}" scope="request" />
														<jsp:include page="include/ContestToolbar_Bootstrap.jsp" />
													</div>
												</c:if>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
							<br>
							<c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
								<hr>
								<button class="btn btn-success" id="save_SortableVContest"
									data-vclassid="${vclass.id}">
									儲存
									<fmt:message key="Contest.VContest" />
									順序
								</button>
								<div class="breadcrumb">
									※ 請用拖曳來調整
									<fmt:message key="Contest.VContest" />
									順序。
								</div>
							</c:if>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12"></div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="row">
					<c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
						<button type="button" class="btn btn-primary" data-toggle="modal"
							data-target="#Modal_UpdateStudentsComments"
							data-vclassid="${param.vclassid}">更新「學生註解」</button>
						<jsp:include
							page="include/Modals/Modal_UpdateStudentsComments.jsp" />
						<hr>
						<div>請 user 參加課程。從使用者選單 -> 「參加課程」-> 輸入:</div>
						<c:set var="vclass" value="${vclass}" scope="request" />
						<jsp:include page="include/div/VclassCode.jsp" />
						<!-- <span id="InsertStudents"><a href="#" type="button">將
                                user 加入本課程</a></span> -->
					</c:if>
					<hr>
					<div>
						學生列表：(共 ${fn:length(students)} 人)
						<button class="btn btn-default btn-sm"
							id="showall_ContestProblems_TypeB" title="展開全部學生的解題結果">
							<span class="fa fa-bars"></span>
						</button>
					</div>
					<br />
					<table class="table table-hover">
						<c:forEach var="student" items="${students}" varStatus="varstatus">
							<!-- 加回 tbody -->
							<tbody>
								<tr>
									<td><fmt:formatNumber type="number" pattern="00"
											value="${varstatus.count}" /></td>
									<td><c:set var="user" value="${student.user}"
											scope="request" /> <jsp:include
											page="include/div/UserAccount_TypeA.jsp" /></td>
									<td><span class="TeachersOnly" style="display: inline;"
										title="學生註解">${student.comment } </span></td>
									<td><c:if test="${student.isOnline}">
											<button type="button" class="btn btn-success btn-xs">ON</button>
										</c:if> <c:if test="${!student.isOnline}">
											<button type="button" class="btn btn-danger btn-xs">off</button>
										</c:if> <c:if test="${student.joinedcontestid == 0}">
											<i class="fa fa-minus-square-o" aria-hidden="true"
												title="尚未參加任何<fmt:message key="Contest.Contest" />"></i>
										</c:if> <c:if test="${student.joinedcontestid != 0}">
											<a href="./ShowContest?contestid=${student.joinedcontestid}"
												class="btn btn-default btn-xs"
												title="正參加 #${student.joinedcontestid} <fmt:message key="Contest.Contest" />">${student.joinedcontestid}</a>
										</c:if>
										<button class="btn btn-default btn-xs"
											id="show_ContestProblems_TypeB" data-vclassid="${vclass.id }"
											data-userid="${student.userid }" title="展開解題結果">
											<span class="fa fa-bars"></span>
										</button> <c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
											<button class="btn btn-default btn-xs" id="RemoveStudent"
												title="將學生移出這個課程" data-userid="${student.user.id}"
												data-vclassid="${vclass.id }">
												<span class="fa fa-times"></span>
											</button>
										</c:if></td>
								</tr>
								<c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
									<tr id="ContestProblems_TypeB" style="display: none">
										<td colspan="4"></td>
									</tr>
								</c:if>
							</tbody>
						</c:forEach>
					</table>
					<p>&nbsp;</p>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="include/Footer.jsp" />
</body>
</html>
