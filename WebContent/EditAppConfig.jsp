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

<script type="text/javascript"
	src="EditAppConfig.js?${applicationScope.built }"></script>
<script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>

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
				toolbar : "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage | forecolor backcolor emoticons",

			});
	tinymce
			.init({
				language : "zh_TW",
				selector : ".mceSimple",
				plugins : "colorpicker textcolor",
				toolbar : "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor emoticons",
			});
</script>
<style type="text/css">
.glyphicon-refresh-animate {
	-animation: spin .7s infinite linear;
	-webkit-animation: spin2 .7s infinite linear;
}

@
-webkit-keyframes spin2 {from { -webkit-transform:rotate(0deg);
	
}

to {
	-webkit-transform: rotate(360deg);
}

}
@
keyframes spin {from { transform:scale(1)rotate(0deg);
	
}

to {
	transform: scale(1) rotate(360deg);
}
}
</style>
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />

	<div class="container">
		<div class="row">
			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#appconfig"
					aria-controls="appconfig" role="tab" data-toggle="tab">系統參數</a></li>
				<li role="presentation"><a href="#serverconfig"
					aria-controls="serverconfig" role="tab" data-toggle="tab">裁判機設定</a></li>
				<li role="presentation"><a href="#problemtabs"
					aria-controls="problemtags" role="tab" data-toggle="tab">題庫分類標籤</a></li>
				<li role="presentation"><a href="#banners"
					aria-controls="banners" role="tab" data-toggle="tab">BANNER 設定</a></li>
			</ul>
			<form id="form1" name="form1" method="post" action=""
				enctype="multipart/form-data">
				<div class="form-group">
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="appconfig">
							<br />
							<table class="table table-hover">
								<tr>
									<th>站名</th>
									<td><input name="title" type="text" id="title"
										value="${appConfig.title}" size="50" maxlength="50" /></td>
								</tr>
								<tr>
									<th>Title Image</th>
									<td><img src="${appConfig.titleImageBase64}"
										style="background-image: url(images/Transparency100.png); padding: 1em;" /><br />
										<br /> <input type="file" name="titleImage"
										style="font-size: large;" /> （請選擇 svg 圖檔）</td>
								</tr>
								<tr>
									<th>LOGO</th>
									<td><img src="${appConfig.logoBase64}"
										style="background-image: url(images/Transparency100.png); padding: 1em;" /><br />
										<br /> <input type="file" name="logo"
										style="font-size: large;" /> （請選擇 svg 圖檔）</td>
								</tr>
								<tr>
									<th>副標題</th>
									<td><input name="header" type="text" id="Header"
										value="${appConfig.header}" size="50" maxlength="50" /></td>
								</tr>
								<tr>
									<th scope="row">整站模式</th>
									<td><input name="systemMode" type="radio"
										value="TRAINING_MODE" systemMode="${appConfig.systemMode}" />
										訓練模式(所有功能都可用，平常開放時適用)<br /> <input name="systemMode"
										type="radio" value="CONTEST_MODE"
										systemMode="${appConfig.systemMode}" /> 競賽模式 競賽編號= <input
										name="systemModeContestid" type="text"
										value="${appConfig.systemModeContestid}" size="5" /> (競賽編號=0
										代表列出目前所有進行中的競賽。)<br /> 競賽模式:所有無關功能都取消，只能使用「競賽」、「課程」功能。<br />
										<input name="systemMode" type="radio" value="CLOSE_MODE"
										systemMode="${appConfig.systemMode}" /> 系統關閉，公告訊息： <input
										name="system_closed_message" type="text"
										value="${appConfig.system_closed_message}"
										id="system_closed_message" size="80" /><br /></td>
								</tr>
								<tr>
									<th>題目編號前置字元：</th>
									<td><select id="problemid_prefix" name="problemid_prefix"
										problemid_prefix="${appConfig.problemid_prefix}">
											<option value="a" selected="selected">a</option>
											<option value="b">b</option>
											<option value="c">c</option>
											<option value="d">d</option>
											<option value="e">e</option>
											<option value="f">f</option>
											<option value="g">g</option>
											<option value="h">h</option>
											<option value="i">i</option>
											<option value="j">j</option>
											<option value="k">k</option>
											<option value="l">l</option>
											<option value="m">m</option>
											<option value="n">n</option>
											<option value="o">o</option>
											<option value="p">p</option>
											<option value="q">q</option>
											<option value="r">r</option>
											<option value="s">s</option>
											<option value="t">t</option>
											<option value="u">u</option>
											<option value="v">v</option>
											<option value="w">w</option>
											<option value="x">x</option>
											<option value="y">y</option>
											<option value="z">z</option>
									</select></td>
								</tr>
								<tr>
									<th>題目管理員門檻</th>
									<td><input name="threshold" type="text"
										value="${appConfig.threshold}" /> (請填寫浮點數，門檻 &gt;1
										代表不接受其它使用者自動升等為題目管理員)</td>
								</tr>
								<tr>
									<th>$CONSOLE_PATH</th>
									<td>CONSOLE 路徑，請注意：請勿放置在 Web 目錄當中<br /> <input
										name="consolePath" type="text" size="70"
										value="${appConfig.consolePath}" /> PS:context 重新啟動才會生效
									</td>
								</tr>

								<tr>
									<th scope="row">郵件發送</th>
									<td>[ <input name="EnableMailer" type="radio" value="true"
										EnableMailer="${appConfig.enableMailer}" /> 是 <input
										name="EnableMailer" type="radio" value="false"
										EnableMailer="${appConfig.enableMailer}" /> 否] 啟用郵件通知功能？<br />
										郵件發送可進行密碼取回，請使用一個 gmail 信箱來當作系統發送信箱。<br /> Email: <input
										name="SystemMail" type="text" id="SystemMail"
										value="${appConfig.systemMail}" /> :請使用 gmail 信箱 OR gmail
										apps 信箱<br /> 密碼： <input name="SystemMailPassword"
										type="password" value="${appConfig.systemMailPassword}" /></td>
								</tr>
								<tr>
									<th scope="row">是否開放重測？</th>
									<td><input name="rejudgeable" type="radio" value="true"
										rejudgeable="${appConfig.rejudgeable}" /> 是 <input
										name="rejudgeable" type="radio" value="false"
										rejudgeable="${appConfig.rejudgeable}" /> 否 開放重測？<br /></td>
								</tr>
								<tr>
									<th scope="row">同時連線數上限</th>
									<td><input name="maxConnectionByIP" type="text"
										value="${appConfig.maxConnectionByIP}" size="5" /> <br />
										若超過此上限，該 IP 會自動被阻擋。</td>
								</tr>
								<tr>
									<th scope="row">測資大小上限</th>
									<td><input name="maxTestdata" type="text"
										value="${appConfig.maxTestdata}" size="10" /> <br /> 以 byte
										為單位。</td>
								</tr>
								<tr>
									<th scope="row">站務管理員列表</th>
									<td><input name="managers" type="text"
										value="${appConfig.managers}" size="50" /> <br /> 多人時以 ,
										分隔。e.q. [tom, john]</td>
								</tr>
								<tr>
									<th scope="row">本站的開放位置<br />（使用 CIDR 描述）
									</th>
									<td><input name="allowedIP" type="text"
										value="${appConfig.allowedIP}" size="50" /> <br />
										[0.0.0.0/0] 代表完全開放。多組資料時請用逗點分隔，如：[1.1.1.1/24,2.2.2.2/24]</td>
								</tr>
								<tr>
									<th scope="row">允許管理員登入的子網段<br />（使用 CIDR 描述）
									</th>
									<td><input name="manager_ip" type="text"
										value="${appConfig.manager_ip}" size="50" /> <br />
										[0.0.0.0/0] 代表完全開放。多組資料時請用逗點分隔，如：[1.1.1.1/24,2.2.2.2/24]</td>
								</tr>
								<tr>
									<th scope="row">SSL 自訂憑證檔</th>
									<td>
										<div class="col-sm-12">
											自訂憑證說明：<input name="httpscrtinfo" type="text"
												value="${appConfig.httpscrtinfo}" size="80" /> <br /> <a
												href="./Download.api?target=HttpscrtFile">下載自訂憑證檔(zerojudge.crt,
												${fn:length(applicationScope.appConfig.httpscrt)} Bytes)</a> <span
												style="text-decoration: underline; cursor: pointer;"
												id="deleteHttpscrt">刪除</span> <input type="file"
												name="httpscrt" />
											<p class="help-block">若網站擁有 domain 建議使用 Let's encrypt
												憑證，無法使用 domain 才使用自訂憑證。</p>
										</div>
									</td>
								</tr>
								<tr>
									<th scope="row">Google Login 設定</th>
									<td><div>
											底下 3項資料請到 Google Developers Console: <a
												href="https://cloud.google.com/console#/project"
												target="_blank">https://cloud.google.com/console#/project</a>
											進行申請及設定。
										</div> <br>
										<div class="form-group">
											<label for="client_id" class="col-sm-2 control-label">
												clientid 用戶端id</label>
											<div class="col-sm-10">
												<input type="text" class="form-control"
													placeholder="client_id" name="client_id" type="text"
													value="${fn:escapeXml(appConfig.client_id)}">
												<p class="help-block">client_id:</p>
											</div>
										</div>
										<div class="form-group">
											<label for="client_secret" class="col-sm-2 control-label">
												client_secret 用戶端密碼</label>
											<div class="col-sm-10">
												<input type="text" class="form-control"
													placeholder="client_secret" name="client_secret"
													type="text"
													value="${fn:escapeXml(appConfig.client_secret)}">
												<p class="help-block">client_secret:</p>
											</div>
										</div>
										<div class="form-group">
											<label for="redirect_uri" class="col-sm-2 control-label">
												redirect_uri 重新導向 URI</label>
											<div class="col-sm-10">
												<%-- <input type="text" class="form-control"
													placeholder="redirect_uri" name="redirect_uri" type="text"
													value="${fn:escapeXml(appConfig.redirect_uri)}"> --%>
												<p class="help-block">
													redirect_uri 注意，必須在「Google Developers Console」設定以下兩個重新導向
													URI:<br /> http://[your domain]/callbackGoogleLogin<br />
													http://[your domain]/callbackInsertGoogleUser
												</p>
											</div>
										</div></td>
								</tr>
							</table>

						</div>
						<div role="tabpanel" class="tab-pane" id="serverconfig">
							<jsp:include page="include/div/ServerConfigCheck.jsp" />
							<table class="table table-hover">
								<tr>
									<td>
										<div class="form-horizontal">
											<div class="form-group">
												<label class="col-sm-2 control-label">本機同步帳號</label>
												<div class="col-sm-10">
													<div class="input-group">
														<input type="text" class="form-control"
															placeholder="本機同步帳號" name="rsyncAccount"
															value="${appConfig.rsyncAccount}">
													</div>
													<p class="help-block">
														說明：請於本機建立一個 root
														以外的帳號，並以此帳號與「裁判機」傳送資訊。本帳號應能夠與「裁判機」的指定帳號進行「SSH 免密碼登入」。<br />「SSH免密碼登入」參考語法如下：
														ssh-keygen; ssh-copy-id zero@[裁判機 IP]</span><br />
														測試免密碼進入裁判機：進入本機終端機執行 <span
															style="font-family: sans-serif;">sudo -u
															${appConfig.rsyncAccount} ssh -p
															${appConfig.serverConfig.sshport }
															${appConfig.serverConfig.rsyncAccount}@[裁判機 IP]</span><br />可以免密碼進入裁判機即可。
													</p>
												</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form-horizontal">
											<div class="form-group">
												<label class="col-sm-2 control-label">裁判機URL</label>
												<div class="col-sm-10">
													<div class="input-group">
														<input type="text" class="form-control"
															placeholder="裁判機URL" name="serverUrl"
															value="${appConfig.serverUrl}"> <span
															class="input-group-btn">
															<button class="btn btn-default" type="button"
																id="EditServerConfig">
																<span class="glyphicon glyphicon-link"
																	aria-hidden="true"> </span> 前往設定裁判機
															</button>
														</span>
													</div>
													<!-- /input-group -->

													<p class="help-block">URL範例：http://192.168.2.3/</p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">裁判機加密鎖</label>
												<div class="col-sm-10">
													<input type="text" class="form-control" id=""
														placeholder="裁判機加密鎖" name="cryptKey"
														value="${appConfig.cryptKey}">
													<p class="help-block">這裡的「裁判機加密鎖」是由裁判機設定參數決定，因此請進入「裁判機設定URL」進行設定，此處必須與裁判機吻合才有辦法與裁判機進行通訊。</p>
												</div>
											</div>
										</div>

									</td>
								</tr>
								<tr>
									<td>
										<button type="button" class="btn btn-primary"
											id="checkServerConfig">進行「裁判機」檢測</button> <br /> <br />
										<div class="alert alert-default" role="alert">
											STEP1. 檢測「裁判機」是否可以正確連接。
											<div class="pull-right">
												<span class="glyphicon glyphicon-question-sign"
													aria-hidden="true" id="status"></span> <span
													class="glyphicon glyphicon-refresh" id="checkAllowedIP"></span>
											</div>
											<br />若此處無法成功，請檢查「裁判機」是否正確連網。
										</div>
										<div class="alert alert-default" role="alert">
											STEP2. 檢測「裁判機」是否正確設定接受「本機」連線。
											<div class="pull-right">
												<span class="glyphicon glyphicon-question-sign"
													aria-hidden="true" id="status"></span> <span
													class="glyphicon glyphicon-refresh"
													id="checkReadServerConfig"></span>
											</div>
											<br />若此處無法成功，請至「裁判機」設定「允許進入的 IP」給本機IP，才有辦法順利連線到裁判機。
										</div>
										<div class="alert alert-default" role="alert">
											STEP3. 檢測是否能與裁判機進行「SSH免密碼」登入。
											<div class="pull-right">
												<span class="glyphicon glyphicon-question-sign"
													aria-hidden="true" id="status"></span> <span
													class="glyphicon glyphicon-refresh" id="checkNopassRsync"></span>
											</div>
											<br />「SSH免密碼登入」參考語法如下： ssh-keygen; ssh-copy-id zero@[裁判機
											IP]
										</div>
										<div class="alert alert-default" role="alert">
											STEP4. 檢測是否能與「裁判機」正確解碼/解碼所傳遞的資訊。
											<div class="pull-right">
												<span class="glyphicon glyphicon-question-sign"
													aria-hidden="true" id="status"></span> <span
													class="glyphicon glyphicon-refresh" id="checkLocker"></span>
											</div>
											<br> 「加密鎖」由「裁判機」負責設定。這裡的加密鎖設定必須和「裁判機」所設定的加密鎖一致。
										</div>
									</td>
								</tr>
								<tr>
									<td><button type="button" class="btn btn-primary"
											id="readServerConfig">重新讀取裁判機參數</button>
										<div class="alert alert-default" role="alert">
											請先通過上方的「進行「裁判機」檢測」，才能成功重新讀取參數</div></td>
								</tr>
							</table>
						</div>
						<div role="tabpanel" class="tab-pane" id="problemtabs">
							<br />
							<div id="Problemtabs"></div>
						</div>
						<div role="tabpanel" class="tab-pane" id="banners">
							<br />
							<table class="table table-hover">
								<c:forEach var="banner"
									items="${applicationScope.appConfig.banners }">
									<tr>
										<td>比率：<input value="${banner.percent }" type="text"
											name="percent"></input> <textarea
												style="width: 80%; height: 10em;" name="bannerContent"
												class="mceAdvanced">${banner.content }</textarea>

										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
					<div>
						<!-- 					<input type="submit" class="button" name="Submit" value="送出" />
 -->
						<button type="submit" class="btn btn-success btn-lg col-md-12" id="submit">儲存</button>
					</div>
				</div>
			</form>

		</div>
	</div>

	<!-- 	<div id="upload_dialog" alt=""
		style="cursor: default; padding: 10px; display: none; text-align: left">
		<form action="" method="post" enctype="multipart/form-data"
			name="form" id="form">
			<table width="80%" border="0">
				<tr>
					<th scope="col">上傳圖片</th>
				</tr>
				<tr>
					<td><input name="logo" type="file" /><br /></td>
				</tr>
			</table>
		</form>
	</div>



	<div class="content_individual">
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">系統參數</a></li>
				<li><a href="#tabs-2">裁判機設定</a></li>
				<li><a href="#tabs-3">BANNERS 設定</a></li>
			</ul>
			<div id="tabs-1"></div>
			<div id="tabs-2"></div>
			<div id="tabs-3"></div>

		</div>
	</div>
 -->
	<jsp:include page="include/Footer.jsp" />

</body>
</html>
