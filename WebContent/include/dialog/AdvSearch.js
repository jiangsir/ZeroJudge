jQuery(document).ready(function() {
	jQuery("span[id='advsearch']").click(function() {
		var advsearch_dialog = $("#advsearch_dialog");

		var $dialog = advsearch_dialog.dialog({
			autoOpen : false,
			width : '60%',
			title : '進階搜尋',
			buttons : {
				"搜尋" : function() {
					$(this).dialog("close");
					$("form[name='advsearch_form']").submit();
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
