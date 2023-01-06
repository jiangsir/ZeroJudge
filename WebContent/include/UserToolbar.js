jQuery(document).ready(function() {
	$('button#rebuiltUserStatistic').on('click', function() {
		var userid = $(this).data("userid");
		var contextPath = $(this).data("contextpath");
		console.log(contextPath)
		jQuery.ajax({
			type : "POST",
			url : contextPath + "/User.api",
			data : "action=doRebuiltUserStatistic&userid=" + userid,
			async : false,
			timeout : 5000,
			success : function(result) {
				// location.reload();
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
		});
	});

	$('button#deleteUser').on('click', function() {
		var userid = $(this).data("userid");
		var message = $(this).data("message");
		var contextPath = $(this).data("contextpath");

		BootstrapDialog.confirm({
			title : "刪除使用者",
			message : message,
			type : BootstrapDialog.TYPE_PRIMARY, // <-- Default value is
			// BootstrapDialog.TYPE_PRIMARY
			closable : true, // <-- Default value is false
			draggable : true, // <-- Default value is false
			btnCancelLabel : '取消', // <-- Default value is
			// 'Cancel',
			btnOKLabel : '確定刪除', // <-- Default value is 'OK',
			callback : function(result) {
				// result will be true if button was click, while it will be
				// false if users close the dialog directly.
				if (result) {
					console.log(contextPath)
					jQuery.ajax({
						type : "POST",
						url : contextPath + "/User.api",
						data : "action=deleteUser&userid=" + userid,
						async : false,
						timeout : 5000,
						success : function(result) {
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
					});
				}
			}
		});
	});

});
