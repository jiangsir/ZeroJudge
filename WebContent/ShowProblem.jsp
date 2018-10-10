<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="ShowProblem.js?${applicationScope.built }"></script>
<%-- <script type="text/javascript"
	src="include/dialog/ProblemSetting.js?${applicationScope.built }"></script>
 --%>
<%-- <script type="text/javascript"
	src="include/dialog/Testjudge.js?${applicationScope.built }"></script>
 --%>
<%-- <script type="text/javascript"
	src="include/dialog/SubmitCode.js?${applicationScope.built }"></script>
 --%>
<%--  <script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
 --%>
<%-- <script type="text/javascript"
	src="include/dialog/ShowDetail.js?${applicationScope.built }"></script>
 --%>
<%-- <script type="text/javascript"
	src="include/dialog/ShowCode.js?${applicationScope.built }"></script>
 --%>

<script type="text/javascript"
	src="include/Modals/Modal_ProblemSetting.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/ProblemToolbar.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/DivProblemStatusInfo.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/ProblemTags.js?${applicationScope.built }"></script>


<script type="text/x-mathjax-config">
MathJax.Hub.Config({
  tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}
});
</script>
<!-- <script type="text/javascript"
	src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
	
</script>
 -->
<script type="text/javascript"
	src="./jscripts/MathJax-2.5-mini/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
	
</script>

<script type="text/javascript">
	jQuery(document).ready(function() {

	});
</script>


