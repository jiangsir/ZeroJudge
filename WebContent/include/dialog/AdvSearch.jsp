<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div id="advsearch_dialog"
	style="display: none; cursor: default; padding: 20px; margin: auto;">
	<div
		style="text-align: left; width: 80%; margin: 10px; padding: 10px; margin: auto;">
		<form name="advsearch_form" method="post" action="">
			<h2>請選擇要篩選的條件：</h2>
			<br /> <br /> 背景知識查詢： <input name="background" type="text"
				size="30" /> <span id="clean_background" class="FakeLink">清除</span>
			<br /> <span style="font-size: smaller"> [ <c:forEach
					var="background" items="${suggest_backgrounds}">
					<span id="background" class="FakeLink">${background}</span>
				</c:forEach> ]
			</span><br /> <br />
			<c:if
				test="${sessionScope.onlineUser.isDEBUGGER}">
				<span class="GeneralManagersOnly"> <br /> 題目分類：<br /> <c:forEach
						var="tab" items="${tabs}" varStatus="varstatus">
						<input name="tab_search" type="checkbox" value="${tab.id}" />
          ${tab.name} </c:forEach> <br /> <br /> 程度篩選：<br /> <input
					name="difficulty" type="checkbox" value="1" /> 程度1 <input
					name="difficulty" type="checkbox" value="2" /> 程度2 <input
					name="difficulty" type="checkbox" value="3" /> 程度3 <input
					name="difficulty" type="checkbox" value="4" /> 程度4 <input
					name="difficulty" type="checkbox" value="5" /> 程度5 <br /> 關鍵字：<br />
					<span style="font-size: smaller"> [ <c:forEach var="keyword"
							items="${suggest_keywords}">
							<span id="keyword" class="FakeLink">${keyword}</span>
						</c:forEach> ]
				</span><br /> <br /> 關鍵字查詢： <input name="keyword" type="text" /> <span
					id="clean_keyword" class="FakeLink">清除</span> <br />
				</span>
			</c:if>
			<br />
		</form>
	</div>
</div>
