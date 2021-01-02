<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<p>&nbsp;</p>
			<div class="contestbox">
				<div align="center">
					<p>增加題目相關說明：</p>
				</div>
				<p style="padding: 0px 10px 0px 10px; font-size: small">
					目前設定的門檻為
					<fmt:formatNumber value="${Threshold}" type="percent" />
					，也就是您需解決高於
					<fmt:formatNumber value="${Threshold}" type="percent" />
					的問題，以確定您具備一定的程式能力，未來視實際執行情況再予以調整。
				</p>
				<ul>
					<li>當您成為出題者，您的帳號將會增加 <br /> 1. 測驗管理：包含新增測驗、管理參加者、...等。<br />
						2. 題目管理：包含新增題目、修改題目、丟棄題目...等。<br /> 共兩組權限。
					</li>
					<li>新增題目時請盡量確認測資的正確性，並請盡可能提供參考解法，以方便驗證。題目公開後，若有使用者在討論區提出疑問，亦請積極參與討論。另，題目若是自別處引用，請註明清楚資料來源，並請盡可能取得原作者同意後再公開，以免著作權爭議。</li>
					<li>新增題目應分類正確，比如您想要加入一個 ACM/UVa 的題目，請將它歸類在
						&quot;UVa題庫&quot;中，請勿隨意歸類。若題目無法歸類在現有分類中，亦請通知 站務管理員 協調增加分類。</li>
					<li>題目內容，若有以下情形，則會將題目下架：<br /> 1. 有明顯不適當且遭致使用者投訴。<br /> 2.
						原作者表達不同意轉載於此。<br />
					</li>
				</ul>
				<form id="form1" name="form1" method="post" action="Application">
					<div align="center">
						<input type="submit" class="btn btn-success" name="Submit" value="成為出題者" />
					</div>
				</form>
				<br />
			</div>
			<p>
				<br />
			</p>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
