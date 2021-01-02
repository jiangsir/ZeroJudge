<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<%-- <div style="text-align: left;">
	<strong>出處：</strong> <input name="reference" type="text" id="reference"
		style="width: 80%" value="${fn:escapeXml(problem.reference)}" /> <span
		id="clean_reference" class="FakeLink">清除</span><br />
	<span
		style="font-size: smaller">"出處"： [ <c:forEach var="reference"
			items="${suggest_references}">
			<span id="reference" class="FakeLink">${reference}</span>
		</c:forEach> ] </span>
	<div style="font-size: smaller; margin-left: 1em;">
		出處請用 , 隔開，可作為標籤查詢。比如 [2013,高雄市,資訊學科能力,複賽] <br /> <span>注意：出處請填寫完整，以免引起著作權爭議，可能的話，並請取得原作者同意再公開。</span>
	</div>
</div>

 --%>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("button[id='clean_reference']").click(function() {
			jQuery("input[name='reference']").val("[]");
		});
	});
</script>
<div class="row">
	<div class="col-lg-12">
		<div class="input-group">
			<span class="input-group-addon" id="sizing-addon2">出處</span> <input
				type="text" class="form-control" placeholder="輸入題目出處" title="輸入題目出處"
				name="reference" value="${fn:escapeXml(problem.reference)}">
			<span class="input-group-btn">
				<button class="btn btn-default" type="button" id="clean_reference">清除</button>
			</span>
		</div>
		<small> 出處請用 , 隔開，可作為標籤查詢。比如 [2013,高雄市,資訊學科能力,複賽] <br /> <span>注意：出處請填寫完整，以免引起著作權爭議，可能的話，並請取得原作者同意再公開。</span>
		</small>
	</div>
</div>
