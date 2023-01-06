<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<div class="modal fade" id="Modal_ProblemSetting_${problem.problemid}"
	tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content" id="ProblemSettings">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">快速題目設定</h4>
			</div>
			<div class="modal-body">
				<h3>${problem.problemid}. ${problem.title }</h3>
				<jsp:include page="Body/ModalBody_ProblemSetting.jsp" />
			</div>
			<div class="modal-footer">
				<button class="btn btn-default" data-dismiss="modal">Close</button>
				<button class="btn btn-primary" id="Modal_ProblemSettings_confirm"
					data-problemid="${problem.problemid }">儲存</button>
			</div>
		</div>
	</div>
</div>


<%-- 
    <form name="ProblemSettings_form" problemid="${problem.problemid}"
        method="post" action="">
        <p>指定本題目分類：</p>
        <c:forEach var="tab" items="${applicationScope.appConfig.problemtabs}">
            <input name="tabid" type="radio" value="${tab.id}"
                tabid="${problem.tabid }" />
            <span id="tabname">${tab.name}</span>: <span id="tabdescript">${tab.descript}</span>
            <br />
        </c:forEach>
               <br /> 所需程度： <input name="difficulty" type="radio" value="1"
            difficulty="${problem.difficulty}" /> 1級 <input name="difficulty"
            type="radio" value="2" difficulty="${problem.difficulty}" /> 2級 <input
            name="difficulty" type="radio" value="3"
            difficulty="${problem.difficulty}" /> 3級 <input name="difficulty"
            type="radio" value="4" difficulty="${problem.difficulty}" /> 4級 <input
            name="difficulty" type="radio" value="5"
            difficulty="${problem.difficulty}" /> 5級 <br /> <span
            style="font-size: smaller; padding-left: 10px;">說明：依照一般使用者的程度來決定困難度。</span><br />

               <br />
        <div>
            答案比對方式：<span name="judgemode" style="display: none">${problem.judgemode}</span>
            <input name="judgemode" type="radio" value="Tolerant"
                checked="checked" /> 寛鬆比對 <input name="judgemode" type="radio"
                value="Strictly" /> 嚴格比對（未完成） <input name="judgemode" type="radio"
                value="special" /> 自訂比對（未完成） <br /> <span
                style="font-size: smaller"> 寛鬆比對： 每一個答案行都經過 trim
                處理，忽略前後不可見字元及空白，並忽略空行。<br /> 嚴格比對： 就如 UVa/ACM 的比對方式，所有字元必須完全相同。 <br />自訂比對：
                由出題者自行撰寫 judge 程式來判斷解題者答案是否正確。<br />
            </span>
        </div>

        <div style="text-align: left; margin-bottom: 1em;">
            是否開放使用者觀看詳細的評分結果？ <input name="wa_visible" type="radio" value="1"
                wa_visible="${problem.ws_visible}" /> 開啟 <input
                name="wa_visible" type="radio" value="0"
                ws_visible="${problem.wa_visible}" /> 關閉<br /> <span
                style="font-size: smaller">(若想保護測試資料，可選擇"關閉"，此時解題者可得知每一個測資點的結果，但無法看到詳細的錯誤內容)</span>
        </div>

        <jsp:include page="../div/ProblemBackgrounds_INPUT.jsp" />
        <jsp:include page="../div/ProblemReference_INPUT.jsp" />

        <c:if
            test="${sessionScope.onlineUser.isDEBUGGER || sessionScope.onlineUser.generalManager}">
            <div class="ManagerOnly">
                <fmt:message key="Problem.Keywords" />
                : <input name="keywords" type="text" id="keywords"
                    value="${problem.keywords}" size="50" /> (請使用 , 作為分隔。例： [DFS,
                台北市資訊學科競賽, 資訊學科競賽] ) <br /> sortable： <input name="sortable"
                    type="text" id="sortable" value="${problem.sortable }" /> (例： 2011
                TOI初賽) <br /> (目前題目分類當中的“競賽題庫”, "NPSC", "TOI/NOI" 都是以 sortable
                倒序作為第一排序原則， problemid 順序作為第二排序原則。因此可用年份為 sortable，因而可以讓較新的題目在前。)<br />
            </div>
        </c:if>
    </form>

 --%>
