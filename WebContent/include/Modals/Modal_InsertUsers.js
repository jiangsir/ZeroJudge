jQuery(document).ready(function() {
	$('button#modal_insertUsers_change').on('click', function() {
		var modal = $(this).closest('.modal');
		var text = modal.find('textarea').val();
		console.log("text=" + text);

		jQuery.ajax({
			type : "POST",
			url : "./InsertUsers",
			data : "scripts=" + text,
			// data : form.serializeArray(),
			// data : new FormData(form[0]),
			cache : false,
			// processData : false,
			// contentType : false,
			async : true,
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
				// console.log(result);
				// window.location.href = document.referrer; // 跳轉到前一頁，並
				// reload
				// location.reload(); // 本頁重讀。
				BootstrapDialog.alert("新增使用者成功。");
				// reload
				//location.reload(); // 本頁重讀。
			},
			beforeSend : function(jqXHR, settings) {
				jqXHR.url = settings.url;
			},
			error : function(jqXHR, textStatus, errorThrown) {
				// console.log("jqXHR.responseText=" +
				// jqXHR.responseText);
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
					BootstrapDialog.alert(errorThrown + ", url=" + jqXHR.url);
				}
			}
		});

	});
});
