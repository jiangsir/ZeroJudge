<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_InsertUsers" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">新增一群使用者</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div>
								請依據一下格式指定要新增的使用者。<br /> #帳號,匿稱,真實姓名,密碼明碼,生日,E-mail,學校 id<br />
								例： Tommy091,Tommy,王湯米,tommy1234,1990,tommy091@gmail.com,0<br />
								<textarea id="tcontent" rows="20" class="form-control"
									name="userscripts"
									placeholder="#Tommy091,Tommy,王湯米,tommy1234,1990,tommy091@gmail.com,0"></textarea>

								<!-- <textarea id="userscripts" name="userscripts" rows="25">#Tommy091,Tommy,王湯米,tommy1234,1990,tommy091@gmail.com,0</textarea> -->
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="modal_insertUsers_change">儲存</button>
			</div>
		</div>
	</div>
</div>
