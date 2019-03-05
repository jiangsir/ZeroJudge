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

<script type="text/javascript"
	src="ShowIMessages.js?${applicationScope.built }"></script>

<%-- <script type="text/javascript"
	src="include/dialog/Confirm.js?${applicationScope.built }"></script>
 --%>
<style type="text/css" media="screen">
.tr_background {
	background-color: #f2f2f2;
}

pre {
	width: 640px;
	white-space: pre-wrap; /* css-3 */
	white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
	white-space: -pre-wrap; /* Opera 4-6 */
	white-space: -o-pre-wrap; /* Opera 7 */
	word-wrap: break-word; /* Internet Explorer 5.5+ */
}
</style>


</head>
<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<body>
	<jsp:include page="include/Header_Fixed_Top.jsp" />
	<jsp:include page="include/Modals/Modal_SendIMessage.jsp" />

	<div class="container">
		<div class="row">

			<button type="button" class="btn btn-primary"
				data-target="#Modal_SendIMessage">
				<fmt:message key="IMessage.Send" />
			</button>

			<hr>
			<ul class="nav nav-tabs" role="tablist">
				<li class="active"><a href="?action=inboxIM#inboxIM"
					aria-controls="inboxIM" role="presentation"><fmt:message
							key="IMessage.Inbox" /></a></li>
				<li><a href="?action=sentIM#sentIM" aria-controls="sentIM"
					role="presentation"><fmt:message key="IMessage.Sentbox" /></a></li>
			</ul>
			<%-- 			<ul id="tabmenu">
							<li class="unreadIM"><a href="?action=unreadIM"><fmt:message
						key="IMessage.Unread" />(${fn:length(sessionScope.unreadIMmessages)})</a></li>

				<li class="inboxIM"><a href="?action=inboxIM"><fmt:message
							key="IMessage.Inbox" /></a></li>
				<li class="sentIM"><a href="?action=sentIM"><fmt:message
							key="IMessage.Sentbox" /></a></li>
			</ul>
 --%>
			<c:choose>
				<c:when test="${fn:length(targetIM)!=0}">
					<table class="table table-hover">
						<tr>
							<th scope="col"><fmt:message key="IMessage.ID" /></th>
							<th scope="col">&nbsp;</th>
							<th scope="col"><fmt:message key="IMessage.Sender" /> <c:if
									test="${param.action=='sentIM'}">/<fmt:message
										key="IMessage.Receive" />
								</c:if></th>
							<th width="50%" scope="col"><fmt:message
									key="IMessage.Subject" /></th>
							<th scope="col"><fmt:message key="IMessage.Time" /></th>
							<th scope="col"><fmt:message key="IMessage.Operate" /></th>
						</tr>
						<c:forEach var="imessage" items="${targetIM}" varStatus="status">
							<tr>
								<th scope="row">${status.count+((pagenum-1)*applicationScope.appConfig.pageSize)}</th>
								<td><c:if test="${param.action!='sentIM'}">
										<img src="./images/IMessage_${imessage.receiver_status}.svg"
											style="height: 1.2em;" id="imageid${imessage.id}"
											imessageid="${imessage.id}" title="已讀/未讀" />
										<%-- <a href="./ReplyIMessage?imessageid=${imessage.id}"
											title="回覆訊息"><img src="./images/IMessage_forward.svg"
											style="height: 1.2em;" /></a> --%>
									</c:if></td>
								<td><c:choose>
										<c:when test="${param.action=='sentIM'}">
											<div style="font-weight: bold">
												<fmt:message key="IMessage.Me" />
												->${imessage.receiver}
											</div>
										</c:when>
										<c:when test="${imessage.isReceiver_status_unread}">
											<div style="font-weight: bold">${imessage.sender}</div>
										</c:when>
										<c:otherwise>
											<div>${imessage.sender}</div>
										</c:otherwise>
									</c:choose></td>
								<td id="subject" width="50%" imessageid="${imessage.id}"><a
									href="#" id="readIM" imessageid="${imessage.id }">${fn:escapeXml(imessage.subject)}</a>
								</td>
								<td><div align="right" style="font-size: smaller">
										<fmt:formatDate value="${imessage.senttime}"
											pattern="yyyy-MM-dd HH:mm" />
									</div></td>
								<td>
									<%-- 										<jsp:include page="include/dialog/Confirm.jsp">
											<jsp:param name="title"
												value="確定要丟棄嗎(${fn:escapeXml(imessage.subject)})?" />
											<jsp:param name="type" value="POST" />
											<jsp:param name="url" value="IMessage.api" />
											<jsp:param name="data"
												value="action=doDelete&imessageid=${imessage.id}" />
										</jsp:include>
 --%> <!-- 										<img src="images/delete18.svg" style="height: 1.2em;"
											title="刪除" class="confirm" /> -->
									<div class="btn-group btn-group-sm" role="group"
										aria-label="...">
										<button type="button" class="btn btn-default"
											data-target="#Modal_SendIMessage"
											data-receiver="${imessage.sender }"
											data-subject="Re: ${fn:escapeXml(imessage.subject) }"
											data-content="${fn:escapeXml(imessage.replyContent)}"
											title="回覆">
											<i class="fa fa-reply" aria-hidden="true"></i>
										</button>
										<button type="button" class="btn btn-default"
											data-toggle="modal" data-target="#Modal_confirm"
											data-title="刪除"
											data-content="確定要刪除站內信(${fn:escapeXml(imessage.subject)})？"
											data-type="POST" data-url="./IMessage.api"
											data-qs="action=doDelete&imessageid=${imessage.id}">
											<span class="glyphicon glyphicon-remove"></span>
										</button>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="6"><pre imessageid="${imessage.id }"
										style="display: none;" class="imcontent">${fn:escapeXml(imessage.content)}</pre></td>
							</tr>
						</c:forEach>
					</table>
				</c:when>
				<c:otherwise>
					<div align="center">
						<fmt:message key="IMessage.NO_DATA" />
					</div>
				</c:otherwise>
			</c:choose>
			<jsp:include page="include/Pagging.jsp">
				<jsp:param name="querystring" value="${querystring}" />
			</jsp:include>
			<ul id="tabmenu"></ul>
		</div>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
