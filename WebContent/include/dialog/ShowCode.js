jQuery(document).ready(function() {
	jQuery("a.showcode").click(function() {
		var index = jQuery("a.showcode").index(this);
		var $dialog = $("div.showcode_dialog:eq(" + index + ")").dialog({
			autoOpen : false,
			width : '60%',
			title : '顯示原始碼',
			close : function() {
				$(this).dialog("destroy"); // dialog("close")
			},
			buttons : {
				"返回" : function() {
					$(this).dialog("destroy");
				}
			}
		});
		$dialog.dialog('open');
		return false;
	});

});
