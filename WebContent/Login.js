jQuery(document).ready(function() {
	$("button[type='submit']").bind("click", function(e) {
		e.preventDefault();

		var form = $(this).closest("form");
		// var action = form.attr("action");
		var action = window.location.pathname.split('/').pop();
		console.log("action=" + action);
		console.log("form=" + form.serialize());
		// 有其他 formdata 無 <form> 寫法。
		// http://www.jianshu.com/p/46e6e03a0d53
		jQuery.ajax({
			type : "POST",
			url : action,
			// data:
			// $('#form').serialize()+"&picture="+$('input[type="file"]').val(),
			cache : false,
			// data : new FormData(form[0]),
			data : form.serialize(),
			// processData : false,
			// contentType : false,
			async : true,
			timeout : 5000,
			success : function(result) {
				console.log("returnPage=" + result);
				returnPage = jQuery.parseJSON(result);
				// window.location.href = document.referrer;
				window.location.href = returnPage.currentPage;
				// 跳轉到前一頁，並 reload

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
	
	
	$('button#modal_ForgetPassword_save').on('click', function() {
		var modal = $(this).closest('.modal');
		var action = $(this).data('action');
		console.log("action=" + action);
		console.log("form=" + modal.find("form").serialize());

		jQuery.ajax({
			type : "POST",
			url : "./"+action,
			data : modal.find("form").serialize(),
			timeout : 5000,
			success : function(result) {
				modal.modal('hide');
				//location.reload();
			},
			error : function(jqXHR, textStatus, errorThrown) {
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
