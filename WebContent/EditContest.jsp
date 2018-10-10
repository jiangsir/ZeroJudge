<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<link href="jscripts/DatetimePicker/jquery-ui-timepicker-addon.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="jscripts/DatetimePicker/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="EditContest.js"></script>

</head>

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<form name="form" method="post" action="">
				<h1>
					<c:choose>
						<c:when test="${contest.vclassid>0}">編輯『隨堂測驗』</c:when>
						<c:otherwise>編輯『競賽』</c:otherwise>
					</c:choose>
				</h1>
				<table class="table table-hover">
					<tr>
						<td class="col-md-3 text-right">標題：</td>
						<td class="col-md-9">
							<div class="form-group">
								<input name="title" type="text" id="title"
									value="${fn:escapeXml(contest.title)}" class="form-control" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right"><c:choose>
								<c:when test="${contest.vclassid>0}">課程大綱：</c:when>
								<c:otherwise>副標題：</c:otherwise>
							</c:choose></td>
						<td><textarea name="subtitle" rows="5" class="form-control"
								id="subtitle">${fn:escapeXml(contest.subtitle)}</textarea></td>
					</tr>
					<c:if test="${contest.vclassid==0}">
						<tr>
							<td class="text-right">參加人員列表:</td>
							<td>

								<div class="form-group">
									<textarea name="userrules" rows="3" class="form-control"
										id="userrules" aria-describedby="userrules_help">${contest.userrules}</textarea>
									<div id="userrules_help" class="help-block">
										<h4>說明：</h4>
										※<strong>[.*]</strong> 表 allow from all, <strong>[!.*]</strong>
										表示 deny from all, 再接上例外允許或拒絕的人員，中間請記得以半形逗點,作為分隔。例如：<br /> <strong>[.*,!guest]</strong>
										代表全部允許，但禁止 guest 參加。<br /> <strong>[!.*,guest]</strong>
										代表全部禁止，只允許 guest 參加。<br /> ※人員列表<em><strong>允許使用正規表示式</strong></em>來簡化。如找出所有
										a 開頭者，記為 <strong>a.*</strong>，找出所有 a 結尾者，<strong>.*a$</strong>，找出所有含
										a 的，記為 <strong>.*a.*</strong><br /> ※人員列表檢查列表檢查具有順序性，越靠<strong>右</strong>邊(後面)，優先性越高。如:<br />
										[.*,guest,!g.*] 則 guest 不允許參與<br /> [.*,!g.*,guest] 則 guest
										允許參與 <br />
									</div>
								</div>
							</td>
						</tr>
					</c:if>
					<tr>
						<td class="text-right">題目：</td>
						<td>
							<div class="form-group">
								<label class="control-label" for="problemids">題目列表： </label> <input
									type="text" class="form-control" value="${contest.problemids}"
									name="problemids" id="problemids"
									aria-describedby="problemids_help"> <span
									id="problemids_help" class="help-block">題號寫法以逗點相隔，題號順序即為題目順序。如
									a002,a001。<br />
									可作為題目的形態有：&quot;公開&quot;，&quot;隱藏&quot;，及&quot;練習&quot;題，
								</span>
							</div>
							<div class="form-group">
								<label class="control-label" for="scores">配分： </label> <input
									type="text" class="form-control"
									value="${contest.scoresString}" id="scores" name="scores"
									aria-describedby="scores_help"> <span id="scores_help"
									class="help-block">配分欄位以逗點相隔，可任意配分，總和不一定需要為100，可依需求任意指定，惟須確保為&gt;0的正整數，如
									[40,60]</span>
							</div>
							<div id="contest_problems"></div>
						</td>
					</tr>

					<tr>
						<td class="text-right">參數：</td>
						<td>
							<%-- 					* 競賽結束前 <input name="freezelimit" type="text"
						id="freezelimit" value="${contest.freezelimit}" size="4"
						maxlength="4" /> 分鐘 停止更新其它參賽者的解題動態？ <br /> 
 --%>
							<h4>* 競賽進行中之相關設定${contest.VContest}：</h4>
							<c:if test="${contest.VContest}">
                            <div>
                                [ <input id="config" name="config_${contest.config_ShowWA}"
                                    type="radio" value="1" data-contestconfig="${contest.config}" />
                                是 <input id="config" name="config_${contest.config_ShowWA}"
                                    type="radio" value="0" data-contestconfig="${contest.config}" />
                                否] 允許參賽者查看 WA 詳情？
                                <br/>
                                *「是」則可以顯示 WA 正確答案，適合使用在平時練習(但，題目測資設定為不公開者，仍無法顯示)<br/>
                                *「否」則不顯示 WA 正確答案，競賽時則應選擇不顯示。
                                
                            </div>
                            </c:if>
							<div>
								[ <input id="config" name="config_${contest.config_ShowDetail}"
									type="radio" value="1" data-contestconfig="${contest.config}" />
								是 <input id="config" name="config_${contest.config_ShowDetail}"
									type="radio" value="0" data-contestconfig="${contest.config}" />
								否] 允許參賽者查看詳細錯誤狀況？<br/>
								*「是」則可以顯示詳細情況，可以查看 NA 內的各種錯誤。<br/>
								*「否」則只顯示結果，無法得知 NA 內有哪些錯誤。
							</div>
							<div>
								[ <input id="config" name="config_${contest.config_MultiSubmit}"
									type="radio" value="1" data-contestconfig="${contest.config}" />
								是 <input id="config" name="config_${contest.config_MultiSubmit}"
									type="radio" value="0" data-contestconfig="${contest.config}" />
								否] 是否允許重複送出？
							</div>
							<h4>* 評審設定：</h4>
							<div>
								[ <input id="config" name="config_${contest.config_ManualJudge}"
									type="radio" value="1" data-contestconfig="${contest.config}"/> 是 <input id="config"
									name="config_${contest.config_ManualJudge}" type="radio"
									value="0" data-contestconfig="${contest.config}"/> 否]
								允許評審(主辦人則為當然評審)以手動方式決定最後的評分結果。可用來挑出假解、作弊解或給部分分數。<br />
							</div>
							<h4>* 競賽結束後之相關設定：</h4>
							<div>
								[ <input id="config" name="config_${contest.config_Visible}"
									type="radio" value="1" data-contestconfig="${contest.config}" />
								是 <input id="config" name="config_${contest.config_Visible}"
									type="radio" value="0" data-contestconfig="${contest.config}" />
								否] 競賽結束後，是否讓他人參觀本次競賽題目？
							</div>
							<div>
								[ <input id="config"
									name="config_${contest.config_ContestSubmissions}" type="radio"
									value="1" data-contestconfig="${contest.config}" /> 是 <input
									id="config" name="config_${contest.config_ContestSubmissions}"
									type="radio" value="0" data-contestconfig="${contest.config}" />
								否] 競賽結束後，立即公開競賽狀態
							</div>
							<div>
								[ <input id="config"
									name="config_${contest.config_ContestRanking}" type="radio"
									value="1" data-contestconfig="${contest.config}" /> 是 <input
									id="config" name="config_${contest.config_ContestRanking}"
									type="radio" value="0" data-contestconfig="${contest.config}" />
								否] 競賽結束後，立即公開競賽結果
							</div>
						</td>
					</tr>
					<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
						<tr class="alert alert-danger">
							<td class="text-right">主辦人：${contest.owner.account }</td>
							<td></td>
						</tr>

						<tr id="DEBUGGEROnly" class="alert alert-danger">
							<td class="text-right">其它：</td>
							<td>
								<h4>選擇計分模式：</h4> <input name="rankingmode" type="radio"
								value="HSIC" checked="checked"
								data-rankingmode="${contest.rankingmode }" /> HSIC：
								後決。高雄市資訊學科能力競賽評分方式。<br /> 計分說明(公開)：
								<div class="contestbox">${applicationScope.appConfig.rankingmode_HSIC}</div>
								<br /> <input name="rankingmode" type="radio" value="CSAPC"
								data-rankingmode="${contest.rankingmode }" />CSAPC: 統計當中"
								時間"欄位計算方式，每人每題(不論對錯)的最後一個送出時間和。“競賽結果”<br /> 計分說明(公開)：
								<div class="contestbox">${applicationScope.appConfig.rankingmode_CSAPC}</div>
								<input name="rankingmode" type="radio" value="NPSC"
								data-rankingmode="${contest.rankingmode }" />NPSC:統計當中會顯示“時間”欄位，該欄位依據
								NPSC 的Penalty 設計。 <br /> 計分說明(公開)：
								<div class="contestbox">${applicationScope.appConfig.rankingmode_NPSC}</div>

								<br />
								<hr />
								<h3>參數設定：</h3>
								<h4>* 競賽中相關設定：</h4>
								<div>
									<input id="config" name="config_${contest.config_ShowResult}"
										type="radio" value="1" data-contestconfig="${contest.config}" />
									是 <input id="config" name="config_${contest.config_ShowResult}"
										type="radio" value="0" data-contestconfig="${contest.config}" />
									否 ${contest.config_ShowResult}.
									顯示評分結果。作用於比賽進行當中。某些時候必須讓選手看到『評分結果』以便知道是否上傳成功，但卻不讓看解題結果。讓評審評分時有機會修正。同時也不能看『競賽結果』。只純粹讓使用者看到自己是否成功送出而已。<br />
								</div>
								<h4>* 競賽方式？</h4>
								<div>
									<input id="config"
										name="config_${contest.config_UploadExefile}" type="radio"
										value="1" data-contestconfig="${contest.config}" /> 是 <input
										id="config" name="config_${contest.config_UploadExefile}"
										type="radio" value="0" data-contestconfig="${contest.config}" />
									否 ${contest.config_UploadExefile}.
									允許上傳編譯後的執行檔(代表可以接受系統無法編譯的語言。但必須為全手動評分，不接受已經編譯完成的程式碼在系統上執行)
								</div>
								<div>
									<input id="config" name="config_${contest.config_Team}"
										type="radio" value="1" data-contestconfig="${contest.config}" />
									是 <input id="config" name="config_${contest.config_Team}"
										type="radio" value="0" data-contestconfig="${contest.config}" />
									否 ${contest.config_Team}. 是否採取組隊參加。若是，則管理者必須先匯入團隊資料<span
										id="BatchRegistContestant">(<a
										href="./BatchRegistContestant">匯入團隊資料</a> -- 必須先新增完成比賽才能匯入)
									</span><br /> &lt;這裡可以改成是否採取組隊參加，若要採取組隊參加就必須事先匯入隊伍資料。該 team
									就可以為非系統帳號。也可以直接以一個人一個team 的方式進行。也可以多人一個 team,
									公用賬號。對於系統端來所是無需判斷的。&gt;
								</div>
								<h4>* 允許使用的語言：(未完)</h4>
								<div>
									<c:forEach var="compiler"
										items="${applicationScope.appConfig.serverConfig.enableCompilers}">
										<span style="font-weight: bold; font-size: large">${compiler.language}</span>: ${compiler.version}<br />
									</c:forEach>
								</div>

								<hr />
								<div>
									<input name="contestvisible" type="radio" value="open"
										data-contestvisible="${contest.visible}" /> 公開 <input
										name="contestvisible" type="radio" value="hide"
										data-contestvisible="${contest.visible}" /> 隱藏(隨堂測驗) <br />
									<br /> 額外的權限控制：(未完成) <input name="addonprivilege" type="text"
										id="addonprivilege" value="${contest.addonprivilege}"
										size="50" />
								</div>
								<p>
									自訂計分模式：<br /> 競賽系統提供底下幾個參數供排名只用： 1.各題目分數#score 2. 每題狀況#result
									3. 各題目答對時間#solvetime 4. 執行時間#timeusage 5.耗用記憶體#memoryusage 6.
									submit 數 <br /> zj script: ORDRE BY
									$(&quot;#1&quot;).summary(), $(&quot;#2：ac&quot;).count()
									,$(&quot;#3:last&quot;).summary(), $(&quot;#4&quot;).summary(),
									$(&quot;#5&quot;).summary(), $(&quot;#6&quot;)<br />
									$(&quot;#2[result='AC']:last&quot;).summary()<br />
									$(&quot;user&quot;).summary('#userid');<br />
									$(&quot;solution:last&quot;).summary('#rumtime');<br />
									$(&quot;solution[result='AC']&quot;).count();<br />
									$(&quot;solution&quot;).count();<br />
									請用文字詳細描述排名方式，以便讓參加者一目了然。 <br />
								</p>
							</td>
						</tr>
					</c:if>
				</table>

				<input name="contestid" type="hidden" value="${contest.id}" /> <input
					name="conteststatus" type="hidden"
					value="${contest.conteststatus.value}" /> <input
					name="contestvisible" type="hidden" value="${contest.visible}" />
				<input name="addonprivilege" type="hidden"
					value="${contest.addonprivilege}" />
				<!-- <input
					class="btn btn-success" name="send" type="button" value="送出" /> -->
				<button type="submit" class="btn btn-success btn-lg col-md-12">儲存</button>

			</form>
			<p></p>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
<!-- <script type="text/javascript">
	Calendar.setup({
		inputField : "starttime", // id of the input field
		ifFormat : "%Y-%m-%d %H:%M:%S", // format of the input field
		showsTime : true, // will display a time selector
		button : "trigger", // trigger for the calendar (button ID)
		singleClick : true, // double-click mode
		step : 1
	// show all years in drop-down boxes (instead of every other year as default)
	});
</script>
 -->
