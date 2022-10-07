jQuery(document).ready(function() {
	jQuery(document).on("click", "#modal_setUserPassword_submit", function(event) {
		event.preventDefault();
		var userid = $(this).data("userid");
		var contextPath = $(this).data("contextpath");
		var modal = $(this).closest(".modal");
		var form = modal.find("form");
		console.log(contextPath+"/UnbindGoogle");
		console.log("form.serialize()="+form.serialize());
		jQuery.ajax({
			type : "GET",
			url : contextPath+"/UnbindGoogle",
			data : form.serialize(),
			//dataType : 'json',
			cache : false,
			timeout : 5000,
			success : function(data) {
				console.log(contextPath+"/UnbindGoogle "+"success:"+data);
				modal.modal('hide');
				location.reload();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				console.log("errorThrown=" + errorThrown);
				console.log("textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					//console.log("alert=" + alert);
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



	jQuery(document).on("click", "#Modal_JoinVclassid_submit", function(event) {
		console.log("click");
		event.preventDefault();
		//var vclasscode = $(this).data("vclasscode");
		var contextPath = $(this).data("contextpath");
		console.log("path="+$(this).data("contextpath"))
		console.log("abc="+$(this).data("abc"))
		console.log('contextPath='+contextPath);
		var modal = $(this).closest(".modal");
		var form = modal.find("form");
		jQuery.ajax({
			type : "POST",
			url : contextPath+"/Vclass.api?action=JoinVclassByVclasscode",
			//url : "./Vclass.api?action=JoinVclassByVclasscode",
			data : form.serialize(),
			//dataType : 'json',
			cache : false,
			timeout : 5000,
			success : function(data) {
				console.log("redirect:"+data);
				modal.modal('hide');
				//location.reload();
				window.location.href = data;
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR.responseText=" + jqXHR.responseText);
				console.log("errorThrown=" + errorThrown);
				console.log("textStatus=" + textStatus);
				try {
					alert = jQuery.parseJSON(jqXHR.responseText);
					//console.log("alert=" + alert);
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

