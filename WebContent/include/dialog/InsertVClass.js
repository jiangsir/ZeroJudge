jQuery(document).ready(function() {
	jQuery("button#InsertVClass").click(function() {
		// alert($("form#InsertVClass").serialize());
		var $dialog = $("#InsertVClass_dialog").dialog({
			autoOpen : false,
			width : '60%',
			title : 'Insert VClass',
			buttons : {
				"確定" : function() {
					jQuery.ajax({
						type : "POST",
						url : "./InsertVClass",
						data : $('form#InsertVClass').serialize(),
						async : false,
						timeout : 5000,
						success : function(result) {
						}
					});
					location.reload();
					$(this).dialog("close");
				},
				"取消" : function() {
					$(this).dialog("close");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});
	jQuery("img#UpdateVClass").click(function() {
		var index = $("img#UpdateVClass").index(this);
		// alert(index);
		var update = $("div.UpdateVClass_dialog:eq(" + index + ")");
		// alert(update.find(".form_UpdateVClass").serialize());
		var $dialog = update.dialog({
			autoOpen : false,
			width : '60%',
			title : 'Update VClass',
			buttons : {
				"確定" : function() {
					// alert($('#form_InsertVClass').serialize());
					jQuery.ajax({
						type : "POST",
						url : "./UpdateVClass",
						data : update.find(".form_UpdateVClass").serialize(),
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
