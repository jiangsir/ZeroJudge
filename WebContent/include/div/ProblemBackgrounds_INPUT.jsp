<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("button[id='clean_background']").click(function() {
			jQuery("input[name='backgrounds']").val("[]");
		});
	});
</script>
<div class="row">
	<div class="col-lg-12">
		<div class="input-group">
			<span class="input-group-addon" id="sizing-addon2">標籤</span> <input
				type="text" class="form-control" placeholder="輸入題目標籤" title="輸入題目標籤"
				name="backgrounds" value="${fn:escapeXml(problem.backgrounds)}">
			<span class="input-group-btn">
				<button class="btn btn-default" type="button" id="clean_background">清除</button>
			</span>
		</div>
		<small> 請用 , 隔開。請為您的題目加上「標籤」，會更容易讓使用者找到。比如 [GCD, DP, 最短路徑] [ <c:forEach
				var="background" items="${suggest_backgrounds}">
				<span id="background" class="FakeLink">${background}</span>
			</c:forEach> ]
		</small>
	</div>
</div>
<!-- /input-group -->
