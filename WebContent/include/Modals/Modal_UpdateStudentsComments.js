jQuery(document).ready(function() {
	$('div#Modal_UpdateStudentsComments').on('show.bs.modal', function() {
		// do something when the modal is shown
		console.log("Modal_UpdateStudentsComments modal 開啟。");
		var vclassid = $(this).data("vclassid");
		console.log("vclassid=" + vclassid);
		textarea = $(this).find("textarea");
		console.log("isclassmode=" + $(this).data("isclassmode"));
		if ($(this).data("isclassmode") == true) {
			$.post("./Vclass.api?action=getStudentsForUpdateStudentsComments_CLASSMODE", "&vclassid=" + vclassid, function(data, status) {
				console.log("data=" + data + ", status=" + status)
				textarea.val(data);
			});
		} else {
			$.post("./Vclass.api?action=getStudentsForUpdateStudentsComments_TRAINMODE", "&vclassid=" + vclassid, function(data, status) {
				console.log("data=" + data + ", status=" + status)
				textarea.val(data);
			});
		}
	});

	$('button#submit_UpdateStudentsComments').on('click', function() {
		var modal = $(this).closest('.modal');
		console.log("form=" + modal.find('form').serialize());
		var vclassid = $(this).data("vclassid");
		console.log("vclassid=" + vclassid);
		jQuery.ajax({
			type : "POST",
			url : "./Vclass.api?action=updateStudentsComments",
			data : modal.find('form').serialize(),
			// + "&vclassid=" + vclassid
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
				console.log('success! textstatus=' + textstatus + ', data=' + data);
				modal.modal('hide');

				// var datas = jQuery.parseJSON(data);
				// console.log("datas.title="+datas.title)
				// var titles = jQuery.parseJSON(datas.title);
				// var message = "";
				// for (var i = 0; i < titles.length; i++) {
				// message += "<pre>"+titles[i]+"</pre>"
				// }

				BootstrapDialog.show({
					title : '成功訊息！',
					message : '學生註解更新成功!',
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
				// console.log("jqXHR.responseText=" +
				// jqXHR.responseText);
				console.log('XMLHttpRequest.status=' + jqXHR.status)
				console.log("ajax error: errorThrown=" + errorThrown);
				console.log("textStatus=" + textStatus);
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					console.log("alert="+alert)
					// BootstrapDialog.alert(alert.title);
					// checkResults = jQuery.parseJSON(alert.title);
					// console.log("checkResults=" + checkResults);
					// var message = "";
					// for (var i = 0; i < checkResults.length; i++) {
					// message += "<pre>"+checkResults[i].rowdata+"</pre>"
					// message += "<pre>"+checkResults[i].errormsg+"</pre>"
					// }
					BootstrapDialog.show({
						title : alert.type,
						message : alert.title,
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
