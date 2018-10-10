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

<script type="text/javascript" src="Debug.js?${applicationScope.built }"></script>
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#tab1"
					aria-controls="tab1" role="tab" data-toggle="tab">系統資訊</a></li>
				<li role="presentation"><a href="#tab2" aria-controls="tab2"
					role="tab" data-toggle="tab">資料庫管理</a></li>
				<li role="presentation"><a href="#tab3" aria-controls="tab3"
					role="tab" data-toggle="tab">功能測試</a></li>
				<li role="presentation"><a href="#tab4" aria-controls="tab4"
					role="tab" data-toggle="tab">properties</a></li>
				<li role="presentation"><a href="#tab5" aria-controls="tab5"
					role="tab" data-toggle="tab">request 參數</a></li>
				<li role="presentation"><a href="#tab6" aria-controls="tab6"
					role="tab" data-toggle="tab">OnlineSessions</a></li>
				<li role="presentation"><a href="#tab7" aria-controls="tab7"
					role="tab" data-toggle="tab">OnlineSessions</a></li>
				<li role="presentation"><a href="#tab8" aria-controls="tab8"
					role="tab" data-toggle="tab">OnlineSessions</a></li>
				<li role="presentation"><a href="#tab9" aria-controls="tab9"
					role="tab" data-toggle="tab">OnlineSessions</a></li>
				<li role="presentation"><a href="#tab10" aria-controls="tab10"
					role="tab" data-toggle="tab">OnlineSessions</a></li>
			</ul>
			<!-- 			<ul>
				<li><a href="#tabs-1">系統資訊</a></li>
								<li><a href="#tabs-4">黑名單</a></li>
				<li><a href="#tabs-5">題目管理員</a></li>

				Cleaner BEGIN
				<li><a href="#tabs-12">程式語言設定</a></li>
				<li><a href="#tabs-13">裁判機設定</a></li>
				<li><a href="#tabs-2">資料庫管理</a></li>
				<li><a href="#tabs-7">功能測試</a></li>
				<li><a href="#tabs-9">properties</a></li>
				<li><a href="#tabs-10">request 參數</a></li>
				<li><a href="#tabs-11">OnlineSessions</a></li>

				Cleaner END
			</ul>
 -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="tab1">
					<div>${applicationScope.appConfig.systemMode}</div>
					<div>tomcat 啟動時間：${tomcatliveTime}</div>
					<div>
						<table class="table table-hover">
							<tr>
								<td>maxMemroy:</td>
								<td><fmt:formatNumber value="${maxMemory}"
										pattern="###,###,###,###" /> bytes Xmx 指定的最大可用記憶體</td>
							</tr>
							<tr>
								<td>totalMemory:</td>
								<td><fmt:formatNumber value="${totalMemory}"
										pattern="###,###,###,###" /> bytes 目前 JVM 已取得的記憶體</td>
							</tr>
							<tr>
								<td>freeMemory:</td>
								<td><fmt:formatNumber value="${freeMemory}"
										pattern="###,###,###,###" /> bytes 目前 JVM 可用的記憶體</td>
							</tr>
							<tr>
								<td>目前系統記憶體用量：</td>
								<td><fmt:formatNumber value="${totalMemory-freeMemory}"
										pattern="###,###,###,###" /> bytes ( <fmt:formatNumber
										value="${(totalMemory-freeMemory)/totalMemory*100}"
										pattern="###.#" /> % )</td>
							</tr>
							<tr>
								<td>uptime:</td>
								<td>${uptime }</td>
							</tr>
							<tr>
								<td>loadAverage:</td>
								<td>${loadAverage }<br /></td>
							</tr>
							<tr>
								<td>whoami:</td>
								<td>${whoami }<br /></td>
							</tr>
							<tr>
								<td>javaVersion:</td>
								<td>${javaVersion }<br /></td>
							</tr>
							<tr>
								<td>tomcatVersion:</td>
								<td>${tomcatVersion }<br /></td>
							</tr>
							<tr>
								<td>mysqlVersion:</td>
								<td>${mysqlVersion }</td>
							</tr>
							<tr>
								<td>JAVA_HOME:</td>
								<td>${JAVA_HOME}</td>
							</tr>
							<tr>
								<td>availableProcessors:</td>
								<td>${availableProcessors}</td>
							</tr>
						</table>
						<br />
					</div>
					<hr />
					<h2>快取資料</h2>
					<table class="table table-hover">
						<tr>
							<th scope="col">Cache 資料</th>
							<th scope="col">
								<div align="left">資料庫數量</div>
							</th>
							<th scope="col">
								<div align="left">HashTable 數量</div>
							</th>
							<th scope="col">備註</th>
						</tr>
						<tr>
							<th scope="row">Problems</th>
							<td>${countByAllProblems}</td>
							<td>${hashProblemsSize}</td>
							<td>存放於applicationScope.CacheProblems.size=${fn:length(applicationScope.CacheProblems)}
							</td>
						</tr>
						<tr>
							<th scope="row">Solutions</th>
							<td>${countByAllSolutions}</td>
							<td>${hashSolutionsSize}</td>
							<td>存放於applicationScope.CacheSolutions.size=${fn:length(applicationScope.CacheSolutions)}</td>
						</tr>
						<tr>
							<th scope="row">Users</th>
							<td>${countByAllUsers}</td>
							<td>${hashUsersSize}</td>
							<td>存放於applicationScope.CacheUsers.size=${fn:length(applicationScope.CacheUsers)}</td>
						</tr>
						<tr>
							<th scope="row">Contests</th>
							<td>${countByAllContests}</td>
							<td>${hashContestsSize}</td>
							<td>存放於applicationScope.CacheContests.size=${fn:length(applicationScope.CacheContests)}</td>
						</tr>
					</table>

					<hr />
					<button id="restartMysql">restart mysql</button>
					<button id="restartTomcat">restart tomcat(可以將 tomcat 重新啟動)</button>
					<button id="reboot">重新開機</button>

				</div>
				<div role="tabpanel" class="tab-pane" id="tab2">
					資料庫管理<br />
					<%--                <div style="text-align: left">
                    若系統軟體升級後，可能必須升級資料庫內的資料才有辦法順利升級。<br /> 除了升級系統程式之外，必須注意資料庫是否有跟著升級。
                    並且由於系統升級後，資料庫的變動分為下列幾種：<br /> 1.
                    欄位名稱的變動，或欄位的增加與刪除。這類變動最容易處理，直接執行相對應的 SQL 語法即可完成更動<br /> 2.
                    欄位內容的重組。比如 details 原本是純文字存放。但為了增加彈性，改為 jsondetails
                    來存放。此時舊有內容就必須重新分析組合。使用 SQL 語法顯然難以達成。因此必須利用 Java
                    來處理。而且為了因應大量資料的更動，應使用多緒處理。<br />
                </div>
                <br />
                <div id="ui-bottom">
                    <ul>
                        <li class="FakeLink" id="UpgradeDatabase"><a
                            href="./UpgradeDatabase">升級資料庫</a></li>
                        <li class="FakeLink" id="updateDatabaseVersion">指定資料庫為
                            ${applicationScope.version}</li>
                    </ul>
                </div>
 --%>
					<hr />
					<p>Schema 列表: (包含舊系統所使用的 Schema)</p>
					<a class="FakeLink" id="addSchema">新增 Schemas 列表</a>

					<table width="40%" border="0" align="center">
						<tr>
							<th scope="col">&nbsp;</th>
							<th scope="col">&nbsp;</th>
						</tr>
						<c:forEach var="systemschema"
							items="${applicationScope.appConfig.schemas}"
							varStatus="varstatus">
							<tr>
								<th scope="row">${systemschema.version}</th>
								<td>升級到這個版本 | <span class="FakeLink" id="compareSchema"
									index="${varstatus.count-1}">比較</span> | <span class="FakeLink"
									id="removeSchema" index="${varstatus.count-1}">刪除</span></td>
							</tr>
						</c:forEach>
					</table>
					<br /> <br />
				</div>
				<div role="tabpanel" class="tab-pane" id="tab3">
					<p>
						ThreadPool 中共有 ${fn:length(ApplicationScope.threadPool)} 個 Thread<br />
						<c:forEach var="list" items="${ApplicationScope.threadPool}"
							varStatus="varstatus"> ${list.key} ${list.value}<br />
						</c:forEach>
						<br />
						<c:forEach var="i" begin="1" end="9" step="1">${i}</c:forEach>
					</p>

				</div>
				<div role="tabpanel" class="tab-pane" id="tab4">
					<!--                <p>
                    資料庫升級<br /> 每個測資都有獨立的時限，第一次建立由, 分隔的 timelimits|
                    ./Debugger.api?action=built_all_Timelimits<br /> <span
                        id="Testjudge"><a href="#">Testjudge</a><br /> </span> <a
                        href="./ShowTestcodes">ShowTestcodes</a><br /> <a
                        href="./Debugger.api?action=currect_ProblemScores">currect_ProblemScores</a><br />
                    <a href="./ShowCatalina">ShowCatalina</a><br /> <a
                        href="./Problems_tags">Problems_tags 測試 tag並且更換 tab 的使用</a><br />
                    <a href="./Problem.api?action=recountAllAcceptedProblem">重算全部AC數</a><br />
                    <a href="./RebuiltAllProblemDifficulity.do?regex=b">重建題目的
                        Difficulity('%b%')</a><br /> <a
                        href="./AdminTools?method=ReplaceImage">將所有題目的圖片存入資料庫！(0%)</a>
                </p>
                <p>
                    <br /> <br />
                </p>
 -->
					<!--                <p>&nbsp;</p>
                <form id="form_webobject">
                    <p>
                        GET &amp; POST 測試<br /> URL: <input name="url" type="text"
                            id="url" size="100" /> <br /> POST datas: <input
                            name="querystring" type="text" id="querystring" /> <br /> POST
                        datas: <input name="querystring" type="text" id="querystring" />
                        <br /> POST datas: <input name="querystring" type="text"
                            id="querystring" /> <br /> POST datas: <input
                            name="querystring" type="text" id="querystring" /> <br /> POST
                        datas: <input name="querystring" type="text" id="querystring" />
                        <br /> POST datas: <input name="querystring" type="text"
                            id="querystring" /> <br /> 方法： <select name="method"
                            id="method">
                            <option value="GET" selected="selected">GET</option>
                            <option value="POST">POST</option>
                        </select> 網頁編碼： <select id="charset" name="charset">
                            <option value="UTF8" selected="selected">UTF8</option>
                            <option value="big5">big5</option>
                        </select> <input name="submit4" type="button" id="submit4" value="送出" />
                    </p>
                </form>
                <p>
                    <textarea name="webresult" cols="80" rows="10" id="webresult"></textarea>
                </p>
 -->


	<!-- cleaner END -->

	<jsp:include page="include/Footer.jsp" />
</body>
</html>
