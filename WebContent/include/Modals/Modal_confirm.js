jQuery(document).ready(function() {
	jQuery("[data-target=#Modal_confirm]").click(function(event) {
		var title = $(this).data("title");
		var content = $(this).data("content");
		var type = $(this).data("type");
		var url = $(this).data("url");
		var qs = $(this).data("qs");
		console.log(content + ": " + type + ":" + url + "?" + qs);
		BootstrapDialog.confirm({
			title : title,
			message : content,
			type : BootstrapDialog.TYPE_PRIMARY, // <-- Default value is
			// BootstrapDialog.TYPE_PRIMARY
			closable : true, // <-- Default value is false
			draggable : true, // <-- Default value is false
			btnCancelLabel : '取消', // <-- Default value is
			// 'Cancel',
			btnOKLabel : '確定', // <-- Default value is 'OK',
			callback : function(result) {
				// result will be true if button was click, while it will be
				// false if users close the dialog directly.
				if (result) {
					jQuery.ajax({
						type : type,
						url : url,
						data : qs,
						cache : false,
						//timeout : 10000,
						success : function() {
							location.reload();
						},
						error : function(jqXHR, textStatus, errorThrown) {
							console.log("jqXHR.responseText=" + jqXHR.responseText);
							console.log("errorThrown=" + errorThrown);
							console.log("textStatus=" + textStatus);
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
					}); // jQuery ajax;

				}
			}
		});
	});

});
