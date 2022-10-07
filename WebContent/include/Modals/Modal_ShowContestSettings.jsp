<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_ShowContestSettings_${param.contestid}" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					顯示 Contest 設定 #<span id="contestid">${param.title}</span>
				</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<label for="tname">請複製 <a
								href="ShowContest?contestid=${param.contestid}" title="">${fn:escapeXml(param.title)}</a>
								的 JSON 設定
							</label>
							<%-- 							<pre>
								<code class="language-json" id="JSONSettings">Contest JSONSettings loading...</code>
							</pre>
 --%>
							<textarea class="col-sm-12" id="JSONSettings" rows="10"
								style="font-family: Consolas, monospace; font-size: 1.1em;">
								Contest JSONSettings loading...
							</textarea>

						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
