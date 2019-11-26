jQuery(document).ready(
		function() {
			jQuery("img#manualjudge").click(
					function() {
						var index = $("img#manualjudge").index(this);
						// var img = $("div.manualjudge_dialog:eq(" + index +
						// ")");
						var manualjudge = $("div.manualjudge_dialog:eq("
								+ index + ")");
						var $dialog = manualjudge.dialog({
							autoOpen : false,
							width : '60%',
							title : '評審手動評分',
							close : function(event, ui) {
								$(this).dialog("destroy");
							},
							buttons : {
								"確定" : function() {
									jQuery.ajax({
										type : "POST",
										url : "./Solution.api",
										data : "action=setServerOutput&"
												+ manualjudge.find(
														'#manualjudge_form')
														.serialize(),
										async : false,
										timeout : 5000,
										success : function(result) {
											location.reload();
										}
									});
									$(this).dialog("destroy");
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
