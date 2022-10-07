<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<script type="text/javascript">
	jQuery(document).ready(function() {
	});
</script>
<div class="modal fade" id="Modal_applyProblemManager"
	data-focus-on="input:first" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">申請成為出題者</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<p>目前設定的門檻為 ${applicationScope.appConfig.getThreshold()*100}%
							，也就是您需解決系統內高於 ${applicationScope.appConfig.getThreshold()*100}%
							的問題，以確定您具備一定的程式能力，未來視實際執行情況再予以調整。</p>
						<ul>
							<li>當您成為出題者，您的帳號將會增加: 新增題目、修改題目、丟棄題目...等權限。</li>
							<li>新增題目時請盡量確認測資的正確性，並請盡可能提供參考解法，以方便驗證。題目公開後，若有使用者在討論區提出疑問，亦請積極參與討論。另，題目若是自別處引用，請註明清楚資料來源，並請盡可能取得原作者同意後再公開，以免著作權爭議。</li>
							<li>新增題目應分類正確，比如您想要加入一個 ACM/UVa 的題目，請將它歸類在
								"UVa題庫"中，請勿隨意歸類。若題目無法歸類在現有分類中，亦請通知 站務管理員 協調增加分類。</li>
							<li>題目內容，若有以下情形，則會將題目下架： <br>1. 有明顯不適當且遭致使用者投訴。 <br>2.
								原作者表達不同意轉載於此。
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_applyProblemManager_submit"
					data-action="applyProblemManager">我同意！</button>
			</div>
		</div>
	</div>
</div>
