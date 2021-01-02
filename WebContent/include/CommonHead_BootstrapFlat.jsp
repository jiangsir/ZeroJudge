<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<link rel="shortcut icon" href="./favicon.ico" type="image/x-icon" />
<!-- <Bootstrap CDN>  -->

<!-- 
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"> 
 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jscripts/bootstrap-3.3.7-dist/css/bootstrap.min.css">

<!-- 
<link href="https://scottdorman.github.io/bootstrap-flat/dist/css/bootstrap-flat.min.css" rel="stylesheet">
 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jscripts/bootstrap-flat-3.3.4-dist/bootstrap-flat.min.css">

<!-- 
<link
	href="https://scottdorman.github.io/bootstrap-flat/dist/css/bootstrap-flat-extras.min.css"
	rel="stylesheet">
 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jscripts/bootstrap-flat-3.3.4-dist/bootstrap-flat-extras.min.css">

<!-- 
<link
	href="https://scottdorman.github.io/bootstrap-flat/dist/css/font-awesome.min.css"
	rel="stylesheet">
-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jscripts/font-awesome-4.7.0/css/font-awesome.min.css">

<!-- 
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/css/bootstrap-dialog.min.css" rel="stylesheet">
 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jscripts/bootstrap3-dialog/bootstrap-dialog.min.css">

<!-- 
<link rel="stylesheet"
	href="//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.10.0/styles/default.min.css">
 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jscripts/highlight.js/default.min.css">

<!-- </Bootstrap CDN>  -->


<link
	href="${pageContext.request.contextPath}/jscripts/bootstrap-flat-3.3.4-dist/docs.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/jscripts/bootstrap-flat-3.3.4-dist/docs-flat.css"
	rel="stylesheet">


<!-- <script
	src=https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js></script>
 -->
<script
	src="${pageContext.request.contextPath}/jscripts/jquery-1.11.3.min.js"></script>


<script type="text/javascript"
	src="${pageContext.request.contextPath}/jscripts/jquery.timeout.interval.idle.js"></script>

<!-- 最新編譯和最佳化的 JavaScript -->
<!-- <link rel="stylesheet"
    href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
 -->
<!-- <script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
 -->
<script
	src="${pageContext.request.contextPath}/jscripts/jquery-ui-1.11.4/jquery-ui.js"></script>

<!-- <script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
 -->
<script
	src="${pageContext.request.contextPath}/jscripts/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>



<!-- <script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/js/bootstrap-dialog.min.js"></script>
 -->
<script
	src="${pageContext.request.contextPath}/jscripts/bootstrap3-dialog/bootstrap-dialog.min.js"></script>



<!-- <script
	src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.10.0/highlight.min.js"></script>
 -->
<script
	src="${pageContext.request.contextPath}/jscripts/highlight.js/highlight.min.js"></script>


<!-- stackable modal 的模組
 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/include/Modals/Modal_confirm.js?${applicationScope.built }"></script>

<!-- <link href="css/navbar.css" rel=stylesheet> -->
<script type="text/javascript">
	jQuery(document).ready(function() {
		$("input").eq(1).focus();
		$('.modal').on('shown.bs.modal', function() {
			$(this).focus()
		})
		$('pre#code').each(function(i, block) {
			hljs.highlightBlock(block);
		});
	});

	jQuery(document).ajaxError(function(event, jqXHR, settings, thrownError) {
		console.log("CommonHead_BootstrapFlat.jsp: .ajaxError 進行全域捕捉 error:");
		console.log("CommonHead_BootstrapFlat.jsp: event=" + event);
		console.log("CommonHead_BootstrapFlat.jsp: jqxhr.responseText=" + jqXHR.responseText);
		//console.log("settings.url=" + settings.url);
		//console.log("thrownError=" + thrownError);
	});
</script>
<style type="text/css">
span#acstyle {
	color: #00BB00;
	font-family: Arial, sans-serif;
	font-weight: bold;
}

.acstyle {
	color: #00BB00;
	font-family: Arial, sans-serif;
	font-weight: bold;
}
</style>
<meta charset="UTF-8">
<title>${prefix}${applicationScope.appConfig.title}</title>
