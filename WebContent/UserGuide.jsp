<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="include/CommonHead_BootstrapFlat.jsp" />
</head>
<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<div class="container">
		<div class="row">
			<strong>程式解題系統使用手冊</strong><br />
			<table class="table table-hover">
				<tr>
					<td valign="top">*</td>
					<td>Online Judge 的運作原理</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
					<td><p>裁判系統會以數量不一的測試資料去測試您所送出的程式碼來判定您的程式是否完全正確，若通過所有的測試資料的測驗，則系統認定您的程式碼為通過，否則將會呈現不同原因的結果，如
							NA, WA, TLE, MLE, OLE, CE, RE...等。因此必須在程式中放入一個 while
							迴圈來讀取所有的測試資料。程式碼請參考 a001。</p> 系統特色：
						<ul>
							<li>可以告訴使用者錯誤答案發生在第幾行，方便除錯。</li>
							<li>使用者參與管理工作，使用者可利用系統出題目以及舉辦考試。</li>
							<li>可直接分享程式碼，以及站內簡訊進行討論。</li>
							<li>可預約競賽，時間一到即可自動開始，並自動結束，不需管理者操作。</li>
							<li>可在系統上進行開課，可成為一個開課平臺。</li>
						</ul></td>
				</tr>
				<tr>
					<td valign="top">*</td>
					<td>主要對象</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
					<td>主要對象為程式語言初學者</td>
				</tr>
				<tr>
					<td valign="top">*</td>
					<td>錯誤訊息的意義</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
					<td><p>
							AC: Accept 即表示通過<br /> NA: Not Accept 表示在多個測資點的情況下，有部分測資點無法通過<br />
							WA: Wrong Answer 表示答案錯誤, 請仔細比對，務必符合題目要求<br /> TLE: Time Limit
							Exceed 表示執行超過時間限制<br /> MLE: Memory Limit Exceed 表示程序超過記憶體限制<br />
							OLE: Output Limit Exceed 表示程序輸出超過限制<br /> RE: Runtime Error
							表示執行時錯誤，通常為記憶體配置錯誤 如：使用了超過陣列大小的位置<br /> RF: Restricted Function
							表示使用了被禁止使用的函式，並在錯誤訊息中指明使用了什麼不合法的函式。<br /> CE: Compile Error
							表示編譯錯誤<br /> SE: System Error 包含 Compile, Runtime 等未定義的錯誤均屬於
							System Error <br /> <br /> 由於本系統以初學者為導向，因此錯誤訊息儘可能提供最詳細的錯誤資訊，如
							WA 訊息會告知您正確答案(題目測資已公開時)，以及在第幾組資料時發生，讓使用者比較容易判斷錯誤發生在哪哩，而不會像在
							ACM/UVa 裡明明做對了，但是就是有莫名的格式錯誤，卻找不出格式錯誤在哪的困境。
						</p></td>
				</tr>
				<tr>
					<td valign="top">*<a name="InsertProblem" id="InsertProblem"></a></td>
					<td>題目的進場/退場機制</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
					<td><p align="left">
							本系統的特色之一是持 Open Source/WIKI 的概念，由廣大使用者共同參與共同成長的理念開放解題 30%
							以上的使用者可以自行新增題目。這是 Online Judge
							系統的創舉。但為了避免有問題的題目充斥，因此特別訂定“題目退場機制”。<br /> 凡符合以下事項 1
							項含以上者，&quot;站務管理員&quot;保留將題目下架之權利。<br /> *
							題目內容易引發爭執者，如：宗教、政治議題、種族議題、歷史情結、文化衝突...等。<br /> *
							容易引發閱讀者不快者，如犯罪、色情、賭博、人身攻擊、影射...等不當文字。<br /> *
							題目敘述有誤或不完整、測資有誤，致使使用者無法作答者<br /> * 進行廣告或置入性行銷者<br /> *
							與其它題目高度雷同或完全相同者<br /> * 涉及著作人格權問題者，包含冒名投題、原作者提出異議並主張下架者...等<br />
							* 內含其它不當內容且遭致使用者投訴者<br /> <br /> 本辦法未盡周全之處，得經站務會議決議後修訂。
						</p>
						<p align="right">
							本辦法訂定時間 2009-05-08<br /> (2009-07-06 第一次修訂) <br /> <br />
						</p></td>
				</tr>
				<tr>
					<td valign="top">*</td>
					<td><p>使用者言論管理辦法</p></td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
					<td><p>使用者違反下列規定時，站務管理員可依情況輕重限制使用者之使用權限，處理原則如下：</p>
						<p>違規定義及標準:</p>
						<p>以下所稱“言論”包含匿稱、帳號、討論區文章、題目內容、簡訊...等任何在 ZeroJudge
							內發布的文字或圖片等相關資訊</p>
						<p>
							一、不當言論<br /> 1、違反中華民國現行法律法規 <br />
							2、涉及宗教、政治議題、種族議題、歷史情結、文化衝突...等<br />
							3、涉及犯罪、色情、賭博、人身攻擊、影射、隱私...等不當文字<br /> 4、在討論版要求貼出完整程式碼或是測資的正確輸出者。
						</p>
						<p>
							二、網路濫用、攻擊<br /> 以任何形式的網路技巧濫用或攻擊，使得使用者無法順利使用網站功能。
						</p>
						<p>
							三、廣告<br /> 請勿在言論當中進行商業廣告或置入性行銷
						</p>
						<p>本辦法未盡周全之處，得經站務會議決議後修訂。</p>
						<p align="right">
							本辦法訂定時間 2009-07-06 <br /> <br />
						</p></td>
				</tr>
				<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
					<tr>
						<td valign="top">*</td>
						<td>站務管理員權責</td>
					</tr>
					<tr>
						<td valign="top">&nbsp;</td>
						<td><p>總計至目前(2010-07)為止，站務管理員共2位，分別為 magrady, MAPLEWING。</p>
							<ul>
								<li>主辦站內常規競賽。</li>
								<li>審查其他題目管理員所提交的題目，並有權決定是否公開。</li>
								<li>既有題目若發現錯誤，並負責修正。</li>
								<li>依據『使用者言論管理辦法』，管理討論區言論。</li>
								<li>管理惡意程式碼。</li>
								<li>維護系統穩定及錯誤修正。</li>
							</ul></td>
					</tr>
				</c:if>
				<tr>
					<td valign="top" id="compiler">*</td>
					<td>各項解題語言所使用的系統編譯器</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
					<td><a name="Samplecode"></a> <jsp:include
							page="include/div/ShowServerConfig.jsp" /></td>
				</tr>
				<tr>
					<td valign="top">*</td>
					<td>後記：關於 ZeroJudge</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
					<td><p>
							ZeroJudge 是一個 Online Judge
							系統亦即線上解題系統，中文來說有許許多多不同的翻譯名詞，諸如：在線裁決系統、在線評判系統、在線提交系統...等等不一而足，建議還是直接稱呼
							Online Judge
							系統。主要是用來讓有意練習程式語言的人士(主要是學生)，有一些題目可以練習，並且可以知道自己所寫的程式是不是正確。<br />
							<br /> 目前世界上最著名的 Online Judge 便是大名鼎鼎的 UVa Online Judge
							System，沒聽過？那是因為大家都叫他 ACM。至於其中原因請參考 DJWS 所寫的『<a
								href="http://www.csie.ntnu.edu.tw/~u91029/oj.html">Online
								Judge System 起源與由來</a>』。目前由西班牙 Valladolid (巴亞多利)大學(<a
								href="http://online-judge.uva.es/problemset/" target="_blank">http://online-judge.uva.es/problemset/</a>)所維護，目前已經有2000個題目，讓全世界的程式愛好者們想辦法去解決，這是全世界對寫程式有興趣的人一個練習的好地方。<br />
							<br />
							然而，UVa(ACM)題目大多數都是需要用到許多演算法及程式技巧，對於中上程度的程式學習者具有很好的練習效果，但對於初學者，可就會覺得障礙連連，深受打擊了。<br />
							<br />
							作者(Jiangsir)所服務學校(國立高雄師大附中)練習程式設計的學生也遭遇類似的情況，因此曾積極尋找備有初級題目的
							Online Judge
							系統(首先排除國外網站，因為語言障礙足以擊退絕大多數初學者)，可惜無法如願，並且發現此時(2006)國內各大學尚未具備公開上線，可供練習的
							Online Judge 系統。也曾嘗試考慮北京大學開發的(Peking
							JudgeOnline)，但該系統並未開放原始碼許多功能無法動手修改。最後終於決定自行開發，於是 ZeroJudge
							計畫誕生，代表從零開始。<br /> <br />
							94學年度首度運用在學生課堂程式考試上，該版本為手動驗證，由人工判斷程式是否正確，此時僅為一個線上測驗系統。<br />
							2006年中，改寫整個操作模式，使其朝向 Online Judge 架構發展，仍應用於程式考試上。<br />
							同年，完成自動裁判服務 (Judge Server)，可判斷出 Accept, Wrong Answer, Time Limit
							Exceed, Runtime Error, Restricted Functions, Compile Error,
							System Error...等狀態，至此可算是一個初步堪用的 Online Judge 系統。<br />
							2007年初，公開上線供公眾練習使用，同時借此檢驗系統穩定度，並逐步發掘問題，逐步修正。<br />
							2007年中，發展完成多重判決(分段計分)及條件式判決(Special
							Judge)，因此可以正確判定可列舉的多重解答以及難以列舉或甚至無法列舉，但有規則可循的不特定測資。<br />
							2007年底，獲教育部全國校園軟體創意競賽 教學應用及自由創意組 第二名。<br />
							2008年初，實作完成多人管理模式，使用者可增加題目及舉辦考試，正式具備使用者參與的特性。<br />
							2008年底，順利舉辦第一屆海峽兩岸青少年程式設計競賽，報名人數達
							2千人以上。通過這場大規模的競賽可證明系統無論是在效能、穩定度方面均具備一定的水準。<br />
							2009年，獲得教育部『國際大專院校程式設計推廣與培訓計劃』採用作為解題平臺，網址： <a
								href="http://140.122.185.166/ZeroJudge/">
								http://ptc.moe.edu.tw/ </a> 。<br />
							2010年，獲『高雄市資訊學科能力競賽複賽』採用作為競賽系統，並順利完成該項競賽。<br /> 2011年，提交之程式碼突破
							100 萬筆。題目數量突破 1000 題。<br />2014年，提交之程式碼達到 200 萬筆。<br />2016年，提交之程式碼達到
							300 萬筆。<br />2017年，ZeroJudge 虛擬機版本開始提供測試。<br />
						</p>
						<p>
							<br />
						</p></td>
				</tr>
			</table>
		</div>
		<br />
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
