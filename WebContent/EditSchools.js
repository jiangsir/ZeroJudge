jQuery(document).ready(function() {
	console.log("!!!");
	$("input[name='checkid']").each(function() {
		console.log("input[name='checkid']" + $(this).data("checkid") + " == " + $(this).val());
		if ($(this).data("checkid") == $(this).val()) {
			$(this).attr("checked", "true");
		}
	});

	jQuery("span[id=deleteSchool]").click(function() {
		var schoolid = $(this).attr("schoolid");
		if (!confirm("刪除 " + schoolid)) {
			return;
		}

		jQuery.ajax({
			type : "POST",
			url : "./Manager.api?action=DeleteSchool",
			data : "schoolid=" + schoolid,
			async : false,
			timeout : 5000,
			success : function(result) {
				location.reload();
			}
		});
	});

	jQuery("button#insertSchool").click(function() {
		var checkSchool_dialog = $(this).find("div#CheckSchool_dialog");

		checkSchool_dialog.find("input#checkid").each(function() {
			if ($(this).attr("schoolcheckid") == $(this).val()) {
				$(this).attr("checked", "true");
			}
		});

		var $dialog = checkSchool_dialog.dialog({
			autoOpen : false,
			width : '60%',
			title : 'Check School',
			close : function() {
				$(this).dialog("destroy");
			},
			buttons : {
				"確定" : function() {
					jQuery.ajax({
						type : "POST",
						url : "./Manager.api?action=InsertSchool",
						data : checkSchool_dialog.find("form").serialize(),
						async : false,
						timeout : 5000,
						success : function(result) {
							location.reload();
						}
					});
					$(this).dialog("destroy");
					location.reload();
				},
				"取消" : function() {
					$(this).dialog("destroy");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});

	$('button#modal_EditSchool_save').on('click', function() {
		var modal = $(this).closest('.modal');
		var action = $(this).data('action');
		console.log("action=" + action);
		console.log("form=" + modal.find("form").serialize());

		jQuery.ajax({
			type : "POST",
			url : "./Manager.api?action=" + action,
			data : modal.find("form").serialize(),
			async : false,
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
				location.reload();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					// BootstrapDialog.alert(alert.title);
					BootstrapDialog.show({
						title : alert.type,
						message : alert.title,
						buttons : [ {
							id : 'btn-ok',
							icon : 'glyphicon glyphicon-check',
							label : 'OK',
							cssClass : 'btn-primary',
							autospin : false,
							action : function(dialogRef) {
								dialogRef.close();
							}
						} ]
					});
				} catch (err) {
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});
});
