<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="article" uri="http://jiangsir.tw/jstl/article"%>
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
/*

*/

jQuery(document).ready(function () {
	console.log("articletype = "+$(".articletype").val())
	console.log("data-articletype = "+$(".mceImage").data("articletype"))
	var articleid = $(".mceImage").data("articleid");
	var images_upload_url = "UploadImages.api?action=uploadForumImage&articleid="+ articleid;

	const example_image_upload_handler = (blobInfo, progress) => new Promise((resolve, reject) => {
		const xhr = new XMLHttpRequest();
		xhr.withCredentials = false;
		//var problemid = $(tinymce.activeEditor.).attr("");
		console.log('xhr.open='+ images_upload_url);
		xhr.open('POST', images_upload_url);
		xhr.upload.onprogress = (e) => {
			progress(e.loaded / e.total * 100);
		};
		xhr.onload = () => {
			if (xhr.status === 403) {
//				reject('HTTP Error: ' + xhr.status+'<br>'+xhr.responseText);
				reject({ message: 'HTTP Error: ' + xhr.status+'<br>'+xhr.responseText, remove: true });
				return;
			}
			if (xhr.status < 200 || xhr.status >= 300) {
				reject('HTTP Error: ' + xhr.status+'<br>'+xhr.responseText);
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
		paste_data_images: true, // here enable 
		plugins: "code table advlist autolink link image lists preview", //字符串方式
		toolbar: 'undo redo | styleselect | bold italic | table tabledelete | link image | code',
		//images_upload_url: images_upload_url,
		images_upload_handler: example_image_upload_handler,
	});
	var articletype = tinymce.get("content").getContent();
	console.log("articletye=" + articletype);
});

</script>
<script type="text/javascript" src="NewThread.js?${applicationScope.built }"></script>
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<form name="form" id="form" method="post" action="">
				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">
							<fmt:message key="Forum.Problem" /> ：
						</div>
						<input type="text" class="form-control" id="problemid" placeholder="題目編號" name="problemid"
							value="${param.problemid}">
					</div>
				</div>
				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">
							<fmt:message key="Forum.Subject" />：
						</div>
						<input type="text" class="form-control" id="subject" placeholder="主題" name="subject"
							value="${fn:escapeXml(article.subject)}">
					</div>
				</div>
				<c:if test="${param.articletype=='problemreport'}">
					<div class="alert alert-danger" role="alert">
						<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
						請注意：您正在編寫「解題報告」，請勿直接貼出完整程式碼(將被隱藏)，而是請說明解題思路、所需使用的演算法...等，讓不會寫的使用者可以從中學習獲得成長。
					</div>
				</c:if>
				<strong>
					<fmt:message key="Forum.Content" /> ：
				</strong><br />
				<div class="my-custom-editor-container">
					<textarea name="content" cols="100%" rows="20" id="content" class="mceImage" data-articletype="${article.articletype }" data-articleid="${article.id}">${article.content}</textarea>
				</div>
				<input type="hidden" name="token" value="${token}" />
				<input name="articletype" id="articletype" type="hidden" value="${article.articletype }" />
				<input name="reply" type="hidden" value="0" />
				<br>
				<input type="submit" class="btn btn-success btn-lg col-md-12" value="送出" />
			</form>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