</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<a href="./Problems?tabid=${problem.tabid}">回『${problem.tab.name}』</a>
			</div>
			<div class="col-md-6">
				<c:set var="problem" value="${problem}" scope="request" />
				<jsp:include page="include/div/ProblemDisplay.jsp" />
				<div class="h1">${problem.problemid}:
					<span id="problem_title">${fn:escapeXml(problem.title)}</span>
				</div>
			</div>
			<div class="col-md-3">
				<fmt:message key="Problem.Tags" />
				:
				<c:set var="problem" value="${problem}" scope="request" />
				<jsp:include page="include/div/ProblemTags.jsp" /><br />
				<fmt:message key="Problem.Rate" />
				:
				<c:choose>
					<c:when test="${problem.submitusers==0}">0% </c:when>
					<c:otherwise>
						<fmt:formatNumber value="${problem.acusers/problem.submitusers}"
							type="percent" />
					</c:otherwise>
				</c:choose>
				(${problem.acusers}
				<fmt:message key="Problem.Users" />
				/ ${problem.submitusers}
				<fmt:message key="Problem.Users" />
				) (非即時)<br /> 評分方式：
				<div class="btn btn-warning btn-xs">${problem.judgemode}</div>
				<br /> 最近更新 :
				<fmt:formatDate value="${problem.updatetime}"
					pattern="yyyy-MM-dd HH:mm" />
				<br /> <br />
				<c:set var="problem" value="${problem}" scope="request" />
				<jsp:include page="include/ProblemToolbar_Bootstrap.jsp" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<%-- 				<strong> <fmt:message key="Problem.Content" /> ：
				</strong>
				<div id="problem_content" class="problembox">${problem.content}
				</div>
 --%>
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<fmt:message key="Problem.Content" />
							</div>
							<div class="panel-body">
								<div id="problem_content" class="problembox">${problem.content}
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- 				<div class="row">
					<div class="col-md-6"></div>
					<div class="col-md-6"></div>
				</div>
 -->
				<!-- ///////
 -->
				<div class="row">
					<div class="col-md-9">
						<div class="row">
							<div class="col-md-6">
								<div class="panel panel-default">
									<div class="panel-heading">
										<fmt:message key="Problem.Input" />
									</div>
									<div class="panel-body">
										<div id="problem_theinput" class="problembox">${problem.theinput}</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="panel panel-default">
									<div class="panel-heading">
										<fmt:message key="Problem.Output" />
									</div>
									<div class="panel-body">
										<c:if test="${problem.theoutput!=null}">
											<div id="problem_theoutput" class="problembox">${problem.theoutput}</div>
										</c:if>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="panel panel-default">
									<div class="panel-heading">
										<fmt:message key="Problem.SampleInput" />
									</div>
									<div class="panel-body">
										<div class="problembox">
											<pre>${fn:escapeXml(problem.sampleinput)}</pre>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="panel panel-default">
									<div class="panel-heading">
										<fmt:message key="Problem.SampleOutput" />
									</div>
									<div class="panel-body">
										<div class="problembox">
											<pre>${fn:escapeXml(problem.sampleoutput)}</pre>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="panel panel-default">
							<div class="panel-heading">測資資訊：</div>
							<div class="panel-body">
								記憶體限制：
								<fmt:formatNumber value="${problem.memorylimit}" pattern="###" />
								MB<br />
								<c:forEach var="testdataPair" items="${problem.testdataPairs}"
									varStatus="varstatus">
									<c:if test="${problem.wa_visible==1}">公開</c:if>
									<c:if test="${problem.wa_visible==0}">不公開</c:if>            
            測資點#${varstatus.count-1} (${testdataPair.score}%): ${testdataPair.timelimit}s 
            , ${testdataPair.infile.range}<br />
								</c:forEach>
							</div>
						</div>

					</div>
				</div>



				<!-- ///////
 -->
				<!-- 				<div class="row">
					<div class="col-md-6"></div>
					<div class="col-md-6"></div>
				</div>
 -->
				<div style="text-align: left; margin-bottom: 1em;">
					<strong> <fmt:message key="Problem.Hint" /> ：
					</strong><br />
					<div id="problem_hint" class="problembox">${problem.hint}</div>
				</div>
				<div style="text-align: left; margin-bottom: 1em;">
					<strong> <fmt:message key="Problem.Tags" />:
					</strong>
					<div class="problembox">
						<c:set var="problem" value="${problem}" scope="request" />
						<jsp:include page="include/div/ProblemTags.jsp" />
					</div>
				</div>
				<div style="text-align: left;">
					<strong> 出處: </strong><br />
					<div class="problembox">
						<%--                <form name="searchReference" action="Problems" method="post"
                    style="display: inline">
                    <input name="searchword" type="hidden"
                        value="${fn:escapeXml(problem.reference)}" /> <span
                        id="search_reference" class="FakeLink">${fn:escapeXml(problem.reference)}</span>
                </form>
 --%>
						<jsp:include page="include/div/ProblemReference.jsp" />

						<c:if test="${!problem.owner.nullUser}">
                [編輯：<c:set var="user" value="${problem.owner}"
								scope="request" />
							<jsp:include page="include/div/UserAccount_TypeA.jsp" />]
                </c:if>
					</div>
				</div>

			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-md-12">
				<c:if
					test="${problem:canSubmitCode(sessionScope.onlineUser, problem)}">
					<c:if
						test="${sessionScope.onlineUser.joinedContest.checkedConfig_Exefile}">
						<a href="SubmitCode?problemid=${problem.problemid }" type="button"
							class="btn btn-success"><fmt:message key="Problem.Solveit" /></a>
					</c:if>
					<c:if
						test="${!sessionScope.onlineUser.joinedContest.checkedConfig_Exefile}">
						<jsp:include page="include/Modals/Modal_SubmitCode.jsp" />
						<button id="SubmitCode" data-problemid="${problem.problemid}"
							class="btn btn-success" data-toggle="modal"
							data-target="#Modal_SubmitCode">
							<fmt:message key="Problem.Solveit" />
						</button>
					</c:if>
				</c:if>
				<c:if test="${sessionScope.onlineUser.getIsTestjudgeAccessible()}">
					<!-- 兩個 modal 有順序關係，不可倒反。 -->
					<jsp:include page="include/Modals/Modal_Testjudge.jsp" />
					<jsp:include page="include/Modals/Modal_ServerOutputs.jsp" />
					<button id="Testjudge" class="btn btn-success" data-toggle="modal"
						data-target="#Modal_Testjudge">測試執行</button>
				</c:if>
				<c:if test="${!systemconfigBean.IS_CONTESTMODE}">
					<a href="./Submissions?problemid=${problem.problemid}"
						type="button" class="btn btn-success"><fmt:message
							key="Problem.Status" /></a>
					<a href="./Forum?problemid=${problem.problemid}" type="button"
						class="btn btn-success"><fmt:message key="Problem.Forum" /></a>
					<a href="./BestSolutions?problemid=${problem.problemid}"
						type="button" class="btn btn-success">排行</a>
					<c:if test="${sessionScope.onlineUser.getIsAccepted(problem)}">
						<a
							href="./NewThread?articletype=problemreport&problemid=${problem.problemid }"
							type="button" class="btn btn-primary">編寫「解題報告」</a>
					</c:if>
				</c:if>
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-hover">
					<tr>
						<td><fmt:message key="Forum.ID" /></td>
						<td><fmt:message key="Forum.User" /></td>
						<td><c:if test="${param.tab!='tab02'}">
								<fmt:message key="Forum.Problem" />
							</c:if></td>
						<td width="50%"><fmt:message key="Forum.Subject" /></td>
						<td><fmt:message key="Forum.Hitnum" /></td>
						<td width="18%"><fmt:message key="Forum.PostDate" /></td>
					</tr>

					<c:choose>
						<c:when test="${fn:length(reportArticles)!=0}">
							<c:forEach var="article" items="${reportArticles}"
								varStatus="varstatus">
								<tr>
									<td>${article.id}</td>
									<td><c:set var="user" value="${article.user}"
											scope="request" /> <jsp:include
											page="include/div/UserAccount_TypeA.jsp" /></td>
									<td><a href="./ShowProblem?problemid=${article.problemid}">${article.problemid}</a></td>
									<td width="50%">
										<div align="left">
											<c:set var="article" value="${article}" scope="request" />
											<jsp:include page="include/div/ArticleTitle.jsp" />
										</div>
									</td>
									<td>${article.clicknum}</td>
									<td width="18%" style="font-size: smaller"><fmt:formatDate
											value="${article.timestamp}" pattern="yyyy-MM-dd HH:mm" /></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6"><div align="center">沒有發現任何「解題報告」</div></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</table>
			</div>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
