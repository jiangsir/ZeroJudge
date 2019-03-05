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
		<p>程式碼比對</p>
		<form id="form1" name="form1" method="post" action="Diff.do">
			<table width="50%" border="0" align="center">
				<tr>
					<td scope="col"><p align="left">
							<input name="diffby" type="radio" value="bysolutionid"
								checked="checked" /> 依 solutionid 編號： <input
								name="solutionidfrom" type="text" id="solutionidfrom" size="5" />
							到 <input name="solutionidto" type="text" id="solutionidto"
								size="5" />
						</p>
						<p align="left">
							<input name="diffby" type="radio" value="bycontestid" /> 依考試編號？
							<input name="contestid" type="text" id="contestid" />
						</p></td>
				</tr>
			</table>
			<p align="center">
				<br /> 只比對 AC： <input name="diffac" type="radio" value="yes"
					checked="checked" /> 是 <input name="diffac" type="radio"
					value="no" /> 否 <br />
			</p>
			<p>
				<input type="submit" class="button" name="Submit" value="確定" />
			</p>
		</form>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
