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
	src="include/JoinContest.js?${applicationScope.built }"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		$("div.panel-collapse:first").collapse();
	});
</script>

</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<h2>${vclass.vclassname}</h2>
			<c:if test="${vclass.descript!=''}">
				<div>
					<pre>${vclass.descript}</pre>
				</div>
			</c:if>
		</div>
		<hr>

		<div class="row">
			<div class="col-md-6">
				<%-- 舊的 隨堂測驗顯示 
				<c:forEach var="vcontest" items="${vcontests}" varStatus="varstatus">
					<hr>
					<fieldset>
						<legend>#${vcontest.id} 第
							${fn:length(vcontests)-varstatus.count+1} 堂 - 隨堂測驗 </legend>
						<span> <c:if test="${vcontest.subtitle!=''}">課程大綱：<br />
								<pre>${fn:escapeXml(vcontest.subtitle)}</pre>
							</c:if> <c:if test="${vcontest.isSuspending}">測驗尚未開始！<br />
							</c:if> <c:if test="${vcontest.isStopped}">
								<c:forEach var="problem" items="${vcontest.problems}"
									varStatus="varstatus">#${varstatus.count}. 
                                        <c:set var="problem"
										value="${problem}" scope="request" />
									<jsp:include page="include/div/ProblemDisplay.jsp" />
									<jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
										(${vcontest.scores[varstatus.count-1]}/${vcontest.totalScore })
										<br />
								</c:forEach>
								<br />
								<a href="./ShowContest?contestid=${vcontest.id}"
									class="btn btn-success">檢視隨堂測驗</a>
								<br />
							</c:if> <c:if test="${vcontest.isStarting || vcontest.isRunning}">
								<c:set var="contest" value="${vcontest}" scope="request" />
								<jsp:include page="include/JoinContest.jsp" />
								<br />
							</c:if> <c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
								<div class="TeachersOnly">
									<c:set var="contest" value="${vcontest}" scope="request" />
									<jsp:include page="include/ContestInfo.jsp" />
									<hr />
									<span id="contestid" style="display: none;">${vcontest.id}</span>

									<c:if test="${fn:length(vcontest.problemids)==0}"> 沒有題目 </c:if>
									<c:if test="${fn:length(vcontest.problemids)>0}">
										<c:forEach var="problem" items="${vcontest.problems}"
											varStatus="varstatus">
											<c:set var="problem" value="${problem}" scope="request" />
											<jsp:include page="include/div/ProblemDisplay.jsp" />
											<jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
												 (${vcontest.scores[varstatus.count-1]}/${vcontest.totalScore })<br />
										</c:forEach>
                共( ${fn:length(vcontest.problemids)} 個題目 ) </c:if>
									<br />
								</div>
								<c:set var="contest" value="${vcontest}" scope="request" />
								<jsp:include page="include/ContestToolbar_Bootstrap.jsp" />
							</c:if>
						</span>
					</fieldset>
				</c:forEach>
				 --%>
				<br />
				<div class="row">
					<div class="col-md-12">
						<c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
							<button class="btn btn-success" id="InsertVContest"
								data-vclassid="${param.vclassid }">新增一個隨堂測驗</button>
							<hr>
						</c:if>

						<div class="panel-group" id="accordion" role="tablist"
							aria-multiselectable="true">
							<c:forEach var="contest" items="${vcontests}"
								varStatus="varstatus">
								<div class="panel panel-default">
									<div class="panel-heading" role="tab"
										id="heading${contest.id }">
										<h4 class="panel-title">
											<a href="#contestid_${contest.id}" data-toggle="collapse"
												data-parent="#accordion" aria-expanded="false"
												aria-controls="collapse"> #${contest.id} 第
												${fn:length(vcontests)-varstatus.count+1} 堂 - ${fn:escapeXml(contest.title)} 
												<%-- ${varstatus.count+((pagenum-1)*applicationScope.appConfig.pageSize)}:
												#${contest.id} ${fn:escapeXml(contest.title)} --%>
											</a>
											<%-- BY
											<c:set var="user" value="${contest.owner}" scope="request" />
											<jsp:include page="include/div/UserAccount_TypeA.jsp" /> --%>
										</h4>
									</div>
									<div id="contestid_${contest.id}"
										class="panel-collapse collapse" role="tabpanel"
										aria-labelledby="heading${contest.id }">
										<div class="panel-body">
											<div class="col-md-7">
												<h3>${fn:escapeXml(contest.title)}</h3>
												<pre>${contest.subtitle }</pre>
												<hr />
												<c:if test="${contest.isSuspending}">測驗尚未開始！<br />
												</c:if>
												<c:if test="${contest.isStopped}">
													<c:forEach var="problem" items="${contest.problems}"
														varStatus="varstatus">#${varstatus.count}. 
                                        <c:set var="problem"
															value="${problem}" scope="request" />
														<jsp:include page="include/div/ProblemDisplay.jsp" />
														<jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
                                        (${contest.scores[varstatus.count-1]}/${contest.totalScore })
                                        <br />
													</c:forEach>
													<br />
													<a href="./ShowContest?contestid=${contest.id}"
														class="btn btn-success">檢視隨堂測驗</a>
													<br />
												</c:if>
												<c:if test="${contest.isStarting || contest.isRunning}">
													<c:set var="contest" value="${contest}" scope="request" />
													<jsp:include page="include/JoinContest.jsp" />
													<br />
												</c:if>

												<%-- 												<c:forEach var="problem" items="${contest.problems}">
													<c:set var="problem" value="${problem}" scope="request" />
													<jsp:include page="include/div/ProblemDisplay.jsp" />
													<jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
													<br />
												</c:forEach>
 --%>
											</div>
											<div class="col-md-5">
												<span id="contestid" style="display: none;">${contest.id}</span>
												<c:set var="contest" value="${contest}" scope="request" />
												<jsp:include page="include/ContestInfo.jsp" />
											</div>

											<div class="col-md-12">
												<br />
												<c:set var="contest" value="${contest}" scope="request" />
												<jsp:include page="include/ContestToolbar_Bootstrap.jsp" />
											</div>
										</div>
									</div>
								</div>


								<%--                    <fieldset style="text-align: left; margin: auto; width: 80%;">
                        <legend>
                            ${varstatus.count+((pagenum-1)*applicationScope.appConfig.pageSize)}:
                            <a href="./ShowContest?contestid=${contest.id}">${fn:escapeXml(contest.title)}</a>
                            BY <a href="UserStatistic?account=${contest.owner.account}">${contest.owner}</a>
                            : ${contest.visible }
                        </legend>
                        <span style="float: left;"> <c:forEach var="problem"
                                items="${contest.problems}">
                                <c:set var="problem" value="${problem}" scope="request" />
                                <jsp:include page="include/div/ProblemDisplay.jsp" />
                                <jsp:include page="include/div/ProblemTitle_TypeB.jsp" />
                                <br />
                            </c:forEach>
                        </span>
                        <div class="TeachersOnly" style="float: right; text-align: left;">
                            <span id="contestid" style="display: none;">${contest.id}</span>
                            <c:set var="contest" value="${contest}" scope="request" />
                            <jsp:include page="include/ContestInfo.jsp" />

                            <span>${contest:getPausepoints(sessionScope.onlineUser,
                            contest)}</span>
                            <br /> <br />
                            <c:set var="contest" value="${contest}" scope="request" />
                            <jsp:include page="include/ContestToolbar.jsp" />

                        </div>
                        <br />
                    </fieldset>
                    <br /> --%>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12"></div>
				</div>
			</div>
			<div class="col-md-3">
				<div class="row">
					<h3>題目總表：(共 ${fn:length(vclass.problemids)} 題)</h3>
					<table class="table table-hover">
						<c:forEach var="problem" items="${vclass.problems}"
							varStatus="varstatus">
							<tr>
								<td>#${varstatus.count}</td>
								<td><c:set var="problem" value="${problem}" scope="request" />
									<jsp:include page="include/div/ProblemDisplay.jsp" /> <jsp:include
										page="include/div/ProblemTitle_TypeB.jsp" /> <br /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
			<div class="col-md-3">
				<div class="row">
					<c:if test="${vclass.getIsOwner(sessionScope.onlineUser)}">
						<jsp:include page="include/Modals/Modal_InsertStudents.jsp" />
						<button class="btn btn-success" id="InsertStudents"
							data-toggle="modal" data-target="#Modal_InsertStudents">將
							user 加入本課程</button>
						<hr>
                    或讓 user 主動加入課程。從使用者選單 -> 「加入課程」-> 輸入: <br />
						<c:set var="vclass" value="${vclass}" scope="request" />
						<jsp:include page="include/div/VclassCode.jsp" />
						<!-- <span id="InsertStudents"><a href="#" type="button">將
                                user 加入本課程</a></span> -->
					</c:if>
					<hr>
					<div>學生列表：(共 ${fn:length(students)} 人)</div>
					<table class="table table-hover">
						<c:forEach var="student" items="${students}" varStatus="varstatus">
							<tr>
								<td width="30%">#${varstatus.count} <c:if
										test="${student.isOnline}">
										<i class="fa fa-user" aria-hidden="true" title="在線上"></i>
									</c:if> <c:if test="${!student.isOnline}">
										<i class="fa fa-user-times" aria-hidden="true" title="未上線"></i>
									</c:if> <c:if test="${student.joinedcontestid == 0}">
										<i class="fa fa-minus-square-o" aria-hidden="true"></i>
									</c:if> <c:if test="${student.joinedcontestid != 0}">
										<a href="./ShowContest?contestid=${student.joinedcontestid}"
											class="btn btn-default btn-xs"
											title="正參加 #${student.joinedcontestid} 競賽">${student.joinedcontestid}</a>
									</c:if>
								</td>
								<td><c:set var="user" value="${student.user}"
										scope="request" /> <jsp:include
										page="include/div/UserAccount_TypeA.jsp" /> <c:if
										test="${vclass.getIsOwner(sessionScope.onlineUser)}">
										<br />
										<span class="TeachersOnly" style="display: inline;"
											title="student truename">[${student.truename}] </span>
									</c:if></td>
								<td>
									<button class="btn btn-default btn-xs" id="RemoveStudent"
										title="將學生移出這個課程" data-userid="${student.user.id}"
										data-vclassid="${vclass.id }">
										<span class="fa fa-times"></span>
									</button>
								</td>
								<%-- 								<td width="46%"><span
									style="text-align: left; margin: 10px; width: 40%;"
									title="課程內解題總數"> 共 <a
										href="./ViewVClassStudent?userid=${student.userid}&vclassid=${param.vclassid}">
											${student.ac} </a>AC
								</span></td>
 --%>
							</tr>
						</c:forEach>
					</table>
					<p>&nbsp;</p>
				</div>
			</div>
		</div>
		<%-- 			<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
				<div class="DEBUGGEROnly">
					<a href="./ContestSubmissions?contestid=${contest.id}">ContestSubmissions</a>
					| <a href="./ContestRanking?contestid=${contest.id}">ContestRanking</a>
				</div>
			</c:if>
 --%>
	</div>

	<jsp:include page="include/Footer.jsp" />
</body>
</html>
