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
<meta charset="UTF-8"><jsp:include
	page="include/CommonHead_BootstrapFlat.jsp" />

<script type="text/javascript" src="jscripts/tinymce/tinymce.min.js"></script>

<!-- 使用 TinyMCE  -->
<script type="text/javascript">
	tinymce
			.init({
                language : "zh_TW",
				selector : ".mceAdvanced",
				theme : "modern",
				plugins : [ "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",
						"searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
						"save table contextmenu directionality emoticons template paste textcolor" ],
				toolbar : "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | l      ink image | print preview media fullpage | forecolor backcolor emoticons",

			});
	tinymce.init({
        language : "zh_TW",
		selector : ".mceSimple"
	});
</script>
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
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<form name="form" id="form" method="post" action="${nextaction}">
				<table style="width: 100%">
					<tr>
						<td><p align="left">
								<strong> <fmt:message key="Forum.Subject" /> ：
								</strong> <input name="subject" type="text"
									value="Re:${fn:escapeXml(origContent.subject)}" size="30" readonly="readonly" />
							</p>
							<p align="left">
								<strong> <fmt:message key="Forum.Content" /> ：<br />
								</strong>${origContent.content}<strong><br /></strong>
							</p>
							<hr /> <strong> <fmt:message key="Forum.ReplyContent" />
								：
						</strong><br /> <textarea name="content" cols="80" rows="20" id="content"
								class="mceSimple">
								<div style="border-left: dotted; padding-left: 20px;">${prevContent.content}</div>
								<hr></hr>
								<br /><br /><br />
						</textarea> <br /></td>
					</tr>
				</table>
				<input name="problemid" type="hidden"
					value="${origContent.problemid}" /> <input name="reply"
					type="hidden" value="${origContent.id}" /> <input type="submit" class="btn btn-success btn-lg col-md-12"></input>
			</form>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
