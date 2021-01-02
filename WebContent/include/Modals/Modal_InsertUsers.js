jQuery(document).ready(function() {
	$('button#modal_insertUsers_change').on('click', function() {
		var modal = $(this).closest('.modal');
		console.log("form=" + modal.find('form').serialize());
		var vclassid = $(this).data("vclassid");
		console.log("vclassid=" + vclassid);
		jQuery.ajax({
			type : "POST",
			url : "./InsertUsers",
			data : modal.find('form').serialize() + "&vclassid=" + vclassid,
			// data : form.serializeArray(),
			// data : new FormData(form[0]),
			// cache : false,
			// processData : false,
			// contentType : false,
			// async : true,
			// timeout : 5000,
			// contentType:"application/x-www-form-urlencoded",
			beforeSend : function(jqXHR, settings) {
				jqXHR.url = settings.url;
			},
			success : function(data, textstatus) {
				console.log('success! textstatus=' + textstatus);
				modal.modal('hide');
				// window.location.href = document.referrer; // 跳轉到前一頁，並
				// reload
				// location.reload(); // 本頁重讀。
				// BootstrapDialog.alert("新增使用者成功。");

				var datas = jQuery.parseJSON(data);
				console.log("datas.title=" + datas.title)
				var titles = jQuery.parseJSON(datas.title);
				var message = "";
				for (var i = 0; i < titles.length; i++) {
					message += "<pre>" + titles[i] + "</pre>"
				}

				BootstrapDialog.show({
					title : '成功訊息！',
					message : message,
					buttons : [ {
						label : '關閉',
						action : function(dialogItself) {
							dialogItself.close();
							location.reload(); // 本頁重讀。
						}
					} ]
				});
				// reload
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("ajax error: jqXHR.responseText=" + jqXHR.responseText);
				console.log('ajax error: XMLHttpRequest.status=' + jqXHR.status)
				console.log("ajax error: errorThrown=" + errorThrown);
				console.log("ajax error: textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);

					// BootstrapDialog.alert(alert.title);
					console.log("jqXHR.responseText=" + jqXHR.responseText);
					checkResults = jQuery.parseJSON(alert.title);
					var message = "";
					for (var i = 0; i < checkResults.length; i++) {
						message += "<pre>" + checkResults[i].rowdata + "</pre>"
						message += "<pre>" + checkResults[i].errormsg + "</pre>"
					}
					BootstrapDialog.show({
						title : alert.type,
						message : message,
						buttons : [ {
							id : 'btn-ok',
							icon : 'glyphicon glyphicon-check',
							label : 'OK',
							cssClass : 'btn-primary',
							autospin : true,
							closeOnEscape : true,
							action : function(dialogRef) {
								dialogRef.close();
								location.reload(); // 本頁重讀。
							}
						} ]
					});
				} catch (err) {
					console.log("catch(err)=" + err);
					BootstrapDialog.alert("發生錯誤(" + err + ")！" + errorThrown + ", url=" + jqXHR.url);
				}
			}
		});

	});
});
