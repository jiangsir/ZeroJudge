<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<div style="display: inline-block;">
	<div class="input-group input-group-sm">
		<span class="input-group-addon">課程代碼</span> <input type="text"
			class="form-control" placeholder="" value="${vclass.vclasscode }">
		<span class="input-group-btn">
			<button class="btn btn-default" type="button" id="RenewVclassCode"
				data-vclassid="${vclass.id }">
				<i class="fa fa-repeat" aria-hidden="true" title="重新產生「課程代碼」"></i>
			</button>
		</span>
	</div>
</div>
