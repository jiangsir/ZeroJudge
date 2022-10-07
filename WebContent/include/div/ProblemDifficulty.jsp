<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<c:if test="${problem.difficulty==1}">
	<button class="btn btn-info btn-xs">APCS
		${problem.difficulty}級</button>
</c:if>
<c:if test="${problem.difficulty==2}">
	<button class="btn btn-success btn-xs">APCS
		${problem.difficulty}級</button>
</c:if>
<c:if test="${problem.difficulty==3}">
	<button class="btn btn-primary btn-xs">APCS
		${problem.difficulty}級</button>
</c:if>
<c:if test="${problem.difficulty==4}">
	<button class="btn btn-warning btn-xs">APCS
		${problem.difficulty}級</button>
</c:if>
<c:if test="${problem.difficulty==5}">
	<button class="btn btn-danger btn-xs">APCS
		${problem.difficulty}級</button>
</c:if>
<c:if test="${problem.difficulty==0}">
	<button class="btn btn-default btn-xs">未分級</button>
</c:if>
