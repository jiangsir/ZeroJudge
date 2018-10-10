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
<%-- <jsp:useBean id="systemconfigBean" class="tw.zerojudge.Beans.SystemConfigBean"/>
 --%>
<body>
	<jsp:include page="include/Header.jsp" />
	<br />
	<div class="content_individual">
		<p>
			重建管理員<br /> 目標為：可以針對 user 進行統計數據的重建<br /> 對 problem 進行統計數據的重建<br />
			由 solution 來重建所有相關數據，可以選擇純粹重建統計數據，或者一併 rejudge
		</p>
		<form id="form1" name="form1" method="post" action="ReJudgeManager">
			<table width="50%" border="0" align="center">
				<tr>
					<td scope="col"><p align="left">
							<input name="rejudgeby" type="radio" value="bysolutionid"
								checked="checked" /> 依 solutionid 編號： <input
								name="solutionidfrom" type="text" id="solutionidfrom" size="5" />
							到 <input name="solutionidto" type="text" id="solutionidto"
								size="5" />
						</p>
						<p align="left">
							<input name="rejudgeby" type="radio" value="byproblemid" /> 依
							problemid ： 列舉 - <input name="problemidlist" type="text"
								id="problemidlist" /> 請以 , 分隔
						</p>
						<p align="left">
							<input name="rejudgeby" type="radio" value="bycontestid" />
							依考試編號？ <input name="contestid" type="text" id="contestid" />
						</p>
						<p align="left">
							<input name="rejudgeby" type="radio" value="bycontestid" />
							依user account？ <input name="contestid2" type="text"
								id="contestid2" /> 以 , 分隔
						</p></td>
				</tr>
			</table>
			<div style="width: 80%; margin: auto; text-align: left;">
				<p>
					要重測的種類： <input name="status" type="checkbox" value="AC"
						checked="checked" /> AC <input name="status" type="checkbox"
						value="WA" checked="checked" /> WA <input name="status"
						type="checkbox" value="CE" checked="checked" /> CE <input
						name="status" type="checkbox" value="RF" checked="checked" /> RF
					<input name="status" type="checkbox" value="RE" checked="checked" />
					RE <input name="status" type="checkbox" value="TLE"
						checked="checked" /> TLE <input name="status" type="checkbox"
						value="MLE" checked="checked" /> MLE <input name="status"
						type="checkbox" value="OLE" checked="checked" /> OLE <input
						name="status" type="checkbox" value="SE" checked="checked" /> SE
					<input name="status" type="checkbox" value="NA" checked="checked" />
					NA <input name="status" type="checkbox" value="Waiting"
						checked="checked" /> Waiting
				</p>
				<p>
					以及重測的語言：
					<c:forEach var="compiler"
						items="${applicationScope.appConfig.serverConfig.enalbeCompilers}"
						varStatus="varstatus">
						<input name="language" type="checkbox"
							value="${compiler.language}" checked="checked" />
					</c:forEach>
				</p>

			</div>
			<p>
				<input name="ReturnPage" type="hidden"
					value="ReJudgeManager?${pageContext.request.queryString}" /> <input
					type="submit" class="button" name="Submit" value="確定重測" />
			</p>
		</form>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
