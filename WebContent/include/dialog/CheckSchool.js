jQuery(document).ready(function() {

	jQuery("img#CheckSchool").click(function() {
		// var index = $("img#CheckSchool").index(this);
		// var checkSchool_dialog = $("div#CheckSchool_dialog:eq(" + index +
		// ")");
		var checkSchool_dialog = $(this).closest("td").find("div#CheckSchool_dialog");
		// alert(checkSchool_dialog.html());

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
						url : "./UpdateSchool",
						data : checkSchool_dialog.find("form").serialize(),
						async : false,
						timeout : 5000,
						success : function(result) {
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

});
