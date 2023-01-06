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

<script src="${pageContext.request.contextPath}/jscripts/tinymce_6.0.1/tinymce/js/tinymce/tinymce.min.js"></script>
<script src="${pageContext.request.contextPath}/jscripts/tinymce_6.0.1/tinymce/js/tinymce/langs/zh_TW.js"></script>
<!-- 使用 TinyMCE 6.0 -->
<script type="text/javascript">
	jQuery(document).ready(function () {
		console.log("articletype = " + $(".articletype").val())
		console.log("data-articletype = " + $(".mceImage").data("articletype"))

		var articleid = $(".mceImage").data("articleid");
		var images_upload_url = "UploadImages.api?action=uploadForumImage&articleid=" + articleid;

		const example_image_upload_handler = (blobInfo, progress) => new Promise((resolve, reject) => {
			const xhr = new XMLHttpRequest();
			xhr.withCredentials = false;
			console.log('xhr.open=' + images_upload_url);
			xhr.open('POST', images_upload_url);
			xhr.upload.onprogress = (e) => {
				progress(e.loaded / e.total * 100);
			};
			xhr.onload = () => {
				if (xhr.status === 403) {
					//				reject('HTTP Error: ' + xhr.status+'<br>'+xhr.responseText);
					reject({ message: 'HTTP Error: ' + xhr.status + '<br>' + xhr.responseText, remove: true });
					return;
				}
				if (xhr.status < 200 || xhr.status >= 300) {
					reject('HTTP Error: ' + xhr.status + '<br>' + xhr.responseText);
					return;
				}
				const json = JSON.parse(xhr.responseText);
				if (!json || typeof json.location != 'string') {
					reject('Invalid JSON: ' + xhr.responseText);
					return;
				}
				resolve(json.location);
			};
			xhr.onerror = () => {
				reject('Image upload failed due to a XHR Transport error. Code: ' + xhr.status);
			};
			const formData = new FormData();
			formData.append('file', blobInfo.blob(), blobInfo.filename());
			xhr.send(formData);
		});

		tinymce.init({
			selector: ".mceImage",
			language: 'zh_TW',
			skin: 'oxide',
			icons: 'material',
			plugins: "code table advlist autolink link image lists preview", //字符串方式
			toolbar: 'undo redo | styleselect | bold italic | table tabledelete | link image | code',
			images_upload_handler: example_image_upload_handler,
		});
	});

</script>

<!-- 處理錯誤訊息彈出 bootstrap.alert -->
<script type="text/javascript" src="NewThread.js?${applicationScope.built }"></script>
<script>
	MathJax = {
		tex: {
			inlineMath: [['$', '$'], ['\\(', '\\)']]
		}
	};
</script>
<script src="jscripts/MathJax-3.1.4/es5/tex-chtml.js" id="MathJax-script" async></script>

</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<form name="form" id="form" method="post" action="${nextaction}">
				<table style="width: 100%">
					<tr>
						<td>
							<!-- <p align="left">
								<strong>
									<fmt:message key="Forum.Subject" /> ：
								</strong> <input name="subject" type="text"
									value="Re:${fn:escapeXml(origContent.subject)}" size="30" readonly="readonly" />
							</p> -->
							<div class="form-group">
								<div class="input-group">
									<div class="input-group-addon">
										<fmt:message key="Forum.Subject" />：
									</div>
									<input type="text" class="form-control" id="subject" placeholder="主題" name="subject"
										value="Re: ${fn:escapeXml(origContent.subject)}" readonly="readonly" >
								</div>
							</div>
							<!-- <p align="left">
								<strong>
									<fmt:message key="Forum.Content" /> ：<br />
								</strong>${origContent.content}<strong><br /></strong>
							</p> -->
							<hr /> <strong>
								<fmt:message key="Forum.ReplyContent" />
								：
							</strong><br /> 
							<textarea name="content" cols="80" rows="20" id="content" class="mceImage" data-articleid="${origContent.id}">
														<blockquote>${prevContent.content}</blockquote>
														<hr />
														<br /><br />
														</textarea> <br />
						</td>
					</tr>
				</table>
				<input name="problemid" type="hidden" value="${origContent.problemid}" />
				<input name="reply" type="hidden" value="${origContent.id}" />
				<input type="submit" class="btn btn-success btn-lg col-md-12" value="送出" />
			</form>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
