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
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<form name="form" id="form" method="post" action="">
				<table class="table table-hover">
					<tr>
						<td>
							<%-- <strong> <fmt:message key="Forum.Problem" /> ：
						</strong> <input name="problemid" type="text" value="${param.problemid}"
							size="5" id="txtSearch" /> <br /> <strong> <fmt:message
									key="Forum.Subject" /> ：
						</strong> <input id="subject" name="subject" type="text"
							value="${fn:escapeXml(article.subject)}" size="100" /> --%>
							<div class="form-group">

								<div class="input-group">
									<div class="input-group-addon">
										<fmt:message key="Forum.Problem" />
										：
									</div>
									<input type="text" class="form-control" id="problemid"
										placeholder="題目編號" name="problemid" value="${param.problemid}">
								</div>
							</div>
							<div class="form-group">

								<div class="input-group">
									<div class="input-group-addon">
										<fmt:message key="Forum.Subject" />
										：
									</div>
									<input type="text" class="form-control" id="subject"
										placeholder="主題" name="subject"
										value="${fn:escapeXml(article.subject)}">
								</div>
							</div> <c:if test="${param.articletype=='problemreport'}">

								<div class="alert alert-danger" role="alert">
									<span class="glyphicon glyphicon-exclamation-sign"
										aria-hidden="true"></span>
									請注意：您正在編寫「解題報告」，請勿直接貼出完整程式碼(將被隱藏)，而是請說明解題思路、所需使用的演算法...等，讓不會寫的使用者可以從中學習獲得成長。
								</div>
							</c:if>
							<div>
								<strong> <fmt:message key="Forum.Content" /> ：
								</strong><br />
								<textarea name="content" cols="100%" rows="20" id="content"
									class="mceAdvanced">${article.content}</textarea>
								<strong><br /></strong>
							</div>
						</td>
					</tr>
				</table>
				<input type="hidden" name="token" value="${token}" /><input
					name="articletype" type="hidden" value="${article.articletype }" />
				<input name="reply" type="hidden" value="0" /> <input type="submit"
					class="btn btn-success btn-lg col-md-12" />
			</form>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
