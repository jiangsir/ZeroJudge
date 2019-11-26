jQuery(document).ready(function() {
	$('button#modal_insert').on('click', function() {
		// var modal = $("#Modal_InsertVClass");
		var modal = $(this).closest('.modal');
		var vclassname = modal.find('[name=vclassname]').val();
		var descript = modal.find('[name=descript]').val();
		var action = $(this).data("action");
		console.log("action=" + action);
		console.log("vclassname=" + vclassname);
		console.log("descript=" + descript);
		jQuery.ajax({
			type : "POST",
			url : "./InsertVClass",
			data : modal.find('form').serialize(),
			async : false,
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
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
	});
	$('button#modal_update').on('click', function() {
		// var modal = $("#Modal_InsertVClass");
		var modal = $(this).closest('.modal');
		var vclassname = modal.find('[name=vclassname]').val();
		var descript = modal.find('[name=descript]').val();
		var action = $(this).data("action");
		console.log("action=" + action);
		console.log("vclassname=" + vclassname);
		console.log("descript=" + descript);
		console.log("form=" + modal.find('form').serialize());

		jQuery.ajax({
			type : "POST",
			url : "./UpdateVClass",
			data : modal.find('form').serialize(),
			async : false,
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
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
	});

	$('button#modal_saveVClass').on('click', function() {
		// var modal = $("#Modal_InsertVClass");
		var modal = $(this).closest('.modal');
		var action = $(this).data("action");
		console.log("action=" + action);
		console.log("form=" + modal.find('form').serialize());

		jQuery.ajax({
			type : "POST",
			url : "./" + action,
			data : modal.find('form').serialize(),
			async : false,
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
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
	});


});