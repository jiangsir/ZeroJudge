<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="problem" uri="http://jiangsir.tw/jstl/problem"%>

<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript"
	src="EditProblem.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/TestdataPairs.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/div/SpecialJudgeFiles.js?${applicationScope.built }"></script>

<script type="text/javascript"
	src="include/div/DivProblemStatusInfo.js?${applicationScope.built }"></script>
<%-- <script type="text/javascript"
	src="include/dialog/ShowDetail.js?${applicationScope.built }"></script>
 --%>
<%-- <script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
 --%>
<%-- <script type="text/javascript"
	src="include/dialog/ProblemSetting.js?${applicationScope.built }"></script> --%>
<script type="text/javascript"
	src="include/Modals/Modal_ProblemSetting.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/ProblemToolbar.js?${applicationScope.built }"></script>
<%-- <script type="text/javascript"
	src="include/dialog/ShowCode.js?${applicationScope.built }"></script>
 --%>
<script type="text/javascript" src="jscripts/tinymce/tinymce.min.js"></script>

<!-- 使用 TinyMCE  -->
<script type="text/javascript">
	tinymce
			.init({
				language : "zh_TW",
				selector : ".mceAdvanced",
				theme : "modern",
				plugins : [
						"advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",
						"searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
						"save table contextmenu directionality emoticons template paste textcolor" ],
				toolbar : "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | l      ink image | print preview media fullpage | forecolor backcolor emoticons",
			});
	tinymce.init({
		language : "zh_TW",
		selector : ".mceSimple"
	});
</script>

