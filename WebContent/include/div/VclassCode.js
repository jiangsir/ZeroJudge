jQuery(document).ready(function() {
	jQuery("button[id='RenewVclassCode']").click(function() {
		jQuery.ajax({
			type : "POST",
			url : "./Vclass.api?action=RenewVclasscode",
			data : "vclassid=" + $(this).data("vclassid"),
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
					console.log("err=" + err);
					BootstrapDialog.alert(errorThrown);
				}
			}

		});
	});
});