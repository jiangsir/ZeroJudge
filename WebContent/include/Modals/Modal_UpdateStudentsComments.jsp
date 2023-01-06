<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_UpdateStudentsComments" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel"
	data-vclassid="${param.vclassid}"
	data-isclassmode="${applicationScope.appConfig.isCLASS_MODE}">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">批次更新「註解」</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-12">
							<div>
								<input name="vclassid" value="${param.vclassid}" hidden="hidden" />

								<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
								請依據以下格式指定要更新的學生註解。<br />
									<br />
									<samp>帳號, 註解</samp>
									<br /> 例：<br />
									<samp>
										1081101,高1仁班_01號_王阿明<br> 1081102,高1仁班_02號_黃阿牛
									</samp>
									<textarea id="tcontent" rows="15" class="form-control"
										name="csv_UpdateStudentsComments"
										placeholder="1081101,高1仁班_01號王阿明"></textarea>
								</c:if>
								<c:if test="${applicationScope.appConfig.isCLASS_MODE}">
								「課程模式」下，學生帳號定義為「學號」，相同學校內學號不可重複。<br>請先建立學校，再取得 schoolid<br />
									<br />
									<samp>#schoolid, 學號, 註解</samp>
									<br /> 例：<br />
									<samp>
										1,1081101,高1仁班_01號_王阿明<br> 1,1081102,高1仁班_02號_黃阿牛
									</samp>
									<textarea id="tcontent" rows="15" class="form-control"
										name="csv_UpdateStudentsComments"
										placeholder="1, 1081101,高1仁班_01號王阿明">
										</textarea>
								</c:if>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary"
					id="submit_UpdateStudentsComments"
					data-vclassid="${param.vclassid}">更新「註解」</button>
			</div>
		</div>
	</div>
</div>