</head>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<%-- 	<div id="prejudgedetail_dialog" alt=""
		style="cursor: default; padding: 10px; display: none;">
		<textarea id="prejudge_detail" name="prejudge_detail" cols="90%"
			rows="20" readonly="readonly">${details}</textarea>
		<br />
	</div>
 --%>
	<!--  	<div id="rejudge_dialog" alt=""
		style="display: none; cursor: default; padding: 20px; margin: auto;">
		<h2>
			本動作會將該題目的所有程式碼全部重測，共 <span id="rejudgesize"></span> 筆<br />
			請問要繼續進行嗎？
		</h2>
		<br />
	</div>
 -->
	<!--  	<div id="readtestdata_dialog" alt=""
		style="cursor: default; padding: 10px; display: none;">
		<h2>本測資檔的輸入、輸出</h2>
		<table width="80%" border="0"
			style="margin: auto; text-align: center;">
			<tr>
				<th scope="col">輸入測資 <span id="indataInfo"></span></th>
				<th scope="col">輸出測資 <span id="outdataInfo"></span></th>
			</tr>
			<tr>
				<td><textarea name="indata" id="indata" cols="45%" rows="18"></textarea></td>
				<td><textarea name="outdata" id="outdata" cols="45%" rows="18"></textarea></td>
			</tr>
		</table>
	</div>
 -->
	<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />


	<div class="container-fluid">
		<a href="EditProblems" class="btn btn-primary btn-lg">回題目管理</a> <a
			href="EditProblems?tabid=${problem.tabid}#${problem.tabid}"
			class="btn btn-primary btn-lg">管理 ${problem.tab}</a>
		<div class="row">
			<div class="col-md-6">
				<table class="table-hover">
					<tr>
						<td>
							<form id="updateProblem" name="updateProblem" method="post"
								action="">
								<input name="problemid" type="hidden" id="problemid"
									value="${problem.problemid}" />
								<fieldset>
									<legend> 題目 (${problem.problemid}) </legend>
									<div class="form-group">
										<label for="title">題目標題</label> <input type="text"
											name="title" class="form-control" placeholder="標題"
											value="${fn:escapeXml(problem.title)}">
									</div>

									<div class="form-group">
										<label for="content">內容：</label>
										<textarea name="content" class="form-control mceAdvanced"
											rows="20">${fn:replace(problem.content, "&", "&amp;")}</textarea>
									</div>



									<div class="form-group">
										<label for="theinput">輸入說明：</label>
										<textarea name="theinput" class="form-control mceAdvanced"
											rows="5">${fn:replace(problem.theinput, "&", "&amp;")}</textarea>
									</div>


									<div class="form-group">
										<label for="theoutput">輸出說明：</label>
										<textarea name="theoutput" class="form-control mceAdvanced"
											rows="5">${fn:replace(problem.theoutput, "&", "&amp;")}</textarea>
									</div>



									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label for="sampleinput"><fmt:message
														key="Problem.SampleInput" />:</label>
												<textarea name="sampleinput" class="form-control" rows="15">${problem.sampleinput}</textarea>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label for="sampleoutput"><fmt:message
														key="Problem.SampleOutput" />:</label>
												<textarea name="sampleoutput" class="form-control" rows="15">${problem.sampleoutput}</textarea>
											</div>
										</div>
									</div>


									<div class="form-group">
										<label for="hint">提示:</label>
										<textarea name="hint" class="form-control mceAdvanced"
											rows="3">${problem.hint}</textarea>
									</div>



									<div class="form-group">
										<label for="samplecode">參考解法：</label>
										<jsp:include page="include/div/EnableCompilers_TypeA.jsp" />
										<textarea name="samplecode" class="form-control" rows="10">${problem.samplecode}</textarea>
									</div>
									<%-- 						<div>
							<strong></strong> <br />
							<span id="prejudge_status" alt="${problem.problemid}">${problem.html_PrejudgeResult}</span>
							<span
                        id="img_prejudge"><img src="./images/exec16.png"
                            style="cursor: pointer" /></span> (可將參考解答置放於此並存檔後進行"前測"，即可觀察結果。) <br />

							<textarea name="samplecode" cols="70" rows="10" id="samplecode"
								style="width: 100%">${problem.samplecode}</textarea>
							<br />

						</div> --%>
									<div class="form-group">
										<label for="comment">備註：</label>
										<textarea name="comment" class="form-control" rows="5"
											id="comment">${problem.comment}</textarea>
									</div>

									<%-- <c:if test="${sessionScope.onlineUser.isDEBUGGER}">
							<div class="alert alert-danger">
								<strong>編輯者：</strong>
								<c:choose>
									<c:when test="${problem.owner==null || problem.owner==''}">
										<c:set var="owner" value="${sessionScope.onlineUser.account}" />
									</c:when>
									<c:otherwise>
										<c:set var="owner" value="${problem.owner}" />
									</c:otherwise>
								</c:choose>
								<input name="author" type="text" id="author" value="${author}" />
								<br />
							</div>
						</c:if> --%>
									<br /> <br /> <input name="tab" type="hidden" id="tab"
										value="tab10,原創題目" /> <input name="pid" type="hidden"
										id="pid" value="${problem.id}" /> <input name="problemid"
										type="hidden" id="problemid" value="${problem.problemid}" />
									<input name="sortable" type="hidden"
										value="${problem.sortable}" /> <input name="keywords"
										type="hidden" value="${problem.keywords}" /> <input
										type="submit" name="updateProblem" value="儲存題目內容"
										class="btn btn-success btn-lg col-md-12" />

								</fieldset>
							</form>
						</td>
					</tr>
				</table>
			</div>
			<div class="col-md-6">
				<div class="row">
					<div class="col-md-12">
						<table class="table-hover">
							<tr>
								<td><jsp:include
										page="include/ProblemToolbar_Bootstrap.jsp" /></td>
							</tr>
						</table>
					</div>
				</div>

				<div class="row">
					<div class="col-md-12" id="ProblemSettings">
						<table class="table-hover">
							<tr>
								<td><jsp:include
										page="include/Modals/Body/ModalBody_ProblemSetting.jsp" /> <input
									type="button" class="btn btn-success btn-lg col-md-12"
									id="save_ProblemSettings" value="儲存設定"
									data-problemid="${problem.problemid }" /></td>
							</tr>
						</table>
						<hr>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="table-hover">
							<tr>
								<td>

									<h2>上傳測資檔</h2>
									<form id="uploadTestdatas" action="" method="post"
										enctype="multipart/form-data" name="uploadTestdatas"
										id="uploadTestdatas">
										<div>
											請選取符合測資檔名 pattern 的檔案, ## 代表序號，序號如 00, 01,
											02,...，請注意，勿跳號，輸入輸出檔必須成對出現。<br /> 例如：您有 a001_00.in,
											a001_00.out, a001_01.in, a001_01.out 等兩組輸出入測資。<br />輸入檔名
											pattern 則應為 a001_##.in<br />輸出檔名 pattern 則應為
											a001_##.out，其餘依此類推。
										</div>
										<div>
											自訂輸入測資檔名 pattern: <input type="text" name="infilepattern"
												value="${problem.problemid}_##.in" /><br />(例:
											filename_##.in) <br />自訂輸出測資檔名 pattern: <input type="text"
												name="outfilepattern" value="${problem.problemid}_##.out" /><br />(例:
											filename_##.out) <br /> <input type="file"
												multiple="multiple" name="testdatas" /> (請複選) <br />
										</div>

										<input type="button" class="btn btn-success btn-lg col-md-12"
											name="uploadFiles" value="立即上傳測資"
											data-problemid="${problem.problemid }" />

									</form>
								</td>
							</tr>
						</table>
						<hr>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="col-md-12 table-hover">
							<tr>
								<td>
									<h2>測資配分、配時</h2> <jsp:include
										page="include/div/TestdataPairs.jsp" />
								</td>
							</tr>
						</table>
						<hr />
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="table-hover">
							<tr>
								<td>
									<h2>自訂比對(Special Judge)</h2>
									<fieldset>
										<legend> </legend>
										<form action="" enctype="multipart/form-data" method="post"
											data-problemid="${problem.problemid }">
											<div>
												<%--                             <c:if
                                test="${sessionScope.onlineUser.isDEBUGGER || sessionScope.onlineUser.isQualifiedAuthor}">
 --%>
												<div>
													<!--                                            <input name="judgemode" type="radio" value="Special" />
                                            Special Judge (自訂比對)
 -->

													<div>自訂比對： 由出題者自行撰寫 judge 程式來判斷解題者答案是否正確。</div>
													<div id="SpecialJudgeHint">
														<br /> 使用自定評分程式(Special Judge) <br /> 指令：<br />
														<div>
															系統會自動經由指令行送入 3 個參數給評審程式，因此所撰寫的評審程式必須接受共 3 個參數輸入分別為 $1:
															標準輸入檔，$2: 標準輸出檔，$3: 使用者輸出檔。詳細規定請參考說明<br />
															<div>
																出題者撰寫 Special Judge 評分程式注意事項：<br /> 1. 評分程式必須由指令行讀入 3
																個參數，分別為"標準輸入測資"，"標準輸出測資&quot;，&quot;使用者輸出檔&quot;。<br />
																2. 評分程式必須利用標準螢幕輸出(cout or printf)回報結果，回報結果必須依據以下格式
															</div>
															<ul>
																<li>$JUDGE_RESULT=AC or WA ：Special Judge
																	只負責答案比對，因此只有 AC or WA</li>
																<li>$CASES=? ：回報使用者錯誤發生在第幾個 test case，也可以不回報。</li>
																<li>$LINECOUNT=? ：回報使用者錯誤發生在第幾行，也可以不回報。</li>
																<li>$USEROUT=xxx ：回報使用者所輸出的答案。</li>
																<li>$SYSTEMOUT=xxx ：回報系統標準答案。</li>
																<li>$MESSAGE=xxx：回報使用者相關訊息，訊息的詳細程度由出題者自行決定。</li>
															</ul>
															<jsp:include
																page="include/Modals/Modal_ShowSpecialJudgeCode.jsp" />
															<strong>評分程式：</strong>
															<button type="button" class="btn btn-default"
																data-toggle="modal"
																data-target="#Modal_ShowSpecialJudgeCode">參考範例</button>
															<script type="text/javascript">
																jQuery(document)
																		.ready(
																				function() {
																					$(
																							"input[name='specialjudge_language']")
																							.each(
																									function() {
																										if ($(
																												this)
																												.val() == $(
																												this)
																												.data(
																														"language")) {
																											$(
																													this)
																													.prop(
																															"checked",
																															true);
																										}
																									});

																					$(
																							'input[type=radio][name=specialjudge_language]')
																							.change(
																									function() {
																										var problemid = $(
																												this)
																												.closest(
																														"form")
																												.data(
																														"problemid");
																										var specialjudge_language = $(
																												this)
																												.val();
																										console
																												.log("problemid="
																														+ problemid
																														+ ", language="
																														+ specialjudge_language);
																										$
																												.getJSON(
																														"./Problem.json?data=GetSpecialJudgeCode&problemid="
																																+ problemid
																																+ "&specialjudge_language="
																																+ specialjudge_language,
																														function(
																																data) {
																															console
																																	.log("data="
																																			+ data);
																															$(
																																	"textarea[name='specialjudge_code']")
																																	.html(
																																			data);
																														})
																												.fail(
																														function(
																																d,
																																textStatus,
																																error) {
																															console
																																	.error("讀不到東西。");
																															$(
																																	"textarea[name='specialjudge_code']")
																																	.html(
																																			"");
																														});
																										;
																									});
																				});
															</script>

															<br /> <input name="specialjudge_language" type="radio"
																value="CPP"
																data-language="${problem.specialjudge_language.name}">CPP
															<input name="specialjudge_language" type="radio"
																value="PYTHON"
																data-language="${problem.specialjudge_language.name}">PYTHON

															<br />
															<textarea id="specialjudge_code" name="specialjudge_code"
																cols="90%" rows="20">${problem.specialjudge_code}</textarea>
															<div>
																目前本題目(${problem.problemid})的 Special Judge 程式如下：<br />
																<div id="SpecialJudgeFiles"
																	data-problemid="${problem.problemid}"></div>
															</div>

														</div>
														<c:if test="${sessionScope.onlineUser.isDEBUGGER }">
															<div class="alert-danger">
																<hr></hr>
																上傳附加程式或資料：<br /> <input type="file" multiple="multiple"
																	name="specialjudges" /> (可多選)<br /> <br />
															</div>
														</c:if>
													</div>
												</div>
											</div>
											<input class="btn btn-success btn-lg col-md-12"
												name="saveUploadSpecialJudgeFiles" value="儲存 Special Judge" />
										</form>
									</fieldset>
								</td>
							</tr>
						</table>
						<hr>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="include/Footer.jsp" />
</body>
</html>
