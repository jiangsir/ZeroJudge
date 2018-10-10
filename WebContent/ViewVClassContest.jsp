<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="include/CommonHead.jsp" />
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<%-- <jsp:useBean id="ENV" class="tw.zerojudge.Utils.ENV" />
<jsp:useBean id="now" class="java.util.Date" />
 --%>
<!-- <script type="text/javascript" src="jscripts/js_date.js"></script>
 -->
<script type="text/javascript"
	src="jscripts/jquery.timeout.interval.idle.js"></script>
<script language="javascript">
	jQuery(document).ready(function() {
		//var stoptime = parseInt(${taskmap.stoptime.time});
		//var nowtime = parseInt(${now.time});
		//countdown( stoptime-nowtime );
		//mytime(parseInt(${now.time}));
	});

	/* function mytime(nowtime){
	 var nowdate = new Date();
	 nowdate.setTime(nowtime);
	 jQuery("#nowdate").text( formatDate( nowdate, "y-MM-dd hh:mm:ss") );
	 jQuery.interval(
	 function(){
	 var nowdate = new Date();
	 nowtime = nowtime + 1000;
	 //  alert("nowtime="+nowtime);
	 nowdate.setTime(nowtime);
	 jQuery("#nowdate").text( formatDate( nowdate, "y-MM-dd hh:mm:ss") );
	 },1000);
	 } */

	function countdown(countdown) {
		var secs = (countdown - (countdown % 1000)) / 1000;
		var mins = (secs - (secs % 60)) / 60;
		var hours = (mins - (mins % 60)) / 60;
		var days = (hours - (hours % 24)) / 24;
		var str = "倒數: " + hours % 24 + " 小時 " + mins % 60 + " 分鐘 " + secs % 60
				+ " 秒";
		jQuery("#countdown").text(str);
		jQuery.interval(function() {
			countdown = countdown - 1000;
			var secs = (countdown - (countdown % 1000)) / 1000;
			var mins = (secs - (secs % 60)) / 60;
			var hours = (mins - (mins % 60)) / 60;
			var days = (hours - (hours % 24)) / 24;
			var str = "倒數: " + hours % 24 + " 小時 " + mins % 60 + " 分鐘 " + secs
					% 60 + " 秒";
			jQuery("#countdown").text(str);
		}, 1000);

	}
</script>
</head>
<jsp:useBean id="contestbean" class="tw.zerojudge.Beans.ContestBean" />
<jsp:setProperty name="contestbean" property="session_account"
	value="${sessionScope.onlineUser.account}" />
<jsp:setProperty name="contestbean" property="contestid"
	value="${param.contestid}" />
<jsp:setProperty name="contestbean" property="locale"
	value="${sessionScope.session_locale}" />

<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<c:set var="contest" value="${contest}" scope="request" />
		<jsp:include page="include/ContestHeader.jsp" />

		<%-- 		<div
			style="width: 80%; margin: auto; text-align: center; font: bolder; font-size: 20px;">${fn:escapeXml(contest.title)}</div>
		<c:if test="${contest.subtitle!=''}">
			<div class="subtitle">
				<pre>${fn:escapeXml(contest.subtitle)}</pre>
			</div>
		</c:if>
 --%>
		<%-- 		<c:if test="${!contest.isStopped}">
			<fmt:message key="Contest.Now" />：<span id="nowdate"></span>
			<br />
		</c:if>
 --%>
		<%-- 		<c:if test="${contest.isRunning}">
			<span id="countdown"></span>
		</c:if>
	 --%>
		<div
			style="width: 80%; margin: auto; text-align: right; font-size: 12px">
			<jsp:include page="include/ContestInfo.jsp" />
		</div>

		<c:choose>
			<c:when test="${contest.isSuspending}">
				<fmt:message key="Contest.ContestSuspendingPlaceWait" />
			</c:when>
			<c:when
				test="${sessionScope.onlineUser.joinedcontestid==contest.id && contest.isStarting}">您已經成功進入隨堂測驗中。<br />
      測驗將於
      <fmt:formatDate value="${contest.starttime}"
					pattern="yyyy-MM-dd HH:mm:ss" />
      開始，題目將在測驗開始後顯示，請稍候！<br />
				<br />
				<c:if test="${contestbean.visible_leaveContest}">
					<br />
					<form action="./KickedUser" method="get">
						<input name="userid" type="hidden"
							value="${sessionScope.onlineUser.id}" /> <input name="contestid"
							type="hidden" value="${contest.id}" /> <input type="submit"
							name="submit" class="button" value="離開" />
					</form>
				</c:if>
				<br />
			</c:when>
			<c:otherwise>
				<p>
					<a href="./ShowVClass?vclassid=${contest.vclassid}"> 回本課程 </a> | <a
						href="./ContestSubmissions?contestid=${param.contestid}"> <fmt:message
							key="Contest.Submissions" />
					</a> | <a href="./ContestRanking?contestid=${param.contestid}"> <fmt:message
							key="Contest.ContestResult" />
					</a><br />
				</p>
				<c:choose>
					<c:when test="${fn:length(problems)==0}"> 沒有題目！！ <br />
					</c:when>
					<c:otherwise>
						<table width="70%" border="0" align="center">
							<tr>
								<td><div align="left">
										<fmt:message key="Contest.CodeID" />
									</div></td>
								<td>&nbsp;</td>
								<td width="60%"><div align="left">
										<fmt:message key="Contest.Problem" />
									</div></td>
								<td>配分</td>
							</tr>
							<c:forEach var="problem" items="${problems}"
								varStatus="varstatus">
								<tr>
									<td><fmt:message key="Contest.ProblemCount">
											<fmt:param value="${varstatus.count}" />
										</fmt:message></td>
									<td>&nbsp;</td>
									<td width="60%"><c:set var="problem" value="${problem}"
											scope="request" /> <jsp:include
											page="include/div/ProblemTitle.jsp" /></td>
									<td>${fn:split(contest.scores, ',')[varstatus.count-1]}%</td>
								</tr>
							</c:forEach>
						</table>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		<p></p>
		<c:if test="${sessionScope.onlineUser.isDEBUGGER}">
			<div class="DEBUGGEROnly">
				<a href="./ContestSubmissions?contestid=${contest.id}">ContestSubmissions</a>
				| <a href="./ContestRanking?contestid=${contest.id}">ContestRanking</a>
			</div>
		</c:if>
		<br />
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
