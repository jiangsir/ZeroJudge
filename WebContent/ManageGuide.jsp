<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
</head>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<strong>程式解題系統管理手冊</strong><br />
		<table width="90%" cellpadding="5">
			<!-- 			<tr>
				<td valign="top">* <a name="infilepath" id="infilepath"></a>測資輸出入原則
				</td>
			</tr>
 -->
			<!-- 			<tr>
				<td valign="top">直接在此輸入測資數據，會直接產生相對應的測資檔<br /> 若測資很大，如超過 1MB
					，則請直接於系統資料夾 ./testdata/ 裡建立測資檔
				</td>
			</tr>
 -->
			<!-- 			<tr>
				<td valign="top">* <a name="alteroutdata" id="alteroutdata"></a>多重解的設定原則
				</td>
			</tr>
			<tr>
				<td valign="top">以列為單位，主測資檔該列如果比對不符，<br />就會比對此處的多重解，格式為：<br />行號:解答<br />
					同一行號可以列出一行以上的替代解答，只要任何一個解答符合就認定該行為正確。<br /> (冒號後緊接著解答，勿任意添加空白)
				</td>
			</tr>
 -->
			<tr>
				<td valign="top">* <a name="specialjudge" id="specialjudge"></a>自訂評分(Special
					Judge)
				</td>
			</tr>
			<tr>
				<td valign="top"><p>
						自訂評分：不特定解答或者答案可由某些規則來判斷，即可使用『自定評分』。<br />
						如：給定一個空格很多的數獨題目，即有可能產生很多個正解，可以經由分析使用者給出的答案是否符合輸入測資的模型以及是否為一個數獨正解即可確定使用者解題是否正確。<br />
						請注意，Special Judge 高度依賴題目所要求的輸出格式，因此Special
						Judge評分程式應由出題者親自撰寫，並放入系統資料夾 $SPECIAL_PATH 中。
					</p>
					<p>
						出題者撰寫 Special Judge 評分程式注意事項：<br /> 1. 評分程式必須由指令行讀入 3
						個參數，分別為"標準輸入測資"，"標準輸出測資&quot;，&quot;使用者輸出檔&quot;。<br /> 2.
						評分程式必須利用標準螢幕輸出(cout or printf)回報結果，回報結果必須依據以下格式
					</p>
					<ul>
						<li>$JUDGE_RESULT=AC or WA</li>
						<li>$CASES=? ：回報使用者錯誤發生在第幾個 test case，也可以不回報。</li>
						<li>$LINECOUNT=? ：回報使用者錯誤發生在第幾行，也可以不回報。</li>
						<li>$USEROUT=xxx ：回報使用者所輸出的答案。</li>
						<li>$SYSTEMOUT=xxx ：回報系統標準答案。</li>
						<li>$MESSAGE=xxx：回報使用者相關訊息，訊息的詳細程度由出題者自行決定。</li>
					</ul> <!-- 					因為評分程式是在系統上獨立執行，因此任何可以在 linux 上獨立執行的程式均可以成為評分程式，如 perl, shell
					script, JAVA, C, C++, python, PHP...等
 --></td>
			</tr>
		</table>
		<p>&nbsp;</p>
	</div>
	<br />
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
