jQuery(document).ready(function() {


	jQuery("img#UpdateVClass").click(function() {
		var index = $("img#UpdateVClass").index(this);
		var update = $("table").find("div.VClass_dialog:eq(" + index + ")");
		// alert(update.find(".form_UpdateVClass").serialize());
		var $dialog = update.dialog({
			autoOpen : false,
			width : '60%',
			title : '修改課程',
			buttons : {
				"確定" : function() {
					// alert(update.find('form').serialize());
					jQuery.ajax({
						type : "POST",
						url : "./UpdateVClass",
						data : update.find("form").serialize(),
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
