<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<c:if test="${problemlevel.level==0}">
	<button class="btn btn-info btn-xs">未分級</button>
</c:if>
<c:if test="${problemlevel.level>0}">
	<button class="btn btn-info btn-xs">${problemlevel.levelname}
		${problemlevel.level}級</button>
</c:if>
<c:if test="${problemlevel.level>=6}">
	<button class="btn btn-default btn-xs">${problemlevel.levelname}
		5級以上</button>
</c:if>
