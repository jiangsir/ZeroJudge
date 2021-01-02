<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<c:if test="${!applicationScope.appConfig.isCLASS_MODE}">
	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target="#Modal_InsertUsers">批次新增/更新使用者</button>
</c:if>
<c:if test="${applicationScope.appConfig.isCLASS_MODE}">
	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target="#Modal_InsertUsers">批次新增/更新「綁定學校的學生帳號」</button>
</c:if>
