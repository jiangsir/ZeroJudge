jQuery(document).ready(function() {
	jQuery("input[name='manualjudge_verdict']").click(function() {
		$("input[name='manualjudge_score']").focus();
	});
	jQuery("input[name='manualjudge_verdict']").click(function() {
		$("#manualjudge_hint textarea").val($(this).data("hint"));
		//$("#manualjudge_hint").show();
	});

	jQuery("button#save_ManualJudge").click(function(e) {
		e.preventDefault();
		var modal = $(this).closest(".modal");
		var form = modal.find("form");
		console.log("formdata=" + form.serialize());
		jQuery.ajax({
			type : "POST",
			url : "./Solution.api",
			data : "action=setServerOutput&" + form.serialize(),
			// async : false,
			timeout : 5000,
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
			},
			success : function(result) {
				console.log("result=" + result);
				// var redirect = JSON.parse(result);
				// window.location.href = redirect.uri;
				location.reload();
			}
		});
	});

});
