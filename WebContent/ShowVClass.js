jQuery(document).ready(function() {
	$("#belong").children().each(function() {
		// alert("$(this).val()="+$(this).val()+"vclassid="+$(this).attr("vclassid"));
		if ($(this).val() == $(this).attr("vclassid")) {
			// jQuery給法
			$(this).attr("selected", "true"); // 或是給selected也可
		}
	});

	// jQuery("span[id='InsertStudents'] a").click(function() {
	// var $dialog = $("#InsertStudents_dialog").dialog({
	// autoOpen : false,
	// width : '60%',
	// title : '將使用者加入本課程',
	// buttons : {
	// "加入" : function() {
	// var scripts = $("#scripts").val();
	// var vclassid = $("#scripts").attr("vclassid");
	// jQuery.ajax({
	// type : "POST",
	// url : "./InsertStudents",
	// data : "vclassid=" + vclassid + "&scripts=" + scripts,
	// async : false,
	// timeout : 5000,
	// success : function(result) {
	// }
	// });
	// location.reload();
	// $(this).dialog("close");
	// },
	// "取消" : function() {
	// $(this).dialog("close");
	// }
	// }
	// });
	// $dialog.dialog('open');
	// return false;
	// });

	$('button#Modal_InsertStudents_confirm').on('click', function() {
		// var modal = $("#Modal_InsertVClass");
		var modal = $(this).closest('.modal');
		// var action = $(this).data("action");
		// console.log("action=" + action);
		console.log("form=" + modal.find('form').serialize());

		jQuery.ajax({
			type : "POST",
			url : "./InsertStudents",
			data : modal.find('form').serialize(),
			// async : false,
			timeout : 5000,
			success : function(result) {
				console.log("result=" + result);
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
					console.log("err=" + err);
					BootstrapDialog.alert(errorThrown);
				}
			}
		});
	});

	jQuery("button[id='InsertVContest']").click(function() {
		jQuery.ajax({
			type : "POST",
			url : "./InsertVContest",
			data : "vclassid=" + $(this).data("vclassid"),
			timeout : 5000,
			success : function(result) {
				location.reload();
			}
		});
	});

	jQuery("button[id='RemoveStudent']").click(function() {
		console.log("ssss");
		jQuery.ajax({
			type : "POST",
			url : "./Vclass.api?action=RemoveStudent",
			data : "vclassid=" + $(this).data("vclassid") + "&userid=" + $(this).data("userid"),
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
