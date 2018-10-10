<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<form
	action="./Problem.api?action=writeScoresTimelimits&problemid=${problem.problemid }"
	method="post">

	<div id="TestdataPairs">
		<jsp:include page="../Modals/Modal_TestdataPair.jsp" />
		<div>
			目前總共 ${fn:length(problem.testdataPairs)} 組測資點 <a
				class="btn btn-default"
				href="Problem.api?action=downloadTestdatasToZip&problemid=${problem.problemid}"><span
				class="glyphicon glyphicon-download-alt"></span> 下載全部測資</a><br />
		</div>
		<br />
		<c:forEach var="testdataPair" items="${problem.testdataPairs}"
			varStatus="varstatus">
			<div id="testdataPair">
				<div>
					測資點#
					<fmt:formatNumber value="${varstatus.count-1}" pattern="00"
						type="number" />
					<input name="scores" type="text" id="score" size="3" maxlength="3"
						value="${testdataPair.score}" />%, <input name="timelimits"
						type="text" id="timelimit" size="3" maxlength="3"
						value="${testdataPair.timelimit}" />s
					<!-- | <span
					id="readTestdataPair"
					style="text-decoration: underline; cursor: pointer;">展開</span> | <span
					id="deleteTestdataPair"
					style="text-decoration: underline; cursor: pointer;">刪除</span> -->
					<div class="btn-group " role="group" aria-label="...">
						<div class="btn btn-default btn-sm"
							data-target="#Modal_TestdataPair"
							data-problemid="${problem.problemid }"
							data-index="${varstatus.count-1}" title="展開測資內容">
							<i class="fa fa-file-text-o" aria-hidden="true"></i>
						</div>
						<div class="btn btn-default btn-sm" title="刪除這組測資">
							<span id="deleteTestdataPair" data-index="${varstatus.count-1}"
								data-problemid="${problem.problemid }"
								class="glyphicon glyphicon-remove"></span>
						</div>
					</div>
				</div>
				<div>
					輸入：${testdataPair.infile.summary} <span
						style="font-weight: normal; font-size: small;">:<fmt:formatDate
							value="${testdataPair.infile.lastModified}"
							pattern="yyyy-MM-dd HH:mm:ss" /></span> 
<%-- 							(<a
						href="./Download.api?target=Indata&problemid=${problem.problemid}&index=${varstatus.count-1}">下載</a>)
 --%>					<a
						href="./Download.api?target=Indata&problemid=${problem.problemid}&index=${varstatus.count-1}"
						class="btn btn-default btn-xs" title="下載"><span
						class="glyphicon glyphicon-download-alt"></span> </a>
				</div>
				<div>
					輸出：${testdataPair.outfile.summary} <span
						style="font-weight: normal; font-size: small;">:<fmt:formatDate
							value="${testdataPair.outfile.lastModified}"
							pattern="yyyy-MM-dd HH:mm:ss" /></span> 
<%-- 							(<a
						href="./Download.api?target=Outdata&problemid=${problem.problemid}&index=${varstatus.count-1}">下載</a>)
 --%>					<a
						href="./Download.api?target=Outdata&problemid=${problem.problemid}&index=${varstatus.count-1}"
						class="btn btn-default btn-xs" title="下載"><span
						class="glyphicon glyphicon-download-alt"></span> </a>
				</div>
			</div>
		</c:forEach>
		<br /> 總分：<span id="totalscore"></span> 分 <span id="averagescore"
			class="btn btn-default">自動配分</span> <br /> 總時間：<span
			id="totaltimelimit"></span> 秒 <span id="averagetimelimit"
			class="btn btn-default">自動配時</span> <br /> <br /> <input
			name="writeScoresTimelimits" class="btn btn-success btn-lg col-md-12" value="儲存配分、配時" />
	</div>

</form>
