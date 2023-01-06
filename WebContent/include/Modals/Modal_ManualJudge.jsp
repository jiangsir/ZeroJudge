<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_ManualJudge_${solution.id }" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">#${solution.id }: 由評審決定該程式碼的評審結果：</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div>
								<input name="manualjudge_verdict" type="radio" value="AC" data-hint=""/> AC
								(指定通過)
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="NA" data-hint=""/> NA
								(部分得分)，得 <input name="manualjudge_score" type="text" size="5" />
								分
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="WA" data-hint=""/> WA
								(答案錯誤)
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="CE" data-hint=""/> CE
								(程式錯誤)
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="SE" data-hint=""/> SE
								(系統錯誤)
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="DN" data-hint="涉嫌抄襲"
									checked="checked" /> DN (拒絕。原因：涉嫌抄襲)
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="DN" data-hint="未遵守題目規定"/> DN
								(拒絕。原因：未遵守題目規定)
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="DN" data-hint="使用不被允許的方法"/> DN
								(拒絕。原因：使用不被允許的方法)
							</div>
							<div>
								<input name="manualjudge_verdict" type="radio" value="DN" data-hint="" /> DN
								(拒絕。原因：如下所述)
							</div>
							<div id="manualjudge_hint">
								<h5>請說明評審結果：</h5>
								<textarea name="manualjudge_hint" rows="3" class="form-control"
									placeholder="請敘明原因">涉嫌抄襲</textarea>
							</div>
							<input name="solutionid" type="hidden" value="${solution.id}" />
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="save_ManualJudge">儲存</button>
			</div>
		</div>
	</div>
</div>
