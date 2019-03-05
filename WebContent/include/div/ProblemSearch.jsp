<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<div id="ProblemSearch" style="margin: 0px; display: inline;">
	<form name="form2" method="post" action="Problems"
		style="margin: 0px; display: inline;" onsubmit="checkForm(this);">
		全文檢索： <input name="searchword" type="text" value="" size="10" />
	</form>
</div>
