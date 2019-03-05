<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<!--  
<radio> C <radio> C++ <radio> JAVA .....

-->

<script type="text/javascript">
	jQuery(document).ready(function() {
		$("input[name='language']").each(function() {
			if ($(this).val() == $(this).data("language")) {
				$(this).prop("checked", true);
			}
		});
	});
</script>

<c:if
	test="${applicationScope.appConfig.serverConfig.enableCompilers==null}">
無法取得裁判機的資訊，請聯繫管理員。
</c:if>
<c:if
	test="${applicationScope.appConfig.serverConfig.enableCompilers!=null}">
	<c:forEach var="compiler"
		items="${applicationScope.appConfig.serverConfig.enableCompilers}"
		varStatus="varstatus">
		<input name="language" type="radio" value="${compiler.language}"
			data-language="${problem.language }" />${compiler.language}
      </c:forEach>
</c:if>
