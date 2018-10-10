jQuery(document).ready(function() {
	jQuery("#InsertUsers").click(function() {
		var $dialog = $("#InsertUsers_dialog").dialog({
			autoOpen : false,
			width : '80%',
			title : '批次新增使用者',
			buttons : {
				"新增/更新" : function() {
					var scripts = $("#userscripts").val();
					// alert("userscripts="+scripts);
					jQuery.ajax({
						type : "POST",
						url : "./InsertUsers",
						data : "scripts=" + scripts,
						async : false,
						timeout : 5000,
						success : function(result) {
							location.reload();
							$(this).dialog("close");
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
					$(this).dialog("close");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});

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
