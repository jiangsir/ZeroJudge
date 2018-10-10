<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="solution" uri="http://jiangsir.tw/jstl/solution"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />

<script type="text/javascript">
	jQuery(document).ready(function() {
		var contestLogin = $("div.contestLogin_dialog");
		var $dialog = contestLogin.dialog({
			autoOpen : false,
			width : '40%',
			title : 'Contest Login',
			close : function() {
				$(this).dialog("destroy");
			},
			buttons : {
				"確定" : function() {
					jQuery.ajax({
						type : "POST",
						url : "Contest.api",
						data : "action=doJoinContest&" + contestLogin.find("form").serialize(),
						async : false,
						timeout : 5000,
						context : this, // success 內部要使用 $(this) 要加這一行。
						success : function(result) {
							$(this).dialog("destroy");
							location.reload();
						},
						error : function(jqXHR, textStatus, errorThrown) {
							if (jqXHR.responseText !== '') {
								showError(jqXHR.responseText);
							} else {
								showError(errorThrown);
							}
						}
					});
				},
				"取消" : function() {
					$(this).dialog("destroy");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});

	function showError(error) {
		var error_dialog = $("div.error_dialog");
		error_dialog.find("h2").html(error);
		var $dialog = error_dialog.dialog({
			autoOpen : false,
			width : '40%',
			title : 'Error Message',
			close : function() {
				$(this).dialog("destroy");
			},
			buttons : {
				"返回" : function() {
					$(this).dialog("destroy");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	}
</script>

<div class="error_dialog"
	style="cursor: default; padding: 10px; text-align: center; display: none;">
	<h2></h2>
</div>


<div class="contestLogin_dialog"
	style="cursor: default; padding: 10px; text-align: center; display: none;">
	<!-- 	<div class="loginbox">
 -->
	<h2>參加競賽！</h2>
	<form>
		<table align="center">
			<tr>
				<td><div align="right">
						<fmt:message key="Login.Account" />
						：
					</div></td>
				<td><div align="left">
						<input name="account" type="text" id="account" size="15"
							maxlength="20" />
					</div></td>
			</tr>
			<tr>
				<td><div align="right">
						<fmt:message key="Login.Password" />
						：
					</div></td>
				<td><div align="left">
						<input name="passwd" type="password" id="passwd" size="15"
							maxlength="30" />
					</div></td>
			</tr>
		</table>
		<input name="contestid" value="${param.contestid}" type="hidden" />
	</form>
	<!-- 	</div>
 -->
</div>
