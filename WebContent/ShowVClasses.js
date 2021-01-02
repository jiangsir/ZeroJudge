jQuery(document).ready(function() {

	$('button#modal_createVClassByTemplate').on('click', function() {
		// var modal = $("#Modal_InsertVClass");
		var modal = $(this).closest('.modal');
		console.log("form=" + modal.find('form').serialize());

		jQuery.ajax({
			type : "POST",
			url : "CreateVClassByTemplate",
			data : modal.find('form').serialize(),
			async : false,
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
				// alert("hide")
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
					console.log("catch(err)=" + err);
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});

	$("button[id='deleteTemplates']").bind("click", function(e) {
		console.log("templateid=" + $(this).data("templateid"));
		jQuery.ajax({
			type : "DELETE",
			url : 'EditVClassTemplate?id=' + $(this).data("templateid"),
			// DELETE 只能串接在 URL 上
			cache : false,
			data : "",
			timeout : 5000,
			success : function(result) {
				console.log("result=" + result);
				// window.location.href = document.referrer; // 跳轉到前一頁，並
				// reload
				location.reload();
				// BootstrapDialog.alert(result);
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
					console.log("catch(err)=" + err);
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});

});